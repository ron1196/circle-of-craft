package io.github.ron1196.circleofcraft.quest.actions;

import io.github.ron1196.circleofcraft.block.PoolCoverBlock;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.entity.PumbaaExplosionEntity;
import io.github.ron1196.circleofcraft.entity.hostile.OutlanderEntity;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import io.github.ron1196.circleofcraft.entity.npc.RafikiEntity;
import io.github.ron1196.circleofcraft.entity.npc.ZiraEntity;
import io.github.ron1196.circleofcraft.entity.projectile.LightningBoltEntity;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline.Stage;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

/**
 * Idempotent helper that ensures the world state matches the current Outlands quest stage.
 * Every {@code ensure*} method checks before acting, so calling {@link #ensureWorldState}
 * repeatedly is safe and cheap when the world is already correct.
 */
public final class OutlandsQuestActions {

    private OutlandsQuestActions() {}

    // ── Constants ───────────────────────────────────────────────────────────

    private static final int OUTLANDER_ESCORT_COUNT = 6;
    private static final int MIN_OUTLANDERS = 3;
    private static final int TREE_SEARCH_RADIUS = 50;
    private static final int POOL_SEARCH_RADIUS = 30;
    private static final int OUTLANDER_SEARCH_RADIUS = 30;
    private static final int OUTLANDER_DISCARD_RADIUS = 64;

    // ── Stage groups ────────────────────────────────────────────────────────

    /** Stages where pool covers should remain (placed by worldgen). */
    private static final Set<Stage> POOL_COVER_STAGES =
            EnumSet.of(Stage.ENTER_OUTLANDS, Stage.FIND_ZIRA, Stage.COLLECT_INGOTS);

    /** Stages where pool covers must be destroyed. */
    private static final Set<Stage> POOL_OPEN_STAGES = EnumSet.of(Stage.THROW_IN_OUTWATER, Stage.COLLECT_FEATHERS);

    /** Stages where Zira occupies Rafiki's tree with an Outlander escort. */
    private static final Set<Stage> TREE_OCCUPATION_STAGES = EnumSet.of(
            Stage.ZIRA_OCCUPIES_TREE,
            Stage.TALK_TO_PUMBAA,
            Stage.GATHER_PUMBAA_INGREDIENTS,
            Stage.USE_PUMBAA_BOX,
            Stage.PUMBAA_BOX_EXPLODING);

    /** Stages where Rafiki should be at his tree and it should not be corrupt. */
    private static final Set<Stage> RAFIKI_RESTORED_STAGES = EnumSet.of(
            Stage.RAFIKI_RETURNS, Stage.ZIRA_RETURNS, Stage.DEFEAT_TERMITE_QUEEN, Stage.DEFEAT_ZIRA, Stage.COMPLETE);

    // ── Public entry point ──────────────────────────────────────────────────

    /** Returns true if the given stage is a tree-occupation stage. */
    public static boolean isTreeOccupationStage(Stage stage) {
        return TREE_OCCUPATION_STAGES.contains(stage);
    }

    /**
     * Ensures the world state matches the given Outlands quest stage. Each branch is idempotent.
     */
    public static void ensureWorldState(ServerLevel level, Stage stage) {
        if (POOL_COVER_STAGES.contains(stage)) {
            // Pool covers exist from worldgen; no tree corruption; no action needed.
            return;
        }

        if (POOL_OPEN_STAGES.contains(stage)) {
            ensurePoolCoversRemoved(level);
            return;
        }

        if (stage == Stage.FOLLOW_OUTLANDERS) {
            ensureNoNonHostileZira(level);
            ensureNoOutlandersNearPlayers(level);
            return;
        }

        if (TREE_OCCUPATION_STAGES.contains(stage)) {
            ensureTreeOccupation(level);
            if (stage == Stage.PUMBAA_BOX_EXPLODING) {
                ensurePumbaaExplosionProgress(level);
            }
            return;
        }

        if (RAFIKI_RESTORED_STAGES.contains(stage)) {
            ensureRafikiRestored(level);
            return;
        }
    }

