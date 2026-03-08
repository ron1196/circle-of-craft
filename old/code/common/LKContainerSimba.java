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
 
public class LKContainerSimba extends Container 
{ 
    private LKInventorySimba simbaInventory;

    public LKContainerSimba(EntityPlayer player, LKEntitySimba simba)
    {
        simbaInventory = simba.inventory;
        for (int l = 0; l < 9; l++)
        {
            addSlotToContainer(new Slot(simbaInventory, l, 8 + l * 18, 31));
        }
        
        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
                addSlotToContainer(new Slot(player.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 84 + k * 18));
            }
        }
		
        for (int l = 0; l < 9; l++)
        {
            addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
        }
    }
  
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {  
        return simbaInventory.isUseableByPlayer(entityplayer);
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
            if (i < 9)
            {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 9, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0)  
            {  
                slot.putStack(null);  
            }
			else  
            {  
                slot.onSlotChanged();  
            }
			
            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(entityplayer, itemstack1);
            }
            else
            {
                return null;
            }
        }  
        return itemstack;  
	}
}
