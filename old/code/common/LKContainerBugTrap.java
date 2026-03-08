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

public class LKContainerBugTrap extends Container
{
    private LKTileEntityBugTrap bugTrap;

    public LKContainerBugTrap(EntityPlayer entityplayer, LKTileEntityBugTrap trap)
    {
        bugTrap = trap;
		
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				addSlotToContainer(new LKSlotBugBait(trap, j + (i * 2), (i * 18) + 40, (j * 18) + 28));
			}
		}
		
        addSlotToContainer(new LKSlotBugTrap(trap, 4, 109, 32));
		
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(entityplayer.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }
        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(entityplayer.inventory, j, 8 + j * 18, 142));
        }
    }

	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return bugTrap.isUseableByPlayer(entityplayer);
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
			
            if (i == 4)
            {
                if (!mergeItemStack(itemstack1, 5, 41, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (i > 3)
            {
				if (((Slot)inventorySlots.get(0)).isItemValid(itemstack1))
				{
                    if (!mergeItemStack(itemstack1, 0, 4, false))
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
                else if (i >= 32 && i < 41 && !mergeItemStack(itemstack1, 5, 32, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 5, 41, false))
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