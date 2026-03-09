package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.animal.DikDikEntity;
import io.github.ron1196.thelionking.entity.animal.GemsbokEntity;
import io.github.ron1196.thelionking.entity.animal.ZebraEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

/**
 * Lions hunt prey animals (zebras, dik-diks, gemsboks).
 * Only starts hunting with a 1/200 chance per tick (simulates hunger).
 */
public class LionAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {

    public LionAttackGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 10, true, false,
                e -> e instanceof ZebraEntity || e instanceof DikDikEntity || e instanceof GemsbokEntity);
    }

    @Override
    public boolean canUse() {
        return mob.getRandom().nextInt(200) == 0 && super.canUse();
    }
}
