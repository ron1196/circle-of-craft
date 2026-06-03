package io.github.ron1196.circleofcraft.world.feature;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.world.feature.FeatureHelper.LootEntry;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.slf4j.Logger;

/**
 * Underground dungeon rooms for Pride Lands and Outlands, ported from the old mod's
 * WorldGenDungeons. Outlands dungeons use better loot (kivulite, corrupt tools).
 *
 * <p>Two variants: Hyena (2/3 chance) and Crocodile (1/3 chance). Crocodile dungeons are
 * larger, have a water floor, and vines on walls. Both use pride brick walls with cracked
 * brick accents, 75/25 mossy/regular pride brick floors, and LK-themed loot chests.
 *
 * <p>Placement follows vanilla's MonsterRoomFeature approach: rooms require 1-5 cave
 * entrances (wall openings), solid floor and ceiling, and use safeSetBlock to respect
 * the FEATURES_CANNOT_REPLACE tag.
 */
public class DungeonFeature extends Feature<NoneFeatureConfiguration> {

    private static final Logger LOGGER = LogUtils.getLogger();

    // Room geometry
    private static final int ROOM_HEIGHT = 3;
    private static final int BASE_HALF_SIZE_MIN = 2;
    private static final int BASE_HALF_SIZE_RANGE = 2; // nextInt(2) gives 0-1, so half-size = 2-3
    private static final int CROC_EXPANSION_MIN = 1;
    private static final int CROC_EXPANSION_RANGE = 2; // nextInt(2) gives 0-1, so expansion = 1-2
    private static final int CORNER_COUNT = 4;

    // Variant selection
    private static final int CROCODILE_CHANCE = 3; // 1 in 3 chance for crocodile variant

    // Entrance validation
    private static final int MIN_ENTRANCES = 1;
    private static final int MAX_ENTRANCES = 10;

    // Floor/ceiling integrity: tolerate up to this fraction of air gaps in either ring
    // (build phase converts unsupported shell blocks to air, so partial floors are visually fine).
    private static final double FLOOR_CEILING_AIR_TOLERANCE = 0.25;

    // Block material chances
    private static final double CRACKED_BRICK_CHANCE = 0.1;
    private static final int MOSSY_FLOOR_DENOMINATOR = 4; // 3 in 4 = 75% mossy
    private static final int PILLAR_SKIP_CHANCE = 4; // 1 in 4 chance to skip a pillar

    // Chest and loot
    private static final int HYENA_CHEST_COUNT = 2;
    private static final int CROC_CHEST_COUNT = 3;
    private static final int CHEST_LOOT_ATTEMPTS = 8;
    private static final int CHEST_PLACEMENT_RETRIES = 20;
    private static final int REQUIRED_SOLID_NEIGHBORS = 1;
    private static final int ENCHANT_CHANCE = 3; // 2 in 3 chance to enchant
    private static final int ENCHANT_LEVEL = 3;
    private static final int PASSION_SAPLING_CHANCE = 4; // 1 in 4

    // Vines (crocodile variant only)
    private static final int VINE_MAX_LENGTH = 6;
    private static final int VINE_CHANCE = 4; // 1 in 4 chance per wall face

    // ── Dungeon loot sub-pools ─────────────────────────────────────────────

    private static final List<LootEntry> DARTS_AND_FEATHERS = List.of(
            LootEntry.of(ModItems.DART_BLUE, 3, 5),
            LootEntry.of(ModItems.DART_YELLOW, 3, 4),
            LootEntry.of(ModItems.DART_RED, 3, 4),
            LootEntry.of(ModItems.FEATHER_BLUE, 3, 4),
            LootEntry.of(ModItems.FEATHER_YELLOW, 3, 3),
            LootEntry.of(ModItems.FEATHER_RED, 3, 3),
            LootEntry.of(ModItems.DART_SHOOTER_SILVER));

    private static final List<LootEntry> SILVER_EQUIPMENT = List.of(
            LootEntry.of(ModItems.SILVER_SHOVEL),
            LootEntry.of(ModItems.SILVER_PICKAXE),
            LootEntry.of(ModItems.SILVER_AXE),
            LootEntry.of(ModItems.SILVER_SWORD),
            LootEntry.of(ModItems.SILVER_HELMET),
            LootEntry.of(ModItems.SILVER_BOOTS),
            LootEntry.of(ModItems.NOTE_B, 1, 3));