    /**
     * Handles the hostile Zira spawn event in the Outlands dimension.
     * Called separately from {@link #ensureWorldState} because it is Outlands-dimension specific
     * and involves player interaction (lightning bolt, quest advancement).
     */
    public static void ensureHostileZira(ServerLevel outlandsLevel) {
        WorldData data = WorldData.get(outlandsLevel);
        QuestlineManager qm = data.getQuestManager();
        Stage stage = qm.getStage(OutlandsQuestline.QUEST_ID, Stage.class);
        if (stage != Stage.ZIRA_RETURNS) return;
        if (outlandsLevel.players().isEmpty()) return;

        Player player = outlandsLevel.players().get(0);
        int px = Mth.floor(player.getX());
        int py = Mth.floor(player.getBoundingBox().minY);
        int pz = Mth.floor(player.getZ());

        // Player must be on the surface
        int surfaceY = outlandsLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, px, pz);
        if (!outlandsLevel.canSeeSky(new BlockPos(px, py, pz)) || py != surfaceY) return;

        // Spawn Zira in front of the player, 10-12 blocks away
        float yawRad = (float) Math.toRadians(player.getYRot());
        int distance = 10 + outlandsLevel.random.nextInt(3);
        int spawnX = px - Mth.floor(Math.sin(yawRad) * distance);
        int spawnZ = pz + Mth.floor(Math.cos(yawRad) * distance);
        int spawnY = outlandsLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, spawnX, spawnZ);

        ZiraEntity zira = EntityTypes.ZIRA.get().create(outlandsLevel);
        if (zira == null) return;

        zira.moveTo(spawnX + 0.5, spawnY, spawnZ + 0.5, player.getYRot() + 180, 0.0F);
        zira.setHostile(true);
        outlandsLevel.addFreshEntity(zira);

        // Spawn Termite Queen and mount Zira on it
        spawnQueenAndMount(outlandsLevel, zira, spawnX + 0.5, spawnY, spawnZ + 0.5);

        // Visual lightning bolt at Zira's spawn position
        outlandsLevel.addFreshEntity(new LightningBoltEntity(outlandsLevel, spawnX, spawnY, spawnZ, 0, player));

        ChatHelper.broadcastNpcMessage(
                outlandsLevel,
                "Zira",
                "TRAITOR! You dare betray the Outsiders?! Now you will face the wrath of the Termite Queen! Scar, give me strength!");

        if (player instanceof ServerPlayer sp) {
            sp.sendSystemMessage(Component.literal("\u00a7c\u00a7lZira has returned!"));
            qm.tryAdvance(OutlandsQuestline.QUEST_ID, sp, QuestTrigger.ZIRA_SPAWN_EVENT);
        }
    }

    // ── Private idempotent helpers ──────────────────────────────────────────

    /**
     * Destroys any {@link PoolCoverBlock} within {@link #POOL_SEARCH_RADIUS} of each player.
     */
    private static void ensurePoolCoversRemoved(ServerLevel level) {
        for (Player player : level.players()) {
            BlockPos center = player.blockPosition();
            for (BlockPos pos : BlockPos.betweenClosed(
                    center.offset(-POOL_SEARCH_RADIUS, -POOL_SEARCH_RADIUS, -POOL_SEARCH_RADIUS),
                    center.offset(POOL_SEARCH_RADIUS, POOL_SEARCH_RADIUS, POOL_SEARCH_RADIUS))) {
                if (level.getBlockState(pos).getBlock() instanceof PoolCoverBlock) {
                    level.destroyBlock(pos, false);
                }
            }
        }
    }

    /**
     * Discards any non-hostile {@link ZiraEntity} in the level.
     */
    private static void ensureNoNonHostileZira(ServerLevel level) {
        for (ZiraEntity zira : level.getEntities(EntityTypes.ZIRA.get(), e -> e.isAlive() && !e.isHostile())) {
            zira.discard();
        }
    }

    /**
     * Discards any {@link OutlanderEntity} within {@link #OUTLANDER_DISCARD_RADIUS} of any player.
     */
    private static void ensureNoOutlandersNearPlayers(ServerLevel level) {
        for (Player player : level.players()) {
            AABB box = player.getBoundingBox().inflate(OUTLANDER_DISCARD_RADIUS);
            for (OutlanderEntity outlander : level.getEntitiesOfClass(OutlanderEntity.class, box)) {
                outlander.discard();
            }
        }
    }

    /**
     * Ensures the tree-occupation invariants hold:
     * <ul>
     *   <li>No Rafiki entities exist (discarded)</li>
     *   <li>Non-hostile Zira exists near each Rafiki tree (spawned if missing)</li>
     *   <li>At least {@link #MIN_OUTLANDERS} Outlanders near Zira (topped up to
     *       {@link #OUTLANDER_ESCORT_COUNT} if below minimum)</li>
     *   <li>Tree blocks are corrupt</li>
     * </ul>
     */
    private static void ensureTreeOccupation(ServerLevel level) {
        for (Player player : level.players()) {
            // Find Rafiki entities near the player — use their position for Zira spawn
            AABB searchArea = player.getBoundingBox().inflate(TREE_SEARCH_RADIUS);
            List<RafikiEntity> rafikis = level.getEntitiesOfClass(RafikiEntity.class, searchArea);

            // Use Rafiki's position (he stands on top of tree), or fall back to tree block scan
            BlockPos spawnPos = null;
            for (RafikiEntity rafiki : rafikis) {
                spawnPos = rafiki.blockPosition();
                rafiki.discard();
            }

            // Check if Zira already exists nearby
            List<ZiraEntity> ziras =
                    level.getEntitiesOfClass(ZiraEntity.class, searchArea, e -> e.isAlive() && !e.isHostile());
            if (ziras.isEmpty()) {
                if (spawnPos == null) {
                    // No Rafiki found — fall back to tree block scan
                    spawnPos = findRafikiTreeTop(level, player.blockPosition());
                }
                if (spawnPos != null) {
                    spawnNonHostileZira(level, spawnPos);
                }
            }

            // Use Zira's position (or spawn pos) for tree corruption and escort
            BlockPos treeCenter = spawnPos;
            if (treeCenter == null && !ziras.isEmpty()) {
                treeCenter = ziras.get(0).blockPosition();
            }
            if (treeCenter == null) {
                treeCenter = findRafikiTreeNear(level, player.blockPosition());
            }
            if (treeCenter == null) continue;

            ensureOutlanderEscort(level, treeCenter);
            OutlandsQuestline.setTreeCorruption(level, treeCenter, true);
        }
    }

    /**
     * Ensures the Rafiki-restored invariants hold:
     * <ul>
     *   <li>No non-hostile Zira in Pride Lands (discarded)</li>
     *   <li>No Outlanders near Rafiki trees (discarded)</li>
     *   <li>Rafiki exists at tree (spawned if missing)</li>
     *   <li>Tree blocks are NOT corrupt</li>
     * </ul>
     */
    private static void ensureRafikiRestored(ServerLevel level) {
        for (Player player : level.players()) {
            AABB searchArea = player.getBoundingBox().inflate(TREE_SEARCH_RADIUS);

            // Find Zira entities — use their position for Rafiki spawn
            BlockPos spawnPos = null;
            for (ZiraEntity zira :
                    level.getEntitiesOfClass(ZiraEntity.class, searchArea, e -> e.isAlive() && !e.isHostile())) {
                spawnPos = zira.blockPosition();
                zira.discard();
            }

            // Discard Outlanders near the player
            for (OutlanderEntity outlander : level.getEntitiesOfClass(OutlanderEntity.class, searchArea)) {
                outlander.discard();
            }

            // Ensure Rafiki exists
            List<RafikiEntity> rafikis = level.getEntitiesOfClass(RafikiEntity.class, searchArea);
            if (rafikis.isEmpty()) {
                if (spawnPos == null) {
                    spawnPos = findRafikiTreeTop(level, player.blockPosition());
                }
                if (spawnPos != null) {
                    spawnRafiki(level, spawnPos);
                }
            }

            // Find a reference point for tree un-corruption
            BlockPos treeCenter = spawnPos;
            if (treeCenter == null && !rafikis.isEmpty()) {
                treeCenter = rafikis.get(0).blockPosition();
            }
            if (treeCenter == null) {
                treeCenter = findRafikiTreeNear(level, player.blockPosition());
            }
            if (treeCenter != null) {
                OutlandsQuestline.setTreeCorruption(level, treeCenter, false);
            }
        }
    }

    /**
     * If the stage is {@link Stage#PUMBAA_BOX_EXPLODING} and no {@link PumbaaExplosionEntity}
     * exists, the explosion sequence may have been lost (e.g. server restart). Advance the quest
     * past the stuck state.
     */
    private static void ensurePumbaaExplosionProgress(ServerLevel level) {
        List<? extends PumbaaExplosionEntity> explosions =
                level.getEntities(EntityTypes.PUMBAA_EXPLOSION.get(), Entity::isAlive);
        if (!explosions.isEmpty()) return;

        // No explosion entity exists -- the sequence was lost. Advance the quest for any player.
        for (ServerPlayer player : level.players()) {
            WorldData data = WorldData.get(level);
            QuestlineManager qm = data.getQuestManager();
            if (qm.tryAdvance(OutlandsQuestline.QUEST_ID, player, QuestTrigger.EXPLOSIONS_DONE)) {
                break; // Only need to advance once
            }
        }
    }

    // ── Entity spawning helpers ─────────────────────────────────────────────

    /**
     * Spawns a Termite Queen at the given position and mounts Zira on it.
     * The queen leaps upward dramatically on spawn.
     */
    public static void spawnQueenAndMount(ServerLevel level, ZiraEntity zira, double x, double y, double z) {
        TermiteQueenEntity queen = EntityTypes.TERMITE_QUEEN.get().create(level);
        if (queen == null) return;
        queen.moveTo(x, y, z, 0.0F, 0.0F);
        level.addFreshEntity(queen);
        zira.startRiding(queen);
        queen.setDeltaMovement(0, 1.0, 0);
    }

    private static void spawnNonHostileZira(ServerLevel level, BlockPos pos) {
        ZiraEntity zira = EntityTypes.ZIRA.get().create(level);
        if (zira == null) return;
        zira.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360F, 0F);
        zira.setHostile(false);
        zira.setPersistenceRequired();
        level.addFreshEntity(zira);
    }

    private static void spawnRafiki(ServerLevel level, BlockPos pos) {
        RafikiEntity rafiki = EntityTypes.RAFIKI.get().create(level);
        if (rafiki == null) return;
        rafiki.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360F, 0F);
        rafiki.setPersistenceRequired();
        level.addFreshEntity(rafiki);
    }

    /**
     * Ensures at least {@link #MIN_OUTLANDERS} exist within {@link #OUTLANDER_SEARCH_RADIUS}
     * of the tree. If fewer are found, spawns up to {@link #OUTLANDER_ESCORT_COUNT} total.
     */
    private static void ensureOutlanderEscort(ServerLevel level, BlockPos treePos) {
        AABB escortArea = new AABB(treePos).inflate(OUTLANDER_SEARCH_RADIUS);
        List<OutlanderEntity> existing = level.getEntitiesOfClass(OutlanderEntity.class, escortArea);
        if (existing.size() >= MIN_OUTLANDERS) return;

        int toSpawn = OUTLANDER_ESCORT_COUNT - existing.size();
        for (int i = 0; i < toSpawn; i++) {
            OutlanderEntity outlander = EntityTypes.OUTLANDER.get().create(level);
            if (outlander == null) continue;
            double x = treePos.getX() + 0.5 + level.random.nextGaussian() * 2;
            double z = treePos.getZ() + 0.5 + level.random.nextGaussian() * 2;
            outlander.moveTo(x, treePos.getY(), z, level.random.nextFloat() * 360F, 0F);
            outlander.setPersistenceRequired();
            level.addFreshEntity(outlander);
        }
    }

    // ── Block scanning helpers ──────────────────────────────────────────────

    /**
     * Scans for a Rafiki wood block within {@link #TREE_SEARCH_RADIUS} of the given center.
     * Returns the position of the first one found (any height), or {@code null}.
     */
    private static BlockPos findRafikiTreeNear(ServerLevel level, BlockPos center) {
        int r = TREE_SEARCH_RADIUS;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -r, -r), center.offset(r, r, r))) {
            if (level.getBlockState(pos).is(ModBlocks.RAFIKI_WOOD.get())) {
                return pos.immutable();
            }
        }
        return null;
    }

    /**
     * Finds the top of the nearest Rafiki tree — the position above the highest wood block.
     * This is where entities (Rafiki/Zira) should stand.
     */
    private static BlockPos findRafikiTreeTop(ServerLevel level, BlockPos center) {
        BlockPos anyWood = findRafikiTreeNear(level, center);
        if (anyWood == null) return null;

        BlockPos top = anyWood;
        for (int dy = 1; dy < 30; dy++) {
            BlockPos above = anyWood.above(dy);
            if (level.getBlockState(above).is(ModBlocks.RAFIKI_WOOD.get())) {
                top = above;
            }
        }
        return top.above();
    }
}
