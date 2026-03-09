package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Zira's Mound in the Outlands — a corrupt pridestone crater with Zira NPC.
 */
public class ZiraMoundFeature extends Feature<NoneFeatureConfiguration> {

    public ZiraMoundFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        BlockState corruptStone = LKBlocks.CORRUPT_PRIDESTONE.get().defaultBlockState();
        BlockState corruptBrick = LKBlocks.CORRUPT_PRIDE_BRICK.get().defaultBlockState();

        // Raised mound — cone of corrupt pridestone
        int moundHeight = 8;
        for (int y = 0; y < moundHeight; y++) {
            int radius = moundHeight - y;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        BlockState block = (y == moundHeight - 1 || (y > 0 && random.nextFloat() < 0.3F))
                                ? corruptBrick : corruptStone;
                        level.setBlock(pos.offset(x, y, z), block, 2);
                    }
                }
            }
        }

        // Flat platform at top
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                level.setBlock(pos.offset(x, moundHeight, z), corruptBrick, 2);
            }
        }

        // Four pillars at corners
        for (int[] corner : new int[][]{{-2, -2}, {-2, 2}, {2, -2}, {2, 2}}) {
            for (int y = moundHeight + 1; y < moundHeight + 4; y++) {
                level.setBlock(pos.offset(corner[0], y, corner[1]), corruptBrick, 2);
            }
        }

        // Spawn Zira on the platform
        if (!level.isClientSide()) {
            var zira = LKEntityTypes.ZIRA.get().create(level.getLevel());
            if (zira != null) {
                zira.moveTo(pos.getX() + 0.5, pos.getY() + moundHeight + 1, pos.getZ() + 0.5, 0, 0);
                zira.setPersistenceRequired();
                level.addFreshEntityWithPassengers(zira);
            }
        }

        return true;
    }
}
