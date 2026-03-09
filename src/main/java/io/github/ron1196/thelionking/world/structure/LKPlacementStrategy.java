package io.github.ron1196.thelionking.world.structure;

import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;

import java.util.Map;

/**
 * Strategy pattern for validating structure placement terrain.
 * Each subclass encapsulates a different validation approach.
 */
public abstract class LKPlacementStrategy {

    abstract boolean isValid(GenerationContext context, int x, int y, int z);

    /**
     * Lazy holder — avoids class loading deadlock from superclass referencing subclasses.
     */
    private static class Registry {
        static final LKPlacementStrategy DEFAULT = new NoFluidStrategy();

        static final Map<String, LKPlacementStrategy> STRATEGIES = Map.of(
                "rafiki_tree", new FourCornersStrategy(3),
                "zira_mound", new AboveSeaLevelStrategy(),
                "treasure_mound", new AboveSeaLevelStrategy(),
                "ticket_booth", new NoFluidStrategy(),
                "timon_pumbaa_lodge", new AboveSeaLevelStrategy()
        );
    }

    static LKPlacementStrategy forStructure(String path) {
        return Registry.STRATEGIES.getOrDefault(path, Registry.DEFAULT);
    }

    static int getHeight(GenerationContext context, int x, int z) {
        return context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );
    }

    /**
     * Large footprint — all 4 corners must be above sea level.
     */
    static class FourCornersStrategy extends LKPlacementStrategy {
        private final int radius;

        FourCornersStrategy(int radius) {
            this.radius = radius;
        }

        @Override
        boolean isValid(GenerationContext context, int x, int y, int z) {
            int seaLevel = context.chunkGenerator().getSeaLevel();
            int h1 = getHeight(context, x - radius, z - radius);
            int h2 = getHeight(context, x + radius, z - radius);
            int h3 = getHeight(context, x - radius, z + radius);
            int h4 = getHeight(context, x + radius, z + radius);
            return Math.min(Math.min(h1, h2), Math.min(h3, h4)) >= seaLevel;
        }
    }

    /**
     * Y minimum check — reject at or below sea level (avoids lava lakes / ocean).
     */
    static class AboveSeaLevelStrategy extends LKPlacementStrategy {
        @Override
        boolean isValid(GenerationContext context, int x, int y, int z) {
            return y > context.chunkGenerator().getSeaLevel();
        }
    }

    /**
     * Fallback — check surface block isn't fluid.
     */
    static class NoFluidStrategy extends LKPlacementStrategy {
        @Override
        boolean isValid(GenerationContext context, int x, int y, int z) {
            NoiseColumn column = context.chunkGenerator().getBaseColumn(
                    x, z, context.heightAccessor(), context.randomState()
            );
            return column.getBlock(y - 1).getFluidState().isEmpty();
        }
    }
}
