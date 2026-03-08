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

public class LKEntityAISimbaAttack extends EntityAIAttackOnCollide
{
	private int privateSuperField;
	private LKEntitySimba theSimba;
	private double speed;
	private boolean aBoolean;
	private int entityAttackTick;
	
	public LKEntityAISimbaAttack(LKEntitySimba simba, double d, boolean flag)
	{
		super(simba, d, flag);
		theSimba = simba;
		speed = d;
		aBoolean = flag;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (!theSimba.isSitting())
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
    public boolean continueExecuting()
    {
        return theSimba.isSitting() ? false : super.continueExecuting();
    }
	
	@Override
    public void updateTask()
    {
		EntityLivingBase target = theSimba.getAttackTarget();
        theSimba.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);

        if ((aBoolean || theSimba.getEntitySenses().canSee(target)) && --privateSuperField <= 0)
        {
            privateSuperField = 4 + theSimba.getRNG().nextInt(7);
            theSimba.getNavigator().tryMoveToEntityLiving(target, speed);
        }

        entityAttackTick = Math.max(entityAttackTick - 1, 0);
        double var1 = (double)(theSimba.width * 1.6F * theSimba.width * 1.6F);

        if (theSimba.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) <= var1)
        {
            if (entityAttackTick <= 0)
            {
                entityAttackTick = 20;
                theSimba.attackEntityAsMob(target);
            }
        }
    }
}
