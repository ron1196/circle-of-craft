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
import java.util.Arrays;
 
public class LKInventoryTimon implements IInventory 
{ 
	public static int[] costs = new int[]
	{
		5,
		6,
		7,
		4,
		10
	};
	
    public ItemStack[] inventory = new ItemStack[]
	{
		new ItemStack(mod_LionKing.tunnahDiggah),
		new ItemStack(mod_LionKing.pumbaaBomb),
		new ItemStack(mod_LionKing.crystal),
		new ItemStack(mod_LionKing.xpGrub),
		new ItemStack(mod_LionKing.amulet)
	};
	
    public LKEntityTimon theTimon;

    public LKInventoryTimon(LKEntityTimon entity) 
    { 
        theTimon = entity;
    }
  
    private int getInventorySlotContainItem(int i)  
    {  
        for (int j = 0; j < inventory.length; j++)
        {  
            if (inventory[j] != null && inventory[j].itemID == i)
            {  
                return j;
            }  
        }
		
        return -1;  
    }  
  
    private int storeItemStack(ItemStack itemstack)  
    {  
        for (int i = 0; i < inventory.length; i++)  
        {  
            if (inventory[i] != null && inventory[i].itemID == itemstack.itemID && inventory[i].isStackable() && inventory[i].stackSize < inventory[i].getMaxStackSize() && inventory[i].stackSize < getInventoryStackLimit() && (!inventory[i].getHasSubtypes() || inventory[i].getItemDamage() == itemstack.getItemDamage()))  
            {  
                return i;  
            }  
        }  
  
        return -1;  
    }  
  
    private int getFirstEmptyStack()  
    {  
        for (int i = 0; i < inventory.length; i++)  
        {  
            if (inventory[i] == null)  
            {  
                return i;  
            }  
        }  
  
        return -1;  
    }  
  
    private int storePartialItemStack(ItemStack itemstack)  
    {  
        int i = itemstack.itemID;  
        int j = itemstack.stackSize;  
        int k = storeItemStack(itemstack);  
        if (k < 0)  
        {  
            k = getFirstEmptyStack();  
        }  
        if (k < 0)  
        {  
            return j;  
        }  
        if (inventory[k] == null)  
        {  
            inventory[k] = new ItemStack(i, 0, itemstack.getItemDamage());  
        }  
        int l = j;  
        if (l > inventory[k].getMaxStackSize() - inventory[k].stackSize)  
        {  
            l = inventory[k].getMaxStackSize() - inventory[k].stackSize;  
        }  
        if (l > getInventoryStackLimit() - inventory[k].stackSize)  
        {  
            l = getInventoryStackLimit() - inventory[k].stackSize;  
        }  
        if (l == 0)  
        {  
            return j;  
        }
		else  
        {  
            j -= l;  
            inventory[k].stackSize += l;  
            inventory[k].animationsToGo = 5;  
            return j;  
        }  
    }  

	@Override
    public ItemStack decrStackSize(int i, int j)  
    {  
        ItemStack aitemstack[] = inventory;  
        if (aitemstack[i] != null)  
        {  
            if (aitemstack[i].stackSize <= j)  
            {  
                ItemStack itemstack = aitemstack[i];  
                aitemstack[i] = null;  
                return itemstack;  
            }  
            ItemStack itemstack1 = aitemstack[i].splitStack(j);  
            if (aitemstack[i].stackSize == 0)  
            {  
                aitemstack[i] = null;  
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
    }  
  
    public NBTTagList writeToNBT(NBTTagList nbttaglist)  
    {  
        for (int i = 0; i < inventory.length; i++)  
        {  
            if (inventory[i] != null)  
            {  
                NBTTagCompound nbttagcompound = new NBTTagCompound();  
                nbttagcompound.setByte("Slot", (byte)i);  
                inventory[i].writeToNBT(nbttagcompound);  
                nbttaglist.appendTag(nbttagcompound);  
            }  
        }   
        return nbttaglist;  
    }  
  
    public void readFromNBT(NBTTagList nbttaglist)  
    {  
        inventory = new ItemStack[5];  
        for (int i = 0; i < nbttaglist.tagCount(); i++)  
        {  
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);  
            int j = nbttagcompound.getByte("Slot") & 0xff;  
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);  
            if (itemstack.getItem() == null)  
            {  
                continue;  
            }  
            if (j >= 0 && j < inventory.length)  
            {  
                inventory[j] = itemstack;  
            }  
        }
    }  
  
	@Override
    public int getSizeInventory()
    {  
        return inventory.length + 1;  
    }  
  
	@Override
    public ItemStack getStackInSlot(int i)
    {
        ItemStack aitemstack[] = inventory;
        return aitemstack[i];
    }  
  
	@Override
    public String getInvName()  
    {  
        return "Inventory";  
    }  
  
	@Override
    public int getInventoryStackLimit()  
    {
        return 1;
    }
  
	@Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (theTimon == null || theTimon.getHealth() <= 0)
        {
            return false;
        }
        return entityplayer.getDistanceSqToEntity(theTimon) <= 144D;
    } 
  
    public void dropAllItems()  
    {  
        for (int i = 0; i < inventory.length; i++)  
        {  
            if (inventory[i] != null)  
            {  
                theTimon.entityDropItem(inventory[i], 0.0F);  
                inventory[i] = null;  
            }  
        } 
    }
	
	@Override
	public void onInventoryChanged() {}
	
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