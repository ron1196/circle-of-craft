package io.github.ron1196.thelionking.world.feature;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.SpawnerBlockEntity;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Pride Dungeon — underground dungeon rooms in the Outlands dimension, ported from the old mod's
 * LKWorldGenDungeons.
 *
 * <p>Two variants: Hyena (2/3 chance) and Crocodile (1/3 chance). Crocodile dungeons are
 * larger, have a water floor, and vines on walls. Both use pride brick walls with cracked
 * brick accents, 75/25 mossy/regular pride brick floors, and LK-themed loot chests.
 */
public class PrideDungeonFeature extends Feature<NoneFeatureConfiguration> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int BASE_HALF_WIDTH = 3;
    private static final int ROOM_HEIGHT = 3;
    private static final int CHEST_LOOT_ATTEMPTS = 8;
    private static final int VINE_MAX_LENGTH = 6;
    private static final int VINE_CHANCE = 4;
    private static final double CRACKED_BRICK_CHANCE = 0.1;

    private static final ResourceLocation HYENA_ID =
            new ResourceLocation(TheLionKingMod.MOD_ID, "hyena");
    private static final ResourceLocation CROCODILE_ID =
            new ResourceLocation(TheLionKingMod.MOD_ID, "crocodile");

    public PrideDungeonFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int cx = origin.getX();
        int cy = origin.getY();
        int cz = origin.getZ();

        // Random half-widths (2-3 blocks from center), matching old mod
        int halfW = random.nextInt(2) + 2;
        int halfD = random.nextInt(2) + 2;

        // Validate with base room size FIRST (matching old mod order — validate small, expand later)
        if (!isValidPlacement(level, cx, cy, cz, halfW, halfD)) {
            LOGGER.debug("[PrideDungeon] FAILED validation at ({}, {}, {}), halfW={}, halfD={}",
                    cx, cy, cz, halfW, halfD);
            return false;
        }

        // Determine variant and expand AFTER validation (old mod expanded post-validation)
        boolean isCrocodile = random.nextInt(3) == 0;
        if (isCrocodile) {
            halfW += random.nextInt(2) + 1;
            halfD += random.nextInt(2) + 1;
        }

        ResourceLocation spawnerId = isCrocodile ? CROCODILE_ID : HYENA_ID;
        LOGGER.info("[PrideDungeon] PLACED {} dungeon at ({}, {}, {}), halfW={}, halfD={}",
                isCrocodile ? "crocodile" : "hyena", cx, cy, cz, halfW, halfD);

        BlockState prideBrick = LionKingBlocks.PRIDE_BRICK.get().defaultBlockState();
        BlockState mossyBrick = LionKingBlocks.MOSSY_PRIDE_BRICK.get().defaultBlockState();
        BlockState crackedBrick = LionKingBlocks.CRACKED_PRIDE_BRICK.get().defaultBlockState();
        BlockState air = Blocks.CAVE_AIR.defaultBlockState();

        // Build the room: iterate top-down like old mod
        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy + ROOM_HEIGHT; by >= cy - 1; by--) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    boolean isShell = bx == cx - halfW - 1 || by == cy - 1
                            || bz == cz - halfD - 1 || bx == cx + halfW + 1
                            || by == cy + ROOM_HEIGHT + 1 || bz == cz + halfD + 1;

                    if (isShell) {
                        // Shell: skip non-solid blocks unless crocodile (which forces placement)
                        if (!isCrocodile) {
                            BlockPos pos = new BlockPos(bx, by, bz);
                            if (by >= 0 && !level.getBlockState(pos.below()).isSolid()) {
                                level.setBlock(pos, air, 2);
                                continue;
                            }
                            if (!level.getBlockState(pos).isSolid()) {
                                continue;
                            }
                        }

                        // Floor: 75% mossy, 25% regular pride brick
                        if (by == cy - 1) {
                            level.setBlock(
                                    new BlockPos(bx, by, bz),
                                    random.nextInt(4) != 0 ? mossyBrick : prideBrick,
                                    2);
                        } else {
                            // Walls and ceiling: pride brick with occasional cracked
                            level.setBlock(
                                    new BlockPos(bx, by, bz),
                                    random.nextDouble() < CRACKED_BRICK_CHANCE
                                            ? crackedBrick : prideBrick,
                                    2);
                        }
                    } else {
                        // Interior: air
                        level.setBlock(new BlockPos(bx, by, bz), air, 2);
                    }

                    // Crocodile variant: water floor + mossy brick below
                    if (isCrocodile && isInterior(bx, bz, cx, cz, halfW, halfD)
                            && by == cy - 1) {
                        if (bx != cx || bz != cz) {
                            // Water on floor level (not at spawner center)
                            level.setBlock(new BlockPos(bx, by, bz),
                                    Blocks.WATER.defaultBlockState(), 2);
                            // Mossy brick below the water
                            level.setBlock(new BlockPos(bx, by - 1, bz),
                                    random.nextInt(4) != 0 ? mossyBrick : prideBrick, 2);
                        }
                    }
                }
            }
        }

        // Place pride pillars at 2 random corners (matching old mod's j5/j6 logic)
        placePillars(level, random, cx, cy, cz, halfW, halfD);

        // Place loot chests (2 for hyena, 3 for crocodile — matching old mod)
        int chestAttempts = isCrocodile ? 3 : 2;
        placeChests(level, random, cx, cy, cz, halfW, halfD, chestAttempts);

        // Place spawner at room center
        BlockPos spawnerPos = new BlockPos(cx, cy, cz);
        level.setBlock(spawnerPos, LionKingBlocks.LK_SPAWNER.get().defaultBlockState(), 2);
        BlockEntity be = level.getBlockEntity(spawnerPos);
        if (be instanceof SpawnerBlockEntity spawnerBE) {
            spawnerBE.setEntityId(spawnerId);
        }

        // Crocodile variant: add vines on interior walls
        if (isCrocodile) {
            placeVines(level, random, cx, cy, cz, halfW, halfD);
        }

        return true;
    }

    private boolean isInterior(int bx, int bz, int cx, int cz, int halfW, int halfD) {
        return bx > cx - halfW - 1 && bx < cx + halfW + 1
                && bz > cz - halfD - 1 && bz < cz + halfD + 1;
    }

    /**
     * Validate placement using vanilla's dungeon approach: floor and ceiling must be solid,
     * walls may have 1-5 "entrance" positions (air at ground level with air above).
     */
    private boolean isValidPlacement(
            WorldGenLevel level, int cx, int cy, int cz, int halfW, int halfD
    ) {
        int entrances = 0;

        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy - 1; by <= cy + ROOM_HEIGHT + 1; by++) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockPos pos = new BlockPos(bx, by, bz);
                    boolean isSolid = level.getBlockState(pos).isSolid();

                    // Floor must be solid
                    if (by == cy - 1 && !isSolid) {
                        LOGGER.debug("[PrideDungeon] Non-solid floor at ({}, {}, {})", bx, by, bz);
                        return false;
                    }
                    // Ceiling must be solid
                    if (by == cy + ROOM_HEIGHT + 1 && !isSolid) {
                        LOGGER.debug("[PrideDungeon] Non-solid ceiling at ({}, {}, {})", bx, by, bz);
                        return false;
                    }

                    // Count wall entrances at ground level
                    boolean isWall = bx == cx - halfW - 1 || bx == cx + halfW + 1
                            || bz == cz - halfD - 1 || bz == cz + halfD + 1;
                    if (isWall && by == cy
                            && level.isEmptyBlock(pos)
                            && level.isEmptyBlock(pos.above())) {
                        entrances++;
                    }
                }
            }
        }

        // Old mod's Pride Lands had very sparse caves (1 in 15 chunks), so dungeons were
        // buried in solid rock — found by mining. Only reject if too exposed (near surface).
        if (entrances > 5) {
            LOGGER.debug("[PrideDungeon] Too exposed ({} entrances) at ({}, {}, {})",
                    entrances, cx, cy, cz);
            return false;
        }
        return true;
    }

    /** Place pride pillars at 2 random corners, matching old mod's paired corner logic. */
    private void placePillars(
            WorldGenLevel level, RandomSource random,
            int cx, int cy, int cz, int halfW, int halfD
    ) {
        BlockState pillar = LionKingBlocks.PRIDE_PILLAR.get().defaultBlockState();
        int[][] corners = {
                {cx - halfW, cz - halfD},
                {cx + halfW, cz - halfD},
                {cx - halfW, cz + halfD},
                {cx + halfW, cz + halfD},
        };

        // Pick 2 random corners (old mod picked j5 and j6 as indices 0-3, with -1 meaning none)
        int corner1 = random.nextInt(4);
        int corner2 = random.nextInt(4);
        if (random.nextInt(4) == 0) corner1 = -1;
        if (random.nextInt(4) == 0) corner2 = -1;

        for (int i = 0; i < corners.length; i++) {
            if (i == corner1 || i == corner2) {
                for (int dy = 0; dy <= ROOM_HEIGHT; dy++) {
                    level.setBlock(
                            new BlockPos(corners[i][0], cy + dy, corners[i][1]),
                            pillar, 2);
                }
            }
        }
    }

    /**
     * Place chests using vanilla's dungeon approach: random position in interior, must have
     * exactly 1 solid horizontal neighbor (against a wall). Up to chestAttempts chests placed.
     */
    private static final int CHEST_PLACEMENT_RETRIES = 20;

    private void placeChests(
            WorldGenLevel level, RandomSource random,
            int cx, int cy, int cz, int halfW, int halfD, int chestAttempts
    ) {
        int placed = 0;
        for (int attempt = 0; attempt < CHEST_PLACEMENT_RETRIES && placed < chestAttempts; attempt++) {
            int chestX = cx + random.nextInt(halfW * 2 + 1) - halfW;
            int chestZ = cz + random.nextInt(halfD * 2 + 1) - halfD;
            BlockPos chestPos = new BlockPos(chestX, cy, chestZ);

            BlockState existing = level.getBlockState(chestPos);
            if (!existing.isAir() && !existing.is(Blocks.WATER)) {
                continue;
            }

            // Must have exactly 1 solid horizontal neighbor (against wall)
            // and no adjacent chests (prevents double-chest merging)
            int solidNeighbors = 0;
            boolean hasAdjacentChest = false;
            Direction wallDir = Direction.NORTH;
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockState neighbor = level.getBlockState(chestPos.relative(dir));
                if (neighbor.isSolid()) {
                    solidNeighbors++;
                    wallDir = dir;
                }
                if (neighbor.is(Blocks.CHEST)) {
                    hasAdjacentChest = true;
                }
            }
            if (solidNeighbors != 1 || hasAdjacentChest) {
                continue;
            }

            // Face away from the wall (into the room) so the player can open it
            Direction chestFacing = wallDir.getOpposite();
            BlockState chestState = Blocks.CHEST.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, chestFacing);
            level.setBlock(chestPos, chestState, 2);
            fillChestLoot(level, chestPos, random);
            placed++;
        }
    }

    /** Fill a chest with loot matching the old mod's dungeon loot pool. */
    private void fillChestLoot(WorldGenLevel level, BlockPos chestPos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(chestPos);
        if (!(be instanceof ChestBlockEntity chest)) {
            return;
        }

        for (int i = 0; i < CHEST_LOOT_ATTEMPTS; i++) {
            ItemStack loot = getRandomLoot(random);
            if (loot != null) {
                if (loot.isEnchantable() && random.nextInt(3) != 0) {
                    EnchantmentHelper.enchantItem(random, loot, 3, false);
                }
                chest.setItem(random.nextInt(chest.getContainerSize()), loot);
            }
        }

        // 25% chance for a bonus passion sapling
        if (random.nextInt(4) == 0) {
            chest.setItem(
                    random.nextInt(chest.getContainerSize()),
                    new ItemStack(LionKingItems.PASSION_SAPLING_ITEM.get()));
        }
    }

    /**
     * Generate a random loot item matching the old mod's LKDungeonLoot pool:
     * hyena bones, bugs, darts, feathers, silver dart shooter, chocolate mufasa,
     * silver ingots, dart quiver, mangos, peacock gems, compass, jar, silver tools/armor.
     */
    private ItemStack getRandomLoot(RandomSource random) {
        int roll = random.nextInt(12);
        return switch (roll) {
            case 0 -> new ItemStack(LionKingItems.HYENA_BONE.get(), 2 + random.nextInt(3));
            case 1 -> new ItemStack(LionKingItems.BUG.get(), 2 + random.nextInt(4));
            case 2, 3, 4 -> getRandomDartOrFeather(random);
            case 5 -> new ItemStack(LionKingItems.CHOCOLATE_MUFASA.get(), 1 + random.nextInt(3));
            case 6 -> new ItemStack(LionKingItems.SILVER_INGOT.get(), 2 + random.nextInt(3));
            case 7 -> new ItemStack(LionKingItems.DART_QUIVER.get());
            case 8 -> new ItemStack(LionKingItems.MANGO.get(), 1 + random.nextInt(3));
            case 9 -> getRandomTreasure(random);
            case 10, 11 -> getRandomSilverEquipment(random);
            default -> null;
        };
    }

    private ItemStack getRandomDartOrFeather(RandomSource random) {
        return switch (random.nextInt(7)) {
            case 0 -> new ItemStack(LionKingItems.DART_BLUE.get(), 3 + random.nextInt(5));
            case 1 -> new ItemStack(LionKingItems.DART_YELLOW.get(), 3 + random.nextInt(4));
            case 2 -> new ItemStack(LionKingItems.DART_RED.get(), 3 + random.nextInt(4));
            case 3 -> new ItemStack(LionKingItems.FEATHER_BLUE.get(), 3 + random.nextInt(4));
            case 4 -> new ItemStack(LionKingItems.FEATHER_YELLOW.get(), 3 + random.nextInt(3));
            case 5 -> new ItemStack(LionKingItems.FEATHER_RED.get(), 3 + random.nextInt(3));
            case 6 -> new ItemStack(LionKingItems.DART_SHOOTER_SILVER.get());
            default -> new ItemStack(LionKingItems.DART_BLUE.get(), 3 + random.nextInt(5));
        };
    }

    private ItemStack getRandomTreasure(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 0 -> new ItemStack(LionKingItems.PEACOCK_GEM.get(), 1 + random.nextInt(2));
            case 1 -> new ItemStack(Items.COMPASS);
            case 2 -> new ItemStack(LionKingItems.JAR_EMPTY.get());
            default -> new ItemStack(Items.COMPASS);
        };
    }

    private ItemStack getRandomSilverEquipment(RandomSource random) {
        return switch (random.nextInt(7)) {
            case 0 -> new ItemStack(LionKingItems.SILVER_SHOVEL.get());
            case 1 -> new ItemStack(LionKingItems.SILVER_PICKAXE.get());
            case 2 -> new ItemStack(LionKingItems.SILVER_AXE.get());
            case 3 -> new ItemStack(LionKingItems.SILVER_SWORD.get());
            case 4 -> new ItemStack(LionKingItems.SILVER_HELMET.get());
            case 5 -> new ItemStack(LionKingItems.SILVER_BOOTS.get());
            case 6 -> new ItemStack(LionKingItems.NOTE_B.get(), 1 + random.nextInt(3));
            default -> new ItemStack(LionKingItems.SILVER_SWORD.get());
        };
    }

    /** Place vines on interior wall faces of crocodile dungeons, matching old mod logic. */
    private void placeVines(
            WorldGenLevel level, RandomSource random,
            int cx, int cy, int cz, int halfW, int halfD
    ) {
        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy + ROOM_HEIGHT; by >= cy - 1; by--) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockState state = level.getBlockState(new BlockPos(bx, by, bz));
                    if (!state.is(LionKingBlocks.PRIDESTONE.get())
                            && !state.is(LionKingBlocks.PRIDE_BRICK.get())
                            && !state.is(LionKingBlocks.CRACKED_PRIDE_BRICK.get())) {
                        continue;
                    }

                    tryPlaceVine(level, random, bx - 1, by, bz, VineBlock.EAST);
                    tryPlaceVine(level, random, bx + 1, by, bz, VineBlock.WEST);
                    tryPlaceVine(level, random, bx, by, bz - 1, VineBlock.SOUTH);
                    tryPlaceVine(level, random, bx, by, bz + 1, VineBlock.NORTH);
                }
            }
        }
    }

    private void tryPlaceVine(
            WorldGenLevel level, RandomSource random,
            int x, int y, int z, BooleanProperty facing
    ) {
        if (random.nextInt(VINE_CHANCE) != 0) {
            return;
        }
        BlockPos pos = new BlockPos(x, y, z);
        if (!level.isEmptyBlock(pos)) {
            return;
        }

        BlockState vine = Blocks.VINE.defaultBlockState().setValue(facing, true);
        level.setBlock(pos, vine, 2);

        // Grow vine downward
        int length = 2 + random.nextInt(VINE_MAX_LENGTH - 2);
        for (int dy = 1; dy <= length; dy++) {
            BlockPos below = pos.below(dy);
            if (!level.isEmptyBlock(below)) {
                break;
            }
            level.setBlock(below, vine, 2);
        }
    }
}
