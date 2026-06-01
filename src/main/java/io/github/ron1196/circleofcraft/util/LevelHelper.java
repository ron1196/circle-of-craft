package io.github.ron1196.circleofcraft.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;

public final class LevelHelper {

    private LevelHelper() {}

    /**
     * Surface Y at (x, z), force-generating the chunk first. Plain {@code getHeight} returns the
     * world's min build height (the void floor) for an unloaded chunk, which drops teleported or
     * respawned players into the void — so every cross-dimension/long-distance teleport must load
     * the destination chunk before reading its height.
     */
    public static int surfaceY(ServerLevel level, int x, int z, Heightmap.Types heightmap) {
        level.getChunk(x >> 4, z >> 4);
        return level.getHeight(heightmap, x, z);
    }

    public static int surfaceY(ServerLevel level, int x, int z) {
        return surfaceY(level, x, z, Heightmap.Types.MOTION_BLOCKING);
    }
}
