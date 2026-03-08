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
 
public class LKContainerTimon extends Container 
{ 
    private LKInventoryTimon timonInventory;

    public LKContainerTimon(EntityPlayer entityplayer, LKEntityTimon timon) 
    {
		timonInventory = timon.inventory;
		for (int i = 0; i < 5; i++)
		{
			addSlotToContainer(new LKSlotTimon(timonInventory, i, 15 + (i * 33), 32, timon, LKInventoryTimon.costs[i]));
		}
		
        for (int k = 0; k < 3; k++)  
        {  
            for (int j1 = 0; j1 < 9; j1++)  
            { 
                addSlotToContainer(new Slot(entityplayer.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 84 + k * 18));  
            }  
  
        }  
        for (int l = 0; l < 9; l++)  
        {  
            addSlotToContainer(new Slot(entityplayer.inventory, l, 8 + l * 18, 142));  
        }
    }
  
	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)  
    {  
        return timonInventory.isUseableByPlayer(entityplayer);  
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
            if (i < 5)
            {
                if (!mergeItemStack(itemstack1, 5, 41, true))
                {
                    return null;
                }
            }
            else if (i >= 5 && i < 32)
            {
                if (!mergeItemStack(itemstack1, 32, 41, false))
                {
                    return null;
                }
            }
            else if (i >= 32 && i < 41)
            {
                if (!mergeItemStack(itemstack1, 5, 32, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 5, 32, false))
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
	
	@Override
    public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer)
    {
		if (i >= 0)
		{
			Slot slotForTrading = (Slot)inventorySlots.get(i);
			if (slotForTrading instanceof LKSlotTimon)
			{
				if (!(hasTradableStack(entityplayer, ((LKSlotTimon)slotForTrading).cost)))
				{
					return null;
				}
			}
		}
		return super.slotClick(i, j, k, entityplayer);
    }
	
	private boolean hasTradableStack(EntityPlayer entityplayer, int cost)
	{
		for (int i = 0; i < entityplayer.inventory.mainInventory.length; i++)
		{
			ItemStack itemstack = entityplayer.inventory.getStackInSlot(i);
			if (itemstack != null && itemstack.itemID == mod_LionKing.bug.itemID && itemstack.stackSize >= cost)
			{
				return true;
			}
		}
		return false;
	}
}