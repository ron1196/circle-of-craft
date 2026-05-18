package io.github.ron1196.thelionking.registry;

import static io.github.ron1196.thelionking.registry.LionKingItemsRegistryHelper.*;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.item.*;
import io.github.ron1196.thelionking.item.tier.LionKingArmorMaterials;
import io.github.ron1196.thelionking.item.tier.LionKingToolTiers;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LionKingItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TheLionKingMod.MOD_ID);

    // ========== Material Items ==========
    public static final RegistryObject<Item> PRIDESTONE_ITEM = simpleItem("pridestone_item");
    public static final RegistryObject<Item> CORRUPT_PRIDESTONE_ITEM = simpleItem("corrupt_pridestone_item");
    public static final RegistryObject<Item> SILVER_INGOT = simpleItem("silver_ingot");
    public static final RegistryObject<Item> PEACOCK_GEM = simpleItem("peacock_gem");
    public static final RegistryObject<Item> KIVULITE = simpleItem("kivulite");
    public static final RegistryObject<Item> HYENA_BONE = simpleItem("hyena_bone");
    public static final RegistryObject<Item> HYENA_BONE_SHARD = simpleItem("hyena_bone_shard");
    public static final RegistryObject<Item> TERMITE_DUST = simpleItem("termite_dust");
    public static final RegistryObject<Item> TERMITE_THROWN = ITEMS.register("termite_thrown", ThrownTermiteItem::new);
    public static final RegistryObject<Item> MANGO_DUST = simpleItem("mango_dust");
    public static final RegistryObject<Item> FEATHER_BLUE = simpleItem("feather_blue");
    public static final RegistryObject<Item> FEATHER_YELLOW = simpleItem("feather_yellow");
    public static final RegistryObject<Item> FEATHER_RED = simpleItem("feather_red");
    public static final RegistryObject<Item> FEATHER_BLACK = simpleItem("feather_black");
    public static final RegistryObject<Item> FEATHER_FLAMINGO = simpleItem("feather_flamingo");
    public static final RegistryObject<Item> POISON = simpleItem("poison");
    public static final RegistryObject<Item> NUKA_SHARD = simpleItem("nuka_shard");
    public static final RegistryObject<Item> OUTLANDER_FUR = simpleItem("outlander_fur");
    public static final RegistryObject<Item> ZEBRA_HIDE = simpleItem("zebra_hide");
    public static final RegistryObject<Item> GEMSBOK_HIDE = simpleItem("gemsbok_hide");
    public static final RegistryObject<Item> GEMSBOK_HORN = simpleItem("gemsbok_horn");
    public static final RegistryObject<Item> RHINO_HORN = simpleItem("rhino_horn");
    public static final RegistryObject<Item> GROUND_RHINO_HORN =
            registerItem("ground_rhino_horn", GroundRhinoHornItem::new);
    public static final RegistryObject<Item> CORN_KERNELS = simpleItem("corn_kernels");
    public static final RegistryObject<Item> DRIED_MAIZE = simpleItem("dried_maize");
    public static final RegistryObject<Item> LION_FUR = simpleItem("lion_fur");
    public static final RegistryObject<Item> BUG = simpleItem("bug");
    public static final RegistryObject<Item> CRYSTAL = simpleItem("crystal", 16);

    // ========== Food Items ==========
    public static final RegistryObject<Item> LION_RAW = meatItem("lion_raw", 3, 0.3F);
    public static final RegistryObject<Item> LION_COOKED = meatItem("lion_cooked", 8, 0.8F);
    public static final RegistryObject<Item> ZEBRA_RAW = meatItem("zebra_raw", 2, 0.1F);
    public static final RegistryObject<Item> ZEBRA_COOKED = meatItem("zebra_cooked", 6, 0.4F);
    public static final RegistryObject<Item> RHINO_RAW = meatItem("rhino_raw", 2, 0.1F);
    public static final RegistryObject<Item> RHINO_COOKED = meatItem("rhino_cooked", 7, 0.4F);
    public static final RegistryObject<Item> MANGO = foodItem("mango", 3, 0.3F);
    public static final RegistryObject<Item> BANANA = foodItem("banana", 2, 0.3F);
    public static final RegistryObject<Item> CORN = foodItem("corn", 1, 0.1F);
    public static final RegistryObject<Item> POPCORN = foodItem("popcorn", 3, 0.4F);
    public static final RegistryObject<Item> KIWANO = foodItem("kiwano", 2, 0.3F);
    public static final RegistryObject<Item> ROAST_YAM = foodItem("roast_yam", 6, 0.6F);
    public static final RegistryObject<Item> BANANA_BREAD = foodItem("banana_bread", 3, 0.5F);
    public static final RegistryObject<Item> CHOCOLATE_MUFASA = foodItem("chocolate_mufasa", 16, 0.8F);

    public static final RegistryObject<Item> OUTLANDER_MEAT = meatEffectItem("outlander_meat", 3, 0.2F, 600, 0.8F);
    public static final RegistryObject<Item> CROCODILE_MEAT = meatEffectItem("crocodile_meat", 4, 0.4F, 300, 0.3F);

    public static final RegistryObject<Item> BUG_STEW = registerItem(
            "bug_stew",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(0.5F)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 160, 0), 0.4F)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 0), 0.12F)
                            .build())
                    .stacksTo(1)
                    .craftRemainder(Items.BOWL));

    public static final RegistryObject<Item> EXPERIENCE_GRUB = registerItem(
            "experience_grub",
            ExperienceGrubItem::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(0)
                            .saturationMod(0.0F)
                            .alwaysEat()
                            .build()));

    // ========== Pridestone Tools ==========
    public static final RegistryObject<SwordItem> PRIDESTONE_SWORD = ITEMS.register(
            "pridestone_sword", () -> new SwordItem(LionKingToolTiers.PRIDESTONE, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> PRIDESTONE_PICKAXE = ITEMS.register(
            "pridestone_pickaxe", () -> new PickaxeItem(LionKingToolTiers.PRIDESTONE, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> PRIDESTONE_AXE = ITEMS.register(
            "pridestone_axe", () -> new AxeItem(LionKingToolTiers.PRIDESTONE, 6.0F, -3.1F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> PRIDESTONE_SHOVEL = ITEMS.register(
            "pridestone_shovel",
            () -> new ShovelItem(LionKingToolTiers.PRIDESTONE, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> PRIDESTONE_HOE = ITEMS.register(
            "pridestone_hoe", () -> new HoeItem(LionKingToolTiers.PRIDESTONE, -1, -2.0F, new Item.Properties()));

    // ========== Silver Tools ==========
    public static final RegistryObject<SwordItem> SILVER_SWORD = ITEMS.register(
            "silver_sword", () -> new SwordItem(LionKingToolTiers.SILVER, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> SILVER_PICKAXE = ITEMS.register(
            "silver_pickaxe", () -> new PickaxeItem(LionKingToolTiers.SILVER, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> SILVER_AXE = ITEMS.register(
            "silver_axe", () -> new AxeItem(LionKingToolTiers.SILVER, 6.0F, -3.1F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> SILVER_SHOVEL = ITEMS.register(
            "silver_shovel", () -> new ShovelItem(LionKingToolTiers.SILVER, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> SILVER_HOE =
            ITEMS.register("silver_hoe", () -> new HoeItem(LionKingToolTiers.SILVER, -2, -1.0F, new Item.Properties()));

    // ========== Peacock Tools ==========
    public static final RegistryObject<SwordItem> PEACOCK_SWORD = ITEMS.register(
            "peacock_sword", () -> new SwordItem(LionKingToolTiers.PEACOCK, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> PEACOCK_PICKAXE = ITEMS.register(
            "peacock_pickaxe", () -> new PickaxeItem(LionKingToolTiers.PEACOCK, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> PEACOCK_AXE = ITEMS.register(
            "peacock_axe", () -> new AxeItem(LionKingToolTiers.PEACOCK, 5.0F, -3.0F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> PEACOCK_SHOVEL = ITEMS.register(
            "peacock_shovel", () -> new ShovelItem(LionKingToolTiers.PEACOCK, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> PEACOCK_HOE = ITEMS.register(
            "peacock_hoe", () -> new HoeItem(LionKingToolTiers.PEACOCK, -3, 0.0F, new Item.Properties()));

    // ========== Kivulite Tools ==========
    public static final RegistryObject<SwordItem> KIVULITE_SWORD = ITEMS.register(
            "kivulite_sword", () -> new KivuliteSwordItem(LionKingToolTiers.KIVULITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> KIVULITE_PICKAXE = ITEMS.register(
            "kivulite_pickaxe",
            () -> new KivulitePickaxeItem(LionKingToolTiers.KIVULITE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> KIVULITE_AXE = ITEMS.register(
            "kivulite_axe", () -> new KivuliteAxeItem(LionKingToolTiers.KIVULITE, 6.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> KIVULITE_SHOVEL = ITEMS.register(
            "kivulite_shovel",
            () -> new KivuliteShovelItem(LionKingToolTiers.KIVULITE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> KIVULITE_HOE = ITEMS.register(
            "kivulite_hoe", () -> new HoeItem(LionKingToolTiers.KIVULITE, -2, -1.0F, new Item.Properties()));

    // ========== Corrupt Pridestone Tools ==========
    public static final RegistryObject<SwordItem> CORRUPT_SWORD = ITEMS.register(
            "corrupt_sword",
            () -> new SwordItem(LionKingToolTiers.CORRUPT_PRIDESTONE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> CORRUPT_PICKAXE = ITEMS.register(
            "corrupt_pickaxe",
            () -> new PickaxeItem(LionKingToolTiers.CORRUPT_PRIDESTONE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> CORRUPT_AXE = ITEMS.register(
            "corrupt_axe", () -> new AxeItem(LionKingToolTiers.CORRUPT_PRIDESTONE, 6.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> CORRUPT_SHOVEL = ITEMS.register(
            "corrupt_shovel",
            () -> new ShovelItem(LionKingToolTiers.CORRUPT_PRIDESTONE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> CORRUPT_HOE = ITEMS.register(
            "corrupt_hoe", () -> new HoeItem(LionKingToolTiers.CORRUPT_PRIDESTONE, -1, -2.0F, new Item.Properties()));

    // ========== Silver Armor ==========
    public static final RegistryObject<ArmorItem> SILVER_HELMET =
            armorItem("silver_helmet", LionKingArmorMaterials.SILVER, ArmorItem.Type.HELMET);
    public static final RegistryObject<ArmorItem> SILVER_CHESTPLATE =
            armorItem("silver_chestplate", LionKingArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<ArmorItem> SILVER_LEGGINGS =
            armorItem("silver_leggings", LionKingArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<ArmorItem> SILVER_BOOTS =
            armorItem("silver_boots", LionKingArmorMaterials.SILVER, ArmorItem.Type.BOOTS);

    // ========== Gemsbok Armor ==========
    public static final RegistryObject<ArmorItem> GEMSBOK_HELMET =
            armorItem("gemsbok_helmet", LionKingArmorMaterials.GEMSBOK, ArmorItem.Type.HELMET);
    public static final RegistryObject<ArmorItem> GEMSBOK_CHESTPLATE =
            armorItem("gemsbok_chestplate", LionKingArmorMaterials.GEMSBOK, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<ArmorItem> GEMSBOK_LEGGINGS =
            armorItem("gemsbok_leggings", LionKingArmorMaterials.GEMSBOK, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<ArmorItem> GEMSBOK_BOOTS =
            armorItem("gemsbok_boots", LionKingArmorMaterials.GEMSBOK, ArmorItem.Type.BOOTS);

    // ========== Peacock Armor ==========
    public static final RegistryObject<ArmorItem> PEACOCK_HELMET =
            armorItem("peacock_helmet", LionKingArmorMaterials.PEACOCK, ArmorItem.Type.HELMET);
    public static final RegistryObject<ArmorItem> PEACOCK_CHESTPLATE =
            armorItem("peacock_chestplate", LionKingArmorMaterials.PEACOCK, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<ArmorItem> PEACOCK_LEGGINGS =
            armorItem("peacock_leggings", LionKingArmorMaterials.PEACOCK, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<ArmorItem> PEACOCK_BOOTS =
            armorItem("peacock_boots", LionKingArmorMaterials.PEACOCK, ArmorItem.Type.BOOTS);

    // ========== Special Armor ==========
    public static final RegistryObject<ArmorItem> PEACOCK_WINGS =
            armorItem("peacock_wings", LionKingArmorMaterials.GEMSBOK, ArmorItem.Type.CHESTPLATE);

    // ========== Block Items ==========
    // Pridestone & Variants
    public static final RegistryObject<BlockItem> PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("pridestone", LionKingBlocks.PRIDESTONE);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("corrupt_pridestone", LionKingBlocks.CORRUPT_PRIDESTONE);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_ITEM =
            registerBlockItem("pride_brick", LionKingBlocks.PRIDE_BRICK);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("corrupt_pride_brick", LionKingBlocks.CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> CRACKED_PRIDE_BRICK_ITEM =
            registerBlockItem("cracked_pride_brick", LionKingBlocks.CRACKED_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> MOSSY_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_pride_brick", LionKingBlocks.MOSSY_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> MOSSY_CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_corrupt_pride_brick", LionKingBlocks.MOSSY_CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> PRIDE_PILLAR_ITEM =
            registerBlockItem("pride_pillar", LionKingBlocks.PRIDE_PILLAR);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_PILLAR_ITEM =
            registerBlockItem("corrupt_pride_pillar", LionKingBlocks.CORRUPT_PRIDE_PILLAR);

    // Ores & Storage
    public static final RegistryObject<BlockItem> PRIDE_COAL_ORE_ITEM =
            registerBlockItem("pride_coal_ore", LionKingBlocks.PRIDE_COAL_ORE);
    public static final RegistryObject<BlockItem> SILVER_ORE_ITEM =
            registerBlockItem("silver_ore", LionKingBlocks.SILVER_ORE);
    public static final RegistryObject<BlockItem> PEACOCK_ORE_ITEM =
            registerBlockItem("peacock_ore", LionKingBlocks.PEACOCK_ORE);
    public static final RegistryObject<BlockItem> KIVULITE_ORE_ITEM =
            registerBlockItem("kivulite_ore", LionKingBlocks.KIVULITE_ORE);
    public static final RegistryObject<BlockItem> NUKA_ORE_ITEM =
            registerBlockItem("nuka_ore", LionKingBlocks.NUKA_ORE);
    public static final RegistryObject<BlockItem> SILVER_BLOCK_ITEM =
            registerBlockItem("silver_block", LionKingBlocks.SILVER_BLOCK);
    public static final RegistryObject<BlockItem> PEACOCK_BLOCK_ITEM =
            registerBlockItem("peacock_block", LionKingBlocks.PEACOCK_BLOCK);
    public static final RegistryObject<BlockItem> KIVULITE_BLOCK_ITEM =
            registerBlockItem("kivulite_block", LionKingBlocks.KIVULITE_BLOCK);

    // Wood - Acacia
    public static final RegistryObject<BlockItem> ACACIA_LOG_ITEM =
            registerBlockItem("pride_acacia_log", LionKingBlocks.ACACIA_LOG);
    public static final RegistryObject<BlockItem> ACACIA_PLANKS_ITEM =
            registerBlockItem("pride_acacia_planks", LionKingBlocks.ACACIA_PLANKS);
    public static final RegistryObject<BlockItem> ACACIA_STAIRS_ITEM =
            registerBlockItem("pride_acacia_stairs", LionKingBlocks.ACACIA_STAIRS);
    public static final RegistryObject<BlockItem> ACACIA_SLAB_ITEM =
            registerBlockItem("pride_acacia_slab", LionKingBlocks.ACACIA_SLAB);

    // Wood - Rainforest
    public static final RegistryObject<BlockItem> RAINFOREST_LOG_ITEM =
            registerBlockItem("rainforest_log", LionKingBlocks.RAINFOREST_LOG);
    public static final RegistryObject<BlockItem> RAINFOREST_PLANKS_ITEM =
            registerBlockItem("rainforest_planks", LionKingBlocks.RAINFOREST_PLANKS);
    public static final RegistryObject<BlockItem> RAINFOREST_STAIRS_ITEM =
            registerBlockItem("rainforest_stairs", LionKingBlocks.RAINFOREST_STAIRS);
    public static final RegistryObject<BlockItem> RAINFOREST_SLAB_ITEM =
            registerBlockItem("rainforest_slab", LionKingBlocks.RAINFOREST_SLAB);

    // Wood - Mango
    public static final RegistryObject<BlockItem> MANGO_LOG_ITEM =
            registerBlockItem("mango_log", LionKingBlocks.MANGO_LOG);
    public static final RegistryObject<BlockItem> MANGO_PLANKS_ITEM =
            registerBlockItem("mango_planks", LionKingBlocks.MANGO_PLANKS);
    public static final RegistryObject<BlockItem> MANGO_STAIRS_ITEM =
            registerBlockItem("mango_stairs", LionKingBlocks.MANGO_STAIRS);
    public static final RegistryObject<BlockItem> MANGO_SLAB_ITEM =
            registerBlockItem("mango_slab", LionKingBlocks.MANGO_SLAB);

    // Wood - Passion
    public static final RegistryObject<BlockItem> PASSION_LOG_ITEM =
            registerBlockItem("passion_log", LionKingBlocks.PASSION_LOG);
    public static final RegistryObject<BlockItem> PASSION_PLANKS_ITEM =
            registerBlockItem("passion_planks", LionKingBlocks.PASSION_PLANKS);
    public static final RegistryObject<BlockItem> PASSION_STAIRS_ITEM =
            registerBlockItem("passion_stairs", LionKingBlocks.PASSION_STAIRS);
    public static final RegistryObject<BlockItem> PASSION_SLAB_ITEM =
            registerBlockItem("passion_slab", LionKingBlocks.PASSION_SLAB);

    // Wood - Banana
    public static final RegistryObject<BlockItem> BANANA_LOG_ITEM =
            registerBlockItem("banana_log", LionKingBlocks.BANANA_LOG);
    public static final RegistryObject<BlockItem> BANANA_PLANKS_ITEM =
            registerBlockItem("banana_planks", LionKingBlocks.BANANA_PLANKS);
    public static final RegistryObject<BlockItem> BANANA_STAIRS_ITEM =
            registerBlockItem("banana_stairs", LionKingBlocks.BANANA_STAIRS);
    public static final RegistryObject<BlockItem> BANANA_SLAB_ITEM =
            registerBlockItem("banana_slab", LionKingBlocks.BANANA_SLAB);

    // Wood - Deadwood
    public static final RegistryObject<BlockItem> DEADWOOD_LOG_ITEM =
            registerBlockItem("deadwood_log", LionKingBlocks.DEADWOOD_LOG);
    public static final RegistryObject<BlockItem> DEADWOOD_PLANKS_ITEM =
            registerBlockItem("deadwood_planks", LionKingBlocks.DEADWOOD_PLANKS);
    public static final RegistryObject<BlockItem> DEADWOOD_STAIRS_ITEM =
            registerBlockItem("deadwood_stairs", LionKingBlocks.DEADWOOD_STAIRS);
    public static final RegistryObject<BlockItem> DEADWOOD_SLAB_ITEM =
            registerBlockItem("deadwood_slab", LionKingBlocks.DEADWOOD_SLAB);

    // Stone stairs/slabs
    public static final RegistryObject<BlockItem> PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("pridestone_stairs", LionKingBlocks.PRIDESTONE_STAIRS);
    public static final RegistryObject<BlockItem> PRIDESTONE_SLAB_ITEM =
            registerBlockItem("pridestone_slab", LionKingBlocks.PRIDESTONE_SLAB);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("pride_brick_stairs", LionKingBlocks.PRIDE_BRICK_STAIRS);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("pride_brick_slab", LionKingBlocks.PRIDE_BRICK_SLAB);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("corrupt_pridestone_stairs", LionKingBlocks.CORRUPT_PRIDESTONE_STAIRS);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_SLAB_ITEM =
            registerBlockItem("corrupt_pridestone_slab", LionKingBlocks.CORRUPT_PRIDESTONE_SLAB);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("corrupt_pride_brick_stairs", LionKingBlocks.CORRUPT_PRIDE_BRICK_STAIRS);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("corrupt_pride_brick_slab", LionKingBlocks.CORRUPT_PRIDE_BRICK_SLAB);
    public static final RegistryObject<BlockItem> PRIDE_PILLAR_SLAB_ITEM =
            registerBlockItem("pride_pillar_slab", LionKingBlocks.PRIDE_PILLAR_SLAB);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_PILLAR_SLAB_ITEM =
            registerBlockItem("corrupt_pride_pillar_slab", LionKingBlocks.CORRUPT_PRIDE_PILLAR_SLAB);

    // Walls
    public static final RegistryObject<BlockItem> PRIDESTONE_WALL_ITEM =
            registerBlockItem("pridestone_wall", LionKingBlocks.PRIDESTONE_WALL);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_WALL_ITEM =
            registerBlockItem("pride_brick_wall", LionKingBlocks.PRIDE_BRICK_WALL);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_WALL_ITEM =
            registerBlockItem("corrupt_pridestone_wall", LionKingBlocks.CORRUPT_PRIDESTONE_WALL);

    // Redstone
    public static final RegistryObject<BlockItem> PRIDESTONE_PRESSURE_PLATE_ITEM =
            registerBlockItem("pridestone_pressure_plate", LionKingBlocks.PRIDESTONE_PRESSURE_PLATE);
    public static final RegistryObject<BlockItem> PRIDESTONE_BUTTON_ITEM =
            registerBlockItem("pridestone_button", LionKingBlocks.PRIDESTONE_BUTTON);

    // Misc blocks
    public static final RegistryObject<BlockItem> DRIED_MAIZE_BLOCK_ITEM =
            registerBlockItem("dried_maize_block", LionKingBlocks.DRIED_MAIZE_BLOCK);
    public static final RegistryObject<BlockItem> DRIED_MAIZE_STAIRS_ITEM =
            registerBlockItem("dried_maize_stairs", LionKingBlocks.DRIED_MAIZE_STAIRS);
    public static final RegistryObject<BlockItem> DRIED_MAIZE_SLAB_ITEM =
            registerBlockItem("dried_maize_slab", LionKingBlocks.DRIED_MAIZE_SLAB);
    public static final RegistryObject<BlockItem> OUTSAND_ITEM = registerBlockItem("outsand", LionKingBlocks.OUTSAND);
    public static final RegistryObject<BlockItem> OUTGLASS_ITEM =
            registerBlockItem("outglass", LionKingBlocks.OUTGLASS);
    public static final RegistryObject<BlockItem> OUTGLASS_PANE_ITEM =
            registerBlockItem("outglass_pane", LionKingBlocks.OUTGLASS_PANE);
    public static final RegistryObject<BlockItem> TERMITE_MOUND_ITEM =
            registerBlockItem("termite_mound", LionKingBlocks.TERMITE_MOUND);
    public static final RegistryObject<BlockItem> ZIRA_MOUND_GATE_ITEM =
            registerBlockItem("zira_mound_gate", LionKingBlocks.ZIRA_MOUND_GATE);
    public static final RegistryObject<BlockItem> PUMBAA_BOX_ITEM =
            registerBlockItem("pumbaa_box", LionKingBlocks.PUMBAA_BOX);

    // Leaves
    public static final RegistryObject<BlockItem> ACACIA_LEAVES_ITEM =
            registerBlockItem("pride_acacia_leaves", LionKingBlocks.ACACIA_LEAVES);
    public static final RegistryObject<BlockItem> RAINFOREST_LEAVES_ITEM =
            registerBlockItem("rainforest_leaves", LionKingBlocks.RAINFOREST_LEAVES);
    public static final RegistryObject<BlockItem> MANGO_LEAVES_ITEM =
            registerBlockItem("mango_leaves", LionKingBlocks.MANGO_LEAVES);
    public static final RegistryObject<BlockItem> PASSION_LEAVES_ITEM =
            registerBlockItem("passion_leaves", LionKingBlocks.PASSION_LEAVES);
    public static final RegistryObject<BlockItem> BANANA_LEAVES_ITEM =
            registerBlockItem("banana_leaves", LionKingBlocks.BANANA_LEAVES);
    public static final RegistryObject<BlockItem> RAFIKI_LEAVES_ITEM =
            registerBlockItem("rafiki_leaves", LionKingBlocks.RAFIKI_LEAVES);

    // Saplings
    public static final RegistryObject<BlockItem> ACACIA_SAPLING_ITEM =
            registerBlockItem("pride_acacia_sapling", LionKingBlocks.ACACIA_SAPLING);
    public static final RegistryObject<BlockItem> RAINFOREST_SAPLING_ITEM =
            registerBlockItem("rainforest_sapling", LionKingBlocks.RAINFOREST_SAPLING);
    public static final RegistryObject<BlockItem> MANGO_SAPLING_ITEM =
            registerBlockItem("mango_sapling", LionKingBlocks.MANGO_SAPLING);
    public static final RegistryObject<BlockItem> PASSION_SAPLING_ITEM =
            registerBlockItem("passion_sapling", LionKingBlocks.PASSION_SAPLING);
    public static final RegistryObject<BlockItem> BANANA_SAPLING_ITEM =
            registerBlockItem("banana_sapling", LionKingBlocks.BANANA_SAPLING);

    // Rafiki Wood
    public static final RegistryObject<BlockItem> RAFIKI_WOOD_ITEM =
            registerBlockItem("rafiki_wood", LionKingBlocks.RAFIKI_WOOD);

    // Flowers
    public static final RegistryObject<BlockItem> WHITE_FLOWER_ITEM =
            registerBlockItem("white_flower", LionKingBlocks.WHITE_FLOWER);
    public static final RegistryObject<BlockItem> BLUE_FLOWER_ITEM =
            registerBlockItem("blue_flower", LionKingBlocks.BLUE_FLOWER);
    public static final RegistryObject<BlockItem> PURPLE_FLOWER_ITEM =
            registerBlockItem("purple_flower", LionKingBlocks.PURPLE_FLOWER);
    public static final RegistryObject<BlockItem> RED_FLOWER_ITEM =
            registerBlockItem("red_flower", LionKingBlocks.RED_FLOWER);

    // Waterlilies
    public static final RegistryObject<BlockItem> LILY_RED_ITEM =
            ITEMS.register("lily_red", () -> new LilyPadItem(LionKingBlocks.LILY_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LILY_VIOLET_ITEM = ITEMS.register(
            "lily_violet", () -> new LilyPadItem(LionKingBlocks.LILY_VIOLET.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LILY_WHITE_ITEM =
            ITEMS.register("lily_white", () -> new LilyPadItem(LionKingBlocks.LILY_WHITE.get(), new Item.Properties()));

    // Mushrooms
    public static final RegistryObject<BlockItem> OUTSHROOM_ITEM =
            registerBlockItem("outshroom", LionKingBlocks.OUTSHROOM);
    public static final RegistryObject<BlockItem> OUTSHROOM_GLOWING_ITEM =
            registerBlockItem("outshroom_glowing", LionKingBlocks.OUTSHROOM_GLOWING);

    // Misc nature
    public static final RegistryObject<BlockItem> ARID_GRASS_ITEM =
            registerBlockItem("arid_grass", LionKingBlocks.ARID_GRASS);
    public static final RegistryObject<Item> HYENA_TORCH_ITEM = ITEMS.register(
            "hyena_torch",
            () -> new StandingAndWallBlockItem(
                    LionKingBlocks.HYENA_TORCH.get(),
                    LionKingBlocks.HYENA_WALL_TORCH.get(),
                    new Item.Properties(),
                    Direction.DOWN));
    public static final RegistryObject<BlockItem> HANGING_BANANA_ITEM =
            registerBlockItem("hanging_banana", LionKingBlocks.HANGING_BANANA);
    public static final RegistryObject<BlockItem> KIWANO_BLOCK_ITEM =
            registerBlockItem("kiwano_block", LionKingBlocks.KIWANO_BLOCK);

    // Crop seeds
    public static final RegistryObject<Item> KIWANO_SEEDS = ITEMS.register(
            "kiwano_seeds", () -> new ItemNameBlockItem(LionKingBlocks.KIWANO_STEM.get(), new Item.Properties()));

    public static final RegistryObject<Item> YAM =
            ITEMS.register("yam", () -> new ItemNameBlockItem(LionKingBlocks.YAM_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> MAIZE_STALKS = ITEMS.register(
            "maize_stalks", () -> new ItemNameBlockItem(LionKingBlocks.MAIZE_CROP.get(), new Item.Properties()));

    // Decorative block entities
    public static final RegistryObject<BlockItem> HYENA_HEAD_ITEM = ITEMS.register(
            "hyena_head", () -> new HyenaHeadBlockItem(LionKingBlocks.HYENA_HEAD.get(), new Item.Properties()));
    // Dyeable rugs (carpet-style)
    public static final RegistryObject<BlockItem> RUG_LION_ITEM =
            registerBlockItem("rug_lion", LionKingBlocks.RUG_LION);
    public static final RegistryObject<BlockItem> RUG_BLACK_ITEM =
            registerBlockItem("rug_black", LionKingBlocks.RUG_BLACK);
    public static final RegistryObject<BlockItem> RUG_BLUE_ITEM =
            registerBlockItem("rug_blue", LionKingBlocks.RUG_BLUE);
    public static final RegistryObject<BlockItem> RUG_GREEN_ITEM =
            registerBlockItem("rug_green", LionKingBlocks.RUG_GREEN);
    public static final RegistryObject<BlockItem> RUG_GRAY_ITEM =
            registerBlockItem("rug_gray", LionKingBlocks.RUG_GRAY);
    public static final RegistryObject<BlockItem> RUG_LIGHT_BLUE_ITEM =
            registerBlockItem("rug_light_blue", LionKingBlocks.RUG_LIGHT_BLUE);
    public static final RegistryObject<BlockItem> RUG_LIGHT_GREEN_ITEM =
            registerBlockItem("rug_light_green", LionKingBlocks.RUG_LIGHT_GREEN);
    public static final RegistryObject<BlockItem> RUG_LIGHT_GRAY_ITEM =
            registerBlockItem("rug_light_gray", LionKingBlocks.RUG_LIGHT_GRAY);
    public static final RegistryObject<BlockItem> RUG_ORANGE_ITEM =
            registerBlockItem("rug_orange", LionKingBlocks.RUG_ORANGE);
    public static final RegistryObject<BlockItem> RUG_OUTLANDER_ITEM =
            registerBlockItem("rug_outlander", LionKingBlocks.RUG_OUTLANDER);
    public static final RegistryObject<BlockItem> RUG_PINK_ITEM =
            registerBlockItem("rug_pink", LionKingBlocks.RUG_PINK);
    public static final RegistryObject<BlockItem> RUG_PURPLE_ITEM =
            registerBlockItem("rug_purple", LionKingBlocks.RUG_PURPLE);
    public static final RegistryObject<BlockItem> RUG_RED_ITEM = registerBlockItem("rug_red", LionKingBlocks.RUG_RED);
    public static final RegistryObject<BlockItem> RUG_VIOLET_ITEM =
            registerBlockItem("rug_violet", LionKingBlocks.RUG_VIOLET);
    public static final RegistryObject<BlockItem> RUG_WHITE_ITEM =
            registerBlockItem("rug_white", LionKingBlocks.RUG_WHITE);
    public static final RegistryObject<BlockItem> RUG_YELLOW_ITEM =
            registerBlockItem("rug_yellow", LionKingBlocks.RUG_YELLOW);

    // ========== Outlands Armor ==========
    public static final RegistryObject<ArmorItem> OUTLANDS_HELMET =
            armorItem("outlands_helmet", LionKingArmorMaterials.OUTLANDS, ArmorItem.Type.HELMET);

    // ========== Ticket Lion Suit ==========
    public static final RegistryObject<ArmorItem> TICKET_LION_HEAD =
            armorItem("ticket_lion_head", LionKingArmorMaterials.TICKET_LION, ArmorItem.Type.HELMET);
    public static final RegistryObject<ArmorItem> TICKET_LION_SUIT =
            armorItem("ticket_lion_suit", LionKingArmorMaterials.TICKET_LION, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<ArmorItem> TICKET_LION_LEGS =
            armorItem("ticket_lion_legs", LionKingArmorMaterials.TICKET_LION, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<ArmorItem> TICKET_LION_FEET =
            armorItem("ticket_lion_feet", LionKingArmorMaterials.TICKET_LION, ArmorItem.Type.BOOTS);

    // ========== Darts ==========
    public static final RegistryObject<Item> DART_BLUE = simpleItem("dart_blue");
    public static final RegistryObject<Item> DART_RED = simpleItem("dart_red");
    public static final RegistryObject<Item> DART_YELLOW = simpleItem("dart_yellow");
    public static final RegistryObject<Item> DART_PINK = simpleItem("dart_pink");
    public static final RegistryObject<Item> DART_BLACK = simpleItem("dart_black");
    public static final RegistryObject<Item> DART_OUTLANDISH = simpleItem("dart_outlandish");

    // ========== Dart Shooters ==========
    public static final RegistryObject<Item> DART_SHOOTER =
            ITEMS.register("dart_shooter", () -> new DartShooterItem(false));
    public static final RegistryObject<Item> DART_SHOOTER_SILVER =
            ITEMS.register("dart_shooter_silver", () -> new DartShooterItem(true));

    // ========== Spears ==========
    public static final RegistryObject<Item> GEMSBOK_SPEAR =
            ITEMS.register("gemsbok_spear", () -> new SpearItem(false));
    public static final RegistryObject<Item> POISONED_SPEAR =
            ITEMS.register("poisoned_spear", () -> new SpearItem(true));

    // ========== Bombs ==========
    public static final RegistryObject<Item> PUMBAA_BOMB = ITEMS.register("pumbaa_bomb", PumbaaBombItem::new);

    // ========== Spawn Eggs - Passive ==========
    public static final RegistryObject<Item> LION_SPAWN_EGG = spawnEgg(EntityTypes.LION, 0xD4A030, 0x8B6914);
    public static final RegistryObject<Item> ZEBRA_SPAWN_EGG = spawnEgg(EntityTypes.ZEBRA, 0xFFFFFF, 0x222222);
    public static final RegistryObject<Item> GIRAFFE_SPAWN_EGG = spawnEgg(EntityTypes.GIRAFFE, 0xE8B84B, 0x8B5E3C);
    public static final RegistryObject<Item> RHINO_SPAWN_EGG = spawnEgg(EntityTypes.RHINO, 0x808080, 0x505050);
    public static final RegistryObject<Item> GEMSBOK_SPAWN_EGG = spawnEgg(EntityTypes.GEMSBOK, 0xC8A878, 0x4A3B2A);
    public static final RegistryObject<Item> DIKDIK_SPAWN_EGG = spawnEgg(EntityTypes.DIKDIK, 0xB8956A, 0x8B7355);
    public static final RegistryObject<Item> FLAMINGO_SPAWN_EGG = spawnEgg(EntityTypes.FLAMINGO, 0xFF69B4, 0xFF1493);
    public static final RegistryObject<Item> ZAZU_SPAWN_EGG = spawnEgg(EntityTypes.ZAZU, 0x4169E1, 0xFFD700);
    public static final RegistryObject<Item> BUG_SPAWN_EGG = spawnEgg(EntityTypes.BUG, 0x4B3621, 0x2E1F0F);

    // ========== Spawn Eggs - Hostile ==========
    public static final RegistryObject<Item> HYENA_SPAWN_EGG = spawnEgg(EntityTypes.HYENA, 0x8B7355, 0x4A3B2A);
    public static final RegistryObject<Item> SKELETAL_HYENA_SPAWN_EGG =
            spawnEgg(EntityTypes.SKELETAL_HYENA, 0xC8C8C8, 0x505050);
    public static final RegistryObject<Item> OUTLANDER_SPAWN_EGG = spawnEgg(EntityTypes.OUTLANDER, 0x5C3A1E, 0x3A2510);
    public static final RegistryObject<Item> VULTURE_SPAWN_EGG = spawnEgg(EntityTypes.VULTURE, 0x2A1F14, 0x8B0000);
    public static final RegistryObject<Item> CROCODILE_SPAWN_EGG = spawnEgg(EntityTypes.CROCODILE, 0x3B5323, 0x1A2E0A);
    public static final RegistryObject<Item> TERMITE_SPAWN_EGG = spawnEgg(EntityTypes.TERMITE, 0xD2B48C, 0x8B6914);
    public static final RegistryObject<Item> TERMITE_QUEEN_SPAWN_EGG =
            spawnEgg(EntityTypes.TERMITE_QUEEN, 0xD2B48C, 0xFF4500);

    // ========== Spawn Eggs - Special ==========
    public static final RegistryObject<Item> TICKET_LION_SPAWN_EGG =
            spawnEgg(EntityTypes.TICKET_LION, 0xD4A030, 0x4169E1);

    // ========== Spawn Eggs - NPCs ==========
    public static final RegistryObject<Item> RAFIKI_SPAWN_EGG = spawnEgg(EntityTypes.RAFIKI, 0x8B4513, 0xFFD700);
    public static final RegistryObject<Item> SIMBA_SPAWN_EGG = spawnEgg(EntityTypes.SIMBA, 0xD4A030, 0xFFD700);
    public static final RegistryObject<Item> TIMON_SPAWN_EGG = spawnEgg(EntityTypes.TIMON, 0xB8860B, 0xFFE4B5);
    public static final RegistryObject<Item> PUMBAA_SPAWN_EGG = spawnEgg(EntityTypes.PUMBAA, 0x8B4513, 0x654321);
    public static final RegistryObject<Item> SCAR_SPAWN_EGG = spawnEgg(EntityTypes.SCAR, 0x2F1A00, 0x000000);
    public static final RegistryObject<Item> ZIRA_SPAWN_EGG = spawnEgg(EntityTypes.ZIRA, 0x5C3A1E, 0x8B0000);

    // ========== Jar Items ==========
    public static final RegistryObject<Item> JAR_EMPTY = registerItem("jar_empty", JarItem::new, itemProps(16));
    // craftRemainder(JAR_EMPTY.get()) must be inside the lambda — .get() fails during static init
    public static final RegistryObject<Item> JAR_WATER = ITEMS.register(
            "jar_water", () -> new FilledJarItem(Fluids.WATER, itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final RegistryObject<Item> JAR_MILK =
            ITEMS.register("jar_milk", () -> new ZebraMilkItem(itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final RegistryObject<Item> JAR_LAVA = ITEMS.register(
            "jar_lava", () -> new FilledJarItem(Fluids.LAVA, itemProps(16).craftRemainder(JAR_EMPTY.get())));
    public static final RegistryObject<Item> MANGO_JUICE = ITEMS.register(
            "mango_juice",
            () -> new MangoJuiceItem(itemProps(16).food(foodProps(6, 0.5F)).craftRemainder(JAR_EMPTY.get())));

    public static final RegistryObject<Item> HYENA_MEAL = registerItem("hyena_meal", HyenaMealItem::new);

    // ========== Giraffe Ties ==========
    public static final RegistryObject<Item> GIRAFFE_TIE = simpleItem("giraffe_tie");
    public static final RegistryObject<Item> GIRAFFE_TIE_WHITE = simpleItem("giraffe_tie_white");
    public static final RegistryObject<Item> GIRAFFE_TIE_BLUE = simpleItem("giraffe_tie_blue");
    public static final RegistryObject<Item> GIRAFFE_TIE_YELLOW = simpleItem("giraffe_tie_yellow");
    public static final RegistryObject<Item> GIRAFFE_TIE_RED = simpleItem("giraffe_tie_red");
    public static final RegistryObject<Item> GIRAFFE_TIE_PURPLE = simpleItem("giraffe_tie_purple");
    public static final RegistryObject<Item> GIRAFFE_TIE_GREEN = simpleItem("giraffe_tie_green");
    public static final RegistryObject<Item> GIRAFFE_TIE_BLACK = simpleItem("giraffe_tie_black");

    // ========== Tunnah Diggah ==========
    public static final RegistryObject<Item> TUNNAH_DIGGAH = ITEMS.register(
            "tunnah_diggah", () -> new TunnahDiggahItem(Tiers.IRON, 1, -2.8F, new Item.Properties().durability(690)));

    // ========== Quest & Special Items ==========
    public static final RegistryObject<Item> AMULET = ITEMS.register(
            "amulet",
            () -> new ArmorItem(
                    LionKingArmorMaterials.GEMSBOK,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> SIMBA_CHARM = registerItem("simba_charm", SimbaCharmItem::new);
    public static final RegistryObject<Item> GIRAFFE_SADDLE = simpleItem("giraffe_saddle", 1);
    public static final RegistryObject<Item> DART_QUIVER = registerItem("dart_quiver", QuiverItem::new, itemProps(1));
    public static final RegistryObject<Item> PASSION_FRUIT = foodItem("passion_fruit", 3, 0.3F);
    public static final RegistryObject<Item> ZAZU_EGG = simpleItem("zazu_egg");

    // ========== Block Entity Items ==========
    public static final RegistryObject<BlockItem> GRINDING_BOWL_ITEM =
            registerBlockItem("grinding_bowl", LionKingBlocks.GRINDING_BOWL);
    public static final RegistryObject<BlockItem> BUG_TRAP_ITEM =
            registerBlockItem("bug_trap", LionKingBlocks.BUG_TRAP);
    public static final RegistryObject<BlockItem> BONGO_DRUM_ITEM =
            registerBlockItem("bongo_drum", LionKingBlocks.BONGO_DRUM);
    public static final RegistryObject<BlockItem> OUTLANDS_POOL_ITEM =
            registerBlockItem("outlands_pool", LionKingBlocks.OUTLANDS_POOL);
    public static final RegistryObject<BlockItem> LK_SPAWNER_ITEM =
            registerBlockItem("lk_spawner", LionKingBlocks.LK_SPAWNER);

    // ========== Quest / NPC Items ==========
    public static final RegistryObject<Item> QUEST_BOOK = registerItem("quest_book", QuestBookItem::new);

    public static final RegistryObject<Item> TICKET = registerItem("ticket", TicketItem::new);

    public static final RegistryObject<Item> RHYTHM_STAFF = simpleItem("rhythm_staff", 1);

    public static final RegistryObject<Item> RAFIKI_COIN = simpleItem("rafiki_coin");
    public static final RegistryObject<Item> ZIRA_COIN = simpleItem("zira_coin");
    public static final RegistryObject<Item> WAYWARD_FEATHER = registerItem("wayward_feather", WaywardFeatherItem::new);

    public static final RegistryObject<Item> RAFIKI_STICK = registerItem("rafiki_stick", RafikiStickItem::new);

    public static final RegistryObject<Item> RAFIKI_DUST = registerItem("rafiki_dust", RafikiDustItem::new);
    public static final RegistryObject<Item> PRIDE_COMPASS = registerItem("pride_compass", PrideCompassItem::new);

    // ========== Phase 12: Missing Block Items ==========
    public static final RegistryObject<BlockItem> BANANA_CAKE_ITEM =
            registerBlockItem("banana_cake", LionKingBlocks.BANANA_CAKE);
    public static final RegistryObject<BlockItem> MOUNTED_SHOOTER_ITEM =
            registerBlockItem("mounted_shooter", LionKingBlocks.MOUNTED_SHOOTER);
    public static final RegistryObject<BlockItem> STAR_ALTAR_ITEM =
            registerBlockItem("star_altar", LionKingBlocks.STAR_ALTAR);
    public static final RegistryObject<BlockItem> OUTLANDS_ALTAR_ITEM =
            registerBlockItem("outlands_altar", LionKingBlocks.OUTLANDS_ALTAR);
    public static final RegistryObject<BlockItem> TILLED_SAND_ITEM =
            registerBlockItem("tilled_sand", LionKingBlocks.TILLED_SAND);
    public static final RegistryObject<BlockItem> VASE_ITEM = registerBlockItem("vase", LionKingBlocks.VASE);
    public static final RegistryObject<BlockItem> VASE_ACACIA_ITEM =
            registerBlockItem("vase_acacia", LionKingBlocks.VASE_ACACIA);
    public static final RegistryObject<BlockItem> VASE_RAINFOREST_ITEM =
            registerBlockItem("vase_rainforest", LionKingBlocks.VASE_RAINFOREST);
    public static final RegistryObject<BlockItem> VASE_MANGO_ITEM =
            registerBlockItem("vase_mango", LionKingBlocks.VASE_MANGO);
    public static final RegistryObject<BlockItem> VASE_PASSION_ITEM =
            registerBlockItem("vase_passion", LionKingBlocks.VASE_PASSION);
    public static final RegistryObject<BlockItem> VASE_BANANA_ITEM =
            registerBlockItem("vase_banana", LionKingBlocks.VASE_BANANA);
    public static final RegistryObject<BlockItem> VASE_WHITE_FLOWER_ITEM =
            registerBlockItem("vase_white_flower", LionKingBlocks.VASE_WHITE_FLOWER);
    public static final RegistryObject<BlockItem> VASE_BLUE_FLOWER_ITEM =
            registerBlockItem("vase_blue_flower", LionKingBlocks.VASE_BLUE_FLOWER);
    public static final RegistryObject<BlockItem> VASE_RED_FLOWER_ITEM =
            registerBlockItem("vase_red_flower", LionKingBlocks.VASE_RED_FLOWER);
    public static final RegistryObject<BlockItem> VASE_PURPLE_FLOWER_ITEM =
            registerBlockItem("vase_purple_flower", LionKingBlocks.VASE_PURPLE_FLOWER);
    public static final RegistryObject<BlockItem> VASE_OUTSHROOM_ITEM =
            registerBlockItem("vase_outshroom", LionKingBlocks.VASE_OUTSHROOM);
    public static final RegistryObject<BlockItem> VASE_OUTSHROOM_GLOWING_ITEM =
            registerBlockItem("vase_outshroom_glowing", LionKingBlocks.VASE_OUTSHROOM_GLOWING);

    // ========== Bed & Lever ==========
    public static final RegistryObject<BlockItem> PRIDE_BED_ITEM =
            ITEMS.register("pride_bed", () -> new BedItem(LionKingBlocks.PRIDE_BED.get(), itemProps(1)));
    public static final RegistryObject<BlockItem> PRIDE_LEVER_ITEM =
            registerBlockItem("pride_lever", LionKingBlocks.PRIDE_LEVER);

    // ========== Portal Frame Items ==========
    public static final RegistryObject<BlockItem> PRIDE_PORTAL_FRAME_ITEM =
            registerBlockItem("pride_portal_frame", LionKingBlocks.PRIDE_PORTAL_FRAME);
    public static final RegistryObject<BlockItem> OUTLANDS_PORTAL_FRAME_ITEM =
            registerBlockItem("outlands_portal_frame", LionKingBlocks.OUTLANDS_PORTAL_FRAME);

    // ========== Scar / Zira Rugs ==========
    public static final RegistryObject<Item> SCAR_RUG =
            ITEMS.register("scar_rug", () -> new RugItem(RugEntity.TYPE_SCAR, itemProps(1)));
    public static final RegistryObject<Item> ZIRA_RUG =
            ITEMS.register("zira_rug", () -> new RugItem(RugEntity.TYPE_ZIRA, itemProps(1)));

    // ========== Skeletal Hyena Head Spawn Egg ==========
    public static final RegistryObject<Item> SKELETAL_HYENA_HEAD_SPAWN_EGG =
            spawnEgg(EntityTypes.SKELETAL_HYENA_HEAD, 0xC8C8C8, 0x3A3A3A);

    // ========== Notes (for Bongo Drum) ==========
    public static final RegistryObject<Item> NOTE_C =
            ITEMS.register("note_c", () -> new NoteItem(1, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_D =
            ITEMS.register("note_d", () -> new NoteItem(1, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_E =
            ITEMS.register("note_e", () -> new NoteItem(2, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_F =
            ITEMS.register("note_f", () -> new NoteItem(5, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_G =
            ITEMS.register("note_g", () -> new NoteItem(5, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_A =
            ITEMS.register("note_a", () -> new NoteItem(10, new Item.Properties()));
    public static final RegistryObject<Item> NOTE_B =
            ITEMS.register("note_b", () -> new NoteItem(20, new Item.Properties()));
}
