package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LKCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheLionKingMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCKS_TAB = TABS.register("blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.blocks"))
                    .icon(() -> new ItemStack(LKBlocks.PRIDE_BRICK.get()))
                    .displayItems((params, output) -> {
                        output.accept(LKItems.PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDESTONE_BLOCK_ITEM.get());
                        output.accept(LKItems.PRIDE_BRICK_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(LKItems.MOSSY_PRIDE_BRICK_ITEM.get());
                        output.accept(LKItems.MOSSY_CORRUPT_PRIDE_BRICK_ITEM.get());
                        output.accept(LKItems.PRIDE_PILLAR_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDE_PILLAR_ITEM.get());
                        output.accept(LKItems.PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(LKItems.PRIDESTONE_SLAB_ITEM.get());
                        output.accept(LKItems.PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(LKItems.PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDESTONE_STAIRS_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDESTONE_SLAB_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDE_BRICK_STAIRS_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDE_BRICK_SLAB_ITEM.get());
                        output.accept(LKItems.PRIDESTONE_WALL_ITEM.get());
                        output.accept(LKItems.PRIDE_BRICK_WALL_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDESTONE_WALL_ITEM.get());
                        output.accept(LKItems.PRIDESTONE_PRESSURE_PLATE_ITEM.get());
                        output.accept(LKItems.PRIDESTONE_BUTTON_ITEM.get());
                        output.accept(LKItems.PRIDE_LEVER_ITEM.get());
                        output.accept(LKItems.PRIDE_COAL_ORE_ITEM.get());
                        output.accept(LKItems.SILVER_ORE_ITEM.get());
                        output.accept(LKItems.PEACOCK_ORE_ITEM.get());
                        output.accept(LKItems.SILVER_BLOCK_ITEM.get());
                        output.accept(LKItems.PEACOCK_BLOCK_ITEM.get());
                        output.accept(LKItems.OUTSAND_ITEM.get());
                        output.accept(LKItems.OUTGLASS_ITEM.get());
                        output.accept(LKItems.OUTGLASS_PANE_ITEM.get());
                        output.accept(LKItems.TERMITE_MOUND_ITEM.get());
                        output.accept(LKItems.PUMBAA_BOX_ITEM.get());
                        output.accept(LKItems.GRINDING_BOWL_ITEM.get());
                        output.accept(LKItems.BUG_TRAP_ITEM.get());
                        output.accept(LKItems.BONGO_DRUM_ITEM.get());
                        output.accept(LKItems.OUTLANDS_POOL_ITEM.get());
                        output.accept(LKItems.LK_SPAWNER_ITEM.get());
                        // Portal frames
                        output.accept(LKItems.PRIDE_PORTAL_FRAME_ITEM.get());
                        output.accept(LKItems.OUTLANDS_PORTAL_FRAME_ITEM.get());
                        // Phase 12 blocks
                        output.accept(LKItems.BANANA_CAKE_ITEM.get());
                        output.accept(LKItems.MOUNTED_SHOOTER_ITEM.get());
                        output.accept(LKItems.STAR_ALTAR_ITEM.get());
                        output.accept(LKItems.OUTLANDS_ALTAR_ITEM.get());
                        output.accept(LKItems.TILLED_SAND_ITEM.get());
                        output.accept(LKItems.VASE_ITEM.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> DECO_TAB = TABS.register("decorations",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.decorations"))
                    .icon(() -> new ItemStack(LKBlocks.ACACIA_LOG.get()))
                    .displayItems((params, output) -> {
                        // Logs
                        output.accept(LKItems.ACACIA_LOG_ITEM.get());
                        output.accept(LKItems.RAINFOREST_LOG_ITEM.get());
                        output.accept(LKItems.MANGO_LOG_ITEM.get());
                        output.accept(LKItems.PASSION_LOG_ITEM.get());
                        output.accept(LKItems.BANANA_LOG_ITEM.get());
                        output.accept(LKItems.DEADWOOD_LOG_ITEM.get());
                        // Planks
                        output.accept(LKItems.ACACIA_PLANKS_ITEM.get());
                        output.accept(LKItems.RAINFOREST_PLANKS_ITEM.get());
                        output.accept(LKItems.MANGO_PLANKS_ITEM.get());
                        output.accept(LKItems.PASSION_PLANKS_ITEM.get());
                        output.accept(LKItems.BANANA_PLANKS_ITEM.get());
                        output.accept(LKItems.DEADWOOD_PLANKS_ITEM.get());
                        // Stairs
                        output.accept(LKItems.ACACIA_STAIRS_ITEM.get());
                        output.accept(LKItems.RAINFOREST_STAIRS_ITEM.get());
                        output.accept(LKItems.MANGO_STAIRS_ITEM.get());
                        output.accept(LKItems.PASSION_STAIRS_ITEM.get());
                        output.accept(LKItems.BANANA_STAIRS_ITEM.get());
                        output.accept(LKItems.DEADWOOD_STAIRS_ITEM.get());
                        // Slabs
                        output.accept(LKItems.ACACIA_SLAB_ITEM.get());
                        output.accept(LKItems.RAINFOREST_SLAB_ITEM.get());
                        output.accept(LKItems.MANGO_SLAB_ITEM.get());
                        output.accept(LKItems.PASSION_SLAB_ITEM.get());
                        output.accept(LKItems.BANANA_SLAB_ITEM.get());
                        output.accept(LKItems.DEADWOOD_SLAB_ITEM.get());
                        // Dried Maize
                        output.accept(LKItems.DRIED_MAIZE_BLOCK_ITEM.get());
                        output.accept(LKItems.DRIED_MAIZE_STAIRS_ITEM.get());
                        output.accept(LKItems.DRIED_MAIZE_SLAB_ITEM.get());
                        // Leaves
                        output.accept(LKItems.ACACIA_LEAVES_ITEM.get());
                        output.accept(LKItems.RAINFOREST_LEAVES_ITEM.get());
                        output.accept(LKItems.MANGO_LEAVES_ITEM.get());
                        output.accept(LKItems.PASSION_LEAVES_ITEM.get());
                        output.accept(LKItems.BANANA_LEAVES_ITEM.get());
                        output.accept(LKItems.RAFIKI_LEAVES_ITEM.get());
                        // Saplings
                        output.accept(LKItems.ACACIA_SAPLING_ITEM.get());
                        output.accept(LKItems.RAINFOREST_SAPLING_ITEM.get());
                        output.accept(LKItems.MANGO_SAPLING_ITEM.get());
                        output.accept(LKItems.PASSION_SAPLING_ITEM.get());
                        output.accept(LKItems.BANANA_SAPLING_ITEM.get());
                        // Rafiki Wood
                        output.accept(LKItems.RAFIKI_WOOD_ITEM.get());
                        // Flowers
                        output.accept(LKItems.WHITE_FLOWER_ITEM.get());
                        output.accept(LKItems.BLUE_FLOWER_ITEM.get());
                        output.accept(LKItems.PURPLE_FLOWER_ITEM.get());
                        output.accept(LKItems.RED_FLOWER_ITEM.get());
                        // Lilies
                        output.accept(LKItems.RED_LILY_ITEM.get());
                        output.accept(LKItems.VIOLET_LILY_ITEM.get());
                        output.accept(LKItems.WHITE_LILY_ITEM.get());
                        // Mushrooms
                        output.accept(LKItems.OUTSHROOM_ITEM.get());
                        output.accept(LKItems.OUTSHROOM_GLOWING_ITEM.get());
                        // Misc
                        output.accept(LKItems.ARID_GRASS_ITEM.get());
                        output.accept(LKItems.HYENA_TORCH_ITEM.get());
                        output.accept(LKItems.HANGING_BANANA_ITEM.get());
                        output.accept(LKItems.KIWANO_BLOCK_ITEM.get());
                        output.accept(LKItems.KIWANO_SEEDS.get());
                        output.accept(LKItems.YAM.get());
                        output.accept(LKItems.MAIZE_STALKS.get());
                        // Decorative block entities
                        output.accept(LKItems.HYENA_HEAD_ITEM.get());
                        output.accept(LKItems.FUR_RUG_ITEM.get());
                        // Bed
                        output.accept(LKItems.PRIDE_BED_ITEM.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> FOOD_TAB = TABS.register("food",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.food"))
                    .icon(() -> new ItemStack(LKItems.ZEBRA_RAW.get()))
                    .displayItems((params, output) -> {
                        output.accept(LKItems.LION_RAW.get());
                        output.accept(LKItems.LION_COOKED.get());
                        output.accept(LKItems.ZEBRA_RAW.get());
                        output.accept(LKItems.ZEBRA_COOKED.get());
                        output.accept(LKItems.RHINO_RAW.get());
                        output.accept(LKItems.RHINO_COOKED.get());
                        output.accept(LKItems.MANGO.get());
                        output.accept(LKItems.BANANA.get());
                        output.accept(LKItems.CORN.get());
                        output.accept(LKItems.POPCORN.get());
                        output.accept(LKItems.KIWANO.get());
                        output.accept(LKItems.OUTLANDER_MEAT.get());
                        output.accept(LKItems.CROCODILE_MEAT.get());
                        output.accept(LKItems.ROAST_YAM.get());
                        output.accept(LKItems.BANANA_BREAD.get());
                        output.accept(LKItems.CHOCOLATE_MUFASA.get());
                        output.accept(LKItems.BUG_STEW.get());
                        output.accept(LKItems.EXPERIENCE_GRUB.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> MATERIALS_TAB = TABS.register("materials",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.materials"))
                    .icon(() -> new ItemStack(LKItems.HYENA_BONE.get()))
                    .displayItems((params, output) -> {
                        output.accept(LKItems.PRIDESTONE_ITEM.get());
                        output.accept(LKItems.CORRUPT_PRIDESTONE_ITEM.get());
                        output.accept(LKItems.SILVER_INGOT.get());
                        output.accept(LKItems.PEACOCK_GEM.get());
                        output.accept(LKItems.KIVULITE.get());
                        output.accept(LKItems.HYENA_BONE.get());
                        output.accept(LKItems.HYENA_BONE_SHARD.get());
                        output.accept(LKItems.TERMITE_DUST.get());
                        output.accept(LKItems.MANGO_DUST.get());
                        output.accept(LKItems.FEATHER_BLUE.get());
                        output.accept(LKItems.FEATHER_YELLOW.get());
                        output.accept(LKItems.FEATHER_RED.get());
                        output.accept(LKItems.FEATHER_BLACK.get());
                        output.accept(LKItems.FEATHER_PINK.get());
                        output.accept(LKItems.POISON.get());
                        output.accept(LKItems.NUKA_SHARD.get());
                        output.accept(LKItems.OUTLANDER_FUR.get());
                        output.accept(LKItems.ZEBRA_HIDE.get());
                        output.accept(LKItems.GEMSBOK_HIDE.get());
                        output.accept(LKItems.GEMSBOK_HORN.get());
                        output.accept(LKItems.RHINO_HORN.get());
                        output.accept(LKItems.GROUND_RHINO_HORN.get());
                        output.accept(LKItems.CORN_KERNELS.get());
                        output.accept(LKItems.DRIED_MAIZE.get());
                        output.accept(LKItems.LION_FUR.get());
                        output.accept(LKItems.BUG.get());
                        output.accept(LKItems.CRYSTAL.get());
                        // Notes
                        output.accept(LKItems.NOTE_A.get());
                        output.accept(LKItems.NOTE_B.get());
                        output.accept(LKItems.NOTE_C.get());
                        output.accept(LKItems.NOTE_D.get());
                        output.accept(LKItems.NOTE_E.get());
                        // Coins & feather
                        output.accept(LKItems.RAFIKI_COIN.get());
                        output.accept(LKItems.ZIRA_COIN.get());
                        output.accept(LKItems.WAYWARD_FEATHER.get());
                        // Jar items
                        output.accept(LKItems.JAR_EMPTY.get());
                        output.accept(LKItems.JAR_WATER.get());
                        output.accept(LKItems.JAR_MILK.get());
                        output.accept(LKItems.JAR_LAVA.get());
                        output.accept(LKItems.MANGO_JUICE.get());
                        output.accept(LKItems.HYENA_MEAL.get());
                        // Giraffe Ties
                        output.accept(LKItems.GIRAFFE_TIE.get());
                        output.accept(LKItems.GIRAFFE_TIE_WHITE.get());
                        output.accept(LKItems.GIRAFFE_TIE_BLUE.get());
                        output.accept(LKItems.GIRAFFE_TIE_YELLOW.get());
                        output.accept(LKItems.GIRAFFE_TIE_RED.get());
                        output.accept(LKItems.GIRAFFE_TIE_PURPLE.get());
                        output.accept(LKItems.GIRAFFE_TIE_GREEN.get());
                        output.accept(LKItems.GIRAFFE_TIE_BLACK.get());
                        // Musical Notes
                        output.accept(LKItems.NOTE_C.get());
                        output.accept(LKItems.NOTE_D.get());
                        output.accept(LKItems.NOTE_E.get());
                        output.accept(LKItems.NOTE_F.get());
                        output.accept(LKItems.NOTE_G.get());
                        output.accept(LKItems.NOTE_A.get());
                        output.accept(LKItems.NOTE_B.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> TOOLS_TAB = TABS.register("tools",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.tools"))
                    .icon(() -> new ItemStack(LKItems.SILVER_PICKAXE.get()))
                    .displayItems((params, output) -> {
                        // Pridestone
                        output.accept(LKItems.PRIDESTONE_SWORD.get());
                        output.accept(LKItems.PRIDESTONE_PICKAXE.get());
                        output.accept(LKItems.PRIDESTONE_AXE.get());
                        output.accept(LKItems.PRIDESTONE_SHOVEL.get());
                        output.accept(LKItems.PRIDESTONE_HOE.get());
                        // Silver
                        output.accept(LKItems.SILVER_SWORD.get());
                        output.accept(LKItems.SILVER_PICKAXE.get());
                        output.accept(LKItems.SILVER_AXE.get());
                        output.accept(LKItems.SILVER_SHOVEL.get());
                        output.accept(LKItems.SILVER_HOE.get());
                        // Peacock
                        output.accept(LKItems.PEACOCK_SWORD.get());
                        output.accept(LKItems.PEACOCK_PICKAXE.get());
                        output.accept(LKItems.PEACOCK_AXE.get());
                        output.accept(LKItems.PEACOCK_SHOVEL.get());
                        output.accept(LKItems.PEACOCK_HOE.get());
                        // Kivulite
                        output.accept(LKItems.KIVULITE_SWORD.get());
                        output.accept(LKItems.KIVULITE_PICKAXE.get());
                        output.accept(LKItems.KIVULITE_AXE.get());
                        output.accept(LKItems.KIVULITE_SHOVEL.get());
                        output.accept(LKItems.KIVULITE_HOE.get());
                        // Corrupt
                        output.accept(LKItems.CORRUPT_SWORD.get());
                        output.accept(LKItems.CORRUPT_PICKAXE.get());
                        output.accept(LKItems.CORRUPT_AXE.get());
                        output.accept(LKItems.CORRUPT_SHOVEL.get());
                        output.accept(LKItems.CORRUPT_HOE.get());
                        // Special
                        output.accept(LKItems.TUNNAH_DIGGAH.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> COMBAT_TAB = TABS.register("combat",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.combat"))
                    .icon(() -> new ItemStack(LKItems.SILVER_SWORD.get()))
                    .displayItems((params, output) -> {
                        // Silver Armor
                        output.accept(LKItems.SILVER_HELMET.get());
                        output.accept(LKItems.SILVER_CHESTPLATE.get());
                        output.accept(LKItems.SILVER_LEGGINGS.get());
                        output.accept(LKItems.SILVER_BOOTS.get());
                        // Gemsbok Armor
                        output.accept(LKItems.GEMSBOK_HELMET.get());
                        output.accept(LKItems.GEMSBOK_CHESTPLATE.get());
                        output.accept(LKItems.GEMSBOK_LEGGINGS.get());
                        output.accept(LKItems.GEMSBOK_BOOTS.get());
                        // Peacock Armor
                        output.accept(LKItems.PEACOCK_HELMET.get());
                        output.accept(LKItems.PEACOCK_CHESTPLATE.get());
                        output.accept(LKItems.PEACOCK_LEGGINGS.get());
                        output.accept(LKItems.PEACOCK_BOOTS.get());
                        // Outlands Armor
                        output.accept(LKItems.OUTLANDS_HELMET.get());
                        // Ticket Lion Suit
                        output.accept(LKItems.TICKET_LION_HEAD.get());
                        output.accept(LKItems.TICKET_LION_SUIT.get());
                        output.accept(LKItems.TICKET_LION_LEGS.get());
                        output.accept(LKItems.TICKET_LION_FEET.get());
                        // Darts
                        output.accept(LKItems.DART_BLUE.get());
                        output.accept(LKItems.DART_RED.get());
                        output.accept(LKItems.DART_YELLOW.get());
                        output.accept(LKItems.DART_PINK.get());
                        output.accept(LKItems.DART_BLACK.get());
                        output.accept(LKItems.DART_SHOOTER.get());
                        output.accept(LKItems.DART_SHOOTER_SILVER.get());
                        // Spears & Bombs
                        output.accept(LKItems.GEMSBOK_SPEAR.get());
                        output.accept(LKItems.POISONED_SPEAR.get());
                        output.accept(LKItems.PUMBAA_BOMB.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> MISC_TAB = TABS.register("misc",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.misc"))
                    .icon(() -> new ItemStack(LKItems.BUG.get()))
                    .displayItems((params, output) -> {
                        output.accept(LKItems.LION_SPAWN_EGG.get());
                        output.accept(LKItems.LIONESS_SPAWN_EGG.get());
                        output.accept(LKItems.ZEBRA_SPAWN_EGG.get());
                        output.accept(LKItems.GIRAFFE_SPAWN_EGG.get());
                        output.accept(LKItems.RHINO_SPAWN_EGG.get());
                        output.accept(LKItems.GEMSBOK_SPAWN_EGG.get());
                        output.accept(LKItems.DIKDIK_SPAWN_EGG.get());
                        output.accept(LKItems.FLAMINGO_SPAWN_EGG.get());
                        output.accept(LKItems.ZAZU_SPAWN_EGG.get());
                        output.accept(LKItems.BUG_SPAWN_EGG.get());
                        // Hostile
                        output.accept(LKItems.HYENA_SPAWN_EGG.get());
                        output.accept(LKItems.SKELETAL_HYENA_SPAWN_EGG.get());
                        output.accept(LKItems.OUTLANDER_SPAWN_EGG.get());
                        output.accept(LKItems.OUTLANDESS_SPAWN_EGG.get());
                        output.accept(LKItems.VULTURE_SPAWN_EGG.get());
                        output.accept(LKItems.CROCODILE_SPAWN_EGG.get());
                        output.accept(LKItems.TERMITE_SPAWN_EGG.get());
                        // NPCs
                        output.accept(LKItems.RAFIKI_SPAWN_EGG.get());
                        output.accept(LKItems.SIMBA_SPAWN_EGG.get());
                        output.accept(LKItems.TIMON_SPAWN_EGG.get());
                        output.accept(LKItems.PUMBAA_SPAWN_EGG.get());
                        output.accept(LKItems.SCAR_SPAWN_EGG.get());
                        output.accept(LKItems.ZIRA_SPAWN_EGG.get());
                        output.accept(LKItems.TICKET_LION_SPAWN_EGG.get());
                        output.accept(LKItems.TERMITE_QUEEN_SPAWN_EGG.get());
                        output.accept(LKItems.SKELETAL_HYENA_HEAD_SPAWN_EGG.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> QUEST_TAB = TABS.register("quest",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thelionking.quest"))
                    .icon(() -> new ItemStack(LKItems.QUEST_BOOK.get()))
                    .displayItems((params, output) -> {
                        output.accept(LKItems.QUEST_BOOK.get());
                        output.accept(LKItems.TICKET.get());
                        output.accept(LKItems.RHYTHM_STAFF.get());
                        output.accept(LKItems.RAFIKI_COIN.get());
                        output.accept(LKItems.ZIRA_COIN.get());
                        output.accept(LKItems.WAYWARD_FEATHER.get());
                        output.accept(LKItems.CRYSTAL.get());
                        // Quest items (Phase 10)
                        output.accept(LKItems.AMULET.get());
                        output.accept(LKItems.SIMBA_CHARM.get());
                        output.accept(LKItems.GIRAFFE_SADDLE.get());
                        output.accept(LKItems.DART_QUIVER.get());
                        output.accept(LKItems.PASSION_FRUIT.get());
                        output.accept(LKItems.ZAZU_EGG.get());
                        output.accept(LKItems.SCAR_RUG.get());
                        output.accept(LKItems.ZIRA_RUG.get());
                        output.accept(LKItems.RAFIKI_STICK.get());
                        output.accept(LKItems.RAFIKI_DUST.get());
                    }).build());

}
