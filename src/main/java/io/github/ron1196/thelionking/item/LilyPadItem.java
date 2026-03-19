package io.github.ron1196.thelionking.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LilyPadItem extends BlockItem {

  public LilyPadItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
    return InteractionResult.PASS;
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(
      @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
    HitResult hit = player.pick(5.0, 0.0F, true);
    if (hit.getType() != HitResult.Type.BLOCK) {
      return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    BlockPos waterPos = ((BlockHitResult) hit).getBlockPos();
    if (!level.getFluidState(waterPos).is(FluidTags.WATER)
        || !level.getFluidState(waterPos).isSource()) {
      return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    BlockPos placePos = waterPos.above();
    BlockState state = getBlock().defaultBlockState();
    if (!level.getBlockState(placePos).canBeReplaced() || !state.canSurvive(level, placePos)) {
      return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    if (!level.isClientSide) {
      level.setBlock(placePos, state, Block.UPDATE_ALL_IMMEDIATE);
      if (!player.getAbilities().instabuild) {
        player.getItemInHand(hand).shrink(1);
      }
    }

    return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
  }
}
