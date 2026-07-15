package io.github.ron1196.circleofcraft.registry;

import static io.github.ron1196.circleofcraft.registry.ModItemsRegistryHelper.*;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.RugEntity;
import io.github.ron1196.circleofcraft.item.*;
import io.github.ron1196.circleofcraft.item.tier.ModArmorMaterials;
import io.github.ron1196.circleofcraft.item.tier.ModToolTiers;
import io.github.ron1196.circleofcraft.world.dimension.Dimensions;
import io.github.ron1196.circleofcraft.world.structure.ModStructurePiece;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CircleOfCraftMod.MOD_ID);

    // ========== Material Items ==========
    public static final DeferredItem<Item> SILVER_INGOT = simpleItem("silver_ingot");
    public static final DeferredItem<Item> PEACOCK_GEM = simpleItem("peacock_gem");
    public static final DeferredItem<Item> KIVULITE = simpleItem("kivulite");
    public static final DeferredItem<Item> HYENA_BONE = simpleItem("hyena_bone");
    public static final DeferredItem<Item> HYENA_BONE_SHARD = simpleItem("hyena_bone_shard");
    public static final DeferredItem<Item> TERMITE_DUST = simpleItem("termite_dust");
    public static final DeferredItem<Item> TERMITE_THROWN = ITEMS.register("termite_thrown", ThrownTermiteItem::new);
    public static final DeferredItem<Item> MANGO_DUST = simpleItem("mango_dust");
    public static final DeferredItem<Item> FEATHER_BLUE = simpleItem("feather_blue");
    public static final DeferredItem<Item> FEATHER_YELLOW = simpleItem("feather_yellow");
    public static final DeferredItem<Item> FEATHER_RED = simpleItem("feather_red");
    public static final DeferredItem<Item> FEATHER_BLACK = simpleItem("feather_black");
    public static final DeferredItem<Item> FEATHER_FLAMINGO = simpleItem("feather_flamingo");
    public static final DeferredItem<Item> POISON = simpleItem("poison");
    public static final DeferredItem<Item> NUKA_SHARD = simpleItem("nuka_shard");
    public static final DeferredItem<Item> OUTLANDER_FUR = simpleItem("outlander_fur");
    public static final DeferredItem<Item> ZEBRA_HIDE = simpleItem("zebra_hide");
    public static final DeferredItem<Item> GEMSBOK_HIDE = simpleItem("gemsbok_hide");
    public static final DeferredItem<Item> GEMSBOK_HORN = simpleItem("gemsbok_horn");
    public static final DeferredItem<Item> RHINO_HORN = simpleItem("rhino_horn");
    public static final DeferredItem<Item> GROUND_RHINO_HORN =
            registerItem("ground_rhino_horn", GroundRhinoHornItem::new);
    public static final DeferredItem<Item> CORN_KERNELS = simpleItem("corn_kernels");
    public static final DeferredItem<Item> DRIED_MAIZE = simpleItem("dried_maize");
    public static final DeferredItem<Item> LION_FUR = simpleItem("lion_fur");
    public static final DeferredItem<Item> BUG = simpleItem("bug");
    public static final DeferredItem<Item> CRYSTAL = simpleItem("crystal", 16);

    // ========== Food Items ==========
    public static final DeferredItem<Item> LION_RAW = meatItem("lion_raw", 3, 0.3F);
    public static final DeferredItem<Item> LION_COOKED = meatItem("lion_cooked", 8, 0.8F);
    public static final DeferredItem<Item> ZEBRA_RAW = meatItem("zebra_raw", 2, 0.1F);
    public static final DeferredItem<Item> ZEBRA_COOKED = meatItem("zebra_cooked", 6, 0.4F);
    public static final DeferredItem<Item> RHINO_RAW = meatItem("rhino_raw", 2, 0.1F);
    public static final DeferredItem<Item> RHINO_COOKED = meatItem("rhino_cooked", 7, 0.4F);
    public static final DeferredItem<Item> MANGO = foodItem("mango", 3, 0.3F);
    public static final DeferredItem<Item> BANANA = foodItem("banana", 2, 0.3F);
    public static final DeferredItem<Item> CORN = foodItem("corn", 1, 0.1F);
    public static final DeferredItem<Item> POPCORN = foodItem("popcorn", 3, 0.4F);
    public static final DeferredItem<Item> KIWANO = foodItem("kiwano", 2, 0.3F);
    public static final DeferredItem<Item> ROAST_YAM = foodItem("roast_yam", 6, 0.6F);
    public static final DeferredItem<Item> BANANA_BREAD = foodItem("banana_bread", 3, 0.5F);
    public static final DeferredItem<Item> CHOCOLATE_MUFASA = foodItem("chocolate_mufasa", 16, 0.8F);

    public static final DeferredItem<Item> OUTLANDER_MEAT = meatEffectItem("outlander_meat", 3, 0.2F, 600, 0.8F);
    public static final DeferredItem<Item> CROCODILE_MEAT = meatEffectItem("crocodile_meat", 4, 0.4F, 300, 0.3F);

    public static final DeferredItem<Item> BUG_STEW = registerItem(
            "bug_stew",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.5F)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 160, 0), 0.4F)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 0), 0.12F)
                            .build())
                    .stacksTo(1)
                    .craftRemainder(Items.BOWL));

    public static final DeferredItem<Item> EXPERIENCE_GRUB = registerItem(
            "experience_grub",
            ExperienceGrubItem::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(0)
                            .saturationModifier(0.0F)
                            .alwaysEdible()
                            .build()));

    // ========== Pridestone Tools ==========
    public static final DeferredItem<SwordItem> PRIDESTONE_SWORD = ITEMS.register(
            "pridestone_sword",
            () -> new SwordItem(
                    ModToolTiers.PRIDESTONE,
                    new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.PRIDESTONE, 3, -2.4F))));

    public static final DeferredItem<PickaxeItem> PRIDESTONE_PICKAXE = ITEMS.register(
            "pridestone_pickaxe",
            () -> new PickaxeItem(
                    ModToolTiers.PRIDESTONE,
                    new Item.Properties()
                            .attributes(PickaxeItem.createAttributes(ModToolTiers.PRIDESTONE, 1.0F, -2.8F))));

    public static final DeferredItem<AxeItem> PRIDESTONE_AXE = ITEMS.register(
            "pridestone_axe",
            () -> new AxeItem(
                    ModToolTiers.PRIDESTONE,
                    new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.PRIDESTONE, 6.0F, -3.1F))));

    public static final DeferredItem<ShovelItem> PRIDESTONE_SHOVEL = ITEMS.register(
            "pridestone_shovel",
            () -> new ShovelItem(
                    ModToolTiers.PRIDESTONE,
                    new Item.Properties()
                            .attributes(ShovelItem.createAttributes(ModToolTiers.PRIDESTONE, 1.5F, -3.0F))));

    public static final DeferredItem<HoeItem> PRIDESTONE_HOE = ITEMS.register(
            "pridestone_hoe",
            () -> new HoeItem(
                    ModToolTiers.PRIDESTONE,
                    new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.PRIDESTONE, -1.0F, -2.0F))));

    // ========== Silver Tools ==========
    public static final DeferredItem<SwordItem> SILVER_SWORD = ITEMS.register(
            "silver_sword",
            () -> new SwordItem(
                    ModToolTiers.SILVER,
                    new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SILVER, 3, -2.4F))));

    public static final DeferredItem<PickaxeItem> SILVER_PICKAXE = ITEMS.register(
            "silver_pickaxe",
            () -> new PickaxeItem(
                    ModToolTiers.SILVER,
                    new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SILVER, 1.0F, -2.8F))));

    public static final DeferredItem<AxeItem> SILVER_AXE = ITEMS.register(
            "silver_axe",
            () -> new AxeItem(
                    ModToolTiers.SILVER,
                    new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.SILVER, 6.0F, -3.1F))));

    public static final DeferredItem<ShovelItem> SILVER_SHOVEL = ITEMS.register(
            "silver_shovel",
            () -> new ShovelItem(
                    ModToolTiers.SILVER,
                    new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.SILVER, 1.5F, -3.0F))));

    public static final DeferredItem<HoeItem> SILVER_HOE = ITEMS.register(
            "silver_hoe",
            () -> new HoeItem(
                    ModToolTiers.SILVER,
                    new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.SILVER, -2.0F, -1.0F))));

    // ========== Peacock Tools ==========
    public static final DeferredItem<SwordItem> PEACOCK_SWORD = ITEMS.register(
            "peacock_sword",
            () -> new SwordItem(
                    ModToolTiers.PEACOCK,
                    new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.PEACOCK, 3, -2.4F))));

    public static final DeferredItem<PickaxeItem> PEACOCK_PICKAXE = ITEMS.register(
            "peacock_pickaxe",
            () -> new PickaxeItem(
                    ModToolTiers.PEACOCK,
                    new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.PEACOCK, 1.0F, -2.8F))));

    public static final DeferredItem<AxeItem> PEACOCK_AXE = ITEMS.register(
            "peacock_axe",
            () -> new AxeItem(
                    ModToolTiers.PEACOCK,
                    new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.PEACOCK, 5.0F, -3.0F))));

    public static final DeferredItem<ShovelItem> PEACOCK_SHOVEL = ITEMS.register(
            "peacock_shovel",
            () -> new ShovelItem(
                    ModToolTiers.PEACOCK,
                    new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.PEACOCK, 1.5F, -3.0F))));

    public static final DeferredItem<HoeItem> PEACOCK_HOE = ITEMS.register(
            "peacock_hoe",
            () -> new HoeItem(
                    ModToolTiers.PEACOCK,
                    new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.PEACOCK, -3.0F, 0.0F))));

    // ========== Kivulite Tools ==========
    public static final DeferredItem<SwordItem> KIVULITE_SWORD = ITEMS.register(
            "kivulite_sword",
            () -> new KivuliteSwordItem(
                    ModToolTiers.KIVULITE,
                    new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.KIVULITE, 3, -2.4F))));
    public static final DeferredItem<PickaxeItem> KIVULITE_PICKAXE = ITEMS.register(
            "kivulite_pickaxe",
            () -> new KivulitePickaxeItem(
                    ModToolTiers.KIVULITE,
                    new Item.Properties()
                            .attributes(PickaxeItem.createAttributes(ModToolTiers.KIVULITE, 1.0F, -2.8F))));
    public static final DeferredItem<AxeItem> KIVULITE_AXE = ITEMS.register(
            "kivulite_axe",
            () -> new KivuliteAxeItem(
                    ModToolTiers.KIVULITE,
                    new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.KIVULITE, 6.0F, -3.1F))));
    public static final DeferredItem<ShovelItem> KIVULITE_SHOVEL = ITEMS.register(
            "kivulite_shovel",
            () -> new KivuliteShovelItem(
                    ModToolTiers.KIVULITE,
                    new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.KIVULITE, 1.5F, -3.0F))));
    public static final DeferredItem<HoeItem> KIVULITE_HOE = ITEMS.register(
            "kivulite_hoe",
            () -> new HoeItem(
                    ModToolTiers.KIVULITE,
                    new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.KIVULITE, -2.0F, -1.0F))));

    // ========== Corrupt Pridestone Tools ==========
    public static final DeferredItem<SwordItem> CORRUPT_SWORD = ITEMS.register(
            "corrupt_sword",
            () -> new SwordItem(
                    ModToolTiers.CORRUPT_PRIDESTONE,
                    new Item.Properties()
                            .attributes(SwordItem.createAttributes(ModToolTiers.CORRUPT_PRIDESTONE, 3, -2.4F))));
    public static final DeferredItem<PickaxeItem> CORRUPT_PICKAXE = ITEMS.register(
            "corrupt_pickaxe",
            () -> new PickaxeItem(
                    ModToolTiers.CORRUPT_PRIDESTONE,
                    new Item.Properties()
                            .attributes(PickaxeItem.createAttributes(ModToolTiers.CORRUPT_PRIDESTONE, 1.0F, -2.8F))));
    public static final DeferredItem<AxeItem> CORRUPT_AXE = ITEMS.register(
            "corrupt_axe",
            () -> new AxeItem(
                    ModToolTiers.CORRUPT_PRIDESTONE,
                    new Item.Properties()
                            .attributes(AxeItem.createAttributes(ModToolTiers.CORRUPT_PRIDESTONE, 6.0F, -3.1F))));
    public static final DeferredItem<ShovelItem> CORRUPT_SHOVEL = ITEMS.register(
            "corrupt_shovel",
            () -> new ShovelItem(
                    ModToolTiers.CORRUPT_PRIDESTONE,
                    new Item.Properties()
                            .attributes(ShovelItem.createAttributes(ModToolTiers.CORRUPT_PRIDESTONE, 1.5F, -3.0F))));
    public static final DeferredItem<HoeItem> CORRUPT_HOE = ITEMS.register(
            "corrupt_hoe",
            () -> new HoeItem(
                    ModToolTiers.CORRUPT_PRIDESTONE,
                    new Item.Properties()
                            .attributes(HoeItem.createAttributes(ModToolTiers.CORRUPT_PRIDESTONE, -1.0F, -2.0F))));

    // ========== Silver Armor ==========
    public static final DeferredItem<ArmorItem> SILVER_HELMET =
            armorItem("silver_helmet", ModArmorMaterials.SILVER, ArmorItem.Type.HELMET, 19);
    public static final DeferredItem<ArmorItem> SILVER_CHESTPLATE =
            armorItem("silver_chestplate", ModArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE, 19);
    public static final DeferredItem<ArmorItem> SILVER_LEGGINGS =
            armorItem("silver_leggings", ModArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS, 19);
    public static final DeferredItem<ArmorItem> SILVER_BOOTS =
            armorItem("silver_boots", ModArmorMaterials.SILVER, ArmorItem.Type.BOOTS, 19);

    // ========== Gemsbok Armor ==========
    public static final DeferredItem<ArmorItem> GEMSBOK_HELMET =
            armorItem("gemsbok_helmet", ModArmorMaterials.GEMSBOK, ArmorItem.Type.HELMET, 8);
    public static final DeferredItem<ArmorItem> GEMSBOK_CHESTPLATE =
            armorItem("gemsbok_chestplate", ModArmorMaterials.GEMSBOK, ArmorItem.Type.CHESTPLATE, 8);
    public static final DeferredItem<ArmorItem> GEMSBOK_LEGGINGS =
            armorItem("gemsbok_leggings", ModArmorMaterials.GEMSBOK, ArmorItem.Type.LEGGINGS, 8);
    public static final DeferredItem<ArmorItem> GEMSBOK_BOOTS =
            armorItem("gemsbok_boots", ModArmorMaterials.GEMSBOK, ArmorItem.Type.BOOTS, 8);

    // ========== Peacock Armor ==========
    public static final DeferredItem<ArmorItem> PEACOCK_HELMET =
            armorItem("peacock_helmet", ModArmorMaterials.PEACOCK, ArmorItem.Type.HELMET, 31);
    public static final DeferredItem<ArmorItem> PEACOCK_CHESTPLATE =
            armorItem("peacock_chestplate", ModArmorMaterials.PEACOCK, ArmorItem.Type.CHESTPLATE, 31);
    public static final DeferredItem<ArmorItem> PEACOCK_LEGGINGS =
            armorItem("peacock_leggings", ModArmorMaterials.PEACOCK, ArmorItem.Type.LEGGINGS, 31);
    public static final DeferredItem<ArmorItem> PEACOCK_BOOTS =
            armorItem("peacock_boots", ModArmorMaterials.PEACOCK, ArmorItem.Type.BOOTS, 31);

    // ========== Special Armor ==========
    public static final DeferredItem<ArmorItem> PEACOCK_WINGS =
            armorItem("peacock_wings", ModArmorMaterials.SPECIAL, ArmorItem.Type.CHESTPLATE, 8);

    // ========== Block Items ==========
    // Pridestone & Variants
    public static final DeferredItem<BlockItem> PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("pridestone", ModBlocks.PRIDESTONE);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("corrupt_pridestone", ModBlocks.CORRUPT_PRIDESTONE);
    public static final DeferredItem<BlockItem> PRIDE_BRICK_ITEM =
            registerBlockItem("pride_brick", ModBlocks.PRIDE_BRICK);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("corrupt_pride_brick", ModBlocks.CORRUPT_PRIDE_BRICK);
    public static final DeferredItem<BlockItem> CRACKED_PRIDE_BRICK_ITEM =
            registerBlockItem("cracked_pride_brick", ModBlocks.CRACKED_PRIDE_BRICK);
    public static final DeferredItem<BlockItem> MOSSY_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_pride_brick", ModBlocks.MOSSY_PRIDE_BRICK);
    public static final DeferredItem<BlockItem> MOSSY_CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_corrupt_pride_brick", ModBlocks.MOSSY_CORRUPT_PRIDE_BRICK);
    public static final DeferredItem<BlockItem> PRIDE_PILLAR_ITEM =
            registerBlockItem("pride_pillar", ModBlocks.PRIDE_PILLAR);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDE_PILLAR_ITEM =
            registerBlockItem("corrupt_pride_pillar", ModBlocks.CORRUPT_PRIDE_PILLAR);

    // Ores & Storage
    public static final DeferredItem<BlockItem> PRIDE_COAL_ORE_ITEM =
            registerBlockItem("pride_coal_ore", ModBlocks.PRIDE_COAL_ORE);
    public static final DeferredItem<BlockItem> SILVER_ORE_ITEM = registerBlockItem("silver_ore", ModBlocks.SILVER_ORE);
    public static final DeferredItem<BlockItem> PEACOCK_ORE_ITEM =
            registerBlockItem("peacock_ore", ModBlocks.PEACOCK_ORE);
    public static final DeferredItem<BlockItem> KIVULITE_ORE_ITEM =
            registerBlockItem("kivulite_ore", ModBlocks.KIVULITE_ORE);
    public static final DeferredItem<BlockItem> NUKA_ORE_ITEM = registerBlockItem("nuka_ore", ModBlocks.NUKA_ORE);
    public static final DeferredItem<BlockItem> SILVER_BLOCK_ITEM =
            registerBlockItem("silver_block", ModBlocks.SILVER_BLOCK);
    public static final DeferredItem<BlockItem> PEACOCK_BLOCK_ITEM =
            registerBlockItem("peacock_block", ModBlocks.PEACOCK_BLOCK);
    public static final DeferredItem<BlockItem> KIVULITE_BLOCK_ITEM =
            registerBlockItem("kivulite_block", ModBlocks.KIVULITE_BLOCK);

    // Wood - Acacia
    public static final DeferredItem<BlockItem> ACACIA_LOG_ITEM =
            registerBlockItem("pride_acacia_log", ModBlocks.ACACIA_LOG);
    public static final DeferredItem<BlockItem> ACACIA_PLANKS_ITEM =
            registerBlockItem("pride_acacia_planks", ModBlocks.ACACIA_PLANKS);
    public static final DeferredItem<BlockItem> ACACIA_STAIRS_ITEM =
            registerBlockItem("pride_acacia_stairs", ModBlocks.ACACIA_STAIRS);
    public static final DeferredItem<BlockItem> ACACIA_SLAB_ITEM =
            registerBlockItem("pride_acacia_slab", ModBlocks.ACACIA_SLAB);

    // Wood - Rainforest
    public static final DeferredItem<BlockItem> RAINFOREST_LOG_ITEM =
            registerBlockItem("rainforest_log", ModBlocks.RAINFOREST_LOG);
    public static final DeferredItem<BlockItem> RAINFOREST_PLANKS_ITEM =
            registerBlockItem("rainforest_planks", ModBlocks.RAINFOREST_PLANKS);
    public static final DeferredItem<BlockItem> RAINFOREST_STAIRS_ITEM =
            registerBlockItem("rainforest_stairs", ModBlocks.RAINFOREST_STAIRS);
    public static final DeferredItem<BlockItem> RAINFOREST_SLAB_ITEM =
            registerBlockItem("rainforest_slab", ModBlocks.RAINFOREST_SLAB);

    // Wood - Mango
    public static final DeferredItem<BlockItem> MANGO_LOG_ITEM = registerBlockItem("mango_log", ModBlocks.MANGO_LOG);
    public static final DeferredItem<BlockItem> MANGO_PLANKS_ITEM =
            registerBlockItem("mango_planks", ModBlocks.MANGO_PLANKS);
    public static final DeferredItem<BlockItem> MANGO_STAIRS_ITEM =
            registerBlockItem("mango_stairs", ModBlocks.MANGO_STAIRS);
    public static final DeferredItem<BlockItem> MANGO_SLAB_ITEM = registerBlockItem("mango_slab", ModBlocks.MANGO_SLAB);

    // Wood - Passion
    public static final DeferredItem<BlockItem> PASSION_LOG_ITEM =
            registerBlockItem("passion_log", ModBlocks.PASSION_LOG);
    public static final DeferredItem<BlockItem> PASSION_PLANKS_ITEM =
            registerBlockItem("passion_planks", ModBlocks.PASSION_PLANKS);
    public static final DeferredItem<BlockItem> PASSION_STAIRS_ITEM =
            registerBlockItem("passion_stairs", ModBlocks.PASSION_STAIRS);
    public static final DeferredItem<BlockItem> PASSION_SLAB_ITEM =
            registerBlockItem("passion_slab", ModBlocks.PASSION_SLAB);

    // Wood - Banana
    public static final DeferredItem<BlockItem> BANANA_LOG_ITEM = registerBlockItem("banana_log", ModBlocks.BANANA_LOG);
    public static final DeferredItem<BlockItem> BANANA_PLANKS_ITEM =
            registerBlockItem("banana_planks", ModBlocks.BANANA_PLANKS);
    public static final DeferredItem<BlockItem> BANANA_STAIRS_ITEM =
            registerBlockItem("banana_stairs", ModBlocks.BANANA_STAIRS);
    public static final DeferredItem<BlockItem> BANANA_SLAB_ITEM =
            registerBlockItem("banana_slab", ModBlocks.BANANA_SLAB);

    // Wood - Deadwood
    public static final DeferredItem<BlockItem> DEADWOOD_LOG_ITEM =
            registerBlockItem("deadwood_log", ModBlocks.DEADWOOD_LOG);
    public static final DeferredItem<BlockItem> DEADWOOD_PLANKS_ITEM =
            registerBlockItem("deadwood_planks", ModBlocks.DEADWOOD_PLANKS);
    public static final DeferredItem<BlockItem> DEADWOOD_STAIRS_ITEM =
            registerBlockItem("deadwood_stairs", ModBlocks.DEADWOOD_STAIRS);
    public static final DeferredItem<BlockItem> DEADWOOD_SLAB_ITEM =
            registerBlockItem("deadwood_slab", ModBlocks.DEADWOOD_SLAB);

    // Stone stairs/slabs
    public static final DeferredItem<BlockItem> PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("pridestone_stairs", ModBlocks.PRIDESTONE_STAIRS);
    public static final DeferredItem<BlockItem> PRIDESTONE_SLAB_ITEM =
            registerBlockItem("pridestone_slab", ModBlocks.PRIDESTONE_SLAB);
    public static final DeferredItem<BlockItem> PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("pride_brick_stairs", ModBlocks.PRIDE_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("pride_brick_slab", ModBlocks.PRIDE_BRICK_SLAB);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("corrupt_pridestone_stairs", ModBlocks.CORRUPT_PRIDESTONE_STAIRS);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDESTONE_SLAB_ITEM =
            registerBlockItem("corrupt_pridestone_slab", ModBlocks.CORRUPT_PRIDESTONE_SLAB);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("corrupt_pride_brick_stairs", ModBlocks.CORRUPT_PRIDE_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("corrupt_pride_brick_slab", ModBlocks.CORRUPT_PRIDE_BRICK_SLAB);
    public static final DeferredItem<BlockItem> PRIDE_PILLAR_SLAB_ITEM =
            registerBlockItem("pride_pillar_slab", ModBlocks.PRIDE_PILLAR_SLAB);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDE_PILLAR_SLAB_ITEM =
            registerBlockItem("corrupt_pride_pillar_slab", ModBlocks.CORRUPT_PRIDE_PILLAR_SLAB);

    // Walls
    public static final DeferredItem<BlockItem> PRIDESTONE_WALL_ITEM =
            registerBlockItem("pridestone_wall", ModBlocks.PRIDESTONE_WALL);
    public static final DeferredItem<BlockItem> PRIDE_BRICK_WALL_ITEM =
            registerBlockItem("pride_brick_wall", ModBlocks.PRIDE_BRICK_WALL);
    public static final DeferredItem<BlockItem> CORRUPT_PRIDESTONE_WALL_ITEM =
            registerBlockItem("corrupt_pridestone_wall", ModBlocks.CORRUPT_PRIDESTONE_WALL);

    // Redstone
    public static final DeferredItem<BlockItem> PRIDESTONE_PRESSURE_PLATE_ITEM =
            registerBlockItem("pridestone_pressure_plate", ModBlocks.PRIDESTONE_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> PRIDESTONE_BUTTON_ITEM =
            registerBlockItem("pridestone_button", ModBlocks.PRIDESTONE_BUTTON);

    // Misc blocks
    public static final DeferredItem<BlockItem> DRIED_MAIZE_BLOCK_ITEM =
            registerBlockItem("dried_maize_block", ModBlocks.DRIED_MAIZE_BLOCK);
    public static final DeferredItem<BlockItem> DRIED_MAIZE_STAIRS_ITEM =
            registerBlockItem("dried_maize_stairs", ModBlocks.DRIED_MAIZE_STAIRS);
    public static final DeferredItem<BlockItem> DRIED_MAIZE_SLAB_ITEM =
            registerBlockItem("dried_maize_slab", ModBlocks.DRIED_MAIZE_SLAB);
    public static final DeferredItem<BlockItem> OUTSAND_ITEM = registerBlockItem("outsand", ModBlocks.OUTSAND);
    public static final DeferredItem<BlockItem> OUTGLASS_ITEM = registerBlockItem("outglass", ModBlocks.OUTGLASS);
    public static final DeferredItem<BlockItem> OUTGLASS_PANE_ITEM =
            registerBlockItem("outglass_pane", ModBlocks.OUTGLASS_PANE);
    public static final DeferredItem<BlockItem> TERMITE_MOUND_ITEM =
            registerBlockItem("termite_mound", ModBlocks.TERMITE_MOUND);
    public static final DeferredItem<BlockItem> ZIRA_MOUND_GATE_ITEM =
            registerBlockItem("zira_mound_gate", ModBlocks.ZIRA_MOUND_GATE);
    public static final DeferredItem<BlockItem> PUMBAA_BOX_ITEM = registerBlockItem("pumbaa_box", ModBlocks.PUMBAA_BOX);

    // Leaves
    public static final DeferredItem<BlockItem> ACACIA_LEAVES_ITEM =
            registerBlockItem("pride_acacia_leaves", ModBlocks.ACACIA_LEAVES);
    public static final DeferredItem<BlockItem> RAINFOREST_LEAVES_ITEM =
            registerBlockItem("rainforest_leaves", ModBlocks.RAINFOREST_LEAVES);
    public static final DeferredItem<BlockItem> MANGO_LEAVES_ITEM =
            registerBlockItem("mango_leaves", ModBlocks.MANGO_LEAVES);
    public static final DeferredItem<BlockItem> PASSION_LEAVES_ITEM =
            registerBlockItem("passion_leaves", ModBlocks.PASSION_LEAVES);
    public static final DeferredItem<BlockItem> BANANA_LEAVES_ITEM =
            registerBlockItem("banana_leaves", ModBlocks.BANANA_LEAVES);
    public static final DeferredItem<BlockItem> RAFIKI_LEAVES_ITEM =
            registerBlockItem("rafiki_leaves", ModBlocks.RAFIKI_LEAVES);

    // Saplings
    public static final DeferredItem<BlockItem> ACACIA_SAPLING_ITEM =
            registerBlockItem("pride_acacia_sapling", ModBlocks.ACACIA_SAPLING);
    public static final DeferredItem<BlockItem> RAINFOREST_SAPLING_ITEM =
            registerBlockItem("rainforest_sapling", ModBlocks.RAINFOREST_SAPLING);
    public static final DeferredItem<BlockItem> MANGO_SAPLING_ITEM =
            registerBlockItem("mango_sapling", ModBlocks.MANGO_SAPLING);
    public static final DeferredItem<BlockItem> PASSION_SAPLING_ITEM =
            registerBlockItem("passion_sapling", ModBlocks.PASSION_SAPLING);
    public static final DeferredItem<BlockItem> BANANA_SAPLING_ITEM =
            registerBlockItem("banana_sapling", ModBlocks.BANANA_SAPLING);

    // Rafiki Wood
    public static final DeferredItem<BlockItem> RAFIKI_WOOD_ITEM =
            registerBlockItem("rafiki_wood", ModBlocks.RAFIKI_WOOD);

    // Flowers
    public static final DeferredItem<BlockItem> WHITE_FLOWER_ITEM =
            registerBlockItem("white_flower", ModBlocks.WHITE_FLOWER);
    public static final DeferredItem<BlockItem> BLUE_FLOWER_ITEM =
            registerBlockItem("blue_flower", ModBlocks.BLUE_FLOWER);
    public static final DeferredItem<BlockItem> PURPLE_FLOWER_ITEM =
            registerBlockItem("purple_flower", ModBlocks.PURPLE_FLOWER);
    public static final DeferredItem<BlockItem> RED_FLOWER_ITEM = registerBlockItem("red_flower", ModBlocks.RED_FLOWER);

    // Waterlilies
    public static final DeferredItem<BlockItem> LILY_RED_ITEM =
            ITEMS.register("lily_red", () -> new LilyPadItem(ModBlocks.LILY_RED.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LILY_VIOLET_ITEM =
            ITEMS.register("lily_violet", () -> new LilyPadItem(ModBlocks.LILY_VIOLET.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LILY_WHITE_ITEM =
            ITEMS.register("lily_white", () -> new LilyPadItem(ModBlocks.LILY_WHITE.get(), new Item.Properties()));

    // Mushrooms
    public static final DeferredItem<BlockItem> OUTSHROOM_ITEM = registerBlockItem("outshroom", ModBlocks.OUTSHROOM);
    public static final DeferredItem<BlockItem> OUTSHROOM_GLOWING_ITEM =
            registerBlockItem("outshroom_glowing", ModBlocks.OUTSHROOM_GLOWING);

    // Misc nature
    public static final DeferredItem<BlockItem> ARID_GRASS_ITEM = registerBlockItem("arid_grass", ModBlocks.ARID_GRASS);
    public static final DeferredItem<Item> HYENA_TORCH_ITEM = ITEMS.register(
            "hyena_torch",
            () -> new StandingAndWallBlockItem(
                    ModBlocks.HYENA_TORCH.get(),
                    ModBlocks.HYENA_WALL_TORCH.get(),
                    new Item.Properties(),
                    Direction.DOWN));
    public static final DeferredItem<BlockItem> HANGING_BANANA_ITEM =
            registerBlockItem("hanging_banana", ModBlocks.HANGING_BANANA);
    public static final DeferredItem<BlockItem> KIWANO_BLOCK_ITEM =
            registerBlockItem("kiwano_block", ModBlocks.KIWANO_BLOCK);

    // Crop seeds
    public static final DeferredItem<Item> KIWANO_SEEDS = ITEMS.register(
            "kiwano_seeds", () -> new ItemNameBlockItem(ModBlocks.KIWANO_STEM.get(), new Item.Properties()));

    public static final DeferredItem<Item> YAM =
            ITEMS.register("yam", () -> new ItemNameBlockItem(ModBlocks.YAM_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> MAIZE_STALKS = ITEMS.register(
            "maize_stalks", () -> new ItemNameBlockItem(ModBlocks.MAIZE_CROP.get(), new Item.Properties()));

    // Decorative block entities
    public static final DeferredItem<BlockItem> HYENA_HEAD_ITEM = ITEMS.register(
            "hyena_head", () -> new HyenaHeadBlockItem(ModBlocks.HYENA_HEAD.get(), new Item.Properties()));
    // Dyeable rugs (carpet-style)
    public static final DeferredItem<BlockItem> RUG_LION_ITEM = registerBlockItem("rug_lion", ModBlocks.RUG_LION);
    public static final DeferredItem<BlockItem> RUG_BLACK_ITEM = registerBlockItem("rug_black", ModBlocks.RUG_BLACK);
    public static final DeferredItem<BlockItem> RUG_BLUE_ITEM = registerBlockItem("rug_blue", ModBlocks.RUG_BLUE);
    public static final DeferredItem<BlockItem> RUG_GREEN_ITEM = registerBlockItem("rug_green", ModBlocks.RUG_GREEN);
    public static final DeferredItem<BlockItem> RUG_GRAY_ITEM = registerBlockItem("rug_gray", ModBlocks.RUG_GRAY);
    public static final DeferredItem<BlockItem> RUG_LIGHT_BLUE_ITEM =
            registerBlockItem("rug_light_blue", ModBlocks.RUG_LIGHT_BLUE);
    public static final DeferredItem<BlockItem> RUG_LIGHT_GREEN_ITEM =
            registerBlockItem("rug_light_green", ModBlocks.RUG_LIGHT_GREEN);
    public static final DeferredItem<BlockItem> RUG_LIGHT_GRAY_ITEM =
            registerBlockItem("rug_light_gray", ModBlocks.RUG_LIGHT_GRAY);
    public static final DeferredItem<BlockItem> RUG_ORANGE_ITEM = registerBlockItem("rug_orange", ModBlocks.RUG_ORANGE);
    public static final DeferredItem<BlockItem> RUG_OUTLANDER_ITEM =
            registerBlockItem("rug_outlander", ModBlocks.RUG_OUTLANDER);
    public static final DeferredItem<BlockItem> RUG_PINK_ITEM = registerBlockItem("rug_pink", ModBlocks.RUG_PINK);
    public static final DeferredItem<BlockItem> RUG_PURPLE_ITEM = registerBlockItem("rug_purple", ModBlocks.RUG_PURPLE);
    public static final DeferredItem<BlockItem> RUG_RED_ITEM = registerBlockItem("rug_red", ModBlocks.RUG_RED);
    public static final DeferredItem<BlockItem> RUG_VIOLET_ITEM = registerBlockItem("rug_violet", ModBlocks.RUG_VIOLET);
    public static final DeferredItem<BlockItem> RUG_WHITE_ITEM = registerBlockItem("rug_white", ModBlocks.RUG_WHITE);
    public static final DeferredItem<BlockItem> RUG_YELLOW_ITEM = registerBlockItem("rug_yellow", ModBlocks.RUG_YELLOW);

    // ========== Outlands Armor ==========
    public static final DeferredItem<ArmorItem> OUTLANDS_HELMET =
            armorItem("outlands_helmet", ModArmorMaterials.SPECIAL, ArmorItem.Type.HELMET, 12);

    // ========== Ticket Lion Suit ==========
    public static final DeferredItem<ArmorItem> TICKET_LION_HEAD =
            armorItem("ticket_lion_head", ModArmorMaterials.TICKET_LION, ArmorItem.Type.HELMET, 0);
    public static final DeferredItem<ArmorItem> TICKET_LION_SUIT =
            armorItem("ticket_lion_suit", ModArmorMaterials.TICKET_LION, ArmorItem.Type.CHESTPLATE, 0);
    public static final DeferredItem<ArmorItem> TICKET_LION_LEGS =
            armorItem("ticket_lion_legs", ModArmorMaterials.TICKET_LION, ArmorItem.Type.LEGGINGS, 0);
    public static final DeferredItem<ArmorItem> TICKET_LION_FEET =
            armorItem("ticket_lion_feet", ModArmorMaterials.TICKET_LION, ArmorItem.Type.BOOTS, 0);

    // ========== Darts ==========
    public static final DeferredItem<Item> DART_BLUE = simpleItem("dart_blue");
    public static final DeferredItem<Item> DART_RED = simpleItem("dart_red");
    public static final DeferredItem<Item> DART_YELLOW = simpleItem("dart_yellow");
    public static final DeferredItem<Item> DART_PINK = simpleItem("dart_pink");
    public static final DeferredItem<Item> DART_BLACK = simpleItem("dart_black");
    public static final DeferredItem<Item> DART_OUTLANDISH = simpleItem("dart_outlandish");

    // ========== Dart Shooters ==========
    public static final DeferredItem<Item> DART_SHOOTER =
            ITEMS.register("dart_shooter", () -> new DartShooterItem(false));
    public static final DeferredItem<Item> DART_SHOOTER_SILVER =
            ITEMS.register("dart_shooter_silver", () -> new DartShooterItem(true));

    // ========== Spears ==========
    public static final DeferredItem<Item> GEMSBOK_SPEAR = ITEMS.register("gemsbok_spear", () -> new SpearItem(false));
    public static final DeferredItem<Item> POISONED_SPEAR = ITEMS.register("poisoned_spear", () -> new SpearItem(true));

    // ========== Bombs ==========
    public static final DeferredItem<Item> PUMBAA_BOMB = ITEMS.register("pumbaa_bomb", PumbaaBombItem::new);

    // ========== Spawn Eggs - Passive ==========
    public static final DeferredItem<Item> LION_SPAWN_EGG = spawnEgg(EntityTypes.LION, 0xD4A030, 0x8B6914);
    public static final DeferredItem<Item> ZEBRA_SPAWN_EGG = spawnEgg(EntityTypes.ZEBRA, 0xFFFFFF, 0x222222);
    public static final DeferredItem<Item> GIRAFFE_SPAWN_EGG = spawnEgg(EntityTypes.GIRAFFE, 0xE8B84B, 0x8B5E3C);
    public static final DeferredItem<Item> RHINO_SPAWN_EGG = spawnEgg(EntityTypes.RHINO, 0x808080, 0x505050);
    public static final DeferredItem<Item> GEMSBOK_SPAWN_EGG = spawnEgg(EntityTypes.GEMSBOK, 0xC8A878, 0x4A3B2A);
    public static final DeferredItem<Item> DIKDIK_SPAWN_EGG = spawnEgg(EntityTypes.DIKDIK, 0xB8956A, 0x8B7355);
    public static final DeferredItem<Item> FLAMINGO_SPAWN_EGG = spawnEgg(EntityTypes.FLAMINGO, 0xFF69B4, 0xFF1493);
    public static final DeferredItem<Item> ZAZU_SPAWN_EGG = spawnEgg(EntityTypes.ZAZU, 0x4169E1, 0xFFD700);
    public static final DeferredItem<Item> BUG_SPAWN_EGG = spawnEgg(EntityTypes.BUG, 0x4B3621, 0x2E1F0F);

    // ========== Spawn Eggs - Hostile ==========
    public static final DeferredItem<Item> HYENA_SPAWN_EGG = spawnEgg(EntityTypes.HYENA, 0x8B7355, 0x4A3B2A);
    public static final DeferredItem<Item> SKELETAL_HYENA_SPAWN_EGG =
            spawnEgg(EntityTypes.SKELETAL_HYENA, 0xC8C8C8, 0x505050);
    public static final DeferredItem<Item> OUTLANDER_SPAWN_EGG = spawnEgg(EntityTypes.OUTLANDER, 0x5C3A1E, 0x3A2510);
    public static final DeferredItem<Item> VULTURE_SPAWN_EGG = spawnEgg(EntityTypes.VULTURE, 0x2A1F14, 0x8B0000);
    public static final DeferredItem<Item> CROCODILE_SPAWN_EGG = spawnEgg(EntityTypes.CROCODILE, 0x3B5323, 0x1A2E0A);
    public static final DeferredItem<Item> TERMITE_SPAWN_EGG = spawnEgg(EntityTypes.TERMITE, 0xD2B48C, 0x8B6914);
    public static final DeferredItem<Item> TERMITE_QUEEN_SPAWN_EGG =
            spawnEgg(EntityTypes.TERMITE_QUEEN, 0xD2B48C, 0xFF4500);

    // ========== Spawn Eggs - Special ==========
    public static final DeferredItem<Item> TICKET_LION_SPAWN_EGG =
            spawnEgg(EntityTypes.TICKET_LION, 0xD4A030, 0x4169E1);

    // ========== Spawn Eggs - NPCs ==========
    public static final DeferredItem<Item> RAFIKI_SPAWN_EGG = spawnEgg(EntityTypes.RAFIKI, 0x8B4513, 0xFFD700);
    public static final DeferredItem<Item> SIMBA_SPAWN_EGG = spawnEgg(EntityTypes.SIMBA, 0xD4A030, 0xFFD700);
    public static final DeferredItem<Item> TIMON_SPAWN_EGG = spawnEgg(EntityTypes.TIMON, 0xB8860B, 0xFFE4B5);
    public static final DeferredItem<Item> PUMBAA_SPAWN_EGG = spawnEgg(EntityTypes.PUMBAA, 0x8B4513, 0x654321);
    public static final DeferredItem<Item> SCAR_SPAWN_EGG = spawnEgg(EntityTypes.SCAR, 0x2F1A00, 0x000000);
    public static final DeferredItem<Item> ZIRA_SPAWN_EGG = spawnEgg(EntityTypes.ZIRA, 0x5C3A1E, 0x8B0000);

    // ========== Jar Items ==========
    public static final DeferredItem<Item> JAR_EMPTY = registerItem("jar_empty", JarItem::new, itemProps(16));
    // craftRemainder(JAR_EMPTY.get()) must be inside the lambda — .get() fails during static init
    public static final DeferredItem<Item> JAR_WATER = ITEMS.register(
            "jar_water", () -> new FilledJarItem(Fluids.WATER, itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final DeferredItem<Item> JAR_MILK =
            ITEMS.register("jar_milk", () -> new ZebraMilkItem(itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final DeferredItem<Item> JAR_LAVA = ITEMS.register(
            "jar_lava", () -> new FilledJarItem(Fluids.LAVA, itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final DeferredItem<Item> MANGO_JUICE = ITEMS.register(
            "mango_juice",
            () -> new MangoJuiceItem(itemProps(16).food(foodProps(6, 0.5F)).craftRemainder(JAR_EMPTY.get())));

    public static final DeferredItem<Item> HYENA_MEAL = registerItem("hyena_meal", HyenaMealItem::new);

    // ========== Giraffe Ties ==========
    public static final DeferredItem<Item> GIRAFFE_TIE = simpleItem("giraffe_tie");
    public static final DeferredItem<Item> GIRAFFE_TIE_WHITE = simpleItem("giraffe_tie_white");
    public static final DeferredItem<Item> GIRAFFE_TIE_BLUE = simpleItem("giraffe_tie_blue");
    public static final DeferredItem<Item> GIRAFFE_TIE_YELLOW = simpleItem("giraffe_tie_yellow");
    public static final DeferredItem<Item> GIRAFFE_TIE_RED = simpleItem("giraffe_tie_red");
    public static final DeferredItem<Item> GIRAFFE_TIE_PURPLE = simpleItem("giraffe_tie_purple");
    public static final DeferredItem<Item> GIRAFFE_TIE_GREEN = simpleItem("giraffe_tie_green");
    public static final DeferredItem<Item> GIRAFFE_TIE_BLACK = simpleItem("giraffe_tie_black");

    // ========== Tunnah Diggah ==========
    public static final DeferredItem<Item> TUNNAH_DIGGAH = ITEMS.register(
            "tunnah_diggah",
            () -> new TunnahDiggahItem(
                    Tiers.IRON,
                    new Item.Properties()
                            .durability(690)
                            .attributes(TunnahDiggahItem.createAttributes(Tiers.IRON, 1.0F, -2.8F))));

    // ========== Quest & Special Items ==========
    public static final DeferredItem<Item> AMULET = ITEMS.register(
            "amulet",
            () -> new ArmorItem(
                    ModArmorMaterials.AMULET,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).durability(0)));
    public static final DeferredItem<Item> ASTRAL_CHARM = registerItem("astral_charm", AstralCharmItem::new);
    public static final DeferredItem<Item> GIRAFFE_SADDLE = simpleItem("giraffe_saddle", 1);
    // Disabled pending rework — see https://github.com/ron1196/circle-of-craft/issues/78
    // public static final RegistryObject<Item> DART_QUIVER = registerItem("dart_quiver", QuiverItem::new,
    // itemProps(1));
    public static final DeferredItem<Item> PASSION_FRUIT = registerItem(
            "passion_fruit",
            PassionFruitItem::new,
            itemProps(64)
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationModifier(0.3F)
                            .alwaysEdible()
                            .build()));
    public static final DeferredItem<Item> ZAZU_EGG = ITEMS.register("zazu_egg", ZazuEggItem::new);

    // ========== Block Entity Items ==========
    public static final DeferredItem<BlockItem> GRINDING_BOWL_ITEM =
            registerBlockItem("grinding_bowl", ModBlocks.GRINDING_BOWL);
    public static final DeferredItem<BlockItem> BUG_TRAP_ITEM = registerBlockItem("bug_trap", ModBlocks.BUG_TRAP);
    public static final DeferredItem<BlockItem> OAK_BUG_TRAP_ITEM =
            registerBlockItem("oak_bug_trap", ModBlocks.OAK_BUG_TRAP);
    public static final DeferredItem<BlockItem> SPRUCE_BUG_TRAP_ITEM =
            registerBlockItem("spruce_bug_trap", ModBlocks.SPRUCE_BUG_TRAP);
    public static final DeferredItem<BlockItem> BIRCH_BUG_TRAP_ITEM =
            registerBlockItem("birch_bug_trap", ModBlocks.BIRCH_BUG_TRAP);
    public static final DeferredItem<BlockItem> JUNGLE_BUG_TRAP_ITEM =
            registerBlockItem("jungle_bug_trap", ModBlocks.JUNGLE_BUG_TRAP);
    public static final DeferredItem<BlockItem> ACACIA_BUG_TRAP_ITEM =
            registerBlockItem("acacia_bug_trap", ModBlocks.ACACIA_BUG_TRAP);
    public static final DeferredItem<BlockItem> DARK_OAK_BUG_TRAP_ITEM =
            registerBlockItem("dark_oak_bug_trap", ModBlocks.DARK_OAK_BUG_TRAP);
    public static final DeferredItem<BlockItem> MANGROVE_BUG_TRAP_ITEM =
            registerBlockItem("mangrove_bug_trap", ModBlocks.MANGROVE_BUG_TRAP);
    public static final DeferredItem<BlockItem> CHERRY_BUG_TRAP_ITEM =
            registerBlockItem("cherry_bug_trap", ModBlocks.CHERRY_BUG_TRAP);
    public static final DeferredItem<BlockItem> BAMBOO_BUG_TRAP_ITEM =
            registerBlockItem("bamboo_bug_trap", ModBlocks.BAMBOO_BUG_TRAP);
    public static final DeferredItem<BlockItem> CRIMSON_BUG_TRAP_ITEM =
            registerBlockItem("crimson_bug_trap", ModBlocks.CRIMSON_BUG_TRAP);
    public static final DeferredItem<BlockItem> WARPED_BUG_TRAP_ITEM =
            registerBlockItem("warped_bug_trap", ModBlocks.WARPED_BUG_TRAP);
    public static final DeferredItem<BlockItem> BANANA_BUG_TRAP_ITEM =
            registerBlockItem("banana_bug_trap", ModBlocks.BANANA_BUG_TRAP);
    public static final DeferredItem<BlockItem> RAINFOREST_BUG_TRAP_ITEM =
            registerBlockItem("rainforest_bug_trap", ModBlocks.RAINFOREST_BUG_TRAP);
    public static final DeferredItem<BlockItem> MANGO_BUG_TRAP_ITEM =
            registerBlockItem("mango_bug_trap", ModBlocks.MANGO_BUG_TRAP);
    public static final DeferredItem<BlockItem> PASSION_BUG_TRAP_ITEM =
            registerBlockItem("passion_bug_trap", ModBlocks.PASSION_BUG_TRAP);
    public static final DeferredItem<BlockItem> DEADWOOD_BUG_TRAP_ITEM =
            registerBlockItem("deadwood_bug_trap", ModBlocks.DEADWOOD_BUG_TRAP);
    public static final DeferredItem<BlockItem> BONGO_DRUM_ITEM = registerBlockItem("bongo_drum", ModBlocks.BONGO_DRUM);
    public static final DeferredItem<BlockItem> OUTLANDS_POOL_ITEM =
            registerBlockItem("outlands_pool", ModBlocks.OUTLANDS_POOL);

    // ========== Quest / NPC Items ==========
    public static final DeferredItem<Item> QUEST_BOOK = registerItem("quest_book", QuestBookItem::new);

    public static final DeferredItem<Item> TICKET = registerItem("ticket", TicketItem::new);

    public static final DeferredItem<Item> RHYTHM_STAFF = simpleItem("rhythm_staff", 1);

    public static final DeferredItem<Item> RAFIKI_COIN = ITEMS.register(
            "rafiki_coin",
            () -> new CoinItem(Dimensions.PRIDE_LANDS_LEVEL, ModStructurePiece.RAFIKI_TREE_ID, new Item.Properties()));
    public static final DeferredItem<Item> ZIRA_COIN = ITEMS.register(
            "zira_coin",
            () -> new CoinItem(Dimensions.OUTLANDS_LEVEL, ModStructurePiece.ZIRA_MOUND_ID, new Item.Properties()));
    public static final DeferredItem<Item> WAYWARD_FEATHER = registerItem("wayward_feather", WaywardFeatherItem::new);

    public static final DeferredItem<Item> RAFIKI_STICK = registerItem("rafiki_stick", RafikiStickItem::new);

    public static final DeferredItem<Item> RAFIKI_DUST = registerItem("rafiki_dust", RafikiDustItem::new);
    public static final DeferredItem<Item> ANCESTORS_SIGHT =
            registerItem("ancestors_sight", AncestorsSightItem::new, itemProps(1));
    public static final DeferredItem<Item> PRIDE_COMPASS = registerItem("pride_compass", PrideCompassItem::new);

    // ========== Phase 12: Missing Block Items ==========
    public static final DeferredItem<BlockItem> BANANA_CAKE_ITEM =
            registerBlockItem("banana_cake", ModBlocks.BANANA_CAKE);
    public static final DeferredItem<BlockItem> MOUNTED_SHOOTER_ITEM =
            registerBlockItem("mounted_shooter", ModBlocks.MOUNTED_SHOOTER);
    public static final DeferredItem<BlockItem> STAR_ALTAR_ITEM = registerBlockItem("star_altar", ModBlocks.STAR_ALTAR);
    public static final DeferredItem<BlockItem> OUTLANDS_ALTAR_ITEM =
            registerBlockItem("outlands_altar", ModBlocks.OUTLANDS_ALTAR);
    public static final DeferredItem<BlockItem> TILLED_SAND_ITEM =
            registerBlockItem("tilled_sand", ModBlocks.TILLED_SAND);
    public static final DeferredItem<BlockItem> VASE_ITEM = registerBlockItem("vase", ModBlocks.VASE);
    public static final DeferredItem<BlockItem> VASE_ACACIA_ITEM =
            registerBlockItem("vase_acacia", ModBlocks.VASE_ACACIA);
    public static final DeferredItem<BlockItem> VASE_RAINFOREST_ITEM =
            registerBlockItem("vase_rainforest", ModBlocks.VASE_RAINFOREST);
    public static final DeferredItem<BlockItem> VASE_MANGO_ITEM = registerBlockItem("vase_mango", ModBlocks.VASE_MANGO);
    public static final DeferredItem<BlockItem> VASE_PASSION_ITEM =
            registerBlockItem("vase_passion", ModBlocks.VASE_PASSION);
    public static final DeferredItem<BlockItem> VASE_BANANA_ITEM =
            registerBlockItem("vase_banana", ModBlocks.VASE_BANANA);
    public static final DeferredItem<BlockItem> VASE_WHITE_FLOWER_ITEM =
            registerBlockItem("vase_white_flower", ModBlocks.VASE_WHITE_FLOWER);
    public static final DeferredItem<BlockItem> VASE_BLUE_FLOWER_ITEM =
            registerBlockItem("vase_blue_flower", ModBlocks.VASE_BLUE_FLOWER);
    public static final DeferredItem<BlockItem> VASE_RED_FLOWER_ITEM =
            registerBlockItem("vase_red_flower", ModBlocks.VASE_RED_FLOWER);
    public static final DeferredItem<BlockItem> VASE_PURPLE_FLOWER_ITEM =
            registerBlockItem("vase_purple_flower", ModBlocks.VASE_PURPLE_FLOWER);
    public static final DeferredItem<BlockItem> VASE_OUTSHROOM_ITEM =
            registerBlockItem("vase_outshroom", ModBlocks.VASE_OUTSHROOM);
    public static final DeferredItem<BlockItem> VASE_OUTSHROOM_GLOWING_ITEM =
            registerBlockItem("vase_outshroom_glowing", ModBlocks.VASE_OUTSHROOM_GLOWING);

    // ========== Bed & Lever ==========
    public static final DeferredItem<BlockItem> PRIDE_BED_ITEM =
            ITEMS.register("pride_bed", () -> new BedItem(ModBlocks.PRIDE_BED.get(), itemProps(1)));
    public static final DeferredItem<BlockItem> PRIDE_LEVER_ITEM =
            registerBlockItem("pride_lever", ModBlocks.PRIDE_LEVER);

    // ========== Portal Frame Items ==========
    public static final DeferredItem<BlockItem> PRIDE_PORTAL_FRAME_ITEM =
            registerBlockItem("pride_portal_frame", ModBlocks.PRIDE_PORTAL_FRAME);
    public static final DeferredItem<BlockItem> OUTLANDS_PORTAL_FRAME_ITEM =
            registerBlockItem("outlands_portal_frame", ModBlocks.OUTLANDS_PORTAL_FRAME);

    // ========== Scar / Zira Rugs ==========
    public static final DeferredItem<Item> SCAR_RUG =
            ITEMS.register("scar_rug", () -> new RugItem(RugEntity.TYPE_SCAR, itemProps(1)));
    public static final DeferredItem<Item> ZIRA_RUG =
            ITEMS.register("zira_rug", () -> new RugItem(RugEntity.TYPE_ZIRA, itemProps(1)));

    // ========== Skeletal Hyena Head Spawn Egg ==========
    public static final DeferredItem<Item> SKELETAL_HYENA_HEAD_SPAWN_EGG =
            spawnEgg(EntityTypes.SKELETAL_HYENA_HEAD, 0xC8C8C8, 0x3A3A3A);

    // ========== Notes (for Bongo Drum) ==========
    public static final DeferredItem<Item> NOTE_C =
            ITEMS.register("note_c", () -> new NoteItem(1, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_D =
            ITEMS.register("note_d", () -> new NoteItem(1, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_E =
            ITEMS.register("note_e", () -> new NoteItem(2, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_F =
            ITEMS.register("note_f", () -> new NoteItem(5, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_G =
            ITEMS.register("note_g", () -> new NoteItem(5, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_A =
            ITEMS.register("note_a", () -> new NoteItem(10, new Item.Properties()));
    public static final DeferredItem<Item> NOTE_B =
            ITEMS.register("note_b", () -> new NoteItem(20, new Item.Properties()));
}
