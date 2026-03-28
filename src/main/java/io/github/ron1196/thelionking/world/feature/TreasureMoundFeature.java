package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.world.feature.FeatureHelper.LootEntry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Treasure Mound — termite block mound structure in the Outlands, matching the old mod's
 * LKWorldGenTreasureMound.
 *
 * <p>6x6 termite block mound with variable heights, hollow interior, pride brick pillars at
 * corners, random entrance, and two loot chests.
 */
public class TreasureMoundFeature extends Feature<NoneFeatureConfiguration> {

    public TreasureMoundFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int i = origin.getX();
        int j = origin.getY() + 1;
        int k = origin.getZ();

        BlockState termite = LionKingBlocks.TERMITE_MOUND.get().defaultBlockState();
        BlockState outsand = LionKingBlocks.OUTSAND.get().defaultBlockState();
        BlockState prideBrick = LionKingBlocks.PRIDE_BRICK.get().defaultBlockState();

        // Replace ground with outsand (6x6 area)
        for (int dx = 0; dx < 6; dx++) {
            for (int dz = 0; dz < 6; dz++) {
                level.setBlock(new BlockPos(i + dx, j - 1, k + dz), outsand, 2);
            }
        }

        // Build 6x6 termite block mound with variable heights
        // Height map based on distance from edge:
        //   Outer ring (edge): 3-4 blocks tall
        //   Middle ring (1 in): 6-9 blocks tall
        //   Inner core (2+ in): 8-13 blocks tall
        for (int dx = 0; dx < 6; dx++) {
            for (int dz = 0; dz < 6; dz++) {
                int distFromEdge = Math.min(Math.min(dx, 5 - dx), Math.min(dz, 5 - dz));
                int height;
                if (distFromEdge == 0) {
                    // Outer ring
                    height = 3 + random.nextInt(2); // 3-4
                } else if (distFromEdge == 1) {
                    // Middle ring
                    height = 6 + random.nextInt(4); // 6-9
                } else {
                    // Inner core
                    height = 8 + random.nextInt(6); // 8-13
                }

                // Build column upward and fill down to ground
                for (int dy = -1; dy < height; dy++) {
                    BlockPos pos = new BlockPos(i + dx, j + dy, k + dz);
                    level.setBlock(pos, termite, 2);
                }
            }
        }

