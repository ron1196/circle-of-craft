package io.github.ron1196.circleofcraft.world.feature;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class FallenLogFeature extends Feature<NoneFeatureConfiguration> {

    private static final int LENGTH = 7;
    private static final int BLOCK_UPDATE_FLAGS = 2;

    private final Supplier<Block> logBlock;

    public FallenLogFeature(@NotNull Codec<NoneFeatureConfiguration> codec, @NotNull Supplier<Block> logBlock) {
        super(codec);
        this.logBlock = logBlock;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        int dx = axis == Direction.Axis.X ? 1 : 0;
        int dz = axis == Direction.Axis.Z ? 1 : 0;

        for (int i = 0; i < LENGTH; i++) {
            BlockPos pos = origin.offset(dx * i, 0, dz * i);
            if (!isValidPlacement(level, pos)) {
                return false;
            }
        }

        BlockState log = logBlock.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);

        for (int i = 0; i < LENGTH; i++) {
            BlockPos pos = origin.offset(dx * i, 0, dz * i);
            level.setBlock(pos, log, BLOCK_UPDATE_FLAGS);
            BlockPos below = pos.below();
            if (level.getBlockState(below).is(Blocks.GRASS_BLOCK)) {
                level.setBlock(below, Blocks.DIRT.defaultBlockState(), BLOCK_UPDATE_FLAGS);
            }
        }
        return true;
    }

    private boolean isValidPlacement(@NotNull WorldGenLevel level, @NotNull BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (!below.is(Blocks.GRASS_BLOCK) && !below.is(Blocks.SAND) && !below.is(Blocks.DIRT)) {
            return false;
        }
        BlockState here = level.getBlockState(pos);
        return here.isAir() || here.canBeReplaced();
    }
}
