package io.github.ron1196.circleofcraft.event;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.command.ModCommands;
import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.data.PlayerData;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.entity.RugEntity;
import io.github.ron1196.circleofcraft.entity.hostile.HyenaEntity;
import io.github.ron1196.circleofcraft.entity.hostile.SkeletalHyenaEntity;
import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import io.github.ron1196.circleofcraft.entity.npc.ZiraEntity;
import io.github.ron1196.circleofcraft.item.GroundRhinoHornItem;
import io.github.ron1196.circleofcraft.item.ZebraBootsItem;
import io.github.ron1196.circleofcraft.network.LoginSyncPacket;
import io.github.ron1196.circleofcraft.quest.actions.OutlandsQuestActions;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.registry.*;
import io.github.ron1196.circleofcraft.util.LevelHelper;
import io.github.ron1196.circleofcraft.world.dimension.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModForgeEvents {

    // ── Outsand Lightning Constants ──────────────────────────────────────────
    /**
     * Average ticks between dry lightning strikes (~6 seconds at 20 tps). 120
     */
    private static final int DRY_LIGHTNING_CHANCE = 120;
    /**
     * How far from a player the lightning can strike (blocks in each axis).
     */
    private static final int DRY_LIGHTNING_RANGE = 100;
    /**
     * Min radius of the outsand patch (inclusive).
     */
    private static final int OUTSAND_MIN_RADIUS = 2;
    /**
     * Max radius of the outsand patch (inclusive).
     */
    private static final int OUTSAND_MAX_RADIUS = 5;
    /**
     * Vertical range above/below strike point to convert sand.
     */
    private static final int OUTSAND_VERTICAL_RANGE = 3;
    /**
     * 1-in-N chance for fire on air blocks above outsand.
     */
    private static final int OUTSAND_FIRE_CHANCE = 8;

    // ── Pumbaa Box Recovery ─────────────────────────────────────────────────
    /**
     * Ticks between recovery checks for a stuck PUMBAA_BOX_EXPLODING stage (~0.5 seconds).
     * Gives the PumbaaExplosionEntity time to register before we assume it's missing.
     */
    private static final int EXPLODING_RECOVERY_INTERVAL = 10;

    // ── Commands ──────────────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    // ── Player Login — sync world state and quest data to client ───────────────

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ServerLevel overworld = serverPlayer.server.overworld();
            WorldData worldData = WorldData.get(overworld);
            PlayerData playerData = PlayerData.get(serverPlayer);
            PacketDistributor.sendToPlayer(serverPlayer, LoginSyncPacket.of(worldData, playerData));
        }
    }

    // ── RightClickBlock — Hoe on sand creates tilled sand ─────────────────────

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        ItemStack held = event.getItemStack();
        if (!(held.getItem() instanceof net.minecraft.world.item.HoeItem)) return;
        if (!tryTillSand(event.getLevel(), event.getPos())) return;

        event.getLevel()
                .playSound(
                        null,
                        event.getPos(),
                        net.minecraft.sounds.SoundEvents.HOE_TILL,
                        net.minecraft.sounds.SoundSource.BLOCKS,
                        1.0F,
                        1.0F);

        if (!event.getEntity().getAbilities().instabuild) {
            held.hurtAndBreak(
                    1, event.getEntity(), net.minecraft.world.entity.LivingEntity.getSlotForHand(event.getHand()));
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /**
     * Tills a sand block to tilled sand if and only if the block is plain sand and there is air
     * directly above (mirroring vanilla farmland's air-above check). Returns true on conversion.
     * Exposed for game tests so they can exercise the mutation without staging a player + event.
     */
    public static boolean tryTillSand(net.minecraft.world.level.Level level, BlockPos pos) {
        if (!level.getBlockState(pos).is(Blocks.SAND)) return false;
        if (!level.getBlockState(pos.above()).isAir()) return false;
        level.setBlock(pos, ModBlocks.TILLED_SAND.get().defaultBlockState(), 11);
        return true;
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

    // ── LivingIncomingDamageEvent (pre-reduction cancels) ─────────────────────────

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();

        // Peacock boots negate fall damage
        if (event.getSource().is(DamageTypes.FALL)) {
            ItemStack boots = target.getItemBySlot(EquipmentSlot.FEET);
            if (boots.is(ModItems.PEACOCK_BOOTS.get())) {
                event.setCanceled(true);
            }
        }
    }

    // ── LivingDamageEvent.Pre (post-reduction bonus damage) ───────────────────────

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        // Scourge of Hyenas enchantment bonus damage
        if (attacker instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            int scourgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    target.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.SCOURGE_OF_HYENAS),
                    weapon);

            if (scourgeLevel > 0 && (target instanceof HyenaEntity || target instanceof SkeletalHyenaEntity)) {
                event.setNewDamage(event.getNewDamage() + 2.5F * scourgeLevel);
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
                    entity.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING),
                    player.getMainHandItem());

            float dropChance = 0.05F + 0.03F * lootingLevel;
            if (entity.level().random.nextFloat() < dropChance) {
                entity.spawnAtLocation(new ItemStack(ModItems.HYENA_HEAD_ITEM.get()));
                ModCriteriaTriggers.BEHEAD_HYENA.trigger((ServerPlayer) player);
            }
        }

        // Scar/Zira kill triggers
        if (entity instanceof ScarEntity && killer instanceof ServerPlayer serverPlayer) {
            ModCriteriaTriggers.KILL_SCAR.trigger(serverPlayer);
        }
        if (entity instanceof ZiraEntity && killer instanceof ServerPlayer serverPlayer) {
            ModCriteriaTriggers.KILL_ZIRA.trigger(serverPlayer);
        }
    }

    // ── Respawn redirect — dying in Outlands/Upendi respawns in Pride Lands ────

    /**
     * Tracks which players died in a mod dimension so we can redirect after respawn.
     */
    private static final java.util.Set<java.util.UUID> DIED_IN_MOD_DIMENSION = new java.util.HashSet<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        boolean isModDimension = player.level().dimension() == Dimensions.OUTLANDS_LEVEL
                || player.level().dimension() == Dimensions.UPENDI_LEVEL;
        if (!isModDimension) return;

        // Only mark for redirect if the player has no bed/respawn anchor anywhere
        if (player.getRespawnPosition() == null) {
            DIED_IN_MOD_DIMENSION.add(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.isEndConquered()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!DIED_IN_MOD_DIMENSION.remove(player.getUUID())) return;

        ServerLevel prideLands = player.server.getLevel(Dimensions.PRIDE_LANDS_LEVEL);
        if (prideLands == null) return;

        BlockPos spawn = prideLands.getSharedSpawnPos();
        int y = LevelHelper.surfaceY(prideLands, spawn.getX(), spawn.getZ()) + 1;
        player.teleportTo(prideLands, spawn.getX() + 0.5, y, spawn.getZ() + 0.5, player.getYRot(), player.getXRot());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel from = player.server.getLevel(event.getFrom());
        if (from == null) return;
        ServerLevel to = player.serverLevel();

        for (SimbaEntity simba : from.getEntities(
                EntityTypeTest.forClass(SimbaEntity.class),
                s -> s.isAlive() && player.getUUID().equals(s.getOwnerUUID()) && s.hasCharm() && !s.isOrderedToSit())) {
            simba.changeDimension(new DimensionTransition(
                    to,
                    player.position(),
                    Vec3.ZERO,
                    simba.getYRot(),
                    simba.getXRot(),
                    DimensionTransition.DO_NOTHING));
        }
    }

    // ── PlayerTickEvent.Post ─────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (!serverPlayer.isAlive()) return;

        ZebraBootsItem.tickWear(serverPlayer);

        PlayerData playerData = PlayerData.get(serverPlayer);

        // Dimension entry triggers (fire once per player)
        if (serverPlayer.level().dimension() == Dimensions.PRIDE_LANDS_LEVEL && !playerData.hasEnteredPrideLands()) {
            playerData.setEnteredPrideLands(true);
            ModCriteriaTriggers.ENTER_PRIDE_LANDS.trigger(serverPlayer);
        } else if (serverPlayer.level().dimension() == Dimensions.OUTLANDS_LEVEL && !playerData.hasEnteredOutlands()) {
            playerData.setEnteredOutlands(true);
            ModCriteriaTriggers.ENTER_OUTLANDS.trigger(serverPlayer);
        } else if (serverPlayer.level().dimension() == Dimensions.UPENDI_LEVEL && !playerData.hasEnteredUpendi()) {
            playerData.setEnteredUpendi(true);
            ModCriteriaTriggers.ENTER_UPENDI.trigger(serverPlayer);
        }

        if (serverPlayer.level().dimension() == Dimensions.OUTLANDS_LEVEL) {
            WorldData.get(serverPlayer.serverLevel())
                    .getQuestManager()
                    .tryAdvance(OutlandsQuestline.QUEST_ID, serverPlayer, QuestTrigger.ENTER_OUTLANDS);
        } else if (serverPlayer.level().dimension() == Dimensions.PRIDE_LANDS_LEVEL) {
            WorldData.get(serverPlayer.serverLevel())
                    .getQuestManager()
                    .tryAdvance(OutlandsQuestline.QUEST_ID, serverPlayer, QuestTrigger.ENTER_PRIDE_LANDS);
        }
    }

    // ── LevelTickEvent.Post ──────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
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
            WorldData outlandsData = WorldData.get(serverLevel);
            QuestlineManager questManager = outlandsData.getQuestManager();
            OutlandsQuestline.Stage stage =
                    questManager.getStage(OutlandsQuestline.QUEST_ID, OutlandsQuestline.Stage.class);
            if (stage == OutlandsQuestline.Stage.ZIRA_RETURNS) {
                OutlandsQuestActions.ensureHostileZira(serverLevel);
            }
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
     * Exposed for game tests so they can call it directly without staging a real Outlands chunk.
     */
    public static void convertSandToOutsand(ServerLevel level, BlockPos center) {
        int radius = level.random.nextInt(OUTSAND_MAX_RADIUS - OUTSAND_MIN_RADIUS + 1) + OUTSAND_MIN_RADIUS;
        int radiusSq = radius * radius;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radiusSq) continue;
                for (int dy = -OUTSAND_VERTICAL_RANGE; dy <= OUTSAND_VERTICAL_RANGE; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (!level.getBlockState(pos).is(Blocks.SAND)) continue;

                    level.setBlock(pos, ModBlocks.OUTSAND.get().defaultBlockState(), 3);

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
                            section.setBlockState(x, y, z, Blocks.LAVA.defaultBlockState(), false);
                        }
                    }
                }
            }
        }
    }
}
