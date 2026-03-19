package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class BugTrapBlock extends BaseEntityBlock {

  private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

  public BugTrapBlock(BlockBehaviour.Properties properties) {
    super(properties);
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
      NetworkHooks.openScreen((ServerPlayer) player, be, pos);
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
