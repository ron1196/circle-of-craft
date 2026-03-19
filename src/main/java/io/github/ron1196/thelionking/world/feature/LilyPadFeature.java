package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class LilyPadFeature extends Feature<NoneFeatureConfiguration> {

  private static final int ATTEMPTS = 3;
  private static final int SPREAD_XZ = 8;
  private static final int SPREAD_Y = 4;
  private static final int BLOCK_UPDATE_FLAGS = 2;
  private static final BlockState[] VARIANTS = new BlockState[3];

  public LilyPadFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
    VARIANTS[0] = LionKingBlocks.LILY_RED.get().defaultBlockState();
    VARIANTS[1] = LionKingBlocks.LILY_VIOLET.get().defaultBlockState();
    VARIANTS[2] = LionKingBlocks.LILY_WHITE.get().defaultBlockState();
  }

  @Override
  public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();

    boolean placed = false;
    for (int i = 0; i < ATTEMPTS; i++) {
      int dx = random.nextInt(SPREAD_XZ) - random.nextInt(SPREAD_XZ);
      int dy = random.nextInt(SPREAD_Y) - random.nextInt(SPREAD_Y);
      int dz = random.nextInt(SPREAD_XZ) - random.nextInt(SPREAD_XZ);
      BlockPos pos = origin.offset(dx, dy, dz);

      BlockPos waterPos = scanDownToWaterSurface(level, pos);
      BlockPos lilyPos = waterPos.above();
      BlockState lily = VARIANTS[random.nextInt(VARIANTS.length)];

      if (!canPlaceLily(level, waterPos, lilyPos, lily)) {
        continue;
      }

      level.setBlock(lilyPos, lily, BLOCK_UPDATE_FLAGS);
      placed = true;
    }
    return placed;
  }

  private BlockPos scanDownToWaterSurface(WorldGenLevel level, BlockPos pos) {
    while (pos.getY() > level.getMinBuildHeight() && level.getBlockState(pos).isAir()) {
      pos = pos.below();
    }
    return pos;
  }

  private boolean canPlaceLily(
      WorldGenLevel level, BlockPos waterPos, BlockPos lilyPos, BlockState lily) {
    return level.getBlockState(waterPos).is(Blocks.WATER)
        && level.getFluidState(waterPos).isSource()
        && level.getBlockState(lilyPos).isAir()
        && lily.canSurvive(level, lilyPos);
  }
}
