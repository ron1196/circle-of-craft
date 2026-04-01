package io.github.ron1196.thelionking.util;

import net.minecraft.core.BlockPos;

public class DirectionHelper {

    public static String getCompassDirection(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();
        double angle = Math.toDegrees(Math.atan2(-dx, dz));
        if (angle < 0) angle += 360;
        if (angle < 22.5 || angle >= 337.5) return "to the south";
        if (angle < 67.5) return "to the southwest";
        if (angle < 112.5) return "to the west";
        if (angle < 157.5) return "to the northwest";
        if (angle < 202.5) return "to the north";
        if (angle < 247.5) return "to the northeast";
        if (angle < 292.5) return "to the east";
        return "to the southeast";
    }
}
