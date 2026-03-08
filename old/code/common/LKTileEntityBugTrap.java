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

import java.util.Random;
import java.util.List;
import java.nio.ByteBuffer;

public class LKTileEntityBugTrap extends TileEntity implements IInventory, ISidedInventory
{
	private Random rand = new Random();
	private ItemStack[] inventory;
	private int closureTime = -1;
	private int[] inputSlots = new int[] {0, 1, 2, 3};
	private int[] outputSlots = new int[] {4};
	
	public LKTileEntityBugTrap()
	{
		inventory = new ItemStack[5];
	}
	
	@Override
    public void updateEntity()
	{
		if (rand.nextInt(4000) == 0 && !worldObj.isRemote)
		{
			float f = rand.nextFloat();
			if (f < calculateBugProbability())
			{
				boolean spawnedBug = false;
				int attempts = 0;
				while (!spawnedBug && attempts < 64)
				{
					double d = xCoord + rand.nextInt(8) - rand.nextInt(8);
					double d1 = yCoord + rand.nextInt(2) - rand.nextInt(2);
					double d2 = zCoord + rand.nextInt(8) - rand.nextInt(8);
					if (worldObj.isAirBlock(MathHelper.floor_double(d), MathHelper.floor_double(d1), MathHelper.floor_double(d2)) && (worldObj.getBlockId(MathHelper.floor_double(d), MathHelper.floor_double(d1) - 1, MathHelper.floor_double(d2)) == Block.grass.blockID || worldObj.getBlockId(MathHelper.floor_double(d), MathHelper.floor_double(d1) - 1, MathHelper.floor_double(d2)) == Block.dirt.blockID))
					{
						LKEntityBug bug = new LKEntityBug(worldObj);
						bug.setLocationAndAngles(d, d1, d2, rand.nextFloat(), rand.nextFloat());
						worldObj.spawnEntityInWorld(bug);
						spawnedBug = true;
					}
					attempts++;
				}
			}
		}
		List list = worldObj.getEntitiesWithinAABB(LKEntityBug.class, AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)(xCoord + 1), (double)(yCoord + 1), (double)(zCoord + 1)).expand(16D, 8D, 16D));
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				LKEntityBug bug = (LKEntityBug)list.get(i);
				if (bug.targetTrap == this)
				{
					if (bug.getDistanceSq((double)xCoord + 0.5F, (double)yCoord + 0.5F, (double)zCoord + 0.5F) <= 4.0F)
					{
						if (bug.trapTick == -1 && getAverageBaitSaturation() > 0.0F)
						{
							int j = rand.nextInt(4);
							ItemStack itemstack = getStackInSlot(j);
							if (itemstack != null)
							{
								if (itemstack.getItem() instanceof ItemFood)
								{
									if (itemstack.stackSize > 1)
									{
										itemstack.stackSize--;
										bug.trapTick = 0;
									}
									else
									{
										if (itemstack.getItem().getContainerItemStack(itemstack) != null)
										{
											setInventorySlotContents(j, itemstack.getItem().getContainerItemStack(itemstack));
											bug.trapTick = 0;
										}
										else if (itemstack.itemID == Item.bowlSoup.itemID)
										{
											setInventorySlotContents(j, new ItemStack(Item.bowlEmpty));
											bug.trapTick = 0;
										}
										else
										{
											setInventorySlotContents(j, null);
											bug.trapTick = 0;
										}
									}
								}
							}
						}
						if (bug.trapTick >= 34)
						{
							double d = ((xCoord + 0.5D) - bug.posX) / 8.0D;
							double d1 = ((yCoord) - bug.posY) / 8.0D;
							double d2 = ((zCoord + 0.5D) - bug.posZ) / 8.0D;
							double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
							double d4 = 1.0D - d3;

							if (d4 > 0.0D)
							{
								d4 *= d4;
								bug.motionX += d / d3 * d4 * 0.06D;
								bug.motionY += d1 / d3 * d4 * 0.06D;
								bug.motionZ += d2 / d3 * d4 * 0.06D;
							}

							bug.moveEntity(bug.motionX, bug.motionY, bug.motionZ);
						}
						if (bug.trapTick == 36)
						{
							closureTime = 0;
						}
						if (bug.trapTick >= 40 && !worldObj.isRemote)
						{
							if (getStackInSlot(4) == null)
							{
								setInventorySlotContents(4, new ItemStack(mod_LionKing.bug));
							}
							else if (getStackInSlot(4).itemID == mod_LionKing.bug.itemID && getStackInSlot(4).stackSize < 64)
							{
								getStackInSlot(4).stackSize++;
							}
							else
							{
								bug.dropItem(mod_LionKing.bug.itemID, 1);
							}
							bug.setDead();
						}
					}
					else
					{
						bug.trapTick = -1;
					}
				}
			}
		}
		if (closureTime > -1 && closureTime < 50)
		{
			closureTime++;
		}
		if (closureTime >= 50)
		{
			closureTime = -1;
		}
		calculateBugProbability();
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, closureTime >= 0 && closureTime < 8 ? closureTime : (closureTime >= 8 && closureTime < 42 ? 8 : (closureTime >= 42 && closureTime < 50 ? 50 - closureTime : 0)), 3);
	}

	public float getAverageBaitSaturation()
	{
		float f = 0.0F;
		for (int i = 0; i < 4; i++)
		{
			ItemStack itemstack = getStackInSlot(i);
			if (itemstack != null && Item.itemsList[itemstack.itemID] instanceof ItemFood)
			{
				float f1 = ((ItemFood)Item.itemsList[itemstack.itemID]).getSaturationModifier();
				float f2 = (float)itemstack.stackSize / 64.0F;
				f2 += 0.6F;
				if (f2 > 1.0F)
				{
					f2 = 1.0F;
				}
				f += f1 * f2;
			}
		}
		return f * 0.25F;
	}
	
	private float calculateBugProbability()
	{
		if (worldObj.provider.dimensionId != mod_LionKing.idPrideLands)
		{
			return 0.0F;
		}
		List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)(xCoord + 1), (double)(yCoord + 1), (double)(zCoord + 1)).expand(16D, 16D, 16D));
		if (!list.isEmpty())
		{
			return 0.0F;
		}
		if (getAverageBaitSaturation() <= 0.0F)
		{
			return 0.0F;
		}
		
		float probability = 0.0F;
		for (int i = -8; i <= 8; i++)
		{
			for (int j = -4; j <= 4; j++)
			{
				for (int k = -8; k <= 8; k++)
				{
					int l = worldObj.getBlockId(xCoord + i, yCoord + j, zCoord + k);
					if (l == Block.tallGrass.blockID)
					{
						probability += 0.02F;
					}
				}
			}
		}
		
		BiomeGenBase biome = worldObj.getWorldChunkManager().getBiomeGenAt(xCoord, zCoord);
		if (biome instanceof LKBiomeGenMountains)
		{
			probability /= 2.0F;
		}
		if (biome instanceof LKBiomeGenAridSavannah)
		{
			probability /= 3.0F;
		}
		if (biome instanceof LKBiomeGenDesert)
		{
			probability /= 10.0F;
		}
		if (biome instanceof LKBiomeGenRainforest)
		{
			probability *= 2.0F;
		}
		if (biome instanceof LKBiomeGenWoodedSavannah || biome instanceof LKBiomeGenBananaForest)
		{
			probability *= 1.5F;
		}
		if (worldObj.getWorldInfo().isRaining())
		{
			probability *= 1.3F;
		}
		
		int numNearbyTrapsIncludingThis = 0;
		for (int i = -8; i <= 8; i++)
		{
			for (int j = -4; j <= 4; j++)
			{
				for (int k = -8; k <= 8; k++)
				{
					TileEntity tileentity = worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
					if (tileentity != null && tileentity instanceof LKTileEntityBugTrap)
					{
						numNearbyTrapsIncludingThis++;
					}
				}
			}
		}
		
		float nearbyTrapReductionFactor = 0.3F + (0.7F / (float)numNearbyTrapsIncludingThis);
		probability *= nearbyTrapReductionFactor;
		
		probability *= 1.0F + getAverageBaitSaturation();
		float finalProbability = probability / 10.0F;
		if (finalProbability > 0.9F)
		{
			finalProbability = 0.9F;
		}
		return finalProbability;
	}
	
	@Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

	@Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

	@Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (inventory[i] != null)
        {
            if (inventory[i].stackSize <= j)
            {
                ItemStack itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = inventory[i].splitStack(j);
            if (inventory[i].stackSize == 0)
            {
                inventory[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

	@Override
    public String getInvName()
    {
        return "Bug Trap";
    }
	
	@Override
    public boolean isInvNameLocalized()
    {
        return false;
    }
	
	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("BugSlots");
            if (byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
		closureTime = nbttagcompound.getInteger("Closure");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("BugSlots", (byte)i);
                inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
		nbttagcompound.setInteger("Closure", closureTime);
    }

	@Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
	
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
    
	@Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inventory[i] != null)
        {
            ItemStack stack = inventory[i];
            inventory[i] = null;
            return stack;
        }
        else
        {
            return null;
        }
    }
	
	@Override
    public void openChest() {}

	@Override
    public void closeChest() {}
	
	@Override
    public Packet getDescriptionPacket()
    {
		byte[] posX = ByteBuffer.allocate(4).putInt(xCoord).array();
		byte[] posY = ByteBuffer.allocate(4).putInt(yCoord).array();
		byte[] posZ = ByteBuffer.allocate(4).putInt(zCoord).array();
		byte[] data = new byte[44];
		for (int i = 0; i < 4; i++)
		{
			data[i] = posX[i];
			data[i + 4] = posY[i];
			data[i + 8] = posZ[i];
			ItemStack itemstack = getStackInSlot(i);
			byte[] id = new byte[4];
			byte[] damage = new byte[4];
			if (itemstack != null)
			{
				id = ByteBuffer.allocate(4).putInt(itemstack.itemID).array();
				damage = ByteBuffer.allocate(4).putInt(itemstack.getItemDamage()).array();
			}
			for (int j = 0; j < 4; j++)
			{
				data[12 + (i * 8) + j] = id[j];
				data[16 + (i * 8) + j] = damage[j];
			}
		}
		return new Packet250CustomPayload("lk.tileEntity", data);
    }
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        if (slot < 4)
		{
			Item item = itemstack.getItem();
			if (item instanceof ItemFood)
			{
				ItemFood food = (ItemFood)item;
				if (food.isWolfsFavoriteMeat())
				{
					return false;
				}
				else if (food == Item.fishRaw || food == Item.fishCooked)
				{
					return false;
				}
				return food.getSaturationModifier() > 0.0F;
			}
			return false;
		}
		return false;
    }
	
	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
		if (side == 0)
		{
			return outputSlots;
		}
		return inputSlots;
    }
	
	@Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int j)
    {
		return isItemValidForSlot(slot, itemstack);
    }
	
	@Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int i)
    {
        return true;
    }
}
