package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TheLionKingMod.MOD_ID);

    // ========== Pridestone & Variants ==========
    public static final RegistryObject<Block> PRIDESTONE = BLOCKS.register("pridestone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CORRUPT_PRIDESTONE = BLOCKS.register("corrupt_pridestone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> PRIDE_BRICK = BLOCKS.register("pride_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CORRUPT_PRIDE_BRICK = BLOCKS.register("corrupt_pride_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> MOSSY_PRIDE_BRICK = BLOCKS.register("mossy_pride_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> MOSSY_CORRUPT_PRIDE_BRICK = BLOCKS.register("mossy_corrupt_pride_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> PRIDE_PILLAR = BLOCKS.register("pride_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(1.2F, 8.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CORRUPT_PRIDE_PILLAR = BLOCKS.register("corrupt_pride_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).strength(1.2F, 8.0F).requiresCorrectToolForDrops()));

    // ========== Ores ==========
    public static final RegistryObject<Block> PRIDE_COAL_ORE = BLOCKS.register("pride_coal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(3.0F, 5.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(3.0F, 5.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> PEACOCK_ORE = BLOCKS.register("peacock_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(3.0F, 5.0F).requiresCorrectToolForDrops()));

    // ========== Storage Blocks ==========
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    public static final RegistryObject<Block> PEACOCK_BLOCK = BLOCKS.register("peacock_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(5.0F, 10.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    // ========== Wood - Acacia ==========
    public static final RegistryObject<Block> ACACIA_LOG = BLOCKS.register("pride_acacia_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> ACACIA_PLANKS = BLOCKS.register("pride_acacia_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> ACACIA_STAIRS = BLOCKS.register("pride_acacia_stairs",
            () -> new StairBlock(() -> ACACIA_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(ACACIA_PLANKS.get())));

    public static final RegistryObject<SlabBlock> ACACIA_SLAB = BLOCKS.register("pride_acacia_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ACACIA_PLANKS.get())));

    // ========== Wood - Rainforest ==========
    public static final RegistryObject<Block> RAINFOREST_LOG = BLOCKS.register("rainforest_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> RAINFOREST_PLANKS = BLOCKS.register("rainforest_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> RAINFOREST_STAIRS = BLOCKS.register("rainforest_stairs",
            () -> new StairBlock(() -> RAINFOREST_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(RAINFOREST_PLANKS.get())));

    public static final RegistryObject<SlabBlock> RAINFOREST_SLAB = BLOCKS.register("rainforest_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(RAINFOREST_PLANKS.get())));

    // ========== Wood - Mango ==========
    public static final RegistryObject<Block> MANGO_LOG = BLOCKS.register("mango_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> MANGO_PLANKS = BLOCKS.register("mango_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> MANGO_STAIRS = BLOCKS.register("mango_stairs",
            () -> new StairBlock(() -> MANGO_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(MANGO_PLANKS.get())));

    public static final RegistryObject<SlabBlock> MANGO_SLAB = BLOCKS.register("mango_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(MANGO_PLANKS.get())));

    // ========== Wood - Passion ==========
    public static final RegistryObject<Block> PASSION_LOG = BLOCKS.register("passion_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> PASSION_PLANKS = BLOCKS.register("passion_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> PASSION_STAIRS = BLOCKS.register("passion_stairs",
            () -> new StairBlock(() -> PASSION_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(PASSION_PLANKS.get())));

    public static final RegistryObject<SlabBlock> PASSION_SLAB = BLOCKS.register("passion_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(PASSION_PLANKS.get())));

    // ========== Wood - Banana ==========
    public static final RegistryObject<Block> BANANA_LOG = BLOCKS.register("banana_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> BANANA_PLANKS = BLOCKS.register("banana_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> BANANA_STAIRS = BLOCKS.register("banana_stairs",
            () -> new StairBlock(() -> BANANA_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(BANANA_PLANKS.get())));

    public static final RegistryObject<SlabBlock> BANANA_SLAB = BLOCKS.register("banana_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(BANANA_PLANKS.get())));

    // ========== Wood - Deadwood ==========
    public static final RegistryObject<Block> DEADWOOD_LOG = BLOCKS.register("deadwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> DEADWOOD_PLANKS = BLOCKS.register("deadwood_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.0F, 5.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<StairBlock> DEADWOOD_STAIRS = BLOCKS.register("deadwood_stairs",
            () -> new StairBlock(() -> DEADWOOD_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(DEADWOOD_PLANKS.get())));

    public static final RegistryObject<SlabBlock> DEADWOOD_SLAB = BLOCKS.register("deadwood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(DEADWOOD_PLANKS.get())));

    // ========== Stone Stairs & Slabs ==========
    public static final RegistryObject<StairBlock> PRIDESTONE_STAIRS = BLOCKS.register("pridestone_stairs",
            () -> new StairBlock(() -> PRIDESTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(PRIDESTONE.get())));

    public static final RegistryObject<SlabBlock> PRIDESTONE_SLAB = BLOCKS.register("pridestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(PRIDESTONE.get())));

    public static final RegistryObject<StairBlock> PRIDE_BRICK_STAIRS = BLOCKS.register("pride_brick_stairs",
            () -> new StairBlock(() -> PRIDE_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(PRIDE_BRICK.get())));

    public static final RegistryObject<SlabBlock> PRIDE_BRICK_SLAB = BLOCKS.register("pride_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(PRIDE_BRICK.get())));

    public static final RegistryObject<StairBlock> CORRUPT_PRIDESTONE_STAIRS = BLOCKS.register("corrupt_pridestone_stairs",
            () -> new StairBlock(() -> CORRUPT_PRIDESTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(CORRUPT_PRIDESTONE.get())));

    public static final RegistryObject<SlabBlock> CORRUPT_PRIDESTONE_SLAB = BLOCKS.register("corrupt_pridestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(CORRUPT_PRIDESTONE.get())));

    public static final RegistryObject<StairBlock> CORRUPT_PRIDE_BRICK_STAIRS = BLOCKS.register("corrupt_pride_brick_stairs",
            () -> new StairBlock(() -> CORRUPT_PRIDE_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(CORRUPT_PRIDE_BRICK.get())));

    public static final RegistryObject<SlabBlock> CORRUPT_PRIDE_BRICK_SLAB = BLOCKS.register("corrupt_pride_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(CORRUPT_PRIDE_BRICK.get())));

    // ========== Walls ==========
    public static final RegistryObject<WallBlock> PRIDESTONE_WALL = BLOCKS.register("pridestone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(PRIDESTONE.get())));

    public static final RegistryObject<WallBlock> PRIDE_BRICK_WALL = BLOCKS.register("pride_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(PRIDE_BRICK.get())));

    public static final RegistryObject<WallBlock> CORRUPT_PRIDESTONE_WALL = BLOCKS.register("corrupt_pridestone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(CORRUPT_PRIDESTONE.get())));

    // ========== Redstone ==========
    public static final RegistryObject<PressurePlateBlock> PRIDESTONE_PRESSURE_PLATE = BLOCKS.register("pridestone_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                            .strength(0.5F).noCollission().requiresCorrectToolForDrops(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.STONE));

    public static final RegistryObject<ButtonBlock> PRIDESTONE_BUTTON = BLOCKS.register("pridestone_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.of()
                    .strength(0.5F).noCollission(),
                    net.minecraft.world.level.block.state.properties.BlockSetType.STONE, 20, false));

    // ========== Misc Blocks ==========
    public static final RegistryObject<Block> DRIED_MAIZE_BLOCK = BLOCKS.register("dried_maize_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)));

    public static final RegistryObject<StairBlock> DRIED_MAIZE_STAIRS = BLOCKS.register("dried_maize_stairs",
            () -> new StairBlock(() -> DRIED_MAIZE_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(DRIED_MAIZE_BLOCK.get())));

    public static final RegistryObject<SlabBlock> DRIED_MAIZE_SLAB = BLOCKS.register("dried_maize_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(DRIED_MAIZE_BLOCK.get())));

    public static final RegistryObject<Block> OUTSAND = BLOCKS.register("outsand",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SAND).strength(0.7F).sound(SoundType.SAND)));

    public static final RegistryObject<Block> OUTGLASS = BLOCKS.register("outglass",
            () -> new GlassBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE).strength(0.4F).sound(SoundType.GLASS).noOcclusion()));

    public static final RegistryObject<IronBarsBlock> OUTGLASS_PANE = BLOCKS.register("outglass_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE).strength(0.4F).sound(SoundType.GLASS).noOcclusion()));

    public static final RegistryObject<Block> TERMITE_MOUND = BLOCKS.register("termite_mound",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT).strength(0.5F, 3.0F)));

    public static final RegistryObject<Block> PUMBAA_BOX = BLOCKS.register("pumbaa_box",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(1.0F).sound(SoundType.WOOD)));
}
