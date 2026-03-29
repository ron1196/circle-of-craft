package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.PumbaaEntity;
import io.github.ron1196.thelionking.entity.npc.TimonEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Pumbaa follows the nearest Timon entity within 32 blocks. Starts following at 8 blocks distance,
 * stops at 3 blocks.
 */
public class PumbaaFollowTimonGoal extends Goal {

    private final PumbaaEntity pumbaa;
    private TimonEntity timon;
    private static final double SEARCH_RANGE = 32.0;
    private static final double START_DISTANCE_SQ = 9.0; // 3 blocks squared
    private static final double STOP_DISTANCE_SQ = 4.0; // 2 blocks squared
    private static final double SPEED = 1.2D;
    private int recheckTimer;

    public PumbaaFollowTimonGoal(PumbaaEntity pumbaa) {
        this.pumbaa = pumbaa;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<TimonEntity> timons = pumbaa.level()
                .getEntitiesOfClass(TimonEntity.class, pumbaa.getBoundingBox().inflate(SEARCH_RANGE));
        if (timons.isEmpty()) return false;

        // Find the nearest Timon
        TimonEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (TimonEntity t : timons) {
            double dist = pumbaa.distanceToSqr(t);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = t;
            }
        }
        if (nearest == null || nearestDist < START_DISTANCE_SQ) return false;
        timon = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (timon == null || !timon.isAlive()) return false;
        return pumbaa.distanceToSqr(timon) > STOP_DISTANCE_SQ;
    }

    @Override
    public void start() {
        recheckTimer = 0;
    }

    @Override
    public void tick() {
        if (timon == null) return;
        pumbaa.getLookControl().setLookAt(timon, 10.0F, pumbaa.getMaxHeadXRot());
        if (--recheckTimer <= 0) {
            recheckTimer = 10;
            pumbaa.getNavigation().moveTo(timon, SPEED);
        }
    }

    @Override
    public void stop() {
        timon = null;
        pumbaa.getNavigation().stop();
    }
}
