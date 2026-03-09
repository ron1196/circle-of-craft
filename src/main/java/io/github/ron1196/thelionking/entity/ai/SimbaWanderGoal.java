package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;

/**
 * Simba wanders only when he has no owner or the owner is far away.
 * Does not wander when sitting.
 */
public class SimbaWanderGoal extends WaterAvoidingRandomStrollGoal {

    private final SimbaEntity simba;

    public SimbaWanderGoal(SimbaEntity simba) {
        super(simba, 1.0D);
        this.simba = simba;
    }

    @Override
    public boolean canUse() {
        if (simba.isSitting()) return false;
        Player owner = simba.getOwner();
        if (owner != null && simba.distanceToSqr(owner) < 100.0) return false;
        return super.canUse();
    }
}
