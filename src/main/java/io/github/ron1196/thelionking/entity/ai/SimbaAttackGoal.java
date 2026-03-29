package io.github.ron1196.thelionking.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Simba's melee attack goal with 1.3x speed multiplier. Follows target even when line of sight is
 * lost.
 */
public class SimbaAttackGoal extends MeleeAttackGoal {

    public SimbaAttackGoal(PathfinderMob mob) {
        super(mob, 1.3D, true);
    }
}