    private static final List<LootEntry> TREASURES = List.of(
            LootEntry.of(ModItems.PEACOCK_GEM, 1, 2),
            LootEntry.of(() -> Items.COMPASS),
            LootEntry.of(ModItems.JAR_EMPTY));

    // ── Main dungeon loot table (matching old mod's DungeonLoot pool) ──
    // Weight controls relative probability. Higher weight = more common.
    private static final List<LootEntry> DUNGEON_LOOT = List.of(
            // Common supplies
            LootEntry.of(ModItems.HYENA_BONE, 2, 3),
            LootEntry.of(ModItems.BUG, 2, 4),
            LootEntry.of(ModItems.CHOCOLATE_MUFASA, 1, 3),
            LootEntry.of(ModItems.SILVER_INGOT, 2, 3),
            LootEntry.of(ModItems.MANGO, 1, 3),

            // Darts and feathers (weight 3 — most common drop)
            LootEntry.of(3, r -> FeatureHelper.pickLoot(DARTS_AND_FEATHERS, r)),

            // Equipment
            // LootEntry.of(ModItems.DART_QUIVER), // disabled, see issue #78
            LootEntry.of(2, r -> FeatureHelper.pickLoot(SILVER_EQUIPMENT, r)),

            // Rare treasures
            LootEntry.of(r -> FeatureHelper.pickLoot(TREASURES, r)));

    // ── Outlands dungeon loot (better rewards for a more dangerous dimension) ──

    private static final List<LootEntry> OUTLANDS_EQUIPMENT = List.of(
            LootEntry.of(ModItems.KIVULITE_SWORD),
            LootEntry.of(ModItems.KIVULITE_PICKAXE),
            LootEntry.of(ModItems.KIVULITE_AXE),
            LootEntry.of(ModItems.CORRUPT_SWORD),
            LootEntry.of(ModItems.CORRUPT_PICKAXE));

    private static final List<LootEntry> OUTLANDS_LOOT = List.of(
            // Common Outlands supplies
            LootEntry.of(ModItems.NUKA_SHARD, 3, 6),
            LootEntry.of(ModItems.KIVULITE, 2, 4),
            LootEntry.of(ModItems.SILVER_INGOT, 3, 4),
            LootEntry.of(ModItems.CHOCOLATE_MUFASA, 2, 4),

            // Outlands darts (black darts are Outlands-exclusive)
            LootEntry.of(3, ModItems.DART_BLACK, 4, 6),
            LootEntry.of(ModItems.DART_SHOOTER_SILVER),

            // Outlands equipment (better than Pride Lands silver)
            LootEntry.of(2, r -> FeatureHelper.pickLoot(OUTLANDS_EQUIPMENT, r)),

            // Rare treasures
            LootEntry.of(ModItems.PEACOCK_GEM, 2, 3),
            LootEntry.of(ModItems.ZIRA_COIN),
            LootEntry.of(ModItems.JAR_EMPTY));

    enum DungeonVariant {
        HYENA(EntityTypes.HYENA, HYENA_CHEST_COUNT, false),
        CROCODILE(EntityTypes.CROCODILE, CROC_CHEST_COUNT, true);

        final Supplier<? extends EntityType<?>> entityType;
        final int chestCount;
        final boolean aquatic;

        DungeonVariant(Supplier<? extends EntityType<?>> entityType, int chestCount, boolean aquatic) {
            this.entityType = entityType;
            this.chestCount = chestCount;
            this.aquatic = aquatic;
        }
    }

    public DungeonFeature(Codec<NoneFeatureConfiguration> codec) {
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
        int halfW = random.nextInt(BASE_HALF_SIZE_RANGE) + BASE_HALF_SIZE_MIN;
        int halfD = random.nextInt(BASE_HALF_SIZE_RANGE) + BASE_HALF_SIZE_MIN;

        // Validate with base room size FIRST (matching old mod order — validate small, expand later)
        if (!isValidPlacement(level, cx, cy, cz, halfW, halfD)) {
            return false;
        }

        // Determine variant and expand AFTER validation (old mod expanded post-validation)
        DungeonVariant variant =
                random.nextInt(CROCODILE_CHANCE) == 0 ? DungeonVariant.CROCODILE : DungeonVariant.HYENA;
        if (variant.aquatic) {
            halfW += random.nextInt(CROC_EXPANSION_RANGE) + CROC_EXPANSION_MIN;
            halfD += random.nextInt(CROC_EXPANSION_RANGE) + CROC_EXPANSION_MIN;
        }

        DungeonPalette palette =
                DungeonPalette.forDimension(level.getLevel().dimension().location());
        boolean isOutlands = palette == DungeonPalette.OUTLANDS;

        LOGGER.debug(
                "[Dungeon] Placed {} dungeon at ({}, {}, {}), halfW={}, halfD={}", variant, cx, cy, cz, halfW, halfD);

        buildRoom(level, random, canReplace, cx, cy, cz, halfW, halfD, variant, palette);
        placePillars(level, random, cx, cy, cz, halfW, halfD, palette);

        placeChests(level, random, cx, cy, cz, halfW, halfD, variant.chestCount, isOutlands);

        placeSpawner(level, random, cx, cy, cz, variant.entityType.get());

        if (variant.aquatic) {
            placeVines(level, random, cx, cy, cz, halfW, halfD, palette);
        }

        return true;
    }

