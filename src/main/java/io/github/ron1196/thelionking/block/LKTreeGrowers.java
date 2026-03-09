package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LKFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class LKTreeGrowers {

    public static final AbstractTreeGrower ACACIA = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return LKFeatures.PRIDE_ACACIA_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower RAINFOREST = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return LKFeatures.RAINFOREST_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower MANGO = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return LKFeatures.MANGO_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower PASSION = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return LKFeatures.PASSION_TREE_KEY;
        }
    };

    public static final AbstractTreeGrower BANANA = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return LKFeatures.BANANA_TREE_KEY;
        }
    };
}
