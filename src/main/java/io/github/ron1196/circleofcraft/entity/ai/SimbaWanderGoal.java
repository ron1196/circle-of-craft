package io.github.ron1196.circleofcraft.entity.ai;

import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

/**
 * Simba wanders only when he has no owner or the owner is far away. Does not wander when sitting.
 * Unlike most land mobs, Simba does not avoid water — he can wade in to fish.
 */
public class SimbaWanderGoal extends RandomStrollGoal {

    private static final double OWNER_PROXIMITY_SQ = 100.0;

    private final SimbaEntity simba;

    public SimbaWanderGoal(SimbaEntity simba) {
        super(simba, 1.0D);
        this.simba = simba;
    }

    @Override
    public boolean canUse() {
        if (simba.isOrderedToSit()) return false;
        LivingEntity owner = simba.getOwner();
        if (owner != null && simba.distanceToSqr(owner) < OWNER_PROXIMITY_SQ) return false;
        return super.canUse();
    }
}
