package io.github.ron1196.circleofcraft.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

/** Prey animals panic when hurt, running at 1.5x speed. */
public class AmbientPanicGoal extends PanicGoal {

    public AmbientPanicGoal(PathfinderMob mob) {
        super(mob, 1.5D);
    }
}
