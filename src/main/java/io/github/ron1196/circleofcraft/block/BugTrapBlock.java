package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.block.entity.BugTrapBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BugTrapBlock extends BaseEntityBlock {

    public enum ClosedFace implements StringRepresentable {
        NONE("none"),
        NORTH("north"),
        EAST("east"),
        SOUTH("south"),
        WEST("west");

        private final String name;

        ClosedFace(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static final EnumProperty<ClosedFace> CLOSED_FACE = EnumProperty.create("closed_face", ClosedFace.class);
    public static final IntegerProperty CLOSURE_LEVEL = IntegerProperty.create("closure_level", 0, 2);

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape COLLISION = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BugTrapBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
                .any()
                .setValue(CLOSED_FACE, ClosedFace.NONE)
                .setValue(CLOSURE_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(CLOSED_FACE, CLOSURE_LEVEL);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext ctx) {
        return COLLISION;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BugTrapBlockEntity(pos, state);
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
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof BugTrapBlockEntity be) {
            ((ServerPlayer) player).openMenu(be, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
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
            if (level.getBlockEntity(pos) instanceof BugTrapBlockEntity be) {
                Containers.dropContents(level, pos, be.getDrops());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof BugTrapBlockEntity trap) trap.serverTick();
            };
        }
        return null;
    }
}
