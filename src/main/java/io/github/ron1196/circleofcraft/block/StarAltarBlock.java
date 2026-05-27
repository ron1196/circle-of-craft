package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.world.dimension.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class StarAltarBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    public StarAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (level instanceof Level world && world.dimension() != Dimensions.PRIDE_LANDS_LEVEL) {
            return false;
        }
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, net.minecraft.core.Direction.UP)) {
            return false;
        }
        if (!level.canSeeSky(pos.above())) {
            return false;
        }
        return hasRoom(level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block neighborBlock,
            @NotNull BlockPos neighborPos,
            boolean movedByPiston) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void animateTick(
            @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        double x = pos.getX() + 0.5 + 0.25 * (random.nextInt(2) * 2 - 1);
        double y = pos.getY() + random.nextFloat();
        double z = pos.getZ() + random.nextFloat();
        double dx = random.nextFloat() * 2.0F * (random.nextInt(2) * 2 - 1);
        double dy = (random.nextFloat() - 0.5) * 0.5;
        double dz = (random.nextFloat() - 0.5) * 0.5;
        level.addParticle(ParticleTypes.PORTAL, x, y, z, dx, dy, dz);
    }

    private static boolean hasRoom(LevelReader level, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 1; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (!level.getBlockState(pos.offset(dx, dy, dz)).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
