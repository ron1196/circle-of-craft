package io.github.ron1196.circleofcraft.block;

import com.mojang.serialization.MapCodec;
import io.github.ron1196.circleofcraft.block.entity.MountedShooterBlockEntity;
import io.github.ron1196.circleofcraft.block.entity.MountedShooterBlockEntity.FireMode;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MountedShooterBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    private static final VoxelShape SHAPE_NS = Block.box(4.0, 0.0, 0.0, 12.0, 12.0, 16.0);
    private static final VoxelShape SHAPE_EW = Block.box(0.0, 0.0, 4.0, 16.0, 12.0, 12.0);

    public static final MapCodec<MountedShooterBlock> CODEC = simpleCodec(MountedShooterBlock::new);

    public MountedShooterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return (facing == Direction.EAST || facing == Direction.WEST) ? SHAPE_EW : SHAPE_NS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MountedShooterBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return createTickerHelper(
                    type,
                    BlockEntityTypes.MOUNTED_SHOOTER.get(),
                    (lvl, pos, st, be) -> MountedShooterBlockEntity.clientTick(be));
        }
        return createTickerHelper(type, BlockEntityTypes.MOUNTED_SHOOTER.get(), MountedShooterBlockEntity::serverTick);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack heldItem,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (heldItem.isEmpty() || !MountedShooterBlockEntity.isDartItem(heldItem.getItem())) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof MountedShooterBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (be.hasDarts()) {
            ItemStack old = be.unloadDarts();
            if (!player.addItem(old)) {
                player.drop(old, false);
            }
        }
        be.loadDarts(heldItem);
        player.setItemInHand(hand, ItemStack.EMPTY);
        player.sendSystemMessage(
                Component.literal("§7Loaded " + be.getDartStack().getCount() + " darts"));
        return ItemInteractionResult.CONSUME;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof MountedShooterBlockEntity be)) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            FireMode newMode = be.cycleFireMode();
            player.sendSystemMessage(Component.literal("§7Mode: " + newMode.getDescription()));
            return InteractionResult.CONSUME;
        }

        if (be.hasDarts()) {
            if (be.getFireMode() == FireMode.MANUAL) {
                be.fireInDirection(level, pos, state.getValue(FACING));
                return InteractionResult.CONSUME;
            } else {
                ItemStack darts = be.unloadDarts();
                if (!player.addItem(darts)) {
                    player.drop(darts, false);
                }
                player.sendSystemMessage(Component.literal("§7Darts removed"));
                return InteractionResult.CONSUME;
            }
        }

        player.sendSystemMessage(
                Component.literal("§7Hold darts and right-click to load. Shift+click to change mode."));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block block,
            @NotNull BlockPos fromPos,
            boolean isMoving) {
        if (!level.isClientSide() && level.hasNeighborSignal(pos)) {
            if (level.getBlockEntity(pos) instanceof MountedShooterBlockEntity be
                    && be.getFireMode() == FireMode.REDSTONE) {
                be.fireInDirection(level, pos, state.getValue(FACING));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState newState,
            boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MountedShooterBlockEntity be && be.hasDarts()) {
                level.addFreshEntity(
                        new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, be.getDartStack()));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
