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
                        output.accept(Items.PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(Items.PRIDE_BRICK_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(Items.MOSSY_PRIDE_BRICK_ITEM.get());
                        output.accept(Items.MOSSY_CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(Items.PRIDE_PILLAR_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDE_PILLAR_ITEM.get());
                        output.accept(Items.PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(Items.PRIDESTONE_SLAB_ITEM.get());
                        output.accept(Items.PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(Items.PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDESTONE_SLAB_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(Items.PRIDESTONE_WALL_ITEM.get());
                        output.accept(Items.PRIDE_BRICK_WALL_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDESTONE_WALL_ITEM.get());
                        output.accept(Items.PRIDESTONE_PRESSURE_PLATE_ITEM.get());
                        output.accept(Items.PRIDESTONE_BUTTON_ITEM.get());
                        output.accept(Items.PRIDE_LEVER_ITEM.get());
                        output.accept(Items.PRIDE_COAL_ORE_ITEM.get());
                        output.accept(Items.SILVER_ORE_ITEM.get());
                        output.accept(Items.PEACOCK_ORE_ITEM.get());
                        output.accept(Items.SILVER_BLOCK_ITEM.get());
                        output.accept(Items.PEACOCK_BLOCK_ITEM.get());
                        output.accept(Items.OUTSAND_ITEM.get());
                        output.accept(Items.OUTGLASS_ITEM.get());
                        output.accept(Items.OUTGLASS_PANE_ITEM.get());
                        output.accept(Items.TERMITE_MOUND_ITEM.get());
                        output.accept(Items.PUMBAA_BOX_ITEM.get());
                        output.accept(Items.GRINDING_BOWL_ITEM.get());
                        output.accept(Items.BUG_TRAP_ITEM.get());
                        output.accept(Items.BONGO_DRUM_ITEM.get());
                        output.accept(Items.OUTLANDS_POOL_ITEM.get());
                        output.accept(Items.LK_SPAWNER_ITEM.get());
                        // Portal frames
                        output.accept(Items.PRIDE_PORTAL_FRAME_ITEM.get());
                        output.accept(Items.OUTLANDS_PORTAL_FRAME_ITEM.get());
                        // Phase 12 blocks
                        output.accept(Items.BANANA_CAKE_ITEM.get());
                        output.accept(Items.MOUNTED_SHOOTER_ITEM.get());
                        output.accept(Items.STAR_ALTAR_ITEM.get());
                        output.accept(Items.OUTLANDS_ALTAR_ITEM.get());
                        output.accept(Items.ZIRA_MOUND_GATE_ITEM.get());
                        output.accept(Items.TILLED_SAND_ITEM.get());
                        output.accept(Items.VASE_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DECO_TAB =
            TABS.register("decorations", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.decorations"))
                    .icon(() -> new ItemStack(LionKingBlocks.ACACIA_LOG.get()))
                    .displayItems((params, output) -> {
                        // Logs
                        output.accept(Items.ACACIA_LOG_ITEM.get());
                        output.accept(Items.RAINFOREST_LOG_ITEM.get());
                        output.accept(Items.MANGO_LOG_ITEM.get());
                        output.accept(Items.PASSION_LOG_ITEM.get());
                        output.accept(Items.BANANA_LOG_ITEM.get());
                        output.accept(Items.DEADWOOD_LOG_ITEM.get());
                        // Planks
                        output.accept(Items.ACACIA_PLANKS_ITEM.get());
                        output.accept(Items.RAINFOREST_PLANKS_ITEM.get());
                        output.accept(Items.MANGO_PLANKS_ITEM.get());
                        output.accept(Items.PASSION_PLANKS_ITEM.get());
                        output.accept(Items.BANANA_PLANKS_ITEM.get());
                        output.accept(Items.DEADWOOD_PLANKS_ITEM.get());
                        // Stairs
                        output.accept(Items.ACACIA_STAIRS_ITEM.get());
                        output.accept(Items.RAINFOREST_STAIRS_ITEM.get());
                        output.accept(Items.MANGO_STAIRS_ITEM.get());
                        output.accept(Items.PASSION_STAIRS_ITEM.get());
                        output.accept(Items.BANANA_STAIRS_ITEM.get());
                        output.accept(Items.DEADWOOD_STAIRS_ITEM.get());
                        // Slabs
                        output.accept(Items.ACACIA_SLAB_ITEM.get());
                        output.accept(Items.RAINFOREST_SLAB_ITEM.get());
                        output.accept(Items.MANGO_SLAB_ITEM.get());
                        output.accept(Items.PASSION_SLAB_ITEM.get());
                        output.accept(Items.BANANA_SLAB_ITEM.get());
                        output.accept(Items.DEADWOOD_SLAB_ITEM.get());
                        // Dried Maize
                        output.accept(Items.DRIED_MAIZE_BLOCK_ITEM.get());
                        output.accept(Items.DRIED_MAIZE_STAIRS_ITEM.get());
                        output.accept(Items.DRIED_MAIZE_SLAB_ITEM.get());
                        // Leaves
                        output.accept(Items.ACACIA_LEAVES_ITEM.get());
                        output.accept(Items.RAINFOREST_LEAVES_ITEM.get());
                        output.accept(Items.MANGO_LEAVES_ITEM.get());
                        output.accept(Items.PASSION_LEAVES_ITEM.get());
                        output.accept(Items.BANANA_LEAVES_ITEM.get());
                        output.accept(Items.RAFIKI_LEAVES_ITEM.get());
                        // Saplings
                        output.accept(Items.ACACIA_SAPLING_ITEM.get());
                        output.accept(Items.RAINFOREST_SAPLING_ITEM.get());
                        output.accept(Items.MANGO_SAPLING_ITEM.get());
                        output.accept(Items.PASSION_SAPLING_ITEM.get());
                        output.accept(Items.BANANA_SAPLING_ITEM.get());
                        // Rafiki Wood
                        output.accept(Items.RAFIKI_WOOD_ITEM.get());
                        // Flowers
                        output.accept(Items.WHITE_FLOWER_ITEM.get());
                        output.accept(Items.BLUE_FLOWER_ITEM.get());
                        output.accept(Items.PURPLE_FLOWER_ITEM.get());
                        output.accept(Items.RED_FLOWER_ITEM.get());
                        // Lilies
                        output.accept(Items.LILY_RED_ITEM.get());
                        output.accept(Items.LILY_VIOLET_ITEM.get());
                        output.accept(Items.LILY_WHITE_ITEM.get());
                        // Mushrooms
                        output.accept(Items.OUTSHROOM_ITEM.get());
                        output.accept(Items.OUTSHROOM_GLOWING_ITEM.get());
                        // Misc
                        output.accept(Items.ARID_GRASS_ITEM.get());
                        output.accept(Items.HYENA_TORCH_ITEM.get());
                        output.accept(Items.HANGING_BANANA_ITEM.get());
                        output.accept(Items.KIWANO_BLOCK_ITEM.get());
                        output.accept(Items.KIWANO_SEEDS.get());
                        output.accept(Items.YAM.get());
                        output.accept(Items.MAIZE_STALKS.get());
                        // Decorative block entities
                        output.accept(Items.HYENA_HEAD_ITEM.get());
                        output.accept(Items.FUR_RUG_ITEM.get());
                        // Bed
                        output.accept(Items.PRIDE_BED_ITEM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> FOOD_TAB = TABS.register("food", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thelionking.food"))
            .icon(() -> new ItemStack(Items.ZEBRA_RAW.get()))
            .displayItems((params, output) -> {
                output.accept(Items.LION_RAW.get());
                output.accept(Items.LION_COOKED.get());
                output.accept(Items.ZEBRA_RAW.get());
                output.accept(Items.ZEBRA_COOKED.get());
                output.accept(Items.RHINO_RAW.get());
                output.accept(Items.RHINO_COOKED.get());
                output.accept(Items.MANGO.get());
                output.accept(Items.BANANA.get());
                output.accept(Items.CORN.get());
                output.accept(Items.POPCORN.get());
                output.accept(Items.KIWANO.get());
                output.accept(Items.OUTLANDER_MEAT.get());
                output.accept(Items.CROCODILE_MEAT.get());
                output.accept(Items.ROAST_YAM.get());
                output.accept(Items.BANANA_BREAD.get());
                output.accept(Items.CHOCOLATE_MUFASA.get());
                output.accept(Items.BUG_STEW.get());
                output.accept(Items.EXPERIENCE_GRUB.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> MATERIALS_TAB =
            TABS.register("materials", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.materials"))
                    .icon(() -> new ItemStack(Items.HYENA_BONE.get()))
                    .displayItems((params, output) -> {
                        output.accept(Items.PRIDESTONE_ITEM.get());
                        output.accept(Items.CORRUPT_PRIDESTONE_ITEM.get());
                        output.accept(Items.SILVER_INGOT.get());
                        output.accept(Items.PEACOCK_GEM.get());
                        output.accept(Items.KIVULITE.get());
                        output.accept(Items.HYENA_BONE.get());
                        output.accept(Items.HYENA_BONE_SHARD.get());
                        output.accept(Items.TERMITE_DUST.get());
                        output.accept(Items.MANGO_DUST.get());
                        output.accept(Items.FEATHER_BLUE.get());
                        output.accept(Items.FEATHER_YELLOW.get());
                        output.accept(Items.FEATHER_RED.get());
                        output.accept(Items.FEATHER_BLACK.get());
                        output.accept(Items.FEATHER_PINK.get());
                        output.accept(Items.POISON.get());
                        output.accept(Items.NUKA_SHARD.get());
                        output.accept(Items.OUTLANDER_FUR.get());
                        output.accept(Items.ZEBRA_HIDE.get());
                        output.accept(Items.GEMSBOK_HIDE.get());
                        output.accept(Items.GEMSBOK_HORN.get());
                        output.accept(Items.RHINO_HORN.get());
                        output.accept(Items.GROUND_RHINO_HORN.get());
                        output.accept(Items.CORN_KERNELS.get());
                        output.accept(Items.DRIED_MAIZE.get());
                        output.accept(Items.LION_FUR.get());
                        output.accept(Items.BUG.get());
                        output.accept(Items.CRYSTAL.get());
                        // Notes
                        output.accept(Items.NOTE_A.get());
                        output.accept(Items.NOTE_B.get());
                        output.accept(Items.NOTE_C.get());
                        output.accept(Items.NOTE_D.get());
                        output.accept(Items.NOTE_E.get());
                        // Coins & feather
                        output.accept(Items.RAFIKI_COIN.get());
                        output.accept(Items.ZIRA_COIN.get());
                        output.accept(Items.WAYWARD_FEATHER.get());
                        // Jar items
                        output.accept(Items.JAR_EMPTY.get());
                        output.accept(Items.JAR_WATER.get());
                        output.accept(Items.JAR_MILK.get());
                        output.accept(Items.JAR_LAVA.get());
                        output.accept(Items.MANGO_JUICE.get());
                        output.accept(Items.HYENA_MEAL.get());
                        // Giraffe Ties
                        output.accept(Items.GIRAFFE_TIE.get());
                        output.accept(Items.GIRAFFE_TIE_WHITE.get());
                        output.accept(Items.GIRAFFE_TIE_BLUE.get());
                        output.accept(Items.GIRAFFE_TIE_YELLOW.get());
                        output.accept(Items.GIRAFFE_TIE_RED.get());
                        output.accept(Items.GIRAFFE_TIE_PURPLE.get());
                        output.accept(Items.GIRAFFE_TIE_GREEN.get());
                        output.accept(Items.GIRAFFE_TIE_BLACK.get());
                        // Musical Notes
                        output.accept(Items.NOTE_C.get());
                        output.accept(Items.NOTE_D.get());
                        output.accept(Items.NOTE_E.get());
                        output.accept(Items.NOTE_F.get());
                        output.accept(Items.NOTE_G.get());
                        output.accept(Items.NOTE_A.get());
                        output.accept(Items.NOTE_B.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TOOLS_TAB =
            TABS.register("tools", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.tools"))
                    .icon(() -> new ItemStack(Items.SILVER_PICKAXE.get()))
                    .displayItems((params, output) -> {
                        // Pridestone
                        output.accept(Items.PRIDESTONE_SWORD.get());
                        output.accept(Items.PRIDESTONE_PICKAXE.get());
                        output.accept(Items.PRIDESTONE_AXE.get());
                        output.accept(Items.PRIDESTONE_SHOVEL.get());
                        output.accept(Items.PRIDESTONE_HOE.get());
                        // Silver
                        output.accept(Items.SILVER_SWORD.get());
                        output.accept(Items.SILVER_PICKAXE.get());
                        output.accept(Items.SILVER_AXE.get());
                        output.accept(Items.SILVER_SHOVEL.get());
                        output.accept(Items.SILVER_HOE.get());
                        // Peacock
                        output.accept(Items.PEACOCK_SWORD.get());
                        output.accept(Items.PEACOCK_PICKAXE.get());
                        output.accept(Items.PEACOCK_AXE.get());
                        output.accept(Items.PEACOCK_SHOVEL.get());
                        output.accept(Items.PEACOCK_HOE.get());
                        // Kivulite
                        output.accept(Items.KIVULITE_SWORD.get());
                        output.accept(Items.KIVULITE_PICKAXE.get());
                        output.accept(Items.KIVULITE_AXE.get());
                        output.accept(Items.KIVULITE_SHOVEL.get());
                        output.accept(Items.KIVULITE_HOE.get());
                        // Corrupt
                        output.accept(Items.CORRUPT_SWORD.get());
                        output.accept(Items.CORRUPT_PICKAXE.get());
                        output.accept(Items.CORRUPT_AXE.get());
                        output.accept(Items.CORRUPT_SHOVEL.get());
                        output.accept(Items.CORRUPT_HOE.get());
                        // Special
                        output.accept(Items.TUNNAH_DIGGAH.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> COMBAT_TAB =
            TABS.register("combat", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.combat"))
                    .icon(() -> new ItemStack(Items.SILVER_SWORD.get()))
                    .displayItems((params, output) -> {
                        // Silver Armor
                        output.accept(Items.SILVER_HELMET.get());
                        output.accept(Items.SILVER_CHESTPLATE.get());
                        output.accept(Items.SILVER_LEGGINGS.get());
                        output.accept(Items.SILVER_BOOTS.get());
                        // Gemsbok Armor
                        output.accept(Items.GEMSBOK_HELMET.get());
                        output.accept(Items.GEMSBOK_CHESTPLATE.get());
                        output.accept(Items.GEMSBOK_LEGGINGS.get());
                        output.accept(Items.GEMSBOK_BOOTS.get());
                        // Peacock Armor
                        output.accept(Items.PEACOCK_HELMET.get());
                        output.accept(Items.PEACOCK_CHESTPLATE.get());
                        output.accept(Items.PEACOCK_LEGGINGS.get());
                        output.accept(Items.PEACOCK_BOOTS.get());
                        output.accept(Items.PEACOCK_WINGS.get());
                        // Outlands Armor
                        output.accept(Items.OUTLANDS_HELMET.get());
                        // Ticket Lion Suit
                        output.accept(Items.TICKET_LION_HEAD.get());
                        output.accept(Items.TICKET_LION_SUIT.get());
                        output.accept(Items.TICKET_LION_LEGS.get());
                        output.accept(Items.TICKET_LION_FEET.get());
                        // Darts
                        output.accept(Items.DART_BLUE.get());
                        output.accept(Items.DART_RED.get());
                        output.accept(Items.DART_YELLOW.get());
                        output.accept(Items.DART_PINK.get());
                        output.accept(Items.DART_BLACK.get());
                        output.accept(Items.DART_OUTLANDISH.get());
                        output.accept(Items.DART_SHOOTER.get());
                        output.accept(Items.DART_SHOOTER_SILVER.get());
                        // Spears & Bombs
                        output.accept(Items.GEMSBOK_SPEAR.get());
                        output.accept(Items.POISONED_SPEAR.get());
                        output.accept(Items.PUMBAA_BOMB.get());
                        output.accept(Items.TERMITE_THROWN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> MISC_TAB = TABS.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thelionking.misc"))
            .icon(() -> new ItemStack(Items.BUG.get()))
            .displayItems((params, output) -> {
                output.accept(Items.LION_SPAWN_EGG.get());
                output.accept(Items.LIONESS_SPAWN_EGG.get());
                output.accept(Items.ZEBRA_SPAWN_EGG.get());
                output.accept(Items.GIRAFFE_SPAWN_EGG.get());
                output.accept(Items.RHINO_SPAWN_EGG.get());
                output.accept(Items.GEMSBOK_SPAWN_EGG.get());
                output.accept(Items.DIKDIK_SPAWN_EGG.get());
                output.accept(Items.FLAMINGO_SPAWN_EGG.get());
                output.accept(Items.ZAZU_SPAWN_EGG.get());
                output.accept(Items.BUG_SPAWN_EGG.get());
                // Hostile
                output.accept(Items.HYENA_SPAWN_EGG.get());
                output.accept(Items.SKELETAL_HYENA_SPAWN_EGG.get());
                output.accept(Items.OUTLANDER_SPAWN_EGG.get());
                output.accept(Items.VULTURE_SPAWN_EGG.get());
                output.accept(Items.CROCODILE_SPAWN_EGG.get());
                output.accept(Items.TERMITE_SPAWN_EGG.get());
                // NPCs
                output.accept(Items.RAFIKI_SPAWN_EGG.get());
                output.accept(Items.SIMBA_SPAWN_EGG.get());
                output.accept(Items.TIMON_SPAWN_EGG.get());
                output.accept(Items.PUMBAA_SPAWN_EGG.get());
                output.accept(Items.SCAR_SPAWN_EGG.get());
                output.accept(Items.ZIRA_SPAWN_EGG.get());
                output.accept(Items.TICKET_LION_SPAWN_EGG.get());
                output.accept(Items.TERMITE_QUEEN_SPAWN_EGG.get());
                output.accept(Items.SKELETAL_HYENA_HEAD_SPAWN_EGG.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> QUEST_TAB =
            TABS.register("quest", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.quest"))
                    .icon(() -> new ItemStack(Items.QUEST_BOOK.get()))
                    .displayItems((params, output) -> {
                        output.accept(Items.QUEST_BOOK.get());
                        output.accept(Items.TICKET.get());
                        output.accept(Items.RHYTHM_STAFF.get());
                        output.accept(Items.RAFIKI_COIN.get());
                        output.accept(Items.ZIRA_COIN.get());
                        output.accept(Items.WAYWARD_FEATHER.get());
                        output.accept(Items.CRYSTAL.get());
                        // Quest items (Phase 10)
                        output.accept(Items.AMULET.get());
                        output.accept(Items.SIMBA_CHARM.get());
                        output.accept(Items.GIRAFFE_SADDLE.get());
                        output.accept(Items.DART_QUIVER.get());
                        output.accept(Items.PASSION_FRUIT.get());
                        output.accept(Items.ZAZU_EGG.get());
                        output.accept(Items.SCAR_RUG.get());
                        output.accept(Items.ZIRA_RUG.get());
                        output.accept(Items.RAFIKI_STICK.get());
                        output.accept(Items.RAFIKI_DUST.get());
                    })
                    .build());
}
