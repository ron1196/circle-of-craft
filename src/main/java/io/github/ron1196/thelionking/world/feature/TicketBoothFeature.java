package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ticket Booth — a ~14x13 theater building with seating, a portal frame screen,
 * a Ticket Lion NPC, a loot chest, sign, and decorative details.
 * Faithfully ported from the original Lion King mod's LKWorldGenTicketBooth.
 */
public class TicketBoothFeature extends Feature<NoneFeatureConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketBoothFeature.class);

    public TicketBoothFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        LOGGER.info("Ticket Booth generating at ({}, {}, {})", origin.getX(), origin.getY(), origin.getZ());

        // Block palette
        BlockState pridestone = LKBlocks.PRIDESTONE.get().defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState stoneBricks = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState wool = Blocks.WHITE_WOOL.defaultBlockState();
        BlockState glowstone = Blocks.GLOWSTONE.defaultBlockState();
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        BlockState glassPane = Blocks.GLASS_PANE.defaultBlockState();
        BlockState portalFrame = LKBlocks.PRIDE_PORTAL_FRAME.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // Building dimensions: 14 wide (X: 0..13), 13 deep (Z: 0..12)
        // origin is the front-left corner at ground level
        // Front faces south (positive Z direction is back/screen end)
        int width = 14;
        int depth = 13;

        // ============================================================
        // FLOOR: Pridestone floor with support fill underneath
        // ============================================================
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                // Fill below floor down a few blocks for support on uneven terrain
                for (int y = -3; y < 0; y++) {
                    BlockPos fillPos = origin.offset(x, y, z);
                    if (!level.getBlockState(fillPos).isSolidRender(level, fillPos)) {
                        level.setBlock(fillPos, pridestone, 2);
                    }
                }
                // Floor level
                level.setBlock(origin.offset(x, 0, z), pridestone, 2);
            }
        }

        // ============================================================
        // CLEAR INTERIOR: Air out the building volume
        // ============================================================
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                for (int y = 1; y <= 6; y++) {
                    level.setBlock(origin.offset(x, y, z), air, 2);
                }
            }
        }

        // ============================================================
        // WALLS: Plank walls, Y+1 to Y+4, with stone brick accent at Y+2
        // ============================================================
        for (int y = 1; y <= 4; y++) {
            BlockState wallBlock = (y == 2) ? stoneBricks : planks;

            // Left wall (x=0)
            for (int z = 0; z < depth; z++) {
                level.setBlock(origin.offset(0, y, z), wallBlock, 2);
            }
            // Right wall (x=width-1)
            for (int z = 0; z < depth; z++) {
                level.setBlock(origin.offset(width - 1, y, z), wallBlock, 2);
            }
            // Back wall (z=depth-1) — the screen end
            for (int x = 0; x < width; x++) {
                level.setBlock(origin.offset(x, y, depth - 1), wallBlock, 2);
            }
            // Front wall (z=0) — with door opening
            for (int x = 0; x < width; x++) {
                // Leave door gap at center: x=6 and x=7
                if (x == 6 || x == 7) {
                    if (y <= 2) continue; // Door opening 2 blocks high
                }
                level.setBlock(origin.offset(x, y, 0), wallBlock, 2);
            }
        }

        // ============================================================
        // GLOWSTONE at 4 corner pillars at Y+2
        // ============================================================
        level.setBlock(origin.offset(0, 2, 0), glowstone, 2);
        level.setBlock(origin.offset(width - 1, 2, 0), glowstone, 2);
        level.setBlock(origin.offset(0, 2, depth - 1), glowstone, 2);
        level.setBlock(origin.offset(width - 1, 2, depth - 1), glowstone, 2);

        // ============================================================
        // GLASS PANES: Windows on side walls at Y+3
        // ============================================================
        for (int z = 2; z <= depth - 3; z += 2) {
            // Left wall windows
            level.setBlock(origin.offset(0, 3, z), glassPane, 2);
            // Right wall windows
            level.setBlock(origin.offset(width - 1, 3, z), glassPane, 2);
        }

        // ============================================================
        // DOOR: Oak door at front center
        // ============================================================
        BlockPos doorPos = origin.offset(6, 1, 0);
        level.setBlock(doorPos, Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.FACING, Direction.SOUTH), 2);
        level.setBlock(doorPos.above(), Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoorBlock.FACING, Direction.SOUTH), 2);

        // Second door leaf
        BlockPos doorPos2 = origin.offset(7, 1, 0);
        level.setBlock(doorPos2, Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.FACING, Direction.SOUTH), 2);
        level.setBlock(doorPos2.above(), Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                .setValue(DoorBlock.FACING, Direction.SOUTH), 2);

        // ============================================================
        // ROOF: Wool flat roof at Y+5
        // ============================================================
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                level.setBlock(origin.offset(x, 5, z), wool, 2);
            }
        }

        // ============================================================
        // STAIR ROOF OVERHANG: Stairs around all 4 sides at Y+5
        // ============================================================
        // Front overhang (z=-1), stairs facing south (outward)
        for (int x = -1; x <= width; x++) {
            level.setBlock(origin.offset(x, 5, -1),
                    Blocks.OAK_STAIRS.defaultBlockState()
                            .setValue(StairBlock.FACING, Direction.SOUTH)
                            .setValue(StairBlock.HALF, Half.TOP), 2);
        }
        // Back overhang (z=depth), stairs facing north (outward)
        for (int x = -1; x <= width; x++) {
            level.setBlock(origin.offset(x, 5, depth),
                    Blocks.OAK_STAIRS.defaultBlockState()
                            .setValue(StairBlock.FACING, Direction.NORTH)
                            .setValue(StairBlock.HALF, Half.TOP), 2);
        }
        // Left overhang (x=-1), stairs facing east (outward)
        for (int z = 0; z < depth; z++) {
            level.setBlock(origin.offset(-1, 5, z),
                    Blocks.OAK_STAIRS.defaultBlockState()
                            .setValue(StairBlock.FACING, Direction.EAST)
                            .setValue(StairBlock.HALF, Half.TOP), 2);
        }
        // Right overhang (x=width), stairs facing west (outward)
        for (int z = 0; z < depth; z++) {
            level.setBlock(origin.offset(width, 5, z),
                    Blocks.OAK_STAIRS.defaultBlockState()
                            .setValue(StairBlock.FACING, Direction.WEST)
                            .setValue(StairBlock.HALF, Half.TOP), 2);
        }

        // ============================================================
        // PORTAL FRAME "SCREEN" at back wall (z=depth-2), 4 wide x 5 tall
        // Centered on the back wall interior
        // ============================================================
        int screenStartX = (width / 2) - 2; // center a 4-wide frame
        for (int sx = 0; sx < 4; sx++) {
            for (int sy = 0; sy < 5; sy++) {
                // Frame is the border; interior could be air or portal
                // Place full frame for now (4x5 solid portal frame blocks)
                level.setBlock(origin.offset(screenStartX + sx, 1 + sy, depth - 2), portalFrame, 2);
            }
        }

        // ============================================================
        // SEATING: Stair blocks in rows, facing the screen (north)
        // Rows at z=3, z=5, z=7, z=9 — alternating with aisles
        // ============================================================
        for (int rowZ : new int[]{3, 5, 7, 9}) {
            for (int x = 2; x <= width - 3; x++) {
                // Leave a center aisle at x=6 and x=7
                if (x == 6 || x == 7) continue;
                level.setBlock(origin.offset(x, 1, rowZ),
                        Blocks.OAK_STAIRS.defaultBlockState()
                                .setValue(StairBlock.FACING, Direction.NORTH), 2);
            }
        }

        // ============================================================
        // FENCE divider between seating and screen area
        // ============================================================
        for (int x = 1; x < width - 1; x++) {
            if (x == 6 || x == 7) continue; // center aisle gap
            level.setBlock(origin.offset(x, 1, 2), fence, 2);
        }

        // ============================================================
        // WALL TORCHES on interior walls
        // ============================================================
        // Left wall torches (facing east, into the room)
        for (int z = 2; z <= depth - 3; z += 3) {
            level.setBlock(origin.offset(1, 3, z),
                    Blocks.WALL_TORCH.defaultBlockState()
                            .setValue(WallTorchBlock.FACING, Direction.EAST), 2);
        }
        // Right wall torches (facing west, into the room)
        for (int z = 2; z <= depth - 3; z += 3) {
            level.setBlock(origin.offset(width - 2, 3, z),
                    Blocks.WALL_TORCH.defaultBlockState()
                            .setValue(WallTorchBlock.FACING, Direction.WEST), 2);
        }

        // ============================================================
        // SIGN on front exterior wall
        // ============================================================
        BlockPos signPos = origin.offset(5, 3, -1);
        // Place as a wall sign on the outside of the front wall, facing north (toward approaching players)
        level.setBlock(signPos, Blocks.OAK_WALL_SIGN.defaultBlockState()
                .setValue(WallSignBlock.FACING, Direction.NORTH), 2);

        // Set sign text via NBT load to avoid markUpdated() NPE during worldgen
        if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
            ListTag messages = new ListTag();
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("---------------"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("Now showing:"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("The Lion King"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("---------------"))));
            ListTag colors = new ListTag();
            for (int ci = 0; ci < 4; ci++) colors.add(StringTag.valueOf("black"));
            CompoundTag frontText = new CompoundTag();
            frontText.put("messages", messages);
            frontText.put("color", StringTag.valueOf("black"));
            frontText.putBoolean("has_glowing_text", false);
            CompoundTag tag = sign.saveWithId();
            tag.put("front_text", frontText);
            sign.load(tag);
        }

        // ============================================================
        // CHEST with loot near the entrance
        // ============================================================
        BlockPos chestPos = origin.offset(2, 1, 1);
        level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 2);

        if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest) {
            // Sticks
            chest.setItem(0, new ItemStack(Items.STICK, 2 + random.nextInt(5)));
            // Paper
            chest.setItem(1, new ItemStack(Items.PAPER, 1 + random.nextInt(4)));
            // Book
            chest.setItem(3, new ItemStack(Items.BOOK, 1 + random.nextInt(2)));
            // Bread
            chest.setItem(5, new ItemStack(Items.BREAD, 1 + random.nextInt(3)));
            // Compass
            if (random.nextInt(3) == 0) {
                chest.setItem(7, new ItemStack(Items.COMPASS, 1));
            }
            // Gold nuggets
            chest.setItem(9, new ItemStack(Items.GOLD_NUGGET, 2 + random.nextInt(6)));
            // Apples
            chest.setItem(11, new ItemStack(Items.APPLE, 1 + random.nextInt(3)));
            // String
            chest.setItem(13, new ItemStack(Items.STRING, 1 + random.nextInt(4)));
            // Bowls
            chest.setItem(15, new ItemStack(Items.BOWL, 1 + random.nextInt(3)));
            // Cookies
            chest.setItem(17, new ItemStack(Items.COOKIE, 2 + random.nextInt(4)));
            // Coal
            chest.setItem(19, new ItemStack(Items.COAL, 1 + random.nextInt(3)));
        }

        // Second chest on other side
        BlockPos chestPos2 = origin.offset(width - 3, 1, 1);
        level.setBlock(chestPos2, Blocks.CHEST.defaultBlockState(), 2);

        if (level.getBlockEntity(chestPos2) instanceof ChestBlockEntity chest2) {
            chest2.setItem(0, new ItemStack(Items.STICK, 1 + random.nextInt(4)));
            chest2.setItem(2, new ItemStack(Items.PAPER, 1 + random.nextInt(3)));
            chest2.setItem(4, new ItemStack(Items.GOLD_NUGGET, 1 + random.nextInt(4)));
            chest2.setItem(6, new ItemStack(Items.APPLE, 1 + random.nextInt(2)));
            chest2.setItem(8, new ItemStack(Items.BREAD, 1 + random.nextInt(2)));
        }

        // Ticket Lion NPC near entrance
        FeatureHelper.spawnEntity(level, LKEntityTypes.TICKET_LION.get(),
                origin.getX() + 7.0, origin.getY() + 1, origin.getZ() + 1.5);

        return true;
    }
}
