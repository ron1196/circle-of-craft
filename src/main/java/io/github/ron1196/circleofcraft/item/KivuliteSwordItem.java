package io.github.ron1196.circleofcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Kivulite Sword — sets enemies on fire on hit (3-5 seconds) with flame particles, and can place
 * fire on blocks like flint & steel.
 */
public class KivuliteSwordItem extends SwordItem {

    public KivuliteSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!target.fireImmune() && target.isAlive()) {
            target.setSecondsOnFire(3 + attacker.level().random.nextInt(3));

            // Flame particles
            Level level = target.level();
            for (int i = 0; i < 8; i++) {
                double dx = level.random.nextGaussian() * 0.02D;
                double dy = level.random.nextGaussian() * 0.02D;
                double dz = level.random.nextGaussian() * 0.02D;
                level.addParticle(
                        ParticleTypes.FLAME,
                        target.getX()
                                + (level.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth()) * 0.75F,
                        target.getY() + 0.25F + level.random.nextFloat() * target.getBbHeight(),
                        target.getZ()
                                + (level.random.nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth()) * 0.75F,
                        dx,
                        dy,
                        dz);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos firePos = pos.relative(face);

        if (context.getPlayer() == null || !context.getPlayer().mayUseItemAt(firePos, face, context.getItemInHand())) {
            return InteractionResult.FAIL;
        }

        BlockState stateAt = level.getBlockState(firePos);
        if (stateAt.isAir()) {
            level.playSound(
                    context.getPlayer(),
                    firePos,
                    SoundEvents.FLINTANDSTEEL_USE,
                    SoundSource.BLOCKS,
                    1.0F,
                    level.random.nextFloat() * 0.4F + 0.8F);
            level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 11);
            context.getItemInHand()
                    .hurtAndBreak(1, context.getPlayer(), (p) -> p.broadcastBreakEvent(context.getHand()));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
