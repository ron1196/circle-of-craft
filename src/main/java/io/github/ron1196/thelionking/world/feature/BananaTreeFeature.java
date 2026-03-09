package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BananaTreeFeature extends Feature<NoneFeatureConfiguration> {

    public BananaTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        int height = 4 + random.nextInt(3);

        // Check space
        for (int y = 0; y < height + 3; y++) {
            if (!level.isStateAtPosition(pos.above(y), s -> s.isAir() || s.is(LKBlocks.BANANA_LEAVES.get()))) {
                return false;
            }
        }

        BlockState log = LKBlocks.BANANA_LOG.get().defaultBlockState();
        BlockState leaves = LKBlocks.BANANA_LEAVES.get().defaultBlockState();

        // Trunk
        for (int y = 0; y < height; y++) {
            level.setBlock(pos.above(y), log, 3);
        }

        // Top leaves
        BlockPos top = pos.above(height);
        level.setBlock(top, leaves, 3);

        // Four directional leaf clusters hanging down from top
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction dir : dirs) {
            BlockPos branch = top.relative(dir);
            int hangLength = 1 + random.nextInt(3);
            level.setBlock(branch, leaves, 3);
            for (int y = 1; y <= hangLength; y++) {
                level.setBlock(branch.below(y), leaves, 3);
            }
            // Second block outward
            BlockPos outer = branch.relative(dir);
            level.setBlock(outer, leaves, 3);
            int outerHang = random.nextInt(2) + 1;
            for (int y = 1; y <= outerHang; y++) {
                level.setBlock(outer.below(y), leaves, 3);
            }
        }

        // Hanging bananas on two sides
        Direction bananaDir1 = dirs[random.nextInt(4)];
        Direction bananaDir2 = bananaDir1.getOpposite();
        BlockPos banana1 = top.relative(bananaDir1).below(2 + random.nextInt(2));
        BlockPos banana2 = top.relative(bananaDir2).below(2 + random.nextInt(2));
        if (level.getBlockState(banana1).isAir()) {
            level.setBlock(banana1, LKBlocks.HANGING_BANANA.get().defaultBlockState(), 3);
        }
        if (level.getBlockState(banana2).isAir()) {
            level.setBlock(banana2, LKBlocks.HANGING_BANANA.get().defaultBlockState(), 3);
        }

        return true;
    }
}
