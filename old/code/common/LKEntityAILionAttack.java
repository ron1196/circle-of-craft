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

public class LKEntityAILionAttack extends EntityAIAttackOnCollide
{
	private int privateSuperField;
	private EntityCreature theEntity;
	private double speed;
	private boolean aBoolean;
	private int entityAttackTick;
	
	public LKEntityAILionAttack(EntityCreature entity, Class entityclass, double d, boolean flag)
	{
		super(entity, entityclass, d, flag);
		theEntity = entity;
		speed = d;
		aBoolean = flag;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (((LKAngerable)theEntity).isHostile())
		{
			return super.shouldExecute();
		}
		return false;
	}
	
	@Override
    public void startExecuting()
	{
		super.startExecuting();
		privateSuperField = 0;
	}
	
	@Override
    public void updateTask()
    {
		EntityLivingBase target = theEntity.getAttackTarget();
        theEntity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);

        if ((aBoolean || theEntity.getEntitySenses().canSee(target)) && --privateSuperField <= 0)
        {
            privateSuperField = 4 + theEntity.getRNG().nextInt(7);
            theEntity.getNavigator().tryMoveToEntityLiving(target, speed);
        }

        entityAttackTick = Math.max(entityAttackTick - 1, 0);
        double d = (double)(theEntity.width * 1.6F * theEntity.width * 1.6F);

        if (theEntity.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) <= d)
        {
            if (entityAttackTick <= 0)
            {
                entityAttackTick = 20;
                theEntity.attackEntityAsMob(target);
            }
        }
    }
}
