package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CircleOfCraftMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB =
            TABS.register("blocks", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.blocks"))
                    .icon(() -> new ItemStack(ModBlocks.PRIDE_BRICK.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(ModItems.PRIDE_BRICK_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(ModItems.CRACKED_PRIDE_BRICK_ITEM.get());
                        output.accept(ModItems.MOSSY_PRIDE_BRICK_ITEM.get());
                        output.accept(ModItems.MOSSY_CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(ModItems.PRIDE_PILLAR_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDE_PILLAR_ITEM.get());
                        output.accept(ModItems.PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(ModItems.PRIDESTONE_SLAB_ITEM.get());
                        output.accept(ModItems.PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(ModItems.PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDESTONE_SLAB_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(ModItems.PRIDE_PILLAR_SLAB_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDE_PILLAR_SLAB_ITEM.get());
                        output.accept(ModItems.PRIDESTONE_WALL_ITEM.get());
                        output.accept(ModItems.PRIDE_BRICK_WALL_ITEM.get());
                        output.accept(ModItems.CORRUPT_PRIDESTONE_WALL_ITEM.get());
                        output.accept(ModItems.PRIDESTONE_PRESSURE_PLATE_ITEM.get());
                        output.accept(ModItems.PRIDESTONE_BUTTON_ITEM.get());
                        output.accept(ModItems.PRIDE_LEVER_ITEM.get());
                        output.accept(ModItems.PRIDE_COAL_ORE_ITEM.get());
                        output.accept(ModItems.SILVER_ORE_ITEM.get());
                        output.accept(ModItems.PEACOCK_ORE_ITEM.get());
                        output.accept(ModItems.KIVULITE_ORE_ITEM.get());
                        output.accept(ModItems.NUKA_ORE_ITEM.get());
                        output.accept(ModItems.SILVER_BLOCK_ITEM.get());
                        output.accept(ModItems.PEACOCK_BLOCK_ITEM.get());
                        output.accept(ModItems.KIVULITE_BLOCK_ITEM.get());
                        output.accept(ModItems.OUTSAND_ITEM.get());
                        output.accept(ModItems.OUTGLASS_ITEM.get());
                        output.accept(ModItems.OUTGLASS_PANE_ITEM.get());
                        output.accept(ModItems.TERMITE_MOUND_ITEM.get());
                        output.accept(ModItems.PUMBAA_BOX_ITEM.get());
                        output.accept(ModItems.GRINDING_BOWL_ITEM.get());
                        output.accept(ModItems.BUG_TRAP_ITEM.get());
                        output.accept(ModItems.OAK_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.SPRUCE_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.BIRCH_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.JUNGLE_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.ACACIA_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.DARK_OAK_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.MANGROVE_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.CHERRY_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.BAMBOO_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.CRIMSON_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.WARPED_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.BANANA_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.RAINFOREST_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.MANGO_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.PASSION_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.DEADWOOD_BUG_TRAP_ITEM.get());
                        output.accept(ModItems.BONGO_DRUM_ITEM.get());
                        output.accept(ModItems.OUTLANDS_POOL_ITEM.get());
                        // Phase 12 blocks
                        output.accept(ModItems.BANANA_CAKE_ITEM.get());
                        output.accept(ModItems.MOUNTED_SHOOTER_ITEM.get());
                        output.accept(ModItems.STAR_ALTAR_ITEM.get());
                        output.accept(ModItems.OUTLANDS_ALTAR_ITEM.get());
                        output.accept(ModItems.ZIRA_MOUND_GATE_ITEM.get());
                        output.accept(ModItems.TILLED_SAND_ITEM.get());
                        output.accept(ModItems.VASE_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DECO_TAB =
            TABS.register("decorations", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.decorations"))
                    .icon(() -> new ItemStack(ModBlocks.ACACIA_LOG.get()))
                    .displayItems((params, output) -> {
                        // Logs
                        output.accept(ModItems.ACACIA_LOG_ITEM.get());
                        output.accept(ModItems.RAINFOREST_LOG_ITEM.get());
                        output.accept(ModItems.MANGO_LOG_ITEM.get());
                        output.accept(ModItems.PASSION_LOG_ITEM.get());
                        output.accept(ModItems.BANANA_LOG_ITEM.get());
                        output.accept(ModItems.DEADWOOD_LOG_ITEM.get());
                        // Planks
                        output.accept(ModItems.ACACIA_PLANKS_ITEM.get());
                        output.accept(ModItems.RAINFOREST_PLANKS_ITEM.get());
                        output.accept(ModItems.MANGO_PLANKS_ITEM.get());
                        output.accept(ModItems.PASSION_PLANKS_ITEM.get());
                        output.accept(ModItems.BANANA_PLANKS_ITEM.get());
                        output.accept(ModItems.DEADWOOD_PLANKS_ITEM.get());
                        // Stairs
                        output.accept(ModItems.ACACIA_STAIRS_ITEM.get());
                        output.accept(ModItems.RAINFOREST_STAIRS_ITEM.get());
                        output.accept(ModItems.MANGO_STAIRS_ITEM.get());
                        output.accept(ModItems.PASSION_STAIRS_ITEM.get());
                        output.accept(ModItems.BANANA_STAIRS_ITEM.get());
                        output.accept(ModItems.DEADWOOD_STAIRS_ITEM.get());
                        // Slabs
                        output.accept(ModItems.ACACIA_SLAB_ITEM.get());
                        output.accept(ModItems.RAINFOREST_SLAB_ITEM.get());
                        output.accept(ModItems.MANGO_SLAB_ITEM.get());
                        output.accept(ModItems.PASSION_SLAB_ITEM.get());
                        output.accept(ModItems.BANANA_SLAB_ITEM.get());
                        output.accept(ModItems.DEADWOOD_SLAB_ITEM.get());
                        // Dried Maize
                        output.accept(ModItems.DRIED_MAIZE_BLOCK_ITEM.get());
                        output.accept(ModItems.DRIED_MAIZE_STAIRS_ITEM.get());
                        output.accept(ModItems.DRIED_MAIZE_SLAB_ITEM.get());
                        // Leaves
                        output.accept(ModItems.ACACIA_LEAVES_ITEM.get());
                        output.accept(ModItems.RAINFOREST_LEAVES_ITEM.get());
                        output.accept(ModItems.MANGO_LEAVES_ITEM.get());
                        output.accept(ModItems.PASSION_LEAVES_ITEM.get());
                        output.accept(ModItems.BANANA_LEAVES_ITEM.get());
                        output.accept(ModItems.RAFIKI_LEAVES_ITEM.get());
                        // Saplings
                        output.accept(ModItems.ACACIA_SAPLING_ITEM.get());
                        output.accept(ModItems.RAINFOREST_SAPLING_ITEM.get());
                        output.accept(ModItems.MANGO_SAPLING_ITEM.get());
                        output.accept(ModItems.PASSION_SAPLING_ITEM.get());
                        output.accept(ModItems.BANANA_SAPLING_ITEM.get());
                        // Rafiki Wood
                        output.accept(ModItems.RAFIKI_WOOD_ITEM.get());
                        // Flowers
                        output.accept(ModItems.WHITE_FLOWER_ITEM.get());
                        output.accept(ModItems.BLUE_FLOWER_ITEM.get());
                        output.accept(ModItems.PURPLE_FLOWER_ITEM.get());
                        output.accept(ModItems.RED_FLOWER_ITEM.get());
                        // Lilies
                        output.accept(ModItems.LILY_RED_ITEM.get());
                        output.accept(ModItems.LILY_VIOLET_ITEM.get());
                        output.accept(ModItems.LILY_WHITE_ITEM.get());
                        // Mushrooms
                        output.accept(ModItems.OUTSHROOM_ITEM.get());
                        output.accept(ModItems.OUTSHROOM_GLOWING_ITEM.get());
                        // Misc
                        output.accept(ModItems.ARID_GRASS_ITEM.get());
                        output.accept(ModItems.HYENA_TORCH_ITEM.get());
                        output.accept(ModItems.HANGING_BANANA_ITEM.get());
                        output.accept(ModItems.KIWANO_BLOCK_ITEM.get());
                        output.accept(ModItems.KIWANO_SEEDS.get());
                        output.accept(ModItems.YAM.get());
                        output.accept(ModItems.MAIZE_STALKS.get());
                        // Decorative block entities
                        output.accept(ModItems.HYENA_HEAD_ITEM.get());
                        // Dyeable rugs
                        output.accept(ModItems.RUG_WHITE_ITEM.get());
                        output.accept(ModItems.RUG_LIGHT_GRAY_ITEM.get());
                        output.accept(ModItems.RUG_GRAY_ITEM.get());
                        output.accept(ModItems.RUG_BLACK_ITEM.get());
                        output.accept(ModItems.RUG_RED_ITEM.get());
                        output.accept(ModItems.RUG_ORANGE_ITEM.get());
                        output.accept(ModItems.RUG_LION_ITEM.get());
                        output.accept(ModItems.RUG_YELLOW_ITEM.get());
                        output.accept(ModItems.RUG_LIGHT_GREEN_ITEM.get());
                        output.accept(ModItems.RUG_GREEN_ITEM.get());
                        output.accept(ModItems.RUG_LIGHT_BLUE_ITEM.get());
                        output.accept(ModItems.RUG_BLUE_ITEM.get());
                        output.accept(ModItems.RUG_VIOLET_ITEM.get());
                        output.accept(ModItems.RUG_PURPLE_ITEM.get());
                        output.accept(ModItems.RUG_PINK_ITEM.get());
                        output.accept(ModItems.RUG_OUTLANDER_ITEM.get());
                        // Bed
                        output.accept(ModItems.PRIDE_BED_ITEM.get());
                        // Vases
                        output.accept(ModItems.VASE_ACACIA_ITEM.get());
                        output.accept(ModItems.VASE_RAINFOREST_ITEM.get());
                        output.accept(ModItems.VASE_MANGO_ITEM.get());
                        output.accept(ModItems.VASE_PASSION_ITEM.get());
                        output.accept(ModItems.VASE_BANANA_ITEM.get());
                        output.accept(ModItems.VASE_WHITE_FLOWER_ITEM.get());
                        output.accept(ModItems.VASE_BLUE_FLOWER_ITEM.get());
                        output.accept(ModItems.VASE_RED_FLOWER_ITEM.get());
                        output.accept(ModItems.VASE_PURPLE_FLOWER_ITEM.get());
                        output.accept(ModItems.VASE_OUTSHROOM_ITEM.get());
                        output.accept(ModItems.VASE_OUTSHROOM_GLOWING_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> FOOD_TAB = TABS.register("food", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.circleofcraft.food"))
            .icon(() -> new ItemStack(ModItems.ZEBRA_RAW.get()))
            .displayItems((params, output) -> {
                output.accept(ModItems.LION_RAW.get());
                output.accept(ModItems.LION_COOKED.get());
                output.accept(ModItems.ZEBRA_RAW.get());
                output.accept(ModItems.ZEBRA_COOKED.get());
                output.accept(ModItems.RHINO_RAW.get());
                output.accept(ModItems.RHINO_COOKED.get());
                output.accept(ModItems.MANGO.get());
                output.accept(ModItems.BANANA.get());
                output.accept(ModItems.CORN.get());
                output.accept(ModItems.POPCORN.get());
                output.accept(ModItems.KIWANO.get());
                output.accept(ModItems.OUTLANDER_MEAT.get());
                output.accept(ModItems.CROCODILE_MEAT.get());
                output.accept(ModItems.ROAST_YAM.get());
                output.accept(ModItems.BANANA_BREAD.get());
                output.accept(ModItems.CHOCOLATE_MUFASA.get());
                output.accept(ModItems.BUG_STEW.get());
                output.accept(ModItems.EXPERIENCE_GRUB.get());
                output.accept(ModItems.BANANA_CAKE_ITEM.get());
                output.accept(ModItems.MANGO_JUICE.get());
                output.accept(ModItems.PASSION_FRUIT.get());
                output.accept(ModItems.JAR_MILK.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> MATERIALS_TAB =
            TABS.register("materials", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.materials"))
                    .icon(() -> new ItemStack(ModItems.HYENA_BONE.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.SILVER_INGOT.get());
                        output.accept(ModItems.PEACOCK_GEM.get());
                        output.accept(ModItems.KIVULITE.get());
                        output.accept(ModItems.HYENA_BONE.get());
                        output.accept(ModItems.HYENA_BONE_SHARD.get());
                        output.accept(ModItems.TERMITE_DUST.get());
                        output.accept(ModItems.MANGO_DUST.get());
                        output.accept(ModItems.FEATHER_BLUE.get());
                        output.accept(ModItems.FEATHER_YELLOW.get());
                        output.accept(ModItems.FEATHER_RED.get());
                        output.accept(ModItems.FEATHER_BLACK.get());
                        output.accept(ModItems.FEATHER_FLAMINGO.get());
                        output.accept(ModItems.POISON.get());
                        output.accept(ModItems.NUKA_SHARD.get());
                        output.accept(ModItems.OUTLANDER_FUR.get());
                        output.accept(ModItems.ZEBRA_HIDE.get());
                        output.accept(ModItems.GEMSBOK_HIDE.get());
                        output.accept(ModItems.GEMSBOK_HORN.get());
                        output.accept(ModItems.RHINO_HORN.get());
                        output.accept(ModItems.GROUND_RHINO_HORN.get());
                        output.accept(ModItems.CORN_KERNELS.get());
                        output.accept(ModItems.DRIED_MAIZE.get());
                        output.accept(ModItems.LION_FUR.get());
                        output.accept(ModItems.BUG.get());
                        output.accept(ModItems.CRYSTAL.get());
                        // Notes
                        output.accept(ModItems.NOTE_A.get());
                        output.accept(ModItems.NOTE_B.get());
                        output.accept(ModItems.NOTE_C.get());
                        output.accept(ModItems.NOTE_D.get());
                        output.accept(ModItems.NOTE_E.get());
                        // Coins & feather
                        output.accept(ModItems.RAFIKI_COIN.get());
                        output.accept(ModItems.ZIRA_COIN.get());
                        output.accept(ModItems.WAYWARD_FEATHER.get());
                        // Jar items
                        output.accept(ModItems.JAR_EMPTY.get());
                        output.accept(ModItems.JAR_WATER.get());
                        output.accept(ModItems.JAR_MILK.get());
                        output.accept(ModItems.JAR_LAVA.get());
                        output.accept(ModItems.MANGO_JUICE.get());
                        output.accept(ModItems.HYENA_MEAL.get());
                        // Giraffe Ties
                        output.accept(ModItems.GIRAFFE_TIE.get());
                        output.accept(ModItems.GIRAFFE_TIE_WHITE.get());
                        output.accept(ModItems.GIRAFFE_TIE_BLUE.get());
                        output.accept(ModItems.GIRAFFE_TIE_YELLOW.get());
                        output.accept(ModItems.GIRAFFE_TIE_RED.get());
                        output.accept(ModItems.GIRAFFE_TIE_PURPLE.get());
                        output.accept(ModItems.GIRAFFE_TIE_GREEN.get());
                        output.accept(ModItems.GIRAFFE_TIE_BLACK.get());
                        // Musical Notes
                        output.accept(ModItems.NOTE_C.get());
                        output.accept(ModItems.NOTE_D.get());
                        output.accept(ModItems.NOTE_E.get());
                        output.accept(ModItems.NOTE_F.get());
                        output.accept(ModItems.NOTE_G.get());
                        output.accept(ModItems.NOTE_A.get());
                        output.accept(ModItems.NOTE_B.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TOOLS_TAB =
            TABS.register("tools", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.tools"))
                    .icon(() -> new ItemStack(ModItems.SILVER_PICKAXE.get()))
                    .displayItems((params, output) -> {
                        // Pridestone
                        output.accept(ModItems.PRIDESTONE_SWORD.get());
                        output.accept(ModItems.PRIDESTONE_PICKAXE.get());
                        output.accept(ModItems.PRIDESTONE_AXE.get());
                        output.accept(ModItems.PRIDESTONE_SHOVEL.get());
                        output.accept(ModItems.PRIDESTONE_HOE.get());
                        // Silver
                        output.accept(ModItems.SILVER_SWORD.get());
                        output.accept(ModItems.SILVER_PICKAXE.get());
                        output.accept(ModItems.SILVER_AXE.get());
                        output.accept(ModItems.SILVER_SHOVEL.get());
                        output.accept(ModItems.SILVER_HOE.get());
                        // Peacock
                        output.accept(ModItems.PEACOCK_SWORD.get());
                        output.accept(ModItems.PEACOCK_PICKAXE.get());
                        output.accept(ModItems.PEACOCK_AXE.get());
                        output.accept(ModItems.PEACOCK_SHOVEL.get());
                        output.accept(ModItems.PEACOCK_HOE.get());
                        // Kivulite
                        output.accept(ModItems.KIVULITE_SWORD.get());
                        output.accept(ModItems.KIVULITE_PICKAXE.get());
                        output.accept(ModItems.KIVULITE_AXE.get());
                        output.accept(ModItems.KIVULITE_SHOVEL.get());
                        output.accept(ModItems.KIVULITE_HOE.get());
                        // Corrupt
                        output.accept(ModItems.CORRUPT_SWORD.get());
                        output.accept(ModItems.CORRUPT_PICKAXE.get());
                        output.accept(ModItems.CORRUPT_AXE.get());
                        output.accept(ModItems.CORRUPT_SHOVEL.get());
                        output.accept(ModItems.CORRUPT_HOE.get());
                        // Special
                        output.accept(ModItems.TUNNAH_DIGGAH.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> COMBAT_TAB =
            TABS.register("combat", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.combat"))
                    .icon(() -> new ItemStack(ModItems.SILVER_SWORD.get()))
                    .displayItems((params, output) -> {
                        // Swords
                        output.accept(ModItems.PRIDESTONE_SWORD.get());
                        output.accept(ModItems.SILVER_SWORD.get());
                        output.accept(ModItems.PEACOCK_SWORD.get());
                        output.accept(ModItems.KIVULITE_SWORD.get());
                        output.accept(ModItems.CORRUPT_SWORD.get());
                        // Silver Armor
                        output.accept(ModItems.SILVER_HELMET.get());
                        output.accept(ModItems.SILVER_CHESTPLATE.get());
                        output.accept(ModItems.SILVER_LEGGINGS.get());
                        output.accept(ModItems.SILVER_BOOTS.get());
                        // Gemsbok Armor
                        output.accept(ModItems.GEMSBOK_HELMET.get());
                        output.accept(ModItems.GEMSBOK_CHESTPLATE.get());
                        output.accept(ModItems.GEMSBOK_LEGGINGS.get());
                        output.accept(ModItems.GEMSBOK_BOOTS.get());
                        // Peacock Armor
                        output.accept(ModItems.PEACOCK_HELMET.get());
                        output.accept(ModItems.PEACOCK_CHESTPLATE.get());
                        output.accept(ModItems.PEACOCK_LEGGINGS.get());
                        output.accept(ModItems.PEACOCK_BOOTS.get());
                        output.accept(ModItems.PEACOCK_WINGS.get());
                        // Outlands Armor
                        output.accept(ModItems.OUTLANDS_HELMET.get());
                        // Ticket Lion Suit
                        output.accept(ModItems.TICKET_LION_HEAD.get());
                        output.accept(ModItems.TICKET_LION_SUIT.get());
                        output.accept(ModItems.TICKET_LION_LEGS.get());
                        output.accept(ModItems.TICKET_LION_FEET.get());
                        // Darts
                        output.accept(ModItems.DART_BLUE.get());
                        output.accept(ModItems.DART_RED.get());
                        output.accept(ModItems.DART_YELLOW.get());
                        output.accept(ModItems.DART_PINK.get());
                        output.accept(ModItems.DART_BLACK.get());
                        output.accept(ModItems.DART_OUTLANDISH.get());
                        output.accept(ModItems.DART_SHOOTER.get());
                        output.accept(ModItems.DART_SHOOTER_SILVER.get());
                        // Spears & Bombs
                        output.accept(ModItems.GEMSBOK_SPEAR.get());
                        output.accept(ModItems.POISONED_SPEAR.get());
                        output.accept(ModItems.PUMBAA_BOMB.get());
                        output.accept(ModItems.TERMITE_THROWN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> MISC_TAB = TABS.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.circleofcraft.misc"))
            .icon(() -> new ItemStack(ModItems.BUG.get()))
            .displayItems((params, output) -> {
                output.accept(ModItems.LION_SPAWN_EGG.get());
                output.accept(ModItems.ZEBRA_SPAWN_EGG.get());
                output.accept(ModItems.GIRAFFE_SPAWN_EGG.get());
                output.accept(ModItems.RHINO_SPAWN_EGG.get());
                output.accept(ModItems.GEMSBOK_SPAWN_EGG.get());
                output.accept(ModItems.DIKDIK_SPAWN_EGG.get());
                output.accept(ModItems.FLAMINGO_SPAWN_EGG.get());
                output.accept(ModItems.ZAZU_SPAWN_EGG.get());
                output.accept(ModItems.BUG_SPAWN_EGG.get());
                // Hostile
                output.accept(ModItems.HYENA_SPAWN_EGG.get());
                output.accept(ModItems.SKELETAL_HYENA_SPAWN_EGG.get());
                output.accept(ModItems.OUTLANDER_SPAWN_EGG.get());
                output.accept(ModItems.VULTURE_SPAWN_EGG.get());
                output.accept(ModItems.CROCODILE_SPAWN_EGG.get());
                output.accept(ModItems.TERMITE_SPAWN_EGG.get());
                // NPCs
                output.accept(ModItems.RAFIKI_SPAWN_EGG.get());
                output.accept(ModItems.SIMBA_SPAWN_EGG.get());
                output.accept(ModItems.TIMON_SPAWN_EGG.get());
                output.accept(ModItems.PUMBAA_SPAWN_EGG.get());
                output.accept(ModItems.SCAR_SPAWN_EGG.get());
                output.accept(ModItems.ZIRA_SPAWN_EGG.get());
                output.accept(ModItems.TICKET_LION_SPAWN_EGG.get());
                output.accept(ModItems.TERMITE_QUEEN_SPAWN_EGG.get());
                output.accept(ModItems.SKELETAL_HYENA_HEAD_SPAWN_EGG.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> QUEST_TAB =
            TABS.register("quest", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.circleofcraft.quest"))
                    .icon(() -> new ItemStack(ModItems.QUEST_BOOK.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.QUEST_BOOK.get());
                        output.accept(ModItems.TICKET.get());
                        output.accept(ModItems.RHYTHM_STAFF.get());
                        output.accept(ModItems.RAFIKI_COIN.get());
                        output.accept(ModItems.ZIRA_COIN.get());
                        output.accept(ModItems.WAYWARD_FEATHER.get());
                        output.accept(ModItems.CRYSTAL.get());
                        // Quest items (Phase 10)
                        output.accept(ModItems.AMULET.get());
                        output.accept(ModItems.ASTRAL_CHARM.get());
                        output.accept(ModItems.GIRAFFE_SADDLE.get());
                        // output.accept(ModItems.DART_QUIVER.get()); // disabled, see issue #78
                        output.accept(ModItems.PASSION_FRUIT.get());
                        output.accept(ModItems.ZAZU_EGG.get());
                        output.accept(ModItems.SCAR_RUG.get());
                        output.accept(ModItems.ZIRA_RUG.get());
                        output.accept(ModItems.RAFIKI_STICK.get());
                        output.accept(ModItems.RAFIKI_DUST.get());
                        output.accept(ModItems.PRIDE_COMPASS.get());
                    })
                    .build());
}
