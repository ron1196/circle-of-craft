package io.github.ron1196.thelionking.event;

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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
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

  // ── Portal interception ───────────────────────────────────────────────────

  @SubscribeEvent
  public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;
    if (handledPortalTeleports.contains(player.getUUID())) return;

    PortalResult result = resolvePortal(player.level(), player.blockPosition());
    if (result == null) return;

    event.setCanceled(true);

    ServerLevel destLevel = player.server.getLevel(result.destination());
    if (destLevel == null) return;

    handledPortalTeleports.add(player.getUUID());
    try {
      player.changeDimension(destLevel, new Teleporter(result.portalBlock()));
    } finally {
      handledPortalTeleports.remove(player.getUUID());
    }
  }

  // Returns the portal destination for the player's current position, or null if not in a portal.
  // Each portal has a "home" dimension: standing inside it sends you to OVERWORLD, otherwise home.
  private static PortalResult resolvePortal(Level level, BlockPos pos) {
    Map<Block, ResourceKey<Level>> portalHomes =
        Map.of(
            LionKingBlocks.OUTLANDS_PORTAL.get(), Dimensions.OUTLANDS_LEVEL,
            LionKingBlocks.PRIDE_LANDS_PORTAL.get(), Dimensions.PRIDE_LANDS_LEVEL);

    for (Map.Entry<Block, ResourceKey<Level>> entry : portalHomes.entrySet()) {
      if (isInBlock(level, pos, entry.getKey())) {
        ResourceKey<Level> home = entry.getValue();
        ResourceKey<Level> destination = level.dimension().equals(home) ? Level.OVERWORLD : home;
        return new PortalResult(destination, entry.getKey());
      }
    }
    return null;
  }

  private record PortalResult(ResourceKey<Level> destination, Block portalBlock) {}

  private static boolean isInBlock(Level level, BlockPos pos, Block block) {
    return level.getBlockState(pos).is(block) || level.getBlockState(pos.above()).is(block);
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
