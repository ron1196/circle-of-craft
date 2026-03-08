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

import java.util.Map;
import java.util.HashMap;

public class LKInventoryQuiver extends WorldSavedData implements IInventory
{
	private ItemStack[] inventory;
	private ItemStack theQuiver;

	public LKInventoryQuiver(String s)
	{
		super(s);
		inventory = new ItemStack[6];
	}

	@Override
	public int getSizeInventory()
	{
		return 6;
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
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0)
			{
				inventory[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		}
		else
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
		onInventoryChanged();
	}

	@Override
	public String getInvName()
	{
		return "Quiver";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		ItemStack[] quiverInventory = new ItemStack[6];
		NBTTagList taglist = data.getTagList("QuiverSlots");
		if (taglist != null)
		{
			for	(int i = 0; i < taglist.tagCount(); i++)
			{
				NBTTagCompound nbt = (NBTTagCompound)taglist.tagAt(i);
				byte byte0 = nbt.getByte("Slot");
				if (byte0 >= 0 && byte0 < quiverInventory.length)
				{
					quiverInventory[byte0] = ItemStack.loadItemStackFromNBT(nbt);
				}
			}
		}
		inventory = quiverInventory;
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		NBTTagList taglist = new NBTTagList();
		for	(int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setByte("Slot", (byte)i);
				inventory[i].writeToNBT(nbt);
				taglist.appendTag(nbt);
			}
		}
		data.setTag("QuiverSlots", taglist);
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest()
	{
		onInventoryChanged();
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return getStackInSlot(i);
	}
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
		return true;
	}
	
	@Override
    public boolean isInvNameLocalized()
    {
        return false;
    }
}
