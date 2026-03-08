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

public class LKTileEntityDrum extends TileEntity implements IInventory
{
    public byte note = 0;
    public ItemStack[] inventory = new ItemStack[8];

	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
	    NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }
        nbt.setTag("Items", items);
        nbt.setByte("note", note);
    }

	@Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items");
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound itemData = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = itemData.getByte("Slot");
            if (byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = ItemStack.loadItemStackFromNBT(itemData);
            }
        }
        note = nbt.getByte("note");
        if (note < 0)
        {
            note = 0;
        }
        if (note > 24)
        {
            note = 24;
        }
    }

    public void changePitch()
    {
        note = (byte)((note + 1) % 25);
        onInventoryChanged();
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
    }

	@Override
    public String getInvName()
    {
        return "Bongo Drum";
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
    public boolean isInvNameLocalized()
    {
        return false;
    }
	
	@Override
    public void openChest() {}

	@Override
    public void closeChest() {}
	
	@Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
		return true;
	}
}
