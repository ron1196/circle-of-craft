package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.registry.LionKingItems;
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
                LionKingItems.LION_FUR,
                "Dropped by Lions and Lionesses.",
                "",
                "Used in crafting beds and various",
                "other items.");
        put(
                LionKingItems.HYENA_BONE,
                "Dropped by Hyenas.",
                "",
                "Used in crafting recipes,",
                "grinding, and breeding lions.");
        put(
                LionKingItems.HYENA_BONE_SHARD,
                "Made by placing Hyena Bones in",
                "the Grinding Bowl.",
                "",
                "Used in crafting blue, yellow",
                "and red darts.");
        put(LionKingItems.OUTLANDER_FUR, "Dropped by Outlanders.", "", "Can be crafted into Fur Rugs.");
        put(LionKingItems.ZEBRA_HIDE, "Dropped by Zebras.", "", "Used to craft Zebra Boots and", "a few other items.");
        put(
                LionKingItems.GEMSBOK_HIDE,
                "Dropped by Gemsboks.",
                "",
                "Used in crafting Gemsbok armour",
                "and Giraffe Saddles.");
        put(
                LionKingItems.GEMSBOK_HORN,
                "Dropped by Gemsboks.",
                "",
                "Used in crafting Gemsbok Spears",
                "and the Rhythm Staff.");
        put(LionKingItems.RHINO_HORN, "Dropped by Rhinos.", "", "Can be ground down in the", "Grinding Bowl.");
        put(
                LionKingItems.GROUND_RHINO_HORN,
                "Made by placing Rhino Horn",
                "in the Grinding Bowl.",
                "",
                "Can be fed to breeding animals",
                "to make them produce more",
                "offspring, and can also dye rugs.");
        put(
                LionKingItems.NUKA_SHARD,
                "Dropped by Nuka Ore.",
                "",
                "Can be used as furnace fuel,",
                "ground down into poison, or",
                "crafted with an Outshroom to",
                "make it glow.");
        put(
                LionKingItems.MANGO_DUST,
                "Made by placing mangoes in",
                "the Grinding Bowl.",
                "",
                "Used in crafting Dart Shooters",
                "and can also dye rugs.");
        put(
                LionKingItems.TERMITE_DUST,
                "Made by placing Exploding Termites",
                "in the Grinding Bowl.",
                "",
                "Can be used to dye rugs or in",
                "place of gunpowder to craft TNT.");
        put(
                LionKingItems.RAFIKI_DUST,
                "Obtained from Rafiki in exchange",
                "for one Ground Mango and one",
                "Ground Termite.",
                "",
                "Can be used on a Star Altar to",
                "summon Simba, or crafted with",
                "four silver ingots into an",
                "Astral Charm.");
        put(
                LionKingItems.POISON,
                "Made by placing Nuka Shards in",
                "the Grinding Bowl.",
                "",
                "Two Poison Powders can be",
                "applied to a Gemsbok Spear to",
                "give it a poison effect.");
        put(
                LionKingItems.SILVER_INGOT,
                "Made by smelting Silver Ore in",
                "a furnace.",
                "",
                "Used to make tools, armour",
                "and various other items.",
                "Rafiki will exchange three silver",
                "ingots for a Rafiki Coin.");
        put(
                LionKingItems.PEACOCK_GEM,
                "Made by smelting Peacock Ore in",
                "a furnace.",
                "",
                "Used to make tools, armour",
                "and other items.");
        put(
                LionKingItems.KIVULITE,
                "Made by smelting Kivulite Ore in",
                "a furnace.",
                "",
                "Used to make tools with special",
                "fire-based abilities.");

        // Feathers
        put(
                LionKingItems.FEATHER_BLUE,
                "Dropped by Zazus.",
                "",
                "Can be crafted into darts, or",
                "ground into Rug Dye.");
        put(
                LionKingItems.FEATHER_YELLOW,
                "Dropped by Zazus.",
                "",
                "Can be crafted into darts, or",
                "ground into Rug Dye.");
        put(
                LionKingItems.FEATHER_RED,
                "Dropped by Zazus.",
                "",
                "Can be crafted into darts, or",
                "ground into Rug Dye.");
        put(
                LionKingItems.FEATHER_BLACK,
                "Dropped by Vultures.",
                "",
                "Can be crafted into Outlandish",
                "Darts, or ground into Rug Dye.");
        put(
                LionKingItems.FEATHER_PINK,
                "Dropped by Flamingos.",
                "",
                "Can be crafted into Flamingo",
                "Darts, or ground into Rug Dye.");

        // Foods
        put(LionKingItems.LION_RAW, "Dropped by Lions and Lionesses.", "", "Can be cooked to restore", "more hunger.");
        put(LionKingItems.LION_COOKED, "Can be eaten to restore hunger.");
        put(LionKingItems.ZEBRA_RAW, "Dropped by Zebras.", "", "Can be cooked to restore", "more hunger.");
        put(LionKingItems.ZEBRA_COOKED, "Can be eaten to restore hunger.");
        put(LionKingItems.RHINO_RAW, "Dropped by Rhinos.", "", "Can be cooked to restore", "more hunger.");
        put(LionKingItems.RHINO_COOKED, "Can be eaten to restore hunger.");
        put(
                LionKingItems.OUTLANDER_MEAT,
                "Dropped by Outlanders.",
                "",
                "Can be cooked to restore",
                "more hunger and remove the",
                "possibility of food poisoning.");
        put(
                LionKingItems.CROCODILE_MEAT,
                "Dropped by Crocodiles.",
                "",
                "Can be eaten to restore hunger.",
                "Has a chance to cause",
                "food poisoning.");
        put(
                LionKingItems.MANGO,
                "Drops from Mango Trees.",
                "",
                "Can be eaten to restore hunger,",
                "used in food recipes, or ground",
                "down in the Grinding Bowl.");
        put(
                LionKingItems.BANANA,
                "Grows on Banana Trees.",
                "",
                "Can be eaten to restore hunger,",
                "or used in food recipes.");
        put(
                LionKingItems.PASSION_FRUIT,
                "Drops from Passion Fruit Trees.",
                "",
                "Can be eaten when in full health",
                "to travel to the Upendi realm.");
        put(
                LionKingItems.KIWANO,
                "Dropped by Kiwano fruit.",
                "",
                "Can be eaten to restore hunger,",
                "or crafted into seeds.");
        put(LionKingItems.KIWANO_SEEDS, "Can be placed on Tilled Sand", "to grow a Kiwano.");
        put(
                LionKingItems.YAM,
                "A vegetable found growing in",
                "savannah biomes.",
                "",
                "Can be farmed, and roasted to",
                "restore more hunger. If eaten",
                "raw, it may cause food poisoning.");
        put(LionKingItems.ROAST_YAM, "Can be eaten to restore hunger.");
        put(
                LionKingItems.CORN,
                "Picked from Maize Stalks.",
                "",
                "Can be eaten, cooked, and used",
                "as an alternative to wheat for",
                "breeding Pride Lands animals.");
        put(LionKingItems.CORN_KERNELS, "Can be used as an alternative to", "seeds for breeding Zazus.");
        put(LionKingItems.POPCORN, "Can be eaten to restore hunger.");
        put(LionKingItems.BANANA_BREAD, "Can be eaten to restore hunger.");
        put(
                LionKingItems.CHOCOLATE_MUFASA,
                "Found in Pride Lands dungeons.",
                "",
                "Can be eaten to restore a lot",
                "of hunger.");
        put(
                LionKingItems.BUG_STEW,
                "A culinary delight made from",
                "all-natural ingredients.",
                "",
                "Can be eaten to restore",
                "hunger, but may have some",
                "unwanted effects.");
        put(
                LionKingItems.EXPERIENCE_GRUB,
                "Obtained from Timon and Pumbaa.",
                "",
                "Can be eaten to gain a moderate",
                "amount of experience.");
        put(LionKingItems.MANGO_JUICE, "A drink made from mangoes.");
        put(LionKingItems.HYENA_MEAL, "Used to grow plants and crops in", "the Pride Lands.");

        // Bugs
        put(
                LionKingItems.BUG,
                "Found underneath fallen logs in",
                "the Pride Lands. Can also be",
                "caught with a Bug Trap.",
                "",
                "Timon and Pumbaa love to",
                "eat these!");
        put(
                LionKingItems.TERMITE_THROWN,
                "Dropped by termites from termite",
                "mounds in the Outlands.",
                "",
                "Can be thrown, crafted into",
                "Outlandish Darts, or ground",
                "down and used to dye rugs.");

        // Pridestone tools
        put(LionKingItems.PRIDESTONE_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(LionKingItems.PRIDESTONE_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(LionKingItems.PRIDESTONE_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(LionKingItems.PRIDESTONE_SWORD, "Used to deal more damage", "to mobs.");
        put(LionKingItems.PRIDESTONE_HOE, "Used to till dirt into farmland.");

        // Silver tools
        put(LionKingItems.SILVER_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(LionKingItems.SILVER_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(LionKingItems.SILVER_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(LionKingItems.SILVER_SWORD, "Used to deal more damage", "to mobs.");
        put(LionKingItems.SILVER_HOE, "Used to till dirt into farmland.");

        // Peacock tools
        put(LionKingItems.PEACOCK_SHOVEL, "Used to harvest blocks like", "dirt and sand more quickly.");
        put(LionKingItems.PEACOCK_PICKAXE, "Used to harvest stone-type", "blocks.");
        put(LionKingItems.PEACOCK_AXE, "Used to harvest wood-type", "blocks more quickly.");
        put(LionKingItems.PEACOCK_SWORD, "Used to deal more damage", "to mobs.");
        put(LionKingItems.PEACOCK_HOE, "Used to till dirt into farmland.");

        // Kivulite tools
        put(
                LionKingItems.KIVULITE_SHOVEL,
                "Used to harvest blocks like",
                "dirt and sand more quickly.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                LionKingItems.KIVULITE_PICKAXE,
                "Used to harvest stone-type",
                "blocks.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                LionKingItems.KIVULITE_AXE,
                "Used to harvest wood-type",
                "blocks more quickly.",
                "",
                "Smeltable blocks harvested by a",
                "Kivulite tool are automatically",
                "smelted.");
        put(
                LionKingItems.KIVULITE_SWORD,
                "Used to deal more damage",
                "to mobs.",
                "",
                "Mobs hit by a Kivulite sword",
                "are set on fire.");
        put(LionKingItems.KIVULITE_HOE, "Used to till dirt into farmland.");

        // Corrupt tools
        put(
                LionKingItems.CORRUPT_SHOVEL,
                "Used to harvest blocks like",
                "dirt and sand more quickly.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                LionKingItems.CORRUPT_PICKAXE,
                "Used to harvest stone-type",
                "blocks.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                LionKingItems.CORRUPT_AXE,
                "Used to harvest wood-type",
                "blocks more quickly.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(
                LionKingItems.CORRUPT_SWORD,
                "Used to deal more damage",
                "to mobs.",
                "",
                "Corrupt Pridestone tools are",
                "strong when first crafted, but",
                "weaken with use.");
        put(LionKingItems.CORRUPT_HOE, "Used to till dirt into farmland.");

        // Armour
        put(LionKingItems.SILVER_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.SILVER_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.SILVER_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.SILVER_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.GEMSBOK_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.GEMSBOK_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.GEMSBOK_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.GEMSBOK_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.PEACOCK_HELMET, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.PEACOCK_CHESTPLATE, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.PEACOCK_LEGGINGS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(LionKingItems.PEACOCK_BOOTS, "Can be equipped to give", "protection against most", "forms of damage.");
        put(
                LionKingItems.PEACOCK_WINGS,
                "When equipped, these give",
                "the wearer the ability of",
                "limited flight, and protection",
                "against fall damage.");
        put(LionKingItems.OUTLANDS_HELMET, "Protects against fire and", "Outlanders when in the Outlands.");

        // Weapons / projectiles
        put(
                LionKingItems.GEMSBOK_SPEAR,
                "A spear that can be thrown and",
                "picked up again, or used as",
                "a melee weapon.",
                "",
                "It may also catch fish if thrown",
                "into water.");
        put(
                LionKingItems.POISONED_SPEAR,
                "A spear that can be thrown and",
                "picked up again, or used as",
                "a melee weapon.",
                "",
                "Mobs hit by this spear have a",
                "chance of becoming poisoned.");
        put(LionKingItems.DART_SHOOTER, "Used to fire darts.");
        put(
                LionKingItems.DART_SHOOTER_SILVER,
                "Used to fire darts.",
                "",
                "Darts fired from the Silver Dart",
                "Shooter will travel faster and be",
                "more powerful than darts fired",
                "from the standard Dart Shooter.");
        put(LionKingItems.DART_BLUE, "Can be fired from a Dart Shooter.");
        put(
                LionKingItems.DART_YELLOW,
                "Can be fired from a Dart Shooter.",
                "",
                "Yellow darts travel faster",
                "than blue ones, and have a",
                "knockback effect.");
        put(
                LionKingItems.DART_RED,
                "Can be fired from a Dart Shooter.",
                "",
                "Red darts travel faster than",
                "blue ones, have a knockback",
                "effect, and set the target on fire.");
        put(
                LionKingItems.DART_BLACK,
                "Can be fired from a Dart Shooter.",
                "",
                "Outlandish Darts create an",
                "explosion wherever they hit!");
        put(
                LionKingItems.DART_OUTLANDISH,
                "Can be fired from a Dart Shooter.",
                "",
                "Outlandish Darts create an",
                "explosion wherever they hit!");
        put(
                LionKingItems.DART_PINK,
                "Can be fired from a Dart Shooter.",
                "",
                "Flamingo Darts have the ability",
                "to drain health from the",
                "creature they hit.");
        put(LionKingItems.DART_QUIVER, "Found in Pride Lands dungeons.", "", "Can hold up to six stacks", "of darts.");
        put(
                LionKingItems.PUMBAA_BOMB,
                "Obtained from Timon and Pumbaa.",
                "",
                "When thrown, it releases a huge",
                "amount of toxic gas which can",
                "instantly kill most creatures.");

        // Special / quest items
        put(LionKingItems.QUEST_BOOK, "You're reading it now!");
        put(
                LionKingItems.RAFIKI_STICK,
                "Obtained from Rafiki in exchange",
                "for a stack of Hyena Bones.",
                "",
                "Can grow saplings, flowers",
                "and grass, harvest leaves and",
                "various other foliage, and is the",
                "only weapon that can harm Scar.");
        put(
                LionKingItems.RAFIKI_COIN,
                "Obtained from Rafiki in exchange",
                "for three silver ingots.",
                "",
                "When thrown, the player will be",
                "instantly transported to",
                "Rafiki's Tree.");
        put(
                LionKingItems.ZIRA_COIN,
                "Created when a Rafiki Coin is",
                "dropped into the Outwater.",
                "",
                "When thrown, the player will be",
                "instantly transported to",
                "Zira's Mound.");
        put(
                LionKingItems.WAYWARD_FEATHER,
                "Can be used to travel quickly",
                "between the Pride Lands and",
                "the Outlands without the need",
                "for a portal.",
                "",
                "However, this method of",
                "travel is not as stable as a",
                "portal, and the results are",
                "unpredictable.");
        put(
                LionKingItems.SIMBA_CHARM,
                "Can be equipped to Simba to",
                "allow him to follow the player",
                "through portals.");
        put(
                LionKingItems.AMULET,
                "Obtained from Timon and Pumbaa.",
                "",
                "Allows the wearer to speak to the",
                "animals of the Pride Lands.");
        put(
                LionKingItems.CRYSTAL,
                "Obtained from Timon and Pumbaa.",
                "",
                "Will completely restore the",
                "player's health if it drops",
                "below two hearts.");
        put(
                LionKingItems.TUNNAH_DIGGAH,
                "Obtained from Timon and Pumbaa.",
                "",
                "Used to dig large tunnels very",
                "quickly through dirt and stone.");
        put(LionKingItems.PRIDE_COMPASS, "A compass that points the way", "back to your home portal.");
        put(LionKingItems.GIRAFFE_SADDLE, "Can be equipped to an adult", "Giraffe to allow it to", "be ridden.");
        put(
                LionKingItems.RHYTHM_STAFF,
                "Used to enchant items on a",
                "Bongo Drum and collect notes",
                "to increase the drum's power.",
                "",
                "When a creature is killed with",
                "this staff, it may drop some notes.");
        put(
                LionKingItems.ZAZU_EGG,
                "Dropped by Zazus when",
                "they breed.",
                "",
                "May spawn a baby Zazu",
                "when thrown.");

        // Tickets / novelty
        put(
                LionKingItems.TICKET,
                "Purchased from Ticket Lions in",
                "exchange for a gold ingot.",
                "",
                "Used to activate portals to",
                "the Pride Lands.");
        put(
                LionKingItems.TICKET_LION_HEAD,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                LionKingItems.TICKET_LION_SUIT,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                LionKingItems.TICKET_LION_LEGS,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");
        put(
                LionKingItems.TICKET_LION_FEET,
                "A novelty item found in",
                "Lion King Ticket Booths.",
                "",
                "Collect the full set!");

        // Jars
        put(LionKingItems.JAR_EMPTY, "Can be used to carry water, lava", "and a few other liquids.");
        put(
                LionKingItems.JAR_WATER,
                "A Pridestone Jar filled with water.",
                "",
                "Can be crafted with various",
                "plants to make a decorative vase.");
        put(
                LionKingItems.JAR_MILK,
                "A Pridestone Jar filled with milk.",
                "",
                "Can be fed to angry Lions and",
                "Lionesses to calm them down,",
                "and is used in the making of",
                "some Pride Lands dishes.");
        put(LionKingItems.JAR_LAVA, "A Pridestone Jar filled with lava.");

        // Decorative
        put(LionKingItems.SCAR_RUG, "Dropped by Scar.", "", "A unique reward which can be", "obtained only once.");
        put(
                LionKingItems.ZIRA_RUG,
                "Dropped by Zira.",
                "",
                "It complements the Scar Rug",
                "nicely as a pleasant decoration",
                "in the home.");
        put(LionKingItems.HYENA_HEAD_ITEM, "A rare decorative block sometimes", "dropped by Hyenas.");
        put(
                LionKingItems.FUR_RUG_ITEM,
                "A decorative rug made from",
                "lion fur.",
                "",
                "After being whitened, it can",
                "be dyed with a range of",
                "different materials.");
        put(LionKingItems.VASE_ITEM, "A decorative vase block.");
        put(LionKingItems.PRIDE_BED_ITEM, "Can be slept in to skip the night", "and set the respawn point.");

        // Building blocks (mod block items)
        put(
                LionKingItems.PRIDESTONE_BLOCK_ITEM,
                "A block found in abundance",
                "in the Pride Lands.",
                "",
                "Can be made into a range of",
                "tools and building blocks.");
        put(
                LionKingItems.CORRUPT_PRIDESTONE_BLOCK_ITEM,
                "A block found in abundance",
                "in the Outlands.",
                "",
                "Can be made into a range of",
                "tools and building blocks.");
        put(LionKingItems.PRIDE_BRICK_ITEM, "A useful building material made", "from Pridestone.");
        put(LionKingItems.CORRUPT_PRIDE_BRICK_ITEM, "A useful building material made", "from Corrupt Pridestone.");
        put(
                LionKingItems.PRIDE_PILLAR_ITEM,
                "A decorative block which can be",
                "made into a range of different",
                "sizes with the Grinding Bowl.");
        put(LionKingItems.SILVER_BLOCK_ITEM, "Made from silver ingots.", "", "Used for storage and", "decoration.");
        put(LionKingItems.PEACOCK_BLOCK_ITEM, "Made from Peacock Gems.", "", "Used for storage and", "decoration.");
        put(
                LionKingItems.PRIDE_COAL_ORE_ITEM,
                "An ore often found underground",
                "in the Pride Lands.",
                "",
                "Will drop coal when broken.");
        put(
                LionKingItems.SILVER_ORE_ITEM,
                "An ore found in the Pride Lands.",
                "",
                "Can be smelted into ingots and",
                "used to make tools, armour and",
                "various other items.");
        put(
                LionKingItems.PEACOCK_ORE_ITEM,
                "A rare ore found deep",
                "underground in the Pride Lands.",
                "",
                "The gems received from smelting",
                "it can be made into very strong",
                "tools and armour.");
        put(
                LionKingItems.KIVULITE_ORE_ITEM,
                "An ore found in the Outlands.",
                "",
                "Can be smelted into ingots and",
                "made into tools with a useful",
                "special ability.");
        put(
                LionKingItems.NUKA_ORE_ITEM,
                "An ore often found underground",
                "in the Outlands.",
                "",
                "Will drop Nuka Shards when broken.");
        put(
                LionKingItems.OUTSAND_ITEM,
                "Formed in the Outlands when",
                "sand is struck by lightning.",
                "",
                "It is dangerous to walk on. It can",
                "also be smelted into Outglass.");
        put(
                LionKingItems.OUTGLASS_ITEM,
                "A stronger type of glass made",
                "by smelting Outsand.",
                "",
                "It drops itself when broken.");
        put(
                LionKingItems.OUTGLASS_PANE_ITEM,
                "A stronger type of glass pane",
                "made from Outglass blocks.",
                "",
                "It drops itself when broken.");
        put(
                LionKingItems.TERMITE_MOUND_ITEM,
                "The block which makes up the",
                "termite mounds found commonly",
                "in the Outlands.",
                "",
                "If it is naturally generated,",
                "it may release Exploding Termites",
                "when broken!");
        put(
                LionKingItems.PUMBAA_BOX_ITEM,
                "A box containing some extremely",
                "potent natural gas from the",
                "Pride Lands' resident warthog.",
                "",
                "Use well, and handle with care!");
        put(
                LionKingItems.HYENA_TORCH_ITEM,
                "An alternative torch made with",
                "Hyena Bones in place of sticks.",
                "",
                "Gives off a slightly dimmer light",
                "than wooden torches.");
        put(
                LionKingItems.STAR_ALTAR_ITEM,
                "A magical block which can be",
                "used to summon Simba and",
                "recharge Voided Charms.",
                "",
                "It must be placed in direct",
                "view of the sky.");
        put(LionKingItems.OUTLANDS_ALTAR_ITEM, "Releases transformed items from", "the Outwater in Zira's Mound.");
        put(
                LionKingItems.OUTLANDS_POOL_ITEM,
                "A mysterious liquid found inside",
                "Zira's Mound.",
                "",
                "Some items thrown in may",
                "be transformed.");
        put(
                LionKingItems.MOUNTED_SHOOTER_ITEM,
                "A Dart Shooter that can be",
                "placed in the world.",
                "",
                "It holds up to one stack of darts",
                "and will fire when powered.");
        put(
                LionKingItems.GRINDING_BOWL_ITEM,
                "The Grinding Bowl is used to",
                "process items into their ground",
                "down forms.",
                "",
                "Examples include turning Hyena",
                "Bones into shards, Rhino Horns",
                "and mangoes into powders, and",
                "various items into Rug Dyes.");
        put(
                LionKingItems.BUG_TRAP_ITEM,
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
                LionKingItems.BONGO_DRUM_ITEM,
                "Hit it to make music!",
                "",
                "It can also be activated with a",
                "Rhythm Staff and used to enchant",
                "items.");
        put(LionKingItems.BANANA_CAKE_ITEM, "Can be placed in the world and", "eaten to restore hunger.");
        put(
                LionKingItems.PRIDE_PORTAL_FRAME_ITEM,
                "The indestructible frame of a",
                "Pride Lands Portal.",
                "",
                "Found in Lion King Ticket Booths",
                "and can be activated with a",
                "Lion King Ticket.");
        put(LionKingItems.OUTLANDS_PORTAL_FRAME_ITEM, "The indestructible frame of an", "Outlands Portal.");
        put(LionKingItems.TILLED_SAND_ITEM, "Made by using a hoe on a", "block of sand in an arid", "savannah biome.");
        put(
                LionKingItems.KIWANO_BLOCK_ITEM,
                "Fruit found in the arid savannah",
                "biome. Also known as the",
                "African Horned Melon.",
                "",
                "Can be grown on tilled sand",
                "to produce slices of fruit.");
        put(LionKingItems.HYENA_HEAD_ITEM, "A rare decorative block sometimes", "dropped by Hyenas.");
        put(LionKingItems.PRIDE_LEVER_ITEM, "Used to provide a stable charge.");
        put(LionKingItems.PRIDESTONE_PRESSURE_PLATE_ITEM, "Will release a charge when", "stepped on.");
        put(LionKingItems.PRIDESTONE_BUTTON_ITEM, "Will release a charge when", "pressed.");
        put(LionKingItems.PRIDESTONE_WALL_ITEM, "A decorative block that can be", "used as an alternative to fences.");
        put(LionKingItems.PRIDE_BRICK_WALL_ITEM, "A decorative block that can be", "used as an alternative to fences.");
        put(
                LionKingItems.CORRUPT_PRIDESTONE_WALL_ITEM,
                "A decorative block that can be",
                "used as an alternative to fences.");

        // Wood (planks)
        put(
                LionKingItems.ACACIA_PLANKS_ITEM,
                "Made from the wood of Acacia",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                LionKingItems.RAINFOREST_PLANKS_ITEM,
                "Made from the wood of Rainforest",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                LionKingItems.MANGO_PLANKS_ITEM,
                "Made from the wood of Mango",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                LionKingItems.PASSION_PLANKS_ITEM,
                "Made from the wood of Passion",
                "Fruit Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                LionKingItems.BANANA_PLANKS_ITEM,
                "Made from the wood of Banana",
                "Trees.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");
        put(
                LionKingItems.DEADWOOD_PLANKS_ITEM,
                "Made from the wood of dead",
                "trees found in the Outlands.",
                "",
                "A readily available building",
                "material which can also be",
                "crafted into a wide range",
                "of blocks, tools and items.");

        // Wood (logs)
        put(
                LionKingItems.ACACIA_LOG_ITEM,
                "The block which makes up the",
                "trunks of Acacia Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.RAINFOREST_LOG_ITEM,
                "The block which makes up the",
                "trunks of Rainforest Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.MANGO_LOG_ITEM,
                "The block which makes up the",
                "trunks of Mango Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.PASSION_LOG_ITEM,
                "The block which makes up the",
                "trunks of Passion Fruit Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.BANANA_LOG_ITEM,
                "The block which makes up the",
                "trunks of Banana Trees.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.DEADWOOD_LOG_ITEM,
                "The block which makes up dead",
                "trees found in the Outlands.",
                "",
                "It can be crafted into planks,",
                "just like normal wood.");
        put(
                LionKingItems.RAFIKI_WOOD_ITEM,
                "The block which makes up the",
                "trunk of Rafiki's Tree.",
                "",
                "It cannot be destroyed.");

        // Saplings
        put(LionKingItems.ACACIA_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Acacia Trees.");
        put(
                LionKingItems.RAINFOREST_SAPLING_ITEM,
                "Can be planted on dirt or grass",
                "to grow Rainforest Trees.",
                "",
                "Four in a square will grow",
                "a huge tree.");
        put(LionKingItems.MANGO_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Mango Trees.");
        put(
                LionKingItems.PASSION_SAPLING_ITEM,
                "Can be planted on dirt or grass",
                "to grow Passion Fruit Trees.",
                "",
                "These trees will not grow",
                "naturally in the Pride Lands,",
                "although they can be grown using",
                "the Rafiki Stick.");
        put(LionKingItems.BANANA_SAPLING_ITEM, "Can be planted on dirt or grass", "to grow Banana Trees.");

        // Leaves
        put(
                LionKingItems.ACACIA_LEAVES_ITEM,
                "The block which makes up the",
                "canopy of Acacia Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay. Can also be",
                "used to breed Giraffes.");
        put(
                LionKingItems.RAINFOREST_LEAVES_ITEM,
                "The block which makes up the",
                "canopy of Rainforest Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay.");
        put(
                LionKingItems.MANGO_LEAVES_ITEM,
                "Found on Mango Trees.",
                "",
                "May drop a sapling or a mango",
                "when broken or left to decay.");
        put(
                LionKingItems.PASSION_LEAVES_ITEM,
                "Found on Passion Fruit Trees.",
                "",
                "May drop a sapling or a Passion",
                "Fruit when broken or left to",
                "decay.");
        put(
                LionKingItems.BANANA_LEAVES_ITEM,
                "Found on Banana Trees.",
                "",
                "May drop a sapling when broken",
                "or left to decay.");
        put(
                LionKingItems.RAFIKI_LEAVES_ITEM,
                "The block which makes up the",
                "canopies of Rafiki's Tree.",
                "",
                "It cannot be destroyed.");

        // Stairs (single descriptions per type)
        put(LionKingItems.ACACIA_STAIRS_ITEM, "Stairs made from Acacia Wood", "Planks.");
        put(LionKingItems.RAINFOREST_STAIRS_ITEM, "Stairs made from Rainforest", "Wood Planks.");
        put(LionKingItems.MANGO_STAIRS_ITEM, "Stairs made from Mango Wood", "Planks.");
        put(LionKingItems.PASSION_STAIRS_ITEM, "Stairs made from Passion Wood", "Planks.");
        put(LionKingItems.BANANA_STAIRS_ITEM, "Stairs made from Banana Wood", "Planks.");
        put(LionKingItems.DEADWOOD_STAIRS_ITEM, "Stairs made from Deadwood Planks.");
        put(LionKingItems.PRIDESTONE_STAIRS_ITEM, "Stairs made from Pridestone.");
        put(LionKingItems.PRIDE_BRICK_STAIRS_ITEM, "Stairs made from Pridestone Brick.");
        put(LionKingItems.CORRUPT_PRIDESTONE_STAIRS_ITEM, "Stairs made from Corrupt", "Pridestone.");
        put(LionKingItems.CORRUPT_PRIDE_BRICK_STAIRS_ITEM, "Stairs made from Corrupt", "Pridestone Brick.");
        put(LionKingItems.DRIED_MAIZE_STAIRS_ITEM, "Stairs made from Dried Maize", "blocks.");

        // Slabs
        put(LionKingItems.ACACIA_SLAB_ITEM, "Half blocks made from Acacia", "Wood Planks.");
        put(LionKingItems.RAINFOREST_SLAB_ITEM, "Half blocks made from Rainforest", "Wood Planks.");
        put(LionKingItems.MANGO_SLAB_ITEM, "Half blocks made from Mango", "Wood Planks.");
        put(LionKingItems.PASSION_SLAB_ITEM, "Half blocks made from Passion", "Wood Planks.");
        put(LionKingItems.BANANA_SLAB_ITEM, "Half blocks made from Banana", "Wood Planks.");
        put(LionKingItems.DEADWOOD_SLAB_ITEM, "Half blocks made from Deadwood", "Planks.");
        put(LionKingItems.PRIDESTONE_SLAB_ITEM, "Half blocks made from Pridestone.");
        put(LionKingItems.PRIDE_BRICK_SLAB_ITEM, "Half blocks made from", "Pridestone Brick.");
        put(LionKingItems.CORRUPT_PRIDESTONE_SLAB_ITEM, "Half blocks made from", "Corrupt Pridestone.");
        put(LionKingItems.CORRUPT_PRIDE_BRICK_SLAB_ITEM, "Half blocks made from", "Corrupt Pridestone Brick.");
        put(
                LionKingItems.DRIED_MAIZE_BLOCK_ITEM,
                "A building block made from dried",
                "stalks of maize.",
                "",
                "Can be crafted into stairs and",
                "slabs.");
        put(LionKingItems.DRIED_MAIZE_SLAB_ITEM, "Half blocks made from Dried", "Maize blocks.");
        put(LionKingItems.DRIED_MAIZE, "Can be crafted into Dried Maize", "building materials.");

        // Bricks
        put(LionKingItems.MOSSY_PRIDE_BRICK_ITEM, "A decorative block found in", "dungeons in the Pride Lands.");
        put(
                LionKingItems.MOSSY_CORRUPT_PRIDE_BRICK_ITEM,
                "Formed from Pridestone Brick as",
                "a result of the destructive",
                "effects of Pumbaa Flatulence.");

        // Plants and decoration
        put(
                LionKingItems.WHITE_FLOWER_ITEM,
                "A common flower found",
                "in the Pride Lands.",
                "",
                "Can be ground into Rug Whitener.");
        put(
                LionKingItems.BLUE_FLOWER_ITEM,
                "A flower found on mountains",
                "in the Pride Lands.",
                "",
                "Can be ground into Rug Dye.");
        put(
                LionKingItems.PURPLE_FLOWER_ITEM,
                "A tall flower found in the",
                "Pride Lands, most commonly",
                "in rainforest biomes.",
                "",
                "Can be ground into Rug Dye.");
        put(
                LionKingItems.RED_FLOWER_ITEM,
                "A tall, luminous flower found",
                "commonly in Upendi.",
                "",
                "Can be ground into Rug Dye.");
        put(
                LionKingItems.LILY_WHITE_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Whitener.");
        put(
                LionKingItems.LILY_RED_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Dye.");
        put(
                LionKingItems.LILY_VIOLET_ITEM,
                "Found in the waters of Pride",
                "Lands rainforests.",
                "",
                "Can be ground into Rug Dye.");
        put(LionKingItems.OUTSHROOM_ITEM, "Found in caves in the Outlands.", "", "Will grow if left in darkness.");
        put(LionKingItems.OUTSHROOM_GLOWING_ITEM, "A light source made from a Nuka", "Shard and a normal Outshroom.");
        put(LionKingItems.ARID_GRASS_ITEM, "A type of tall grass that grows", "in arid savannah biomes.");
        put(LionKingItems.HANGING_BANANA_ITEM, "Bunches of bananas hanging", "from a Banana Tree.");
        put(
                LionKingItems.MAIZE_STALKS,
                "Found in the Pride Lands, often",
                "by rivers or in rainforests.",
                "",
                "Can be grown on farmland which",
                "is next to water, or dried in a",
                "furnace for use in building.");

        // Spawn eggs (one description, applies to all)
        for (RegistryObject<? extends Item> egg : new RegistryObject[] {
            LionKingItems.LION_SPAWN_EGG, LionKingItems.ZEBRA_SPAWN_EGG,
            LionKingItems.GIRAFFE_SPAWN_EGG, LionKingItems.RHINO_SPAWN_EGG,
            LionKingItems.GEMSBOK_SPAWN_EGG, LionKingItems.DIKDIK_SPAWN_EGG,
            LionKingItems.FLAMINGO_SPAWN_EGG, LionKingItems.ZAZU_SPAWN_EGG,
            LionKingItems.BUG_SPAWN_EGG, LionKingItems.HYENA_SPAWN_EGG,
            LionKingItems.SKELETAL_HYENA_SPAWN_EGG, LionKingItems.OUTLANDER_SPAWN_EGG,
            LionKingItems.VULTURE_SPAWN_EGG, LionKingItems.CROCODILE_SPAWN_EGG,
            LionKingItems.TERMITE_SPAWN_EGG, LionKingItems.TERMITE_QUEEN_SPAWN_EGG,
            LionKingItems.TICKET_LION_SPAWN_EGG, LionKingItems.RAFIKI_SPAWN_EGG,
            LionKingItems.SIMBA_SPAWN_EGG, LionKingItems.TIMON_SPAWN_EGG,
            LionKingItems.PUMBAA_SPAWN_EGG, LionKingItems.SCAR_SPAWN_EGG,
            LionKingItems.ZIRA_SPAWN_EGG, LionKingItems.SKELETAL_HYENA_HEAD_SPAWN_EGG
        }) {
            put(egg, "Used to spawn a creature from", "the Lion King worlds.");
        }

        // Giraffe ties
        put(
                LionKingItems.GIRAFFE_TIE,
                "Used to customise your saddled",
                "Giraffes.",
                "",
                "After being whitened with Rug",
                "Whitener, a tie can be dyed in",
                "a variety of different colours.");
        put(LionKingItems.GIRAFFE_TIE_WHITE, "A whitened Giraffe Tie ready", "for dyeing.");
        put(LionKingItems.GIRAFFE_TIE_BLUE, "A dyed Giraffe Tie.");
        put(LionKingItems.GIRAFFE_TIE_YELLOW, "A dyed Giraffe Tie.");
        put(LionKingItems.GIRAFFE_TIE_RED, "A dyed Giraffe Tie.");
        put(LionKingItems.GIRAFFE_TIE_PURPLE, "A dyed Giraffe Tie.");
        put(LionKingItems.GIRAFFE_TIE_GREEN, "A dyed Giraffe Tie.");
        put(LionKingItems.GIRAFFE_TIE_BLACK, "A dyed Giraffe Tie.");

        // Notes
        for (RegistryObject<? extends Item> note : new RegistryObject[] {
            LionKingItems.NOTE_C,
            LionKingItems.NOTE_D,
            LionKingItems.NOTE_E,
            LionKingItems.NOTE_F,
            LionKingItems.NOTE_G,
            LionKingItems.NOTE_A,
            LionKingItems.NOTE_B
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

        // Misc
        put(LionKingItems.LK_SPAWNER_ITEM, "A creature spawner.");
    }
}
