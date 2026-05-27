package io.github.ron1196.circleofcraft.entity.ai;

import io.github.ron1196.circleofcraft.entity.animal.DikDikEntity;
import io.github.ron1196.circleofcraft.entity.animal.GemsbokEntity;
import io.github.ron1196.circleofcraft.entity.animal.ZebraEntity;
import io.github.ron1196.circleofcraft.entity.hostile.HyenaEntity;
import io.github.ron1196.circleofcraft.entity.hostile.SkeletalHyenaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

/** Lions hunt prey animals and attack hyenas on sight. */
public class LionAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {

    public LionAttackGoal(PathfinderMob mob) {
        super(
                mob,
                LivingEntity.class,
                2,
                true,
                false,
                e -> e instanceof ZebraEntity
                        || e instanceof DikDikEntity
                        || e instanceof GemsbokEntity
                        || e instanceof HyenaEntity
                        || e instanceof SkeletalHyenaEntity);
    }
}
