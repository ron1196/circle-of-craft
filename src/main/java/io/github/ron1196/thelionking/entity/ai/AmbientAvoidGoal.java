package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.animal.LionEntity;
import io.github.ron1196.thelionking.entity.hostile.HyenaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

/**
 * Prey animals (zebras, dik-diks, gemsboks, etc.) avoid predators like lions and hyenas. Flee
 * distance: 12 blocks. Walk speed: 1.0, sprint speed: 1.5.
 */
public class AmbientAvoidGoal extends AvoidEntityGoal<LivingEntity> {

    public AmbientAvoidGoal(PathfinderMob mob) {
        super(mob, LivingEntity.class, 12.0F, 1.0D, 1.5D, e -> e instanceof LionEntity || e instanceof HyenaEntity);
    }
}
