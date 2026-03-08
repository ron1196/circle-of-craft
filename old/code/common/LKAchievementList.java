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

import net.minecraftforge.common.*;
import cpw.mods.fml.common.registry.*;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class LKAchievementList extends AchievementPage
{
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;
	public static LKAchievement enterPrideLands;
	public static LKAchievement killHyena;
	public static LKAchievement getMango;
	public static LKAchievement morningReport;
	public static LKAchievement craftGrindBowl;
	public static LKAchievement getStick;
	public static LKAchievement shootDart;
	public static LKAchievement killScar;
	public static LKAchievement enterOutlands;
	public static LKAchievement heads;
	public static LKAchievement simba;
	public static LKAchievement hakunaMatata;
	public static LKAchievement rhinoHorn;
	public static LKAchievement termite;
	public static LKAchievement fartBomb;
	public static LKAchievement crystal;
	public static LKAchievement rugs;
	public static LKAchievement fireTool;
	public static LKAchievement chocolateMufasa;
	public static LKAchievement enchantDiggah;
	public static LKAchievement bugTrap;
	public static LKAchievement ticketLionSuit;
	public static LKAchievement killZira;
	public static LKAchievement peacock;
	public static LKAchievement teleportSimba;
	public static LKAchievement upendi;
	public static LKAchievement animalQuest;
	public static LKAchievement drumEnchant;
	public static LKAchievement powerDrum;
	public static LKAchievement getBanana;
	public static LKAchievement rideGiraffe;
	public static LKAchievement behead;
	public static LKAchievement wings;
	
	public static void preInitAchievements()
	{
		enterPrideLands = new LKAchievement(0, "Enter the Pride Lands", 0, -5, mod_LionKing.ticket, null).setIndependent().registerAchievement();
		killHyena = new LKAchievement(1, "Kill a Hyena", -2, 2, mod_LionKing.hyenaBone, enterPrideLands).registerAchievement();
		getMango = new LKAchievement(2, "Collect a Mango from a tree", -3, -4, mod_LionKing.mango, enterPrideLands).registerAchievement();
		morningReport = new LKAchievement(3, "Speak to a Zazu before midday", 3, -4, mod_LionKing.featherBlue, enterPrideLands).registerAchievement();
		craftGrindBowl = new LKAchievement(4, "Craft a Grinding Bowl", -4, 4, mod_LionKing.itemGrindingBowl, killHyena).registerAchievement();
		getStick = new LKAchievement(5, "Obtain the Rafiki Stick", 0, 5, mod_LionKing.rafikiStick, killHyena).registerAchievement();
		shootDart = new LKAchievement(6, "Fire a dart from a Dart Shooter", -6, 5, mod_LionKing.dartShooter, craftGrindBowl).registerAchievement();
		killScar = new LKAchievement(7, "Defeat Scar", -1, 9, mod_LionKing.scarRug, getStick).registerAchievement().setSpecial();
		enterOutlands = new LKAchievement(8, "Enter the Outlands", 1, 11, mod_LionKing.outlandsPortalFrame, killScar).registerAchievement();
		heads = new LKAchievement(9, "Collect all three Hyena Head variants", -5, 2, mod_LionKing.hyenaHeadItem, killHyena).registerAchievement().setSpecial();
		simba = new LKAchievement(10, "Complete Rafiki's Quest", -3, 13, mod_LionKing.lionDust, enterOutlands).registerAchievement().setSpecial();
		hakunaMatata = new LKAchievement(11, "Trade with Timon and Pumbaa", 5, 3, mod_LionKing.bug, enterPrideLands).registerAchievement();
		rhinoHorn = new LKAchievement(12, "Give some Ground Rhino Horn to a breeding animal", -3, 6, mod_LionKing.hornGround, craftGrindBowl).registerAchievement();
		termite = new LKAchievement(13, "Shoot an Exploding Termite directly with an Outlandish Dart", 3, 12, mod_LionKing.dartBlack, enterOutlands).registerAchievement();
		fartBomb = new LKAchievement(14, "Attack a monster with Pumbaa Flatulence", 3, 4, mod_LionKing.pumbaaBomb, hakunaMatata).registerAchievement();
		crystal = new LKAchievement(15, "Restore health with a Hakuna Matata Crystal", 7, 5, mod_LionKing.crystal, hakunaMatata).registerAchievement();
		rugs = new LKAchievement(16, "Have all sixteen types of Fur Rug in your inventory", -5, 7, mod_LionKing.rug, craftGrindBowl).registerAchievement().setSpecial();
		fireTool = new LKAchievement(17, "Harvest a smeltable block using a Kivulite tool", 2, 14, mod_LionKing.pickaxeKivulite, enterOutlands).registerAchievement();
		chocolateMufasa = new LKAchievement(18, "Loot some tasty treats from a dungeon", -3, 0, mod_LionKing.chocolateMufasa, enterPrideLands).registerAchievement();
		enchantDiggah = new LKAchievement(19, "Enchant the Tunnah Diggah to increase its digging range", 8, 2, mod_LionKing.tunnahDiggah, hakunaMatata).registerAchievement().setSpecial();
		bugTrap = new LKAchievement(20, "Use a Bug Trap and bait to collect some more bugs", 4, 6, mod_LionKing.bugTrap, hakunaMatata).registerAchievement();
		ticketLionSuit = new LKAchievement(21, "Dress up and show some class... by raiding Ticket Booths?", 4, -6, mod_LionKing.ticketLionSuit, null).setIndependent().registerAchievement().setSpecial();
		killZira = new LKAchievement(22, "Complete An Outlandish Scheme", -2, 16, mod_LionKing.outlandsFeather, simba).registerAchievement().setSpecial();
		peacock = new LKAchievement(23, "Mine and smelt some Peacock Ore", 3, 0, mod_LionKing.peacockGem, enterPrideLands).registerAchievement();
		teleportSimba = new LKAchievement(24, "Take Simba through a portal to another dimension", -5, 14, mod_LionKing.charm, simba).registerAchievement();
		upendi = new LKAchievement(25, "Enter Upendi", 2, 8, mod_LionKing.passionFruit, getStick).registerAchievement().setSpecial();
		animalQuest = new LKAchievement(26, "Equip an Animalspeak Amulet and feed a hungry animal", 6, 7, mod_LionKing.amulet, hakunaMatata).registerAchievement();
		drumEnchant = new LKAchievement(27, "Enchant an item on a Bongo Drum", 6, 1, mod_LionKing.drum, peacock).registerAchievement();
		powerDrum = new LKAchievement(28, "Collect enough musical notes to fully power a Bongo Drum", 6, -1, mod_LionKing.staff, drumEnchant).registerAchievement().setSpecial();
		getBanana = new LKAchievement(29, "Pick a Banana from a tree", -3, -2, mod_LionKing.banana, enterPrideLands).registerAchievement();
		rideGiraffe = new LKAchievement(30, "Saddle and ride a Giraffe", 3, -2, mod_LionKing.giraffeSaddle, enterPrideLands).registerAchievement();
		behead = new LKAchievement(31, "Behead a Skeletal Hyena", 0, 14, new ItemStack(mod_LionKing.hyenaHeadItem, 1, 3), enterOutlands).registerAchievement();
		wings = new LKAchievement(32, "Craft a set of Peacock Wings and fly into the air", 2, 2, mod_LionKing.wings, peacock).registerAchievement();
	
		addTitle(enterPrideLands, "The Circle of Life");
		addTitle(killHyena, "Hyenas!");
		addTitle(getMango, "Underneath the Mango Tree");
		addTitle(morningReport, "The Morning Report");
		addTitle(craftGrindBowl, "Asante Sana...");
		addTitle(getStick, "He Knows the Way");
		addTitle(shootDart, "One Hundred and Eighty!");
		addTitle(killScar, "Life's Not Fair");
		addTitle(enterOutlands, "Exiled");
		addTitle(heads, "Who's Laughing Now?");
		addTitle(simba, "He Lives in You");
		addTitle(hakunaMatata, "Hakuna Matata!");
		addTitle(rhinoHorn, "Horn of Plenty");
		addTitle(termite, "Payback Time");
		addTitle(fartBomb, "Not in Front of the Kids");
		addTitle(crystal, "No Worries");
		addTitle(rugs, "Rug Collector");
		addTitle(fireTool, "The Fire Within");
		addTitle(chocolateMufasa, "Chocolatiering");
		addTitle(enchantDiggah, "Diggah Biggah Tunnah");
		addTitle(bugTrap, "It's a Trap!");
		addTitle(ticketLionSuit, "Looking Fabulous!");
		addTitle(killZira, "Not One of Us");
		addTitle(peacock, "No Peacocks Here");
		addTitle(teleportSimba, "Nants Ingonyama Bagithi");
		addTitle(upendi, "In Upendi!");
		addTitle(animalQuest, "The Animal Whisperer");
		addTitle(drumEnchant, "Enchanting Rhythms");
		addTitle(powerDrum, "Master Drummer");
		addTitle(getBanana, "Hanging Around");
		addTitle(rideGiraffe, "Who Rides Pigs?");
		addTitle(behead, "Heads Up!");
		addTitle(wings, "Limitless");
	}
	
	public LKAchievementList()
	{
		super("Lion King", getLKAchievements());
	}
	
	private static void addTitle(LKAchievement achievement, String title)
	{
		achievement.lkAchievementTitle = title;
	}
	
	private static LKAchievement[] getLKAchievements()
	{
		LKAchievement[] fallback = new LKAchievement[]{enterPrideLands};
		try
		{
			LinkedList achievements = new LinkedList();
			Field[] fields = LKAchievementList.class.getFields();
			for (Field field : fields)
			{
				if (field.get(null) instanceof LKAchievement)
				{
					achievements.add((LKAchievement)field.get(null));
				}
			}
			return (LKAchievement[])achievements.toArray(fallback);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return fallback;
		}
	}
}
