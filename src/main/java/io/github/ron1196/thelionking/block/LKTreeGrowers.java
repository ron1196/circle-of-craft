package io.github.ron1196.thelionking.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Placeholder tree growers. These will be replaced with real configured features in Phase 7.
 * For now they grow vanilla oak trees as a fallback.
 */
public class LKTreeGrowers {

    public static final AbstractTreeGrower ACACIA = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return net.minecraft.data.worldgen.features.TreeFeatures.ACACIA;
        }
    };

    public static final AbstractTreeGrower RAINFOREST = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return net.minecraft.data.worldgen.features.TreeFeatures.JUNGLE_TREE;
        }
    };

    public static final AbstractTreeGrower MANGO = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return net.minecraft.data.worldgen.features.TreeFeatures.OAK;
        }
    };

    public static final AbstractTreeGrower PASSION = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return net.minecraft.data.worldgen.features.TreeFeatures.OAK;
        }
    };

    public static final AbstractTreeGrower BANANA = new AbstractTreeGrower() {
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
            return net.minecraft.data.worldgen.features.TreeFeatures.OAK;
        }
    };
}
