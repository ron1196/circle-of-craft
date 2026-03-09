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

public class TermiteMoundFeature extends Feature<NoneFeatureConfiguration> {

    public TermiteMoundFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        BlockState mound = LKBlocks.TERMITE_MOUND.get().defaultBlockState();

        boolean large = random.nextInt(5) == 0;

        if (large) {
            int maxHeight = 6 + random.nextInt(8);
            for (int y = 0; y < maxHeight; y++) {
                // Radius shrinks as we go up (cone shape)
                int radius = Math.max(1, (int) ((maxHeight - y) * 0.5));
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z <= radius * radius) {
                            BlockPos p = pos.offset(x, y, z);
                            if (level.getBlockState(p).isAir() || y == 0) {
                                level.setBlock(p, mound, 3);
                            }
                        }
                    }
                }
            }
        } else {
            int height = 3 + random.nextInt(3);
            for (int y = 0; y < height; y++) {
                int radius = Math.max(1, (height - y));
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (Math.abs(x) + Math.abs(z) <= radius) {
                            BlockPos p = pos.offset(x, y, z);
                            if (level.getBlockState(p).isAir() || y == 0) {
                                level.setBlock(p, mound, 3);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
