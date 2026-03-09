package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/**
 * Simba follows his owner player.
 * Start distance: 10 blocks, stop distance: 3 blocks, speed: 1.3.
 * Does not follow when sitting.
 */
public class SimbaFollowOwnerGoal extends Goal {

    private final SimbaEntity simba;
    private Player owner;
    private static final double START_DISTANCE_SQ = 16.0;  // 4 blocks squared
    private static final double STOP_DISTANCE_SQ = 4.0;    // 2 blocks squared
    private static final double TELEPORT_DISTANCE_SQ = 400.0; // 20 blocks squared
    private static final double SPEED = 1.3D;
    private int timeToRecalcPath;

    public SimbaFollowOwnerGoal(SimbaEntity simba) {
        this.simba = simba;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (simba.isSitting()) return false;
        owner = simba.getOwner();
        if (owner == null) return false;
        if (simba.getTarget() != null && simba.getTarget().isAlive()) return false;
        return simba.distanceToSqr(owner) > START_DISTANCE_SQ;
    }

    @Override
    public boolean canContinueToUse() {
        if (simba.isSitting()) return false;
        if (owner == null || !owner.isAlive()) return false;
        if (simba.getTarget() != null && simba.getTarget().isAlive()) return false;
        return simba.distanceToSqr(owner) > STOP_DISTANCE_SQ;
    }

    @Override
    public void start() {
        timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        if (owner == null) return;
        simba.getLookControl().setLookAt(owner, 10.0F, simba.getMaxHeadXRot());

        // Teleport to owner if too far
        if (simba.distanceToSqr(owner) > TELEPORT_DISTANCE_SQ) {
            simba.moveTo(owner.getX(), owner.getY(), owner.getZ(), simba.getYRot(), simba.getXRot());
            simba.getNavigation().stop();
            return;
        }

        if (--timeToRecalcPath <= 0) {
            timeToRecalcPath = 10;
            simba.getNavigation().moveTo(owner, SPEED);
        }
    }

    @Override
    public void stop() {
        owner = null;
        simba.getNavigation().stop();
    }
}
