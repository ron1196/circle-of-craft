package io.github.ron1196.circleofcraft.client;

import net.minecraft.util.Mth;

/**
 * Mirror of the package-private {@code CompassItem.CompassWobble} damped-spring smoothing.
 * Shortest-path delta means it interpolates across the modulo seam.
 */
public final class CompassWobble {

    private double rotation;
    private double deltaRotation;
    private long lastUpdateTick = -1;

    public double rotation() {
        return rotation;
    }

    public void update(long gameTime, double target) {
        if (lastUpdateTick == gameTime) {
            return;
        }
        lastUpdateTick = gameTime;
        double delta = Mth.positiveModulo(target - rotation + 0.5, 1.0) - 0.5;
        deltaRotation = (deltaRotation + delta * 0.1) * 0.8;
        rotation = Mth.positiveModulo(rotation + deltaRotation, 1.0);
    }
}
