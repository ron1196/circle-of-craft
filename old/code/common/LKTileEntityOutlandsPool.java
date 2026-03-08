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

import java.util.ArrayList;
import java.util.List;

public class LKTileEntityOutlandsPool extends TileEntity
{
	public static ArrayList inventory = new ArrayList();
	private static int timeUntilInventoryClear;
	
	public static void updateInventory(World world)
	{
		if (world.isRemote)
		{
			return;
		}
		
		if (timeUntilInventoryClear > 0)
		{
			timeUntilInventoryClear--;
		}
		
		if (timeUntilInventoryClear == 0)
		{
			if (!inventory.isEmpty())
			{
				ArrayList silverList = new ArrayList();
				ArrayList kivuliteList = new ArrayList();
				int silver = 0;
				int kivulite = 0;
				
				for	(int i = 0; i < inventory.size(); i++)
				{
					ListedItem l = (ListedItem)inventory.get(i);
					if (l != null)
					{
						if (l.item.itemID == mod_LionKing.silver.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								silver++;
								silverList.add(Integer.valueOf(i));
							}
						}
						if (l.item.itemID == mod_LionKing.kivulite.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								kivulite++;
								kivuliteList.add(Integer.valueOf(i));
							}
						}
					}
				}
				
				if (silver > 1 && kivulite > 4)
				{
					int count = MathHelper.floor_double(silver / 2);
					if (MathHelper.floor_double(kivulite / 5) < count)
					{
						count = MathHelper.floor_double(kivulite / 5);
					}
					
					for (int i = 0; i < count; i++)
					{
						for (int k = 0; k < 2; k++)
						{
							int j = (Integer)silverList.get(i * 2 + k);
							ListedItem l = (ListedItem)inventory.get(j);
							if (l != null)
							{
								ItemStack itemstack = l.item;
								itemstack.stackSize--;
								l.item = itemstack;
								if (l.item.stackSize <= 0)
								{
									l = null;
								}
								inventory.set(j, l);
							}
						}
						for (int k = 0; k < 5; k++)
						{
							int j = (Integer)kivuliteList.get(i * 5 + k);
							ListedItem l = (ListedItem)inventory.get(j);
							if (l != null)
							{
								ItemStack itemstack = l.item;
								itemstack.stackSize--;
								l.item = itemstack;
								if (l.item.stackSize <= 0)
								{
									l = null;
								}
								inventory.set(j, l);
							}
						}
					}
					
					for (int i = 0; i < count; i++)
					{
						EntityItem item = new EntityItem(world, (double)LKLevelData.moundX + 0.45D + (double)(world.rand.nextFloat() * 0.1F), (double)LKLevelData.moundY + 9D, (double)LKLevelData.moundZ + 0.45D + (double)(world.rand.nextFloat() * 0.1F), new ItemStack(mod_LionKing.outlandsHelm));
						item.delayBeforeCanPickup = 10;
						item.motionX = 0.0D;
						item.motionY = 0.4D + (double)(world.rand.nextFloat() / 5.0F);
						item.motionZ = 0.0D;
						world.spawnEntityInWorld(item);
						world.playSoundAtEntity(item, "random.explode", 1.5F, 0.5F + world.rand.nextFloat() * 0.2F);
					}
				}
				
				ArrayList featherBlueList = new ArrayList();
				ArrayList featherYellowList = new ArrayList();
				ArrayList featherRedList = new ArrayList();
				ArrayList featherBlackList = new ArrayList();
				int featherBlue = 0;
				int featherYellow = 0;
				int featherRed = 0;
				int featherBlack = 0;
				
				for	(int i = 0; i < inventory.size(); i++)
				{
					ListedItem l = (ListedItem)inventory.get(i);
					if (l != null)
					{
						if (l.item.itemID == mod_LionKing.featherBlue.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								featherBlue++;
								featherBlueList.add(Integer.valueOf(i));
							}
						}
						if (l.item.itemID == mod_LionKing.featherYellow.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								featherYellow++;
								featherYellowList.add(Integer.valueOf(i));
							}
						}
						if (l.item.itemID == mod_LionKing.featherRed.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								featherRed++;
								featherRedList.add(Integer.valueOf(i));
							}
						}
						if (l.item.itemID == mod_LionKing.featherBlack.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								featherBlack++;
								featherBlackList.add(Integer.valueOf(i));
							}
						}
					}
				}
				
				if (featherBlue > 0 && featherYellow > 0 && featherRed > 0 && featherBlack > 0)
				{
					int count = featherBlue;
					if (featherYellow < count)
					{
						count = featherYellow;
					}
					if (featherRed < count)
					{
						count = featherRed;
					}
					if (featherBlack < count)
					{
						count = featherBlack;
					}
					
					for (int i = 0; i < count; i++)
					{
						int j = (Integer)featherBlueList.get(i);
						ListedItem l = (ListedItem)inventory.get(j);
						if (l != null)
						{
							ItemStack itemstack = l.item;
							itemstack.stackSize--;
							l.item = itemstack;
							if (l.item.stackSize <= 0)
							{
								l = null;
							}
							inventory.set(j, l);
						}
						j = (Integer)featherYellowList.get(i);
						l = (ListedItem)inventory.get(j);
						if (l != null)
						{
							ItemStack itemstack = l.item;
							itemstack.stackSize--;
							l.item = itemstack;
							if (l.item.stackSize <= 0)
							{
								l = null;
							}
							inventory.set(j, l);
						}
						j = (Integer)featherRedList.get(i);
						l = (ListedItem)inventory.get(j);
						if (l != null)
						{
							ItemStack itemstack = l.item;
							itemstack.stackSize--;
							l.item = itemstack;
							if (l.item.stackSize <= 0)
							{
								l = null;
							}
							inventory.set(j, l);
						}
						j = (Integer)featherBlackList.get(i);
						l = (ListedItem)inventory.get(j);
						if (l != null)
						{
							ItemStack itemstack = l.item;
							itemstack.stackSize--;
							l.item = itemstack;
							if (l.item.stackSize <= 0)
							{
								l = null;
							}
							inventory.set(j, l);
						}
					}
					
					for (int i = 0; i < count; i++)
					{
						EntityItem item = new EntityItem(world, (double)LKLevelData.moundX + 0.45D + (double)(world.rand.nextFloat() * 0.1F), (double)LKLevelData.moundY + 9D, (double)LKLevelData.moundZ + 0.45D + (double)(world.rand.nextFloat() * 0.1F), new ItemStack(mod_LionKing.outlandsFeather));
						item.delayBeforeCanPickup = 10;
						item.motionX = 0.0D;
						item.motionY = 0.4D + (double)(world.rand.nextFloat() / 5.0F);
						item.motionZ = 0.0D;
						world.spawnEntityInWorld(item);
						world.playSoundAtEntity(item, "random.explode", 1.5F, 0.5F + world.rand.nextFloat() * 0.2F);
					}
				}
				
				ArrayList rafikiCoinList = new ArrayList();
				
				for	(int i = 0; i < inventory.size(); i++)
				{
					ListedItem l = (ListedItem)inventory.get(i);
					if (l != null)
					{
						if (l.item.itemID == mod_LionKing.rafikiCoin.itemID)
						{
							for (int j = 0; j < l.item.stackSize; j++)
							{
								rafikiCoinList.add(Integer.valueOf(i));
							}
						}
					}
				}
				
				if (!rafikiCoinList.isEmpty())
				{
					for (int i = 0; i < rafikiCoinList.size(); i++)
					{
						int j = (Integer)rafikiCoinList.get(i);
						ListedItem l = (ListedItem)inventory.get(j);
						if (l != null)
						{
							ItemStack itemstack = l.item;
							itemstack.stackSize--;
							l.item = itemstack;
							if (l.item.stackSize <= 0)
							{
								l = null;
							}
							inventory.set(j, l);
						}
					}
					
					for (int i = 0; i < rafikiCoinList.size(); i++)
					{
						EntityItem item = new EntityItem(world, (double)LKLevelData.moundX + 0.45D + (double)(world.rand.nextFloat() * 0.1F), (double)LKLevelData.moundY + 9D, (double)LKLevelData.moundZ + 0.45D + (double)(world.rand.nextFloat() * 0.1F), new ItemStack(mod_LionKing.ziraCoin));
						item.delayBeforeCanPickup = 10;
						item.motionX = 0.0D;
						item.motionY = 0.4D + (double)(world.rand.nextFloat() / 5.0F);
						item.motionZ = 0.0D;
						world.spawnEntityInWorld(item);
						world.playSoundAtEntity(item, "random.explode", 1.5F, 0.5F + world.rand.nextFloat() * 0.2F);
					}
				}
			
				for	(int i = 0; i < inventory.size(); i++)
				{
					ListedItem l = (ListedItem)inventory.get(i);
					if (l != null)
					{
						int randPosX = LKLevelData.moundX + (world.rand.nextBoolean() ? 1 : -1);
						int randPosZ = LKLevelData.moundZ + (world.rand.nextBoolean() ? 1 : -1);
						
						EntityItem item = new EntityItem(world, (double)randPosX + 0.25D + (double)(world.rand.nextFloat() / 2.0F), (double)LKLevelData.moundY + 8D, (double)randPosZ + 0.25D + (double)(world.rand.nextFloat() / 2.0F), l.item);
						item.delayBeforeCanPickup = 10;
						item.motionX = 0.0D;
						item.motionY = 0.3D + (double)(world.rand.nextFloat() / 5.0F);
						item.motionZ = 0.0D;
						world.spawnEntityInWorld(item);
					}
				}
			
				inventory.clear();
			}
		}
	}
	
	public static void writeInventoryToNBT(NBTTagCompound levelData)
	{
		NBTTagList taglist = new NBTTagList();
		
		for	(int i = 0; i < inventory.size(); i++)
		{
			ListedItem l = (ListedItem)inventory.get(i);
			if (l != null)
			{
				ItemStack itemstack = l.item;
				Location location = l.location;
				NBTTagCompound nbt = new NBTTagCompound();
				
				itemstack.writeToNBT(nbt);
				nbt.setDouble("X", location.posX);
				nbt.setDouble("Y", location.posY);
				nbt.setDouble("Z", location.posZ);
				nbt.setLong("TIMESTAMP", l.timestamp);
				
				taglist.appendTag(nbt);
			}
		}
		
		levelData.setTag("PoolInventory", taglist);
	}
	
	public static void readInventoryFromNBT(NBTTagCompound levelData)
	{
		inventory.clear();
		
		NBTTagList taglist = levelData.getTagList("PoolInventory");
		if (taglist != null)
		{
			for	(int i1 = 0; i1 < taglist.tagCount(); i1++)
			{
				NBTTagCompound nbt = (NBTTagCompound)taglist.tagAt(i1);
				
				ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbt);
				double d = nbt.getDouble("X");
				double d1 = nbt.getDouble("Y");
				double d2 = nbt.getDouble("Z");
				long l = nbt.getLong("TIMESTAMP");
				
				inventory.add(new ListedItem(l, itemstack, new Location(d, d1, d2)));
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		if (worldObj.provider.dimensionId == mod_LionKing.idOutlands && !worldObj.isRemote)
		{
			List list = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 0.875, zCoord + 1));
			if (!list.isEmpty())
			{
				for (int i = 0; i < list.size(); i++)
				{
					EntityItem entity = (EntityItem)list.get(i);
					ItemStack itemstack = entity.getEntityItem();
					worldObj.playSoundAtEntity(entity, "random.fizz", 0.7F, 1.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
					if (itemstack != null)
					{
						ListedItem item = new ListedItem(worldObj.getWorldTime(), itemstack, new Location(entity.posX, entity.posY, entity.posZ));
						if (!inventory.contains(item))
						{
							inventory.add(item);
							timeUntilInventoryClear = 75;
						}
					}
					entity.setDead();
				}
			}
		}
		
		if (timeUntilInventoryClear > 0)
		{
			if (worldObj.rand.nextInt(3) == 0)
			{
				worldObj.spawnParticle("smoke", (double)((float)xCoord + worldObj.rand.nextFloat()), (double)((float)yCoord + 0.8F), (double)((float)zCoord + worldObj.rand.nextFloat()), 0.0D, 0.0D + (double)(worldObj.rand.nextFloat() * 0.25F), 0.0D);
			}
			if (worldObj.rand.nextInt(100) == 0)
			{
				worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.fizz", 0.7F, 1.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
			}
		}
	}
	
	private static class ListedItem
	{
		public long timestamp;
		public ItemStack item;
		public Location location;
		
		public ListedItem(long l, ItemStack itemstack, Location coords)
		{	
			timestamp = l;
			item = itemstack;
			location = coords;
		}
		
		public boolean equals(Object obj)
		{
			if (!(obj instanceof ListedItem) || obj == null)
			{
				return false;
			}
			ListedItem l = (ListedItem)obj;
			return timestamp == l.timestamp && location.equals(l.location) && item.itemID == l.item.itemID && item.stackSize == l.item.stackSize && item.getItemDamage() == l.item.getItemDamage();
		}
	}
	
	private static class Location
	{
		public double posX;
		public double posY;
		public double posZ;
		
		public Location(double d, double d1, double d2)
		{
			posX = d;
			posY = d1;
			posZ = d2;
		}
		
		public boolean equals(Object obj)
		{
			if (!(obj instanceof Location) || obj == null)
			{
				return false;
			}
			Location l = (Location)obj;
			return posX == l.posX && posY == l.posY && posZ == l.posZ;
		}
	}
}