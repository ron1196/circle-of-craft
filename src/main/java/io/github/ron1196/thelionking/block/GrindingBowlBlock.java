package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.GrindingBowlBlockEntity;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrindingBowlBlock extends BaseEntityBlock {

  public GrindingBowlBlock(Properties properties) {
    super(properties);
  }

  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new GrindingBowlBlockEntity(pos, state);
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
    if (!level.isClientSide) {
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof GrindingBowlBlockEntity grindingBowl) {
        NetworkHooks.openScreen((ServerPlayer) player, grindingBowl, pos);
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
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
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof GrindingBowlBlockEntity grindingBowl) {
        grindingBowl.drops();
      }
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      @NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    if (level.isClientSide) {
      return null;
    }
    return createTickerHelper(
        type, BlockEntityTypes.GRINDING_BOWL.get(), GrindingBowlBlockEntity::serverTick);
  }
}
