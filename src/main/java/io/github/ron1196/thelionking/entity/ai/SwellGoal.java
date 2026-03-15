package io.github.ron1196.thelionking.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.function.BooleanSupplier;

/**
 * AI goal: start swelling when close to target, stop when far.
 * Generic version — any mob implementing {@link Swellable} can use this.
 */
public class SwellGoal extends Goal {

    public interface Swellable {
        int getSwellDir();
        void setSwellDir(int dir);
    }

    private final Mob mob;
    private final Swellable swellable;
    private final double triggerDistanceSqr;
    private final BooleanSupplier enabled;

    public SwellGoal(Mob mob, double triggerDistance) {
        this(mob, triggerDistance, () -> true);
    }

    public SwellGoal(Mob mob, double triggerDistance, BooleanSupplier enabled) {
        if (!(mob instanceof Swellable)) {
            throw new IllegalArgumentException("Mob must implement Swellable: " + mob.getClass().getName());
        }
        this.mob = mob;
        this.swellable = (Swellable) mob;
        this.triggerDistanceSqr = triggerDistance * triggerDistance;
        this.enabled = enabled;
    }

    @Override
    public boolean canUse() {
        if (!this.enabled.getAsBoolean()) return false;
        if (this.mob.getTarget() == null) return false;
        return this.mob.distanceToSqr(this.mob.getTarget()) < this.triggerDistanceSqr;
    }

    @Override
    public void start() {
        this.swellable.setSwellDir(1);
    }

    @Override
    public void stop() {
        this.swellable.setSwellDir(-1);
    }
}
