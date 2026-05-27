package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Maize — sugar cane-like stacking plant that grows near water on grass/dirt.
 *
 * <p>Ported from old mod's BlockMaize. Stacks up to 4 blocks tall, requires adjacent
 * water at the base. Has a corn ear state that can be harvested by right-clicking.
 */
public class MaizeCropBlock extends Block {

    public static final BooleanProperty HAS_CORN = BooleanProperty.create("has_corn");

    private static final int MAX_HEIGHT = 4;
    private static final int GROW_UP_CHANCE = 22;
    private static final int CORN_SPROUT_CHANCE = 25;
    private static final int BASE_GROWTH_RATE = 8;
    private static final int HYDRATED_GROWTH_RATE = 2;
    private static final int DRY_GROWTH_RATE = 4;
    private static final int FARMLAND_CHECK_DEPTH = 5;
    private static final int BONUS_CORN_CHANCE = 4; // 1 in 4 chance for bonus corn

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public MaizeCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_CORN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_CORN);
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
    public @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());

        if (below.is(this)) { // Can stack on top of another maize block
            return true;
        }

        // Can be placed on farmland or tilled sand
        if (below.is(Blocks.FARMLAND) || below.is(ModBlocks.TILLED_SAND.get())) {
            return true;
        }

        // Can be placed on grass/dirt/sand if water is adjacent (sugar cane style)
        if (below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.SAND)) {
            return hasAdjacentWater(level, pos.below());
        }

        return false;
    }

    private boolean hasAdjacentWater(LevelReader level, BlockPos groundPos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            FluidState fluid = level.getFluidState(groundPos.relative(dir));
            if (fluid.is(Fluids.WATER) || fluid.is(Fluids.FLOWING_WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
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
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void randomTick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        int height = getHeightBelow(level, pos) + 1;
        if (height >= MAX_HEIGHT) {
            return;
        }

        int growthRate = getGrowthRate(level, pos);
        if (random.nextInt(growthRate) != 0) {
            return;
        }

        // Try to grow upward
        if (level.isEmptyBlock(pos.above()) && random.nextInt(GROW_UP_CHANCE) == 0) {
            level.setBlock(pos.above(), defaultBlockState().setValue(HAS_CORN, true), 3);
        }

        // Try to sprout a corn ear on this block
        if (!state.getValue(HAS_CORN) && hasMaizeBelow(level, pos) && random.nextInt(CORN_SPROUT_CHANCE) == 0) {
            level.setBlock(pos, state.setValue(HAS_CORN, true), 3);
        }
    }

    private int getHeightBelow(LevelReader level, BlockPos pos) {
        int count = 0;
        while (level.getBlockState(pos.below(count + 1)).is(this)) {
            count++;
        }
        return count;
    }

    private boolean hasMaizeBelow(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(this);
    }

    private int getGrowthRate(LevelReader level, BlockPos pos) {
        // Check for farmland below (up to FARMLAND_CHECK_DEPTH blocks down)
        for (int depth = 1; depth <= FARMLAND_CHECK_DEPTH; depth++) {
            BlockState below = level.getBlockState(pos.below(depth));
            if (below.is(Blocks.FARMLAND)) {
                // Hydrated farmland grows faster
                Integer moistureValue = below.getValue(TilledSandBlock.MOISTURE);
                return moistureValue > 0 ? HYDRATED_GROWTH_RATE : DRY_GROWTH_RATE;
            }
            if (!below.is(this)) {
                break;
            }
        }
        return BASE_GROWTH_RATE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (!state.getValue(HAS_CORN)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            // Harvest corn: remove ear, drop 1-2 corn
            level.setBlock(pos, state.setValue(HAS_CORN, false), 3);
            popResource(level, pos, new ItemStack(ModItems.CORN.get()));
            if (level.random.nextInt(BONUS_CORN_CHANCE) == 0) {
                popResource(level, pos, new ItemStack(ModItems.CORN.get()));
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(
            @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return new ItemStack(ModItems.MAIZE_STALKS.get());
    }

    @Override
    public boolean isPathfindable(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull PathComputationType type) {
        return false;
    }
}
