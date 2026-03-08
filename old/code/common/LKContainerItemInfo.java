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

public class LKContainerItemInfo extends Container 
{
	public IInventory itemInfoInventory = new InventoryBasic("LKItemInfo", false, 1);
	
    public LKContainerItemInfo(EntityPlayer entityplayer) 
    {
		addSlotToContainer(new LKSlotItemInfo(itemInfoInventory, 0, 182, 104));
		
		for (int k = 0; k < 3; k++)
		{
			for (int j1 = 0; j1 < 9; j1++)
			{
				addSlotToContainer(new Slot(entityplayer.inventory, j1 + k * 9 + 9, 182 + j1 * 18, 139 + k * 18));
			}
		}
		for (int l = 0; l < 9; l++)
		{
			addSlotToContainer(new Slot(entityplayer.inventory, l, 182 + l * 18, 197));
		}
    }
  
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
	
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);

        if (!entityplayer.worldObj.isRemote)
        {
			ItemStack itemstack = getSlot(0).getStack();

			if (itemstack != null)
			{
				if (!entityplayer.inventory.addItemStackToInventory(itemstack))
				{
					entityplayer.dropPlayerItem(itemstack);
				}
				getSlot(0).putStack(null);
			}
        }
    }
  
	@Override
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 0)
            {
                if (!mergeItemStack(itemstack1, 1, 37, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (i >= 1 && i < 28)
            {
                if (!mergeItemStack(itemstack1, 28, 37, false))
                {
                    return null;
                }
            }
            else if (i >= 28 && i < 37)
            {
                if (!mergeItemStack(itemstack1, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 1, 37, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(entityplayer, itemstack1);
        }

        return itemstack;
    }
}
