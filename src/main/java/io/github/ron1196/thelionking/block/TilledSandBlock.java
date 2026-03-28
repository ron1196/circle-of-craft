package io.github.ron1196.thelionking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

/**
 * Tilled Sand — the farmland equivalent for sand. Created by hoeing sand.
 * Supports Kiwano Stem crops. Reverts to sand (not dirt) when conditions aren't met.
 */
public class TilledSandBlock extends FarmBlock {

    private static final int WATER_SEARCH_RADIUS = 4;

    public TilledSandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            turnToSand(level, pos);
        }
    }

    @Override
    public void randomTick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        int moisture = state.getValue(MOISTURE);
        if (isNearWater(level, pos)) {
            if (moisture >= 7) return;
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
            return;
        }
        if (moisture <= 0) {
            turnToSand(level, pos);
            return;
        }
        level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
    }

    @Override
    public void fallOn(
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull Entity entity,
            float fallDistance) {
        // Vanilla FarmBlock turns to dirt on fall — we turn to sand
        if (!level.isClientSide && level.random.nextFloat() < fallDistance - 0.5F) {
            turnToSand(level, pos);
        }
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block neighborBlock,
            @NotNull BlockPos neighborPos,
            boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        // Solid block placed on top → revert to sand (old mod behavior)
        if (level.getBlockState(pos.above()).isSolid()) {
            turnToSand(level, pos);
            return;
        }
        // Nothing below → revert to sand (gravity, but instant instead of falling entity)
        if (!level.getBlockState(pos.below()).isSolid()) {
            turnToSand(level, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState oldState,
            boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.getBlockState(pos.below()).isSolid()) {
            turnToSand(level, pos);
        }
    }

    private static void turnToSand(Level level, BlockPos pos) {
        level.setBlockAndUpdate(
                pos, pushEntitiesUp(level.getBlockState(pos), Blocks.SAND.defaultBlockState(), level, pos));
    }

    @Override
    public boolean canSustainPlant(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull net.minecraft.core.Direction facing,
            @NotNull net.minecraftforge.common.IPlantable plantable) {
        return true;
    }

    private static boolean isNearWater(LevelReader level, BlockPos pos) {
        for (BlockPos checkPos : BlockPos.betweenClosed(
                pos.offset(-WATER_SEARCH_RADIUS, 0, -WATER_SEARCH_RADIUS),
                pos.offset(WATER_SEARCH_RADIUS, 1, WATER_SEARCH_RADIUS))) {
            if (level.getFluidState(checkPos).is(Fluids.WATER)) {
                return true;
            }
        }
        return false;
    }
}
