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

import java.util.ArrayList;
import java.util.Iterator;

public class LKItemGroundRhinoHorn extends LKItem
{
    public LKItemGroundRhinoHorn(int i)
    {
        super(i);
		setCreativeTab(LKCreativeTabs.tabMaterials);
    }
	
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityplayer, EntityLivingBase entityliving)
    {
		if (entityliving.getHealth() <= 0)
		{
			return false;
		}
		try
		{
			if (entityliving instanceof LKAngerable && entityliving instanceof EntityAnimal)
			{
				EntityAnimal animal = (LKEntityLionBase)entityliving;
				ArrayList tasks = animal.tasks.taskEntries;
				LKEntityAIAngerableMate matingAI = null;
				for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
				{
					Object obj = iterator.next();
					if (obj instanceof EntityAITaskEntry)
					{
						EntityAIBase AI = ((EntityAITaskEntry)obj).action;
						if (AI instanceof LKEntityAIAngerableMate)
						{
							matingAI = (LKEntityAIAngerableMate)AI;
						}
					}
				}
				if (matingAI != null && matingAI.shouldExecute() && matingAI.continueExecuting())
				{
					EntityAnimal targetMate = matingAI.targetMate;
					if (targetMate != null)
					{
						if (itemRand.nextInt(3) == 0)
						{
							for (int i = 0; i < 7; i++)
							{
								double d = itemRand.nextGaussian() * 0.02D;
								double d1 = itemRand.nextGaussian() * 0.02D;
								double d2 = itemRand.nextGaussian() * 0.02D;
								entityliving.worldObj.spawnParticle("smoke", entityliving.posX + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, entityliving.posY + 0.5D + (double)(itemRand.nextFloat() * entityliving.height), entityliving.posZ + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, d, d1, d2);
								itemstack.stackSize--;
								return false;
							}
						}						
						procreate(animal);
						itemstack.stackSize--;
						entityplayer.triggerAchievement(LKAchievementList.rhinoHorn);
						return true;
					}
				}
			}
			else if (entityliving instanceof EntityAnimal)
			{
				EntityAnimal animal = (EntityAnimal)entityliving;
				if (!animal.tasks.taskEntries.isEmpty())
				{
					ArrayList tasks = animal.tasks.taskEntries;
					EntityAIMate matingAI = null;
					for (Iterator iterator = tasks.iterator(); iterator.hasNext();) 
					{
						Object obj = iterator.next();
						if (obj instanceof EntityAITaskEntry)
						{
							EntityAIBase AI = ((EntityAITaskEntry)obj).action;
							if (AI instanceof EntityAIMate)
							{
								matingAI = (EntityAIMate)AI;
							}
						}
					}
					if (matingAI != null && matingAI.shouldExecute() && matingAI.continueExecuting())
					{
						EntityAnimal targetMate = matingAI.targetMate;
						if (targetMate != null)
						{
							if (itemRand.nextInt(3) == 0)
							{
								for (int i = 0; i < 7; i++)
								{
									double d = itemRand.nextGaussian() * 0.02D;
									double d1 = itemRand.nextGaussian() * 0.02D;
									double d2 = itemRand.nextGaussian() * 0.02D;
									entityliving.worldObj.spawnParticle("smoke", entityliving.posX + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, entityliving.posY + 0.5D + (double)(itemRand.nextFloat() * entityliving.height), entityliving.posZ + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, d, d1, d2);
									itemstack.stackSize--;
									return false;
								}
							}
							procreate(animal);
							itemstack.stackSize--;
							entityplayer.triggerAchievement(LKAchievementList.rhinoHorn);
							return true;
						}
					}
				}
				else if (animal.breeding > 0)
				{
					if (itemRand.nextInt(3) == 0)
					{
						for (int i = 0; i < 7; i++)
						{
							double d = itemRand.nextGaussian() * 0.02D;
							double d1 = itemRand.nextGaussian() * 0.02D;
							double d2 = itemRand.nextGaussian() * 0.02D;
							entityliving.worldObj.spawnParticle("smoke", entityliving.posX + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, entityliving.posY + 0.5D + (double)(itemRand.nextFloat() * entityliving.height), entityliving.posZ + (double)(itemRand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width, d, d1, d2);
							itemstack.stackSize--;
							return false;
						}
					}
					procreate(animal);
					itemstack.stackSize--;
					entityplayer.triggerAchievement(LKAchievementList.rhinoHorn);
					return true;
				}
			}
		}
		catch (SecurityException exception)
		{
			exception.printStackTrace();
		}
		catch (IllegalArgumentException exception)
		{
			exception.printStackTrace();
		}
		return false;
    }
	
    private void procreate(EntityAnimal theAnimal)
    {
		World theWorld = theAnimal.worldObj;
        EntityAgeable babyAnimal = theAnimal.createChild(theAnimal);
        if (babyAnimal != null)
        {
            babyAnimal.setGrowingAge(-24000);
            babyAnimal.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
			if (!theWorld.isRemote)
			{
				theWorld.spawnEntityInWorld(babyAnimal);
			}
            for (int i = 0; i < 7; i++)
            {
                double d = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                theWorld.spawnParticle("heart", theAnimal.posX + (double)(itemRand.nextFloat() * theAnimal.width * 2.0F) - (double)theAnimal.width, theAnimal.posY + 0.5D + (double)(itemRand.nextFloat() * theAnimal.height), theAnimal.posZ + (double)(itemRand.nextFloat() * theAnimal.width * 2.0F) - (double)theAnimal.width, d, d1, d2);
            }
        }
    }
}
