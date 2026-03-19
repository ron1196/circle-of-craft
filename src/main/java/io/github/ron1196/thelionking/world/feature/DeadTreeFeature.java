package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DeadTreeFeature extends Feature<NoneFeatureConfiguration> {

  public DeadTreeFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    // Only grow on sand or outsand
    BlockState below = level.getBlockState(pos.below());
    if (!below.is(net.minecraft.world.level.block.Blocks.SAND)
        && !below.is(LionKingBlocks.OUTSAND.get())) {
      return false;
    }

    int height = 6 + random.nextInt(3);

    // Check space
    for (int y = 0; y < height + 2; y++) {
      if (!level.isStateAtPosition(pos.above(y), BlockState::isAir)) {
        return false;
      }
    }

    BlockState log = LionKingBlocks.DEADWOOD_LOG.get().defaultBlockState();

    // Trunk
    for (int y = 0; y < height; y++) {
      level.setBlock(pos.above(y), log, 2);
    }

    // 4 branch stubs at various heights
    Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    for (int i = 0; i < 4; i++) {
      int branchY = 3 + random.nextInt(height - 3);
      Direction dir = dirs[i];
      BlockPos branchPos = pos.above(branchY).relative(dir);
      level.setBlock(branchPos, log, 2);
      // Extend branch 1-2 blocks
      if (random.nextBoolean()) {
        level.setBlock(branchPos.relative(dir), log, 2);
      }
    }

    return true;
  }
}
