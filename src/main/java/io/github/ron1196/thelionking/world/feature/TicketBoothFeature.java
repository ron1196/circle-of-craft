package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.world.structure.LionKingStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

/**
 * Ticket Booth — faithful 1:1 port of LKWorldGenTicketBooth. L-shaped structure: small ticket
 * counter + larger theater with seating and portal frame screen.
 */
public class TicketBoothFeature extends Feature<NoneFeatureConfiguration> {

    public TicketBoothFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int i = origin.getX();
        int j = origin.getY();
        int k = origin.getZ();

        // Old mod used default oak + red wool; randomBooths not implemented yet
        BlockState stairBlock = net.minecraft.world.level.block.Blocks.OAK_STAIRS.defaultBlockState();
        BlockState seatBlock = net.minecraft.world.level.block.Blocks.OAK_STAIRS.defaultBlockState();
        BlockState planks = net.minecraft.world.level.block.Blocks.OAK_PLANKS.defaultBlockState();
        BlockState wool = net.minecraft.world.level.block.Blocks.RED_WOOL.defaultBlockState();
        BlockState cobble = net.minecraft.world.level.block.Blocks.COBBLESTONE.defaultBlockState();
        BlockState stoneBrick = net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState();
        BlockState glowstone = net.minecraft.world.level.block.Blocks.GLOWSTONE.defaultBlockState();
        BlockState fence = net.minecraft.world.level.block.Blocks.OAK_FENCE.defaultBlockState();
        BlockState glassPane = net.minecraft.world.level.block.Blocks.GLASS_PANE.defaultBlockState();
        BlockState portalFrame = LionKingBlocks.PRIDE_PORTAL_FRAME.get().defaultBlockState();
        BlockState torch = net.minecraft.world.level.block.Blocks.TORCH.defaultBlockState();
        BlockState air = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        BlockState dirt = net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();

        // ============================================================
        // FLOOR + WALLS: cobblestone floor, fill support below, plank walls
        // L-shaped footprint: x=-2..15, z=-2..9 except (z>4 && x<2)
        // ============================================================
        for (int i1 = -2; i1 < 16; i1++) {
            for (int k1 = -2; k1 < 10; k1++) {
                if (k1 > 4 && i1 < 2) continue;

                // Cobblestone floor
                FeatureHelper.placeBlock(level, i + i1, j, k + k1, cobble);

                // Fill support below until solid ground
                for (int j1 = 1; j1 < 10; j1++) {
                    BlockPos below = new BlockPos(i + i1, j - j1, k + k1);
                    if (level.getBlockState(below).isSolidRender(level, below)) break;
                    FeatureHelper.placeBlock(level, i + i1, j - j1, k + k1, dirt);
                }

                // Plank walls y+1 to y+3
                for (int j1 = 1; j1 < 4; j1++) {
                    FeatureHelper.placeBlock(level, i + i1, j + j1, k + k1, planks);
                }
                // Theater section (x>2) gets taller walls y+4 to y+5
                if (i1 > 2) {
                    for (int j1 = 4; j1 < 6; j1++) {
                        FeatureHelper.placeBlock(level, i + i1, j + j1, k + k1, planks);
                    }
                }
                // Stone brick accent at y+2
                FeatureHelper.placeBlock(level, i + i1, j + 2, k + k1, stoneBrick);
            }
        }

        // ============================================================
        // THEATER INTERIOR: clear, wool carpet, seats, center aisle
        // x=3..14, z=-1..8
        // ============================================================
        for (int i1 = 3; i1 < 15; i1++) {
            for (int k1 = -1; k1 < 9; k1++) {
                // Clear interior
                for (int j1 = 1; j1 < 5; j1++) {
                    FeatureHelper.placeBlock(level, i + i1, j + j1, k + k1, air);
                }
                // Wool carpet on floor (except seat rows)
                if (!(i1 < 10 && i1 % 2 == 1)) {
                    FeatureHelper.placeBlock(level, i + i1, j, k + k1, wool);
                }
                // Seats: odd x columns (3,5,7,9), not on center aisle (z=3,4)
                if (i1 < 10 && i1 % 2 == 1 && k1 != 3 && k1 != 4) {
                    FeatureHelper.placeBlock(
                            level, i + i1, j + 1, k + k1, seatBlock.setValue(StairBlock.FACING, Direction.WEST));
                }
                // Center aisle stays cobblestone
                if (k1 == 3 || k1 == 4) {
                    FeatureHelper.placeBlock(level, i + i1, j, k + k1, cobble);
                }
            }
        }

