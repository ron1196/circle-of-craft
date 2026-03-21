package io.github.ron1196.thelionking.world.feature;

import io.github.ron1196.thelionking.world.structure.LionKingStructurePiece;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** Shared helpers for structure feature generation. */
public final class FeatureHelper {

    private FeatureHelper() {}

    /**
     * A weighted loot entry. Higher weight = more likely to be picked.
     *
     * @param weight   relative probability (e.g. 3 is 3x more likely than 1)
     * @param factory  creates the ItemStack given a RandomSource (for variable counts)
     */
    public record LootEntry(int weight, Function<RandomSource, ItemStack> factory) {
        /** Convenience constructor for single-item entries with fixed count. */
        public static LootEntry of(int weight, java.util.function.Supplier<ItemStack> supplier) {
            return new LootEntry(weight, random -> supplier.get());
        }

        /** Weight-1 convenience for single-item entries with fixed count. */
        public static LootEntry of(java.util.function.Supplier<ItemStack> supplier) {
            return of(1, supplier);
        }

        /** Weight-1 convenience for entries that need a RandomSource. */
        public static LootEntry of(Function<RandomSource, ItemStack> factory) {
            return new LootEntry(1, factory);
        }
    }

    /**
     * Pick a random loot entry from a weighted list and create the ItemStack.
     * Returns null if the list is empty.
     */
    public static ItemStack pickLoot(List<LootEntry> entries, RandomSource random) {
        int totalWeight = 0;
        for (LootEntry entry : entries) {
            totalWeight += entry.weight();
        }
        if (totalWeight <= 0) {
            return null;
        }

        int roll = random.nextInt(totalWeight);
        int cumulative = 0;
        for (LootEntry entry : entries) {
            cumulative += entry.weight();
            if (roll < cumulative) {
                return entry.factory().apply(random);
            }
        }
        return entries.get(entries.size() - 1).factory().apply(random);
    }

    /**
     * Places a block only if the position is within the current chunk's bounding box. Use this in
     * features that are called per-chunk via LKStructurePiece.
     */
    public static void placeBlock(WorldGenLevel level, int x, int y, int z, BlockState state) {
        if (!LionKingStructurePiece.isInCurrentChunk(x, y, z)) return;
        level.setBlock(new BlockPos(x, y, z), state, 2);
    }

    /**
     * Spawns a persistent entity at the given position, but only if that position falls within the
     * current chunk's bounding box. This prevents duplicate spawns when postProcess is called once
     * per overlapping chunk.
     */
    public static <T extends Mob> void spawnEntity(
            WorldGenLevel level, EntityType<T> type, double x, double y, double z) {
        if (level.isClientSide()) return;
        if (!LionKingStructurePiece.isInCurrentChunk((int) x, (int) y, (int) z)) return;
        T entity = type.create(level.getLevel());
        if (entity != null) {
            entity.moveTo(x, y, z, 0, 0);
            entity.setPersistenceRequired();
            level.addFreshEntityWithPassengers(entity);
        }
    }
}
