package io.github.ron1196.thelionking.world.dimension;

import io.github.ron1196.thelionking.block.PortalBlock;
import io.github.ron1196.thelionking.registry.Blocks;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    private final boolean isOutlands;

    public Teleporter(boolean isOutlands) {
        this.isOutlands = isOutlands;
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(
            Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        BlockPos destPos = findOrCreatePortal(entity, destWorld);
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

    private BlockPos findOrCreatePortal(Entity entity, ServerLevel destWorld) {
        Block portalBlock = isOutlands ? Blocks.OUTLANDS_PORTAL.get() : Blocks.PRIDE_LANDS_PORTAL.get();
        Block frameBlock = isOutlands ? Blocks.OUTLANDS_PORTAL_FRAME.get() : Blocks.PRIDE_PORTAL_FRAME.get();

        BlockPos entityPos = entity.blockPosition();
        BlockPos destPos = new BlockPos(entityPos.getX(), entityPos.getY(), entityPos.getZ());

        // Search for existing portal in a small loaded area (16 block radius, only loaded chunks)
        BlockPos existingPortal = findExistingPortal(destWorld, destPos, portalBlock);
        if (existingPortal != null) {
            return existingPortal;
        }

        // Create new portal at the destination
        return createPortal(destWorld, destPos, frameBlock, portalBlock);
    }

    @Nullable
    private BlockPos findExistingPortal(ServerLevel level, BlockPos center, Block portalBlock) {
        int range = 16;
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                int checkX = center.getX() + x;
                int checkZ = center.getZ() + z;

                // Only check loaded chunks
                if (!level.isLoaded(new BlockPos(checkX, 0, checkZ))) continue;

                int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, checkX, checkZ);
                // Search a vertical range around surface
                int minY = Math.max(level.getMinBuildHeight(), surfaceY - 20);
                int maxY = Math.min(level.getMaxBuildHeight(), surfaceY + 20);

                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(checkX, y, checkZ);
                    if (level.getBlockState(pos).is(portalBlock)) {
                        double dist = pos.distSqr(center);
                        if (dist < bestDist) {
                            bestDist = dist;
                            best = pos;
                        }
                    }
                }
            }
        }

        return best;
    }

    private BlockPos createPortal(ServerLevel level, BlockPos pos, Block frameBlock, Block portalBlock) {
        // Force chunk generation so heightmap is available
        ChunkPos chunkPos = new ChunkPos(pos);
        level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, true);

        int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
        if (surfaceY <= level.getMinBuildHeight() + 1) {
            // Heightmap returned bottom, try world spawn instead
            BlockPos spawn = level.getSharedSpawnPos();
            level.getChunk(new ChunkPos(spawn).x, new ChunkPos(spawn).z, ChunkStatus.FULL, true);
            surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawn.getX(), spawn.getZ());
            pos = new BlockPos(spawn.getX(), surfaceY, spawn.getZ());
            if (surfaceY <= level.getMinBuildHeight() + 1) {
                surfaceY = 70;
            }
        }
        BlockPos base = new BlockPos(pos.getX(), surfaceY, pos.getZ());

        BlockState frame = frameBlock.defaultBlockState();
        BlockState portal = portalBlock.defaultBlockState();
        if (portal.hasProperty(PortalBlock.AXIS)) {
            portal = portal.setValue(PortalBlock.AXIS, Direction.Axis.X);
        }

        // Build 4-wide x 5-tall frame with 2x3 portal interior
        //  F F F F
        //  F P P F
        //  F P P F
        //  F P P F
        //  F F F F
        for (int dx = 0; dx < 4; dx++) {
            for (int dy = 0; dy < 5; dy++) {
                BlockPos p = base.offset(dx, dy, 0);
                boolean isEdge = dx == 0 || dx == 3 || dy == 0 || dy == 4;
                level.setBlockAndUpdate(p, isEdge ? frame : portal);
            }
        }

        // Clear space in front and behind the portal
        for (int dx = 0; dx < 4; dx++) {
            for (int dy = 0; dy < 5; dy++) {
                for (int dz : new int[] {-1, 1}) {
                    BlockPos p = base.offset(dx, dy, dz);
                    if (!level.getBlockState(p).isAir()) {
                        level.removeBlock(p, false);
                    }
                }
            }
        }

        // Place solid ground under the portal
        for (int dx = -1; dx < 5; dx++) {
            for (int dz = -1; dz < 2; dz++) {
                BlockPos p = base.offset(dx, -1, dz);
                if (level.getBlockState(p).isAir()) {
                    level.setBlockAndUpdate(p, frame);
                }
            }
        }

        // Return position inside the portal (1 block in, 1 block up)
        return base.offset(1, 1, 0);
    }
}
