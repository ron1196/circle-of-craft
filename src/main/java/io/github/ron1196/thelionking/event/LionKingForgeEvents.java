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
import io.github.ron1196.thelionking.item.GroundRhinoHornItem;
import io.github.ron1196.thelionking.network.LoginSyncPacket;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.registry.Enchantments;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LionKingForgeEvents {

    // ── Outsand Lightning Constants ──────────────────────────────────────────
    /** Average ticks between dry lightning strikes (~6 seconds at 20 tps). 120*/
    private static final int DRY_LIGHTNING_CHANCE = 120;
    /** How far from a player the lightning can strike (blocks in each axis). */
    private static final int DRY_LIGHTNING_RANGE = 100;
    /** Min radius of the outsand patch (inclusive). */
    private static final int OUTSAND_MIN_RADIUS = 2;
    /** Max radius of the outsand patch (inclusive). */
    private static final int OUTSAND_MAX_RADIUS = 5;
    /** Vertical range above/below strike point to convert sand. */
    private static final int OUTSAND_VERTICAL_RANGE = 3;
    /** 1-in-N chance for fire on air blocks above outsand. */
    private static final int OUTSAND_FIRE_CHANCE = 8;

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
                    PacketDistributor.PLAYER.with(() -> serverPlayer), new LoginSyncPacket(worldData, playerData));
        }
    }

    // ── EntityInteract — Ground Rhino Horn intercepts before mobInteract() ──────

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack held = event.getItemStack();

        if (!(held.getItem() instanceof GroundRhinoHornItem)) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        if (animal.isBaby()) return;

        InteractionResult result = GroundRhinoHornItem.tryBreed(animal, event.getEntity(), held);
        event.setCancellationResult(result);
        event.setCanceled(true);
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
            int scourgeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SCOURGE_OF_HYENAS.get(), weapon);

            if (scourgeLevel > 0 && (target instanceof HyenaEntity || target instanceof SkeletalHyenaEntity)) {
                event.setAmount(event.getAmount() + 2.5F * scourgeLevel);
            }
        }

        // Peacock boots negate fall damage
        if (event.getSource().is(DamageTypes.FALL)) {
            ItemStack boots = target.getItemBySlot(EquipmentSlot.FEET);
            if (boots.is(LionKingItems.PEACOCK_BOOTS.get())) {
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
            int lootingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    net.minecraft.world.item.enchantment.Enchantments.MOB_LOOTING, player.getMainHandItem());

            float dropChance = 0.05F + 0.03F * lootingLevel;
            if (entity.level().random.nextFloat() < dropChance) {
                entity.spawnAtLocation(new ItemStack(LionKingItems.HYENA_HEAD_ITEM.get()));
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
        if (serverPlayer.level().dimension() == Dimensions.PRIDE_LANDS_LEVEL && !playerData.hasEnteredPrideLands()) {
            playerData.setEnteredPrideLands(true);
            LionKingCriteriaTriggers.ENTER_PRIDE_LANDS.trigger(serverPlayer);
        } else if (serverPlayer.level().dimension() == Dimensions.OUTLANDS_LEVEL && !playerData.hasEnteredOutlands()) {
            playerData.setEnteredOutlands(true);
            LionKingCriteriaTriggers.ENTER_OUTLANDS.trigger(serverPlayer);
        } else if (serverPlayer.level().dimension() == Dimensions.UPENDI_LEVEL && !playerData.hasEnteredUpendi()) {
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

        // Outlands-specific tick events
        if (serverLevel.dimension() == Dimensions.OUTLANDS_LEVEL) {
            handleZiraSpawnEvent(serverLevel);
            handleDryLightning(serverLevel);
        }
    }

    // ── EntityJoinLevelEvent ─────────────────────────────────────────────────

    /**
     * When any lightning bolt lands in the Outlands, convert nearby sand to outsand.
     * Works for dry lightning, channeling tridents, commands, etc.
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LightningBolt bolt)) return;
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (serverLevel.dimension() != Dimensions.OUTLANDS_LEVEL) return;
        convertSandToOutsand(serverLevel, bolt.blockPosition());
    }

    /**
     * Converts sand blocks in a circular patch around the strike point to outsand.
     * Occasionally sets fire on air blocks above. Matches the old mod's LKWorldGenOutsand.
     */
    private static void convertSandToOutsand(ServerLevel level, BlockPos center) {
        int radius = level.random.nextInt(OUTSAND_MAX_RADIUS - OUTSAND_MIN_RADIUS + 1) + OUTSAND_MIN_RADIUS;
        int radiusSq = radius * radius;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radiusSq) continue;
                for (int dy = -OUTSAND_VERTICAL_RANGE; dy <= OUTSAND_VERTICAL_RANGE; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (!level.getBlockState(pos).is(Blocks.SAND)) continue;

                    level.setBlock(pos, LionKingBlocks.OUTSAND.get().defaultBlockState(), 3);

                    BlockPos above = pos.above();
                    if (level.random.nextInt(OUTSAND_FIRE_CHANCE) == 0 && level.isEmptyBlock(above)) {
                        level.setBlock(above, BaseFireBlock.getState(level, above), 3);
                    }
                }
            }
        }
    }

    /**
     * Periodically spawns "dry lightning" in the Outlands near a random player.
     * The lightning is visual-only (no vanilla fire/damage) — the outsand it creates
     * is the hazard. Conversion is handled by {@link #onEntityJoinLevel}.
     */
    private static void handleDryLightning(ServerLevel level) {
        if (level.players().isEmpty()) return;
        if (level.random.nextInt(DRY_LIGHTNING_CHANCE) != 0) return;

        // Pick a random player
        Player player = level.players().get(level.random.nextInt(level.players().size()));

        int px = Mth.floor(player.getX());
        int pz = Mth.floor(player.getZ());

        int strikeX = px + level.random.nextInt(DRY_LIGHTNING_RANGE * 2 + 1) - DRY_LIGHTNING_RANGE;
        int strikeZ = pz + level.random.nextInt(DRY_LIGHTNING_RANGE * 2 + 1) - DRY_LIGHTNING_RANGE;
        int strikeY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, strikeX, strikeZ);

        // Must be a surface block that can see the sky
        BlockPos strikePos = new BlockPos(strikeX, strikeY, strikeZ);
        if (!level.canSeeSky(strikePos)) return;

        // Must have sand at or near the surface to be worth striking
        if (!hasSandNearSurface(level, strikePos)) return;

        // Spawn a visual-only vanilla lightning bolt — outsand conversion is event-driven
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(strikeX + 0.5, strikeY, strikeZ + 0.5);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
    }

    private static boolean hasSandNearSurface(ServerLevel level, BlockPos surface) {
        for (int dy = -OUTSAND_VERTICAL_RANGE; dy <= OUTSAND_VERTICAL_RANGE; dy++) {
            if (level.getBlockState(surface.offset(0, dy, 0)).is(Blocks.SAND)) {
                return true;
            }
        }
        return false;
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
        int surfaceY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(px, 0, pz))
                .getY();
        if (!level.canSeeSky(new BlockPos(px, py, pz)) || py != surfaceY) return;

        // Spawn Zira at a random nearby position
        int spawnX = px - 8 + level.random.nextInt(17);
        int spawnZ = pz - 8 + level.random.nextInt(17);
        int spawnY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(spawnX, 0, spawnZ))
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

    // ── Outlands Water → Lava Replacement ──────────────────────────────────
    // The vanilla aquifer system has a hardcoded lava short-circuit that
    // floods all caves when default_fluid is lava. To work around this,
    // we keep default_fluid as water (so caves stay dry like overworld)
    // and replace water with lava after chunk generation in the Outlands.

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!serverLevel.dimension().equals(Dimensions.OUTLANDS_LEVEL)) {
            return;
        }
        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }

        int minY = chunk.getMinBuildHeight();
        LevelChunkSection[] sections = chunk.getSections();
        for (int sectionIdx = 0; sectionIdx < sections.length; sectionIdx++) {
            LevelChunkSection section = sections[sectionIdx];
            if (section == null || !section.maybeHas(state -> state.is(Blocks.WATER))) {
                continue;
            }
            int sectionY = minY + (sectionIdx << 4);
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.getFluidState().is(Fluids.WATER)
                                || state.getFluidState().is(Fluids.FLOWING_WATER)) {
                            section.setBlockState(
                                    x, y, z,
                                    Blocks.LAVA.defaultBlockState(),
                                    false);
                        }
                    }
                }
            }
        }
    }
}
