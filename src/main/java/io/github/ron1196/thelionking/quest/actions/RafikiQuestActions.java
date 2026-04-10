package io.github.ron1196.thelionking.quest.actions;

import io.github.ron1196.thelionking.block.PortalBlock;
import io.github.ron1196.thelionking.block.ZiraMoundGateBlock;
import io.github.ron1196.thelionking.entity.npc.ScarEntity;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Idempotent helper that ensures the world state matches the current Rafiki quest stage.
 * Every {@code ensure*} method checks before acting, so calling {@link #ensureWorldState}
 * repeatedly is safe and cheap when the world is already correct.
 */
public final class RafikiQuestActions {

    private RafikiQuestActions() {}

    // ── Constants ───────────────────────────────────────────────────────────

    private static final int SCAR_SEARCH_RANGE = 250;
    private static final int CAVE_SEARCH_RADIUS = 80;
    private static final int CAVE_MIN_DISTANCE = 40;
    private static final int CAVE_MIN_Y = 10;
    private static final int CAVE_MAX_Y = 40;
    private static final int CAVE_SEARCH_ATTEMPTS = 200;
    private static final int SCAR_FALLBACK_DISTANCE = 30;
    private static final int GATE_SEARCH_RADIUS = 30;
    private static final int RAFIKI_TREE_SEARCH_RADIUS = 50;

    // ── Stage groups ────────────────────────────────────────────────────────

    /** Stages where Rafiki should exist but Scar should not. Early quest. */
    private static final Set<Stage> EARLY_STAGES =
            EnumSet.of(Stage.FIND_RAFIKI, Stage.CRAFT_RAFIKI_STICK, Stage.RALLY_PUMBAA, Stage.COLLECT_BONES);

    /** Stages after Scar is defeated: no Scar, gate broken, portal active. */
    private static final Set<Stage> POST_SCAR_STAGES =
            EnumSet.of(Stage.COLLECT_TERMITES, Stage.COLLECT_MANGOES, Stage.USE_STAR_ALTAR, Stage.COMPLETE);

    // ── Public entry point ──────────────────────────────────────────────────

    /**
     * Ensures the world state matches the given Rafiki quest stage. Each branch is idempotent.
     */
    public static void ensureWorldState(ServerLevel level, Stage stage) {
        if (EARLY_STAGES.contains(stage)) {
            ensureNoScar(level);
            return;
        }

        if (stage == Stage.DEFEAT_SCAR) {
            ensureScarExists(level);
            return;
        }

        if (POST_SCAR_STAGES.contains(stage)) {
            ensureNoScar(level);
            ensureOutlandsPortalOpen(level);
            return;
        }
    }

    // ── Private idempotent helpers ──────────────────────────────────────────

    /**
     * Discards any {@link ScarEntity} in the level.
     */
    private static void ensureNoScar(ServerLevel level) {
        for (ScarEntity scar : level.getEntities(EntityTypes.SCAR.get(), e -> e.isAlive())) {
            scar.discard();
        }
    }

    /**
     * Ensures at least one {@link ScarEntity} exists in the level. If none exists
     * (e.g. chunk unload or server restart), spawns one in a cave near the nearest player.
     * This is a recovery spawn -- no lightning VFX.
     */
    private static void ensureScarExists(ServerLevel level) {
        List<? extends ScarEntity> scars = level.getEntities(EntityTypes.SCAR.get(), e -> e.isAlive());
        if (!scars.isEmpty()) return;
        if (level.players().isEmpty()) return;

        Player player = level.players().get(0);
        BlockPos spawnPos = findCaveSpawn(level, player.blockPosition());
        if (spawnPos == null) {
            // Fallback: spawn on surface nearby
            int x = Mth.floor(player.getX()) + SCAR_FALLBACK_DISTANCE;
            int z = Mth.floor(player.getZ()) + SCAR_FALLBACK_DISTANCE;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            spawnPos = new BlockPos(x, y, z);
        }

        ScarEntity scar = EntityTypes.SCAR.get().create(level);
        if (scar == null) return;

        scar.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0F, 0F);
        scar.setPersistenceRequired();
        level.addFreshEntity(scar);
    }

    /**
     * Searches for a cave spawn position: air block with air above and solid below,
     * within {@link #CAVE_SEARCH_RADIUS} blocks of the center but at least
     * {@link #CAVE_MIN_DISTANCE} blocks away, between Y {@link #CAVE_MIN_Y} and
     * {@link #CAVE_MAX_Y}.
     *
     * @return a valid cave position, or {@code null} if none found
     */
    private static BlockPos findCaveSpawn(ServerLevel level, BlockPos center) {
        for (int attempt = 0; attempt < CAVE_SEARCH_ATTEMPTS; attempt++) {
            int dx = level.random.nextInt(CAVE_SEARCH_RADIUS * 2) - CAVE_SEARCH_RADIUS;
            int dz = level.random.nextInt(CAVE_SEARCH_RADIUS * 2) - CAVE_SEARCH_RADIUS;

            // Enforce minimum distance from center
            if (Math.abs(dx) < CAVE_MIN_DISTANCE && Math.abs(dz) < CAVE_MIN_DISTANCE) {
                continue;
            }

            int x = center.getX() + dx;
            int z = center.getZ() + dz;
            int y = CAVE_MIN_Y + level.random.nextInt(CAVE_MAX_Y - CAVE_MIN_Y);

            BlockPos pos = new BlockPos(x, y, z);
            if (level.getBlockState(pos).isAir()
                    && level.getBlockState(pos.above()).isAir()
                    && level.getBlockState(pos.below()).isSolid()) {
                return pos;
            }
        }
        return null;
    }

    /**
     * Breaks any gate blocks near players and lights the portal in the cleared space.
     * These always happen together — the gate seals the portal entrance.
     */
    private static void ensureOutlandsPortalOpen(ServerLevel level) {
        Block gateBlock = LionKingBlocks.ZIRA_MOUND_GATE.get();
        PortalBlock portalBlock = (PortalBlock) LionKingBlocks.OUTLANDS_PORTAL.get();

        for (Player player : level.players()) {
            BlockPos center = player.blockPosition();

            // Break any remaining gate blocks
            BlockPos gatePos = findNearbyBlock(level, center, gateBlock, GATE_SEARCH_RADIUS);
            if (gatePos != null) {
                breakGateChain(level, gatePos, gateBlock);
            }

            // Light the portal if not already active
            if (findNearbyBlock(level, center, LionKingBlocks.OUTLANDS_PORTAL.get(), GATE_SEARCH_RADIUS) == null) {
                activateNearbyPortal(level, center, portalBlock);
            }
        }
    }

    // ── Block scanning helpers ──────────────────────────────────────────────

    /**
     * Finds the first occurrence of {@code target} block within {@code radius} of {@code center}.
     */
    private static BlockPos findNearbyBlock(Level level, BlockPos center, Block target, int radius) {
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            if (level.getBlockState(pos).is(target)) {
                return pos.immutable();
            }
        }
        return null;
    }

    /**
     * Recursively destroys all connected {@link ZiraMoundGateBlock} blocks starting
     * from the given position.
     */
    private static void breakGateChain(Level level, BlockPos pos, Block gateBlock) {
        if (!level.getBlockState(pos).is(gateBlock)) return;

        level.destroyBlock(pos, false);

        for (Direction dir : Direction.values()) {
            breakGateChain(level, pos.relative(dir), gateBlock);
        }
    }

    /**
     * Scans for a Rafiki wood block within {@link #RAFIKI_TREE_SEARCH_RADIUS} of the given
     * center position. Returns the position of the first one found, or {@code null} if none.
     */
    private static BlockPos findRafikiTreeNear(ServerLevel level, BlockPos center) {
        return findNearbyBlock(level, center, LionKingBlocks.RAFIKI_WOOD.get(), RAFIKI_TREE_SEARCH_RADIUS);
    }

    /**
     * Tries to activate the Outlands portal by testing every air block within
     * {@link #PORTAL_SEARCH_RADIUS} of the given center.
     */
    private static void activateNearbyPortal(ServerLevel level, BlockPos center, PortalBlock portalBlock) {
        int r = GATE_SEARCH_RADIUS;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -r, -r), center.offset(r, r, r))) {
            if (level.getBlockState(pos).isAir()) {
                if (portalBlock.trySpawnPortal(level, pos)) {
                    return;
                }
            }
        }
    }
}
