package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.PrideBedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PrideBedBlock extends BedBlock {

  public PrideBedBlock(Properties properties) {
    super(DyeColor.BROWN, properties);
  }

  @Override
  public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new PrideBedBlockEntity(pos, state);
  }
}
