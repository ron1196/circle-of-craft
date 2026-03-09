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
 * Timon & Pumbaa's Lodge — jungle-style hut with both NPCs.
 */
public class TimonPumbaaLodgeFeature extends Feature<NoneFeatureConfiguration> {

    public TimonPumbaaLodgeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        BlockState log = LKBlocks.MANGO_LOG.get().defaultBlockState();
        BlockState planks = LKBlocks.MANGO_PLANKS.get().defaultBlockState();
        BlockState leaves = LKBlocks.MANGO_LEAVES.get().defaultBlockState();

        // No strict ground check — structure system handles terrain placement

        // Floor 5x5
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                level.setBlock(pos.offset(x, 0, z), planks, 2);
            }
        }

        // Log pillars at corners, 3 high
        for (int[] corner : new int[][]{{0, 0}, {0, 4}, {4, 0}, {4, 4}}) {
            for (int y = 1; y <= 3; y++) {
                level.setBlock(pos.offset(corner[0], y, corner[1]), log, 2);
            }
        }

        // Walls — planks on 3 sides, open front (z=0)
        for (int y = 1; y <= 3; y++) {
            for (int x = 1; x < 4; x++) {
                level.setBlock(pos.offset(x, y, 4), planks, 2); // Back
            }
            for (int z = 1; z < 4; z++) {
                level.setBlock(pos.offset(0, y, z), planks, 2); // Left
                level.setBlock(pos.offset(4, y, z), planks, 2); // Right
            }
        }

        // Roof — leaf thatch, overhanging
        for (int x = -1; x <= 5; x++) {
            for (int z = -1; z <= 5; z++) {
                BlockPos roofPos = pos.offset(x, 4, z);
                if (level.getBlockState(roofPos).isAir()) {
                    level.setBlock(roofPos, leaves, 2);
                }
            }
        }

        // Spawn Timon and Pumbaa inside
        if (!level.isClientSide()) {
            var timon = LKEntityTypes.TIMON.get().create(level.getLevel());
            if (timon != null) {
                timon.moveTo(pos.getX() + 2.5, pos.getY() + 1, pos.getZ() + 2.5, 180, 0);
                timon.setPersistenceRequired();
                level.addFreshEntityWithPassengers(timon);
            }

            var pumbaa = LKEntityTypes.PUMBAA.get().create(level.getLevel());
            if (pumbaa != null) {
                pumbaa.moveTo(pos.getX() + 1.5, pos.getY() + 1, pos.getZ() + 2.5, 180, 0);
                pumbaa.setPersistenceRequired();
                level.addFreshEntityWithPassengers(pumbaa);
            }
        }

        return true;
    }
}
