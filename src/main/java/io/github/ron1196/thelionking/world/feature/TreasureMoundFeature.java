package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Treasure Mound — random loot structure in the Outlands.
 * Small corrupt pridestone mound with a chest inside.
 */
public class TreasureMoundFeature extends Feature<NoneFeatureConfiguration> {

    public TreasureMoundFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        BlockState corruptStone = LKBlocks.CORRUPT_PRIDESTONE.get().defaultBlockState();

        // Small dome of corrupt pridestone
        int radius = 3;
        int height = 3;
        for (int y = 0; y < height; y++) {
            int r = radius - y;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (x * x + z * z <= r * r) {
                        level.setBlock(pos.offset(x, y, z), corruptStone, 3);
                    }
                }
            }
        }

        // Hollow interior — air pocket with chest
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlock(pos.offset(x, 1, z), Blocks.AIR.defaultBlockState(), 3);
            }
        }

        // Place chest with loot
        level.setBlock(pos.above(1), Blocks.CHEST.defaultBlockState(), 3);

        // Entrance (clear a 1x2 opening)
        level.setBlock(pos.offset(0, 1, -radius), Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(pos.offset(0, 2, -radius), Blocks.AIR.defaultBlockState(), 3);

        return true;
    }
}
