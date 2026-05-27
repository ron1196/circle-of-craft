package io.github.ron1196.circleofcraft.data;

import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

/**
 * Per-item lore/description shown in the quest book's item-inspector.
 * Ported from the old mod's {@code LKItemInfo} static map. Entries are registered lazily on first
 * lookup to avoid races with item registration during mod init.
 */
public final class ItemInfo {

    private static final Map<Item, String[]> INFO = new HashMap<>();
    private static volatile boolean initialized = false;

    private ItemInfo() {}

    public static String[] get(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        ensureInit();
        return INFO.get(stack.getItem());
    }

    private static void ensureInit() {
        if (initialized) return;
        synchronized (ItemInfo.class) {
            if (initialized) return;
            populate();
            initialized = true;
        }
    }

    private static void put(RegistryObject<? extends Item> reg, String... lines) {
        Item item = reg.get();
        if (item != null) INFO.put(item, lines);
    }

    private static void putItem(Supplier<? extends Item> reg, String... lines) {
        Item item = reg.get();
        if (item != null) INFO.put(item, lines);
    }

    private static void populate() {
        // Materials and drops
        put(
                ModItems.LION_FUR,
                "Dropped by Lions and Lionesses.",
                "",
                "Used in crafting beds and various",
                "other items.");
        put(
                ModItems.HYENA_BONE,
                "Dropped by Hyenas.",
                "",
                "Used in crafting recipes,",
                "grinding, and breeding lions.");
        put(
                ModItems.HYENA_BONE_SHARD,
                "Made by placing Hyena Bones in",
                "the Grinding Bowl.",
                "",
                "Used in crafting blue, yellow",
                "and red darts.");
        put(ModItems.OUTLANDER_FUR, "Dropped by Outlanders.", "", "Can be crafted into Fur Rugs.");
        put(ModItems.ZEBRA_HIDE, "Dropped by Zebras.", "", "Used to craft Zebra Boots and", "a few other items.");
        put(
                ModItems.GEMSBOK_HIDE,
                "Dropped by Gemsboks.",
                "",
                "Used in crafting Gemsbok armour",
                "and Giraffe Saddles.");
        put(
                ModItems.GEMSBOK_HORN,
                "Dropped by Gemsboks.",
                "",
                "Used in crafting Gemsbok Spears",
                "and the Rhythm Staff.");
        put(ModItems.RHINO_HORN, "Dropped by Rhinos.", "", "Can be ground down in the", "Grinding Bowl.");
        put(
                ModItems.GROUND_RHINO_HORN,
                "Made by placing Rhino Horn",
                "in the Grinding Bowl.",
                "",
                "Can be fed to breeding animals",
                "to make them produce more",
                "offspring, and can also dye rugs.");
        put(
                ModItems.NUKA_SHARD,
                "Dropped by Nuka Ore.",
                "",
                "Can be used as furnace fuel,",
                "ground down into poison, or",
                "crafted with an Outshroom to",
                "make it glow.");
        put(
                ModItems.MANGO_DUST,
                "Made by placing mangoes in",
                "the Grinding Bowl.",
                "",
                "Used in crafting Dart Shooters",
                "and can also dye rugs.");
        put(
                ModItems.TERMITE_DUST,
                "Made by placing Exploding Termites",
                "in the Grinding Bowl.",
                "",
                "Can be used to dye rugs or in",
                "place of gunpowder to craft TNT.");
        put(
                ModItems.RAFIKI_DUST,
                "Obtained from Rafiki in exchange",
                "for one Ground Mango and one",
                "Ground Termite.",
                "",
                "Can be used on a Star Altar to",
                "summon Simba, or crafted with",
                "four silver ingots into an",
                "Astral Charm.");
        put(
                ModItems.POISON,
                "Made by placing Nuka Shards in",
                "the Grinding Bowl.",
                "",
                "Two Poison Powders can be",
                "applied to a Gemsbok Spear to",
                "give it a poison effect.");
        put(
                ModItems.SILVER_INGOT,
                "Made by smelting Silver Ore in",
                "a furnace.",
                "",
                "Used to make tools, armour",
                "and various other items.",
                "Rafiki will exchange three silver",
                "ingots for a Rafiki Coin.");
        put(
                ModItems.PEACOCK_GEM,
                "Made by smelting Peacock Ore in",
                "a furnace.",
                "",
                "Used to make tools, armour",
                "and other items.");
        put(
                ModItems.KIVULITE,
                "Made by smelting Kivulite Ore in",
                "a furnace.",
                "",
                "Used to make tools with special",
                "fire-based abilities.");

        // Feathers
        put(ModItems.FEATHER_BLUE, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
        put(ModItems.FEATHER_YELLOW, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
        put(ModItems.FEATHER_RED, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
        put(
                ModItems.FEATHER_BLACK,
                "Dropped by Vultures.",
                "",
                "Can be crafted into Outlandish",
                "Darts, or ground into Rug Dye.");
        put(
                ModItems.FEATHER_FLAMINGO,
                "Dropped by Flamingos.",
                "",
                "Can be crafted into Flamingo",
                "Darts, or ground into Rug Dye.");

        // Foods
        put(ModItems.LION_RAW, "Dropped by Lions and Lionesses.", "", "Can be cooked to restore", "more hunger.");
        put(ModItems.LION_COOKED, "Can be eaten to restore hunger.");
        put(ModItems.ZEBRA_RAW, "Dropped by Zebras.", "", "Can be cooked to restore", "more hunger.");
        put(ModItems.ZEBRA_COOKED, "Can be eaten to restore hunger.");
        put(ModItems.RHINO_RAW, "Dropped by Rhinos.", "", "Can be cooked to restore", "more hunger.");
        put(ModItems.RHINO_COOKED, "Can be eaten to restore hunger.");
        put(
                ModItems.OUTLANDER_MEAT,
                "Dropped by Outlanders.",
                "",
                "Can be cooked to restore",
                "more hunger and remove the",
                "possibility of food poisoning.");
        put(
                ModItems.CROCODILE_MEAT,
                "Dropped by Crocodiles.",
                "",
                "Can be eaten to restore hunger.",
                "Has a chance to cause",
                "food poisoning.");
        put(
                ModItems.MANGO,
                "Drops from Mango Trees.",
                "",
                "Can be eaten to restore hunger,",
                "used in food recipes, or ground",
                "down in the Grinding Bowl.");
        put(
                ModItems.BANANA,
                "Grows on Banana Trees.",
                "",
                "Can be eaten to restore hunger,",
                "or used in food recipes.");
        put(
                ModItems.PASSION_FRUIT,
                "Drops from Passion Fruit Trees.",
                "",
                "Can be eaten when in full health",
                "to travel to the Upendi realm.");
        put(
                ModItems.KIWANO,
                "Dropped by Kiwano fruit.",
                "",
                "Can be eaten to restore hunger,",
                "or crafted into seeds.");
        put(ModItems.KIWANO_SEEDS, "Can be placed on Tilled Sand", "to grow a Kiwano.");
        put(
                ModItems.YAM,
                "A vegetable found growing in",
                "savannah biomes.",
                "",
                "Can be farmed, and roasted to",
                "restore more hunger. If eaten",
                "raw, it may cause food poisoning.");
        put(ModItems.ROAST_YAM, "Can be eaten to restore hunger.");
        put(
                ModItems.CORN,
                "Picked from Maize Stalks.",
                "",
                "Can be eaten, cooked, and used",
                "as an alternative to wheat for",
                "breeding Pride Lands animals.");
        put(ModItems.CORN_KERNELS, "Can be used as an alternative to", "seeds for breeding Zazus.");
        put(ModItems.POPCORN, "Can be eaten to restore hunger.");
        put(ModItems.BANANA_BREAD, "Can be eaten to restore hunger.");
        put(
                ModItems.CHOCOLATE_MUFASA,
                "Found in Pride Lands dungeons.",
                "",
                "Can be eaten to restore a lot",
                "of hunger.");
        put(
                ModItems.BUG_STEW,
                "A culinary delight made from",
                "all-natural ingredients.",
                "",
                "Can be eaten to restore",
                "hunger, but may have some",
                "unwanted effects.");
        put(
                ModItems.EXPERIENCE_GRUB,
                "Obtained from Timon and Pumbaa.",
                "",
                "Can be eaten to gain a moderate",
                "amount of experience.");
        put(ModItems.MANGO_JUICE, "A drink made from mangoes.");
        put(ModItems.HYENA_MEAL, "Used to grow plants and crops in", "the Pride Lands.");

        // Bugs
        put(
                ModItems.BUG,
                "Found underneath fallen logs in",
                "the Pride Lands. Can also be",
                "caught with a Bug Trap.",
                "",
                "Timon and Pumbaa love to",
                "eat these!");
        put(
                ModItems.TERMITE_THROWN,
                "Dropped by termites from termite",
                "mounds in the Outlands.",
                "",
                "Can be thrown, crafted into",
                "Outlandish Darts, or ground",
                "down and used to dye rugs.");

        // Pridestone tools
        put(ModItems.PRIDESTONE_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(ModItems.PRIDESTONE_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(ModItems.PRIDESTONE_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(ModItems.PRIDESTONE_SWORD, "Used to deal more damage", "to mobs.");
        put(ModItems.PRIDESTONE_HOE, "Used to till dirt into farmland.");

        // Silver tools
        put(ModItems.SILVER_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(ModItems.SILVER_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(ModItems.SILVER_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(ModItems.SILVER_SWORD, "Used to deal more damage", "to mobs.");
        put(ModItems.SILVER_HOE, "Used to till dirt into farmland.");

        // Peacock tools
        put(ModItems.PEACOCK_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(ModItems.PEACOCK_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(ModItems.PEACOCK_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(ModItems.PEACOCK_SWORD, "Used to deal more damage", "to mobs.");
        put(ModItems.PEACOCK_HOE, "Used to till dirt into farmland.");

        // Kivulite tools
        put(
                ModItems.KIVULITE_SHOVEL,
                "Used to harvest blocks like",
                "dirt and sand more quickly.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                ModItems.KIVULITE_PICKAXE,
                "Used to harvest stone-type",
                "blocks.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                ModItems.KIVULITE_AXE,
                "Used to harvest wood-type",
                "blocks more quickly.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                ModItems.KIVULITE_SWORD,
                "Used to deal more damage",
                "to mobs.",
                "",
                "Mobs hit by a Kivulite sword",
                "are set on fire.");
        put(ModItems.KIVULITE_HOE, "Used to till dirt into farmland.");

        // Corrupt tools
        put(
                ModItems.CORRUPT_SHOVEL,
                "Used to harvest blocks like",
                "dirt and sand more quickly.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                ModItems.CORRUPT_PICKAXE,
                "Used to harvest stone-type",
                "blocks.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                ModItems.CORRUPT_AXE,
                "Used to harvest wood-type",
                "blocks more quickly.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                ModItems.CORRUPT_SWORD,
                "Used to deal more damage",
                "to mobs.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(ModItems.CORRUPT_HOE, "Used to till dirt into farmland.");

        // Armour
        put(ModItems.SILVER_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.SILVER_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.SILVER_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.SILVER_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.GEMSBOK_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.GEMSBOK_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.GEMSBOK_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.GEMSBOK_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.PEACOCK_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.PEACOCK_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.PEACOCK_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(ModItems.PEACOCK_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(
                ModItems.PEACOCK_WINGS,
                "When equipped, these give",
                "the wearer the ability of",
                "limited flight, and protection",
                "against fall damage.");
        put(ModItems.OUTLANDS_HELMET, "Protects against fire and", "Outlanders when in the Outlands.");

        // Weapons / projectiles
        put(
                ModItems.GEMSBOK_SPEAR,
                "A spear that can be thrown and",
                "picked up again, or used as",
                "a melee weapon.",
                "",
                "It may also catch fish if thrown",
                "into water.");
        put(
                ModItems.POISONED_SPEAR,
                "A spear that can be thrown and",
                "picked up again, or used as",
                "a melee weapon.",
                "",
                "Mobs hit by this spear have a",
                "chance of becoming poisoned.");
        put(ModItems.DART_SHOOTER, "Used to fire darts.");
        put(
                ModItems.DART_SHOOTER_SILVER,
                "Used to fire darts.",
                "",
                "Darts fired from the Silver Dart",
                "Shooter will travel faster and be",
                "more powerful than darts fired",
                "from the standard Dart Shooter.");
        put(ModItems.DART_BLUE, "Can be fired from a Dart Shooter.");
        put(
                ModItems.DART_YELLOW,
                "Can be fired from a Dart Shooter.",
                "",
                "Yellow darts travel faster",
                "than blue ones, and have a",
                "knockback effect.");
        put(
                ModItems.DART_RED,
                "Can be fired from a Dart Shooter.",
                "",
                "Red darts travel faster than",
                "blue ones, have a knockback",
                "effect, and set the target on fire.");
        put(
                ModItems.DART_BLACK,
                "Can be fired from a Dart Shooter.",
                "",
                "Outlandish Darts create an",
                "explosion wherever they hit!");
        put(
                ModItems.DART_OUTLANDISH,
                "Can be fired from a Dart Shooter.",
                "",
                "Outlandish Darts create an",
                "explosion wherever they hit!");
        put(
                ModItems.DART_PINK,
                "Can be fired from a Dart Shooter.",
                "",
                "Flamingo Darts have the ability",
                "to drain health from the",
                "creature they hit.");
        // put(ModItems.DART_QUIVER, "Found in Pride Lands dungeons.", "", "Can hold up to six stacks", "of
        // darts.");
        put(
                ModItems.PUMBAA_BOMB,
                "Obtained from Timon and Pumbaa.",
                "",
                "When thrown, it releases a huge",
                "amount of toxic gas which can",
                "instantly kill most creatures.");

        // Special / quest items
        put(ModItems.QUEST_BOOK, "You're reading it now!");
        put(
                ModItems.RAFIKI_STICK,
                "Obtained from Rafiki in exchange",
                "for a stack of Hyena Bones.",
                "",
                "Can grow saplings, flowers",
                "and grass, harvest leaves and",
                "various other foliage, and is the",
                "only weapon that can harm Scar.");
        put(
                ModItems.RAFIKI_COIN,
                "Obtained from Rafiki in exchange",
                "for three silver ingots.",
                "",
                "When thrown, the player will be",
                "instantly transported to",
                "Rafiki's Tree.");
        put(
                ModItems.ZIRA_COIN,
                "Created when a Rafiki Coin is",
                "dropped into the Outwater.",
                "",
                "When thrown, the player will be",
                "instantly transported to",
                "Zira's Mound.");
        put(
                ModItems.WAYWARD_FEATHER,
                "Can be used to travel quickly",
                "between the Pride Lands and",
                "the Outlands without the need",
                "for a portal.",
                "",
                "However, this method of",
                "travel is not as stable as a",
                "portal, and the results are",
                "unpredictable.");
        put(ModItems.SIMBA_CHARM, "Can be equipped to Simba to", "allow him to follow the player", "through portals.");
        put(
                ModItems.AMULET,
                "Obtained from Timon and Pumbaa.",
                "",
                "Allows the wearer to speak to the",
                "animals of the Pride Lands.");
        put(
                ModItems.CRYSTAL,
                "Obtained from Timon and Pumbaa.",
                "",
                "Will completely restore the",
                "player's health if it drops",
                "below two hearts.");
        put(
                ModItems.TUNNAH_DIGGAH,
                "Obtained from Timon and Pumbaa.",
                "",
                "Used to dig large tunnels very",
                "quickly through dirt and stone.");
        put(ModItems.PRIDE_COMPASS, "A compass that points the way", "back to your home portal.");
        put(ModItems.GIRAFFE_SADDLE, "Can be equipped to an adult", "Giraffe to allow it to", "be ridden.");
        put(
                ModItems.RHYTHM_STAFF,
                "Used to enchant items on a",
                "Bongo Drum and collect notes",
                "to increase the drum's power.",
                "",
                "When a creature is killed with",
                "this staff, it may drop some notes.");
        put(ModItems.ZAZU_EGG, "Dropped by Zazus when", "they breed.", "", "May spawn a baby Zazu", "when thrown.");

        // Tickets / novelty
        put(
                ModItems.TICKET,
                "Purchased from Ticket Lions in",
                "exchange for a gold ingot.",
                "",
                "Used to activate portals to",
                "the Pride Lands.");
        put(
                ModItems.TICKET_LION_HEAD,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                ModItems.TICKET_LION_SUIT,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                ModItems.TICKET_LION_LEGS,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                ModItems.TICKET_LION_FEET,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");

        // Jars
        put(ModItems.JAR_EMPTY, "Can be used to carry water, lava", "and a few other liquids.");
        put(
                ModItems.JAR_WATER,
                "A Pridestone Jar filled with water.",
                "",
                "Can be crafted with various",
                "plants to make a decorative vase.");
        put(
                ModItems.JAR_MILK,
                "A Pridestone Jar filled with milk.",
                "",
                "Can be fed to angry Lions and",
                "Lionesses to calm them down,",
                "and is used in the making of",
                "some Pride Lands dishes.");
        put(ModItems.JAR_LAVA, "A Pridestone Jar filled with lava.");

        // Decorative
        put(ModItems.SCAR_RUG, "Dropped by Scar.", "", "A unique reward which can be", "obtained only once.");
        put(
                ModItems.ZIRA_RUG,
                "Dropped by Zira.",
                "",
                "It complements the Scar Rug",
                "nicely as a pleasant decoration",
                "in the home.");
        put(ModItems.HYENA_HEAD_ITEM, "A rare decorative block sometimes", "dropped by Hyenas.");
        put(ModItems.VASE_ITEM, "A decorative vase block.");
        put(ModItems.PRIDE_BED_ITEM, "Can be slept in to skip the night", "and set the respawn point.");

        // Building blocks (mod block items)
        put(
                ModItems.PRIDESTONE_BLOCK_ITEM,
                "A block found in abundance",
                "in the Pride Lands.",
                "",
                "Can be made into a range of",
                "tools and building blocks.");
        put(
                ModItems.CORRUPT_PRIDESTONE_BLOCK_ITEM,
                "A block found in abundance",
                "in the Outlands.",
                "",
                "Can be made into a range of",
                "tools and building blocks.");
        put(ModItems.PRIDE_BRICK_ITEM, "A useful building material made", "from Pridestone.");
        put(ModItems.CORRUPT_PRIDE_BRICK_ITEM, "A useful building material made", "from Corrupt Pridestone.");
        put(
                ModItems.PRIDE_PILLAR_ITEM,
                "A decorative block which can be",
                "made into a range of different",
                "sizes with the Grinding Bowl.");
        put(ModItems.SILVER_BLOCK_ITEM, "Made from silver ingots.", "", "Used for storage and", "decoration.");
        put(ModItems.PEACOCK_BLOCK_ITEM, "Made from Peacock Gems.", "", "Used for storage and", "decoration.");
        put(ModItems.KIVULITE_BLOCK_ITEM, "Made from Kivulite.", "", "Used for storage and", "decoration.");
        put(
                ModItems.PRIDE_COAL_ORE_ITEM,
                "An ore often found underground",
                "in the Pride Lands.",
                "",
                "Will drop coal when broken.");
        put(
                ModItems.SILVER_ORE_ITEM,
                "An ore found in the Pride Lands.",
                "",
                "Can be smelted into ingots and",
                "used to make tools, armour and",
                "various other items.");
        put(
                ModItems.PEACOCK_ORE_ITEM,
                "A rare ore found deep",
                "underground in the Pride Lands.",
                "",
                "The gems received from smelting",
                "it can be made into very strong",
                "tools and armour.");
        put(
                ModItems.KIVULITE_ORE_ITEM,
                "An ore found in the Outlands.",
                "",
                "Can be smelted into ingots and",
                "made into tools with a useful",
                "special ability.");
        put(
                ModItems.NUKA_ORE_ITEM,
                "An ore often found underground",
                "in the Outlands.",
                "",
                "Will drop Nuka Shards when broken.");
        put(
                ModItems.OUTSAND_ITEM,
                "Formed in the Outlands when",
                "sand is struck by lightning.",
                "",
                "It is dangerous to walk on. It can",
                "also be smelted into Outglass.");
        put(
                ModItems.OUTGLASS_ITEM,
                "A stronger type of glass made",
                "by smelting Outsand.",
                "",
                "It drops itself when broken.");
        put(
                ModItems.OUTGLASS_PANE_ITEM,
                "A stronger type of glass pane",
                "made from Outglass blocks.",
                "",
                "It drops itself when broken.");
        put(
                ModItems.TERMITE_MOUND_ITEM,
                "The block which makes up the",
                "termite mounds found commonly",
                "in the Outlands.",
                "",
                "If it is naturally generated,",
                "it may release Exploding Termites",
                "when broken!");
        put(
                ModItems.PUMBAA_BOX_ITEM,
                "A box containing some extremely",
                "potent natural gas from the",
                "Pride Lands' resident warthog.",
                "",
                "Use well, and handle with care!");
        put(
                ModItems.HYENA_TORCH_ITEM,
                "An alternative torch made with",
                "Hyena Bones in place of sticks.",
                "",
                "Gives off a slightly dimmer light",
                "than wooden torches.");
        put(
                ModItems.STAR_ALTAR_ITEM,
                "A magical block which can be",
                "used to summon Simba and",
                "recharge Voided Charms.",
                "",
                "It must be placed in direct",
                "view of the sky.");
        put(ModItems.OUTLANDS_ALTAR_ITEM, "Releases transformed items from", "the Outwater in Zira's Mound.");
        put(
                ModItems.OUTLANDS_POOL_ITEM,
                "A mysterious liquid found inside",
                "Zira's Mound.",
                "",
                "Some items thrown in may",
                "be transformed.");
        put(
                ModItems.MOUNTED_SHOOTER_ITEM,
                "A Dart Shooter that can be",
                "placed in the world.",
                "",
                "It holds up to one stack of darts",
                "and will fire when powered.");
        put(
                ModItems.GRINDING_BOWL_ITEM,
                "The Grinding Bowl is used to",
                "process items into their ground",
                "down forms.",
                "",
                "Examples include turning Hyena",
                "Bones into shards, Rhino Horns",
                "and mangoes into powders, and",
                "various items into Rug Dyes.");
        put(
                ModItems.BUG_TRAP_ITEM,
                "Can be placed and baited to",
                "catch bugs for Timon and Pumbaa.",
                "",
                "The likelihood of catching a bug",
                "can be increased or decreased",
                "by certain conditions.",
                "The trap will not catch bugs",
                "if there are any players",
                "within sixteen blocks of it.");
        put(
                ModItems.BONGO_DRUM_ITEM,
                "Hit it to make music!",
                "",
                "It can also be activated with a",
                "Rhythm Staff and used to enchant",
                "items.");
        put(ModItems.BANANA_CAKE_ITEM, "Can be placed in the world and", "eaten to restore hunger.");
        put(
                ModItems.PRIDE_PORTAL_FRAME_ITEM,
                "The indestructible frame of a",
                "Pride Lands Portal.",
                "",
                "Found in Lion King Ticket Booths",
                "and can be activated with a",
                "Lion King Ticket.");
        put(ModItems.OUTLANDS_PORTAL_FRAME_ITEM, "The indestructible frame of an", "Outlands Portal.");
        put(ModItems.TILLED_SAND_ITEM, "Made by using a hoe on a", "block of sand in an arid", "savannah biome.");
        put(
                ModItems.KIWANO_BLOCK_ITEM,
                "Fruit found in the arid savannah",
                "biome. Also known as the",
                "African Horned Melon.",
                "",
                "Can be grown on tilled sand",
                "to produce slices of fruit.");
        put(ModItems.HYENA_HEAD_ITEM, "A rare decorative block sometimes", "dropped by Hyenas.");
        put(ModItems.PRIDE_LEVER_ITEM, "Used to provide a stable charge.");
        put(ModItems.PRIDESTONE_PRESSURE_PLATE_ITEM, "Will release a charge when", "stepped on.");
        put(ModItems.PRIDESTONE_BUTTON_ITEM, "Will release a charge when", "pressed.");
        put(ModItems.PRIDESTONE_WALL_ITEM, "A decorative block that can be", "used as an alternative to fences.");
        put(ModItems.PRIDE_BRICK_WALL_ITEM, "A decorative block that can be", "used as an alternative to fences.");
        put(
                ModItems.CORRUPT_PRIDESTONE_WALL_ITEM,
                "A decorative block that can be",
                "used as an alternative to fences.");

        // Wood (planks)
        put(
                ModItems.ACACIA_PLANKS_ITEM,
                "Made from the wood of Acacia",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                ModItems.RAINFOREST_PLANKS_ITEM,
                "Made from the wood of Rainforest",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                ModItems.MANGO_PLANKS_ITEM,
                "Made from the wood of Mango",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                ModItems.PASSION_PLANKS_ITEM,
                "Made from the wood of Passion",
                "Fruit Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                ModItems.BANANA_PLANKS_ITEM,
                "Made from the wood of Banana",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                ModItems.DEADWOOD_PLANKS_ITEM,
                "Made from the wood of dead",
                "trees found in the Outlands.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");

        // Wood (logs)
        put(
                ModItems.ACACIA_LOG_ITEM,
                "The block which makes up the",
                "trunks of Acacia Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.RAINFOREST_LOG_ITEM,
                "The block which makes up the",
                "trunks of Rainforest Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.MANGO_LOG_ITEM,
                "The block which makes up the",
                "trunks of Mango Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.PASSION_LOG_ITEM,
                "The block which makes up the",
                "trunks of Passion Fruit Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.BANANA_LOG_ITEM,
                "The block which makes up the",
                "trunks of Banana Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.DEADWOOD_LOG_ITEM,
                "The block which makes up dead",
                "trees found in the Outlands.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                ModItems.RAFIKI_WOOD_ITEM,
                "The block which makes up the",
                "trunk of Rafiki's Tree.",
                "",
                "It cannot be destroyed.");

        // Saplings
        put(ModItems.ACACIA_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Acacia Trees.");
        put(
                ModItems.RAINFOREST_SAPLING_ITEM,
                "Can be planted on dirt or grass",
                "to grow Rainforest Trees.",
                "",
                "Four in a square will grow",
                "a huge tree.");
        put(ModItems.MANGO_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Mango Trees.");
        put(
                ModItems.PASSION_SAPLING_ITEM,
                "Can be planted on dirt or grass",
                "to grow Passion Fruit Trees.",
                "",
                "These trees will not grow",
                "naturally in the Pride Lands,",
                "although they can be grown using",
                "the Rafiki Stick.");
        put(ModItems.BANANA_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Banana Trees.");

        // Leaves
        put(
                ModItems.ACACIA_LEAVES_ITEM,
                "The block which makes up the",
                "canopy of Acacia Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay. Can also be",
                "used to breed Giraffes.");
        put(
                ModItems.RAINFOREST_LEAVES_ITEM,
                "The block which makes up the",
                "canopy of Rainforest Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay.");
        put(
                ModItems.MANGO_LEAVES_ITEM,
                "Found on Mango Trees.",
                "",
                "May drop a sapling or a mango",
                "when broken or left to decay.");
        put(
                ModItems.PASSION_LEAVES_ITEM,
                "Found on Passion Fruit Trees.",
                "",
                "May drop a sapling or a Passion",
                "Fruit when broken or left to",
                "decay.");
        put(
                ModItems.BANANA_LEAVES_ITEM,
                "Found on Banana Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay.");
        put(
                ModItems.RAFIKI_LEAVES_ITEM,
                "The block which makes up the",
                "canopies of Rafiki's Tree.",
                "",
                "It cannot be destroyed.");

        // Stairs (single descriptions per type)
        put(ModItems.ACACIA_STAIRS_ITEM, "Stairs made from Acacia Wood", "Planks.");
        put(ModItems.RAINFOREST_STAIRS_ITEM, "Stairs made from Rainforest", "Wood Planks.");
        put(ModItems.MANGO_STAIRS_ITEM, "Stairs made from Mango Wood", "Planks.");
        put(ModItems.PASSION_STAIRS_ITEM, "Stairs made from Passion Wood", "Planks.");
        put(ModItems.BANANA_STAIRS_ITEM, "Stairs made from Banana Wood", "Planks.");
        put(ModItems.DEADWOOD_STAIRS_ITEM, "Stairs made from Deadwood Planks.");
        put(ModItems.PRIDESTONE_STAIRS_ITEM, "Stairs made from Pridestone.");
        put(ModItems.PRIDE_BRICK_STAIRS_ITEM, "Stairs made from Pridestone Brick.");
        put(ModItems.CORRUPT_PRIDESTONE_STAIRS_ITEM, "Stairs made from Corrupt", "Pridestone.");
        put(ModItems.CORRUPT_PRIDE_BRICK_STAIRS_ITEM, "Stairs made from Corrupt", "Pridestone Brick.");
        put(ModItems.DRIED_MAIZE_STAIRS_ITEM, "Stairs made from Dried Maize", "blocks.");

        // Slabs
        put(ModItems.ACACIA_SLAB_ITEM, "Half blocks made from Acacia", "Wood Planks.");
        put(ModItems.RAINFOREST_SLAB_ITEM, "Half blocks made from Rainforest", "Wood Planks.");
        put(ModItems.MANGO_SLAB_ITEM, "Half blocks made from Mango", "Wood Planks.");
        put(ModItems.PASSION_SLAB_ITEM, "Half blocks made from Passion", "Wood Planks.");
        put(ModItems.BANANA_SLAB_ITEM, "Half blocks made from Banana", "Wood Planks.");
        put(ModItems.DEADWOOD_SLAB_ITEM, "Half blocks made from Deadwood", "Planks.");
        put(ModItems.PRIDESTONE_SLAB_ITEM, "Half blocks made from Pridestone.");
        put(ModItems.PRIDE_BRICK_SLAB_ITEM, "Half blocks made from", "Pridestone Brick.");
        put(ModItems.CORRUPT_PRIDESTONE_SLAB_ITEM, "Half blocks made from", "Corrupt Pridestone.");
        put(ModItems.CORRUPT_PRIDE_BRICK_SLAB_ITEM, "Half blocks made from", "Corrupt Pridestone Brick.");
        put(
                ModItems.DRIED_MAIZE_BLOCK_ITEM,
                "A building block made from dried",
                "stalks of maize.",
                "",
                "Can be crafted into stairs and",
                "slabs.");
        put(ModItems.DRIED_MAIZE_SLAB_ITEM, "Half blocks made from Dried", "Maize blocks.");
        put(ModItems.DRIED_MAIZE, "Can be crafted into Dried Maize", "building materials.");

        // Bricks
        put(ModItems.MOSSY_PRIDE_BRICK_ITEM, "A decorative block found in", "dungeons in the Pride Lands.");
        put(
                ModItems.MOSSY_CORRUPT_PRIDE_BRICK_ITEM,
                "Formed from Pridestone Brick as",
                "a result of the destructive",
                "effects of Pumbaa Flatulence.");

        // Plants and decoration
        put(
                ModItems.WHITE_FLOWER_ITEM,
                "A common flower found",
                "in the Pride Lands.",
                "",
                "Can be ground into Rug Whitener.");
        put(
                ModItems.BLUE_FLOWER_ITEM,
                "A flower found on mountains",
                "in the Pride Lands.",
                "",
                "Can be ground into Rug Dye.");
        put(
                ModItems.PURPLE_FLOWER_ITEM,
                "A tall flower found in the",
                "Pride Lands, most commonly",
                "in rainforest biomes.",
                "",
                "Can be ground into Rug Dye.");
        put(
                ModItems.RED_FLOWER_ITEM,
                "A tall, luminous flower found",
                "commonly in Upendi.",
                "",
                "Can be ground into Rug Dye.");
        put(
                ModItems.LILY_WHITE_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Whitener.");
        put(
                ModItems.LILY_RED_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Dye.");
        put(
                ModItems.LILY_VIOLET_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Dye.");
        put(ModItems.OUTSHROOM_ITEM, "Found in caves in the Outlands.", "", "Will grow if left in darkness.");
        put(ModItems.OUTSHROOM_GLOWING_ITEM, "A light source made from a Nuka", "Shard and a normal Outshroom.");
        put(ModItems.ARID_GRASS_ITEM, "A type of tall grass that grows", "in arid savannah biomes.");
        put(ModItems.HANGING_BANANA_ITEM, "Bunches of bananas hanging", "from a Banana Tree.");
        put(
                ModItems.MAIZE_STALKS,
                "Found in the Pride Lands, often",
                "by rivers or in rainforests.",
                "",
                "Can be grown on farmland which",
                "is next to water, or dried in a",
                "furnace for use in building.");

        // Spawn eggs (one description, applies to all)
        for (RegistryObject<? extends Item> egg : new RegistryObject[] {
            ModItems.LION_SPAWN_EGG, ModItems.ZEBRA_SPAWN_EGG,
            ModItems.GIRAFFE_SPAWN_EGG, ModItems.RHINO_SPAWN_EGG,
            ModItems.GEMSBOK_SPAWN_EGG, ModItems.DIKDIK_SPAWN_EGG,
            ModItems.FLAMINGO_SPAWN_EGG, ModItems.ZAZU_SPAWN_EGG,
            ModItems.BUG_SPAWN_EGG, ModItems.HYENA_SPAWN_EGG,
            ModItems.SKELETAL_HYENA_SPAWN_EGG, ModItems.OUTLANDER_SPAWN_EGG,
            ModItems.VULTURE_SPAWN_EGG, ModItems.CROCODILE_SPAWN_EGG,
            ModItems.TERMITE_SPAWN_EGG, ModItems.TERMITE_QUEEN_SPAWN_EGG,
            ModItems.TICKET_LION_SPAWN_EGG, ModItems.RAFIKI_SPAWN_EGG,
            ModItems.SIMBA_SPAWN_EGG, ModItems.TIMON_SPAWN_EGG,
            ModItems.PUMBAA_SPAWN_EGG, ModItems.SCAR_SPAWN_EGG,
            ModItems.ZIRA_SPAWN_EGG, ModItems.SKELETAL_HYENA_HEAD_SPAWN_EGG
        }) {
            put(egg, "Used to spawn a creature from", "the Lion King worlds.");
        }

        // Giraffe ties
        put(
                ModItems.GIRAFFE_TIE,
                "Used to customise your saddled",
                "Giraffes.",
                "",
                "After being whitened with Rug",
                "Whitener, a tie can be dyed in",
                "a variety of different colours.");
        put(ModItems.GIRAFFE_TIE_WHITE, "A whitened Giraffe Tie ready", "for dyeing.");
        put(ModItems.GIRAFFE_TIE_BLUE, "A dyed Giraffe Tie.");
        put(ModItems.GIRAFFE_TIE_YELLOW, "A dyed Giraffe Tie.");
        put(ModItems.GIRAFFE_TIE_RED, "A dyed Giraffe Tie.");
        put(ModItems.GIRAFFE_TIE_PURPLE, "A dyed Giraffe Tie.");
        put(ModItems.GIRAFFE_TIE_GREEN, "A dyed Giraffe Tie.");
        put(ModItems.GIRAFFE_TIE_BLACK, "A dyed Giraffe Tie.");

        // Notes
        for (RegistryObject<? extends Item> note : new RegistryObject[] {
            ModItems.NOTE_C,
            ModItems.NOTE_D,
            ModItems.NOTE_E,
            ModItems.NOTE_F,
            ModItems.NOTE_G,
            ModItems.NOTE_A,
            ModItems.NOTE_B
        }) {
            put(
                    note,
                    "Dropped from creatures killed",
                    "with a Rhythm Staff.",
                    "",
                    "Can be placed in a Bongo Drum",
                    "to increase its enchanting power.",
                    "Rarer notes have higher values.");
        }
    }
}