    /**
     * Validate placement using vanilla's dungeon approach: floor and ceiling must be solid,
     * walls may have 0-5 "entrance" positions (air at ground level with air above).
     * Rooms must have at least 1 entrance to be reachable via caves.
     */
    private boolean isValidPlacement(WorldGenLevel level, int cx, int cy, int cz, int halfW, int halfD) {
        int entrances = 0;
        int floorAir = 0;
        int ceilingAir = 0;
        int ringSize = (2 * halfW + 3) * (2 * halfD + 3);
        int maxAir = (int) Math.floor(ringSize * FLOOR_CEILING_AIR_TOLERANCE);

        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy - 1; by <= cy + ROOM_HEIGHT + 1; by++) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockPos pos = new BlockPos(bx, by, bz);
                    boolean isSolid = level.getBlockState(pos).isSolid();

                    // Floor: tolerate a few air gaps (build phase fills them with air anyway)
                    if (by == cy - 1 && !isSolid && ++floorAir > maxAir) {
                        return false;
                    }
                    // Ceiling (ROOM_HEIGHT + 1 above origin = 1 above interior)
                    if (by == cy + ROOM_HEIGHT + 1 && !isSolid && ++ceilingAir > maxAir) {
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
            int cx,
            int cy,
            int cz,
            int halfW,
            int halfD,
            DungeonVariant variant,
            DungeonPalette palette) {
        BlockState floorPrimary = palette.floorPrimary.get().defaultBlockState();
        BlockState floorAccent = palette.floorAccent.get().defaultBlockState();
        BlockState wallPrimary = palette.wallPrimary.get().defaultBlockState();
        BlockState wallAccent = palette.wallAccent.get().defaultBlockState();
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
                        if (!existing.is(Blocks.CHEST) && !existing.is(Blocks.SPAWNER)) {
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
                                // Floor: 75% primary (mossy), 25% accent (plain brick)
                                this.safeSetBlock(
                                        level,
                                        pos,
                                        random.nextInt(MOSSY_FLOOR_DENOMINATOR) != 0 ? floorPrimary : floorAccent,
                                        canReplace);
                            } else {
                                // Walls and ceiling: primary brick with occasional accent (cracked)
                                this.safeSetBlock(
                                        level,
                                        pos,
                                        random.nextDouble() < CRACKED_BRICK_CHANCE ? wallAccent : wallPrimary,
                                        canReplace);
                            }
                        }
                    }
                }
            }
        }

        // Aquatic variant: water floor with mossy brick beneath
        if (variant.aquatic) {
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
                            level,
                            belowFloor,
                            random.nextInt(MOSSY_FLOOR_DENOMINATOR) != 0 ? floorPrimary : floorAccent,
                            canReplace);
                }
            }
        }
    }

    /**
     * Check if a position is in the room interior (not shell).
     */
    private boolean isInterior(int bx, int by, int bz, int cx, int cy, int cz, int halfW, int halfD) {
        return bx > cx - halfW - 1
                && bx < cx + halfW + 1
                && by > cy - 1
                && by < cy + ROOM_HEIGHT
                && bz > cz - halfD - 1
                && bz < cz + halfD + 1;
    }

    /**
     * Place pride pillars at 2 random corners, matching old mod's paired corner logic.
     */
    private void placePillars(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            int halfW,
            int halfD,
            DungeonPalette palette) {
        BlockState pillar = palette.pillar.get().defaultBlockState();
        int[][] corners = {
            {cx - halfW, cz - halfD},
            {cx + halfW, cz - halfD},
            {cx - halfW, cz + halfD},
            {cx + halfW, cz + halfD},
        };

        // Pick 2 random corners (old mod picked j5 and j6 as indices 0-3, with -1 meaning none)
        int corner1 = random.nextInt(CORNER_COUNT);
        int corner2 = random.nextInt(CORNER_COUNT);
        if (random.nextInt(PILLAR_SKIP_CHANCE) == 0) corner1 = -1;
        if (random.nextInt(PILLAR_SKIP_CHANCE) == 0) corner2 = -1;

        for (int i = 0; i < corners.length; i++) {
            if (i == corner1 || i == corner2) {
                for (int dy = 0; dy <= ROOM_HEIGHT; dy++) {
                    level.setBlock(new BlockPos(corners[i][0], cy + dy, corners[i][1]), pillar, 2);
                }
            }
        }
    }

    private void placeSpawner(
            WorldGenLevel level, RandomSource random, int cx, int cy, int cz, EntityType<?> spawnerEntity) {
        BlockPos spawnerPos = new BlockPos(cx, cy, cz);
        level.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
        BlockEntity be = level.getBlockEntity(spawnerPos);
        if (!(be instanceof SpawnerBlockEntity spawnerBE)) {
            return;
        }
        spawnerBE.setEntityId(spawnerEntity, random);
    }

    /**
     * Place chests using vanilla's dungeon approach: random position in interior, must have
     * exactly 1 solid horizontal neighbor (against a wall). Up to chestAttempts chests placed.
     */
    private void placeChests(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            int halfW,
            int halfD,
            int chestAttempts,
            boolean isOutlands) {
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
            if (solidNeighbors != REQUIRED_SOLID_NEIGHBORS || hasAdjacentChest) {
                continue;
            }

            // Face away from the wall (into the room) so the player can open it
            Direction chestFacing = wallDir.getOpposite();
            BlockState chestState =
                    Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, chestFacing);
            level.setBlock(chestPos, chestState, 2);
            fillChestLoot(level, chestPos, random, isOutlands);
            placed++;
        }
    }

    /**
     * Fill a chest with loot matching the old mod's dungeon loot pool.
     */
    private void fillChestLoot(WorldGenLevel level, BlockPos chestPos, RandomSource random, boolean isOutlands) {
        BlockEntity be = level.getBlockEntity(chestPos);
        if (!(be instanceof ChestBlockEntity chest)) {
            return;
        }

        List<LootEntry> lootTable = isOutlands ? OUTLANDS_LOOT : DUNGEON_LOOT;
        for (int i = 0; i < CHEST_LOOT_ATTEMPTS; i++) {
            ItemStack loot = FeatureHelper.pickLoot(lootTable, random);
            if (loot != null) {
                if (loot.isEnchantable() && random.nextInt(ENCHANT_CHANCE) != 0) {
                    FeatureHelper.enchantWithTableEnchantments(level.registryAccess(), random, loot, ENCHANT_LEVEL);
                }
                chest.setItem(random.nextInt(chest.getContainerSize()), loot);
            }
        }

        // 25% chance for a bonus passion sapling
        if (random.nextInt(PASSION_SAPLING_CHANCE) == 0) {
            ItemStack passionItemStack = new ItemStack(ModItems.PASSION_SAPLING_ITEM.get());
            chest.setItem(random.nextInt(chest.getContainerSize()), passionItemStack);
        }
    }

    /**
     * Place vines on interior wall faces of crocodile dungeons.
     */
    private void placeVines(
            WorldGenLevel level,
            RandomSource random,
            int cx,
            int cy,
            int cz,
            int halfW,
            int halfD,
            DungeonPalette palette) {
        Block wallPrimaryBlock = palette.wallPrimary.get();
        Block wallAccentBlock = palette.wallAccent.get();
        Block floorPrimaryBlock = palette.floorPrimary.get();
        Block floorAccentBlock = palette.floorAccent.get();
        for (int bx = cx - halfW - 1; bx <= cx + halfW + 1; bx++) {
            for (int by = cy + ROOM_HEIGHT; by >= cy; by--) {
                for (int bz = cz - halfD - 1; bz <= cz + halfD + 1; bz++) {
                    BlockState state = level.getBlockState(new BlockPos(bx, by, bz));

                    if (!state.is(wallPrimaryBlock)
                            && !state.is(wallAccentBlock)
                            && !state.is(floorPrimaryBlock)
                            && !state.is(floorAccentBlock)) {
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

    private void tryPlaceVine(WorldGenLevel level, RandomSource random, int x, int y, int z, BooleanProperty facing) {
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
