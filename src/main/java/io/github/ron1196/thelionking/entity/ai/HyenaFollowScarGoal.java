package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.ScarEntity;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;

/**
 * Hyenas occasionally wander toward the nearest Scar entity.
 * This creates a trail of hyenas leading the player to Scar's cave.
 */
public class HyenaFollowScarGoal extends Goal {

    private static final double SCAR_DETECTION_RANGE = 80.0;
    private static final double FOLLOW_SPEED = 0.8;
    private static final int FOLLOW_INTERVAL_MIN = 200;
    private static final int FOLLOW_INTERVAL_RANGE = 400;

    private final Monster hyena;
    private ScarEntity target;
    private int cooldown;

    public HyenaFollowScarGoal(Monster hyena) {
        this.hyena = hyena;
        this.cooldown = hyena.getRandom().nextInt(FOLLOW_INTERVAL_RANGE);
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        // Don't follow if already fighting
        if (hyena.getTarget() != null) return false;

        List<ScarEntity> scars = hyena.level().getEntitiesOfClass(
                ScarEntity.class, hyena.getBoundingBox().inflate(SCAR_DETECTION_RANGE));
        if (scars.isEmpty()) return false;

        target = scars.get(0);
        return target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        return hyena.distanceToSqr(target) > 16.0; // Stop when within 4 blocks
    }

    @Override
    public void start() {
        if (target != null) {
            hyena.getNavigation().moveTo(target, FOLLOW_SPEED);
        }
    }

    @Override
    public void tick() {
        if (target != null && hyena.getNavigation().isDone()) {
            hyena.getNavigation().moveTo(target, FOLLOW_SPEED);
        }
    }

    @Override
    public void stop() {
        target = null;
        cooldown = FOLLOW_INTERVAL_MIN + hyena.getRandom().nextInt(FOLLOW_INTERVAL_RANGE);
        hyena.getNavigation().stop();
    }
}