        // Hollow out interior: 4x4 area (dx=1..4, dz=1..4), 2-3 blocks of air inside
        int hollowHeight = 2 + random.nextInt(2); // 2-3
        for (int dx = 1; dx <= 4; dx++) {
            for (int dz = 1; dz <= 4; dz++) {
                for (int dy = 0; dy < hollowHeight; dy++) {
                    level.setBlock(
                            new BlockPos(i + dx, j + dy, k + dz),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                }
            }
        }

        // Pride brick pillars at 4 inner corners: (1,1), (1,4), (4,1), (4,4)
        // From floor (j) up to hollow height
        int[][] pillarPositions = {{1, 1}, {1, 4}, {4, 1}, {4, 4}};
        for (int[] pp : pillarPositions) {
            for (int dy = 0; dy < hollowHeight; dy++) {
                level.setBlock(new BlockPos(i + pp[0], j + dy, k + pp[1]), prideBrick, 2);
            }
        }

        // Random entrance direction (N/S/E/W): 2-block wide opening on one side
        int entranceDir = random.nextInt(4); // 0=N, 1=S, 2=E, 3=W
        // Create a 2-wide, hollowHeight-tall opening through the mound wall
        switch (entranceDir) {
            case 0: // North (z = k, dx = 2,3)
                for (int dy = 0; dy < hollowHeight; dy++) {
                    level.setBlock(
                            new BlockPos(i + 2, j + dy, k),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                    level.setBlock(
                            new BlockPos(i + 3, j + dy, k),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                }
                // Termite block lintel above entrance
                level.setBlock(new BlockPos(i + 2, j + hollowHeight, k), termite, 2);
                level.setBlock(new BlockPos(i + 3, j + hollowHeight, k), termite, 2);
                break;
            case 1: // South (z = k+5, dx = 2,3)
                for (int dy = 0; dy < hollowHeight; dy++) {
                    level.setBlock(
                            new BlockPos(i + 2, j + dy, k + 5),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                    level.setBlock(
                            new BlockPos(i + 3, j + dy, k + 5),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                }
                level.setBlock(new BlockPos(i + 2, j + hollowHeight, k + 5), termite, 2);
                level.setBlock(new BlockPos(i + 3, j + hollowHeight, k + 5), termite, 2);
                break;
            case 2: // East (x = i+5, dz = 2,3)
                for (int dy = 0; dy < hollowHeight; dy++) {
                    level.setBlock(
                            new BlockPos(i + 5, j + dy, k + 2),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                    level.setBlock(
                            new BlockPos(i + 5, j + dy, k + 3),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                }
                level.setBlock(new BlockPos(i + 5, j + hollowHeight, k + 2), termite, 2);
                level.setBlock(new BlockPos(i + 5, j + hollowHeight, k + 3), termite, 2);
                break;
            case 3: // West (x = i, dz = 2,3)
                for (int dy = 0; dy < hollowHeight; dy++) {
                    level.setBlock(
                            new BlockPos(i, j + dy, k + 2),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                    level.setBlock(
                            new BlockPos(i, j + dy, k + 3),
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            2);
                }
                level.setBlock(new BlockPos(i, j + hollowHeight, k + 2), termite, 2);
                level.setBlock(new BlockPos(i, j + hollowHeight, k + 3), termite, 2);
                break;
        }

        // Place two chests on the opposite side from entrance
        Direction chestFacing = getChestFacing(entranceDir);
        BlockState chestState = net.minecraft.world.level.block.Blocks.CHEST
                .defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, chestFacing);
        BlockPos chest1Pos;
        BlockPos chest2Pos;

        switch (entranceDir) {
            case 0: // Entrance is North, chests on South interior wall (z = k+4)
                chest1Pos = new BlockPos(i + 2, j, k + 4);
                chest2Pos = new BlockPos(i + 3, j, k + 4);
                break;
            case 1: // Entrance is South, chests on North interior wall (z = k+1)
                chest1Pos = new BlockPos(i + 2, j, k + 1);
                chest2Pos = new BlockPos(i + 3, j, k + 1);
                break;
            case 2: // Entrance is East, chests on West interior wall (x = i+1)
                chest1Pos = new BlockPos(i + 1, j, k + 2);
                chest2Pos = new BlockPos(i + 1, j, k + 3);
                break;
            case 3: // Entrance is West, chests on East interior wall (x = i+4)
            default:
                chest1Pos = new BlockPos(i + 4, j, k + 2);
                chest2Pos = new BlockPos(i + 4, j, k + 3);
                break;
        }

        level.setBlock(chest1Pos, chestState, 2);
        level.setBlock(chest2Pos, chestState, 2);

        // Fill chests with loot (4 random items per chest)
        fillChestLoot(level, chest1Pos, random);
        fillChestLoot(level, chest2Pos, random);

        return true;
    }

    /** Check if a block is sand-like (valid ground for treasure mound). */
    private boolean isSandLike(BlockState state) {
        return state.is(BlockTags.SAND)
                || state.is(LionKingBlocks.OUTSAND.get())
                || state.is(net.minecraft.world.level.block.Blocks.SAND)
                || state.is(net.minecraft.world.level.block.Blocks.RED_SAND);
    }

    /** Get the facing direction for chests (face toward entrance). */
    private Direction getChestFacing(int entranceDir) {
        switch (entranceDir) {
            case 0:
                return Direction.NORTH; // Entrance N, chests face N toward entrance
            case 1:
                return Direction.SOUTH; // Entrance S, chests face S
            case 2:
                return Direction.EAST; // Entrance E, chests face E
            case 3:
                return Direction.WEST; // Entrance W, chests face W
            default:
                return Direction.NORTH;
        }
    }

    // ── Treasure mound loot sub-pools ──────────────────────────────────────

    private static final List<LootEntry> CORRUPT_TOOLS = List.of(
            LootEntry.of(LionKingItems.CORRUPT_SWORD),
            LootEntry.of(LionKingItems.CORRUPT_PICKAXE),
            LootEntry.of(LionKingItems.CORRUPT_AXE),
            LootEntry.of(LionKingItems.CORRUPT_SHOVEL),
            LootEntry.of(LionKingItems.CORRUPT_HOE));

    private static final List<LootEntry> KIVULITE_TOOLS = List.of(
            LootEntry.of(LionKingItems.KIVULITE_SWORD),
            LootEntry.of(LionKingItems.KIVULITE_PICKAXE),
            LootEntry.of(LionKingItems.KIVULITE_AXE),
            LootEntry.of(LionKingItems.KIVULITE_SHOVEL),
            LootEntry.of(LionKingItems.KIVULITE_HOE));

    // ── Main treasure mound loot table ─────────────────────────────────────
    private static final int CHEST_SLOT_COUNT = 4;

    private static final List<LootEntry> TREASURE_MOUND_LOOT = List.of(
            LootEntry.of(LionKingItems.TERMITE_DUST, 2, 4),
            LootEntry.of(2, LionKingItems.DART_BLACK, 4, 5),
            LootEntry.of(2, LionKingItems.NUKA_SHARD, 3, 8),
            LootEntry.of(LionKingItems.FEATHER_BLACK, 2, 4),
            LootEntry.of(LionKingItems.LION_COOKED, 2, 4),
            LootEntry.of(r -> enchantedTool(r, CORRUPT_TOOLS)),
            LootEntry.of(r -> enchantedTool(r, KIVULITE_TOOLS)),
            LootEntry.of(LionKingItems.KIVULITE, 1, 3));

    private static ItemStack enchantedTool(RandomSource random, List<LootEntry> toolPool) {
        ItemStack tool = FeatureHelper.pickLoot(toolPool, random);
        if (tool != null) {
            EnchantmentHelper.enchantItem(random, tool, 3, false);
        }
        return tool;
    }

    /** Fill a chest with random loot matching the old mod's treasure mound loot table. */
    private void fillChestLoot(WorldGenLevel level, BlockPos chestPos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(chestPos);
        if (!(be instanceof ChestBlockEntity chest)) {
            return;
        }

        for (int slot = 0; slot < CHEST_SLOT_COUNT; slot++) {
            ItemStack loot = FeatureHelper.pickLoot(TREASURE_MOUND_LOOT, random);
            if (loot != null) {
                chest.setItem(slot, loot);
            }
        }
    }
}
