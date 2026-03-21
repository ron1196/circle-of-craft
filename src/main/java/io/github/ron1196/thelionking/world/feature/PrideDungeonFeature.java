package io.github.ron1196.thelionking.world.feature;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.SpawnerBlockEntity;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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
import org.slf4j.Logger;

/**
 * Pride Dungeon — underground dungeon rooms in the Outlands dimension, ported from the old mod's
 * LKWorldGenDungeons.
 *
 * <p>Two variants: Hyena (2/3 chance) and Crocodile (1/3 chance). Crocodile dungeons are
 * larger, have a water floor, and vines on walls. Both use pride brick walls with cracked
 * brick accents, 75/25 mossy/regular pride brick floors, and LK-themed loot chests.
 *
 * <p>Placement follows vanilla's MonsterRoomFeature approach: rooms require 1-5 cave
 * entrances (wall openings), solid floor and ceiling, and use safeSetBlock to respect
 * the FEATURES_CANNOT_REPLACE tag.
 */
public class PrideDungeonFeature extends Feature<NoneFeatureConfiguration> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int ROOM_HEIGHT = 3;
    private static final int CHEST_LOOT_ATTEMPTS = 8;
    private static final int CHEST_PLACEMENT_RETRIES = 20;
    private static final int VINE_MAX_LENGTH = 6;
    private static final int VINE_CHANCE = 4;
    private static final double CRACKED_BRICK_CHANCE = 0.1;
    private static final int MIN_ENTRANCES = 1;
    private static final int MAX_ENTRANCES = 5;

    private static final ResourceLocation HYENA_ID = new ResourceLocation(TheLionKingMod.MOD_ID, "hyena");
    private static final ResourceLocation CROCODILE_ID = new ResourceLocation(TheLionKingMod.MOD_ID, "crocodile");

    public PrideDungeonFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Predicate<BlockState> canReplace = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
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
            return false;
        }

        // Determine variant and expand AFTER validation (old mod expanded post-validation)
        boolean isCrocodile = random.nextInt(3) == 0;
        if (isCrocodile) {
            halfW += random.nextInt(2) + 1;
            halfD += random.nextInt(2) + 1;
        }

        ResourceLocation spawnerId = isCrocodile ? CROCODILE_ID : HYENA_ID;
        LOGGER.info(
                "[PrideDungeon] Placed {} dungeon at ({}, {}, {}), halfW={}, halfD={}",
                isCrocodile ? "crocodile" : "hyena",
                cx,
                cy,
                cz,
                halfW,
                halfD);

        buildRoom(level, random, canReplace, cx, cy, cz, halfW, halfD, isCrocodile);
        placePillars(level, random, cx, cy, cz, halfW, halfD);

        int chestAttempts = isCrocodile ? 3 : 2;
        placeChests(level, random, cx, cy, cz, halfW, halfD, chestAttempts);

        placeSpawner(level, cx, cy, cz, spawnerId);

        if (isCrocodile) {
            placeVines(level, random, cx, cy, cz, halfW, halfD);
        }

        return true;
    }

    /**
     * Validate placement using vanilla's dungeon approach: floor and ceiling must be solid,
     * walls may have 0-5 "entrance" positions (air at ground level with air above).
     * Rooms must have at least 1 entrance to be reachable via caves.
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
                        return false;
                    }
                    // Ceiling must be solid (ROOM_HEIGHT + 1 above origin = 1 above interior)
                    if (by == cy + ROOM_HEIGHT + 1 && !isSolid) {
                        return false;
                    }

                    // Count wall entrances at ground level
                    boolean isWall = bx == cx - halfW - 1
                            || bx == cx + halfW + 1
                            || bz == cz - halfD - 1
                            || bz == cz + halfD + 1;
                    if (isWall && by == cy && level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above())) {
                        entrances++;
                    }
                }
            }
        }

        // Require at least 1 cave opening so the room is reachable,
        // but reject if too exposed (more than 5 openings means near surface or large cave).
        return entrances >= MIN_ENTRANCES && entrances <= MAX_ENTRANCES;
    }

    /**
     * Build the dungeon room shell and interior, following vanilla's MonsterRoomFeature pattern:
     * shell blocks with no solid support become air, solid shell blocks become dungeon material,
     * interior becomes cave air.
     */
    private void buildRoom(
            WorldGenLevel level,
            RandomSource random,
            Predicate<BlockState> canReplace,
            int cx, int cy, int cz,
            int halfW, int halfD,
            boolean isCrocodile
    ) {
        BlockState prideBrick = LionKingBlocks.PRIDE_BRICK.get().defaultBlockState();
        BlockState mossyBrick = LionKingBlocks.MOSSY_PRIDE_BRICK.get().defaultBlockState();
        BlockState crackedBrick = LionKingBlocks.CRACKED_PRIDE_BRICK.get().defaultBlockState();
        BlockState air = Blocks.CAVE_AIR.defaultBlockState();

        // Iterate top-down like vanilla MonsterRoomFeature
        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy + ROOM_HEIGHT; by >= cy - 1; by--) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockPos pos = new BlockPos(bx, by, bz);
                    BlockState existing = level.getBlockState(pos);
                    boolean isInterior = isInterior(bx, by, bz, cx, cy, cz, halfW, halfD);

                    if (isInterior) {
                        // Interior: clear to cave air (preserve chests and spawners)
                        if (!existing.is(Blocks.CHEST) && !existing.is(LionKingBlocks.LK_SPAWNER.get())) {
                            this.safeSetBlock(level, pos, air, canReplace);
                        }
                    } else {
                        // Shell (walls, floor, ceiling)
                        if (pos.getY() >= level.getMinBuildHeight()
                                && !level.getBlockState(pos.below()).isSolid()) {
                            // Floating shell block: set to air (vanilla behavior)
                            level.setBlock(pos, air, 2);
                        } else if (existing.isSolid() && !existing.is(Blocks.CHEST)) {
                            // Solid shell: replace with dungeon material
                            if (by == cy - 1) {
                                // Floor: 75% mossy, 25% regular pride brick
                                this.safeSetBlock(
                                        level, pos,
                                        random.nextInt(4) != 0 ? mossyBrick : prideBrick,
                                        canReplace);
                            } else {
                                // Walls and ceiling: pride brick with occasional cracked
                                this.safeSetBlock(
                                        level, pos,
                                        random.nextDouble() < CRACKED_BRICK_CHANCE ? crackedBrick : prideBrick,
                                        canReplace);
                            }
                        }
                    }
                }
            }
        }

        // Crocodile variant: water floor with mossy brick beneath
        if (isCrocodile) {
            for (int bx = cx - halfW; bx <= cx + halfW; bx++) {
                for (int bz = cz - halfD; bz <= cz + halfD; bz++) {
                    // Skip spawner center position
                    if (bx == cx && bz == cz) {
                        continue;
                    }
                    BlockPos floorPos = new BlockPos(bx, cy, bz);
                    level.setBlock(floorPos, Blocks.WATER.defaultBlockState(), 2);
                    // Ensure solid floor under the water
                    BlockPos belowFloor = new BlockPos(bx, cy - 1, bz);
                    this.safeSetBlock(
                            level, belowFloor,
                            random.nextInt(4) != 0 ? mossyBrick : prideBrick,
                            canReplace);
                }
            }
        }
    }

    /** Check if a position is in the room interior (not shell). */
    private boolean isInterior(
            int bx, int by, int bz,
            int cx, int cy, int cz,
            int halfW, int halfD
    ) {
        return bx > cx - halfW - 1 && bx < cx + halfW + 1
                && by > cy - 1 && by < cy + ROOM_HEIGHT
                && bz > cz - halfD - 1 && bz < cz + halfD + 1;
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
                    level.setBlock(new BlockPos(corners[i][0], cy + dy, corners[i][1]), pillar, 2);
                }
            }
        }
    }

    private void placeSpawner(WorldGenLevel level, int cx, int cy, int cz, ResourceLocation spawnerId) {
        BlockPos spawnerPos = new BlockPos(cx, cy, cz);
        level.setBlock(spawnerPos, LionKingBlocks.LK_SPAWNER.get().defaultBlockState(), 2);
        BlockEntity be = level.getBlockEntity(spawnerPos);
        if (be instanceof SpawnerBlockEntity spawnerBE) {
            spawnerBE.setEntityId(spawnerId);
        } else {
            LOGGER.error("[PrideDungeon] Failed to get SpawnerBlockEntity at ({}, {}, {})", cx, cy, cz);
        }
    }

    /**
     * Place chests using vanilla's dungeon approach: random position in interior, must have
     * exactly 1 solid horizontal neighbor (against a wall). Up to chestAttempts chests placed.
     */
    private void placeChests(
            WorldGenLevel level, RandomSource random,
            int cx, int cy, int cz, int halfW, int halfD,
            int chestAttempts
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
            BlockState chestState =
                    Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, chestFacing);
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
                    random.nextInt(chest.getContainerSize()), new ItemStack(LionKingItems.PASSION_SAPLING_ITEM.get()));
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

    /** Place vines on interior wall faces of crocodile dungeons. */
    private void placeVines(
            WorldGenLevel level, RandomSource random,
            int cx, int cy, int cz, int halfW, int halfD
    ) {
        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy + ROOM_HEIGHT; by >= cy; by--) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockState state = level.getBlockState(new BlockPos(bx, by, bz));
                    if (!state.is(LionKingBlocks.PRIDE_BRICK.get())
                            && !state.is(LionKingBlocks.CRACKED_PRIDE_BRICK.get())
                            && !state.is(LionKingBlocks.MOSSY_PRIDE_BRICK.get())) {
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
