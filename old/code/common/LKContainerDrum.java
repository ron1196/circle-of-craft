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

import java.util.*;

public class LKContainerDrum extends Container
{
    private IInventory theDrum;
	private IInventory theDrumEnchant = new LKInventoryDrum(this, "Bongo Drum", false, 1);
	private World worldObj;
    public int[] enchantLevels = new int[3];
	private Random rand = new Random();
	private EntityPlayer thePlayer;
	
    public LKContainerDrum(EntityPlayer entityplayer, World world, LKTileEntityDrum drum)
    {
        theDrum = new LKInventoryDrum(this, false, drum);
		worldObj = world;
		thePlayer = entityplayer;
		
		addSlotToContainer(new LKSlotDrum(theDrumEnchant, 0, 43, 43));
		
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				addSlotToContainer(new LKSlotNote(theDrum, j + (i * 4), i == 0 ? 8 : 152, j * 20 + 5));
			}
		}        
		
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(entityplayer.inventory, k + i * 9 + 9, 8 + k * 18, 85 + i * 18));
            }

        }
        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(entityplayer.inventory, j, 8 + j * 18, 143));
        }
    }
	
	@Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, enchantLevels[0]);
        crafting.sendProgressBarUpdate(this, 1, enchantLevels[1]);
        crafting.sendProgressBarUpdate(this, 2, enchantLevels[2]);
    }
	
	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        Iterator i = crafters.iterator();
        while (i.hasNext())
        {
            ICrafting crafting = (ICrafting)i.next();
            crafting.sendProgressBarUpdate(this, 0, enchantLevels[0]);
            crafting.sendProgressBarUpdate(this, 1, enchantLevels[1]);
            crafting.sendProgressBarUpdate(this, 2, enchantLevels[2]);
        }
    }

    @Override
    public void updateProgressBar(int i, int j)
    {
        if (i >= 0 && i <= 2)
        {
            enchantLevels[i] = j;
        }
        else
        {
            super.updateProgressBar(i, j);
        }
    }
	
	@Override
    public void onCraftMatrixChanged(IInventory inv)
    {
        if (inv == theDrumEnchant || inv == theDrum)
        {
            ItemStack itemstack = theDrumEnchant.getStackInSlot(0);
            if (itemstack != null && itemstack.isItemEnchantable())
			{
                if (!worldObj.isRemote)
                {
					int noteValues = 0;
					for (int i = 0; i < 8; i++)
					{
						ItemStack note = theDrum.getStackInSlot(i);
						if (note != null && note.itemID == mod_LionKing.note.itemID)
						{
							noteValues += note.stackSize * LKItemNote.getNoteValue(note.getItemDamage());
						}
					}
					
					noteValues = MathHelper.floor_double(noteValues / 7F);
					
                    for (int i = 0; i < 3; i++)
                    {
                        int j = EnchantmentHelper.calcItemStackEnchantability(rand, i, noteValues, itemstack);
						enchantLevels[i] = j;
						if (j >= 30)
						{
							thePlayer.triggerAchievement(LKAchievementList.powerDrum);
						}
                    }

                    detectAndSendChanges();
                }
            }
            else
            {
				for (int i = 0; i < 3;i++)
				{
					enchantLevels[i] = 0;
				}
            }
        }
    }
	
	@Override
    public boolean enchantItem(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = theDrumEnchant.getStackInSlot(0);
        if (enchantLevels[i] > 0 && itemstack != null && (entityplayer.experienceLevel >= enchantLevels[i] || entityplayer.capabilities.isCreativeMode))
        {
            if (!worldObj.isRemote)
            {
                List list = EnchantmentHelper.buildEnchantmentList(rand, itemstack, enchantLevels[i]);
                if (list != null)
                {
                    entityplayer.addExperienceLevel(-enchantLevels[i]);
                    Iterator iterator = list.iterator();
                    while (iterator.hasNext())
                    {
                        EnchantmentData enchantment = (EnchantmentData)iterator.next();
                        itemstack.addEnchantment(enchantment.enchantmentobj, enchantment.enchantmentLevel);
                    }
                    onCraftMatrixChanged(theDrumEnchant);
					entityplayer.triggerAchievement(LKAchievementList.drumEnchant);
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return theDrum.isUseableByPlayer(entityplayer);
    }
	
	@Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        if (!worldObj.isRemote)
        {
            ItemStack itemstack = theDrumEnchant.getStackInSlotOnClosing(0);
            if (itemstack != null)
            {
                entityplayer.dropPlayerItem(itemstack);
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
            if (i < 9)
            {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }
            }
            else if (i >= 9 && i < 36)
            {
                if (!mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (i >= 36 && i < 45)
            {
                if (!mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 9, 45, false))
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