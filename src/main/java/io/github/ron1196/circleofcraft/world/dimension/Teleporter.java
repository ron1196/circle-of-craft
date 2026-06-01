package io.github.ron1196.circleofcraft.world.dimension;

import io.github.ron1196.circleofcraft.block.PortalBlock;
import io.github.ron1196.circleofcraft.util.LevelHelper;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class Teleporter implements ITeleporter {

    private static final int SEARCH_RADIUS_CHUNKS = 8;
    private static final int FRAME_WIDTH = 4;
    private static final int FRAME_HEIGHT = 5;
    private static final int FALLBACK_SURFACE_Y = 70;

    private final PortalBlock portalBlock;

    public Teleporter(PortalBlock portalBlock) {
        this.portalBlock = portalBlock;
    }

    // ── ITeleporter overrides ─────────────────────────────────────────────────

    @Nullable
    @Override
    public PortalInfo getPortalInfo(
            Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        BlockPos destPos = findOrCreatePortal(entity.blockPosition(), destWorld);
        return new PortalInfo(
                new Vec3(destPos.getX() + 0.5, destPos.getY(), destPos.getZ() + 0.5),
                Vec3.ZERO,
                entity.getYRot(),
                entity.getXRot());
    }

    @Override
    public Entity placeEntity(
            Entity entity,
            ServerLevel currentWorld,
            ServerLevel destWorld,
            float yaw,
            Function<Boolean, Entity> repositionEntity) {
        return repositionEntity.apply(false);
    }

    // ── Portal lookup ─────────────────────────────────────────────────────────

    private BlockPos findOrCreatePortal(BlockPos entityPos, ServerLevel destWorld) {
        BlockPos existing = findExistingPortal(destWorld, entityPos);
        if (existing != null) return existing;
        return createPortal(destWorld, entityPos);
    }

    @Nullable
    private BlockPos findExistingPortal(ServerLevel level, BlockPos center) {
        loadChunksAround(level, center);
        return findClosestPortalBlock(level, center);
    }

    private void loadChunksAround(ServerLevel level, BlockPos center) {
        ChunkPos centerChunk = new ChunkPos(center);
        for (int cx = -SEARCH_RADIUS_CHUNKS; cx <= SEARCH_RADIUS_CHUNKS; cx++) {
            for (int cz = -SEARCH_RADIUS_CHUNKS; cz <= SEARCH_RADIUS_CHUNKS; cz++) {
                level.getChunk(centerChunk.x + cx, centerChunk.z + cz, ChunkStatus.FULL, true);
            }
        }
    }

    @Nullable
    private BlockPos findClosestPortalBlock(ServerLevel level, BlockPos center) {
        int range = SEARCH_RADIUS_CHUNKS * 16;
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                int checkX = center.getX() + x;
                int checkZ = center.getZ() + z;

                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockPos pos = new BlockPos(checkX, y, checkZ);
                    if (!level.getBlockState(pos).is(portalBlock)) continue;

                    double dist = pos.distSqr(center);
                    if (!(dist < bestDist)) {
                        continue;
                    }

                    bestDist = dist;
                    best = pos;
                }
            }
        }

        return best;
    }

    // ── Portal creation ───────────────────────────────────────────────────────

    private BlockPos createPortal(ServerLevel level, BlockPos pos) {
        BlockPos base = findSuitableSurface(level, pos);
        Block frameBlock = portalBlock.getFrameBlock();

        clearSurroundings(level, base);
        placeGround(level, base, frameBlock);
        placeFrame(level, base, frameBlock);
        clearInterior(level, base);
        lightPortal(level, base);

        // Return position inside the portal (1 block in, 1 block up)
        return base.offset(1, 1, 0);
    }

    private BlockPos findSuitableSurface(ServerLevel level, BlockPos pos) {
        int surfaceY = LevelHelper.surfaceY(level, pos.getX(), pos.getZ(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
        if (surfaceY > level.getMinBuildHeight() + 1) {
            return new BlockPos(pos.getX(), surfaceY, pos.getZ());
        }

        // Heightmap returned bottom — fall back to world spawn
        BlockPos spawn = level.getSharedSpawnPos();
        surfaceY = LevelHelper.surfaceY(level, spawn.getX(), spawn.getZ(), Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
        if (surfaceY <= level.getMinBuildHeight() + 1) {
            surfaceY = FALLBACK_SURFACE_Y;
        }

        return new BlockPos(spawn.getX(), surfaceY, spawn.getZ());
    }

    /**
     * Builds a 4-wide x 5-tall frame (no interior fill):
     * <pre>
     *  F F F F
     *  F . . F
     *  F . . F
     *  F . . F
     *  F F F F
     * </pre>
     */
    private void placeFrame(ServerLevel level, BlockPos base, Block frameBlock) {
        BlockState frame = frameBlock.defaultBlockState();
        for (int dx = 0; dx < FRAME_WIDTH; dx++) {
            for (int dy = 0; dy < FRAME_HEIGHT; dy++) {
                boolean isEdge = dx == 0 || dx == FRAME_WIDTH - 1 || dy == 0 || dy == FRAME_HEIGHT - 1;
                if (!isEdge) continue;
                level.setBlockAndUpdate(base.offset(dx, dy, 0), frame);
            }
        }
    }

    private void lightPortal(ServerLevel level, BlockPos base) {
        // Interior bottom-left is at (1, 1) relative to base
        BlockPos interiorPos = base.offset(1, 1, 0);
        portalBlock.trySpawnPortal(level, interiorPos);
    }

    private void clearInterior(ServerLevel level, BlockPos base) {
        for (int dx = 1; dx < FRAME_WIDTH - 1; dx++) {
            for (int dy = 1; dy < FRAME_HEIGHT - 1; dy++) {
                BlockPos p = base.offset(dx, dy, 0);
                if (level.getBlockState(p).isAir()) continue;
                level.removeBlock(p, false);
            }
        }
    }

    private void clearSurroundings(ServerLevel level, BlockPos base) {
        for (int dx = 0; dx < FRAME_WIDTH; dx++) {
            for (int dy = 0; dy < FRAME_HEIGHT; dy++) {
                for (int dz : new int[] {-1, 1}) {
                    BlockPos p = base.offset(dx, dy, dz);
                    if (level.getBlockState(p).isAir()) continue;
                    level.removeBlock(p, false);
                }
            }
        }
    }

    private void placeGround(ServerLevel level, BlockPos base, Block frameBlock) {
        BlockState frame = frameBlock.defaultBlockState();
        for (int dx = -1; dx < FRAME_WIDTH + 1; dx++) {
            for (int dz = -1; dz < 2; dz++) {
                BlockPos p = base.offset(dx, -1, dz);
                if (!level.getBlockState(p).isAir()) continue;
                level.setBlockAndUpdate(p, frame);
            }
        }
    }
}
