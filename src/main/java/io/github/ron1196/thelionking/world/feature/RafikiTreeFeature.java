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
 * Giant Rafiki tree at the origin of the Pride Lands.
 * Generates a massive rafiki_wood trunk with a large canopy and spawns Rafiki at the top.
 */
public class RafikiTreeFeature extends Feature<NoneFeatureConfiguration> {

    public RafikiTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        BlockState log = LKBlocks.RAFIKI_WOOD.get().defaultBlockState();
        BlockState leaves = LKBlocks.RAFIKI_LEAVES.get().defaultBlockState();
        BlockState platform = LKBlocks.RAFIKI_WOOD.get().defaultBlockState();

        int trunkHeight = 16;

        // Check ground
        if (!level.getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.DIRT)) {
            return false;
        }

        // Main trunk — 2x2
        for (int y = 0; y < trunkHeight; y++) {
            for (int x = 0; x <= 1; x++) {
                for (int z = 0; z <= 1; z++) {
                    level.setBlock(pos.offset(x, y, z), log, 3);
                }
            }
        }

        // Roots at base — spreading
        for (int dx = -2; dx <= 3; dx++) {
            for (int dz = -2; dz <= 3; dz++) {
                if ((dx >= 0 && dx <= 1) && (dz >= 0 && dz <= 1)) continue;
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist <= 3.0 && random.nextFloat() < 0.6F) {
                    level.setBlock(pos.offset(dx, 0, dz), log, 3);
                    if (random.nextFloat() < 0.4F) {
                        level.setBlock(pos.offset(dx, 1, dz), log, 3);
                    }
                }
            }
        }

        // Platform at top for Rafiki (platform)
        int platformY = trunkHeight;
        for (int x = -2; x <= 3; x++) {
            for (int z = -2; z <= 3; z++) {
                BlockPos platformPos = pos.offset(x, platformY, z);
                if (level.getBlockState(platformPos).isAir()) {
                    level.setBlock(platformPos, platform, 3);
                }
            }
        }

        // Large canopy — 4 layers
        for (int layer = 0; layer < 4; layer++) {
            int y = trunkHeight + 1 + layer;
            int radius = layer <= 1 ? 5 : (layer == 2 ? 4 : 2);
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radius * radius + 1) continue;
                    BlockPos leafPos = pos.offset(x, y, z);
                    if (level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, leaves, 3);
                    }
                }
            }
        }

        // Spawn Rafiki on the platform
        if (!level.isClientSide()) {
            var rafiki = LKEntityTypes.RAFIKI.get().create(level.getLevel());
            if (rafiki != null) {
                rafiki.moveTo(pos.getX() + 0.5, pos.getY() + platformY + 1, pos.getZ() + 0.5, 0, 0);
                rafiki.setPersistenceRequired();
                level.addFreshEntityWithPassengers(rafiki);
            }
        }

        return true;
    }
}
