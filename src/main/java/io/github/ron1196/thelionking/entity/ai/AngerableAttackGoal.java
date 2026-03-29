package io.github.ron1196.thelionking.entity.ai;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

/**
 * Target goal that only activates when the mob is angry (has a persistent anger target). Used by
 * neutral animals like rhinos and gemsboks that only attack when provoked.
 */
public class AngerableAttackGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public AngerableAttackGoal(PathfinderMob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    public AngerableAttackGoal(
            PathfinderMob mob,
            Class<T> targetType,
            int randomInterval,
            boolean mustSee,
            boolean mustReach,
            @Nullable Predicate<LivingEntity> targetCondition) {
        super(mob, targetType, randomInterval, mustSee, mustReach, targetCondition);
    }

    @Override
    public boolean canUse() {
        if (mob instanceof NeutralMob neutralMob) {
            if (!neutralMob.isAngry()) return false;
        } else {
            // If not a NeutralMob, check for a revenge target
            if (mob.getLastHurtByMob() == null) return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob instanceof NeutralMob neutralMob) {
            if (!neutralMob.isAngry()) return false;
        }
        return super.canContinueToUse();
    }
}
