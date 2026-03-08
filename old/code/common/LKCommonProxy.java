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

import java.util.List;
import cpw.mods.fml.common.network.*;
import net.minecraft.src.*;
import lionking.client.*;

public class LKCommonProxy implements IGuiHandler
{
	public static int GUI_ID_BOWL = 0;
	public static int GUI_ID_SIMBA = 1;
	public static int GUI_ID_TIMON = 2;
	public static int GUI_ID_QUIVER = 3;
	public static int GUI_ID_QUESTS = 4;
	public static int GUI_ID_ACHIEVEMENTS = 5;
	public static int GUI_ID_TRAP = 6;
	public static int GUI_ID_DRUM = 7;
	
	public void onPreload() {}
	
	public void onLoad() {}
	
	public Object getServerGuiElement(int ID, EntityPlayer entityplayer, World world, int i, int j, int k)
	{
		if (ID == GUI_ID_BOWL)
		{
			TileEntity bowl = world.getBlockTileEntity(i, j, k);
			if (bowl != null && bowl instanceof LKTileEntityGrindingBowl)
			{
				return new LKContainerGrindingBowl(entityplayer, (LKTileEntityGrindingBowl)bowl);
			}
		}
		if (ID == GUI_ID_SIMBA)
		{
			Entity simba = getEntityFromID(i, world);
			if (simba != null && simba instanceof LKEntitySimba)
			{
				return new LKContainerSimba(entityplayer, (LKEntitySimba)simba);
			}
		}
		if (ID == GUI_ID_TIMON)
		{
			Entity timon = getEntityFromID(i, world);
			if (timon != null && timon instanceof LKEntityTimon)
			{
				return new LKContainerTimon(entityplayer, (LKEntityTimon)timon);
			}
		}
		if (ID == GUI_ID_QUIVER)
		{
			LKInventoryQuiver inv = LKItemDartQuiver.getQuiverInventory(i, world);
			return new LKContainerQuiver(entityplayer, inv);
		}
		if (ID == GUI_ID_QUESTS)
		{
			return new LKContainerItemInfo(entityplayer);
		}
		if (ID == GUI_ID_TRAP)
		{
			TileEntity trap = world.getBlockTileEntity(i, j, k);
			if (trap != null && trap instanceof LKTileEntityBugTrap)
			{
				return new LKContainerBugTrap(entityplayer, (LKTileEntityBugTrap)trap);
			}
		}
		if (ID == GUI_ID_DRUM)
		{
			TileEntity drum = world.getBlockTileEntity(i, j, k);
			if (drum != null && drum instanceof LKTileEntityDrum)
			{
				return new LKContainerDrum(entityplayer, world, (LKTileEntityDrum)drum);
			}
		}
		return null;
	}
	
	public Object getClientGuiElement(int ID, EntityPlayer entityplayer, World world, int i, int j, int k)
	{
		if (ID == GUI_ID_BOWL)
		{
			TileEntity bowl = world.getBlockTileEntity(i, j, k);
			if (bowl != null && bowl instanceof LKTileEntityGrindingBowl)
			{
				return new LKGuiGrindingBowl(entityplayer, (LKTileEntityGrindingBowl)bowl);
			}
		}
		if (ID == GUI_ID_SIMBA)
		{
			Entity simba = getEntityFromID(i, world);
			if (simba != null && simba instanceof LKEntitySimba)
			{
				return new LKGuiSimba(entityplayer, (LKEntitySimba)simba);
			}
		}
		if (ID == GUI_ID_TIMON)
		{
			Entity timon = getEntityFromID(i, world);
			if (timon != null && timon instanceof LKEntityTimon)
			{
				return new LKGuiTimon(entityplayer, (LKEntityTimon)timon);
			}
		}
		if (ID == GUI_ID_QUIVER)
		{
			LKInventoryQuiver inv = LKItemDartQuiver.getQuiverInventory(i, world);
			return new LKGuiQuiver(entityplayer, inv);
		}
		if (ID == GUI_ID_QUESTS)
		{
			return new LKGuiQuests(entityplayer);
		}
		if (ID == GUI_ID_ACHIEVEMENTS)
		{
			return new LKGuiAchievements(Minecraft.getMinecraft().statFileWriter, i);
		}
		if (ID == GUI_ID_TRAP)
		{
			TileEntity trap = world.getBlockTileEntity(i, j, k);
			if (trap != null && trap instanceof LKTileEntityBugTrap)
			{
				return new LKGuiBugTrap(entityplayer, (LKTileEntityBugTrap)trap);
			}
		}
		if (ID == GUI_ID_DRUM)
		{
			TileEntity drum = world.getBlockTileEntity(i, j, k);
			if (drum != null && drum instanceof LKTileEntityDrum)
			{
				return new LKGuiDrum(entityplayer, world, (LKTileEntityDrum)drum);
			}
		}
		return null;
	}
	
	public Entity getEntityFromID(int ID, World world)
	{
		List entities = world.loadedEntityList;
		if (!entities.isEmpty())
		{
			for (Object obj : entities)
			{
				Entity entity = (Entity)obj;
				if (entity != null && entity.entityId == ID)
				{
					return entity;
				}
			}
		}
		return null;
	}
	
	public int getGrindingBowlRenderID()
	{
		return 0;
	}
	
	public int getPillarRenderID()
	{
		return 0;
	}
	
	public int getStarAltarRenderID()
	{
		return 0;
	}
	
	public int getVaseRenderID()
	{
		return 0;
	}
	
	public int getBugTrapRenderID()
	{
		return 0;
	}
	
	public int getAridGrassRenderID()
	{
		return 0;
	}
	
	public int getKiwanoBlockRenderID()
	{
		return 0;
	}
	
	public int getKiwanoStemRenderID()
	{
		return 0;
	}
	
	public int getLeverRenderID()
	{
		return 0;
	}
	
	public int getLilyRenderID()
	{
		return 0;
	}

	public int getRugRenderID()
	{
		return 0;
	}
	
	public void setInPrideLandsPortal(EntityPlayer entityplayer)
	{
		if (!LKTickHandlerServer.playersInPortals.containsKey(entityplayer))
		{
			LKTickHandlerServer.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	public void setInOutlandsPortal(EntityPlayer entityplayer)
	{
		if (!LKTickHandlerServer.playersInOutPortals.containsKey(entityplayer))
		{
			LKTickHandlerServer.playersInOutPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	public void playPortalFXForUpendi(World world) {}
	
	public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5) {}
	
	public EntityPlayer getSPPlayer()
	{
		return null;
	}
}
