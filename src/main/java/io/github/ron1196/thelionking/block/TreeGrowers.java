package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.Features;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

public class TreeGrowers {

  public static final AbstractTreeGrower ACACIA =
      new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            @NotNull RandomSource random, boolean bees) {
          return Features.PRIDE_ACACIA_TREE_KEY;
        }
      };

  public static final AbstractMegaTreeGrower RAINFOREST =
      new AbstractMegaTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            @NotNull RandomSource random, boolean bees) {
          return Features.RAINFOREST_TREE_KEY;
        }

        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(
            @NotNull RandomSource random) {
          return Features.MEGA_RAINFOREST_TREE_KEY;
        }
      };

  public static final AbstractTreeGrower MANGO =
      new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            @NotNull RandomSource random, boolean bees) {
          return Features.MANGO_TREE_KEY;
        }
      };

  public static final AbstractTreeGrower PASSION =
      new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            @NotNull RandomSource random, boolean bees) {
          return Features.PASSION_TREE_KEY;
        }
      };

  public static final AbstractTreeGrower BANANA =
      new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            @NotNull RandomSource random, boolean bees) {
          return Features.BANANA_TREE_KEY;
        }
      };
}
