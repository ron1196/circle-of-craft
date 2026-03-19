package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.entity.hostile.TermiteEntity;
import io.github.ron1196.thelionking.registry.EntityTypes;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TermiteMoundBlock extends Block {

  private static final int MAX_TERMITES_ON_DESTROY = 2;
  private static final float SPAWN_CHANCE = 0.33F;

  public TermiteMoundBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void playerDestroy(
      @NotNull Level level,
      @NotNull Player player,
      @NotNull BlockPos pos,
      @NotNull BlockState state,
      @Nullable BlockEntity blockEntity,
      @NotNull ItemStack tool) {
    super.playerDestroy(level, player, pos, state, blockEntity, tool);
    if (level instanceof ServerLevel serverLevel && level.getRandom().nextFloat() < SPAWN_CHANCE) {
      int count = level.getRandom().nextInt(MAX_TERMITES_ON_DESTROY);
      for (int i = 0; i < count; i++) {
        spawnTermite(serverLevel, pos, player);
      }
    }
  }

  private void spawnTermite(ServerLevel level, BlockPos pos, Player target) {
    TermiteEntity termite = EntityTypes.TERMITE.get().create(level);
    if (termite != null) {
      termite.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      termite.setTarget(target);
      level.addFreshEntity(termite);
    }
  }
}
