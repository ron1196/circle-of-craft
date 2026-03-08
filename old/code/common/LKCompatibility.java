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

import java.util.HashSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.lwjgl.input.Keyboard;
import static lionking.common.mod_LionKing.*;

public class LKCompatibility
{
	private static Item[] toolItems;
	public static boolean isTimberModInstalled;
	
	public static void timber(World world, int i, int j, int k, int blockID)
	{
		try
		{
			boolean isAxe = (Boolean)(Class.forName("BlockTimberTree").getDeclaredField("isAxe").get(null));
			int key = (Integer)(Class.forName("mod_Timber").getDeclaredField("key").get(null));
			
			if (isAxe && !Keyboard.isKeyDown(key))
			{
				int l = 1;
				for (int i1 = -l; i1 <= l; i1++)
				{
					for (int k1 = -l; k1 <= l; k1++)
					{
						for (int j1 = 0; j1 <= l; j1++)
						{
							int l1 = world.getBlockId(i + i1, j + j1, k + k1);

							if (l1 != blockID)
							{
								continue;
							}
							
							Block block = Block.blocksList[l1];
							int i2 = world.getBlockMetadata(i + i1, j + j1, k + k1) & 3;
							if (block != null)
							{
								if (!world.isRemote)
								{
									world.setBlockToAir(i + i1, j + j1, k + k1);
								}
							}
						}
					}
				}
			}
		}
		catch (Throwable throwable)
		{
		}
	}
	
	public static void init()
	{
		toolItems = new Item[]
		{
			rafikiStick, dartShooter, zebraBoots, shovel, pickaxe, axe, sword, hoe, silverDartShooter, shovelSilver,
			pickaxeSilver, axeSilver, swordSilver, hoeSilver, tunnahDiggah, helmetSilver, bodySilver, legsSilver,
			bootsSilver, gemsbokSpear, helmetGemsbok, bodyGemsbok, legsGemsbok, bootsGemsbok, shovelPeacock, 
			pickaxePeacock, axePeacock, swordPeacock, hoePeacock, helmetPeacock, bodyPeacock, legsPeacock, bootsPeacock, 
			wings, shovelKivulite, pickaxeKivulite, axeKivulite, swordKivulite, poisonedSpear, shovelCorrupt, pickaxeCorrupt, 
			axeCorrupt, swordCorrupt, staff
		};
		
		setupTMICompatibility();
		setupTimberCompatibility();
	}
	
	private static void setupTMICompatibility()
	{
		try
		{
			Class class_TMIConfig = Class.forName("TMIConfig");
			Field field_toolIds = class_TMIConfig.getDeclaredField("toolIds");
			field_toolIds.setAccessible(true);
			HashSet toolIds = (HashSet)field_toolIds.get(null);

			for (int i = 0; i < toolItems.length; i++)
			{
				toolIds.add(toolItems[i].itemID);
			}
			
			field_toolIds.set(null, toolIds);
		}
		catch (Throwable throwable) {}
    }
	
	private static void setupTimberCompatibility()
	{
		boolean flag = true;
		
		try
		{
			Class class_mod_Timber = Class.forName("mod_Timber");
			Field field_axes = class_mod_Timber.getDeclaredField("axes");
			String axes = (String)field_axes.get(null);

			axes = axes + ", " + axe.itemID;
			axes = axes + ", " + axeSilver.itemID;
			axes = axes + ", " + axePeacock.itemID;
			axes = axes + ", " + axeKivulite.itemID;
			axes = axes + ", " + axeCorrupt.itemID;
			
			field_axes.set(null, axes);
		}
		catch (Throwable throwable)
		{
			flag = false;
		}
		
		isTimberModInstalled = flag;
	}
}