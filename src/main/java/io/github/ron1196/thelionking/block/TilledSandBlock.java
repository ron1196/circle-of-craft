package io.github.ron1196.thelionking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

/**
 * Tilled Sand — the farmland equivalent for sand. Created by hoeing sand.
 * Supports crops (especially Kiwano Stem). Reverts to sand when conditions aren't met.
 * Does NOT extend FarmBlock to avoid hardcoded dirt reversion.
 */
public class TilledSandBlock extends Block {

    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    private static final int WATER_SEARCH_RADIUS = 4;

    public TilledSandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MOISTURE);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    // ── Survival / reversion ────────────────────────────────────────────────

    @Override
    public @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        if (direction == Direction.UP && level.getBlockState(pos.above()).isSolid()) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        if (level.getBlockState(pos.above()).isSolid()) {
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
            if (moisture < 7) {
                level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
            }
        } else if (moisture > 0) {
            level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
        } else if (!hasCrop(level, pos)) {
            turnToSand(level, pos);
        }
    }

    @Override
    public void fallOn(
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull Entity entity,
            float fallDistance) {
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
        if (level.getBlockState(pos.above()).isSolid()) {
            turnToSand(level, pos);
        } else if (!level.getBlockState(pos.below()).isSolid()) {
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
        if (!level.getBlockState(pos.below()).isSolid()) {
            turnToSand(level, pos);
        }
    }

    // ── Plant support ───────────────────────────────────────────────────────

    @Override
    public boolean canSustainPlant(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull Direction facing,
            @NotNull IPlantable plantable) {
        return true;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static void turnToSand(Level level, BlockPos pos) {
        BlockState sand = Blocks.SAND.defaultBlockState();
        level.setBlockAndUpdate(pos, pushEntitiesUp(level.getBlockState(pos), sand, level, pos));
    }

    private static boolean hasCrop(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof CropBlock;
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
