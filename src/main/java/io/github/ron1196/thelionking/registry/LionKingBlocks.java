package io.github.ron1196.thelionking.registry;

import static io.github.ron1196.thelionking.registry.LionKingBlocksRegistryHelper.*;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.*;
import io.github.ron1196.thelionking.block.MushroomBlock;
import io.github.ron1196.thelionking.block.SpawnerBlock;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LionKingBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TheLionKingMod.MOD_ID);

    // ========== Pridestone & Variants ==========
    public static final RegistryObject<Block> PRIDESTONE = stoneBlock("pridestone");

    public static final RegistryObject<Block> CORRUPT_PRIDESTONE = BLOCKS.register(
            "corrupt_pridestone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.05F, 10.0F)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> PRIDE_BRICK = stoneBlock("pride_brick");
    public static final RegistryObject<Block> CORRUPT_PRIDE_BRICK =
            stoneBlock("corrupt_pride_brick", MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> CRACKED_PRIDE_BRICK = stoneBlock("cracked_pride_brick");
    public static final RegistryObject<Block> MOSSY_PRIDE_BRICK = stoneBlock("mossy_pride_brick");
    public static final RegistryObject<Block> MOSSY_CORRUPT_PRIDE_BRICK =
            stoneBlock("mossy_corrupt_pride_brick", MapColor.COLOR_PURPLE);

    public static final RegistryObject<Block> PRIDE_PILLAR =
            BLOCKS.register("pride_pillar", () -> new RotatedPillarBlock(pillarProps(MapColor.STONE)));
    public static final RegistryObject<Block> CORRUPT_PRIDE_PILLAR =
            BLOCKS.register("corrupt_pride_pillar", () -> new RotatedPillarBlock(pillarProps(MapColor.COLOR_PURPLE)));

    // ========== Ores ==========
    public static final RegistryObject<Block> PRIDE_COAL_ORE =
            BLOCKS.register("pride_coal_ore", () -> new DropExperienceBlock(oreProps()));

    public static final RegistryObject<Block> SILVER_ORE =
            BLOCKS.register("silver_ore", () -> new DropExperienceBlock(oreProps()));

    public static final RegistryObject<Block> PEACOCK_ORE =
            BLOCKS.register("peacock_ore", () -> new DropExperienceBlock(oreProps()));

    public static final RegistryObject<Block> KIVULITE_ORE =
            BLOCKS.register("kivulite_ore", () -> new DropExperienceBlock(oreProps()));

    public static final RegistryObject<Block> NUKA_ORE =
            BLOCKS.register("nuka_ore", () -> new DropExperienceBlock(oreProps()));

    // ========== Storage Blocks ==========
    public static final RegistryObject<Block> SILVER_BLOCK =
            BLOCKS.register("silver_block", () -> new Block(metalProps()));

    public static final RegistryObject<Block> PEACOCK_BLOCK =
            BLOCKS.register("peacock_block", () -> new Block(metalProps()));

    public static final RegistryObject<Block> KIVULITE_BLOCK =
            BLOCKS.register("kivulite_block", () -> new Block(metalProps()));

    // ========== Wood - Acacia ==========
    public static final RegistryObject<Block> ACACIA_LOG = log("pride_acacia_log");
    public static final RegistryObject<Block> ACACIA_PLANKS = planks("pride_acacia_planks");
    public static final RegistryObject<StairBlock> ACACIA_STAIRS = stairs("pride_acacia_stairs", ACACIA_PLANKS);
    public static final RegistryObject<SlabBlock> ACACIA_SLAB = slab("pride_acacia_slab", ACACIA_PLANKS);

    // ========== Wood - Rainforest ==========
    public static final RegistryObject<Block> RAINFOREST_LOG = log("rainforest_log");
    public static final RegistryObject<Block> RAINFOREST_PLANKS = planks("rainforest_planks");
    public static final RegistryObject<StairBlock> RAINFOREST_STAIRS = stairs("rainforest_stairs", RAINFOREST_PLANKS);
    public static final RegistryObject<SlabBlock> RAINFOREST_SLAB = slab("rainforest_slab", RAINFOREST_PLANKS);

    // ========== Wood - Mango ==========
    public static final RegistryObject<Block> MANGO_LOG = log("mango_log");
    public static final RegistryObject<Block> MANGO_PLANKS = planks("mango_planks");
    public static final RegistryObject<StairBlock> MANGO_STAIRS = stairs("mango_stairs", MANGO_PLANKS);
    public static final RegistryObject<SlabBlock> MANGO_SLAB = slab("mango_slab", MANGO_PLANKS);

    // ========== Wood - Passion ==========
    public static final RegistryObject<Block> PASSION_LOG = log("passion_log");
    public static final RegistryObject<Block> PASSION_PLANKS = planks("passion_planks");
    public static final RegistryObject<StairBlock> PASSION_STAIRS = stairs("passion_stairs", PASSION_PLANKS);
    public static final RegistryObject<SlabBlock> PASSION_SLAB = slab("passion_slab", PASSION_PLANKS);

    // ========== Wood - Banana ==========
    public static final RegistryObject<Block> BANANA_LOG = log("banana_log");
    public static final RegistryObject<Block> BANANA_PLANKS = planks("banana_planks");
    public static final RegistryObject<StairBlock> BANANA_STAIRS = stairs("banana_stairs", BANANA_PLANKS);
    public static final RegistryObject<SlabBlock> BANANA_SLAB = slab("banana_slab", BANANA_PLANKS);

    // ========== Wood - Deadwood ==========
    public static final RegistryObject<Block> DEADWOOD_LOG = log("deadwood_log", MapColor.COLOR_GRAY);
    public static final RegistryObject<Block> DEADWOOD_PLANKS = planks("deadwood_planks", MapColor.COLOR_GRAY);
    public static final RegistryObject<StairBlock> DEADWOOD_STAIRS = stairs("deadwood_stairs", DEADWOOD_PLANKS);
    public static final RegistryObject<SlabBlock> DEADWOOD_SLAB = slab("deadwood_slab", DEADWOOD_PLANKS);

    // ========== Stone Stairs & Slabs ==========
    public static final RegistryObject<StairBlock> PRIDESTONE_STAIRS = stairs("pridestone_stairs", PRIDESTONE);
    public static final RegistryObject<SlabBlock> PRIDESTONE_SLAB = slab("pridestone_slab", PRIDESTONE);
    public static final RegistryObject<StairBlock> PRIDE_BRICK_STAIRS = stairs("pride_brick_stairs", PRIDE_BRICK);
    public static final RegistryObject<SlabBlock> PRIDE_BRICK_SLAB = slab("pride_brick_slab", PRIDE_BRICK);
    public static final RegistryObject<StairBlock> CORRUPT_PRIDESTONE_STAIRS =
            stairs("corrupt_pridestone_stairs", CORRUPT_PRIDESTONE);
    public static final RegistryObject<SlabBlock> CORRUPT_PRIDESTONE_SLAB =
            slab("corrupt_pridestone_slab", CORRUPT_PRIDESTONE);
    public static final RegistryObject<StairBlock> CORRUPT_PRIDE_BRICK_STAIRS =
            stairs("corrupt_pride_brick_stairs", CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<SlabBlock> CORRUPT_PRIDE_BRICK_SLAB =
            slab("corrupt_pride_brick_slab", CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<SlabBlock> PRIDE_PILLAR_SLAB = slab("pride_pillar_slab", PRIDE_PILLAR);
    public static final RegistryObject<SlabBlock> CORRUPT_PRIDE_PILLAR_SLAB =
            slab("corrupt_pride_pillar_slab", CORRUPT_PRIDE_PILLAR);

    // ========== Walls ==========
    public static final RegistryObject<WallBlock> PRIDESTONE_WALL = wall("pridestone_wall", PRIDESTONE);
    public static final RegistryObject<WallBlock> PRIDE_BRICK_WALL = wall("pride_brick_wall", PRIDE_BRICK);
    public static final RegistryObject<WallBlock> CORRUPT_PRIDESTONE_WALL =
            wall("corrupt_pridestone_wall", CORRUPT_PRIDESTONE);

    // ========== Redstone ==========
    public static final RegistryObject<PressurePlateBlock> PRIDESTONE_PRESSURE_PLATE = BLOCKS.register(
            "pridestone_pressure_plate",
            () -> new PressurePlateBlock(
                    PressurePlateBlock.Sensitivity.MOBS,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(0.5F)
                            .noCollission()
                            .requiresCorrectToolForDrops(),
                    BlockSetType.STONE));

    public static final RegistryObject<ButtonBlock> PRIDESTONE_BUTTON = BLOCKS.register(
            "pridestone_button",
            () -> new ButtonBlock(
                    BlockBehaviour.Properties.of().strength(0.5F).noCollission(), BlockSetType.STONE, 20, false));

    // ========== Misc Blocks ==========
    public static final RegistryObject<Block> DRIED_MAIZE_BLOCK = BLOCKS.register(
            "dried_maize_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.5F)
                    .sound(SoundType.GRASS)));

    public static final RegistryObject<StairBlock> DRIED_MAIZE_STAIRS = stairs("dried_maize_stairs", DRIED_MAIZE_BLOCK);
    public static final RegistryObject<SlabBlock> DRIED_MAIZE_SLAB = slab("dried_maize_slab", DRIED_MAIZE_BLOCK);

    public static final RegistryObject<Block> OUTSAND = BLOCKS.register(
            "outsand",
            () -> new OutsandBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SAND)
                    .strength(0.7F)
                    .sound(SoundType.SAND)));

    public static final RegistryObject<Block> OUTGLASS = BLOCKS.register(
            "outglass",
            () -> new GlassBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .strength(0.4F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final RegistryObject<IronBarsBlock> OUTGLASS_PANE = BLOCKS.register(
            "outglass_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .strength(0.4F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final RegistryObject<Block> TERMITE_MOUND = BLOCKS.register(
            "termite_mound",
            () -> new TermiteMoundBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F, 3.0F)));

    public static final RegistryObject<Block> ZIRA_MOUND_GATE = BLOCKS.register(
            "zira_mound_gate",
            () -> new ZiraMoundGateBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()));

    public static final RegistryObject<Block> POOL_COVER = BLOCKS.register(
            "pool_cover",
            () -> new PoolCoverBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()));

    public static final RegistryObject<Block> PUMBAA_BOX = BLOCKS.register(
            "pumbaa_box",
            () -> new PumbaaBoxBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)));

    // ========== Leaves ==========
    public static final RegistryObject<LeavesBlock> ACACIA_LEAVES = leaves("pride_acacia_leaves");
    public static final RegistryObject<LeavesBlock> RAINFOREST_LEAVES = leaves("rainforest_leaves");
    public static final RegistryObject<LeavesBlock> MANGO_LEAVES = leaves("mango_leaves");
    public static final RegistryObject<LeavesBlock> PASSION_LEAVES = BLOCKS.register(
            "passion_leaves", () -> new LionKingLeavesBlock(leavesProps().lightLevel(s -> 11)));
    public static final RegistryObject<LeavesBlock> BANANA_LEAVES = leaves("banana_leaves");
    public static final RegistryObject<LeavesBlock> RAFIKI_LEAVES = BLOCKS.register(
            "rafiki_leaves", () -> new RafikiLeavesBlock(leavesProps().strength(-1.0F, 3600000.0F)));

    // ========== Saplings ==========
    public static final RegistryObject<Block> ACACIA_SAPLING = sapling("pride_acacia_sapling", TreeGrowers.ACACIA);
    public static final RegistryObject<Block> RAINFOREST_SAPLING =
            sapling("rainforest_sapling", TreeGrowers.RAINFOREST);
    public static final RegistryObject<Block> MANGO_SAPLING = sapling("mango_sapling", TreeGrowers.MANGO);
    public static final RegistryObject<Block> PASSION_SAPLING = BLOCKS.register(
            "passion_sapling",
            () -> new LionKingSaplingBlock(TreeGrowers.PASSION, saplingProps().lightLevel(s -> 11)));
    public static final RegistryObject<Block> BANANA_SAPLING = sapling("banana_sapling", TreeGrowers.BANANA);

    // ========== Rafiki Wood ==========
    public static final RegistryObject<Block> RAFIKI_WOOD = BLOCKS.register(
            "rafiki_wood",
            () -> new RafikiWoodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.WOOD)));

    // ========== Flowers ==========
    public static final RegistryObject<Block> WHITE_FLOWER =
            BLOCKS.register("white_flower", () -> new LionKingFlowerBlock(MobEffects.HEAL, 5, flowerProps()));

    public static final RegistryObject<Block> BLUE_FLOWER =
            BLOCKS.register("blue_flower", () -> new LionKingFlowerBlock(MobEffects.NIGHT_VISION, 5, flowerProps()));

    // ========== Tall Flowers ==========
    public static final RegistryObject<Block> PURPLE_FLOWER =
            BLOCKS.register("purple_flower", () -> new DoublePlantBlock(flowerProps()));

    public static final RegistryObject<Block> RED_FLOWER =
            BLOCKS.register("red_flower", () -> new DoublePlantBlock(flowerProps()));

    // ========== Waterlilies ==========
    public static final RegistryObject<Block> LILY_RED = lily("lily_red");
    public static final RegistryObject<Block> LILY_VIOLET = lily("lily_violet");
    public static final RegistryObject<Block> LILY_WHITE = lily("lily_white");

    // ========== Mushrooms ==========
    public static final RegistryObject<Block> OUTSHROOM =
            BLOCKS.register("outshroom", () -> new MushroomBlock(flowerProps()));

    public static final RegistryObject<Block> OUTSHROOM_GLOWING = BLOCKS.register(
            "outshroom_glowing", () -> new MushroomBlock(flowerProps().lightLevel(s -> 12)));

    // ========== Arid Grass ==========
    public static final RegistryObject<Block> ARID_GRASS = BLOCKS.register(
            "arid_grass", () -> new AridGrassBlock(flowerProps().offsetType(BlockBehaviour.OffsetType.XZ)));

    // ========== Hyena Torch ==========
    public static final RegistryObject<Block> HYENA_TORCH = BLOCKS.register(
            "hyena_torch",
            () -> new TorchBlock(
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.WOOD)
                            .lightLevel(s -> 14),
                    net.minecraft.core.particles.ParticleTypes.FLAME));

    public static final RegistryObject<Block> HYENA_WALL_TORCH = BLOCKS.register(
            "hyena_wall_torch",
            () -> new WallTorchBlock(
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.WOOD)
                            .lightLevel(s -> 14)
                            .lootFrom(HYENA_TORCH),
                    net.minecraft.core.particles.ParticleTypes.FLAME));

    // ========== Hanging Banana ==========
    public static final RegistryObject<Block> HANGING_BANANA = BLOCKS.register(
            "hanging_banana",
            () -> new HangingBananaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .noOcclusion()));

    // ========== Crops ==========
    public static final RegistryObject<Block> MAIZE_CROP =
            BLOCKS.register("maize_crop", () -> new MaizeCropBlock(cropProps()));

    public static final RegistryObject<Block> YAM_CROP =
            BLOCKS.register("yam_crop", () -> new YamCropBlock(cropProps()));

    public static final RegistryObject<Block> KIWANO_STEM =
            BLOCKS.register("kiwano_stem", () -> new KiwanoStemBlock(cropProps().sound(SoundType.WOOD)));

    public static final RegistryObject<Block> KIWANO_BLOCK = BLOCKS.register(
            "kiwano_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    // ========== Decorative Block Entities ==========
    public static final RegistryObject<Block> HYENA_HEAD = BLOCKS.register(
            "hyena_head",
            () -> new HyenaHeadBlock(
                    BlockBehaviour.Properties.of().strength(1.0F).noCollission().noOcclusion()));

    // ========== Dyeable Rugs (carpet-style) ==========
    public static final RegistryObject<Block> RUG_LION = carpet("rug_lion", MapColor.COLOR_YELLOW);
    public static final RegistryObject<Block> RUG_BLACK = carpet("rug_black", MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> RUG_BLUE = carpet("rug_blue", MapColor.COLOR_BLUE);
    public static final RegistryObject<Block> RUG_GREEN = carpet("rug_green", MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> RUG_GRAY = carpet("rug_gray", MapColor.COLOR_GRAY);
    public static final RegistryObject<Block> RUG_LIGHT_BLUE = carpet("rug_light_blue", MapColor.COLOR_LIGHT_BLUE);
    public static final RegistryObject<Block> RUG_LIGHT_GREEN = carpet("rug_light_green", MapColor.COLOR_LIGHT_GREEN);
    public static final RegistryObject<Block> RUG_LIGHT_GRAY = carpet("rug_light_gray", MapColor.COLOR_LIGHT_GRAY);
    public static final RegistryObject<Block> RUG_ORANGE = carpet("rug_orange", MapColor.COLOR_ORANGE);
    public static final RegistryObject<Block> RUG_OUTLANDER = carpet("rug_outlander", MapColor.COLOR_BROWN);
    public static final RegistryObject<Block> RUG_PINK = carpet("rug_pink", MapColor.COLOR_PINK);
    public static final RegistryObject<Block> RUG_PURPLE = carpet("rug_purple", MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> RUG_RED = carpet("rug_red", MapColor.COLOR_RED);
    public static final RegistryObject<Block> RUG_VIOLET = carpet("rug_violet", MapColor.COLOR_MAGENTA);
    public static final RegistryObject<Block> RUG_WHITE = carpet("rug_white", MapColor.SNOW);
    public static final RegistryObject<Block> RUG_YELLOW = carpet("rug_yellow", MapColor.COLOR_YELLOW);

    // ========== Grinding Bowl ==========
    public static final RegistryObject<Block> GRINDING_BOWL = BLOCKS.register(
            "grinding_bowl",
            () -> new GrindingBowlBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    // ========== Bug Trap ==========
    public static final RegistryObject<Block> BUG_TRAP = BLOCKS.register(
            "bug_trap",
            () -> new BugTrapBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    // ========== Bongo Drum ==========
    public static final RegistryObject<Block> BONGO_DRUM = BLOCKS.register(
            "bongo_drum",
            () -> new BongoDrumBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    // ========== Outlands Pool ==========
    public static final RegistryObject<Block> OUTLANDS_POOL = BLOCKS.register(
            "outlands_pool",
            () -> new OutlandsPoolBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(50.0F, 1200.0F)
                    .lightLevel(s -> 14)
                    .noOcclusion()));

    // ========== LK Spawner ==========
    public static final RegistryObject<Block> LK_SPAWNER = BLOCKS.register(
            "lk_spawner",
            () -> new SpawnerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(5.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    // ========== Phase 12: Missing Blocks ==========
    public static final RegistryObject<Block> BANANA_CAKE = BLOCKS.register(
            "banana_cake",
            () -> new BananaCakeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.5F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> MOUNTED_SHOOTER = BLOCKS.register(
            "mounted_shooter",
            () -> new MountedShooterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final RegistryObject<Block> STAR_ALTAR = BLOCKS.register(
            "star_altar",
            () -> new StarAltarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(5.0F, 1200.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 7)));

    public static final RegistryObject<Block> OUTLANDS_ALTAR = BLOCKS.register(
            "outlands_altar",
            () -> new OutlandsAltarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(5.0F, 1200.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> 7)));

    public static final RegistryObject<Block> TILLED_SAND = BLOCKS.register(
            "tilled_sand",
            () -> new TilledSandBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SAND)
                    .strength(0.6F)
                    .sound(SoundType.SAND)
                    .randomTicks()
                    .noOcclusion()));

    public static final RegistryObject<Block> VASE = BLOCKS.register(
            "vase",
            () -> new VaseBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(0.5F)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    public static final RegistryObject<Block> VASE_ACACIA = filledVase("vase_acacia", "pride_acacia_sapling");
    public static final RegistryObject<Block> VASE_RAINFOREST = filledVase("vase_rainforest", "rainforest_sapling");
    public static final RegistryObject<Block> VASE_MANGO = filledVase("vase_mango", "mango_sapling");
    public static final RegistryObject<Block> VASE_PASSION = filledVase("vase_passion", "passion_sapling", 11);
    public static final RegistryObject<Block> VASE_BANANA = filledVase("vase_banana", "banana_sapling");
    public static final RegistryObject<Block> VASE_WHITE_FLOWER = filledVase("vase_white_flower", "white_flower");
    public static final RegistryObject<Block> VASE_BLUE_FLOWER = filledVase("vase_blue_flower", "blue_flower");
    public static final RegistryObject<Block> VASE_RED_FLOWER = filledVase("vase_red_flower", "red_flower");
    public static final RegistryObject<Block> VASE_PURPLE_FLOWER = filledVase("vase_purple_flower", "purple_flower");
    public static final RegistryObject<Block> VASE_OUTSHROOM = filledVase("vase_outshroom", "outshroom");
    public static final RegistryObject<Block> VASE_OUTSHROOM_GLOWING =
            filledVase("vase_outshroom_glowing", "outshroom_glowing", 13);

    private static RegistryObject<Block> filledVase(String name, String contentItemId) {
        return filledVase(name, contentItemId, -1);
    }

    private static RegistryObject<Block> filledVase(String name, String contentItemId, int lightLevel) {
        return BLOCKS.register(name, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.copy(VASE.get());
            if (lightLevel >= 0) {
                final int level = lightLevel;
                props = props.lightLevel(s -> level);
            }
            java.util.function.Supplier<net.minecraft.world.item.Item> contentSupplier = () ->
                    net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                            new net.minecraft.resources.ResourceLocation(TheLionKingMod.MOD_ID, contentItemId));
            return new io.github.ron1196.thelionking.block.FilledVaseBlock(contentSupplier, props);
        });
    }

    // ========== Bed & Lever ==========
    public static final RegistryObject<BedBlock> PRIDE_BED = BLOCKS.register(
            "pride_bed",
            () -> new PrideBedBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.2F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<LeverBlock> PRIDE_LEVER = BLOCKS.register(
            "pride_lever",
            () -> new LeverBlock(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .pushReaction(PushReaction.DESTROY)));

    // ========== Portal Blocks ==========
    public static final RegistryObject<Block> PRIDE_PORTAL_FRAME = BLOCKS.register(
            "pride_portal_frame",
            () -> new PortalFrameBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable(),
                    false));

    public static final RegistryObject<Block> OUTLANDS_PORTAL_FRAME = BLOCKS.register(
            "outlands_portal_frame",
            () -> new PortalFrameBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable(),
                    true));

    public static final RegistryObject<Block> PRIDE_LANDS_PORTAL = BLOCKS.register(
            "pride_lands_portal",
            () -> new PortalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_LIGHT_GREEN)
                            .noCollission()
                            .strength(-1.0F)
                            .lightLevel(s -> 11)
                            .noLootTable()
                            .noOcclusion()
                            .pushReaction(PushReaction.BLOCK),
                    PRIDE_PORTAL_FRAME,
                    ParticleTypes.PRIDE_LANDS_PORTAL,
                    Level.OVERWORLD,
                    Dimensions.PRIDE_LANDS_LEVEL));

    public static final RegistryObject<Block> OUTLANDS_PORTAL = BLOCKS.register(
            "outlands_portal",
            () -> new PortalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .noCollission()
                            .strength(-1.0F)
                            .lightLevel(s -> 11)
                            .noLootTable()
                            .noOcclusion()
                            .pushReaction(PushReaction.BLOCK),
                    OUTLANDS_PORTAL_FRAME,
                    ParticleTypes.OUTLANDS_PORTAL,
                    Dimensions.PRIDE_LANDS_LEVEL,
                    Dimensions.OUTLANDS_LEVEL));
}
