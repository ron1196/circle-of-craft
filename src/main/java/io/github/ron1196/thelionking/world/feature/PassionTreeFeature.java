package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PassionTreeFeature extends Feature<NoneFeatureConfiguration> {

    public PassionTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        int height = 5 + random.nextInt(3); // medium trees, 5-7

        BlockState below = level.getBlockState(pos.below());
        if (!below.is(net.minecraft.tags.BlockTags.DIRT)) {
            return false;
        }

        for (int y = 0; y < height + 3; y++) {
            if (!level.isStateAtPosition(pos.above(y), BlockState::isAir)) {
                return false;
            }
        }

        BlockState log = LKBlocks.PASSION_LOG.get().defaultBlockState();
        BlockState leaves = LKBlocks.PASSION_LEAVES.get().defaultBlockState();

        // Trunk
        for (int y = 0; y < height; y++) {
            level.setBlock(pos.above(y), log, 3);
        }

        // Rounded canopy — 2 layers
        for (int layer = 0; layer < 2; layer++) {
            int y = height - 1 + layer;
            int radius = 2;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius && Math.abs(z) == radius) continue;
                    BlockPos leafPos = pos.offset(x, y, z);
                    if (level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, leaves, 3);
                    }
                }
            }
        }

        // Top
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (Math.abs(x) == 1 && Math.abs(z) == 1 && random.nextBoolean()) continue;
                BlockPos leafPos = pos.offset(x, height + 1, z);
                if (level.getBlockState(leafPos).isAir()) {
                    level.setBlock(leafPos, leaves, 3);
                }
            }
        }

        return true;
    }
}
