package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RainforestTreeFeature extends Feature<NoneFeatureConfiguration> {

    public RainforestTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        int height = 8 + random.nextInt(5); // tall trees, 8-12

        BlockState below = level.getBlockState(pos.below());
        if (!below.is(net.minecraft.tags.BlockTags.DIRT)) {
            return false;
        }

        for (int y = 0; y < height + 4; y++) {
            if (!level.isStateAtPosition(pos.above(y), BlockState::isAir)) {
                return false;
            }
        }

        BlockState log = Blocks.RAINFOREST_LOG.get().defaultBlockState();
        BlockState leaves = Blocks.RAINFOREST_LEAVES.get().defaultBlockState()
                .setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true);

        // Trunk
        for (int y = 0; y < height; y++) {
            level.setBlock(pos.above(y), log, 2);
        }

        // Wide canopy at top — 3 layers
        for (int layer = 0; layer < 3; layer++) {
            int y = height - 1 + layer;
            int radius = layer == 1 ? 3 : 2; // widest in the middle
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius && Math.abs(z) == radius) continue; // corners
                    BlockPos leafPos = pos.offset(x, y, z);
                    if (level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, leaves, 2);
                    }
                }
            }
        }

        // Top cap
        level.setBlock(pos.above(height + 2), leaves, 2);

        return true;
    }
}
