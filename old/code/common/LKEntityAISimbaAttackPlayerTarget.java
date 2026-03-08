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

public class LKEntityAISimbaAttackPlayerTarget extends EntityAITarget
{
    private LKEntitySimba theSimba;
    private EntityLivingBase attackTarget;
	private int ownerTicksExisted;

    public LKEntityAISimbaAttackPlayerTarget(LKEntitySimba simba)
    {
        super(simba, false);
        theSimba = simba;
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
		if (theSimba.isSitting())
		{
			return false;
		}
		EntityPlayer entityplayer = theSimba.getOwner();
		if (entityplayer == null)
		{
			return false;
		}
		else
		{
			attackTarget = entityplayer.getLastAttacker();
			int i = entityplayer.getLastAttackerTime();
			return i != ownerTicksExisted && isSuitableTarget(attackTarget, false);
		}
    }

	
    public void startExecuting()
    {
        taskOwner.setAttackTarget(attackTarget);
        EntityPlayer entityplayer = theSimba.getOwner();
        if (entityplayer != null)
        {
            ownerTicksExisted = entityplayer.getLastAttackerTime();
        }
        super.startExecuting();
    }
	
	@Override
    public boolean continueExecuting()
    {
        return theSimba.isSitting() ? false : super.continueExecuting();
    }
}
