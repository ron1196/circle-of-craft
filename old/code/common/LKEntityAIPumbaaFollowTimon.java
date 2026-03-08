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

public class LKEntityAIPumbaaFollowTimon extends EntityAIBase
{
    private LKEntityPumbaa thePumbaa;
    private LKEntityTimon theTimon;
    private double speed;
    private int moveTick;
	private World theWorld;

    public LKEntityAIPumbaaFollowTimon(LKEntityPumbaa pumbaa, double d)
    {
        thePumbaa = pumbaa;
        speed = d;
		theWorld = pumbaa.worldObj;
    }

	@Override
    public boolean shouldExecute()
    {
		List nearbyTimons = thePumbaa.worldObj.getEntitiesWithinAABB(LKEntityTimon.class, thePumbaa.boundingBox.expand(32.0D, 32.0D, 32.0D));
		LKEntityTimon timon = null;
		double d = Double.MAX_VALUE;
		Iterator i = nearbyTimons.iterator();

		while (i.hasNext())
		{
			LKEntityTimon timon1 = (LKEntityTimon)i.next();
			double d1 = thePumbaa.getDistanceSqToEntity(timon1);

			if (d1 <= d)
			{
				d = d1;
				timon = timon1;
			}
		}

		if (timon == null)
		{
			return false;
		}
		else if (d < 12.0D)
		{
			return false;
		}
		else
		{
			theTimon = timon;
			return true;
		}
    }

	@Override
    public boolean continueExecuting()
    {
        if (!theTimon.isEntityAlive())
        {
            return false;
        }
        else
        {
            double d = thePumbaa.getDistanceSqToEntity(theTimon);
            return d >= 12.0D && d <= 256.0D;
        }
    }

	@Override
    public void startExecuting()
    {
        moveTick = 0;
    }

	@Override
    public void resetTask()
    {
        theTimon = null;
    }

	@Override
    public void updateTask()
    {
        if (--moveTick <= 0)
        {
            moveTick = 10;
            if (!thePumbaa.getNavigator().tryMoveToEntityLiving(theTimon, speed))
			{
				if (thePumbaa.getDistanceSqToEntity(theTimon) >= 144.0D)
				{
					int i = MathHelper.floor_double(theTimon.posX);
					int j = MathHelper.floor_double(theTimon.boundingBox.minY);
					int k = MathHelper.floor_double(theTimon.posZ);
					
					if (theWorld.isBlockNormalCube(i, j - 1, k))
					{
						boolean canMoveHere = true;
						
						for (int i1 = -1; i1 <= 1; i1++)
						{
							for (int j1 = 0; j1 <= 1; j1++)
							{
								for (int k1 = -1; k1 <= 1; k1++)
								{
									Block block = Block.blocksList[theWorld.getBlockId(i + i1, j + j1, k + k1)];
									if (block != null && (block.getCollisionBoundingBoxFromPool(theWorld, i + i1, j + j1, k + k1) != null || block.blockMaterial == Material.lava))
									{
										canMoveHere = false;
									}
								}
							}
						}
						
						if (canMoveHere)
						{
							thePumbaa.fart();
							thePumbaa.setLocationAndAngles((float)i + 0.5F, j, (float)k + 0.5F, thePumbaa.rotationYaw, thePumbaa.rotationPitch);
						}
					}
				}
			}
        }
    }
}
