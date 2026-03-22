package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheLionKingMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB =
            TABS.register("blocks", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.blocks"))
                    .icon(() -> new ItemStack(LionKingBlocks.PRIDE_BRICK.get()))
                    .displayItems((params, output) -> {
                        output.accept(LionKingItems.PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(LionKingItems.PRIDE_BRICK_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(LionKingItems.CRACKED_PRIDE_BRICK_ITEM.get());
                        output.accept(LionKingItems.MOSSY_PRIDE_BRICK_ITEM.get());
                        output.accept(LionKingItems.MOSSY_CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(LionKingItems.PRIDE_PILLAR_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDE_PILLAR_ITEM.get());
                        output.accept(LionKingItems.PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(LionKingItems.PRIDESTONE_SLAB_ITEM.get());
                        output.accept(LionKingItems.PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(LionKingItems.PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDESTONE_SLAB_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(LionKingItems.PRIDESTONE_WALL_ITEM.get());
                        output.accept(LionKingItems.PRIDE_BRICK_WALL_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDESTONE_WALL_ITEM.get());
                        output.accept(LionKingItems.PRIDESTONE_PRESSURE_PLATE_ITEM.get());
                        output.accept(LionKingItems.PRIDESTONE_BUTTON_ITEM.get());
                        output.accept(LionKingItems.PRIDE_LEVER_ITEM.get());
                        output.accept(LionKingItems.PRIDE_COAL_ORE_ITEM.get());
                        output.accept(LionKingItems.SILVER_ORE_ITEM.get());
                        output.accept(LionKingItems.PEACOCK_ORE_ITEM.get());
                        output.accept(LionKingItems.KIVULITE_ORE_ITEM.get());
                        output.accept(LionKingItems.NUKA_ORE_ITEM.get());
                        output.accept(LionKingItems.SILVER_BLOCK_ITEM.get());
                        output.accept(LionKingItems.PEACOCK_BLOCK_ITEM.get());
                        output.accept(LionKingItems.OUTSAND_ITEM.get());
                        output.accept(LionKingItems.OUTGLASS_ITEM.get());
                        output.accept(LionKingItems.OUTGLASS_PANE_ITEM.get());
                        output.accept(LionKingItems.TERMITE_MOUND_ITEM.get());
                        output.accept(LionKingItems.PUMBAA_BOX_ITEM.get());
                        output.accept(LionKingItems.GRINDING_BOWL_ITEM.get());
                        output.accept(LionKingItems.BUG_TRAP_ITEM.get());
                        output.accept(LionKingItems.BONGO_DRUM_ITEM.get());
                        output.accept(LionKingItems.OUTLANDS_POOL_ITEM.get());
                        output.accept(LionKingItems.LK_SPAWNER_ITEM.get());
                        // Portal frames
                        output.accept(LionKingItems.PRIDE_PORTAL_FRAME_ITEM.get());
                        output.accept(LionKingItems.OUTLANDS_PORTAL_FRAME_ITEM.get());
                        // Phase 12 blocks
                        output.accept(LionKingItems.BANANA_CAKE_ITEM.get());
                        output.accept(LionKingItems.MOUNTED_SHOOTER_ITEM.get());
                        output.accept(LionKingItems.STAR_ALTAR_ITEM.get());
                        output.accept(LionKingItems.OUTLANDS_ALTAR_ITEM.get());
                        output.accept(LionKingItems.ZIRA_MOUND_GATE_ITEM.get());
                        output.accept(LionKingItems.TILLED_SAND_ITEM.get());
                        output.accept(LionKingItems.VASE_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DECO_TAB =
            TABS.register("decorations", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.decorations"))
                    .icon(() -> new ItemStack(LionKingBlocks.ACACIA_LOG.get()))
                    .displayItems((params, output) -> {
                        // Logs
                        output.accept(LionKingItems.ACACIA_LOG_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_LOG_ITEM.get());
                        output.accept(LionKingItems.MANGO_LOG_ITEM.get());
                        output.accept(LionKingItems.PASSION_LOG_ITEM.get());
                        output.accept(LionKingItems.BANANA_LOG_ITEM.get());
                        output.accept(LionKingItems.DEADWOOD_LOG_ITEM.get());
                        // Planks
                        output.accept(LionKingItems.ACACIA_PLANKS_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_PLANKS_ITEM.get());
                        output.accept(LionKingItems.MANGO_PLANKS_ITEM.get());
                        output.accept(LionKingItems.PASSION_PLANKS_ITEM.get());
                        output.accept(LionKingItems.BANANA_PLANKS_ITEM.get());
                        output.accept(LionKingItems.DEADWOOD_PLANKS_ITEM.get());
                        // Stairs
                        output.accept(LionKingItems.ACACIA_STAIRS_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_STAIRS_ITEM.get());
                        output.accept(LionKingItems.MANGO_STAIRS_ITEM.get());
                        output.accept(LionKingItems.PASSION_STAIRS_ITEM.get());
                        output.accept(LionKingItems.BANANA_STAIRS_ITEM.get());
                        output.accept(LionKingItems.DEADWOOD_STAIRS_ITEM.get());
                        // Slabs
                        output.accept(LionKingItems.ACACIA_SLAB_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_SLAB_ITEM.get());
                        output.accept(LionKingItems.MANGO_SLAB_ITEM.get());
                        output.accept(LionKingItems.PASSION_SLAB_ITEM.get());
                        output.accept(LionKingItems.BANANA_SLAB_ITEM.get());
                        output.accept(LionKingItems.DEADWOOD_SLAB_ITEM.get());
                        // Dried Maize
                        output.accept(LionKingItems.DRIED_MAIZE_BLOCK_ITEM.get());
                        output.accept(LionKingItems.DRIED_MAIZE_STAIRS_ITEM.get());
                        output.accept(LionKingItems.DRIED_MAIZE_SLAB_ITEM.get());
                        // Leaves
                        output.accept(LionKingItems.ACACIA_LEAVES_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_LEAVES_ITEM.get());
                        output.accept(LionKingItems.MANGO_LEAVES_ITEM.get());
                        output.accept(LionKingItems.PASSION_LEAVES_ITEM.get());
                        output.accept(LionKingItems.BANANA_LEAVES_ITEM.get());
                        output.accept(LionKingItems.RAFIKI_LEAVES_ITEM.get());
                        // Saplings
                        output.accept(LionKingItems.ACACIA_SAPLING_ITEM.get());
                        output.accept(LionKingItems.RAINFOREST_SAPLING_ITEM.get());
                        output.accept(LionKingItems.MANGO_SAPLING_ITEM.get());
                        output.accept(LionKingItems.PASSION_SAPLING_ITEM.get());
                        output.accept(LionKingItems.BANANA_SAPLING_ITEM.get());
                        // Rafiki Wood
                        output.accept(LionKingItems.RAFIKI_WOOD_ITEM.get());
                        // Flowers
                        output.accept(LionKingItems.WHITE_FLOWER_ITEM.get());
                        output.accept(LionKingItems.BLUE_FLOWER_ITEM.get());
                        output.accept(LionKingItems.PURPLE_FLOWER_ITEM.get());
                        output.accept(LionKingItems.RED_FLOWER_ITEM.get());
                        // Lilies
                        output.accept(LionKingItems.LILY_RED_ITEM.get());
                        output.accept(LionKingItems.LILY_VIOLET_ITEM.get());
                        output.accept(LionKingItems.LILY_WHITE_ITEM.get());
                        // Mushrooms
                        output.accept(LionKingItems.OUTSHROOM_ITEM.get());
                        output.accept(LionKingItems.OUTSHROOM_GLOWING_ITEM.get());
                        // Misc
                        output.accept(LionKingItems.ARID_GRASS_ITEM.get());
                        output.accept(LionKingItems.HYENA_TORCH_ITEM.get());
                        output.accept(LionKingItems.HANGING_BANANA_ITEM.get());
                        output.accept(LionKingItems.KIWANO_BLOCK_ITEM.get());
                        output.accept(LionKingItems.KIWANO_SEEDS.get());
                        output.accept(LionKingItems.YAM.get());
                        output.accept(LionKingItems.MAIZE_STALKS.get());
                        // Decorative block entities
                        output.accept(LionKingItems.HYENA_HEAD_ITEM.get());
                        output.accept(LionKingItems.FUR_RUG_ITEM.get());
                        // Bed
                        output.accept(LionKingItems.PRIDE_BED_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> FOOD_TAB = TABS.register("food", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thelionking.food"))
            .icon(() -> new ItemStack(LionKingItems.ZEBRA_RAW.get()))
            .displayItems((params, output) -> {
                output.accept(LionKingItems.LION_RAW.get());
                output.accept(LionKingItems.LION_COOKED.get());
                output.accept(LionKingItems.ZEBRA_RAW.get());
                output.accept(LionKingItems.ZEBRA_COOKED.get());
                output.accept(LionKingItems.RHINO_RAW.get());
                output.accept(LionKingItems.RHINO_COOKED.get());
                output.accept(LionKingItems.MANGO.get());
                output.accept(LionKingItems.BANANA.get());
                output.accept(LionKingItems.CORN.get());
                output.accept(LionKingItems.POPCORN.get());
                output.accept(LionKingItems.KIWANO.get());
                output.accept(LionKingItems.OUTLANDER_MEAT.get());
                output.accept(LionKingItems.CROCODILE_MEAT.get());
                output.accept(LionKingItems.ROAST_YAM.get());
                output.accept(LionKingItems.BANANA_BREAD.get());
                output.accept(LionKingItems.CHOCOLATE_MUFASA.get());
                output.accept(LionKingItems.BUG_STEW.get());
                output.accept(LionKingItems.EXPERIENCE_GRUB.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> MATERIALS_TAB =
            TABS.register("materials", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.materials"))
                    .icon(() -> new ItemStack(LionKingItems.HYENA_BONE.get()))
                    .displayItems((params, output) -> {
                        output.accept(LionKingItems.PRIDESTONE_ITEM.get());
                        output.accept(LionKingItems.CORRUPT_PRIDESTONE_ITEM.get());
                        output.accept(LionKingItems.SILVER_INGOT.get());
                        output.accept(LionKingItems.PEACOCK_GEM.get());
                        output.accept(LionKingItems.KIVULITE.get());
                        output.accept(LionKingItems.HYENA_BONE.get());
                        output.accept(LionKingItems.HYENA_BONE_SHARD.get());
                        output.accept(LionKingItems.TERMITE_DUST.get());
                        output.accept(LionKingItems.MANGO_DUST.get());
                        output.accept(LionKingItems.FEATHER_BLUE.get());
                        output.accept(LionKingItems.FEATHER_YELLOW.get());
                        output.accept(LionKingItems.FEATHER_RED.get());
                        output.accept(LionKingItems.FEATHER_BLACK.get());
                        output.accept(LionKingItems.FEATHER_PINK.get());
                        output.accept(LionKingItems.POISON.get());
                        output.accept(LionKingItems.NUKA_SHARD.get());
                        output.accept(LionKingItems.OUTLANDER_FUR.get());
                        output.accept(LionKingItems.ZEBRA_HIDE.get());
                        output.accept(LionKingItems.GEMSBOK_HIDE.get());
                        output.accept(LionKingItems.GEMSBOK_HORN.get());
                        output.accept(LionKingItems.RHINO_HORN.get());
                        output.accept(LionKingItems.GROUND_RHINO_HORN.get());
                        output.accept(LionKingItems.CORN_KERNELS.get());
                        output.accept(LionKingItems.DRIED_MAIZE.get());
                        output.accept(LionKingItems.LION_FUR.get());
                        output.accept(LionKingItems.BUG.get());
                        output.accept(LionKingItems.CRYSTAL.get());
                        // Notes
                        output.accept(LionKingItems.NOTE_A.get());
                        output.accept(LionKingItems.NOTE_B.get());
                        output.accept(LionKingItems.NOTE_C.get());
                        output.accept(LionKingItems.NOTE_D.get());
                        output.accept(LionKingItems.NOTE_E.get());
                        // Coins & feather
                        output.accept(LionKingItems.RAFIKI_COIN.get());
                        output.accept(LionKingItems.ZIRA_COIN.get());
                        output.accept(LionKingItems.WAYWARD_FEATHER.get());
                        // Jar items
                        output.accept(LionKingItems.JAR_EMPTY.get());
                        output.accept(LionKingItems.JAR_WATER.get());
                        output.accept(LionKingItems.JAR_MILK.get());
                        output.accept(LionKingItems.JAR_LAVA.get());
                        output.accept(LionKingItems.MANGO_JUICE.get());
                        output.accept(LionKingItems.HYENA_MEAL.get());
                        // Giraffe Ties
                        output.accept(LionKingItems.GIRAFFE_TIE.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_WHITE.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_BLUE.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_YELLOW.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_RED.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_PURPLE.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_GREEN.get());
                        output.accept(LionKingItems.GIRAFFE_TIE_BLACK.get());
                        // Musical Notes
                        output.accept(LionKingItems.NOTE_C.get());
                        output.accept(LionKingItems.NOTE_D.get());
                        output.accept(LionKingItems.NOTE_E.get());
                        output.accept(LionKingItems.NOTE_F.get());
                        output.accept(LionKingItems.NOTE_G.get());
                        output.accept(LionKingItems.NOTE_A.get());
                        output.accept(LionKingItems.NOTE_B.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TOOLS_TAB =
            TABS.register("tools", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.tools"))
                    .icon(() -> new ItemStack(LionKingItems.SILVER_PICKAXE.get()))
                    .displayItems((params, output) -> {
                        // Pridestone
                        output.accept(LionKingItems.PRIDESTONE_SWORD.get());
                        output.accept(LionKingItems.PRIDESTONE_PICKAXE.get());
                        output.accept(LionKingItems.PRIDESTONE_AXE.get());
                        output.accept(LionKingItems.PRIDESTONE_SHOVEL.get());
                        output.accept(LionKingItems.PRIDESTONE_HOE.get());
                        // Silver
                        output.accept(LionKingItems.SILVER_SWORD.get());
                        output.accept(LionKingItems.SILVER_PICKAXE.get());
                        output.accept(LionKingItems.SILVER_AXE.get());
                        output.accept(LionKingItems.SILVER_SHOVEL.get());
                        output.accept(LionKingItems.SILVER_HOE.get());
                        // Peacock
                        output.accept(LionKingItems.PEACOCK_SWORD.get());
                        output.accept(LionKingItems.PEACOCK_PICKAXE.get());
                        output.accept(LionKingItems.PEACOCK_AXE.get());
                        output.accept(LionKingItems.PEACOCK_SHOVEL.get());
                        output.accept(LionKingItems.PEACOCK_HOE.get());
                        // Kivulite
                        output.accept(LionKingItems.KIVULITE_SWORD.get());
                        output.accept(LionKingItems.KIVULITE_PICKAXE.get());
                        output.accept(LionKingItems.KIVULITE_AXE.get());
                        output.accept(LionKingItems.KIVULITE_SHOVEL.get());
                        output.accept(LionKingItems.KIVULITE_HOE.get());
                        // Corrupt
                        output.accept(LionKingItems.CORRUPT_SWORD.get());
                        output.accept(LionKingItems.CORRUPT_PICKAXE.get());
                        output.accept(LionKingItems.CORRUPT_AXE.get());
                        output.accept(LionKingItems.CORRUPT_SHOVEL.get());
                        output.accept(LionKingItems.CORRUPT_HOE.get());
                        // Special
                        output.accept(LionKingItems.TUNNAH_DIGGAH.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> COMBAT_TAB =
            TABS.register("combat", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.combat"))
                    .icon(() -> new ItemStack(LionKingItems.SILVER_SWORD.get()))
                    .displayItems((params, output) -> {
                        // Silver Armor
                        output.accept(LionKingItems.SILVER_HELMET.get());
                        output.accept(LionKingItems.SILVER_CHESTPLATE.get());
                        output.accept(LionKingItems.SILVER_LEGGINGS.get());
                        output.accept(LionKingItems.SILVER_BOOTS.get());
                        // Gemsbok Armor
                        output.accept(LionKingItems.GEMSBOK_HELMET.get());
                        output.accept(LionKingItems.GEMSBOK_CHESTPLATE.get());
                        output.accept(LionKingItems.GEMSBOK_LEGGINGS.get());
                        output.accept(LionKingItems.GEMSBOK_BOOTS.get());
                        // Peacock Armor
                        output.accept(LionKingItems.PEACOCK_HELMET.get());
                        output.accept(LionKingItems.PEACOCK_CHESTPLATE.get());
                        output.accept(LionKingItems.PEACOCK_LEGGINGS.get());
                        output.accept(LionKingItems.PEACOCK_BOOTS.get());
                        output.accept(LionKingItems.PEACOCK_WINGS.get());
                        // Outlands Armor
                        output.accept(LionKingItems.OUTLANDS_HELMET.get());
                        // Ticket Lion Suit
                        output.accept(LionKingItems.TICKET_LION_HEAD.get());
                        output.accept(LionKingItems.TICKET_LION_SUIT.get());
                        output.accept(LionKingItems.TICKET_LION_LEGS.get());
                        output.accept(LionKingItems.TICKET_LION_FEET.get());
                        // Darts
                        output.accept(LionKingItems.DART_BLUE.get());
                        output.accept(LionKingItems.DART_RED.get());
                        output.accept(LionKingItems.DART_YELLOW.get());
                        output.accept(LionKingItems.DART_PINK.get());
                        output.accept(LionKingItems.DART_BLACK.get());
                        output.accept(LionKingItems.DART_OUTLANDISH.get());
                        output.accept(LionKingItems.DART_SHOOTER.get());
                        output.accept(LionKingItems.DART_SHOOTER_SILVER.get());
                        // Spears & Bombs
                        output.accept(LionKingItems.GEMSBOK_SPEAR.get());
                        output.accept(LionKingItems.POISONED_SPEAR.get());
                        output.accept(LionKingItems.PUMBAA_BOMB.get());
                        output.accept(LionKingItems.TERMITE_THROWN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> MISC_TAB = TABS.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thelionking.misc"))
            .icon(() -> new ItemStack(LionKingItems.BUG.get()))
            .displayItems((params, output) -> {
                output.accept(LionKingItems.LION_SPAWN_EGG.get());
                output.accept(LionKingItems.LIONESS_SPAWN_EGG.get());
                output.accept(LionKingItems.ZEBRA_SPAWN_EGG.get());
                output.accept(LionKingItems.GIRAFFE_SPAWN_EGG.get());
                output.accept(LionKingItems.RHINO_SPAWN_EGG.get());
                output.accept(LionKingItems.GEMSBOK_SPAWN_EGG.get());
                output.accept(LionKingItems.DIKDIK_SPAWN_EGG.get());
                output.accept(LionKingItems.FLAMINGO_SPAWN_EGG.get());
                output.accept(LionKingItems.ZAZU_SPAWN_EGG.get());
                output.accept(LionKingItems.BUG_SPAWN_EGG.get());
                // Hostile
                output.accept(LionKingItems.HYENA_SPAWN_EGG.get());
                output.accept(LionKingItems.SKELETAL_HYENA_SPAWN_EGG.get());
                output.accept(LionKingItems.OUTLANDER_SPAWN_EGG.get());
                output.accept(LionKingItems.VULTURE_SPAWN_EGG.get());
                output.accept(LionKingItems.CROCODILE_SPAWN_EGG.get());
                output.accept(LionKingItems.TERMITE_SPAWN_EGG.get());
                // NPCs
                output.accept(LionKingItems.RAFIKI_SPAWN_EGG.get());
                output.accept(LionKingItems.SIMBA_SPAWN_EGG.get());
                output.accept(LionKingItems.TIMON_SPAWN_EGG.get());
                output.accept(LionKingItems.PUMBAA_SPAWN_EGG.get());
                output.accept(LionKingItems.SCAR_SPAWN_EGG.get());
                output.accept(LionKingItems.ZIRA_SPAWN_EGG.get());
                output.accept(LionKingItems.TICKET_LION_SPAWN_EGG.get());
                output.accept(LionKingItems.TERMITE_QUEEN_SPAWN_EGG.get());
                output.accept(LionKingItems.SKELETAL_HYENA_HEAD_SPAWN_EGG.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> QUEST_TAB =
            TABS.register("quest", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.quest"))
                    .icon(() -> new ItemStack(LionKingItems.QUEST_BOOK.get()))
                    .displayItems((params, output) -> {
                        output.accept(LionKingItems.QUEST_BOOK.get());
                        output.accept(LionKingItems.TICKET.get());
                        output.accept(LionKingItems.RHYTHM_STAFF.get());
                        output.accept(LionKingItems.RAFIKI_COIN.get());
                        output.accept(LionKingItems.ZIRA_COIN.get());
                        output.accept(LionKingItems.WAYWARD_FEATHER.get());
                        output.accept(LionKingItems.CRYSTAL.get());
                        // Quest items (Phase 10)
                        output.accept(LionKingItems.AMULET.get());
                        output.accept(LionKingItems.SIMBA_CHARM.get());
                        output.accept(LionKingItems.GIRAFFE_SADDLE.get());
                        output.accept(LionKingItems.DART_QUIVER.get());
                        output.accept(LionKingItems.PASSION_FRUIT.get());
                        output.accept(LionKingItems.ZAZU_EGG.get());
                        output.accept(LionKingItems.SCAR_RUG.get());
                        output.accept(LionKingItems.ZIRA_RUG.get());
                        output.accept(LionKingItems.RAFIKI_STICK.get());
                        output.accept(LionKingItems.RAFIKI_DUST.get());
                    })
                    .build());
}
