package io.github.ron1196.thelionking.world.feature;

import io.github.ron1196.thelionking.block.ZiraMoundGateBlock;
import io.github.ron1196.thelionking.world.structure.LKStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Shared helpers for structure feature generation.
 */
public final class FeatureHelper {

    private FeatureHelper() {}

    /**
     * Places a block only if the position is within the current chunk's bounding box.
     * Use this in features that are called per-chunk via LKStructurePiece.
     */
    public static void placeBlock(WorldGenLevel level, int x, int y, int z, BlockState state) {
        if (!LKStructurePiece.isInCurrentChunk(x, y, z)) return;
        BlockPos pos = new BlockPos(x, y, z);
        // Never overwrite gate blocks — they must survive cross-chunk generation
        if (level.getBlockState(pos).getBlock() instanceof ZiraMoundGateBlock) return;
        level.setBlock(pos, state, 2);
    }

    /**
     * Spawns a persistent entity at the given position, but only if that position
     * falls within the current chunk's bounding box. This prevents duplicate spawns
     * when postProcess is called once per overlapping chunk.
     */
    public static <T extends Mob> void spawnEntity(WorldGenLevel level, EntityType<T> type,
                                                    double x, double y, double z) {
        if (level.isClientSide()) return;
        if (!LKStructurePiece.isInCurrentChunk((int) x, (int) y, (int) z)) return;

        T entity = type.create(level.getLevel());
        if (entity != null) {
            entity.moveTo(x, y, z, 0, 0);
            entity.setPersistenceRequired();
            level.addFreshEntityWithPassengers(entity);
        }
    }
}
