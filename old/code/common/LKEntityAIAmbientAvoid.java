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

import java.util.List;

public class LKEntityAIAmbientAvoid extends EntityAIBase
{
    private EntityAmbientCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    private Entity closestLivingEntity;
    private float distanceFromEntity;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Class targetEntityClass;

    public LKEntityAIAmbientAvoid(EntityAmbientCreature creature, Class cls, float f, double d, double d1)
    {
        theEntity = creature;
        targetEntityClass = cls;
        distanceFromEntity = f;
        farSpeed = d;
        nearSpeed = d1;
        entityPathNavigate = creature.getNavigator();
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
		List list = theEntity.worldObj.getEntitiesWithinAABB(targetEntityClass, theEntity.boundingBox.expand((double)distanceFromEntity, 3.0D, (double)distanceFromEntity));

		if (list.isEmpty())
		{
			return false;
		}

		closestLivingEntity = (Entity)list.get(0);

        if (!theEntity.getEntitySenses().canSee(closestLivingEntity))
        {
            return false;
        }
        else
        {
            Vec3 v = LKAmbientPositionGenerator.findRandomTargetBlockAwayFrom(theEntity, 16, 7, theEntity.worldObj.getWorldVec3Pool().getVecFromPool(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));

            if (v == null)
            {
                return false;
            }
            else if (closestLivingEntity.getDistanceSq(v.xCoord, v.yCoord, v.zCoord) < closestLivingEntity.getDistanceSqToEntity(theEntity))
            {
                return false;
            }
            else
            {
                entityPathEntity = entityPathNavigate.getPathToXYZ(v.xCoord, v.yCoord, v.zCoord);
                return entityPathEntity == null ? false : entityPathEntity.isDestinationSame(v);
            }
        }
    }

	@Override
    public boolean continueExecuting()
    {
        return !entityPathNavigate.noPath();
    }

    @Override
    public void startExecuting()
    {
        entityPathNavigate.setPath(entityPathEntity, farSpeed);
    }

    @Override
    public void resetTask()
    {
        closestLivingEntity = null;
    }

    @Override
    public void updateTask()
    {
        if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D)
        {
            theEntity.getNavigator().setSpeed(nearSpeed);
        }
        else
        {
            theEntity.getNavigator().setSpeed(farSpeed);
        }
    }
}
