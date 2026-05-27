package io.github.ron1196.circleofcraft.world.feature;

import io.github.ron1196.circleofcraft.world.structure.ModStructurePiece;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared helpers for structure feature generation.
 */
public final class FeatureHelper {

    private FeatureHelper() {}

    /**
     * A weighted loot entry. Higher weight = more likely to be picked.
     *
     * @param weight  relative probability (e.g. 3 is 3x more likely than 1)
     * @param factory creates the ItemStack given a RandomSource (for variable counts)
     */
    public record LootEntry(int weight, Function<RandomSource, ItemStack> factory) {
        /**
         * Single item, count 1, weight 1.
         */
        public static LootEntry of(Supplier<? extends Item> item) {
            return new LootEntry(1, r -> new ItemStack(item.get()));
        }

        /**
         * Single item, base + random(0…extraRange) count, weight 1.
         */
        public static LootEntry of(Supplier<? extends Item> item, int base, int extraRange) {
            return new LootEntry(1, r -> new ItemStack(item.get(), base + r.nextInt(extraRange)));
        }

        /**
         * Single item, base + random(0…extraRange) count, custom weight.
         */
        public static LootEntry of(int weight, Supplier<? extends Item> item, int base, int extraRange) {
            return new LootEntry(weight, r -> new ItemStack(item.get(), base + r.nextInt(extraRange)));
        }

        /**
         * Custom factory with weight 1 (for complex loot like sub-pool picks).
         */
        public static LootEntry of(Function<RandomSource, ItemStack> factory) {
            return new LootEntry(1, factory);
        }

        /**
         * Custom factory with custom weight.
         */
        public static LootEntry of(int weight, Function<RandomSource, ItemStack> factory) {
            return new LootEntry(weight, factory);
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
     * features that are called per-chunk via StructurePiece.
     */
    public static void placeBlock(WorldGenLevel level, int x, int y, int z, BlockState state) {
        if (!ModStructurePiece.isInCurrentChunk(x, y, z)) return;
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
        if (!ModStructurePiece.isInCurrentChunk((int) x, (int) y, (int) z)) return;
        T entity = type.create(level.getLevel());
        if (entity == null) {
            return;
        }
        entity.moveTo(x, y, z, 0, 0);
        entity.setPersistenceRequired();
        level.addFreshEntityWithPassengers(entity);
    }
}
