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
import java.util.List;
import java.util.Random;

public class LKEntityAIAngerableMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    public EntityAnimal targetMate;
    int breeding = 0;
    double moveSpeed;

    public LKEntityAIAngerableMate(EntityAnimal animal, double d)
    {
        theAnimal = animal;
        theWorld = animal.worldObj;
        moveSpeed = d;
        setMutexBits(3);
    }

	@Override
    public boolean shouldExecute()
    {
        if (!theAnimal.isInLove())
        {
            return false;
        }
        if (((LKAngerable)theAnimal).isHostile())
        {
            return false;
        }
        else
        {
            targetMate = findMate();
            return targetMate != null;
        }
    }

	@Override
    public boolean continueExecuting()
    {
        return !((LKAngerable)theAnimal).isHostile() && !((LKAngerable)targetMate).isHostile() && targetMate.isEntityAlive() && targetMate.isInLove() && breeding < 60;
    }

	@Override
    public void resetTask()
    {
        targetMate = null;
        breeding = 0;
    }

	@Override
    public void updateTask()
    {
        theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10.0F, (float)theAnimal.getVerticalFaceSpeed());
        theAnimal.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
        ++breeding;

        if (breeding == 60)
        {
            procreate();
        }
    }

    private EntityAnimal findMate()
    {
        float f = 8.0F;
		Class mateClass = theAnimal.getClass();
		if (theAnimal instanceof LKEntityLion)
		{
			mateClass = LKEntityLioness.class;

		}
		else if (theAnimal instanceof LKEntityLioness)
		{
			mateClass = LKEntityLion.class;
		}
		
		List list = theWorld.getEntitiesWithinAABB(mateClass, theAnimal.boundingBox.expand((double)f, (double)f, (double)f));
		Iterator i = list.iterator();
		EntityAnimal mate;
		do
		{
			if (!i.hasNext())
			{
				return null;
			}

			mate = (EntityAnimal)i.next();
		}
		while (!theAnimal.canMateWith(mate));
		
		return mate;
    }

    private void procreate()
    {
        EntityAgeable babyAnimal = theAnimal.createChild(targetMate);
        if (babyAnimal != null)
        {
            theAnimal.setGrowingAge(6000);
            targetMate.setGrowingAge(6000);
            theAnimal.resetInLove();
            targetMate.resetInLove();
            babyAnimal.setGrowingAge(-24000);
            babyAnimal.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
            theWorld.spawnEntityInWorld(babyAnimal);
            Random rand = theAnimal.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double var4 = rand.nextGaussian() * 0.02D;
                double var6 = rand.nextGaussian() * 0.02D;
                double var8 = rand.nextGaussian() * 0.02D;
                theWorld.spawnParticle("heart", theAnimal.posX + (double)(rand.nextFloat() * theAnimal.width * 2.0F) - (double)theAnimal.width, theAnimal.posY + 0.5D + (double)(rand.nextFloat() * theAnimal.height), theAnimal.posZ + (double)(rand.nextFloat() * theAnimal.width * 2.0F) - (double)theAnimal.width, var4, var6, var8);
            }
			
			theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theAnimal.posX, theAnimal.posY, theAnimal.posZ, rand.nextInt(4) + 1));
        }
    }
}
