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

public class LKEntityAISimbaFollow extends EntityAIBase
{
    private LKEntitySimba theSimba;
    private EntityPlayer thePlayer;
    private World theWorld;
    private double speed;
    private PathNavigate pathfinder;
    private int moveTick;
    private float maxDist;
    private float minDist;
    private boolean avoidWater;

    public LKEntityAISimbaFollow(LKEntitySimba simba, double d, float f1, float f2)
    {
        theSimba = simba;
        theWorld = simba.worldObj;
        speed = d;
        pathfinder = simba.getNavigator();
        minDist = f1;
        maxDist = f2;
        setMutexBits(3);
    }

	@Override
    public boolean shouldExecute()
    {
        EntityPlayer entityplayer = theSimba.getOwner();

        if (entityplayer == null)
        {
            return false;
        }
        else if (theSimba.isSitting())
        {
            return false;
        }
        else if (theSimba.getDistanceSqToEntity(entityplayer) < (double)(minDist * minDist))
        {
            return false;
        }
        else
        {
            thePlayer = entityplayer;
            return true;
        }
    }

	@Override
    public boolean continueExecuting()
    {
        return !pathfinder.noPath() && theSimba.getDistanceSqToEntity(thePlayer) > (double)(maxDist * maxDist) && !theSimba.isSitting();
    }

	@Override
    public void startExecuting()
    {
        moveTick = 0;
        avoidWater = theSimba.getNavigator().getAvoidsWater();
        theSimba.getNavigator().setAvoidsWater(false);
    }

	@Override
    public void resetTask()
    {
        thePlayer = null;
        pathfinder.clearPathEntity();
        theSimba.getNavigator().setAvoidsWater(avoidWater);
    }

	@Override
    public void updateTask()
    {
        theSimba.getLookHelper().setLookPositionWithEntity(thePlayer, 10.0F, (float)theSimba.getVerticalFaceSpeed());

        if (!theSimba.isSitting())
        {
            if (--moveTick <= 0)
            {
                moveTick = 10;

                if (!pathfinder.tryMoveToEntityLiving(thePlayer, speed))
                {
                    if (theSimba.getDistanceSqToEntity(thePlayer) >= 144.0D)
                    {
						int i = MathHelper.floor_double(thePlayer.posX);
						int j = MathHelper.floor_double(thePlayer.boundingBox.minY);
						int k = MathHelper.floor_double(thePlayer.posZ);
						
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
										if (block != null && (block.getCollisionBoundingBoxFromPool(theWorld, i + i1, j + j1, k + k1) != null || block.blockMaterial == Material.lava || block == mod_LionKing.outlandsPool))
										{
											canMoveHere = false;
										}
									}
								}
							}
							
							if (canMoveHere)
							{
								theSimba.fallDistance = 0.0F;
								theSimba.setLocationAndAngles((float)i + 0.5F, j, (float)k + 0.5F, theSimba.rotationYaw, theSimba.rotationPitch);
							}
						}
                    }
                }
            }
        }
    }
}
