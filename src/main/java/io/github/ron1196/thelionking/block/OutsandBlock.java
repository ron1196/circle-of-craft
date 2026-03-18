package io.github.ron1196.thelionking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Outsand block from the Outlands dimension.
 * Falls like sand, acts as a fire source, damages entities on contact, and emits smoke particles.
 */
public class OutsandBlock extends FallingBlock {

    public OutsandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(
            BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public boolean isFireSource(
            BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos, Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        damageEntity(level, entity);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        damageEntity(level, entity);
    }

    private void damageEntity(Level level, Entity entity) {
        if (entity instanceof LivingEntity && !entity.fireImmune() && level.random.nextBoolean()) {
            entity.hurt(level.damageSources().inFire(), 2.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int direction = random.nextInt(2) * 2 - 1;
        double x = (double) pos.getX() + 0.5D + 0.25D * (double) direction;
        double y = (double) pos.getY() + random.nextFloat();
        double z = (double) pos.getZ() + random.nextFloat();
        double dx = random.nextFloat() * 2.0F * (float) direction;
        double dy = ((double) random.nextFloat() - 0.5D) * 0.5D;
        double dz = ((double) random.nextFloat() - 0.5D) * 0.5D;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, dx, dy, dz);
    }

    @Override
    public int getDustColor(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        // Dark sand/ash color for falling particles
        return 0x4A3728;
    }
}
