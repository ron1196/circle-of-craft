package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.item.*;
import io.github.ron1196.thelionking.item.tier.LKArmorMaterials;
import io.github.ron1196.thelionking.item.tier.LKToolTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TheLionKingMod.MOD_ID);

    // ========== Block Items ==========
    // These are registered automatically with blocks via registerBlockItem helper

    // ========== Material Items ==========
    public static final RegistryObject<Item> PRIDESTONE_ITEM =
            ITEMS.register("pridestone_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CORRUPT_PRIDESTONE_ITEM =
            ITEMS.register("corrupt_pridestone_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SILVER_INGOT =
            ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEACOCK_GEM =
            ITEMS.register("peacock_gem", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KIVULITE =
            ITEMS.register("kivulite", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HYENA_BONE =
            ITEMS.register("hyena_bone", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HYENA_BONE_SHARD =
            ITEMS.register("hyena_bone_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TERMITE_DUST =
            ITEMS.register("termite_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MANGO_DUST =
            ITEMS.register("mango_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_BLUE =
            ITEMS.register("feather_blue", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_YELLOW =
            ITEMS.register("feather_yellow", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_RED =
            ITEMS.register("feather_red", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_BLACK =
            ITEMS.register("feather_black", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_PINK =
            ITEMS.register("feather_pink", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POISON = ITEMS.register("poison", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NUKA_SHARD =
            ITEMS.register("nuka_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> OUTLANDER_FUR =
            ITEMS.register("outlander_fur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ZEBRA_HIDE =
            ITEMS.register("zebra_hide", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GEMSBOK_HIDE =
            ITEMS.register("gemsbok_hide", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GEMSBOK_HORN =
            ITEMS.register("gemsbok_horn", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RHINO_HORN =
            ITEMS.register("rhino_horn", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GROUND_RHINO_HORN =
            ITEMS.register("ground_rhino_horn", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CORN_KERNELS =
            ITEMS.register("corn_kernels", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRIED_MAIZE =
            ITEMS.register("dried_maize", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LION_FUR =
            ITEMS.register("lion_fur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BUG = ITEMS.register("bug", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRYSTAL =
            ITEMS.register("crystal", () -> new Item(new Item.Properties().stacksTo(16)));

    // ========== Food Items ==========
    public static final RegistryObject<Item> LION_RAW = ITEMS.register(
            "lion_raw",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.3F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> LION_COOKED = ITEMS.register(
            "lion_cooked",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(0.8F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> ZEBRA_RAW = ITEMS.register(
            "zebra_raw",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.1F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> ZEBRA_COOKED = ITEMS.register(
            "zebra_cooked",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.4F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> RHINO_RAW = ITEMS.register(
            "rhino_raw",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.1F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> RHINO_COOKED = ITEMS.register(
            "rhino_cooked",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(7)
                            .saturationMod(0.4F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> MANGO = ITEMS.register(
            "mango",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.3F)
                            .build())));

    public static final RegistryObject<Item> BANANA = ITEMS.register(
            "banana",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.3F)
                            .build())));

    public static final RegistryObject<Item> CORN = ITEMS.register(
            "corn",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(0.1F)
                            .build())));

    public static final RegistryObject<Item> POPCORN = ITEMS.register(
            "popcorn",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.4F)
                            .build())));

    public static final RegistryObject<Item> KIWANO = ITEMS.register(
            "kiwano",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.3F)
                            .build())));

    public static final RegistryObject<Item> OUTLANDER_MEAT = ITEMS.register(
            "outlander_meat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.2F)
                            .meat()
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8F)
                            .build())));

    public static final RegistryObject<Item> CROCODILE_MEAT = ITEMS.register(
            "crocodile_meat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.4F)
                            .meat()
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.3F)
                            .build())));

    public static final RegistryObject<Item> ROAST_YAM = ITEMS.register(
            "roast_yam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.6F)
                            .build())));

    public static final RegistryObject<Item> BANANA_BREAD = ITEMS.register(
            "banana_bread",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.5F)
                            .build())));

    public static final RegistryObject<Item> CHOCOLATE_MUFASA = ITEMS.register(
            "chocolate_mufasa",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(16)
                            .saturationMod(0.8F)
                            .build())));

    public static final RegistryObject<Item> BUG_STEW = ITEMS.register(
            "bug_stew",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(0.5F)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 160, 0), 0.4F)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 0), 0.12F)
                            .build())
                    .stacksTo(1)
                    .craftRemainder(net.minecraft.world.item.Items.BOWL)));

    public static final RegistryObject<Item> EXPERIENCE_GRUB = ITEMS.register(
            "experience_grub",
            () -> new io.github.ron1196.thelionking.item.ExperienceGrubItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(0)
                            .saturationMod(0.0F)
                            .alwaysEat()
                            .build())));

    // ========== Pridestone Tools ==========
    public static final RegistryObject<SwordItem> PRIDESTONE_SWORD = ITEMS.register(
            "pridestone_sword", () -> new SwordItem(LKToolTiers.PRIDESTONE, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> PRIDESTONE_PICKAXE = ITEMS.register(
            "pridestone_pickaxe", () -> new PickaxeItem(LKToolTiers.PRIDESTONE, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> PRIDESTONE_AXE = ITEMS.register(
            "pridestone_axe", () -> new AxeItem(LKToolTiers.PRIDESTONE, 6.0F, -3.1F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> PRIDESTONE_SHOVEL = ITEMS.register(
            "pridestone_shovel", () -> new ShovelItem(LKToolTiers.PRIDESTONE, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> PRIDESTONE_HOE = ITEMS.register(
            "pridestone_hoe", () -> new HoeItem(LKToolTiers.PRIDESTONE, -1, -2.0F, new Item.Properties()));

    // ========== Silver Tools ==========
    public static final RegistryObject<SwordItem> SILVER_SWORD =
            ITEMS.register("silver_sword", () -> new SwordItem(LKToolTiers.SILVER, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> SILVER_PICKAXE = ITEMS.register(
            "silver_pickaxe", () -> new PickaxeItem(LKToolTiers.SILVER, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> SILVER_AXE =
            ITEMS.register("silver_axe", () -> new AxeItem(LKToolTiers.SILVER, 6.0F, -3.1F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> SILVER_SHOVEL = ITEMS.register(
            "silver_shovel", () -> new ShovelItem(LKToolTiers.SILVER, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> SILVER_HOE =
            ITEMS.register("silver_hoe", () -> new HoeItem(LKToolTiers.SILVER, -2, -1.0F, new Item.Properties()));

    // ========== Peacock Tools ==========
    public static final RegistryObject<SwordItem> PEACOCK_SWORD =
            ITEMS.register("peacock_sword", () -> new SwordItem(LKToolTiers.PEACOCK, 3, -2.4F, new Item.Properties()));

    public static final RegistryObject<PickaxeItem> PEACOCK_PICKAXE = ITEMS.register(
            "peacock_pickaxe", () -> new PickaxeItem(LKToolTiers.PEACOCK, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<AxeItem> PEACOCK_AXE =
            ITEMS.register("peacock_axe", () -> new AxeItem(LKToolTiers.PEACOCK, 5.0F, -3.0F, new Item.Properties()));

    public static final RegistryObject<ShovelItem> PEACOCK_SHOVEL = ITEMS.register(
            "peacock_shovel", () -> new ShovelItem(LKToolTiers.PEACOCK, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<HoeItem> PEACOCK_HOE =
            ITEMS.register("peacock_hoe", () -> new HoeItem(LKToolTiers.PEACOCK, -3, 0.0F, new Item.Properties()));

    // ========== Kivulite Tools ==========
    public static final RegistryObject<SwordItem> KIVULITE_SWORD = ITEMS.register(
            "kivulite_sword", () -> new KivuliteSwordItem(LKToolTiers.KIVULITE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> KIVULITE_PICKAXE = ITEMS.register(
            "kivulite_pickaxe", () -> new KivulitePickaxeItem(LKToolTiers.KIVULITE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> KIVULITE_AXE = ITEMS.register(
            "kivulite_axe", () -> new KivuliteAxeItem(LKToolTiers.KIVULITE, 6.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> KIVULITE_SHOVEL = ITEMS.register(
            "kivulite_shovel", () -> new KivuliteShovelItem(LKToolTiers.KIVULITE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> KIVULITE_HOE =
            ITEMS.register("kivulite_hoe", () -> new HoeItem(LKToolTiers.KIVULITE, -2, -1.0F, new Item.Properties()));

    // ========== Corrupt Pridestone Tools ==========
    public static final RegistryObject<SwordItem> CORRUPT_SWORD = ITEMS.register(
            "corrupt_sword", () -> new SwordItem(LKToolTiers.CORRUPT_PRIDESTONE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> CORRUPT_PICKAXE = ITEMS.register(
            "corrupt_pickaxe", () -> new PickaxeItem(LKToolTiers.CORRUPT_PRIDESTONE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> CORRUPT_AXE = ITEMS.register(
            "corrupt_axe", () -> new AxeItem(LKToolTiers.CORRUPT_PRIDESTONE, 6.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> CORRUPT_SHOVEL = ITEMS.register(
            "corrupt_shovel", () -> new ShovelItem(LKToolTiers.CORRUPT_PRIDESTONE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> CORRUPT_HOE = ITEMS.register(
            "corrupt_hoe", () -> new HoeItem(LKToolTiers.CORRUPT_PRIDESTONE, -1, -2.0F, new Item.Properties()));

    // ========== Silver Armor ==========
    public static final RegistryObject<ArmorItem> SILVER_HELMET = ITEMS.register(
            "silver_helmet",
            () -> new ArmorItem(LKArmorMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<ArmorItem> SILVER_CHESTPLATE = ITEMS.register(
            "silver_chestplate",
            () -> new ArmorItem(LKArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<ArmorItem> SILVER_LEGGINGS = ITEMS.register(
            "silver_leggings",
            () -> new ArmorItem(LKArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<ArmorItem> SILVER_BOOTS = ITEMS.register(
            "silver_boots", () -> new ArmorItem(LKArmorMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties()));

    // ========== Gemsbok Armor ==========
    public static final RegistryObject<ArmorItem> GEMSBOK_HELMET = ITEMS.register(
            "gemsbok_helmet",
            () -> new ArmorItem(LKArmorMaterials.GEMSBOK, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<ArmorItem> GEMSBOK_CHESTPLATE = ITEMS.register(
            "gemsbok_chestplate",
            () -> new ArmorItem(LKArmorMaterials.GEMSBOK, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<ArmorItem> GEMSBOK_LEGGINGS = ITEMS.register(
            "gemsbok_leggings",
            () -> new ArmorItem(LKArmorMaterials.GEMSBOK, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<ArmorItem> GEMSBOK_BOOTS = ITEMS.register(
            "gemsbok_boots",
            () -> new ArmorItem(LKArmorMaterials.GEMSBOK, ArmorItem.Type.BOOTS, new Item.Properties()));

    // ========== Peacock Armor ==========
    public static final RegistryObject<ArmorItem> PEACOCK_HELMET = ITEMS.register(
            "peacock_helmet",
            () -> new ArmorItem(LKArmorMaterials.PEACOCK, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<ArmorItem> PEACOCK_CHESTPLATE = ITEMS.register(
            "peacock_chestplate",
            () -> new ArmorItem(LKArmorMaterials.PEACOCK, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<ArmorItem> PEACOCK_LEGGINGS = ITEMS.register(
            "peacock_leggings",
            () -> new ArmorItem(LKArmorMaterials.PEACOCK, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<ArmorItem> PEACOCK_BOOTS = ITEMS.register(
            "peacock_boots",
            () -> new ArmorItem(LKArmorMaterials.PEACOCK, ArmorItem.Type.BOOTS, new Item.Properties()));

    // ========== Block Items (auto-registered with blocks) ==========
    // Helper method to create block items
    private static RegistryObject<BlockItem> registerBlockItem(
            String name, RegistryObject<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    // Pridestone & Variants
    public static final RegistryObject<BlockItem> PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("pridestone", Blocks.PRIDESTONE);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_BLOCK_ITEM =
            registerBlockItem("corrupt_pridestone", Blocks.CORRUPT_PRIDESTONE);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_ITEM =
            registerBlockItem("pride_brick", Blocks.PRIDE_BRICK);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("corrupt_pride_brick", Blocks.CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> MOSSY_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_pride_brick", Blocks.MOSSY_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> MOSSY_CORRUPT_PRIDE_BRICK_ITEM =
            registerBlockItem("mossy_corrupt_pride_brick", Blocks.MOSSY_CORRUPT_PRIDE_BRICK);
    public static final RegistryObject<BlockItem> PRIDE_PILLAR_ITEM =
            registerBlockItem("pride_pillar", Blocks.PRIDE_PILLAR);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_PILLAR_ITEM =
            registerBlockItem("corrupt_pride_pillar", Blocks.CORRUPT_PRIDE_PILLAR);

    // Ores & Storage
    public static final RegistryObject<BlockItem> PRIDE_COAL_ORE_ITEM =
            registerBlockItem("pride_coal_ore", Blocks.PRIDE_COAL_ORE);
    public static final RegistryObject<BlockItem> SILVER_ORE_ITEM = registerBlockItem("silver_ore", Blocks.SILVER_ORE);
    public static final RegistryObject<BlockItem> PEACOCK_ORE_ITEM =
            registerBlockItem("peacock_ore", Blocks.PEACOCK_ORE);
    public static final RegistryObject<BlockItem> SILVER_BLOCK_ITEM =
            registerBlockItem("silver_block", Blocks.SILVER_BLOCK);
    public static final RegistryObject<BlockItem> PEACOCK_BLOCK_ITEM =
            registerBlockItem("peacock_block", Blocks.PEACOCK_BLOCK);

    // Wood - Acacia
    public static final RegistryObject<BlockItem> ACACIA_LOG_ITEM =
            registerBlockItem("pride_acacia_log", Blocks.ACACIA_LOG);
    public static final RegistryObject<BlockItem> ACACIA_PLANKS_ITEM =
            registerBlockItem("pride_acacia_planks", Blocks.ACACIA_PLANKS);
    public static final RegistryObject<BlockItem> ACACIA_STAIRS_ITEM =
            registerBlockItem("pride_acacia_stairs", Blocks.ACACIA_STAIRS);
    public static final RegistryObject<BlockItem> ACACIA_SLAB_ITEM =
            registerBlockItem("pride_acacia_slab", Blocks.ACACIA_SLAB);

    // Wood - Rainforest
    public static final RegistryObject<BlockItem> RAINFOREST_LOG_ITEM =
            registerBlockItem("rainforest_log", Blocks.RAINFOREST_LOG);
    public static final RegistryObject<BlockItem> RAINFOREST_PLANKS_ITEM =
            registerBlockItem("rainforest_planks", Blocks.RAINFOREST_PLANKS);
    public static final RegistryObject<BlockItem> RAINFOREST_STAIRS_ITEM =
            registerBlockItem("rainforest_stairs", Blocks.RAINFOREST_STAIRS);
    public static final RegistryObject<BlockItem> RAINFOREST_SLAB_ITEM =
            registerBlockItem("rainforest_slab", Blocks.RAINFOREST_SLAB);

    // Wood - Mango
    public static final RegistryObject<BlockItem> MANGO_LOG_ITEM = registerBlockItem("mango_log", Blocks.MANGO_LOG);
    public static final RegistryObject<BlockItem> MANGO_PLANKS_ITEM =
            registerBlockItem("mango_planks", Blocks.MANGO_PLANKS);
    public static final RegistryObject<BlockItem> MANGO_STAIRS_ITEM =
            registerBlockItem("mango_stairs", Blocks.MANGO_STAIRS);
    public static final RegistryObject<BlockItem> MANGO_SLAB_ITEM = registerBlockItem("mango_slab", Blocks.MANGO_SLAB);

    // Wood - Passion
    public static final RegistryObject<BlockItem> PASSION_LOG_ITEM =
            registerBlockItem("passion_log", Blocks.PASSION_LOG);
    public static final RegistryObject<BlockItem> PASSION_PLANKS_ITEM =
            registerBlockItem("passion_planks", Blocks.PASSION_PLANKS);
    public static final RegistryObject<BlockItem> PASSION_STAIRS_ITEM =
            registerBlockItem("passion_stairs", Blocks.PASSION_STAIRS);
    public static final RegistryObject<BlockItem> PASSION_SLAB_ITEM =
            registerBlockItem("passion_slab", Blocks.PASSION_SLAB);

    // Wood - Banana
    public static final RegistryObject<BlockItem> BANANA_LOG_ITEM = registerBlockItem("banana_log", Blocks.BANANA_LOG);
    public static final RegistryObject<BlockItem> BANANA_PLANKS_ITEM =
            registerBlockItem("banana_planks", Blocks.BANANA_PLANKS);
    public static final RegistryObject<BlockItem> BANANA_STAIRS_ITEM =
            registerBlockItem("banana_stairs", Blocks.BANANA_STAIRS);
    public static final RegistryObject<BlockItem> BANANA_SLAB_ITEM =
            registerBlockItem("banana_slab", Blocks.BANANA_SLAB);

    // Wood - Deadwood
    public static final RegistryObject<BlockItem> DEADWOOD_LOG_ITEM =
            registerBlockItem("deadwood_log", Blocks.DEADWOOD_LOG);
    public static final RegistryObject<BlockItem> DEADWOOD_PLANKS_ITEM =
            registerBlockItem("deadwood_planks", Blocks.DEADWOOD_PLANKS);
    public static final RegistryObject<BlockItem> DEADWOOD_STAIRS_ITEM =
            registerBlockItem("deadwood_stairs", Blocks.DEADWOOD_STAIRS);
    public static final RegistryObject<BlockItem> DEADWOOD_SLAB_ITEM =
            registerBlockItem("deadwood_slab", Blocks.DEADWOOD_SLAB);

    // Stone stairs/slabs
    public static final RegistryObject<BlockItem> PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("pridestone_stairs", Blocks.PRIDESTONE_STAIRS);
    public static final RegistryObject<BlockItem> PRIDESTONE_SLAB_ITEM =
            registerBlockItem("pridestone_slab", Blocks.PRIDESTONE_SLAB);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("pride_brick_stairs", Blocks.PRIDE_BRICK_STAIRS);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("pride_brick_slab", Blocks.PRIDE_BRICK_SLAB);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_STAIRS_ITEM =
            registerBlockItem("corrupt_pridestone_stairs", Blocks.CORRUPT_PRIDESTONE_STAIRS);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_SLAB_ITEM =
            registerBlockItem("corrupt_pridestone_slab", Blocks.CORRUPT_PRIDESTONE_SLAB);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_STAIRS_ITEM =
            registerBlockItem("corrupt_pride_brick_stairs", Blocks.CORRUPT_PRIDE_BRICK_STAIRS);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDE_BRICK_SLAB_ITEM =
            registerBlockItem("corrupt_pride_brick_slab", Blocks.CORRUPT_PRIDE_BRICK_SLAB);

    // Walls
    public static final RegistryObject<BlockItem> PRIDESTONE_WALL_ITEM =
            registerBlockItem("pridestone_wall", Blocks.PRIDESTONE_WALL);
    public static final RegistryObject<BlockItem> PRIDE_BRICK_WALL_ITEM =
            registerBlockItem("pride_brick_wall", Blocks.PRIDE_BRICK_WALL);
    public static final RegistryObject<BlockItem> CORRUPT_PRIDESTONE_WALL_ITEM =
            registerBlockItem("corrupt_pridestone_wall", Blocks.CORRUPT_PRIDESTONE_WALL);

    // Redstone
    public static final RegistryObject<BlockItem> PRIDESTONE_PRESSURE_PLATE_ITEM =
            registerBlockItem("pridestone_pressure_plate", Blocks.PRIDESTONE_PRESSURE_PLATE);
    public static final RegistryObject<BlockItem> PRIDESTONE_BUTTON_ITEM =
            registerBlockItem("pridestone_button", Blocks.PRIDESTONE_BUTTON);

    // Misc blocks
    public static final RegistryObject<BlockItem> DRIED_MAIZE_BLOCK_ITEM =
            registerBlockItem("dried_maize_block", Blocks.DRIED_MAIZE_BLOCK);
    public static final RegistryObject<BlockItem> DRIED_MAIZE_STAIRS_ITEM =
            registerBlockItem("dried_maize_stairs", Blocks.DRIED_MAIZE_STAIRS);
    public static final RegistryObject<BlockItem> DRIED_MAIZE_SLAB_ITEM =
            registerBlockItem("dried_maize_slab", Blocks.DRIED_MAIZE_SLAB);
    public static final RegistryObject<BlockItem> OUTSAND_ITEM = registerBlockItem("outsand", Blocks.OUTSAND);
    public static final RegistryObject<BlockItem> OUTGLASS_ITEM = registerBlockItem("outglass", Blocks.OUTGLASS);
    public static final RegistryObject<BlockItem> OUTGLASS_PANE_ITEM =
            registerBlockItem("outglass_pane", Blocks.OUTGLASS_PANE);
    public static final RegistryObject<BlockItem> TERMITE_MOUND_ITEM =
            registerBlockItem("termite_mound", Blocks.TERMITE_MOUND);
    public static final RegistryObject<BlockItem> ZIRA_MOUND_GATE_ITEM =
            registerBlockItem("zira_mound_gate", Blocks.ZIRA_MOUND_GATE);
    public static final RegistryObject<BlockItem> PUMBAA_BOX_ITEM = registerBlockItem("pumbaa_box", Blocks.PUMBAA_BOX);

    // ========== Phase 2: Nature Block Items ==========
    // Leaves
    public static final RegistryObject<BlockItem> ACACIA_LEAVES_ITEM =
            registerBlockItem("pride_acacia_leaves", Blocks.ACACIA_LEAVES);
    public static final RegistryObject<BlockItem> RAINFOREST_LEAVES_ITEM =
            registerBlockItem("rainforest_leaves", Blocks.RAINFOREST_LEAVES);
    public static final RegistryObject<BlockItem> MANGO_LEAVES_ITEM =
            registerBlockItem("mango_leaves", Blocks.MANGO_LEAVES);
    public static final RegistryObject<BlockItem> PASSION_LEAVES_ITEM =
            registerBlockItem("passion_leaves", Blocks.PASSION_LEAVES);
    public static final RegistryObject<BlockItem> BANANA_LEAVES_ITEM =
            registerBlockItem("banana_leaves", Blocks.BANANA_LEAVES);
    public static final RegistryObject<BlockItem> RAFIKI_LEAVES_ITEM =
            registerBlockItem("rafiki_leaves", Blocks.RAFIKI_LEAVES);

    // Saplings
    public static final RegistryObject<BlockItem> ACACIA_SAPLING_ITEM =
            registerBlockItem("pride_acacia_sapling", Blocks.ACACIA_SAPLING);
    public static final RegistryObject<BlockItem> RAINFOREST_SAPLING_ITEM =
            registerBlockItem("rainforest_sapling", Blocks.RAINFOREST_SAPLING);
    public static final RegistryObject<BlockItem> MANGO_SAPLING_ITEM =
            registerBlockItem("mango_sapling", Blocks.MANGO_SAPLING);
    public static final RegistryObject<BlockItem> PASSION_SAPLING_ITEM =
            registerBlockItem("passion_sapling", Blocks.PASSION_SAPLING);
    public static final RegistryObject<BlockItem> BANANA_SAPLING_ITEM =
            registerBlockItem("banana_sapling", Blocks.BANANA_SAPLING);

    // Rafiki Wood
    public static final RegistryObject<BlockItem> RAFIKI_WOOD_ITEM =
            registerBlockItem("rafiki_wood", Blocks.RAFIKI_WOOD);

    // Flowers
    public static final RegistryObject<BlockItem> WHITE_FLOWER_ITEM =
            registerBlockItem("white_flower", Blocks.WHITE_FLOWER);
    public static final RegistryObject<BlockItem> BLUE_FLOWER_ITEM =
            registerBlockItem("blue_flower", Blocks.BLUE_FLOWER);
    public static final RegistryObject<BlockItem> PURPLE_FLOWER_ITEM =
            registerBlockItem("purple_flower", Blocks.PURPLE_FLOWER);
    public static final RegistryObject<BlockItem> RED_FLOWER_ITEM = registerBlockItem("red_flower", Blocks.RED_FLOWER);

    // Waterlilies
    public static final RegistryObject<BlockItem> RED_LILY_ITEM = registerBlockItem("red_lily", Blocks.RED_LILY);
    public static final RegistryObject<BlockItem> VIOLET_LILY_ITEM =
            registerBlockItem("violet_lily", Blocks.VIOLET_LILY);
    public static final RegistryObject<BlockItem> WHITE_LILY_ITEM = registerBlockItem("white_lily", Blocks.WHITE_LILY);

    // Mushrooms
    public static final RegistryObject<BlockItem> OUTSHROOM_ITEM = registerBlockItem("outshroom", Blocks.OUTSHROOM);
    public static final RegistryObject<BlockItem> OUTSHROOM_GLOWING_ITEM =
            registerBlockItem("outshroom_glowing", Blocks.OUTSHROOM_GLOWING);

    // Misc nature
    public static final RegistryObject<BlockItem> ARID_GRASS_ITEM = registerBlockItem("arid_grass", Blocks.ARID_GRASS);
    public static final RegistryObject<BlockItem> HYENA_TORCH_ITEM =
            registerBlockItem("hyena_torch", Blocks.HYENA_TORCH);
    public static final RegistryObject<BlockItem> HANGING_BANANA_ITEM =
            registerBlockItem("hanging_banana", Blocks.HANGING_BANANA);
    public static final RegistryObject<BlockItem> KIWANO_BLOCK_ITEM =
            registerBlockItem("kiwano_block", Blocks.KIWANO_BLOCK);

    // Crop seeds
    public static final RegistryObject<Item> KIWANO_SEEDS = ITEMS.register(
            "kiwano_seeds",
            () -> new net.minecraft.world.item.ItemNameBlockItem(Blocks.KIWANO_STEM.get(), new Item.Properties()));

    public static final RegistryObject<Item> YAM = ITEMS.register(
            "yam", () -> new net.minecraft.world.item.ItemNameBlockItem(Blocks.YAM_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> MAIZE_STALKS = ITEMS.register(
            "maize_stalks",
            () -> new net.minecraft.world.item.ItemNameBlockItem(Blocks.MAIZE_CROP.get(), new Item.Properties()));

    // Decorative block entities
    public static final RegistryObject<BlockItem> HYENA_HEAD_ITEM = ITEMS.register(
            "hyena_head",
            () -> new io.github.ron1196.thelionking.item.HyenaHeadBlockItem(
                    Blocks.HYENA_HEAD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> FUR_RUG_ITEM = registerBlockItem("fur_rug", Blocks.FUR_RUG);

    // ========== Outlands Armor ==========
    public static final RegistryObject<ArmorItem> OUTLANDS_HELMET = ITEMS.register(
            "outlands_helmet",
            () -> new ArmorItem(LKArmorMaterials.OUTLANDS, ArmorItem.Type.HELMET, new Item.Properties()));

    // ========== Ticket Lion Suit ==========
    public static final RegistryObject<ArmorItem> TICKET_LION_HEAD = ITEMS.register(
            "ticket_lion_head",
            () -> new ArmorItem(LKArmorMaterials.TICKET_LION, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<ArmorItem> TICKET_LION_SUIT = ITEMS.register(
            "ticket_lion_suit",
            () -> new ArmorItem(LKArmorMaterials.TICKET_LION, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<ArmorItem> TICKET_LION_LEGS = ITEMS.register(
            "ticket_lion_legs",
            () -> new ArmorItem(LKArmorMaterials.TICKET_LION, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> TICKET_LION_FEET = ITEMS.register(
            "ticket_lion_feet",
            () -> new ArmorItem(LKArmorMaterials.TICKET_LION, ArmorItem.Type.BOOTS, new Item.Properties()));

    // ========== Darts ==========
    public static final RegistryObject<Item> DART_BLUE =
            ITEMS.register("dart_blue", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DART_RED =
            ITEMS.register("dart_red", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DART_YELLOW =
            ITEMS.register("dart_yellow", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DART_PINK =
            ITEMS.register("dart_pink", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DART_BLACK =
            ITEMS.register("dart_black", () -> new Item(new Item.Properties()));

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

    // ========== Phase 3: Spawn Eggs ==========
    public static final RegistryObject<Item> LION_SPAWN_EGG = ITEMS.register(
            "lion_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypes.LION, 0xD4A030, 0x8B6914, new Item.Properties()));
    public static final RegistryObject<Item> LIONESS_SPAWN_EGG = ITEMS.register(
            "lioness_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.LIONESS, 0xD4A030, 0xC8A848, new Item.Properties()));
    public static final RegistryObject<Item> ZEBRA_SPAWN_EGG = ITEMS.register(
            "zebra_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.ZEBRA, 0xFFFFFF, 0x222222, new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_SPAWN_EGG = ITEMS.register(
            "giraffe_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.GIRAFFE, 0xE8B84B, 0x8B5E3C, new Item.Properties()));
    public static final RegistryObject<Item> RHINO_SPAWN_EGG = ITEMS.register(
            "rhino_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.RHINO, 0x808080, 0x505050, new Item.Properties()));
    public static final RegistryObject<Item> GEMSBOK_SPAWN_EGG = ITEMS.register(
            "gemsbok_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.GEMSBOK, 0xC8A878, 0x4A3B2A, new Item.Properties()));
    public static final RegistryObject<Item> DIKDIK_SPAWN_EGG = ITEMS.register(
            "dikdik_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.DIKDIK, 0xB8956A, 0x8B7355, new Item.Properties()));
    public static final RegistryObject<Item> FLAMINGO_SPAWN_EGG = ITEMS.register(
            "flamingo_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.FLAMINGO, 0xFF69B4, 0xFF1493, new Item.Properties()));
    public static final RegistryObject<Item> ZAZU_SPAWN_EGG = ITEMS.register(
            "zazu_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypes.ZAZU, 0x4169E1, 0xFFD700, new Item.Properties()));
    public static final RegistryObject<Item> BUG_SPAWN_EGG = ITEMS.register(
            "bug_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypes.BUG, 0x4B3621, 0x2E1F0F, new Item.Properties()));

    // ========== Phase 4: Hostile Spawn Eggs ==========
    public static final RegistryObject<Item> HYENA_SPAWN_EGG = ITEMS.register(
            "hyena_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.HYENA, 0x8B7355, 0x4A3B2A, new Item.Properties()));
    public static final RegistryObject<Item> SKELETAL_HYENA_SPAWN_EGG = ITEMS.register(
            "skeletal_hyena_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.SKELETAL_HYENA, 0xC8C8C8, 0x505050, new Item.Properties()));
    public static final RegistryObject<Item> OUTLANDER_SPAWN_EGG = ITEMS.register(
            "outlander_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.OUTLANDER, 0x5C3A1E, 0x3A2510, new Item.Properties()));
    public static final RegistryObject<Item> VULTURE_SPAWN_EGG = ITEMS.register(
            "vulture_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.VULTURE, 0x2A1F14, 0x8B0000, new Item.Properties()));
    public static final RegistryObject<Item> CROCODILE_SPAWN_EGG = ITEMS.register(
            "crocodile_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.CROCODILE, 0x3B5323, 0x1A2E0A, new Item.Properties()));
    public static final RegistryObject<Item> TERMITE_SPAWN_EGG = ITEMS.register(
            "termite_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.TERMITE, 0xD2B48C, 0x8B6914, new Item.Properties()));
    public static final RegistryObject<Item> TERMITE_QUEEN_SPAWN_EGG = ITEMS.register(
            "termite_queen_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.TERMITE_QUEEN, 0xD2B48C, 0xFF4500, new Item.Properties()));

    // ========== Ticket Lion Spawn Egg ==========
    public static final RegistryObject<Item> TICKET_LION_SPAWN_EGG = ITEMS.register(
            "ticket_lion_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.TICKET_LION, 0xD4A030, 0x4169E1, new Item.Properties()));

    // ========== NPC Spawn Eggs ==========
    public static final RegistryObject<Item> RAFIKI_SPAWN_EGG = ITEMS.register(
            "rafiki_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.RAFIKI, 0x8B4513, 0xFFD700, new Item.Properties()));
    public static final RegistryObject<Item> SIMBA_SPAWN_EGG = ITEMS.register(
            "simba_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.SIMBA, 0xD4A030, 0xFFD700, new Item.Properties()));
    public static final RegistryObject<Item> TIMON_SPAWN_EGG = ITEMS.register(
            "timon_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.TIMON, 0xB8860B, 0xFFE4B5, new Item.Properties()));
    public static final RegistryObject<Item> PUMBAA_SPAWN_EGG = ITEMS.register(
            "pumbaa_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.PUMBAA, 0x8B4513, 0x654321, new Item.Properties()));
    public static final RegistryObject<Item> SCAR_SPAWN_EGG = ITEMS.register(
            "scar_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypes.SCAR, 0x2F1A00, 0x000000, new Item.Properties()));
    public static final RegistryObject<Item> ZIRA_SPAWN_EGG = ITEMS.register(
            "zira_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypes.ZIRA, 0x5C3A1E, 0x8B0000, new Item.Properties()));

    // ========== Jar Items ==========
    public static final RegistryObject<Item> JAR_EMPTY =
            ITEMS.register("jar_empty", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JAR_WATER =
            ITEMS.register("jar_water", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> JAR_MILK =
            ITEMS.register("jar_milk", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> JAR_LAVA = ITEMS.register(
            "jar_lava", () -> new Item(new Item.Properties().stacksTo(16).craftRemainder(JAR_EMPTY.get())));
    public static final RegistryObject<Item> MANGO_JUICE = ITEMS.register(
            "mango_juice",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.5F)
                            .build())
                    .craftRemainder(JAR_EMPTY.get())));

    public static final RegistryObject<Item> HYENA_MEAL =
            ITEMS.register("hyena_meal", () -> new HyenaMealItem(new Item.Properties()));

    // ========== Giraffe Ties ==========
    public static final RegistryObject<Item> GIRAFFE_TIE =
            ITEMS.register("giraffe_tie", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_WHITE =
            ITEMS.register("giraffe_tie_white", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_BLUE =
            ITEMS.register("giraffe_tie_blue", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_YELLOW =
            ITEMS.register("giraffe_tie_yellow", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_RED =
            ITEMS.register("giraffe_tie_red", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_PURPLE =
            ITEMS.register("giraffe_tie_purple", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_GREEN =
            ITEMS.register("giraffe_tie_green", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_TIE_BLACK =
            ITEMS.register("giraffe_tie_black", () -> new Item(new Item.Properties()));

    // ========== Tunnah Diggah ==========
    public static final RegistryObject<Item> TUNNAH_DIGGAH = ITEMS.register(
            "tunnah_diggah", () -> new TunnahDiggahItem(Tiers.IRON, 1, -2.8F, new Item.Properties().durability(690)));

    // ========== Quest & Special Items ==========
    public static final RegistryObject<Item> AMULET =
            ITEMS.register("amulet", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIMBA_CHARM = ITEMS.register(
            "simba_charm", () -> new io.github.ron1196.thelionking.item.SimbaCharmItem(new Item.Properties()));
    public static final RegistryObject<Item> GIRAFFE_SADDLE =
            ITEMS.register("giraffe_saddle", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DART_QUIVER =
            ITEMS.register("dart_quiver", () -> new QuiverItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PASSION_FRUIT = ITEMS.register(
            "passion_fruit",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.3F)
                            .build())));
    public static final RegistryObject<Item> ZAZU_EGG =
            ITEMS.register("zazu_egg", () -> new Item(new Item.Properties()));

    // ========== Block Entity Items ==========
    public static final RegistryObject<BlockItem> GRINDING_BOWL_ITEM =
            registerBlockItem("grinding_bowl", Blocks.GRINDING_BOWL);
    public static final RegistryObject<BlockItem> BUG_TRAP_ITEM = registerBlockItem("bug_trap", Blocks.BUG_TRAP);
    public static final RegistryObject<BlockItem> BONGO_DRUM_ITEM = registerBlockItem("bongo_drum", Blocks.BONGO_DRUM);
    public static final RegistryObject<BlockItem> OUTLANDS_POOL_ITEM =
            registerBlockItem("outlands_pool", Blocks.OUTLANDS_POOL);
    public static final RegistryObject<BlockItem> LK_SPAWNER_ITEM = registerBlockItem("lk_spawner", Blocks.LK_SPAWNER);

    // ========== Quest / NPC Items ==========
    public static final RegistryObject<Item> QUEST_BOOK = ITEMS.register(
            "quest_book", () -> new io.github.ron1196.thelionking.item.QuestBookItem(new Item.Properties()));

    public static final RegistryObject<Item> TICKET =
            ITEMS.register("ticket", () -> new io.github.ron1196.thelionking.item.TicketItem(new Item.Properties()));

    public static final RegistryObject<Item> RHYTHM_STAFF =
            ITEMS.register("rhythm_staff", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> RAFIKI_COIN =
            ITEMS.register("rafiki_coin", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ZIRA_COIN =
            ITEMS.register("zira_coin", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WAYWARD_FEATHER =
            ITEMS.register("wayward_feather", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAFIKI_STICK = ITEMS.register(
            "rafiki_stick", () -> new io.github.ron1196.thelionking.item.RafikiStickItem(new Item.Properties()));

    public static final RegistryObject<Item> RAFIKI_DUST = ITEMS.register(
            "rafiki_dust", () -> new io.github.ron1196.thelionking.item.RafikiDustItem(new Item.Properties()));

    // ========== Phase 12: Missing Block Items ==========
    public static final RegistryObject<BlockItem> BANANA_CAKE_ITEM =
            registerBlockItem("banana_cake", Blocks.BANANA_CAKE);
    public static final RegistryObject<BlockItem> MOUNTED_SHOOTER_ITEM =
            registerBlockItem("mounted_shooter", Blocks.MOUNTED_SHOOTER);
    public static final RegistryObject<BlockItem> STAR_ALTAR_ITEM = registerBlockItem("star_altar", Blocks.STAR_ALTAR);
    public static final RegistryObject<BlockItem> OUTLANDS_ALTAR_ITEM =
            registerBlockItem("outlands_altar", Blocks.OUTLANDS_ALTAR);
    public static final RegistryObject<BlockItem> TILLED_SAND_ITEM =
            registerBlockItem("tilled_sand", Blocks.TILLED_SAND);
    public static final RegistryObject<BlockItem> VASE_ITEM = registerBlockItem("vase", Blocks.VASE);

    // ========== Bed & Lever ==========
    public static final RegistryObject<BlockItem> PRIDE_BED_ITEM =
            ITEMS.register("pride_bed", () -> new BedItem(Blocks.PRIDE_BED.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BlockItem> PRIDE_LEVER_ITEM =
            registerBlockItem("pride_lever", Blocks.PRIDE_LEVER);

    // ========== Portal Frame Items ==========
    public static final RegistryObject<BlockItem> PRIDE_PORTAL_FRAME_ITEM =
            registerBlockItem("pride_portal_frame", Blocks.PRIDE_PORTAL_FRAME);
    public static final RegistryObject<BlockItem> OUTLANDS_PORTAL_FRAME_ITEM =
            registerBlockItem("outlands_portal_frame", Blocks.OUTLANDS_PORTAL_FRAME);

    // ========== Scar / Zira Rugs ==========
    public static final RegistryObject<Item> SCAR_RUG =
            ITEMS.register("scar_rug", () -> new RugItem(RugEntity.TYPE_SCAR, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ZIRA_RUG =
            ITEMS.register("zira_rug", () -> new RugItem(RugEntity.TYPE_ZIRA, new Item.Properties().stacksTo(1)));

    // ========== Skeletal Hyena Head Spawn Egg ==========
    public static final RegistryObject<Item> SKELETAL_HYENA_HEAD_SPAWN_EGG = ITEMS.register(
            "skeletal_hyena_head_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypes.SKELETAL_HYENA_HEAD, 0xC8C8C8, 0x3A3A3A, new Item.Properties()));

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