        // ============================================================
        // PORTAL FRAME SCREEN at x+14, z=2..5, j+0..j+4
        // Hollow frame (border only)
        // ============================================================
        for (int j1 = 0; j1 < 5; j1++) {
            for (int k1 = 2; k1 < 6; k1++) {
                if (j1 > 0 && j1 < 4 && k1 > 2 && k1 < 5) continue; // hollow center
                FeatureHelper.placeBlock(level, i + 14, j + j1, k + k1, portalFrame);
            }
        }

        // ============================================================
        // TICKET COUNTER: clear entrance area
        // ============================================================
        // Clear doorway area at z+3
        for (int i1 = -2; i1 < 3; i1++) {
            for (int j1 = 1; j1 < 3; j1++) {
                FeatureHelper.placeBlock(level, i + i1, j + j1, k + 3, air);
            }
        }

        // Clear ticket counter interior + add details
        for (int i1 = -1; i1 < 1; i1++) {
            for (int j1 = 1; j1 < 3; j1++) {
                for (int k1 = -1; k1 < 2; k1++) {
                    FeatureHelper.placeBlock(level, i + i1, j + j1, k + k1, air);
                    // Clear above on left side
                    if (i1 == -1 && j1 == 2) {
                        FeatureHelper.placeBlock(level, i + i1 - 1, j + j1, k + k1, air);
                    }
                    // Torches on counter
                    if (i1 == 0 && j1 == 2 && k1 != 0) {
                        FeatureHelper.placeBlock(level, i + i1, j + j1, k + k1, torch);
                    }
                    // Fence at counter window
                    if (i1 == -1 && j1 == 1 && k1 == 0) {
                        FeatureHelper.placeBlock(level, i + i1 - 1, j + j1, k + k1, fence);
                    }
                    // Glass panes at counter
                    if (i1 == -1 && j1 == 2 && k1 != 0) {
                        FeatureHelper.placeBlock(level, i + i1 - 1, j + j1, k + k1, glassPane);
                    }
                }
            }
        }

