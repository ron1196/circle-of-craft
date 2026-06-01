package io.github.ron1196.circleofcraft.entity.ai;

import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Hyenas periodically step a short distance toward the nearest Scar, then wander off again.
 * Each activation only nudges the hyena ~one stride closer (never all the way to Scar), so the
 * population forms a loose trail pointing at Scar's cave instead of every hyena stacking on the
 * single reachable block above it — Scar spawns underground and is unpathable, which previously
 * froze the whole pack on one spot.
 */
public class HyenaFollowScarGoal extends Goal {

    private static final double SCAR_DETECTION_RANGE = 120.0;
    private static final double FOLLOW_SPEED = 1.1;
    private static final double NUDGE_MIN = 8.0;
    private static final double NUDGE_RANGE = 5.0;
    private static final double NUDGE_STOP_DISTANCE = 6.0;
    private static final double SCATTER_RADIUS = 4.0;
    private static final int FOLLOW_INTERVAL_MIN = 40;
    private static final int FOLLOW_INTERVAL_RANGE = 60;
    private static final int MAX_NUDGE_TICKS = 100;

    private final Monster hyena;
    private double nudgeX;
    private double nudgeY;
    private double nudgeZ;
    private int cooldown;
    private int ticksRunning;

    public HyenaFollowScarGoal(Monster hyena) {
        this.hyena = hyena;
        this.cooldown = hyena.getRandom().nextInt(FOLLOW_INTERVAL_RANGE);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (hyena.getTarget() != null) return false;

        List<ScarEntity> scars = hyena.level()
                .getEntitiesOfClass(ScarEntity.class, hyena.getBoundingBox().inflate(SCAR_DETECTION_RANGE));
        if (scars.isEmpty()) return false;

        ScarEntity scar = scars.get(0);
        if (!scar.isAlive()) return false;

        double dx = scar.getX() - hyena.getX();
        double dz = scar.getZ() - hyena.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal < NUDGE_STOP_DISTANCE) return false;

        double step = Math.min(NUDGE_MIN + hyena.getRandom().nextDouble() * NUDGE_RANGE, horizontal);
        double scatterX = (hyena.getRandom().nextDouble() * 2 - 1) * SCATTER_RADIUS;
        double scatterZ = (hyena.getRandom().nextDouble() * 2 - 1) * SCATTER_RADIUS;
        nudgeX = hyena.getX() + dx / horizontal * step + scatterX;
        nudgeZ = hyena.getZ() + dz / horizontal * step + scatterZ;
        nudgeY = hyena.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) nudgeX, (int) nudgeZ);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return ticksRunning < MAX_NUDGE_TICKS
                && hyena.getTarget() == null
                && !hyena.getNavigation().isDone();
    }

    @Override
    public void start() {
        ticksRunning = 0;
        hyena.getNavigation().moveTo(nudgeX, nudgeY, nudgeZ, FOLLOW_SPEED);
    }

    @Override
    public void tick() {
        ticksRunning++;
    }

    @Override
    public void stop() {
        cooldown = FOLLOW_INTERVAL_MIN + hyena.getRandom().nextInt(FOLLOW_INTERVAL_RANGE);
        hyena.getNavigation().stop();
    }
}
