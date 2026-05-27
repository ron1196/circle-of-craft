package io.github.ron1196.circleofcraft.entity.ai;

import io.github.ron1196.circleofcraft.entity.animal.BugEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BugFleePlayerGoal extends Goal {

    private static final double FLEE_SPEED = 1.6;
    private static final float SEARCH_RADIUS = 16.0F;
    private static final int FLEE_DISTANCE = 12;
    private static final int FLEE_VERTICAL = 5;

    private final BugEntity bug;

    @Nullable
    private Player threat;

    public BugFleePlayerGoal(BugEntity bug) {
        this.bug = bug;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bug.panicTicks <= 0) return false;
        threat = bug.level().getNearestPlayer(bug, SEARCH_RADIUS);
        return threat != null;
    }

    @Override
    public boolean canContinueToUse() {
        return bug.panicTicks > 0 && threat != null && !bug.getNavigation().isDone();
    }

    @Override
    public void start() {
        repath();
    }

    @Override
    public void tick() {
        if (bug.getNavigation().isDone()) {
            threat = bug.level().getNearestPlayer(bug, SEARCH_RADIUS);
            repath();
        }
    }

    @Override
    public void stop() {
        threat = null;
    }

    private void repath() {
        if (threat == null) return;
        Vec3 fleePos = DefaultRandomPos.getPosAway(bug, FLEE_DISTANCE, FLEE_VERTICAL, threat.position());
        if (fleePos != null) {
            bug.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, FLEE_SPEED);
        }
    }
}
