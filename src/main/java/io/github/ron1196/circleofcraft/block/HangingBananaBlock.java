package io.github.ron1196.circleofcraft.block;

import com.mojang.serialization.MapCodec;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class HangingBananaBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final int RIPE_AGE = 3;
    private static final int GROW_CHANCE = 2;
    private static final int MAX_HARVEST = 3;

    // Small box shapes for each facing direction (box is flush against the trunk side)
    private static final VoxelShape NORTH_SHAPE = Block.box(6, 3, 12, 10, 15, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(6, 3, 0, 10, 15, 4);
    private static final VoxelShape WEST_SHAPE = Block.box(12, 3, 6, 16, 15, 10);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 3, 6, 4, 15, 10);

    public static final MapCodec<HangingBananaBlock> CODEC = simpleCodec(HangingBananaBlock::new);

    public HangingBananaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, RIPE_AGE));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hit) {
        if (state.getValue(AGE) < RIPE_AGE) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(AGE, 0), 3);
            popResource(level, pos, new ItemStack(ModItems.BANANA.get(), 1 + level.random.nextInt(MAX_HARVEST)));
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void randomTick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        int age = state.getValue(AGE);
        if (age < RIPE_AGE && random.nextInt(GROW_CHANCE) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedTo = pos.relative(facing.getOpposite());
        BlockState attachedState = level.getBlockState(attachedTo);
        return attachedState.is(ModBlocks.BANANA_LOG.get());
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        if (clickedFace.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, clickedFace);
        }
        return this.defaultBlockState();
    }
}
