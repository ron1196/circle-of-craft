package io.github.ron1196.circleofcraft.entity.ai;

import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

/**
 * Panic goal that is suppressed when the animal is angry. Angry animals fight back instead of
 * running away.
 */
public class AngerablePanicGoal extends PanicGoal {

    private final PathfinderMob mob;

    public AngerablePanicGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (mob instanceof NeutralMob neutralMob) {
            if (neutralMob.isAngry()) return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob instanceof NeutralMob neutralMob) {
            if (neutralMob.isAngry()) return false;
        }
        return super.canContinueToUse();
    }
}
