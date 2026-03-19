package io.github.ron1196.thelionking.event;

import com.google.common.base.Suppliers;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.command.LionKingCommands;
import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.PlayerDataProvider;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.entity.hostile.HyenaEntity;
import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaEntity;
import io.github.ron1196.thelionking.entity.npc.ScarEntity;
import io.github.ron1196.thelionking.entity.npc.ZiraEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.network.LoginSyncPacket;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.registry.Enchantments;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import io.github.ron1196.thelionking.world.dimension.Teleporter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LionKingForgeEvents {

  // Guard against re-entrancy: when we call changeDimension ourselves inside the
  // event handler, NeoForge fires EntityTravelToDimensionEvent a second time —
  // we let that second call through so the teleport actually completes.
  private static final Set<UUID> handledPortalTeleports =
      Collections.newSetFromMap(new ConcurrentHashMap<>());

  // Tracks which portal block type each player is standing in, updated every tick by
  // PortalBlock.entityInside. Using the Block directly avoids any position lookup ambiguity.
  public static final Map<UUID, Block> PORTAL_BLOCK_CACHE = new ConcurrentHashMap<>();

  // Saves each player's position before they enter a portal, keyed by (UUID, source dimension),
  // so the return trip can drop them back where they came from.
  private record ReturnKey(UUID uuid, ResourceKey<Level> dimension) {}

  private record SavedPosition(Vec3 pos, float yaw, float xRot) {}

  private static final Map<ReturnKey, SavedPosition> RETURN_POSITIONS = new ConcurrentHashMap<>();

  private static final Supplier<Map<Block, ResourceKey<Level>>> PORTALS =
      Suppliers.memoize(
          () ->
              Map.of(
                  LionKingBlocks.OUTLANDS_PORTAL.get(), Dimensions.OUTLANDS_LEVEL,
                  LionKingBlocks.PRIDE_LANDS_PORTAL.get(), Dimensions.PRIDE_LANDS_LEVEL));

  // ── Portal interception ───────────────────────────────────────────────────

  @SubscribeEvent
  public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;
    if (handledPortalTeleports.contains(player.getUUID())) return;

    // Look up the portal block type cached by PortalBlock.entityInside each tick.
    // This avoids any world block lookup, which can fail due to hitbox/grid misalignment.
    Block portalBlock = PORTAL_BLOCK_CACHE.get(player.getUUID());
    if (portalBlock == null) return;

    ResourceKey<Level> home = PORTALS.get().get(portalBlock);
    if (home == null) return;

    event.setCanceled(true);

    ResourceKey<Level> destination =
        player.level().dimension().equals(home) ? Level.OVERWORLD : home;
    ServerLevel destLevel = player.server.getLevel(destination);
    if (destLevel == null) return;

    // Check for a saved return position in the destination dimension.
    ReturnKey returnKey = new ReturnKey(player.getUUID(), destination);
    SavedPosition savedPos = RETURN_POSITIONS.remove(returnKey);

    // Save the player's current position so the return trip can bring them back here.
    RETURN_POSITIONS.put(
        new ReturnKey(player.getUUID(), player.level().dimension()),
        new SavedPosition(player.position(), player.getYRot(), player.getXRot()));

    Teleporter teleporter =
        savedPos != null
            ? Teleporter.returning(savedPos.pos(), savedPos.yaw(), savedPos.xRot())
            : new Teleporter(portalBlock);

    handledPortalTeleports.add(player.getUUID());
    try {
      player.changeDimension(destLevel, teleporter);
    } finally {
      handledPortalTeleports.remove(player.getUUID());
      PORTAL_BLOCK_CACHE.remove(player.getUUID());
    }
  }

  // ── Commands ──────────────────────────────────────────────────────────────

  @SubscribeEvent
  public static void onRegisterCommands(RegisterCommandsEvent event) {
    LionKingCommands.register(event.getDispatcher());
  }

  // ── Player Login — sync world state and quest data to client ───────────────

  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer serverPlayer) {
      ServerLevel overworld = serverPlayer.server.overworld();
      WorldData worldData = WorldData.get(overworld);
      PlayerData playerData = PlayerDataProvider.get(serverPlayer);
      Networking.CHANNEL.send(
          PacketDistributor.PLAYER.with(() -> serverPlayer),
          new LoginSyncPacket(worldData, playerData));
    }
  }

  // ── AttackEntityEvent (punch Scar Rug to pick it up) ───────────────────────

  @SubscribeEvent
  public static void onAttackEntity(AttackEntityEvent event) {
    if (event.getTarget() instanceof RugEntity rug) {
      rug.dropAsItem();
    }
  }

  // ── LivingHurtEvent ─────────────────────────────────────────────────────────

  @SubscribeEvent
  public static void onLivingHurt(LivingHurtEvent event) {
    LivingEntity target = event.getEntity();
    Entity attacker = event.getSource().getEntity();

    // Scourge of Hyenas enchantment bonus damage
    if (attacker instanceof Player player) {
      ItemStack weapon = player.getMainHandItem();
      int scourgeLevel =
          EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SCOURGE_OF_HYENAS.get(), weapon);

      if (scourgeLevel > 0
          && (target instanceof HyenaEntity || target instanceof SkeletalHyenaEntity)) {
        event.setAmount(event.getAmount() + 2.5F * scourgeLevel);
      }
    }

    // Peacock boots negate fall damage
    if (event.getSource().is(DamageTypes.FALL)) {
      ItemStack boots = target.getItemBySlot(EquipmentSlot.FEET);
      if (boots.is(Items.PEACOCK_BOOTS.get())) {
        event.setCanceled(true);
      }
    }
  }

  // ── LivingDeathEvent ────────────────────────────────────────────────────────

  @SubscribeEvent
  public static void onLivingDeath(LivingDeathEvent event) {
    LivingEntity entity = event.getEntity();
    Entity killer = event.getSource().getEntity();

    // Hyena special drop: hyena head with looting
    if (entity instanceof HyenaEntity && killer instanceof Player player) {
      int lootingLevel =
          EnchantmentHelper.getItemEnchantmentLevel(
              net.minecraft.world.item.enchantment.Enchantments.MOB_LOOTING,
              player.getMainHandItem());

      float dropChance = 0.05F + 0.03F * lootingLevel;
      if (entity.level().random.nextFloat() < dropChance) {
        entity.spawnAtLocation(new ItemStack(Items.HYENA_HEAD_ITEM.get()));
        LionKingCriteriaTriggers.BEHEAD_HYENA.trigger((ServerPlayer) player);
      }
    }

    // Scar/Zira kill triggers
    if (entity instanceof ScarEntity && killer instanceof ServerPlayer serverPlayer) {
      LionKingCriteriaTriggers.KILL_SCAR.trigger(serverPlayer);
    }
    if (entity instanceof ZiraEntity && killer instanceof ServerPlayer serverPlayer) {
      LionKingCriteriaTriggers.KILL_ZIRA.trigger(serverPlayer);
    }
  }

  // ── TickEvent.PlayerTickEvent ────────────────────────────────────────────────

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }
    if (event.player.level().isClientSide()) {
      return;
    }

    if (!(event.player instanceof ServerPlayer serverPlayer)) return;
    if (!serverPlayer.isAlive()) return;

    PlayerData playerData = PlayerDataProvider.get(serverPlayer);

    // Dimension entry triggers (fire once per player)
    if (serverPlayer.level().dimension() == Dimensions.PRIDE_LANDS_LEVEL
        && !playerData.hasEnteredPrideLands()) {
      playerData.setEnteredPrideLands(true);
      LionKingCriteriaTriggers.ENTER_PRIDE_LANDS.trigger(serverPlayer);
    } else if (serverPlayer.level().dimension() == Dimensions.OUTLANDS_LEVEL
        && !playerData.hasEnteredOutlands()) {
      playerData.setEnteredOutlands(true);
      LionKingCriteriaTriggers.ENTER_OUTLANDS.trigger(serverPlayer);
    } else if (serverPlayer.level().dimension() == Dimensions.UPENDI_LEVEL
        && !playerData.hasEnteredUpendi()) {
      playerData.setEnteredUpendi(true);
      LionKingCriteriaTriggers.ENTER_UPENDI.trigger(serverPlayer);
    }
  }

  // ── TickEvent.LevelTickEvent ────────────────────────────────────────────────

  @SubscribeEvent
  public static void onLevelTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }
    if (!(event.level instanceof ServerLevel serverLevel)) {
      return;
    }

    // Save level data every 100 ticks if dirty
    if (serverLevel.getGameTime() % 100 == 0) {
      WorldData data = WorldData.get(serverLevel);
      if (data.isDirty()) {
        data.setDirty();
      }
    }

    // Outlands: Zira stage 22 — spawn Zira with dramatic lightning when player is on surface
    if (serverLevel.dimension() == Dimensions.OUTLANDS_LEVEL) {
      handleZiraSpawnEvent(serverLevel);
    }
  }

  /**
   * When ziraStage == 22 and a player is on the surface of the Outlands, spawn Zira nearby with a
   * visual lightning bolt.
   */
  private static void handleZiraSpawnEvent(ServerLevel level) {
    WorldData data = WorldData.get(level);
    if (data.ziraStage != 22) return;
    if (level.players().isEmpty()) return;

    Player player = level.players().get(0);
    int px = Mth.floor(player.getX());
    int py = Mth.floor(player.getBoundingBox().minY);
    int pz = Mth.floor(player.getZ());

    // Player must be on the surface (can see sky and at heightmap level)
    int surfaceY =
        level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(px, 0, pz)).getY();
    if (!level.canSeeSky(new BlockPos(px, py, pz)) || py != surfaceY) return;

    // Spawn Zira at a random nearby position
    int spawnX = px - 8 + level.random.nextInt(17);
    int spawnZ = pz - 8 + level.random.nextInt(17);
    int spawnY =
        level
            .getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(spawnX, 0, spawnZ))
            .getY();

    ZiraEntity zira = EntityTypes.ZIRA.get().create(level);
    if (zira != null) {
      zira.moveTo(spawnX, spawnY, spawnZ, 0.0F, 0.0F);
      zira.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ(), 10.0F, 40.0F);
      level.addFreshEntity(zira);

      // Visual lightning bolt at Zira's spawn position
      level.addFreshEntity(new LightningBoltEntity(level, spawnX, spawnY, spawnZ, 0, player));

      data.ziraStage = 23;
      data.setDirty();
    }
  }
}
