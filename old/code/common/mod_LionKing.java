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
import net.minecraft.world.chunk.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.Field;
import lionking.common.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.common.registry.EntityRegistry.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "lionking", version = "v1.14 for Minecraft 1.6.4")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_LionKing implements ICraftingHandler, IFuelHandler, IPickupNotifier, IWorldGenerator
{
	@SidedProxy(clientSide = "lionking.client.LKClientProxy", serverSide = "lionking.common.LKCommonProxy")
	public static LKCommonProxy proxy;
	
	@Mod.Instance("lionking")
	public static mod_LionKing instance;

	public static int idPrideLands;
	public static int idOutlands;
	public static int idUpendi;
	
	private static int idHyenaEnchantment;
	private static int idDiggahEnchantment;
	private static int idRafikiStickDamage;
	private static int idRafikiStickDurability;
	private static int idRafikiStickThunder;
	private static int idDiggahPrecision;
	
	public static int boothLimit;
	public static boolean shouldCheckUpdates;
	public static boolean randomBooths;
	public static int lkMusicChance;
	
	private static EnumToolMaterial toolPridestone = EnumHelper.addToolMaterial("LK_PRIDESTONE", 1, 150, 4.0F, 1, 5);
	private static EnumToolMaterial toolSilver = EnumHelper.addToolMaterial("LK_SILVER", 2, 490, 6.0F, 2, 16);
	private static EnumToolMaterial toolPeacock = EnumHelper.addToolMaterial("LK_PEACOCK", 3, 1475, 8.0F, 3, 9);
	private static EnumToolMaterial toolKivulite = EnumHelper.addToolMaterial("LK_KIVULITE", 2, 70, 6.0F, 0, 3);
	private static EnumToolMaterial toolCorrupt = EnumHelper.addToolMaterial("LK_CORRUPT_PRIDESTONE", 1, 120, 5.5F, 0, 7);

    private static EnumArmorMaterial armorSilver = EnumHelper.addArmorMaterial("LK_SILVER", 19, new int[] {2, 7, 5, 2}, 16);
    private static EnumArmorMaterial armorGemsbok = EnumHelper.addArmorMaterial("LK_GEMSBOK", 8, new int[] {2, 5, 4, 1}, 8);
    private static EnumArmorMaterial armorPeacock = EnumHelper.addArmorMaterial("LK_PEACOCK", 31, new int[] {3, 8, 6, 3}, 9);
	public static EnumArmorMaterial armorSuit = EnumHelper.addArmorMaterial("LK_TICKET_LION_SUIT", 0, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial armorOutlandsHelm = EnumHelper.addArmorMaterial("LK_OUTLANDISH_HELM", 12, new int[] {2, 6, 5, 2}, 0);

	public static Block lionPortalFrame;
	public static Block lionPortal;
	public static Block woodSlabDouble;
	public static Block whiteFlower;
	public static Block forestLeaves;
	public static Block forestSapling;
	public static Block flowerTop;
	public static Block flowerBase;
	public static Block mangoLeaves;
	public static Block mangoSapling;
	public static Block grindBowl;
	public static Block rafikiWood;
	public static Block rafikiLeaves;
	public static Block woodSlabSingle;
	public static Block outlandsPortalFrame;
	public static Block outlandsPortal;
	public static Block bugTrap;
	public static Block pridestone;
	public static Block prideCoal;
	public static Block prideBrick;
    public static Block pridePillar; 
	public static Block sapling;  
	public static Block leaves;
	public static Block termite;
	public static Block stoneStairs;
	public static Block brickStairs;
	public static Block prideBrickMossy;
	public static Block oreSilver;
	public static Block outsand;  
	public static Block outglass;
	public static Block outglassPane;
	public static Block starAltar; 
	public static Block slabSingle;
	public static Block slabDouble;
	public static Block log;
	public static Block prideWood;
	public static Block blueFlower;
	public static Block drum;
	public static Block flowerVase;
	public static Block orePeacock;
	public static Block blockSilver;
	public static Block blockPeacock;
	public static Block rug;
	public static Block maize;
	public static Block stoneStairsCorrupt;
	public static Block brickStairsCorrupt;
	public static Block aridGrass;
	public static Block tilledSand;
	public static Block kiwanoBlock;
	public static Block kiwanoStem;
	public static Block pressurePlate;
	public static Block button;
	public static Block lever;
	public static Block outlandsPool;
	public static Block outshroom;
	public static Block outshroomGlowing;
	public static Block pumbaaBox;
	public static Block outlandsAltar;
	public static Block lily;
	public static Block stairsAcacia;
	public static Block stairsRainforest;
	public static Block stairsMango;
	public static Block blockBed;
	public static Block planks;
	public static Block hyenaHead;
	public static Block mountedShooter;
	public static Block passionSapling;
	public static Block passionLeaves;
	public static Block stairsPassion;
	public static Block hyenaTorch;
	public static Block wall;
	public static Block yamCrops;
	public static Block stairsBanana;
	public static Block prideWood2;
	public static Block bananaSapling;
	public static Block bananaLeaves;
	public static Block bananaCakeBlock;
	public static Block hangingBanana;
	public static Block stairsDeadwood;
	public static Block mobSpawner;
	public static Block driedMaizeBlock;
	public static Block driedMaizeSlabSingle;
	public static Block driedMaizeSlabDouble;
	public static Block stairsDriedMaize;

	public static Item ticket;
	public static Item hyenaBone;
	public static Item lionRaw;
	public static Item lionCooked;
	public static Item rafikiStick;
	public static Item purpleFlower;
	public static Item mango;
	public static Item featherBlue; 
    public static Item featherYellow; 
    public static Item featherRed; 
    public static Item dartBlue; 
    public static Item dartYellow; 
    public static Item dartRed; 
	public static Item dartShooter; 
	public static Item hyenaBoneShard;
	public static Item zebraBoots;
	public static Item zebraHide;
	public static Item itemGrindingBowl;
	public static Item mangoDust;
    public static Item dartBlack;
    public static Item featherBlack; 
    public static Item shovel;
    public static Item pickaxe;
    public static Item axe;
    public static Item sword;
    public static Item hoe;
    public static Item itemTermite;
    public static Item scarRug;
    public static Item jar;
    public static Item jarWater;
    public static Item silver;
	public static Item silverDartShooter; 
    public static Item shovelSilver;
    public static Item pickaxeSilver;
    public static Item axeSilver;
    public static Item swordSilver;
    public static Item hoeSilver;
    public static Item rafikiCoin;
	public static Item termiteDust;
    public static Item lionDust;
	public static Item tunnahDiggah;
	public static Item crystal;;
	public static Item bug;
	public static Item chocolateMufasa;
	public static Item pumbaaBomb;
    public static Item fur;
	public static Item jarMilk;
	public static Item zebraRaw;
	public static Item zebraCooked;
	public static Item rhinoRaw;
	public static Item rhinoCooked;
    public static Item helmetSilver;
    public static Item bodySilver;
    public static Item legsSilver;
    public static Item bootsSilver;
    public static Item vase;
    public static Item horn;
    public static Item hornGround;
    public static Item bed;
    public static Item gemsbokHide;
    public static Item gemsbokHorn;
    public static Item gemsbokSpear;
    public static Item juice;
    public static Item helmetGemsbok;
    public static Item bodyGemsbok;
    public static Item legsGemsbok;
    public static Item bootsGemsbok;
    public static Item jarLava;
	public static Item peacockGem;
    public static Item shovelPeacock;
    public static Item pickaxePeacock;
    public static Item axePeacock;
    public static Item swordPeacock;
    public static Item hoePeacock;
    public static Item helmetPeacock;
    public static Item bodyPeacock;
    public static Item legsPeacock;
    public static Item bootsPeacock;
	public static Item rugDye;
	public static Item wings;
	public static Item corn;
	public static Item cornStalk;
	public static Item popcorn;
	public static Item nukaShard;
	public static Item outlanderFur;
	public static Item outlanderMeat;
	public static Item passionFruit;
	public static Item redFlower;
	public static Item kivulite;
    public static Item shovelKivulite;
    public static Item pickaxeKivulite;
    public static Item axeKivulite;
    public static Item swordKivulite;
	public static Item bugStew;
	public static Item crocodileMeat;
	public static Item poison;
	public static Item poisonedSpear;
	public static Item xpGrub;
    public static Item shovelCorrupt;
    public static Item pickaxeCorrupt;
    public static Item axeCorrupt;
    public static Item swordCorrupt;
	public static Item charm;
	public static Item zazuEgg;
	public static Item kiwano;
	public static Item kiwanoSeeds;
	public static Item ticketLionHead;
	public static Item ticketLionSuit;
	public static Item ticketLionLegs;
	public static Item ticketLionFeet;
	public static Item questBook;
	public static Item outlandsHelm;
	public static Item dartQuiver;
	public static Item outlandsFeather;
	public static Item ziraRug;
	public static Item ziraCoin;
	public static Item hyenaHeadItem;
	public static Item amulet;
	public static Item mountedShooterItem;
	public static Item staff;
	public static Item note;
	public static Item giraffeSaddle;
	public static Item spawnEgg;
	public static Item tie;
	public static Item yam;
	public static Item roastYam;
	public static Item banana;
	public static Item bananaCake;
	public static Item featherPink; 
    public static Item dartPink;
	public static Item bananaBread;
	public static Item hyenaMeal;
	public static Item cornKernels;
	public static Item driedMaize;

	public static Enchantment hyenaEnchantment;
	public static Enchantment diggahEnchantment;
	public static Enchantment rafikiStickDamage;
	public static Enchantment rafikiStickDurability;
	public static Enchantment rafikiStickThunder;
	public static Enchantment diggahPrecision;
	
	private static LKPacketHandlerServer serverHandler = new LKPacketHandlerServer();
	
	@Mod.EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
        int idLionPortalFrame = config.getBlock("Pride Lands Portal Frame", 1200).getInt();
		int idLionPortal = config.getBlock("Pride Lands Portal", 1201).getInt();
		int idWoodSlabDouble = config.getBlock("Double Wooden Slabs", 1202).getInt();
		int idWhiteFlower = config.getBlock("White Flower", 1203).getInt();
		int idForestLeaves = config.getBlock("Forest Leaves", 1204).getInt();
		int idForestSapling = config.getBlock("Forest Sapling", 1205).getInt();
		int idFlowerTop = config.getBlock("Purple Flower Top", 1206).getInt();
		int idFlowerBase = config.getBlock("Purple Flower Base", 1207).getInt();
		int idMangoLeaves = config.getBlock("Mango Leaves", 1208).getInt();
		int idMangoSapling = config.getBlock("Mango Sapling", 1209).getInt();
		int idGrindBowl = config.getBlock("Grinding Bowl Block", 1210).getInt();
		int idRafikiWood = config.getBlock("Rafiki Tree Wood", 1211).getInt();
		int idRafikiLeaves = config.getBlock("Rafiki Tree Leaves", 1212).getInt();
		int idWoodSlabSingle = config.getBlock("Single Wooden Slabs", 1213).getInt();
		int idOutlandsPortalFrame = config.getBlock("Outlands Portal Frame", 1214).getInt();
		int idOutlandsPortal = config.getBlock("Outlands Portal", 1215).getInt();
		int idBugTrap = config.getBlock("Bug Trap", 1216).getInt();
		int idPridestone = config.getTerrainBlock(config.CATEGORY_BLOCK, "Pridestone", 217, null).getInt();
		int idPrideCoal = config.getBlock("Coal Ore", 1218).getInt();
		int idPrideBrick = config.getBlock("Pridestone Brick", 1219).getInt();
		int idPridePillar = config.getBlock("Pridestone Pillar", 1220).getInt();
		int idSapling = config.getBlock("Acacia Sapling", 1221).getInt();
		int idLeaves = config.getBlock("Acacia Leaves", 1222).getInt();
		int idTermite = config.getBlock("Termite Mound", 1223).getInt();
		int idStoneStairs = config.getBlock("Pridestone Stairs", 1224).getInt();
		int idBrickStairs = config.getBlock("Pridestone Brick Stairs", 1225).getInt();
		int idPrideBrickMossy = config.getBlock("Mossy Pridestone Brick", 1226).getInt();
		int idOreSilver = config.getBlock("Silver Ore", 1227).getInt();
		int idOutsand = config.getBlock("Outsand", 1228).getInt();
		int idOutglass = config.getBlock("Outglass", 1229).getInt();
		int idOutglassPane = config.getBlock("Outglass Pane", 1230).getInt();
		int idStarAltar = config.getBlock("Star Altar", 1231).getInt();
		int idSlabSingle = config.getBlock("Single Slabs", 1232).getInt();
		int idSlabDouble = config.getBlock("Double Slabs", 1233).getInt();
		int idLog = config.getBlock("Fallen Log", 1234).getInt();
		int idPrideWood = config.getBlock("Wood", 1235).getInt();
		int idBlueFlower = config.getBlock("Blue Flower", 1236).getInt();
		int idDrum = config.getBlock("Bongo Drum", 1237).getInt();
		int idFlowerVase = config.getBlock("Flower Vase Blocks", 1238).getInt();
		int idOrePeacock = config.getBlock("Peacock Ore", 1239).getInt();
		int idBlockSilver = config.getBlock("Block of Silver", 1240).getInt();
		int idBlockPeacock = config.getBlock("Peacock Block", 1241).getInt();
		int idRug = config.getBlock("Fur Rug", 1242).getInt();
		int idMaize = config.getBlock("Maize Block", 1243).getInt();
		int idStoneStairsCorrupt = config.getBlock("Corrupt Pridestone Stairs", 1244).getInt();
		int idBrickStairsCorrupt = config.getBlock("Corrupt Pridestone Brick Stairs", 1245).getInt();
		int idAridGrass = config.getBlock("Arid Savannah Grass", 1246).getInt();
		int idTilledSand = config.getBlock("Tilled Sand", 1247).getInt();
		int idKiwanoBlock = config.getBlock("Kiwano", 1248).getInt();
		int idKiwanoStem = config.getBlock("Kiwano Stem", 1249).getInt();
		int idPressurePlate = config.getBlock("Pressure Plate", 1250).getInt();
		int idButton = config.getBlock("Button", 1251).getInt();
		int idLever = config.getBlock("Lever", 1252).getInt();
		int idOutlandsPool = config.getBlock("Outwater", 1253).getInt();
		int idOutshroom = config.getBlock("Outshroom", 1254).getInt();
		int idOutshroomGlowing = config.getBlock("Glowing Outshroom", 1255).getInt();
		int idPumbaaBox = config.getBlock("Pumbbox", 1256).getInt();
		int idOutlandsAltar = config.getBlock("Outwater Focus", 1257).getInt();
		int idLily = config.getBlock("Waterlily", 1258).getInt();
		int idStairsAcacia = config.getBlock("Acacia Wood Stairs", 1259).getInt();
		int idStairsRainforest = config.getBlock("Rainforest Wood Stairs", 1260).getInt();
		int idStairsMango = config.getBlock("Mango Wood Stairs", 1261).getInt();
		int idBlockBed = config.getBlock("Bed Block", 1262).getInt();
		int idPlanks = config.getBlock("Wooden Planks", 1263).getInt();
		int idHyenaHead = config.getBlock("Hyena Head Block", 1264).getInt();
		int idMountedShooter = config.getBlock("Mounted Dart Shooter", 1265).getInt();
		int idPassionSapling = config.getBlock("Passion Sapling", 1266).getInt();
		int idPassionLeaves = config.getBlock("Passion Leaves", 1267).getInt();
		int idStairsPassion = config.getBlock("Passion Wood Stairs", 1268).getInt();
		int idHyenaTorch = config.getBlock("Hyena Bone Torch", 1269).getInt();
		int idWall = config.getBlock("Pridestone Wall", 1270).getInt();
		int idYamCrops = config.getBlock("Yam Crops", 1271).getInt();
		int idStairsBanana = config.getBlock("Banana Wood Stairs", 1272).getInt();
		int idPrideWood2 = config.getBlock("Wood2", 1273).getInt();
		int idBananaSapling = config.getBlock("Banana Sapling", 1274).getInt();
		int idBananaLeaves = config.getBlock("Banana Leaves", 1275).getInt();
		int idBananaCakeBlock = config.getBlock("Banana Cake Block", 1276).getInt();
		int idHangingBanana = config.getBlock("Hanging Banana", 1277).getInt();
		int idStairsDeadwood = config.getBlock("Deadwood Stairs", 1278).getInt();
		int idMobSpawner = config.getBlock("Mob Spawner", 1279).getInt();
		int idDriedMaizeBlock = config.getBlock("Dried Maize Block", 1280).getInt();
		int idDriedMaizeSlabSingle = config.getBlock("Dried Maize Single Slab", 1281).getInt();
		int idDriedMaizeSlabDouble = config.getBlock("Dried Maize Double Slab", 1282).getInt();
		int idStairsDriedMaize = config.getBlock("Dried Maize Stairs", 1283).getInt();

        int idTicket = config.getItem("Lion King Ticket", 4256).getInt();
		int idHyenaBone = config.getItem("Hyena Bone", 4257).getInt();
		int idLionRaw = config.getItem("Raw Lion", 4258).getInt();
		int idLionCooked = config.getItem("Lion Chop", 4259).getInt();
		int idRafikiStick = config.getItem("Rafiki Stick", 4260).getInt();
		int idPurpleFlower = config.getItem("Purple Flower", 4261).getInt();
		int idMango = config.getItem("Mango", 4262).getInt();
		int idFeatherBlue = config.getItem("Blue Feather", 4263).getInt();
		int idFeatherYellow = config.getItem("Yellow Feather", 4264).getInt();
		int idFeatherRed = config.getItem("Red Feather", 4265).getInt();
		int idDartBlue = config.getItem("Blue Dart", 4266).getInt();
		int idDartYellow = config.getItem("Yellow Dart", 4267).getInt();
		int idDartRed = config.getItem("Red Dart", 4268).getInt();
		int idDartShooter = config.getItem("Dart Shooter", 4269).getInt();
		int idHyenaBoneShard = config.getItem("Hyena Bone Shard", 4270).getInt();
		int idZebraBoots = config.getItem("Zebra Boots", 4271).getInt();
		int idZebraHide = config.getItem("Zebra Hide", 4272).getInt();
		int idItemGrindingBowl = config.getItem("Grinding Bowl", 4273).getInt();
		int idMangoDust = config.getItem("Ground Mango", 4274).getInt();
		int idDartBlack = config.getItem("Outlandish Dart", 4275).getInt();
		int idFeatherBlack = config.getItem("Vulture Feather", 4276).getInt();
		int idShovel = config.getItem("Pridestone Shovel", 4277).getInt();
		int idPickaxe = config.getItem("Pridestone Pickaxe", 4278).getInt();
		int idAxe = config.getItem("Pridestone Axe", 4279).getInt();
		int idSword = config.getItem("Pridestone Sword", 4280).getInt();
		int idHoe = config.getItem("Pridestone Hoe", 4281).getInt();
		int idItemTermite = config.getItem("Exploding Termite", 4282).getInt();
		int idScarRug = config.getItem("Scar Rug", 4283).getInt();
		int idJar = config.getItem("Pridestone Jar", 4284).getInt();
		int idJarWater = config.getItem("Jar of Water", 4285).getInt();
		int idSilver = config.getItem("Silver Ingot", 4286).getInt();
		int idSilverDartShooter = config.getItem("Silver Dart Shooter", 4287).getInt();
		int idShovelSilver = config.getItem("Silver Shovel", 4288).getInt();
		int idPickaxeSilver = config.getItem("Silver Pickaxe", 4289).getInt();
		int idAxeSilver = config.getItem("Silver Axe", 4290).getInt();
		int idSwordSilver = config.getItem("Silver Sword", 4291).getInt();
		int idHoeSilver = config.getItem("Silver Hoe", 4292).getInt();
		int idRafikiCoin = config.getItem("Rafiki Coin", 4293).getInt();
		int idTermiteDust = config.getItem("Ground Termite", 4294).getInt();
		int idLionDust = config.getItem("Rafiki Dust", 4295).getInt();
		int idTunnahDiggah = config.getItem("Tunnah Diggah", 4296).getInt();
		int idCrystal = config.getItem("Hakuna Matata Crystal", 4297).getInt();
		int idBug = config.getItem("Bug", 4298).getInt();
		int idChocolateMufasa = config.getItem("Chocolate Mufasa", 4299).getInt();
		int idPumbaaBomb = config.getItem("Pumbaa Flatulence", 4300).getInt();
		int idFur = config.getItem("Lion Fur", 4301).getInt();
		int idJarMilk = config.getItem("Zebra Milk", 4302).getInt();
		int idZebraRaw = config.getItem("Raw Zebra", 4303).getInt();
		int idZebraCooked = config.getItem("Zebra Chop", 4304).getInt();
		int idRhinoRaw = config.getItem("Raw Rhino", 4305).getInt();
		int idRhinoCooked = config.getItem("Cooked Rhino", 4306).getInt();
		int idHelmetSilver = config.getItem("Silver Helmet", 4307).getInt();
		int idBodySilver = config.getItem("Silver Chestplate", 4308).getInt();
		int idLegsSilver = config.getItem("Silver Leggings", 4309).getInt();
		int idBootsSilver = config.getItem("Silver Boots", 4310).getInt();
		int idVase = config.getItem("Flower Vase Items", 4311).getInt();
		int idHorn = config.getItem("Rhino Horn", 4312).getInt();
		int idHornGround = config.getItem("Ground Rhino Horn", 4313).getInt();
		int idBed = config.getItem("Bed", 4314).getInt();
		int idGemsbokHide = config.getItem("Gemsbok Hide", 4315).getInt();
		int idGemsbokHorn = config.getItem("Gemsbok Horn", 4316).getInt();
		int idGemsbokSpear = config.getItem("Gemsbok Spear", 4317).getInt();
		int idJuice = config.getItem("Mango Juice", 4318).getInt();
		int idHelmetGemsbok = config.getItem("Gemsbok Helmet", 4319).getInt();
		int idBodyGemsbok = config.getItem("Gemsbok Chestplate", 4320).getInt();
		int idLegsGemsbok = config.getItem("Gemsbok Leggings", 4321).getInt();
		int idBootsGemsbok = config.getItem("Gemsbok Boots", 4322).getInt();
		int idJarLava = config.getItem("Jar of Lava", 4323).getInt();
		int idPeacockGem = config.getItem("Peacock Gem", 4324).getInt();
		int idShovelPeacock = config.getItem("Peacock Shovel", 4325).getInt();
		int idPickaxePeacock = config.getItem("Peacock Pickaxe", 4326).getInt();
		int idAxePeacock = config.getItem("Peacock Axe", 4327).getInt();
		int idSwordPeacock = config.getItem("Peacock Sword", 4328).getInt();
		int idHoePeacock = config.getItem("Peacock Hoe", 4329).getInt();
		int idHelmetPeacock = config.getItem("Peacock Helmet", 4330).getInt();
		int idBodyPeacock = config.getItem("Peacock Chestplate", 4331).getInt();
		int idLegsPeacock = config.getItem("Peacock Leggings", 4332).getInt();
		int idBootsPeacock = config.getItem("Peacock Boots", 4333).getInt();
		int idRugDye = config.getItem("Rug Dyes", 4334).getInt();
		int idWings = config.getItem("Peacock Wings", 4335).getInt();
		int idCorn = config.getItem("Corn", 4336).getInt();
		int idCornStalk = config.getItem("Maize Stalks", 4337).getInt();
		int idPopcorn = config.getItem("Popcorn", 4338).getInt();
		int idNukaShard = config.getItem("Nuka Shard", 4339).getInt();
		int idOutlanderFur = config.getItem("Outlander Fur", 4340).getInt();
		int idOutlanderMeat = config.getItem("Outlander Meat", 4341).getInt();
		int idPassionFruit = config.getItem("Passion Fruit", 4342).getInt();
		int idRedFlower = config.getItem("Red Flower", 4343).getInt();
		int idKivulite = config.getItem("Kivulite Ingot", 4344).getInt();
		int idShovelKivulite = config.getItem("Kivulite Shovel", 4345).getInt();
		int idPickaxeKivulite = config.getItem("Kivulite Pickaxe", 4346).getInt();
		int idAxeKivulite = config.getItem("Kivulite Axe", 4347).getInt();
		int idSwordKivulite = config.getItem("Kivulite Sword", 4348).getInt();
		int idBugStew = config.getItem("Bug Stew", 4349).getInt();
		int idCrocodileMeat = config.getItem("Crocodile Meat", 4350).getInt();
		int idPoison = config.getItem("Poison Powder", 4351).getInt();
		int idPoisonedSpear = config.getItem("Poisoned Spear", 4352).getInt();
		int idXpGrub = config.getItem("Experience Grub", 4353).getInt();
		int idShovelCorrupt = config.getItem("Corrupt Pridestone Shovel", 4354).getInt();
		int idPickaxeCorrupt = config.getItem("Corrupt Pridestone Pickaxe", 4355).getInt();
		int idAxeCorrupt = config.getItem("Corrupt Pridestone Axe", 4356).getInt();
		int idSwordCorrupt = config.getItem("Corrupt Pridestone Sword", 4357).getInt();
		int idCharm = config.getItem("Astral Charm", 4358).getInt();
		int idZazuEgg = config.getItem("Zazu Egg", 4359).getInt();
		int idKiwano = config.getItem("Kiwano Slice", 4360).getInt();
		int idKiwanoSeeds = config.getItem("Kiwano Seeds", 4361).getInt();
		int idTicketLionHead = config.getItem("Ticket Lion Head", 4362).getInt();
		int idTicketLionSuit = config.getItem("Ticket Lion Suit", 4363).getInt();
		int idTicketLionLegs = config.getItem("Ticket Lion Legs", 4364).getInt();
		int idTicketLionFeet = config.getItem("Ticket Lion Feet", 4365).getInt();
		int idQuestBook = config.getItem("Book of Quests", 4366).getInt();
		int idOutlandsHelm = config.getItem("Outlandish Helm", 4367).getInt();
		int idDartQuiver = config.getItem("Dart Quiver", 4368).getInt();
		int idOutlandsFeather = config.getItem("Wayward Feather", 4369).getInt();
		int idZiraRug = config.getItem("Zira Rug", 4370).getInt();
		int idZiraCoin = config.getItem("Zira Coin", 4371).getInt();
		int idHyenaHeadItem = config.getItem("Hyena Head", 4372).getInt();
		int idAmulet = config.getItem("Animalspeak Amulet", 4373).getInt();
		int idMountedShooterItem = config.getItem("Mounted Dart Shooter Item", 4374).getInt();
		int idStaff = config.getItem("Rhythm Staff", 4375).getInt();
		int idNote = config.getItem("Musical Notes", 4376).getInt();
		int idGiraffeSaddle = config.getItem("Giraffe Saddle", 4377).getInt();
		int idSpawnEgg = config.getItem("Spawn Eggs", 4378).getInt();
		int idTie = config.getItem("Giraffe Tie", 4379).getInt();
		int idYam = config.getItem("Yam", 4380).getInt();
		int idRoastYam = config.getItem("Roast Yam", 4381).getInt();
		int idBanana = config.getItem("Banana", 4382).getInt();
		int idBananaCake = config.getItem("Banana Cake", 4383).getInt();
		int idFeatherPink = config.getItem("Flamingo Feather", 4384).getInt();
		int idDartPink = config.getItem("Flamingo Dart", 4385).getInt();
		int idBananaBread = config.getItem("Banana Bread", 4386).getInt();
		int idHyenaMeal = config.getItem("Hyena Meal", 4387).getInt();
		int idCornKernels = config.getItem("Corn Kernels", 4388).getInt();
		int idDriedMaize = config.getItem("Dried Maize", 4389).getInt();
		
		planks = new LKBlockPlanks(idPlanks).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:planks");
		
		lionPortalFrame = new LKBlockPortalFrame(idLionPortalFrame).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:portalFrame");
		lionPortal = new LKBlockPortal(idLionPortal).setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundGlassFootstep).setLightValue(0.75F).setUnlocalizedName("lionking:portal");
		woodSlabDouble = new LKBlockWoodSlab(idWoodSlabDouble, true).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:woodSlabDouble");
		whiteFlower = new LKBlockFlower(idWhiteFlower).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:whiteFlower");
		forestLeaves = new LKBlockLeaves(idForestLeaves).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:rainforestLeaves");
		forestSapling = new LKBlockSapling(idForestSapling).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:rainforestSapling");
		flowerTop = new LKBlockFlowerTop(idFlowerTop).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:tallFlowerTop");
		flowerBase = new LKBlockFlowerBase(idFlowerBase).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:tallFlowerBase");
		mangoLeaves = new LKBlockLeaves(idMangoLeaves).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:mangoLeaves");
		mangoSapling = new LKBlockSapling(idMangoSapling).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:mangoSapling");
		grindBowl = new LKBlockGrindingBowl(idGrindBowl).setHardness(1.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:bowlBlock");
		rafikiWood = new LKBlockRafikiWood(idRafikiWood).setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:rafikiWood");
		rafikiLeaves = new LKBlockRafikiLeaves(idRafikiLeaves).setBlockUnbreakable().setResistance(6000000F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:rafikiLeaves");
		woodSlabSingle = new LKBlockWoodSlab(idWoodSlabSingle, false).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:woodSlabSingle");
		outlandsPortalFrame = new LKBlockPortalFrame(idOutlandsPortalFrame).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:outlandsPortalFrame");
		outlandsPortal = new LKBlockOutlandsPortal(idOutlandsPortal).setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundGlassFootstep).setLightValue(0.75F).setUnlocalizedName("lionking:outlandsPortal");
		bugTrap = new LKBlockBugTrap(idBugTrap).setHardness(1.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:bugTrap");
		pridestone =  new LKBlockPridestone(idPridestone).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:pridestone");
		prideCoal = new LKBlockOre(idPrideCoal).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:oreCoal");
		prideBrick = new LKBlockPrideBrick(idPrideBrick).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:pridestoneBrick");
		pridePillar = new LKBlockPillar(idPridePillar).setHardness(1.2F).setResistance(8.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:pillar"); 
		sapling = new LKBlockSapling(idSapling).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:acaciaSapling");  
		leaves = new LKBlockLeaves(idLeaves).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:acaciaLeaves");
		termite = new LKBlockTermite(idTermite).setHardness(0.5F).setResistance(3.0F).setUnlocalizedName("lionking:termiteMound");
		stoneStairs = new LKBlockStairs(idStoneStairs, pridestone, 0).setUnlocalizedName("lionking:stairsStone");
		brickStairs = new LKBlockStairs(idBrickStairs, prideBrick, 0).setUnlocalizedName("lionking:stairsBrick");
		prideBrickMossy = new LKBlockPrideBrickVariant(idPrideBrickMossy).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:pridestoneBrickMossy");
		oreSilver = new LKBlockOre(idOreSilver).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:oreSilver");
		outsand = new LKBlockOutsand(idOutsand).setHardness(0.7F).setStepSound(Block.soundSandFootstep).setUnlocalizedName("lionking:outsand");  
		outglass = new LKBlockGlass(idOutglass, Material.glass, false).setHardness(0.4F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("lionking:outglass");
		outglassPane = new LKBlockPane(idOutglassPane, "lionking:outglass", "lionking:outsand", Material.glass, true).setHardness(0.4F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("lionking:outglassPane");
		starAltar = new LKBlockStarAltar(idStarAltar).setHardness(2.0F).setResistance(15.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:altar"); 
		slabSingle = new LKBlockSlab(idSlabSingle, false).setHardness(2.0F).setResistance(8.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:slab");
		slabDouble = new LKBlockSlab(idSlabDouble, true).setHardness(2.0F).setResistance(8.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:slabDouble").setCreativeTab(null);
		log = new LKBlockLog(idLog).setHardness(2.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:log");
		prideWood = new LKBlockWood(idPrideWood).setHardness(2.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:wood");
		blueFlower = new LKBlockFlower(idBlueFlower).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:blueFlower");
		drum = new LKBlockDrum(idDrum).setHardness(1.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:drum");
		flowerVase = new LKBlockVase(idFlowerVase).setHardness(0.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:vaseBlock");
		orePeacock = new LKBlockOre(idOrePeacock).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:orePeacock");
		blockSilver = new LKBlockOreStorage(idBlockSilver).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("lionking:blockSilver");
		blockPeacock = new LKBlockOreStorage(idBlockPeacock).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("lionking:blockPeacock");
		rug = new LKBlockRug(idRug).setHardness(0.3F).setStepSound(Block.soundClothFootstep).setUnlocalizedName("lionking:rug");
		maize = new LKBlockMaize(idMaize).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:maize");
		stoneStairsCorrupt = new LKBlockStairs(idStoneStairsCorrupt, pridestone, 1).setUnlocalizedName("lionking:stairsStoneCorrupt");
		brickStairsCorrupt = new LKBlockStairs(idBrickStairsCorrupt, prideBrick, 1).setUnlocalizedName("lionking:stairsBrickCorrupt");
		aridGrass = new LKBlockAridGrass(idAridGrass).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:aridGrass");
		tilledSand = new LKBlockTilledSand(idTilledSand).setHardness(0.6F).setStepSound(Block.soundGravelFootstep).setUnlocalizedName("lionking:tilledSand");
		kiwanoBlock = new LKBlockKiwano(idKiwanoBlock).setHardness(1.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:kiwanoBlock");
		kiwanoStem = new LKBlockKiwanoStem(idKiwanoStem).setHardness(0.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:stemKiwano");
		pressurePlate = new LKBlockPressurePlate(idPressurePlate, "lionking:pridestone", EnumMobType.mobs, Material.rock).setHardness(0.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:pressurePlate");
		button = new LKBlockButton(idButton).setHardness(0.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:button");
		lever = new LKBlockLever(idLever).setHardness(0.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:lever");
		outlandsPool = new LKBlockOutlandsPool(idOutlandsPool).setHardness(-1.0F).setResistance(6000000F).setLightOpacity(3).setUnlocalizedName("lionking:pool");
		outshroom = new LKBlockOutshroom(idOutshroom, false).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:outshroom");
		outshroomGlowing = new LKBlockOutshroom(idOutshroomGlowing, true).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:outshroomGlowing");
		pumbaaBox = new LKBlockPumbaaBox(idPumbaaBox).setHardness(1.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:box");
		outlandsAltar = new LKBlockOutlandsAltar(idOutlandsAltar).setBlockUnbreakable().setResistance(6000000F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:outlandsAltar");
		lily = new LKBlockLily(idLily).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:lily");
		stairsAcacia = new LKBlockStairs(idStairsAcacia, planks, 0).setUnlocalizedName("lionking:stairsAcacia");
		stairsRainforest = new LKBlockStairs(idStairsRainforest, planks, 1).setUnlocalizedName("lionking:stairsRainforest");
		stairsMango = new LKBlockStairs(idStairsMango, planks, 2).setUnlocalizedName("lionking:stairsMango");
		blockBed = new LKBlockBed(idBlockBed).setHardness(0.2F).setUnlocalizedName("lionking:bed");
		hyenaHead = new LKBlockHyenaHead(idHyenaHead).setHardness(1.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:hyenaHead");
		mountedShooter = new LKBlockMountedShooter(idMountedShooter).setHardness(0.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:mountedShooter");
		passionSapling = new LKBlockSapling(idPassionSapling).setHardness(0.0F).setLightValue(0.75F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:passionSapling");
		passionLeaves = new LKBlockLeaves(idPassionLeaves).setHardness(0.2F).setLightOpacity(1).setLightValue(0.75F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:passionLeaves");
		stairsPassion = new LKBlockStairs(idStairsPassion, planks, 3).setUnlocalizedName("lionking:stairsPassion");
		hyenaTorch = new LKBlockHyenaTorch(idHyenaTorch).setHardness(0.0F).setLightValue(0.875F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("lionking:hyenaBoneTorch");
		wall = new LKBlockWall(idWall).setUnlocalizedName("lionking:stoneWall");
		yamCrops = new LKBlockYam(idYamCrops).setUnlocalizedName("lionking:yam");
		stairsBanana = new LKBlockStairs(idStairsBanana, planks, 4).setUnlocalizedName("lionking:stairsBanana");
		prideWood2 = new LKBlockWood2(idPrideWood2).setHardness(2.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:wood2");
		bananaSapling = new LKBlockSapling(idBananaSapling).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:bananaSapling");
		bananaLeaves = new LKBlockLeaves(idBananaLeaves).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:bananaLeaves");
		bananaCakeBlock = new LKBlockBananaCake(idBananaCakeBlock).setHardness(0.5F).setStepSound(Block.soundClothFootstep).setUnlocalizedName("lionking:bananaCake");
		hangingBanana = new LKBlockHangingBanana(idHangingBanana).setHardness(0.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("lionking:hangingBanana");
		stairsDeadwood = new LKBlockStairs(idStairsDeadwood, planks, 5).setUnlocalizedName("lionking:stairsDeadwood");
		mobSpawner = new LKBlockMobSpawner(idMobSpawner).setHardness(5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("lionking.mobSpawner");
		driedMaizeBlock = new LKBlock(idDriedMaizeBlock, Material.grass).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:driedMaize");
		driedMaizeSlabSingle = new LKBlockMaizeSlab(idDriedMaizeSlabSingle, false).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:slabDriedMaizeSingle");
		driedMaizeSlabDouble = new LKBlockMaizeSlab(idDriedMaizeSlabDouble, true).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lionking:slabDriedMaizeDouble").setCreativeTab(null);
		stairsDriedMaize = new LKBlockStairs(idStairsDriedMaize, driedMaizeBlock, 0).setUnlocalizedName("lionking:stairsDriedMaize");

		ticket = new LKItemTicket(idTicket - 256).setUnlocalizedName("lionking:ticket");
		hyenaBone = new LKItem(idHyenaBone - 256).setFull3D().setUnlocalizedName("lionking:hyenaBone").setCreativeTab(LKCreativeTabs.tabMaterials);
		lionRaw = new LKItemFood(idLionRaw - 256, 3, 0.3F, true).setUnlocalizedName("lionking:lionRaw");
		lionCooked = new LKItemFood(idLionCooked - 256, 8, 0.8F, true).setUnlocalizedName("lionking:lionCooked");
		rafikiStick = new LKItemRafikiStick(idRafikiStick - 256).setUnlocalizedName("lionking:rafikiStick");
		purpleFlower = new LKItemTallFlower(idPurpleFlower - 256, 0).setUnlocalizedName("lionking:purpleFlower");
		mango = new LKItemFood(idMango - 256, 3, 0.3F, false).setUnlocalizedName("lionking:mango");
		featherBlue = new LKItem(idFeatherBlue - 256).setUnlocalizedName("lionking:featherBlue").setCreativeTab(LKCreativeTabs.tabMaterials); 
		featherYellow = new LKItem(idFeatherYellow - 256).setUnlocalizedName("lionking:featherYellow").setCreativeTab(LKCreativeTabs.tabMaterials); 
		featherRed = new LKItem(idFeatherRed - 256).setUnlocalizedName("lionking:featherRed").setCreativeTab(LKCreativeTabs.tabMaterials); 
		dartBlue = new LKItem(idDartBlue - 256).setUnlocalizedName("lionking:dartBlue").setCreativeTab(LKCreativeTabs.tabCombat); 
		dartYellow = new LKItem(idDartYellow - 256).setUnlocalizedName("lionking:dartYellow").setCreativeTab(LKCreativeTabs.tabCombat); 
		dartRed = new LKItem(idDartRed - 256).setUnlocalizedName("lionking:dartRed").setCreativeTab(LKCreativeTabs.tabCombat); 
		dartShooter = new LKItemDartShooter(idDartShooter - 256, false).setUnlocalizedName("lionking:dartShooter"); 
		hyenaBoneShard = new LKItem(idHyenaBoneShard - 256).setUnlocalizedName("lionking:hyenaBoneShard").setCreativeTab(LKCreativeTabs.tabMaterials);
		zebraBoots = new LKItemSpecialArmor(idZebraBoots - 256, armorGemsbok, 3, 2000).setUnlocalizedName("lionking:zebraBoots").setCreativeTab(LKCreativeTabs.tabMisc);
		zebraHide = new LKItem(idZebraHide - 256).setUnlocalizedName("lionking:zebraHide").setCreativeTab(LKCreativeTabs.tabMaterials);
		itemGrindingBowl = new LKItemBlockPlacer(idItemGrindingBowl - 256, grindBowl).setUnlocalizedName("lionking:grindingBowl");
		mangoDust = new LKItem(idMangoDust - 256).setUnlocalizedName("lionking:mangoGround").setCreativeTab(LKCreativeTabs.tabMaterials);
		dartBlack = new LKItem(idDartBlack - 256).setUnlocalizedName("lionking:dartOutlandish").setCreativeTab(LKCreativeTabs.tabCombat);
		featherBlack = new LKItem(idFeatherBlack - 256).setUnlocalizedName("lionking:featherVulture").setCreativeTab(LKCreativeTabs.tabMaterials); 
		shovel = new LKItemShovel(idShovel - 256, toolPridestone).setUnlocalizedName("lionking:shovelPridestone");
		pickaxe = new LKItemPickaxe(idPickaxe - 256, toolPridestone).setUnlocalizedName("lionking:pickaxePridestone");
		axe = new LKItemAxe(idAxe - 256, toolPridestone).setUnlocalizedName("lionking:axePridestone");
		sword = new LKItemSword(idSword - 256, toolPridestone).setUnlocalizedName("lionking:swordPridestone");
		hoe = new LKItemHoe(idHoe - 256, toolPridestone).setUnlocalizedName("lionking:hoePridestone");
		itemTermite = new LKItemTermite(idItemTermite - 256).setUnlocalizedName("lionking:termite");
		scarRug = new LKItemScarRug(idScarRug - 256, 0).setUnlocalizedName("lionking:scarRug");
		jar = new LKItemJar(idJar - 256, 0).setUnlocalizedName("lionking:jar");
		jarWater = new LKItemJar(idJarWater - 256, Block.waterMoving.blockID).setUnlocalizedName("lionking:jarWater");
		silver = new LKItem(idSilver - 256).setUnlocalizedName("lionking:silver").setCreativeTab(LKCreativeTabs.tabMaterials);
		silverDartShooter = new LKItemDartShooter(idSilverDartShooter - 256, true).setUnlocalizedName("lionking:dartShooterSilver"); 
		shovelSilver = new LKItemShovel(idShovelSilver - 256, toolSilver).setUnlocalizedName("lionking:shovelSilver");
		pickaxeSilver = new LKItemPickaxe(idPickaxeSilver - 256, toolSilver).setUnlocalizedName("lionking:pickaxeSilver");
		axeSilver = new LKItemAxe(idAxeSilver - 256, toolSilver).setUnlocalizedName("lionking:axeSilver");
		swordSilver = new LKItemSword(idSwordSilver - 256, toolSilver).setUnlocalizedName("lionking:swordSilver");
		hoeSilver = new LKItemHoe(idHoeSilver - 256, toolSilver).setUnlocalizedName("lionking:hoeSilver");
		rafikiCoin = new LKItemCoin(idRafikiCoin - 256, (byte)0).setUnlocalizedName("lionking:rafikiCoin");
		termiteDust = new LKItem(idTermiteDust - 256).setUnlocalizedName("lionking:termiteGround").setCreativeTab(LKCreativeTabs.tabMaterials);
		lionDust = new LKItemDust(idLionDust - 256).setUnlocalizedName("lionking:rafikiDust");
		tunnahDiggah = new LKItemTunnahDiggah(idTunnahDiggah - 256).setUnlocalizedName("lionking:tunnahDiggah");
		crystal = new LKItem(idCrystal - 256).setUnlocalizedName("lionking:crystal").setMaxStackSize(16).setCreativeTab(LKCreativeTabs.tabQuest);;
		bug = new LKItem(idBug - 256).setUnlocalizedName("lionking:bug");
		chocolateMufasa = new LKItemFood(idChocolateMufasa - 256, 16, 0.8F, false).setUnlocalizedName("lionking:chocolateMufasa");
		pumbaaBomb = new LKItemPumbaaBomb(idPumbaaBomb - 256).setUnlocalizedName("lionking:pumbaaFlatulence");
		fur = new LKItem(idFur - 256).setUnlocalizedName("lionking:lionFur").setCreativeTab(LKCreativeTabs.tabMaterials);
		jarMilk = new LKItemZebraMilk(idJarMilk - 256).setUnlocalizedName("lionking:jarMilk").setContainerItem(jar);
		zebraRaw = new LKItemFood(idZebraRaw - 256, 2, 0.1F, true).setUnlocalizedName("lionking:zebraRaw");
		zebraCooked = new LKItemFood(idZebraCooked - 256, 6, 0.4F, true).setUnlocalizedName("lionking:zebraCooked");
		rhinoRaw = new LKItemFood(idRhinoRaw - 256, 2, 0.1F, true).setUnlocalizedName("lionking:rhinoRaw");
		rhinoCooked = new LKItemFood(idRhinoCooked - 256, 7, 0.4F, true).setUnlocalizedName("lionking:rhinoCooked");
		helmetSilver = new LKItemArmor(idHelmetSilver - 256, armorSilver, 0, 0).setUnlocalizedName("lionking:helmetSilver");
		bodySilver = new LKItemArmor(idBodySilver - 256, armorSilver, 0, 1).setUnlocalizedName("lionking:bodySilver");
		legsSilver = new LKItemArmor(idLegsSilver - 256, armorSilver, 0, 2).setUnlocalizedName("lionking:legsSilver");
		bootsSilver = new LKItemArmor(idBootsSilver - 256, armorSilver, 0, 3).setUnlocalizedName("lionking:bootsSilver");
		vase = new LKItemVase(idVase - 256).setUnlocalizedName("lionking:vase");
		horn = new LKItem(idHorn - 256).setUnlocalizedName("lionking:rhinoHorn").setCreativeTab(LKCreativeTabs.tabMaterials);
		hornGround = new LKItemGroundRhinoHorn(idHornGround - 256).setUnlocalizedName("lionking:rhinoHornGround");
		bed = new LKItemBed(idBed - 256).setUnlocalizedName("lionking:bed");
		gemsbokHide = new LKItem(idGemsbokHide - 256).setUnlocalizedName("lionking:gemsbokHide").setCreativeTab(LKCreativeTabs.tabMaterials);
		gemsbokHorn = new LKItem(idGemsbokHorn - 256).setUnlocalizedName("lionking:gemsbokHorn").setCreativeTab(LKCreativeTabs.tabMaterials);
		gemsbokSpear = new LKItemSpear(idGemsbokSpear - 256, false).setUnlocalizedName("lionking:gemsbokSpear");
		juice = new LKItemMangoJuice(idJuice - 256).setUnlocalizedName("lionking:jarMangoJuice").setContainerItem(jar);
		helmetGemsbok = new LKItemArmor(idHelmetGemsbok - 256, armorGemsbok, 1, 0).setUnlocalizedName("lionking:helmetGemsbok");
		bodyGemsbok = new LKItemArmor(idBodyGemsbok - 256, armorGemsbok, 1, 1).setUnlocalizedName("lionking:bodyGemsbok");
		legsGemsbok = new LKItemArmor(idLegsGemsbok - 256, armorGemsbok, 1, 2).setUnlocalizedName("lionking:legsGemsbok");
		bootsGemsbok = new LKItemArmor(idBootsGemsbok - 256, armorGemsbok, 1, 3).setUnlocalizedName("lionking:bootsGemsbok");
		jarLava = new LKItemJar(idJarLava - 256, Block.lavaMoving.blockID).setUnlocalizedName("lionking:jarLava").setContainerItem(jar);
		peacockGem = new LKItem(idPeacockGem - 256).setUnlocalizedName("lionking:peacockGem").setCreativeTab(LKCreativeTabs.tabMaterials);
		shovelPeacock = new LKItemShovel(idShovelPeacock - 256, toolPeacock).setUnlocalizedName("lionking:shovelPeacock");
		pickaxePeacock = new LKItemPickaxe(idPickaxePeacock - 256, toolPeacock).setUnlocalizedName("lionking:pickaxePeacock");
		axePeacock = new LKItemAxe(idAxePeacock - 256, toolPeacock).setUnlocalizedName("lionking:axePeacock");
		swordPeacock = new LKItemSword(idSwordPeacock - 256, toolPeacock).setUnlocalizedName("lionking:swordPeacock");
		hoePeacock = new LKItemHoe(idHoePeacock - 256, toolPeacock).setUnlocalizedName("lionking:hoePeacock");
		helmetPeacock = new LKItemArmor(idHelmetPeacock - 256, armorPeacock, 2, 0).setUnlocalizedName("lionking:helmetPeacock");
		bodyPeacock = new LKItemArmor(idBodyPeacock - 256, armorPeacock, 2, 1).setUnlocalizedName("lionking:bodyPeacock");
		legsPeacock = new LKItemArmor(idLegsPeacock - 256, armorPeacock, 2, 2).setUnlocalizedName("lionking:legsPeacock");
		bootsPeacock = new LKItemArmor(idBootsPeacock - 256, armorPeacock, 2, 3).setUnlocalizedName("lionking:bootsPeacock");
		rugDye = new LKItemRugDye(idRugDye - 256).setUnlocalizedName("lionking:dye");
		wings = new LKItemSpecialArmor(idWings - 256, armorGemsbok, 1, 260).setUnlocalizedName("lionking:peacockWings").setCreativeTab(LKCreativeTabs.tabMisc);
		corn = new LKItemFood(idCorn - 256, 1, 0.1F, false).setUnlocalizedName("lionking:corn");
		cornStalk = new LKItemBlockPlacer(idCornStalk - 256, maize).setUnlocalizedName("lionking:maizeStalks").setCreativeTab(LKCreativeTabs.tabMaterials);
		popcorn = new LKItemFood(idPopcorn - 256, 3, 0.4F, false).setUnlocalizedName("lionking:popcorn");
		nukaShard = new LKItem(idNukaShard - 256).setUnlocalizedName("lionking:nukaShard").setCreativeTab(LKCreativeTabs.tabMaterials);
		outlanderFur = new LKItem(idOutlanderFur - 256).setUnlocalizedName("lionking:outlanderFur").setCreativeTab(LKCreativeTabs.tabMaterials);
		outlanderMeat = new LKItemFood(idOutlanderMeat - 256, 3, 0.2F, true).setPotionEffect(Potion.hunger.id, 30, 0, 0.5F).setUnlocalizedName("lionking:outlanderMeat");
		passionFruit = new LKItemPassionFruit(idPassionFruit - 256).setUnlocalizedName("lionking:passionFruit");
		redFlower = new LKItemTallFlower(idRedFlower - 256, 1).setUnlocalizedName("lionking:redFlower");
		kivulite = new LKItem(idKivulite - 256).setUnlocalizedName("lionking:kivulite").setCreativeTab(LKCreativeTabs.tabMaterials);
		shovelKivulite = new LKItemShovelFire(idShovelKivulite - 256, toolKivulite).setUnlocalizedName("lionking:shovelKivulite");
		pickaxeKivulite = new LKItemPickaxeFire(idPickaxeKivulite - 256, toolKivulite).setUnlocalizedName("lionking:pickaxeKivulite");
		axeKivulite = new LKItemAxeFire(idAxeKivulite - 256, toolKivulite).setUnlocalizedName("lionking:axeKivulite");
		swordKivulite = new LKItemSwordFire(idSwordKivulite - 256, toolKivulite).setUnlocalizedName("lionking:swordKivulite");
		bugStew = new LKItemBugStew(idBugStew - 256).setUnlocalizedName("lionking:bugStew");
		crocodileMeat = new LKItemFood(idCrocodileMeat - 256, 4, 0.4F, true).setPotionEffect(Potion.hunger.id, 15, 0, 0.4F).setUnlocalizedName("lionking:crocodileMeat");
		poison = new LKItem(idPoison - 256).setUnlocalizedName("lionking:poison").setCreativeTab(LKCreativeTabs.tabMaterials);
		poisonedSpear = new LKItemSpear(idPoisonedSpear - 256, true).setUnlocalizedName("lionking:gemsbokSpearPoisoned");
		xpGrub = new LKItemXpGrub(idXpGrub - 256).setUnlocalizedName("lionking:experienceGrub");
		shovelCorrupt = new LKItemShovelCorrupt(idShovelCorrupt - 256, toolCorrupt).setUnlocalizedName("lionking:shovelCorruptPridestone");
		pickaxeCorrupt = new LKItemPickaxeCorrupt(idPickaxeCorrupt - 256, toolCorrupt).setUnlocalizedName("lionking:pickaxeCorruptPridestone");
		axeCorrupt = new LKItemAxeCorrupt(idAxeCorrupt - 256, toolCorrupt).setUnlocalizedName("lionking:axeCorruptPridestone");
		swordCorrupt = new LKItemSwordCorrupt(idSwordCorrupt - 256, toolCorrupt).setUnlocalizedName("lionking:swordCorruptPridestone");
		charm = new LKItemSimbaCharm(idCharm - 256).setUnlocalizedName("lionking:charm");
		zazuEgg = new LKItemZazuEgg(idZazuEgg - 256).setUnlocalizedName("lionking:zazuEgg").setCreativeTab(LKCreativeTabs.tabMaterials);
		kiwano = new LKItemFood(idKiwano - 256, 2, 0.3F, false).setUnlocalizedName("lionking:kiwano");
		kiwanoSeeds = new LKItemSeeds(idKiwanoSeeds - 256, kiwanoStem.blockID, tilledSand.blockID).setUnlocalizedName("lionking:kiwanoSeeds");
		ticketLionHead = new LKItemTicketLionSuit(idTicketLionHead - 256, 0).setUnlocalizedName("lionking:helmetTicketLion");
		ticketLionSuit = new LKItemTicketLionSuit(idTicketLionSuit - 256, 1).setUnlocalizedName("lionking:bodyTicketLion");
		ticketLionLegs = new LKItemTicketLionSuit(idTicketLionLegs - 256, 2).setUnlocalizedName("lionking:legsTicketLion");
		ticketLionFeet = new LKItemTicketLionSuit(idTicketLionFeet - 256, 3).setUnlocalizedName("lionking:bootsTicketLion");
		questBook = new LKItemQuestBook(idQuestBook - 256).setUnlocalizedName("lionking:questBook");
		outlandsHelm = new LKItemOutlandsHelm(idOutlandsHelm - 256).setUnlocalizedName("lionking:outlandishHelm");
		dartQuiver = new LKItemDartQuiver(idDartQuiver - 256).setUnlocalizedName("lionking:quiver");
		outlandsFeather = new LKItemOutlandsFeather(idOutlandsFeather - 256).setUnlocalizedName("lionking:waywardFeather");
		ziraRug = new LKItemScarRug(idZiraRug - 256, 1).setUnlocalizedName("lionking:ziraRug");
		ziraCoin = new LKItemCoin(idZiraCoin - 256, (byte)1).setUnlocalizedName("lionking:ziraCoin");
		hyenaHeadItem = new LKItemHyenaHead(idHyenaHeadItem - 256).setUnlocalizedName("lionking:hyenaHead");
		amulet = new LKItemAmulet(idAmulet - 256).setUnlocalizedName("lionking:amulet");
		mountedShooterItem = new LKItemMountedShooter(idMountedShooterItem - 256).setUnlocalizedName("lionking:mountedShooter");
		staff = new LKItemStaff(idStaff - 256).setUnlocalizedName("lionking:rhythmStaff");
		note = new LKItemNote(idNote - 256).setUnlocalizedName("lionking:note");
		giraffeSaddle = new LKItemGiraffeSaddle(idGiraffeSaddle - 256).setUnlocalizedName("lionking:giraffeSaddle");
		spawnEgg = new LKItemSpawnEgg(idSpawnEgg - 256).setUnlocalizedName("monsterPlacer");
		tie = new LKItemGiraffeTie(idTie - 256).setUnlocalizedName("lionking:giraffeTie");
		yam = new LKItemYam(idYam - 256).setUnlocalizedName("lionking:yam");
		roastYam = new LKItemFood(idRoastYam - 256, 6, 0.6F, false).setUnlocalizedName("lionking:yamRoast");
		banana = new LKItemBanana(idBanana - 256, 2, 0.3F, false).setUnlocalizedName("lionking:banana");
		bananaCake = new LKItemBlockPlacer(idBananaCake - 256, bananaCakeBlock).setMaxStackSize(1).setUnlocalizedName("lionking:bananaCake").setCreativeTab(LKCreativeTabs.tabFood);
		featherPink = new LKItem(idFeatherPink - 256).setUnlocalizedName("lionking:featherFlamingo").setCreativeTab(LKCreativeTabs.tabMaterials);
		dartPink = new LKItem(idDartPink - 256).setUnlocalizedName("lionking:dartFlamingo").setCreativeTab(LKCreativeTabs.tabCombat);
		bananaBread = new LKItemFood(idBananaBread - 256, 3, 0.5F, false).setUnlocalizedName("lionking:bananaBread");
		hyenaMeal = new LKItemHyenaMeal(idHyenaMeal - 256).setUnlocalizedName("lionking:hyenaMeal");
		cornKernels = new LKItem(idCornKernels - 256).setUnlocalizedName("lionking:cornKernels").setCreativeTab(LKCreativeTabs.tabMaterials);
		driedMaize = new LKItem(idDriedMaize - 256).setUnlocalizedName("lionking:driedMaize").setCreativeTab(LKCreativeTabs.tabMaterials);
		
		try
		{
			for (Field field : mod_LionKing.class.getFields())
			{
				if (field.get(null) instanceof Block)
				{
					Block block = (Block)field.get(null);
					field.set(null, block.setTextureName(block.getUnlocalizedName().substring(5)));
				}
				
				if (field.get(null) instanceof Item)
				{
					Item item = (Item)field.get(null);
					field.set(null, item.setTextureName(item.getUnlocalizedName().substring(5)));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		idPrideLands = config.get("dimension", "Pride Lands", -2).getInt();
		idOutlands = config.get("dimension", "Outlands", -3).getInt();
		idUpendi = config.get("dimension", "Upendi", -4).getInt();
		
		idHyenaEnchantment = config.get("enchantment", "Scourge of Hyenas", 194).getInt();
		idDiggahEnchantment = config.get("enchantment", "Biggah Diggah", 195).getInt();
		idRafikiStickDamage = config.get("enchantment", "Sharpness", 196).getInt();
		idRafikiStickDurability = config.get("enchantment", "Sturdy", 197).getInt();
		idRafikiStickThunder = config.get("enchantment", "Thunder", 198).getInt();
		idDiggahPrecision = config.get("enchantment", "Precision", 199).getInt();
		
		hyenaEnchantment = new LKEnchantmentHyena(idHyenaEnchantment, 5);
		diggahEnchantment = new LKEnchantmentTunnahDiggah(idDiggahEnchantment, 25).setName("big");
		rafikiStickDamage = new LKEnchantmentRafikiDamage(idRafikiStickDamage, 5);
		rafikiStickDurability = new LKEnchantmentRafikiDurability(idRafikiStickDurability, 5);
		rafikiStickThunder = new LKEnchantmentRafikiThunder(idRafikiStickThunder, 3);
		diggahPrecision = new LKEnchantmentTunnahDiggah(idDiggahPrecision, 10).setName("precision");
		
		boothLimit = config.get("general", "Minimum distance between ticket booths", 250).getInt();
		shouldCheckUpdates = config.get("general", "Check for updates", true).getBoolean(true);
		randomBooths = config.get("general", "Random ticket booths", false).getBoolean(false);
		lkMusicChance = config.get("general", "Lion King music percentage chance", 40).getInt();
		
		LKPrideLandsBiome.initBiomes(config);
		
		if (config.hasChanged())
		{
			config.save();
		}

		proxy.onPreload();
		LKAchievementList.preInitAchievements();
		AchievementPage.registerAchievementPage(new LKAchievementList());
	}
	
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		instance = this;
		
		LKCreativeTabs.setupIcons();
		
		registerBlock(lionPortalFrame, "Pride Lands Portal Frame");
		registerBlock(lionPortal, "Pride Lands Portal");
		Item.itemsList[woodSlabDouble.blockID] = (new ItemSlab(woodSlabDouble.blockID - 256, (BlockHalfSlab)woodSlabSingle, (BlockHalfSlab)woodSlabDouble, true)).setUnlocalizedName("lk.woodSlabDouble");
		registerBlock(whiteFlower, "Flower");
		registerBlock(forestLeaves, LKItemLeaves.class, "Rainforest Leaves");
		LanguageRegistry.addName(new ItemStack(forestLeaves, 1, 4), "Rainforest Leaves");
		registerBlock(forestSapling, "Rainforest Sapling");
		registerBlock(flowerTop);
		registerBlock(flowerBase);
		registerBlock(mangoLeaves, LKItemLeaves.class, "Mango Leaves");
		LanguageRegistry.addName(new ItemStack(mangoLeaves, 1, 4), "Mango Leaves");
		registerBlock(mangoSapling, "Mango Sapling");
		registerBlock(grindBowl);
		registerBlock(rafikiWood, LKItemMetadata.class, "Rafiki Wood");
		for (int i = 0; i <= 2; i++)
		{
			LanguageRegistry.addName(new ItemStack(rafikiWood, 1, i), "Rafiki Tree Wood");
		}
		registerBlock(rafikiLeaves, "Rafiki Leaves");
		Item.itemsList[woodSlabSingle.blockID] = (new ItemSlab(woodSlabSingle.blockID - 256, (BlockHalfSlab)woodSlabSingle, (BlockHalfSlab)woodSlabDouble, false)).setUnlocalizedName("lk.woodSlabSingle");
		for (int i = 0; i <= 5; i++)
		{
			LanguageRegistry.addName(new ItemStack(woodSlabSingle, 1, i), LKBlockWoodSlab.getSlabName(i));
		}
		registerBlock(outlandsPortalFrame, LKItemMetadata.class, "Outlands Portal Frame");
		LanguageRegistry.addName(new ItemStack(outlandsPortalFrame, 1, 0), "Outlands Portal Frame");
		LanguageRegistry.addName(new ItemStack(outlandsPortalFrame, 1, 1), "Zira Mound");
		LanguageRegistry.addName(new ItemStack(outlandsPortalFrame, 1, 2), "Zira Mound Gate");
		registerBlock(outlandsPortal, "Outlands Portal");
		registerBlock(bugTrap, "Bug Trap");
		registerBlock(pridestone, LKItemMetadata.class, "Pridestone");
		LanguageRegistry.addName(new ItemStack(pridestone, 1, 0), "Pridestone");
		LanguageRegistry.addName(new ItemStack(pridestone, 1, 1), "Corrupt Pridestone");
		registerBlock(prideCoal, LKItemMetadata.class, "Coal Ore");
		LanguageRegistry.addName(new ItemStack(prideCoal, 1, 0), "Coal Ore");
		LanguageRegistry.addName(new ItemStack(prideCoal, 1, 1), "Nuka Ore");
		registerBlock(prideBrick, LKItemMetadata.class, "Pridestone Brick");
		LanguageRegistry.addName(new ItemStack(prideBrick, 1, 0), "Pridestone Brick");
		LanguageRegistry.addName(new ItemStack(prideBrick, 1, 1), "Corrupt Pridestone Brick");
		registerBlock(pridePillar, LKItemMetadata.class, "Pridestone Pillar");
		for (int i = 0; i <= 7; i++)
		{
			LanguageRegistry.addName(new ItemStack(pridePillar, 1, i), i < 4 ? "Pridestone Pillar" : "Corrupt Pridestone Pillar");
		}
		registerBlock(sapling, "Acacia Sapling");
		registerBlock(leaves, LKItemLeaves.class, "Acacia Leaves");
		LanguageRegistry.addName(new ItemStack(leaves, 1, 4), "Acacia Leaves");
		registerBlock(termite, LKItemMetadata.class, "Termite Mound");
		LanguageRegistry.addName(new ItemStack(termite, 1, 0), "Infested Termite Mound");
		LanguageRegistry.addName(new ItemStack(termite, 1, 1), "Termite Mound");
		registerBlock(stoneStairs, "Pridestone Stairs");
		registerBlock(brickStairs, "Pridestone Brick Stairs");
		registerBlock(prideBrickMossy, LKItemMetadata.class, "Mossy Pridestone Brick");
		LanguageRegistry.addName(new ItemStack(prideBrickMossy, 1, 0), "Mossy Pridestone Brick");
		LanguageRegistry.addName(new ItemStack(prideBrickMossy, 1, 1), "Cracked Pridestone Brick");
		registerBlock(oreSilver, LKItemMetadata.class, "Silver Ore");
		LanguageRegistry.addName(new ItemStack(oreSilver, 1, 0), "Silver Ore");
		LanguageRegistry.addName(new ItemStack(oreSilver, 1, 1), "Kivulite Ore");
		registerBlock(outsand, "Outsand");
		registerBlock(outglass, "Outglass");
		registerBlock(outglassPane, "Outglass Pane");
		registerBlock(starAltar, LKItemQuestBlock.class, "Star Altar");
		LanguageRegistry.addName(new ItemStack(starAltar, 1, 0), "Star Altar");
		Item.itemsList[slabSingle.blockID] = (new ItemSlab(slabSingle.blockID - 256, (BlockHalfSlab)slabSingle, (BlockHalfSlab)slabDouble, false)).setUnlocalizedName("lk.slab");
		for (int i = 0; i <= 5; i++)
		{
			LanguageRegistry.addName(new ItemStack(slabSingle, 1, i), LKBlockSlab.getSlabName(i));
		}
		Item.itemsList[slabDouble.blockID] = (new ItemSlab(slabDouble.blockID - 256, (BlockHalfSlab)slabSingle, (BlockHalfSlab)slabDouble, true)).setUnlocalizedName("lk.slabDouble");
		registerBlock(log, "Log");
		registerBlock(prideWood, LKItemMetadata.class, "Wood");
		LanguageRegistry.addName(new ItemStack(prideWood, 1, 0), "Acacia Wood");
		LanguageRegistry.addName(new ItemStack(prideWood, 1, 1), "Rainforest Wood");
		LanguageRegistry.addName(new ItemStack(prideWood, 1, 2), "Mango Wood");
		LanguageRegistry.addName(new ItemStack(prideWood, 1, 3), "Passion Wood");
		registerBlock(blueFlower, "Flower");
		registerBlock(drum, LKItemMetadata.class, "lk.drum");
		for (int i = 0; i <= 5; i++)
		{
			LanguageRegistry.addName(new ItemStack(drum, 1, i), "Bongo Drum");
		}
		registerBlock(flowerVase, "Vase");
		registerBlock(orePeacock, "Peacock Ore");
		registerBlock(blockSilver, LKItemMetadata.class, "Ore Block");
		LanguageRegistry.addName(new ItemStack(blockSilver, 1, 0), "Block of Silver");
		LanguageRegistry.addName(new ItemStack(blockSilver, 1, 1), "Kivulite Block");
		registerBlock(blockPeacock, "Block of Peacock");
		registerBlock(rug, LKItemRug.class, "Rug");
		LanguageRegistry.addName(new ItemStack(rug, 1, 0), "Fur Rug");
		for (int i = 1; i <= 15; i++)
		{
			String s = LKBlockRug.colourNames[i];
			if (s != null)
			{
				LanguageRegistry.instance().addNameForObject(new ItemStack(rug, 1, i), "en_GB", s + " " + "Fur Rug");
				LanguageRegistry.instance().addNameForObject(new ItemStack(rug, 1, i), "en_CA", s + " " + "Fur Rug");
			}
			String s1 = LKBlockRug.colourNames_US[i];
			if (s1 != null)
			{
				LanguageRegistry.instance().addNameForObject(new ItemStack(rug, 1, i), "en_US", s1 + " " + "Fur Rug");
			}
		}
		registerBlock(maize);
		registerBlock(stoneStairsCorrupt, "Corrupt Pridestone Stairs");
		registerBlock(brickStairsCorrupt, "Corrupt Pridestone Brick Stairs");
		registerBlock(aridGrass, "Grass");
		registerBlock(tilledSand, "Tilled Sand");
		registerBlock(kiwanoBlock, "Kiwano");
		registerBlock(kiwanoStem, "Kiwano Stem");
		registerBlock(pressurePlate, "Pressure Plate");
		registerBlock(button, "Button");
		registerBlock(lever, "Lever");
		registerBlock(outlandsPool);
		registerBlock(outshroom, "Outshroom");
		registerBlock(outshroomGlowing, "Glowing Outshroom");
		registerBlock(pumbaaBox, LKItemQuestBlock.class, "Pumbbox");
		registerBlock(outlandsAltar);
		registerBlock(lily, LKItemLily.class, "Waterlily");
		LanguageRegistry.addName(new ItemStack(lily, 1, 0), "White Waterlily");
		LanguageRegistry.addName(new ItemStack(lily, 1, 1), "Violet Waterlily");
		LanguageRegistry.addName(new ItemStack(lily, 1, 2), "Red Waterlily");
		registerBlock(stairsAcacia, "Acacia Wood Stairs");
		registerBlock(stairsRainforest, "Rainforest Wood Stairs");
		registerBlock(stairsMango, "Mango Wood Stairs");
		registerBlock(blockBed);
		registerBlock(planks, LKItemMetadata.class, "Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 0), "Acacia Wood Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 1), "Rainforest Wood Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 2), "Mango Wood Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 3), "Passion Wood Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 4), "Banana Wood Planks");
		LanguageRegistry.addName(new ItemStack(planks, 1, 5), "Deadwood Planks");
		registerBlock(hyenaHead);
		registerBlock(mountedShooter);
		registerBlock(passionSapling, "Passion Sapling");
		registerBlock(passionLeaves, LKItemLeaves.class, "Passion Leaves");
		LanguageRegistry.addName(new ItemStack(passionLeaves, 1, 4), "Passion Leaves");
		registerBlock(stairsPassion, "Passion Wood Stairs");
		registerBlock(hyenaTorch, "Hyena Bone Torch");
		registerBlock(wall, LKItemMetadata.class, "Wall");
		LanguageRegistry.addName(new ItemStack(wall, 1, 0), "Pridestone Wall");
		LanguageRegistry.addName(new ItemStack(wall, 1, 1), "Pridestone Brick Wall");
		LanguageRegistry.addName(new ItemStack(wall, 1, 2), "Mossy Pridestone Brick Wall");
		LanguageRegistry.addName(new ItemStack(wall, 1, 3), "Corrupt Pridestone Wall");
		LanguageRegistry.addName(new ItemStack(wall, 1, 4), "Corrupt Pridestone Brick Wall");
		registerBlock(yamCrops);
		registerBlock(stairsBanana, "Banana Wood Stairs");
		registerBlock(prideWood2, LKItemMetadata.class, "Wood");
		LanguageRegistry.addName(new ItemStack(prideWood2, 1, 0), "Banana Wood");
		LanguageRegistry.addName(new ItemStack(prideWood2, 1, 1), "Deadwood");
		registerBlock(bananaSapling, "Banana Sapling");
		registerBlock(bananaLeaves, LKItemLeaves.class, "Banana Leaves");
		LanguageRegistry.addName(new ItemStack(bananaLeaves, 1, 4), "Banana Leaves");
		registerBlock(bananaCakeBlock);
		registerBlock(hangingBanana);
		registerBlock(stairsDeadwood, "Deadwood Stairs");
		registerBlock(mobSpawner);
		registerBlock(driedMaizeBlock, "Dried Maize Block");
		Item.itemsList[driedMaizeSlabSingle.blockID] = (new ItemSlab(driedMaizeSlabSingle.blockID - 256, (BlockHalfSlab)driedMaizeSlabSingle, (BlockHalfSlab)driedMaizeSlabDouble, false)).setUnlocalizedName("lk.driedMaizeSlabSingle");
		LanguageRegistry.addName(new ItemStack(driedMaizeSlabSingle, 1, 0), "Dried Maize Slab");
		Item.itemsList[driedMaizeSlabDouble.blockID] = (new ItemSlab(driedMaizeSlabDouble.blockID - 256, (BlockHalfSlab)driedMaizeSlabSingle, (BlockHalfSlab)driedMaizeSlabDouble, true)).setUnlocalizedName("lk.driedMaizeSlabDouble");
		registerBlock(stairsDriedMaize, "Dried Maize Stairs");
		
		LanguageRegistry.addName(ticket, "Lion King Ticket");
		LanguageRegistry.addName(new ItemStack(ticket, 1, 0), "Lion King Ticket");
		LanguageRegistry.addName(new ItemStack(ticket, 1, 1), "Christmas Cracker");
		LanguageRegistry.addName(hyenaBone, "Hyena Bone");
		LanguageRegistry.addName(lionRaw, "Raw Lion");
		LanguageRegistry.addName(lionCooked, "Lion Chop");
		LanguageRegistry.addName(rafikiStick, "Rafiki Stick");
		LanguageRegistry.addName(purpleFlower, "Flower");
		LanguageRegistry.addName(mango, "Mango");
		LanguageRegistry.addName(featherBlue, "Blue Feather");
		LanguageRegistry.addName(featherYellow, "Yellow Feather");
		LanguageRegistry.addName(featherRed, "Red Feather");
		LanguageRegistry.addName(dartBlue, "Blue Dart");
		LanguageRegistry.addName(dartYellow, "Yellow Dart");
		LanguageRegistry.addName(dartRed, "Red Dart");
		LanguageRegistry.addName(dartShooter, "Dart Shooter");
		LanguageRegistry.addName(hyenaBoneShard, "Hyena Bone Shard");
		LanguageRegistry.addName(zebraBoots, "Zebra Boots");
		LanguageRegistry.addName(zebraHide, "Zebra Hide");
		LanguageRegistry.addName(itemGrindingBowl, "Grinding Bowl");
		LanguageRegistry.addName(mangoDust, "Ground Mango");
		LanguageRegistry.addName(dartBlack, "Outlandish Dart");
		LanguageRegistry.addName(featherBlack, "Vulture Feather");
		LanguageRegistry.addName(shovel, "Pridestone Shovel");
		LanguageRegistry.addName(pickaxe, "Pridestone Pickaxe");
		LanguageRegistry.addName(axe, "Pridestone Axe");
		LanguageRegistry.addName(sword, "Pridestone Sword");
		LanguageRegistry.addName(hoe, "Pridestone Hoe");
		LanguageRegistry.addName(itemTermite, "Exploding Termite");
		LanguageRegistry.addName(scarRug, "Scar Rug");
		LanguageRegistry.addName(jar, "Pridestone Jar");
		LanguageRegistry.addName(jarWater, "Jar of Water");
		LanguageRegistry.addName(silver, "Silver Ingot");
		LanguageRegistry.addName(silverDartShooter, "Silver Dart Shooter");
		LanguageRegistry.addName(shovelSilver, "Silver Shovel");
		LanguageRegistry.addName(pickaxeSilver, "Silver Pickaxe");
		LanguageRegistry.addName(axeSilver, "Silver Axe");
		LanguageRegistry.addName(swordSilver, "Silver Sword");
		LanguageRegistry.addName(hoeSilver, "Silver Hoe");
		LanguageRegistry.addName(rafikiCoin, "Rafiki Coin");
		LanguageRegistry.addName(termiteDust, "Ground Termite");
		LanguageRegistry.addName(lionDust, "Rafiki Dust");
		LanguageRegistry.addName(tunnahDiggah, "Tunnah Diggah");
		LanguageRegistry.addName(crystal, "Hakuna Matata Crystal");
		LanguageRegistry.addName(bug, "Bug");
		LanguageRegistry.addName(chocolateMufasa, "Chocolate Mufasa");
		LanguageRegistry.addName(pumbaaBomb, "Pumbaa Flatulence");
		LanguageRegistry.addName(fur, "Lion Fur");
		LanguageRegistry.addName(jarMilk, "Zebra Milk");
		LanguageRegistry.addName(zebraRaw, "Raw Zebra");
		LanguageRegistry.addName(zebraCooked, "Zebra Chop");
		LanguageRegistry.addName(rhinoRaw, "Raw Rhino");
		LanguageRegistry.addName(rhinoCooked, "Cooked Rhino");
		LanguageRegistry.addName(helmetSilver, "Silver Helmet");
		LanguageRegistry.addName(bodySilver, "Silver Chestplate");
		LanguageRegistry.addName(legsSilver, "Silver Leggings");
		LanguageRegistry.addName(bootsSilver, "Silver Boots"); 
		LanguageRegistry.addName(vase, "Vase"); 
		LanguageRegistry.addName(horn, "Rhino Horn");
		LanguageRegistry.addName(hornGround, "Ground Rhino Horn"); 
		LanguageRegistry.addName(bed, "Bed");
		LanguageRegistry.addName(gemsbokHide, "Gemsbok Hide");
		LanguageRegistry.addName(gemsbokHorn, "Gemsbok Horn");
		LanguageRegistry.addName(gemsbokSpear, "Gemsbok Spear");
		LanguageRegistry.addName(juice, "Mango Juice");
		LanguageRegistry.addName(helmetGemsbok, "Gemsbok Helmet");
		LanguageRegistry.addName(bodyGemsbok, "Gemsbok Chestplate");
		LanguageRegistry.addName(legsGemsbok, "Gemsbok Leggings");
		LanguageRegistry.addName(bootsGemsbok, "Gemsbok Boots");
		LanguageRegistry.addName(jarLava, "Jar of Lava");
		LanguageRegistry.addName(peacockGem, "Peacock Gem");
		LanguageRegistry.addName(shovelPeacock, "Peacock Shovel");
		LanguageRegistry.addName(pickaxePeacock, "Peacock Pickaxe");
		LanguageRegistry.addName(axePeacock, "Peacock Axe");
		LanguageRegistry.addName(swordPeacock, "Peacock Sword");
		LanguageRegistry.addName(hoePeacock, "Peacock Hoe");
		LanguageRegistry.addName(helmetPeacock, "Peacock Helmet");
		LanguageRegistry.addName(bodyPeacock, "Peacock Chestplate");
		LanguageRegistry.addName(legsPeacock, "Peacock Leggings");
		LanguageRegistry.addName(bootsPeacock, "Peacock Boots");
		LanguageRegistry.addName(rugDye, "Rug Dye");
		LanguageRegistry.addName(new ItemStack(rugDye, 1, 0), "Rug Whitener");
		for (int i = 1; i <= 10; i++)
		{
			LanguageRegistry.addName(new ItemStack(rugDye, 1, i), "Rug Dye");
		}
		LanguageRegistry.addName(wings, "Peacock Wings");
		LanguageRegistry.addName(corn, "Corn");
		LanguageRegistry.addName(cornStalk, "Maize Stalks");
		LanguageRegistry.addName(popcorn, "Popcorn");
		LanguageRegistry.addName(nukaShard, "Nuka Shard");
		LanguageRegistry.addName(outlanderFur, "Outlander Fur");
		LanguageRegistry.addName(outlanderMeat, "Outlander Meat");
		LanguageRegistry.addName(passionFruit, "Passion Fruit");
		LanguageRegistry.addName(redFlower, "Flower");
		LanguageRegistry.addName(kivulite, "Kivulite Ingot");
		LanguageRegistry.addName(shovelKivulite, "Kivulite Shovel");
		LanguageRegistry.addName(pickaxeKivulite, "Kivulite Pickaxe");
		LanguageRegistry.addName(axeKivulite, "Kivulite Axe");
		LanguageRegistry.addName(swordKivulite, "Kivulite Sword");
		LanguageRegistry.addName(bugStew, "Bug Stew");
		LanguageRegistry.addName(crocodileMeat, "Crocodile Meat");
		LanguageRegistry.addName(poison, "Poison Powder");
		LanguageRegistry.addName(poisonedSpear, "Poisoned Spear");
		LanguageRegistry.addName(xpGrub, "Experience Grub");
		LanguageRegistry.addName(shovelCorrupt, "Corrupt Pridestone Shovel");
		LanguageRegistry.addName(pickaxeCorrupt, "Corrupt Pridestone Pickaxe");
		LanguageRegistry.addName(axeCorrupt, "Corrupt Pridestone Axe");
		LanguageRegistry.addName(swordCorrupt, "Corrupt Pridestone Sword");
		LanguageRegistry.addName(charm, "Astral Charm");
		LanguageRegistry.addName(new ItemStack(charm, 1, 0), "Astral Charm");
		LanguageRegistry.addName(new ItemStack(charm, 1, 1), "Voided Charm");
		LanguageRegistry.addName(zazuEgg, "Zazu Egg");
		LanguageRegistry.addName(kiwano, "Kiwano Slice");
		LanguageRegistry.addName(kiwanoSeeds, "Kiwano Seeds");
		LanguageRegistry.addName(ticketLionHead, "Ticket Lion Head");
		LanguageRegistry.addName(ticketLionSuit, "Ticket Lion Suit");
		LanguageRegistry.instance().addNameForObject(ticketLionLegs, "en_GB", "Ticket Lion Trousers");
		LanguageRegistry.instance().addNameForObject(ticketLionLegs, "en_CA", "Ticket Lion Trousers");
		LanguageRegistry.instance().addNameForObject(ticketLionLegs, "en_US", "Ticket Lion Pants");
		LanguageRegistry.addName(ticketLionFeet, "Ticket Lion Feet");
		LanguageRegistry.addName(questBook, "Book of Quests");
		LanguageRegistry.addName(outlandsHelm, "Outlandish Helm");
		LanguageRegistry.addName(dartQuiver, "Dart Quiver");
		LanguageRegistry.addName(outlandsFeather, "Wayward Feather");
		LanguageRegistry.addName(ziraRug, "Zira Rug");
		LanguageRegistry.addName(ziraCoin, "Zira Coin");
		LanguageRegistry.addName(hyenaHeadItem, "Hyena Head");
		LanguageRegistry.addName(new ItemStack(hyenaHeadItem, 1, 0), "Hyena Head");
		LanguageRegistry.addName(new ItemStack(hyenaHeadItem, 1, 1), "Hyena Head");
		LanguageRegistry.addName(new ItemStack(hyenaHeadItem, 1, 2), "Hyena Head");
		LanguageRegistry.addName(new ItemStack(hyenaHeadItem, 1, 3), "Skeletal Hyena Skull");
		LanguageRegistry.addName(amulet, "Animalspeak Amulet");
		LanguageRegistry.addName(mountedShooterItem, "Mounted Dart Shooter");
		LanguageRegistry.addName(new ItemStack(mountedShooterItem, 1, 0), "Mounted Dart Shooter");
		LanguageRegistry.addName(new ItemStack(mountedShooterItem, 1, 1), "Mounted Silver Dart Shooter");
		LanguageRegistry.addName(staff, "Rhythm Staff");
		LanguageRegistry.addName(note, "Musical Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 0), "C Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 1), "D Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 2), "E Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 3), "F Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 4), "G Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 5), "A Note");
		LanguageRegistry.addName(new ItemStack(note, 1, 6), "B Note");
		LanguageRegistry.addName(giraffeSaddle, "Giraffe Saddle");
		LanguageRegistry.addName(tie, "Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 0), "Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 1), "White Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 2), "Blue Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 3), "Yellow Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 4), "Red Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 5), "Purple Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 6), "Green Giraffe Tie");
		LanguageRegistry.addName(new ItemStack(tie, 1, 7), "Black Giraffe Tie");
		LanguageRegistry.addName(yam, "Yam");
		LanguageRegistry.addName(roastYam, "Roast Yam");
		LanguageRegistry.addName(banana, "Banana");
		LanguageRegistry.addName(bananaCake, "Banana Cake");
		LanguageRegistry.addName(featherPink, "Flamingo Feather");
		LanguageRegistry.addName(dartPink, "Flamingo Dart");
		LanguageRegistry.addName(bananaBread, "Banana Bread");
		LanguageRegistry.addName(hyenaMeal, "Hyena Meal");
		LanguageRegistry.addName(cornKernels, "Corn Kernels");
		LanguageRegistry.addName(driedMaize, "Dried Maize");

		GameRegistry.addRecipe(new ItemStack(dartBlue, 3), new Object[]{
			"X", "Y", "Z", 'X', hyenaBoneShard, 'Y', Item.stick, 'Z', featherBlue
		});
		GameRegistry.addRecipe(new ItemStack(dartYellow, 3), new Object[]{
			"X", "Y", "Z", 'X', hyenaBoneShard, 'Y', Item.stick, 'Z', featherYellow
		});
		GameRegistry.addRecipe(new ItemStack(dartRed, 3), new Object[]{
			"X", "Y", "Z", 'X', hyenaBoneShard, 'Y', Item.stick, 'Z', featherRed
		});
		GameRegistry.addRecipe(new ItemStack(dartBlack, 3), new Object[]{
			"X", "Y", "Z", 'X', itemTermite, 'Y', Item.stick, 'Z', featherBlack
		});
		GameRegistry.addRecipe(new ItemStack(itemGrindingBowl), new Object[]{
			" A ", "B B", "BBB", 'A', Item.stick, 'B', planks
		});
		GameRegistry.addRecipe(new ItemStack(zebraBoots), new Object[]{
			"  X", "XXX", "YYY", 'X', zebraHide, 'Y', hyenaBone
		});
		GameRegistry.addRecipe(new ItemStack(dartShooter), new Object[]{
			"XYY", 'X', mangoDust, 'Y', planks
		});
		GameRegistry.addRecipe(new ItemStack(prideBrick, 4, 0), new Object[]{
			"XX", "XX", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(pridePillar, 3, 0), new Object[]{
			"X", "X", "X", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(Block.furnaceIdle), new Object[]{
			"XXX", "X X", "XXX", 'X', pridestone
		});
		GameRegistry.addShapelessRecipe(new ItemStack(button), new Object[]{
			new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(lever), new Object[]{
			"X", "Y", 'X', Item.stick, 'Y', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(pressurePlate), new Object[]{
			"XX", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(shovel), new Object[]{
			"X", "Y", "Y", 'X', new ItemStack(pridestone, 1, 0), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(pickaxe), new Object[]{
			"XXX", " Y ", " Y ", 'X', new ItemStack(pridestone, 1, 0), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(axe), new Object[]{
			"XX", "XY", " Y", 'X', new ItemStack(pridestone, 1, 0), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(sword), new Object[]{
			"X", "X", "Y", 'X', new ItemStack(pridestone, 1, 0), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(hoe), new Object[]{
			"XX", " Y", " Y", 'X', new ItemStack(pridestone, 1, 0), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(stoneStairs, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(brickStairs, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(prideBrick, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(jar), new Object[]{
			"X X", "X X", " X ", 'X', pridestone
		});
		GameRegistry.addRecipe(new ItemStack(silverDartShooter), new Object[]{
			"XYY", 'X', mangoDust, 'Y', silver
		});
		GameRegistry.addRecipe(new ItemStack(shovelSilver), new Object[]{
			"X", "Y", "Y", 'X', silver, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(pickaxeSilver), new Object[]{
			"XXX", " Y ", " Y ", 'X', silver, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(axeSilver), new Object[]{
			"XX", "XY", " Y", 'X', silver, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(swordSilver), new Object[]{
			"X", "X", "Y", 'X', silver, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(hoeSilver), new Object[]{
			"XX", " Y", " Y", 'X', silver, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(outglassPane, 16), new Object[]{
			"XXX", "XXX", 'X', outglass
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 0), new Object[]{
			"XXX", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 1), new Object[]{
			"XXX", 'X', new ItemStack(prideBrick, 1, 0)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 0), new Object[]{
			new ItemStack(prideWood, 1, 0)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 1), new Object[]{
			new ItemStack(prideWood, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 2), new Object[]{
			new ItemStack(prideWood, 1, 2)
		});
		for (int i = 0; i < 4; i++)
		{
			GameRegistry.addRecipe(new ItemStack(drum, 1, i), new Object[]{
				"XXX", "Y Y", "YYY", 'X', zebraHide, 'Y', new ItemStack(prideWood, 1, i)
			});
		}
		GameRegistry.addRecipe(new ItemStack(Block.dispenser), new Object[]{
			"XXX", "XYX", "XXX", 'X', pridestone, 'Y', dartShooter
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 2), new Object[]{
			"XXX", 'X', new ItemStack(pridePillar, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(helmetSilver), new Object[]{
			"XXX", "X X", 'X', silver
		});
		GameRegistry.addRecipe(new ItemStack(bodySilver), new Object[]{
			"X X", "XXX", "XXX", 'X', silver
		});
		GameRegistry.addRecipe(new ItemStack(legsSilver), new Object[]{
			"XXX", "X X", "X X", 'X', silver
		});
		GameRegistry.addRecipe(new ItemStack(bootsSilver), new Object[]{
			"X X", "X X", 'X', silver
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 0), new Object[]{
			"X", "Y", 'X', whiteFlower, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 1), new Object[]{
			"X", "Y", 'X', blueFlower, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 2), new Object[]{
			"X", "Y", 'X', purpleFlower, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(gemsbokSpear), new Object[]{
			"  Y", " Y ", "X  ", 'X', Item.stick, 'Y', gemsbokHorn
		});
		GameRegistry.addShapelessRecipe(new ItemStack(juice), new Object[] {
            new ItemStack(mango), new ItemStack(mango), new ItemStack(jarWater)
        });
		GameRegistry.addRecipe(new ItemStack(helmetGemsbok), new Object[]{
			"XXX", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(bodyGemsbok), new Object[]{
			"X X", "XXX", "XXX", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(legsGemsbok), new Object[]{
			"XXX", "X X", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(bootsGemsbok), new Object[]{
			"X X", "X X", 'X', gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(blockSilver, 1, 0), new Object[]{
			"XXX", "XXX", "XXX", 'X', silver
		});
		GameRegistry.addRecipe(new ItemStack(silver, 9), new Object[]{
			"X", 'X', new ItemStack(blockSilver, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(blockPeacock), new Object[]{
			"XXX", "XXX", "XXX", 'X', peacockGem
		});
		GameRegistry.addRecipe(new ItemStack(peacockGem, 9), new Object[]{
			"X", 'X', blockPeacock
		});
		GameRegistry.addRecipe(new ItemStack(shovelPeacock), new Object[]{
			"X", "Y", "Y", 'X', peacockGem, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(pickaxePeacock), new Object[]{
			"XXX", " Y ", " Y ", 'X', peacockGem, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(axePeacock), new Object[]{
			"XX", "XY", " Y", 'X', peacockGem, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(swordPeacock), new Object[]{
			"X", "X", "Y", 'X', peacockGem, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(hoePeacock), new Object[]{
			"XX", " Y", " Y", 'X', peacockGem, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(helmetPeacock), new Object[]{
			"XXX", "X X", 'X', peacockGem
		});
		GameRegistry.addRecipe(new ItemStack(bodyPeacock), new Object[]{
			"X X", "XXX", "XXX", 'X', peacockGem
		});
		GameRegistry.addRecipe(new ItemStack(legsPeacock), new Object[]{
			"XXX", "X X", "X X", 'X', peacockGem
		});
		GameRegistry.addRecipe(new ItemStack(bootsPeacock), new Object[]{
			"X X", "X X", 'X', peacockGem
		});
		GameRegistry.addRecipe(new ItemStack(rug, 4, 0), new Object[]{
			"XX", "XX", 'X', fur
		});
		for (int i = 0; i <= 15; i++)
		{
			if (i != 1)
			{
				GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 1), new Object[]{
					new ItemStack(rug, 1, i), new ItemStack(rugDye, 1, 0)
				});
			}
		}
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 2), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 3), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 4), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 5), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 4)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 6), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 5)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 7), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 6)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 8), new Object[]{
			new ItemStack(rug, 1, 1), mangoDust
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 9), new Object[]{
			new ItemStack(rug, 1, 1), hornGround
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 10), new Object[]{
			new ItemStack(rug, 1, 1), termiteDust
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 11), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 7)
		});
		GameRegistry.addRecipe(new ItemStack(rug, 4, 12), new Object[]{
			"XX", "XX", 'X', outlanderFur
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 13), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 8)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 14), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 9)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(rug, 1, 15), new Object[]{
			new ItemStack(rug, 1, 1), new ItemStack(rugDye, 1, 10)
		});
		GameRegistry.addRecipe(new ItemStack(wings), new Object[]{
			"PRP", "YPY", "BPB", 'P', peacockGem, 'R', featherRed, 'Y', featherYellow, 'B', featherBlue
		});
		GameRegistry.addRecipe(new ItemStack(bed), new Object[]{
			"XXX", "YYY", 'X', fur, 'Y', planks
		});
		GameRegistry.addRecipe(new ItemStack(Item.paper, 3), new Object[]{
			"XXX", 'X', cornStalk
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 3), new Object[]{
			"X", "Y", 'X', redFlower, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(blockSilver, 1, 1), new Object[]{
			"XXX", "XXX", "XXX", 'X', kivulite
		});
		GameRegistry.addRecipe(new ItemStack(kivulite, 9), new Object[]{
			"X", 'X', new ItemStack(blockSilver, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(shovelKivulite), new Object[]{
			"X", "Y", "Y", 'X', kivulite, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(pickaxeKivulite), new Object[]{
			"XXX", " Y ", " Y ", 'X', kivulite, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(axeKivulite), new Object[]{
			"XX", "XY", " Y", 'X', kivulite, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(swordKivulite), new Object[]{
			"X", "X", "Y", 'X', kivulite, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(bugTrap), new Object[]{
			"XXX", "YZY", "XBX", 'X', new ItemStack(prideWood, 1, 0), 'Y', silver, 'Z', Item.stick, 'B', bug
		});
		GameRegistry.addRecipe(new ItemStack(starAltar), new Object[]{
			"XXX", "YYY", 'X', lionDust, 'Y', silver
		});
		GameRegistry.addShapelessRecipe(new ItemStack(poisonedSpear), new Object[]{
			gemsbokSpear, poison, poison
		});
		GameRegistry.addRecipe(new ItemStack(prideBrick, 4, 1), new Object[]{
			"XX", "XX", 'X', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(pridePillar, 3, 4), new Object[]{
			"X", "X", "X", 'X', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 3), new Object[]{
			"XXX", 'X', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 4), new Object[]{
			"XXX", 'X', new ItemStack(prideBrick, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(slabSingle, 6, 5), new Object[]{
			"XXX", 'X', new ItemStack(pridePillar, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(shovelCorrupt), new Object[]{
			"X", "Y", "Y", 'X', new ItemStack(pridestone, 1, 1), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(pickaxeCorrupt), new Object[]{
			"XXX", " Y ", " Y ", 'X', new ItemStack(pridestone, 1, 1), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(axeCorrupt), new Object[]{
			"XX", "XY", " Y", 'X', new ItemStack(pridestone, 1, 1), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(swordCorrupt), new Object[]{
			"X", "X", "Y", 'X', new ItemStack(pridestone, 1, 1), 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(stoneStairsCorrupt, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(brickStairsCorrupt, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(prideBrick, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(charm), new Object[]{
			" X ", "XYX", " X ", 'X', silver, 'Y', lionDust
		});
		GameRegistry.addShapelessRecipe(new ItemStack(kiwanoSeeds), new Object[]{
			kiwano
		});
		GameRegistry.addRecipe(new ItemStack(kiwanoBlock), new Object[]{
			"XXX", "XXX", "XXX", 'X', kiwano
		});
		GameRegistry.addShapelessRecipe(new ItemStack(bugStew), new Object[]{
			Item.bowlEmpty, bug, mango, jarMilk
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.book), new Object[]{
			Item.paper, Item.paper, Item.paper, gemsbokHide
		});
		GameRegistry.addRecipe(new ItemStack(termite, 1, 1), new Object[]{
			" X ", "XYX", " X ", 'X', itemTermite, 'Y', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.bowlSoup), new Object[]{
			Item.bowlEmpty, outshroom, outshroom
		});
		GameRegistry.addRecipe(new ItemStack(outshroomGlowing), new Object[]{
			"X", "Y", 'X', nukaShard, 'Y', outshroom
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 4), new Object[]{
			"X", "Y", 'X', sapling, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 5), new Object[]{
			"X", "Y", 'X', forestSapling, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 6), new Object[]{
			"X", "Y", 'X', mangoSapling, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 7), new Object[]{
			"X", "Y", 'X', outshroom, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 8), new Object[]{
			"X", "Y", 'X', outshroomGlowing, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(stairsAcacia, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(stairsRainforest, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(stairsMango, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 0), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 1), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 2), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 2)
		});
		GameRegistry.addRecipe(new ItemStack(Block.tnt), new Object[]{
			"XYX", "YXY", "XYX", 'X', termiteDust, 'Y', Block.sand
		});
		GameRegistry.addRecipe(new ItemStack(Item.stick, 4), new Object[]{
			"X", "X", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Block.pressurePlatePlanks), new Object[]{
			"XX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{
			"X X", " X ", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Block.workbench), new Object[]{
			"XX", "XX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Item.boat), new Object[]{
			"X X", "XXX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Item.doorWood), new Object[]{
			"XX", "XX", "XX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Block.trapdoor, 2), new Object[]{
			"XXX", "XXX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Block.chest), new Object[]{
			"XXX", "X X", "XXX", 'X', planks
		});
		GameRegistry.addRecipe(new ItemStack(Item.shovelWood), new Object[]{
			"X", "Y", "Y", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Item.pickaxeWood), new Object[]{
			"XXX", " Y ", " Y ", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Item.axeWood), new Object[]{
			"XX", "XY", " Y", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Item.swordWood), new Object[]{
			"X", "X", "Y", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Item.hoeWood), new Object[]{
			"XX", " Y", " Y", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Block.fenceGate), new Object[]{
			"YXY", "YXY", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(Block.bookShelf), new Object[]{
			"XXX", "YYY", "XXX", 'X', planks, 'Y', Item.book
		});
		GameRegistry.addRecipe(new ItemStack(Item.sign, 3), new Object[]{
			"XXX", "XXX", " Y ", 'X', planks, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(mountedShooterItem, 1, 0), new Object[]{
			" X ", "Y Y", 'X', dartShooter, 'Y', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(mountedShooterItem, 1, 1), new Object[]{
			" X ", "Y Y", 'X', silverDartShooter, 'Y', Item.stick
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 3), new Object[]{
			new ItemStack(prideWood, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 3), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(stairsPassion, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 3)
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 9), new Object[]{
			"X", "Y", 'X', passionSapling, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(hyenaTorch, 4), new Object[]{
			"X", "Y", 'X', Item.coal, 'Y', hyenaBone
		});
		GameRegistry.addRecipe(new ItemStack(hyenaTorch, 4), new Object[]{
			"X", "Y", 'X', new ItemStack(Item.coal, 1, 1), 'Y', hyenaBone
		});
		GameRegistry.addRecipe(new ItemStack(staff), new Object[]{
			"X", "Y", "Y", 'X', peacockGem, 'Y', gemsbokHorn
		});
		GameRegistry.addRecipe(new ItemStack(giraffeSaddle), new Object[]{
			"XXX", "Y Y", 'X', gemsbokHide, 'Y', silver
		});
		GameRegistry.addRecipe(new ItemStack(Item.itemFrame), new Object[]{
			"XXX", "XYX", "XXX", 'X', Item.stick, 'Y', zebraHide
		});
		GameRegistry.addRecipe(new ItemStack(wall, 1, 0), new Object[]{
			"XXX", "XXX", 'X', new ItemStack(pridestone, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 1, 1), new Object[]{
			"XXX", "XXX", 'X', new ItemStack(prideBrick, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 1, 2), new Object[]{
			"XXX", "XXX", 'X', new ItemStack(prideBrickMossy, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 1, 3), new Object[]{
			"XXX", "XXX", 'X', new ItemStack(pridestone, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(wall, 1, 4), new Object[]{
			"XXX", "XXX", 'X', new ItemStack(prideBrick, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(tie, 1, 0), new Object[]{
			"X", "X", "X", 'X', fur
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 2), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 1)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 3), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 2)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 4), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 3)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 5), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 4)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 6), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 6)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 7), new Object[]{
			new ItemStack(tie, 1, 1), new ItemStack(rugDye, 1, 7)
		});
		for (int i = 0; i <= 7; i++)
		{
			if (i != 1)
			{
				GameRegistry.addShapelessRecipe(new ItemStack(tie, 1, 1), new Object[]{
					new ItemStack(tie, 1, i), new ItemStack(rugDye, 1, 0)
				});
			}
		}
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 4), new Object[]{
			new ItemStack(prideWood2, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(drum, 1, 4), new Object[]{
			"XXX", "Y Y", "YYY", 'X', zebraHide, 'Y', new ItemStack(prideWood2, 1, 0)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 4), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(stairsBanana, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 4)
		});
		GameRegistry.addRecipe(new ItemStack(vase, 1, 10), new Object[]{
			"X", "Y", 'X', bananaSapling, 'Y', jarWater
		});
		GameRegistry.addRecipe(new ItemStack(bananaCake), new Object[]{
			"AAA", "BCB", "DDD", 'A', jarMilk, 'B', banana, 'C', zazuEgg, 'D', Item.wheat
		});
		GameRegistry.addRecipe(new ItemStack(dartPink, 3), new Object[]{
			"X", "Y", "Z", 'X', hyenaBoneShard, 'Y', Item.stick, 'Z', featherPink
		});
		GameRegistry.addRecipe(new ItemStack(bananaBread), new Object[]{
			"XYX", 'X', Item.wheat, 'Y', banana
		});
		GameRegistry.addShapelessRecipe(new ItemStack(planks, 4, 5), new Object[]{
			new ItemStack(prideWood2, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(drum, 1, 5), new Object[]{
			"XXX", "Y Y", "YYY", 'X', zebraHide, 'Y', new ItemStack(prideWood2, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(woodSlabSingle, 6, 5), new Object[]{
			"XXX", 'X', new ItemStack(planks, 1, 5)
		});
		GameRegistry.addRecipe(new ItemStack(stairsDeadwood, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', new ItemStack(planks, 1, 5)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(Block.woodenButton), new Object[]{
			planks
		});
		GameRegistry.addShapelessRecipe(new ItemStack(hyenaMeal, 3), new Object[]{
			hyenaBone
		});
		GameRegistry.addShapelessRecipe(new ItemStack(cornKernels, 2), new Object[]{
			corn
		});
		GameRegistry.addRecipe(new ItemStack(driedMaizeBlock), new Object[]{
			"XX", "XX", 'X', driedMaize
		});
		GameRegistry.addRecipe(new ItemStack(driedMaizeSlabSingle, 6, 0), new Object[]{
			"XXX", 'X', driedMaizeBlock
		});
		GameRegistry.addRecipe(new ItemStack(stairsDriedMaize, 4), new Object[]{
			"X  ", "XX ", "XXX", 'X', driedMaizeBlock
		});

		FurnaceRecipes.smelting().addSmelting(lionRaw.itemID, new ItemStack(lionCooked), 0.35F);
		FurnaceRecipes.smelting().addSmelting(zebraRaw.itemID, new ItemStack(zebraCooked), 0.35F);
		FurnaceRecipes.smelting().addSmelting(rhinoRaw.itemID, new ItemStack(rhinoCooked), 0.35F);
		FurnaceRecipes.smelting().addSmelting(corn.itemID, new ItemStack(popcorn), 0.35F);
		FurnaceRecipes.smelting().addSmelting(yam.itemID, new ItemStack(roastYam), 0.35F);
		FurnaceRecipes.smelting().addSmelting(outlanderMeat.itemID, new ItemStack(lionCooked), 0.2F);
		
		FurnaceRecipes.smelting().addSmelting(prideWood.blockID, new ItemStack(Item.coal, 1, 1), 0.15F);
		FurnaceRecipes.smelting().addSmelting(prideWood2.blockID, new ItemStack(Item.coal, 1, 1), 0.15F);
		FurnaceRecipes.smelting().addSmelting(log.blockID, new ItemStack(Item.coal, 1, 1), 0.15F);
		FurnaceRecipes.smelting().addSmelting(outsand.blockID, new ItemStack(outglass), 0.1F);
		FurnaceRecipes.smelting().addSmelting(cornStalk.itemID, new ItemStack(driedMaize), 0.15F);

		FurnaceRecipes.smelting().addSmelting(prideCoal.blockID, 0, new ItemStack(Item.coal), 0.1F);
		FurnaceRecipes.smelting().addSmelting(prideCoal.blockID, 1, new ItemStack(nukaShard), 0.5F);
		
		FurnaceRecipes.smelting().addSmelting(oreSilver.blockID, 0, new ItemStack(silver), 0.8F);
		FurnaceRecipes.smelting().addSmelting(oreSilver.blockID, 1, new ItemStack(kivulite), 0.8F);
		
		FurnaceRecipes.smelting().addSmelting(orePeacock.blockID, new ItemStack(peacockGem), 1.0F);
		
		LKEntities.registerCreature(LKEntityLion.class, "Lion", 1, 0xE8AB28, 0xA94400);
		LKEntities.registerCreature(LKEntityLioness.class, "Lioness", 2, 0xDDAA39, 0xD35D1D);
		LKEntities.registerCreature(LKEntityZebra.class, "Zebra", 3, 0xE2E2E2, 0x161616);
		LKEntities.registerCreature(LKEntityZazu.class, "Zazu", 4, 0x0E64E2, 0xB9D5FB);
		LKEntities.registerCreature(LKEntityRafiki.class, "Rafiki", 5, 0x191919, 0xF12D2A);
		LKEntities.registerCreature(LKEntityTimon.class, "Timon", 6, 0xE0923B, 0x4B2C17);
		LKEntities.registerCreature(LKEntityPumbaa.class, "Pumbaa", 7, 0x9C210F, 0x2D0C0C);
		LKEntities.registerCreature(LKEntityBug.class, "Bug", 8, 0x011164, 0xC1BB18);
		LKEntities.registerCreature(LKEntitySimba.class, "Simba", 9, 0xFEA517, 0xBD3200);
		LKEntities.registerCreature(LKEntityHyena.class, "Hyena", 10, 0x444243, 0x140C17);
		LKEntities.registerCreature(LKEntityScar.class, "Scar", 11, 0x6C270B, 0x0A0A0A);
		LKEntities.registerCreature(LKEntityOutlander.class, "Outlander", 12, 0xCAB061, 0x291C13);
		LKEntities.registerCreature(LKEntityVulture.class, "Vulture", 13, 0x131313, 0xC79210);
		LKEntities.registerCreature(LKEntityTermite.class, "Exploding Termite", 14, 0x17140D, 0x2F2B24);
		LKEntities.registerCreature(LKEntityTicketLion.class, "Ticket Lion", 15, 0xBA630E, 0x111111);
		LKEntities.registerCreature(LKEntityRhino.class, "Rhino", 16, 0x5D5C51, 0xB9B79D);
		LKEntities.registerCreature(LKEntityOutlandess.class, "Outlandess", 17, 0xAF904A, 0x20130D);
		LKEntities.registerCreature(LKEntityGemsbok.class, "Gemsbok", 18, 0xB36F3F, 0xF2ECD7);
		LKEntities.registerCreature(LKEntityCrocodile.class, "Crocodile", 19, 0x2C3313, 0x0F0F06);
		LKEntities.registerCreature(LKEntityZira.class, "Zira", 20, 0xAF9C4F, 0x7A0000);
		LKEntities.registerCreature(LKEntityTermiteQueen.class, "Termite Queen", 21, 0x17140D, 0x2F2B24);
		LKEntities.registerCreature(LKEntityGiraffe.class, "Giraffe", 22, 0xF2B438, 0x604006);
		LKEntities.registerCreature(LKEntitySkeletalHyena.class, "Skeletal Hyena", 23, 0xD0CAAA, 0x767360);
		LKEntities.registerCreature(LKEntitySkeletalHyenaHead.class, "Skeletal Hyena Head", 24, 0xD0CAAA, 0x767360);
		LKEntities.registerCreature(LKEntityDikdik.class, "Dik-dik", 25, 0xB7783B, 0x684729);
		LKEntities.registerCreature(LKEntityFlamingo.class, "Flamingo", 26, 0xF57B9E, 0xF9D9E3);
		
		LKEntities.registerEntity(LKEntityBlueDart.class, "Blue Dart", 100);
		LKEntities.registerEntity(LKEntityYellowDart.class, "Yellow Dart", 101);
		LKEntities.registerEntity(LKEntityRedDart.class, "Red Dart", 102);
		LKEntities.registerEntity(LKEntityBlackDart.class, "Outlandish Dart", 103);
		LKEntities.registerEntity(LKEntityCoin.class, "Thrown Rafiki Coin", 104);
		LKEntities.registerEntity(LKEntityThrownTermite.class, "Thrown Termite", 105);
		LKEntities.registerEntity(LKEntityOutsand.class, "Falling Outsand", 106);
		LKEntities.registerEntity(LKEntityPumbaaBomb.class, "Thrown Pumbaa Bomb", 107);
		LKEntities.registerEntity(LKEntityGemsbokSpear.class, "Gemsbok Spear", 108);
		LKEntities.registerEntity(LKEntityPoisonedSpear.class, "Poisoned Gemsbok Spear", 109);
		LKEntities.registerEntity(LKEntityZazuEgg.class, "Zazu Egg", 110);
		LKEntities.registerEntity(LKEntityScarRug.class, "Scar Rug", 111);
		LKEntities.registerEntity(LKEntityPinkDart.class, "Flamingo Dart", 112);
		
 		GameRegistry.registerTileEntity(LKTileEntityGrindingBowl.class, "Grinding Bowl");
		GameRegistry.registerTileEntity(LKTileEntityBugTrap.class, "LKBugTrap");
		GameRegistry.registerTileEntity(LKTileEntityDrum.class, "LKBongoDrum");
		GameRegistry.registerTileEntity(LKTileEntityOutlandsPool.class, "LKOutlandsPool");
		GameRegistry.registerTileEntity(LKTileEntityHyenaHead.class, "LKHyenaHead");
		GameRegistry.registerTileEntity(LKTileEntityMountedShooter.class, "LKMountedShooter");
		GameRegistry.registerTileEntity(LKTileEntityFurRug.class, "LKRug");
		GameRegistry.registerTileEntity(LKTileEntityMobSpawner.class, "LKMobSpawner");
		
		LanguageRegistry.instance().addStringLocalization("enchantment.damage.hyena", "Scourge of Hyenas");
		LanguageRegistry.instance().addStringLocalization("enchantment.td.big", "Biggah Diggah");
		LanguageRegistry.instance().addStringLocalization("enchantment.rafiki.durability", "Sturdy");
		LanguageRegistry.instance().addStringLocalization("enchantment.rafiki.thunder", "Thunder");
		LanguageRegistry.instance().addStringLocalization("enchantment.td.precision", "Precision");
		
		Block.setBurnProperties(woodSlabDouble.blockID, 5, 20);
		Block.setBurnProperties(forestLeaves.blockID, 30, 60);
		Block.setBurnProperties(mangoLeaves.blockID, 30, 60);
		Block.setBurnProperties(woodSlabSingle.blockID, 5, 20);
		Block.setBurnProperties(leaves.blockID, 30, 60);
		Block.setBurnProperties(log.blockID, 5, 5);
		Block.setBurnProperties(prideWood.blockID, 5, 5);
		Block.setBurnProperties(aridGrass.blockID, 100, 150);
		Block.setBurnProperties(stairsAcacia.blockID, 5, 20);
		Block.setBurnProperties(stairsRainforest.blockID, 5, 20);
		Block.setBurnProperties(stairsMango.blockID, 5, 20);
		Block.setBurnProperties(planks.blockID, 5, 20);
		Block.setBurnProperties(passionLeaves.blockID, 30, 60);
		Block.setBurnProperties(stairsPassion.blockID, 5, 20);
		Block.setBurnProperties(stairsBanana.blockID, 5, 20);
		Block.setBurnProperties(prideWood2.blockID, 5, 5);
		Block.setBurnProperties(bananaLeaves.blockID, 30, 60);
		Block.setBurnProperties(stairsDeadwood.blockID, 30, 60);
		Block.setBurnProperties(driedMaizeBlock.blockID, 50, 80);
		Block.setBurnProperties(driedMaizeSlabSingle.blockID, 50, 80);
		Block.setBurnProperties(driedMaizeSlabDouble.blockID, 50, 80);
		Block.setBurnProperties(stairsDriedMaize.blockID, 50, 80);

		Block.useNeighborBrightness[woodSlabSingle.blockID] = true;
		Block.useNeighborBrightness[pridePillar.blockID] = true;
        Block.useNeighborBrightness[stoneStairs.blockID] = true;
        Block.useNeighborBrightness[brickStairs.blockID] = true;
		Block.useNeighborBrightness[slabSingle.blockID] = true;
        Block.useNeighborBrightness[stoneStairsCorrupt.blockID] = true;
        Block.useNeighborBrightness[brickStairsCorrupt.blockID] = true;
		Block.useNeighborBrightness[tilledSand.blockID] = true;
		Block.useNeighborBrightness[stairsAcacia.blockID] = true;
		Block.useNeighborBrightness[stairsRainforest.blockID] = true;
		Block.useNeighborBrightness[stairsMango.blockID] = true;
		Block.useNeighborBrightness[stairsPassion.blockID] = true;
		Block.useNeighborBrightness[stairsBanana.blockID] = true;
		Block.useNeighborBrightness[stairsDeadwood.blockID] = true;
		Block.useNeighborBrightness[driedMaizeSlabSingle.blockID] = true;
		Block.useNeighborBrightness[stairsDriedMaize.blockID] = true;

		MinecraftForge.setBlockHarvestLevel(oreSilver, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(orePeacock, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockSilver, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockPeacock, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(outsand, "shovel", 0);
		MinecraftForge.setBlockHarvestLevel(log, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(prideWood, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(tilledSand, "shovel", 0);
		MinecraftForge.setBlockHarvestLevel(kiwanoBlock, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(prideWood2, "axe", 0);

		MinecraftForge.setToolClass(shovel, "shovel", 1);
		MinecraftForge.setToolClass(pickaxe, "pickaxe", 1);
		MinecraftForge.setToolClass(axe, "axe", 1);
		MinecraftForge.setToolClass(shovelSilver, "shovel", 2);
		MinecraftForge.setToolClass(pickaxeSilver, "pickaxe", 2);
		MinecraftForge.setToolClass(axeSilver, "axe", 2);
		MinecraftForge.setToolClass(tunnahDiggah, "pickaxe", 1);
		MinecraftForge.setToolClass(shovelPeacock, "shovel", 3);
		MinecraftForge.setToolClass(pickaxePeacock, "pickaxe", 3);
		MinecraftForge.setToolClass(axePeacock, "axe", 3);
		MinecraftForge.setToolClass(shovelKivulite, "shovel", 2);
		MinecraftForge.setToolClass(pickaxeKivulite, "pickaxe", 2);
		MinecraftForge.setToolClass(axeKivulite, "axe", 2);
		MinecraftForge.setToolClass(shovelCorrupt, "shovel", 1);
		MinecraftForge.setToolClass(pickaxeCorrupt, "pickaxe", 1);
		MinecraftForge.setToolClass(axeCorrupt, "axe", 1);
		
		GameRegistry.registerCraftingHandler(this);
		GameRegistry.registerFuelHandler(this);
		GameRegistry.registerPickupHandler(this);
		GameRegistry.registerWorldGenerator(this);
		TickRegistry.registerTickHandler(new LKTickHandlerServer(), Side.SERVER);
		NetworkRegistry.instance().registerConnectionHandler(new LKConnectionHandler());
		NetworkRegistry.instance().registerChannel(serverHandler, "lk.simbaSit", Side.SERVER);
		NetworkRegistry.instance().registerChannel(serverHandler, "lk.damageItem", Side.SERVER);
		NetworkRegistry.instance().registerChannel(serverHandler, "lk.sendQCheck", Side.SERVER);
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		DimensionManager.registerProviderType(idPrideLands, LKWorldProviderPrideLands.class, true);
		DimensionManager.registerProviderType(idOutlands, LKWorldProviderOutlands.class, true);
		DimensionManager.registerProviderType(idUpendi, LKWorldProviderUpendi.class, true);
		DimensionManager.registerDimension(idPrideLands, idPrideLands);
		DimensionManager.registerDimension(idOutlands, idOutlands);
		DimensionManager.registerDimension(idUpendi, idUpendi);

		LKCompatibility.init();
		
		proxy.onLoad();
	}
	
	public void onCrafting(EntityPlayer entityplayer, ItemStack itemstack, IInventory iinventory)
    {
		if (itemstack.itemID == itemGrindingBowl.itemID)
		{
			entityplayer.triggerAchievement(LKAchievementList.craftGrindBowl);
		}
    }
	
    public void onSmelting(EntityPlayer entityplayer, ItemStack itemstack)
    {
		if (itemstack.itemID == peacockGem.itemID)
		{
			entityplayer.triggerAchievement(LKAchievementList.peacock);
		}
    }

	public int getBurnTime(ItemStack itemstack)
    {
		int i = itemstack.itemID;
        if (i < Block.blocksList.length && Block.blocksList[i] != null && Block.blocksList[i] instanceof LKBlockSapling)
        {                            
        	return 100;
        }
        if (i == jarLava.itemID)
        {
            return 20000;
        }
        if (i == cornStalk.itemID)
        {
            return 100;
        }
        if (i == nukaShard.itemID)
        {
            return 400;
        }
		return 0;
   	}
	
	public void notifyPickup(EntityItem item, EntityPlayer entityplayer)
	{
		ItemStack itemstack = item.getEntityItem();
		if (itemstack.itemID == mango.itemID)
		{
			entityplayer.triggerAchievement(LKAchievementList.getMango);
		}
		if (itemstack.itemID == banana.itemID)
		{
			entityplayer.triggerAchievement(LKAchievementList.getBanana);
		}
	}

   	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
   	{
		if (world.provider.dimensionId != 0)
		{
			return;
		}
		for (int i = 0; i < 3; i++)
		{
			int posX = chunkX * 16 + rand.nextInt(16);
			int posZ = chunkZ * 16 + rand.nextInt(16);
			int posY = world.getHeightValue(posX, posZ);
			if (LKWorldGenTicketBooth.canGenerateAtPosition(posX, posY, posZ))
			{
				if ((new LKWorldGenTicketBooth()).generate(world, rand, posX, posY, posZ))
				{
					LKLevelData.ticketBoothLocations.add(new ChunkCoordinates(posX, posY, posZ));
					LKLevelData.needsSave = true;
				}
			}
		}
   	}
	
	@ForgeSubscribe
    public void onEntityAttack(AttackEntityEvent event)
	{
		if (event.target instanceof LKEntityScarRug)
		{
			((LKEntityScarRug)event.target).dropAsItem();
		}
	}
	
	@ForgeSubscribe
    public void onEntityInteract(EntityInteractEvent event)
	{
		if (event.target instanceof LKEntityScarRug && ((LKEntityScarRug)event.target).interact(event.entityPlayer))
		{
			event.setCanceled(true);
		}
	}

	@ForgeSubscribe
    public void onEntityLivingHurt(LivingHurtEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer && event.source.isFireDamage())
		{
			EntityPlayer entityplayer = (EntityPlayer)event.entityLiving;
			ItemStack helmet = entityplayer.inventory.armorItemInSlot(3);
			if (helmet != null && helmet.itemID == outlandsHelm.itemID && entityplayer.dimension == idOutlands && !entityplayer.worldObj.isRemote)
			{
				entityplayer.extinguish();
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onEntityLivingDeath(LivingDeathEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		if (!entity.worldObj.isRemote && entity.getRNG().nextInt(3) == 0 && !(entity instanceof LKCharacter) && !(entity instanceof EntityPlayer))
		{
			DamageSource source = event.source;
			if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)source.getEntity();
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if (itemstack != null && itemstack.itemID == staff.itemID)
				{
					int notes = entity.getRNG().nextInt(3) == 0 ? 2 + entity.getRNG().nextInt(2) : 1;
					for (int i = 0; i < notes; i++)
					{
						int damage = 0;
						float f = entity.getRNG().nextFloat();
						if (f >= 0.25F && f < 0.5F)
						{
							damage = 1;
						}
						else if (f >= 0.5F && f < 0.75F)
						{
							damage = 2;
						}
						else if (f >= 0.75F && f < 0.85F)
						{
							damage = 3;
						}
						else if (f >= 0.85F && f < 0.95F)
						{
							damage = 4;
						}
						else if (f >= 0.95F && f < 1F)
						{
							damage = 5;
						}
						entity.entityDropItem(new ItemStack(note, 1, damage), 0.5F);
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
    public void onUseHoe(UseHoeEvent event)
	{
		World world = event.world;
		int i = event.x;
		int j = event.y;
		int k = event.z;
		if (!world.isRemote && world.getBlockId(i, j, k) == Block.sand.blockID && world.isAirBlock(i, j + 1, k) && world.getWorldChunkManager().getBiomeGenAt(i, k) instanceof LKBiomeGenAridSavannah)
		{
			world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), tilledSand.stepSound.getStepSound(), (tilledSand.stepSound.getVolume() + 1.0F) / 2.0F, tilledSand.stepSound.getPitch() * 0.8F);
			world.setBlock(i, j, k, tilledSand.blockID, 0, 3);
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@ForgeSubscribe
    public void onUseBonemeal(BonemealEvent event)
	{
		int dimension = event.world.provider.dimensionId;
		if (LKIngame.isLKWorld(dimension))
		{
			event.setCanceled(true);
		}
	}
	
	private void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
	}
	
	private void registerBlock(Block block, String name)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
		LanguageRegistry.addName(block, name);
	}
	
	private void registerBlock(Block block, Class itemClass, String name)
	{
		GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName());
		LanguageRegistry.addName(block, name);
	}
	
	private void registerItem(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName());
	}
	
	private void registerItem(Item item, String name)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName());
		LanguageRegistry.addName(item, name);
	}
	
    public static void dropItemsFromBlock(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
    {
        if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float var6 = 0.7F;
            double var7 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var9 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var11 = (double)(par1World.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(par1World, (double)par2 + var7, (double)par3 + var9, (double)par4 + var11, par5ItemStack);
            var13.delayBeforeCanPickup = 10;
            par1World.spawnEntityInWorld(var13);
        }
    }
}
