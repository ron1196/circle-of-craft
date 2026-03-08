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

import java.util.Iterator;

public class LKContainerGrindingBowl extends Container
{
    public LKTileEntityGrindingBowl grindingBowl;
    private int grindTime;

    public LKContainerGrindingBowl(EntityPlayer entityplayer, LKTileEntityGrindingBowl bowl)
    {
        grindTime = 0;
        grindingBowl = bowl;
		
        addSlotToContainer(new Slot(bowl, 0, 40, 35));
        addSlotToContainer(new LKSlotGrindingBowl(bowl, 1, 116, 35));
		
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
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, grindingBowl.grindTime);
    }

	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
		
        Iterator iterator = crafters.iterator();
        while (iterator.hasNext())
        {
            ICrafting slot = (ICrafting)iterator.next();
            if (grindTime != grindingBowl.grindTime)
            {
                slot.sendProgressBarUpdate(this, 0, grindingBowl.grindTime);
            }
        }
		
        grindTime = grindingBowl.grindTime;
    }

	@Override
    public void updateProgressBar(int i, int j)
    {
        if (i == 0)
        {
            grindingBowl.grindTime = j;
        }
    }

	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return grindingBowl.isUseableByPlayer(entityplayer);
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

            if (i == 1)
            {
                if (!mergeItemStack(itemstack1, 2, 38, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (i != 0)
            {
				if (LKGrindingRecipes.grinding().getGrindingResult(itemstack1) != null)
				{
                    if (!mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
				}
                else if (i >= 2 && i < 29)
                {
                    if (!mergeItemStack(itemstack1, 29, 38, false))
                    {
                        return null;
                    }
                }
                else if (i >= 29 && i < 38 && !mergeItemStack(itemstack1, 2, 29, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 2, 38, false))
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