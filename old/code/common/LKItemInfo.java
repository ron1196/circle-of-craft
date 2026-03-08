package lionking.common;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

import java.util.*;
import static lionking.common.mod_LionKing.*;

public class LKItemInfo
{
	private static Map itemInfo = new HashMap();
	private static Map metaItemInfo = new HashMap();
	
	private static void addInfo(Block block, String... lines)
	{
		itemInfo.put(Integer.valueOf(block.blockID), lines);
	}
	
	private static void addInfo(Block block, int i, String... lines)
	{
		metaItemInfo.put(Arrays.asList(block.blockID, i), lines);
	}
	
	private static void addInfo(Item item, String... lines)
	{
		itemInfo.put(Integer.valueOf(item.itemID), lines);
	}
	
	private static void addInfo(Item item, int i, String... lines)
	{
		metaItemInfo.put(Arrays.asList(item.itemID, i), lines);
	}
	
    public static String[] getItemInfo(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return null;
        }
        String[] info = (String[])metaItemInfo.get(Arrays.asList(itemstack.itemID, itemstack.getItemDamage()));
        if (info != null)
        {
            return info;
        }
        return (String[])itemInfo.get(Integer.valueOf(itemstack.itemID));
    }
	
	static
	{
		addInfo(lionPortalFrame, "The indestructible frame of a", "Pride Lands Portal.", "", "Found in Lion King Ticket Booths", "and can be activated with a", "Lion King Ticket.");
		addInfo(lionPortal, "A block which transports the", "player between the Pride Lands", "and the Overworld.");
		addInfo(whiteFlower, "A common flower found", "in the Pride Lands.", "", "Can be ground into Rug Whitener.");
		addInfo(forestLeaves, "The block which makes up the", "canopy of Rainforest Trees.", "", "May drop a sapling when broken", "or left to decay.");
		addInfo(forestSapling, "Can be planted on dirt or grass", "to grow Rainforest Trees.", "", "Four in a square will grow", "a huge tree.");
		addInfo(mangoLeaves, "Found on Mango Trees.", "", "May drop a sapling or a mango", "when broken or left to decay.");
		addInfo(mangoSapling, "Can be planted on dirt or grass", "to grow Mango Trees.");
		addInfo(rafikiWood, "The block which makes up the", "trunk of Rafiki's Tree.", "", "It cannot be destroyed.");
		addInfo(rafikiLeaves, "The block which makes up the", "canopies of Rafiki's Tree.", "", "It cannot be destroyed.");
		addInfo(woodSlabSingle, 0, "Half blocks made from Acacia", "Wood Planks.");
		addInfo(woodSlabSingle, 1, "Half blocks made from Rainforest", "Wood Planks.");
		addInfo(woodSlabSingle, 2, "Half blocks made from Mango", "Wood Planks.");
		addInfo(woodSlabSingle, 3, "Half blocks made from Passion", "Wood Planks.");
		addInfo(woodSlabSingle, 4, "Half blocks made from Banana", "Wood Planks.");
		addInfo(woodSlabSingle, 5, "Half blocks made from Deadwood", "Planks.");
		addInfo(outlandsPortalFrame, 0, "The indestructible frame of an", "Outlands Portal.");
		addInfo(outlandsPortalFrame, 1, "The block which makes up Zira's", "Mound in the Outlands.", "It cannot be destroyed.");
		addInfo(outlandsPortalFrame, 2, "The block which makes up Zira's", "Mound in the Outlands.", "It cannot be destroyed.");
		addInfo(outlandsPortal,	"A block which transports the", "player between the Pride Lands", "and the Outlands.");
		addInfo(bugTrap, "Can be placed and baited to", "catch bugs for Timon and Pumbaa.", "", "The likelihood of catching a bug", "can be increased or decreased", "by certain conditions.", "The trap will not catch bugs", "if there are any players", "within sixteen blocks of it.");
		addInfo(pridestone, 0, "A block found in abundance", "in the Pride Lands.", "", "Can be made into a range of", "tools and building blocks.");
		addInfo(pridestone, 1, "A block found in abundance", "in the Outlands.", "", "Can be made into a range of", "tools and building blocks.");
		addInfo(prideCoal, 0, "An ore often found underground", "in the Pride Lands.", "", "Will drop coal when broken.");
		addInfo(prideCoal, 1, "An ore often found underground", "in the Outlands.", "", "Will drop Nuka Shards when broken.");
		addInfo(prideBrick, 0, "A useful building material made", "from Pridestone.");
		addInfo(prideBrick, 1, "A useful building material made", "from Corrupt Pridestone.");
		addInfo(pridePillar, "A decorative block which can be", "made into a range of different", "sizes with the Grinding Bowl.");
		addInfo(sapling, "Can be planted on dirt or grass", "to grow Acacia Trees.");
		addInfo(leaves, "The block which makes up the", "canopy of Acacia Trees.", "", "May drop a sapling when broken", "or left to decay. Can also be", "used to breed Giraffes.");
		addInfo(termite, "The block which makes up the", "termite mounds found commonly", "in the Outlands.", "", "If it is naturally generated,", "it may release Exploding Termites", "when broken!");
		addInfo(stoneStairs, "Stairs made from Pridestone.");
		addInfo(brickStairs, "Stairs made from Pridestone Brick.");
		addInfo(prideBrickMossy, 0, "A decorative block found in", "dungeons in the Pride Lands.");
		addInfo(prideBrickMossy, 1, "Formed from Pridestone Brick as", "a result of the destructive", "effects of Pumbaa Flatulence.");
		addInfo(oreSilver, 0, "An ore found in the Pride Lands.", "", "Can be smelted into ingots and", "used to make tools, armour and", "various other items.");
		addInfo(oreSilver, 1, "An ore found in the Outlands.", "", "Can be smelted into ingots and", "made into tools with a useful", "special ability.");
		addInfo(outsand, "Formed in the Outlands when", "sand is struck by lightning.", "", "It is dangerous to walk on. It can", "also be smelted into Outglass.");
		addInfo(outglass, "A stronger type of glass made", "by smelting Outsand.", "", "It drops itself when broken.");
		addInfo(outglassPane, "A stronger type of glass pane", "made from Outglass blocks.", "", "It drops itself when broken.");
		addInfo(starAltar, "A magical block which can be", "used to summon Simba and", "recharge Voided Charms.", "", "It must be placed in direct", "view of the sky.");
		addInfo(slabSingle, 0, "Half blocks made from Pridestone.");
		addInfo(slabSingle, 1, "Half blocks made from", "Pridestone Brick.");
		addInfo(slabSingle, 2, "Half blocks made from", "Pridestone Pillars.");
		addInfo(slabSingle, 3, "Half blocks made from", "Corrupt Pridestone.");
		addInfo(slabSingle, 4, "Half blocks made from", "Corrupt Pridestone Brick.");
		addInfo(slabSingle, 5, "Half blocks made from", "Corrupt Pridestone Pillars.");
		addInfo(log, "Part of the fallen logs which are", "found throughout the Pride Lands.", "", "Logs can be broken to get bugs", "for trading with Timon and Pumbaa.");
		addInfo(prideWood, 0, "The block which makes up the", "trunks of Acacia Trees.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(prideWood, 1, "The block which makes up the", "trunks of Rainforest Trees.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(prideWood, 2, "The block which makes up the", "trunks of Mango Trees.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(prideWood, 3, "The block which makes up the", "trunks of Passion Fruit Trees.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(blueFlower, "A flower found on mountains", "in the Pride Lands.", "", "Can be ground into Rug Dye.");
		addInfo(drum, "Hit it to make music!", "", "It can also be activated with a", "Rhythm Staff and used to enchant", "items.");
		addInfo(orePeacock, "A rare ore found deep", "underground in the Pride Lands.", "", "The gems received from smelting", "it can be made into very strong", "tools and armour.");
		addInfo(blockSilver, 0, "Made from silver ingots.", "", "Used for storage and", "decoration.");
		addInfo(blockSilver, 1, "Made from Kivulite ingots.", "", "Used for storage and", "decoration.");
		addInfo(blockPeacock, "Made from Peacock Gems.", "", "Used for storage and", "decoration.");
		addInfo(rug, "A decorative rug made from", "lion fur.", "", "After being whitened, it can", "be dyed with a range of", "different materials.");
		addInfo(stoneStairsCorrupt, "Stairs made from Corrupt", "Pridestone.");
		addInfo(brickStairsCorrupt, "Stairs made from Corrupt", "Pridestone Brick.");
		addInfo(aridGrass, "A type of tall grass that grows", "in arid savannah biomes.");
		addInfo(tilledSand, "Made by using a hoe on a", "block of sand in an arid", "savannah biome.");
		addInfo(kiwanoBlock, "Fruit found in the arid savannah", "biome. Also known as the", "African Horned Melon.", "", "Can be grown on tilled sand", "to produce slices of fruit.");
		addInfo(pressurePlate, "Will release a charge when", "stepped on.");
		addInfo(button, "Will release a charge when", "pressed.");
		addInfo(lever, "Used to provide a stable charge.");
		addInfo(outlandsPool, "A mysterious liquid found inside", "Zira's Mound.", "", "Some items thrown in may", "be transformed.");
		addInfo(outshroom, "Found in caves in the Outlands.", "", "Will grow if left in darkness.");
		addInfo(outshroomGlowing, "A light source made from a Nuka", "Shard and a normal Outshroom.");
		addInfo(pumbaaBox, "A box containing some extremely", "potent natural gas from the", "Pride Lands' resident warthog.", "", "Use well, and handle with care!");
		addInfo(outlandsAltar, "Releases transformed items from", "the Outwater in Zira's Mound.");
		addInfo(lily, 0, "Found in the waters of Pride", "Lands rainforests.", "", "Can be ground into Rug Whitener.");
		addInfo(lily, 1, "Found in the waters of Pride", "Lands rainforests.", "", "Can be ground into Rug Dye.");
		addInfo(lily, 2, "Found in the waters of Pride", "Lands rainforests.", "", "Can be ground into Rug Dye.");
		addInfo(stairsAcacia, "Stairs made from Acacia Wood", "Planks.");
		addInfo(stairsRainforest, "Stairs made from Rainforest", "Wood Planks.");
		addInfo(stairsMango, "Stairs made from Mango Wood", "Planks.");
		addInfo(planks, 0, "Made from the wood of Acacia", "Trees.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(planks, 1, "Made from the wood of Rainforest", "Trees.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(planks, 2, "Made from the wood of Mango", "Trees.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(planks, 3, "Made from the wood of Passion", "Fruit Trees.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(planks, 4, "Made from the wood of Banana", "Trees.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(planks, 5, "Made from the wood of dead", "trees found in the Outlands.", "", "A readily available building", "material which can also be", "crafted into a wide range", "of blocks, tools and items.");
		addInfo(passionSapling, "Can be planted on dirt or grass", "to grow Passion Fruit Trees.", "", "These trees will not grow", "naturally in the Pride Lands,", "although they can be grown using", "the Rafiki Stick.");
		addInfo(passionLeaves, "Found on Passion Fruit Trees.", "", "May drop a sapling or a Passion", "Fruit when broken or left to", "decay.");
		addInfo(stairsPassion, "Stairs made from Passion Wood", "Planks.");
		addInfo(hyenaTorch, "An alternative torch made with", "Hyena Bones in place of sticks.", "", "Gives off a slightly dimmer light", "than wooden torches.");
		addInfo(wall, "A decorative block that can be", "used as an alternative to fences.");
		addInfo(stairsBanana, "Stairs made from Banana Wood", "Planks.");
		addInfo(prideWood2, 0, "The block which makes up the", "trunks of Banana Trees.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(prideWood2, 1, "The block which makes up dead", "trees found in the Outlands.", "", "It can be crafted into planks,", "just like normal wood.");
		addInfo(bananaSapling, "Can be planted on dirt or grass", "to grow Banana Trees.");
		addInfo(bananaLeaves, "Found on Banana Trees.", "", "May drop a sapling when broken", "or left to decay.");
		addInfo(stairsDeadwood, "Stairs made from Deadwood Planks.");
		addInfo(driedMaizeBlock, "A building block made from dried", "stalks of maize.", "", "Can be crafted into stairs and", "slabs.");
		addInfo(driedMaizeSlabSingle, "Half blocks made from Dried", "Maize blocks.");
		addInfo(stairsDriedMaize, "Stairs made from Dried Maize", "blocks.");
		
		addInfo(ticket, 0, "Purchased from Ticket Lions in", "exchange for a gold ingot.", "", "Used to activate portals to", "the Pride Lands.");
		addInfo(ticket, 1, "Pull it and see what happens!");
		addInfo(hyenaBone, "Dropped by Hyenas.", "", "Used in crafting recipes,", "grinding, and breeding lions.");
		addInfo(lionRaw, "Dropped by Lions and Lionesses.", "", "Can be cooked to restore", "more hunger.");
		addInfo(lionCooked, "Can be eaten to restore hunger.");
		addInfo(rafikiStick, "Obtained from Rafiki in exchange", "for a stack of Hyena Bones.", "", "Can grow saplings, flowers", "and grass, harvest leaves and", "various other foliage, and is the", "only weapon that can harm Scar.");
		addInfo(purpleFlower, "A tall flower found in the", "Pride Lands, most commonly", "in rainforest biomes.", "", "Can be ground into Rug Dye.");
		addInfo(mango, "Drops from Mango Trees.", "", "Can be eaten to restore hunger,", "used in food recipes, or ground", "down in the Grinding Bowl.");
		addInfo(featherBlue, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
		addInfo(featherYellow, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
		addInfo(featherRed, "Dropped by Zazus.", "", "Can be crafted into darts, or", "ground into Rug Dye.");
		addInfo(dartBlue, "Can be fired from a Dart Shooter.");
		addInfo(dartYellow, "Can be fired from a Dart Shooter.", "", "Yellow darts travel faster", "than blue ones, and have a", "knockback effect.");
		addInfo(dartRed, "Can be fired from a Dart Shooter.", "", "Red darts travel faster than", "blue ones, have a knockback", "effect, and set the target on fire.");
		addInfo(dartShooter, "Used to fire darts.");
		addInfo(hyenaBoneShard, "Made by placing Hyena Bones in", "the Grinding Bowl.", "", "Used in crafting blue, yellow", "and red darts.");
		addInfo(zebraBoots, "When equipped, these allow the", "wearer to run at extremely", "fast speeds.");
		addInfo(zebraHide, "Dropped by Zebras.", "", "Used to craft Zebra Boots and", "a few other items.");
		addInfo(itemGrindingBowl, "The Grinding Bowl is used to", "process items into their ground", "down forms.", "", "Examples include turning Hyena", "Bones into shards, Rhino Horns", "and mangoes into powders, and", "various items into Rug Dyes.");
		addInfo(mangoDust, "Made by placing mangoes in", "the Grinding Bowl.", "", "Used in crafting Dart Shooters", "and can also dye rugs.");
		addInfo(dartBlack, "Can be fired from a Dart Shooter.", "", "Outlandish Darts create an", "explosion wherever they hit!");
		addInfo(featherBlack, "Dropped by Vultures.", "", "Can be crafted into Outlandish", "Darts, or ground into Rug Dye.");
		addInfo(shovel, "Used to harvest blocks like", "dirt and sand more quickly.");
		addInfo(pickaxe, "Used to harvest stone-type", "blocks.");
		addInfo(axe, "Used to harvest wood-type", "blocks more quickly.");
		addInfo(sword, "Used to deal more damage", "to mobs.");
		addInfo(hoe, "Used to till dirt into farmland.");
		addInfo(itemTermite, "Dropped by termites from termite", "mounds in the Outlands.", "", "Can be thrown, crafted into", "Outlandish Darts, or ground", "down and used to dye rugs.");
		addInfo(scarRug, "Dropped by Scar.", "", "A unique reward which can be", "obtained only once.");
		addInfo(jar, "Can be used to carry water, lava", "and a few other liquids.");
		addInfo(jarWater, "A Pridestone Jar filled with water.", "", "Can be crafted with various", "plants to make a decorative vase.");
		addInfo(silver, "Made by smelting Silver Ore in", "a furnace.", "", "Used to make tools, armour", "and various other items.", "Rafiki will exchange three silver", "ingots for a Rafiki Coin.");
		addInfo(silverDartShooter, "Used to fire darts.", "", "Darts fired from the Silver Dart", "Shooter will travel faster and be", "more powerful than darts fired", "from the standard Dart Shooter.");
		addInfo(shovelSilver, "Used to harvest blocks like", "dirt and sand more quickly.");
		addInfo(pickaxeSilver, "Used to harvest stone-type", "blocks.");
		addInfo(axeSilver, "Used to harvest wood-type", "blocks more quickly.");
		addInfo(swordSilver, "Used to deal more damage", "to mobs.");
		addInfo(hoeSilver, "Used to till dirt into farmland.");
		addInfo(rafikiCoin, "Obtained from Rafiki in exchange", "for three silver ingots.", "", "When thrown, the player will be", "instantly transported to", "Rafiki's Tree.");
		addInfo(termiteDust, "Made by placing Exploding Termites", "in the Grinding Bowl.", "", "Can be used to dye rugs or in", "place of gunpowder to craft TNT.");
		addInfo(lionDust, "Obtained from Rafiki in exchange", "for one Ground Mango and one", "Ground Termite.", "", "Can be used on a Star Altar to", "summon Simba, or crafted with", "four silver ingots into an", "Astral Charm.");
		addInfo(tunnahDiggah, "Obtained from Timon and Pumbaa.", "", "Used to dig large tunnels very", "quickly through dirt and stone.");
		addInfo(crystal, "Obtained from Timon and Pumbaa.", "", "Will completely restore the", "player's health if it drops", "below two hearts.");
		addInfo(bug, "Found underneath fallen logs in", "the Pride Lands. Can also be", "caught with a Bug Trap.", "", "Timon and Pumbaa love to", "eat these!");
		addInfo(chocolateMufasa, "Found in Pride Lands dungeons.", "", "Can be eaten to restore a lot", "of hunger.");
		addInfo(pumbaaBomb, "Obtained from Timon and Pumbaa.", "", "When thrown, it releases a huge", "amount of toxic gas which can", "instantly kill most creatures.");
		addInfo(fur, "Dropped by Lions and Lionesses.", "", "Used in crafting beds and various", "other items.");
		addInfo(jarMilk, "A Pridestone Jar filled with milk.", "", "Can be fed to angry Lions and", "Lionesses to calm them down,", "and is used in the making of", "some Pride Lands dishes.");
		addInfo(zebraRaw, "Dropped by Zebras.", "", "Can be cooked to restore", "more hunger.");
		addInfo(zebraCooked, "Can be eaten to restore hunger.");
		addInfo(rhinoRaw, "Dropped by Rhinos.", "", "Can be cooked to restore", "more hunger.");
		addInfo(rhinoCooked, "Can be eaten to restore hunger.");
		addInfo(helmetSilver, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bodySilver, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(legsSilver, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bootsSilver, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(vase, "A decorative vase block.");
		addInfo(horn, "Dropped by Rhinos.", "", "Can be ground down in the", "Grinding Bowl.");
		addInfo(hornGround, "Made by placing Rhino Horn", "in the Grinding Bowl.", "", "Can be fed to breeding animals", "to make them produce more", "offspring, and can also dye rugs.");
		addInfo(bed, "Can be slept in to skip the night", "and set the respawn point.");
		addInfo(gemsbokHide, "Dropped by Gemsboks.", "", "Used in crafting Gemsbok armour", "and Giraffe Saddles.");
		addInfo(gemsbokHorn, "Dropped by Gemsboks.", "", "Used in crafting Gemsbok Spears", "and the Rhythm Staff.");
		addInfo(gemsbokSpear, "A spear that can be thrown and", "picked up again, or used as", "a melee weapon.", "", "It may also catch fish if thrown", "into water.");
		addInfo(juice, "A drink made from mangoes.");
		addInfo(helmetGemsbok, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bodyGemsbok, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(legsGemsbok, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bootsGemsbok, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(jarLava, "A Pridestone Jar filled with lava.");
		addInfo(peacockGem,	"Made by smelting Peacock Ore in", "a furnace.", "", "Used to make tools, armour", "and other items.");
		addInfo(shovelPeacock, "Used to harvest blocks like", "dirt and sand more quickly.");
		addInfo(pickaxePeacock, "Used to harvest stone-type", "blocks.");
		addInfo(axePeacock, "Used to harvest wood-type", "blocks more quickly.");
		addInfo(swordPeacock, "Used to deal more damage", "to mobs.");
		addInfo(hoePeacock, "Used to till dirt into farmland.");
		addInfo(helmetPeacock, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bodyPeacock, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(legsPeacock, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(bootsPeacock, "Can be equipped to give", "protection against most", "forms of damage.");
		addInfo(rugDye, 0, "Made by placing a flower or a", "White Waterlily in the Grinding", "Bowl.", "", "Used to whiten a Fur Rug", "before it is dyed.");
		addInfo(rugDye, 1, "Made by placing a Blue", "Feather in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 2, "Made by placing a Yellow", "Feather in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 3, "Made by placing a Red", "Feather or a Red Waterlily in", "the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 4, "Made by placing a flower in", "the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 5, "Made by placing a flower in", "the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 6, "Made by placing leaves", "the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 7, "Made by placing an Outlandish", "Feather in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 8, "Made by placing a Violet", "Waterlily in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 9, "Made by placing a Flamingo", "Feather in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(rugDye, 10, "Made by placing Passion Leaves", "in the Grinding Bowl.", "", "Used to dye Fur Rugs.");
		addInfo(wings, "When equipped, these give", "the wearer the ability of", "limited flight, and protection", "against fall damage.");
		addInfo(corn, "Picked from Maize Stalks.", "", "Can be eaten, cooked, and used", "as an alternative to wheat for", "breeding Pride Lands animals.");
		addInfo(cornStalk, "Found in the Pride Lands, often", "by rivers or in rainforests.", "", "Can be grown on farmland which", "is next to water, or dried in a", "furnace for use in building.");
		addInfo(popcorn, "Can be eaten to restore hunger.");
		addInfo(nukaShard, "Dropped by Nuka Ore.", "", "Can be used as furnace fuel,", "ground down into poison, or", "crafted with an Outshroom to", "make it glow.");
		addInfo(outlanderFur, "Dropped by Outlanders.", "", "Can be crafted into Fur Rugs.");
		addInfo(outlanderMeat, "Dropped by Outlanders.", "", "Can be cooked to restore", "more hunger and remove the", "possibility of food poisoning.");
		addInfo(passionFruit, "Drops from Passion Fruit Trees.", "", "Can be eaten when in full health", "to travel to the Upendi realm.");
		addInfo(redFlower, "A tall, luminous flower found", "commonly in Upendi.", "", "Can be ground into Rug Dye.");
		addInfo(kivulite, "Made by smelting Kivulite Ore in", "a furnace.", "", "Used to make tools with special", "fire-based abilities.");
		addInfo(shovelKivulite, "Used to harvest blocks like", "dirt and sand more quickly.", "", "Smeltable blocks harvested by a", "Kivulite tool are automatically", "smelted.");
		addInfo(pickaxeKivulite, "Used to harvest stone-type", "blocks.", "", "Smeltable blocks harvested by a", "Kivulite tool are automatically", "smelted.");
		addInfo(axeKivulite, "Used to harvest wood-type", "blocks more quickly.", "", "Smeltable blocks harvested by a", "Kivulite tool are automatically", "smelted.");
		addInfo(swordKivulite, "Used to deal more damage", "to mobs.", "", "Mobs hit by a Kivulite sword", "are set on fire.");
		addInfo(bugStew, "A culinary delight made from", "all-natural ingredients.", "", "Can be eaten to restore", "hunger, but may have some", "unwanted effects.");
		addInfo(crocodileMeat, "Dropped by Crocodiles.", "", "Can be eaten to restore hunger.", "Has a chance to cause", "food poisoning.");
		addInfo(poison, "Made by placing Nuka Shards in", "the Grinding Bowl.", "", "Two Poison Powders can be", "applied to a Gemsbok Spear to", "give it a poison effect.");
		addInfo(poisonedSpear, "A spear that can be thrown and", "picked up again, or used as", "a melee weapon.", "", "Mobs hit by this spear have a", "chance of becoming poisoned.");
		addInfo(xpGrub, "Obtained from Timon and Pumbaa.", "", "Can be eaten to gain a moderate", "amount of experience.");
		addInfo(shovelCorrupt, "Used to harvest blocks like", "dirt and sand more quickly.", "", "Corrupt Pridestone tools are", "strong when first crafted, but", "weaken with use.");
		addInfo(pickaxeCorrupt, "Used to harvest stone-type", "blocks.", "", "Corrupt Pridestone tools are", "strong when first crafted, but", "weaken with use.");
		addInfo(axeCorrupt, "Used to harvest wood-type", "blocks more quickly.", "", "Corrupt Pridestone tools are", "strong when first crafted, but", "weaken with use.");
		addInfo(swordCorrupt, "Used to deal more damage", "to mobs.", "", "Corrupt Pridestone tools are", "strong when first crafted, but", "weaken with use.");
		addInfo(charm, 0, "Can be equipped to Simba to", "allow him to follow the player", "through portals.");
		addInfo(charm, 1, "Dropped by Simba if he was", "equipped with an Astral Charm.", "", "Can be recharged at the", "Star Altar.");
		addInfo(zazuEgg, "Dropped by Zazus when", "they breed.", "", "May spawn a baby Zazu", "when thrown.");
		addInfo(kiwano, "Dropped by Kiwano fruit.", "", "Can be eaten to restore hunger,", "or crafted into seeds.");
		addInfo(kiwanoSeeds, "Can be placed on Tilled Sand", "to grow a Kiwano.");
		addInfo(ticketLionHead, "A novelty item found in", "Lion King Ticket Booths.", "", "Collect the full set!");
		addInfo(ticketLionSuit, "A novelty item found in", "Lion King Ticket Booths.", "", "Collect the full set!");
		addInfo(ticketLionLegs, "A novelty item found in", "Lion King Ticket Booths.", "", "Collect the full set!");
		addInfo(ticketLionFeet, "A novelty item found in", "Lion King Ticket Booths.", "", "Collect the full set!");
		addInfo(questBook, "You're reading it now!");
		addInfo(outlandsHelm, "Protects against fire and", "Outlanders when in the Outlands.");
		addInfo(dartQuiver, "Found in Pride Lands dungeons.", "", "Can hold up to six stacks", "of darts.");
		addInfo(outlandsFeather, "Can be used to travel quickly", "between the Pride Lands and", "the Outlands without the need", "for a portal.", "", "However, this method of", "travel is not as stable as a", "portal, and the results are", "unpredictable.");
		addInfo(ziraRug, "Dropped by Zira.", "", "It complements the Scar Rug", "nicely as a pleasant decoration", "in the home.");
		addInfo(ziraCoin, "Created when a Rafiki Coin is", "dropped into the Outwater.", "", "When thrown, the player will be", "instantly transported to", "Zira's Mound.");
		addInfo(hyenaHeadItem, "A rare decorative block sometimes", "dropped by Hyenas.");
		addInfo(amulet, "Obtained from Timon and Pumbaa.", "", "Allows the wearer to speak to the", "animals of the Pride Lands.");
		addInfo(mountedShooterItem, 0, "A Dart Shooter that can be", "placed in the world.", "", "It holds up to one stack of darts", "and will fire when powered.");
		addInfo(mountedShooterItem, 1, "A Silver Dart Shooter that can", "be placed in the world.", "", "It holds up to one stack of darts", "and will fire when powered.");
		addInfo(staff, "Used to enchant items on a", "Bongo Drum and collect notes", "to increase the drum's power.", "", "When a creature is killed with", "this staff, it may drop some notes.");
		addInfo(note, "Dropped from creatures killed", "with a Rhythm Staff.", "", "Can be placed in a Bongo Drum", "to increase its enchanting power.", "Rarer notes have higher values.");
		addInfo(giraffeSaddle, "Can be equipped to an adult", "Giraffe to allow it to", "be ridden.");
		addInfo(spawnEgg, "Used to spawn a creature from", "the Lion King worlds.");
		addInfo(tie, "Used to customise your saddled", "Giraffes.", "", "After being whitened with Rug", "Whitener, a tie can be dyed in", "a variety of different colours.");
		addInfo(yam, "A vegetable found growing in", "savannah biomes.", "", "Can be farmed, and roasted to", "restore more hunger. If eaten", "raw, it may cause food poisoning.");
		addInfo(roastYam, "Can be eaten to restore hunger.");
		addInfo(banana, "Grows on Banana Trees.", "", "Can be eaten to restore hunger,", "or used in food recipes.");
		addInfo(bananaCake, "Can be placed in the world and", "eaten to restore hunger.");
		addInfo(featherPink, "Dropped by Flamingos.", "", "Can be crafted into Flamingo", "Darts, or ground into Rug Dye.");
		addInfo(dartPink, "Can be fired from a Dart Shooter.", "", "Flamingo Darts have the ability", "to drain health from the", "creature they hit.");
		addInfo(bananaBread, "Can be eaten to restore hunger.");
		addInfo(hyenaMeal, "Used to grow plants and crops in", "the Pride Lands.");
		addInfo(cornKernels, "Can be used as an alternative to", "seeds for breeding Zazus.");
		addInfo(driedMaize, "Can be crafted into Dried Maize", "building materials.");
	}
}