        // ============================================================
        // THEATER ROOF: stair overhangs at y+4
        // ============================================================
        // Front and back theater overhangs
        for (int i1 = 4; i1 < 15; i1++) {
            // Front (z-1): upside-down stairs facing north (old meta 7)
            FeatureHelper.placeBlock(
                    level,
                    i + i1,
                    j + 4,
                    k - 1,
                    stairBlock.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.TOP));
            // Back (z+8): upside-down stairs facing south (old meta 6)
            FeatureHelper.placeBlock(
                    level,
                    i + i1,
                    j + 4,
                    k + 8,
                    stairBlock.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.TOP));
        }

        // Left wall overhang (x+3): upside-down facing west (old meta 5)
        for (int k1 = 0; k1 < 8; k1++) {
            FeatureHelper.placeBlock(
                    level,
                    i + 3,
                    j + 4,
                    k + k1,
                    stairBlock.setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.TOP));
            // Right wall overhang (x+14): upside-down facing east (old meta 4)
            if (k1 < 2 || k1 > 5) {
                FeatureHelper.placeBlock(
                        level,
                        i + 14,
                        j + 4,
                        k + k1,
                        stairBlock.setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.TOP));
            }
        }

        // ============================================================
        // CORNER PILLARS: planks with glowstone at y+2
        // ============================================================
        for (int j1 = 0; j1 < 5; j1++) {
            BlockState pillar = j1 == 2 ? glowstone : planks;
            FeatureHelper.placeBlock(level, i + 3, j + j1, k - 1, pillar);
            FeatureHelper.placeBlock(level, i + 14, j + j1, k - 1, pillar);
            FeatureHelper.placeBlock(level, i + 3, j + j1, k + 8, pillar);
            FeatureHelper.placeBlock(level, i + 14, j + j1, k + 8, pillar);
        }

        // ============================================================
        // TICKET COUNTER ROOF: stairs at y+3
        // ============================================================
        // Left side (x-3): facing east (old meta 0)
        for (int k1 = -2; k1 < 5; k1++) {
            if (k1 == 3) {
                FeatureHelper.placeBlock(level, i - 3, j + 3, k + k1, planks); // solid above door
            } else {
                FeatureHelper.placeBlock(
                        level, i - 3, j + 3, k + k1, stairBlock.setValue(StairBlock.FACING, Direction.EAST));
            }
        }
        // Front
        for (int i1 = -2; i1 < 4; i1++) {
            FeatureHelper.placeBlock(
                    level, i + i1, j + 3, k - 3, stairBlock.setValue(StairBlock.FACING, Direction.SOUTH));
        }
        // Back
        for (int i1 = -2; i1 < 1; i1++) {
            FeatureHelper.placeBlock(
                    level, i + i1, j + 3, k + 5, stairBlock.setValue(StairBlock.FACING, Direction.NORTH));
        }
        // Connection between counter and theater (old meta 0 = EAST)
        generateSupports(level, i + 1, j + 3, k + 5, stairBlock, Direction.EAST);
        FeatureHelper.placeBlock(level, i + 1, j + 3, k + 5, planks);
        for (int k1 = 6; k1 < 10; k1++) {
            FeatureHelper.placeBlock(
                    level, i + 1, j + 3, k + k1, stairBlock.setValue(StairBlock.FACING, Direction.EAST));
        }

        // ============================================================
        // OUTER ROOF OVERHANGS at y+5
        // ============================================================
        for (int k1 = -2; k1 < 10; k1++) {
            FeatureHelper.placeBlock(
                    level, i + 2, j + 5, k + k1, stairBlock.setValue(StairBlock.FACING, Direction.EAST));
            FeatureHelper.placeBlock(
                    level, i + 16, j + 5, k + k1, stairBlock.setValue(StairBlock.FACING, Direction.WEST));
        }
        for (int i1 = 3; i1 < 16; i1++) {
            FeatureHelper.placeBlock(
                    level, i + i1, j + 5, k - 3, stairBlock.setValue(StairBlock.FACING, Direction.SOUTH));
            FeatureHelper.placeBlock(
                    level, i + i1, j + 5, k + 10, stairBlock.setValue(StairBlock.FACING, Direction.NORTH));
        }
        // Extra stair connections
        FeatureHelper.placeBlock(level, i + 2, j + 3, k + 10, stairBlock.setValue(StairBlock.FACING, Direction.NORTH));
        FeatureHelper.placeBlock(level, i + 3, j + 3, k + 10, stairBlock.setValue(StairBlock.FACING, Direction.NORTH));

        // ============================================================
        // SUPPORTS: fence posts under overhangs
        // ============================================================
        generateSupports(level, i - 3, j + 3, k - 3, stairBlock, Direction.EAST);
        generateSupports(level, i - 3, j + 3, k + 5, stairBlock, Direction.EAST);
        generateSupports(level, i + 1, j + 3, k + 10, stairBlock, Direction.EAST);
        generateSupports(level, i + 4, j + 3, k + 10, stairBlock, Direction.NORTH);
        generateSupports(level, i + 4, j + 3, k - 3, stairBlock, Direction.WEST);
        FeatureHelper.placeBlock(level, i + 2, j + 5, k - 3, stairBlock.setValue(StairBlock.FACING, Direction.EAST));
        FeatureHelper.placeBlock(level, i + 2, j + 5, k + 10, stairBlock.setValue(StairBlock.FACING, Direction.EAST));
        generateSupports(level, i + 16, j + 5, k - 3, stairBlock, Direction.SOUTH);
        generateSupports(level, i + 16, j + 5, k + 10, stairBlock, Direction.NORTH);

        // ============================================================
        // DOOR at ticket counter entrance
        // ============================================================
        FeatureHelper.placeBlock(
                level,
                i - 2,
                j + 1,
                k + 3,
                net.minecraft.world.level.block.Blocks.OAK_DOOR
                        .defaultBlockState()
                        .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                        .setValue(DoorBlock.FACING, Direction.WEST));
        FeatureHelper.placeBlock(
                level,
                i - 2,
                j + 2,
                k + 3,
                net.minecraft.world.level.block.Blocks.OAK_DOOR
                        .defaultBlockState()
                        .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                        .setValue(DoorBlock.FACING, Direction.WEST));

        // ============================================================
        // TORCHES
        // ============================================================
        // Theater interior walls
        for (int i1 = 5; i1 < 13; i1++) {
            if (i1 % 3 != 1) {
                FeatureHelper.placeBlock(level, i + i1, j + 2, k - 1, torch);
                FeatureHelper.placeBlock(level, i + i1, j + 2, k + 8, torch);
            }
        }
        // Right wall (near screen)
        for (int k1 = 1; k1 < 7; k1++) {
            if (k1 < 2 || k1 > 5) {
                FeatureHelper.placeBlock(level, i + 14, j + 2, k + k1, torch);
            }
            if (k1 < 3 || k1 > 4) {
                FeatureHelper.placeBlock(level, i + 3, j + 2, k + k1, torch);
            }
        }
        // Ticket counter torches
        FeatureHelper.placeBlock(level, i - 3, j + 2, k - 2, torch);
        FeatureHelper.placeBlock(level, i - 3, j + 2, k + 2, torch);
        FeatureHelper.placeBlock(level, i - 3, j + 2, k + 4, torch);

        // ============================================================
        // SIGN on exterior
        // ============================================================
        BlockPos signPos = new BlockPos(i - 4, j + 3, k + 3);
        FeatureHelper.placeBlock(
                level,
                signPos.getX(),
                signPos.getY(),
                signPos.getZ(),
                net.minecraft.world.level.block.Blocks.OAK_WALL_SIGN
                        .defaultBlockState()
                        .setValue(WallSignBlock.FACING, Direction.WEST));

        if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
            ListTag messages = new ListTag();
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("---------------"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("Now showing:"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("The Lion King"))));
            messages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("---------------"))));
            CompoundTag frontText = new CompoundTag();
            frontText.put("messages", messages);
            frontText.put("color", StringTag.valueOf("black"));
            frontText.putBoolean("has_glowing_text", false);
            CompoundTag tag = sign.saveWithId();
            tag.put("front_text", frontText);
            sign.load(tag);
        }

        // ============================================================
        // TICKET LION NPC at origin
        // ============================================================
        FeatureHelper.spawnEntity(level, EntityTypes.TICKET_LION.get(), i + 0.5, j + 1, k + 0.5);

        // ============================================================
        // CHEST with TRAPDOOR above and loot + random ticket lion armor
        // ============================================================
        BlockPos chestPos = new BlockPos(i + 2, j + 1, k);
        FeatureHelper.placeBlock(
                level,
                chestPos.getX(),
                chestPos.getY(),
                chestPos.getZ(),
                net.minecraft.world.level.block.Blocks.CHEST
                        .defaultBlockState()
                        .setValue(ChestBlock.FACING, Direction.WEST));
        FeatureHelper.placeBlock(
                level,
                i + 2,
                j + 2,
                k,
                net.minecraft.world.level.block.Blocks.OAK_TRAPDOOR
                        .defaultBlockState()
                        .setValue(TrapDoorBlock.FACING, Direction.NORTH));

        if (LionKingStructurePiece.isInCurrentChunk(chestPos)
                && level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest) {
            int lootCount = 2 + random.nextInt(4);
            for (int l = 0; l < lootCount; l++) {
                chest.setItem(random.nextInt(chest.getContainerSize()), getBasicLoot(random));
            }
            Item ticketLionArmor =
                    switch (random.nextInt(4)) {
                        case 0 -> LionKingItems.TICKET_LION_HEAD.get();
                        case 1 -> LionKingItems.TICKET_LION_SUIT.get();
                        case 2 -> LionKingItems.TICKET_LION_LEGS.get();
                        default -> LionKingItems.TICKET_LION_FEET.get();
                    };
            chest.setItem(random.nextInt(chest.getContainerSize()), new ItemStack(ticketLionArmor));
        }

        return true;
    }

    private ItemStack getBasicLoot(RandomSource random) {
        return switch (random.nextInt(11)) {
            case 1 -> new ItemStack(net.minecraft.world.item.Items.PAPER, 1 + random.nextInt(3));
            case 2 -> new ItemStack(net.minecraft.world.item.Items.BOOK, 1 + random.nextInt(2));
            case 3 -> new ItemStack(net.minecraft.world.item.Items.BREAD, 3 + random.nextInt(2));
            case 4 -> new ItemStack(net.minecraft.world.item.Items.COMPASS);
            case 5 -> new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 2 + random.nextInt(6));
            case 6 -> new ItemStack(net.minecraft.world.item.Items.APPLE, 1 + random.nextInt(3));
            case 7 -> new ItemStack(net.minecraft.world.item.Items.STRING, 2 + random.nextInt(2));
            case 8 -> new ItemStack(net.minecraft.world.item.Items.BOWL, 1 + random.nextInt(4));
            case 9 -> new ItemStack(net.minecraft.world.item.Items.COOKIE, 1 + random.nextInt(3));
            case 10 -> new ItemStack(net.minecraft.world.item.Items.COAL, 1 + random.nextInt(2));
            default -> new ItemStack(net.minecraft.world.item.Items.STICK, 2 + random.nextInt(4));
        };
    }

    /**
     * Creates a stair block with fence post supports extending downward to solid ground. Port of old
     * mod's generateSupports method.
     */
    private void generateSupports(WorldGenLevel level, int x, int y, int z, BlockState stairBlock, Direction facing) {
        FeatureHelper.placeBlock(level, x, y, z, stairBlock.setValue(StairBlock.FACING, facing));
        for (int j1 = 1; j1 < 20; j1++) {
            BlockPos below = new BlockPos(x, y - j1, z);
            BlockState existing = level.getBlockState(below);
            if (existing.isSolidRender(level, below)) break;
            // Use planks for water/lava, fence for air
            if (!existing.getFluidState().is(Fluids.EMPTY)) {
                FeatureHelper.placeBlock(
                        level, x, y - j1, z, net.minecraft.world.level.block.Blocks.OAK_PLANKS.defaultBlockState());
            } else {
                FeatureHelper.placeBlock(
                        level, x, y - j1, z, net.minecraft.world.level.block.Blocks.OAK_FENCE.defaultBlockState());
            }
        }
    }
}
