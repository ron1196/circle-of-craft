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

public class LKEntitySkeletalHyenaHead extends EntityLiving implements IMob
{
    private float field_70813_a;
    private float field_70811_b;
    private float field_70812_c;
    private int headJumpDelay;
	
	public LKEntitySkeletalHyenaHead(World world)
	{
		super(world);
		setSize(0.6F, 0.6F);
		experienceValue = 2;
	}
		
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(15D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.2D);
    }
	
	@Override
    public void onUpdate()
    {
        if (!worldObj.isRemote && worldObj.difficultySetting == 0)
        {
            setDead();
        }

        field_70811_b += (field_70813_a - field_70811_b) * 0.5F;
        field_70812_c = field_70811_b;
        boolean flag = onGround;
        super.onUpdate();

        if (onGround && !flag)
        {   
			worldObj.playSoundAtEntity(this, getHurtSound(), 0.4F, ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1F) / 0.8F);

            field_70813_a = -0.5F;
        }
        else if (!onGround && flag)
        {
            field_70813_a = 1F;
        }

        field_70813_a *= 0.6F;
    }
	
	@Override
    protected void updateEntityActionState()
    {
        despawnEntity();
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);

        if (entityplayer != null)
        {
            faceEntity(entityplayer, 10F, 20F);
        }

        if (onGround && headJumpDelay-- <= 0)
        {
            headJumpDelay = getRNG().nextInt(20) + 10;

            if (entityplayer != null)
            {
                headJumpDelay /= 3;
            }

            isJumping = true;

            worldObj.playSoundAtEntity(this, getHurtSound(), 0.4F, ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1F) * 0.8F);

            moveStrafing = 1F - getRNG().nextFloat() * 2F;
            moveForward = 1F;
        }
        else
        {
            isJumping = false;

            if (onGround)
            {
                moveStrafing = moveForward = 0F;
            }
        }
    }
	
	@Override
    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
		if (canEntityBeSeen(entityplayer) && getDistanceSqToEntity(entityplayer) < 1D && entityplayer.attackEntityFrom(DamageSource.causeMobDamage(this), 3F))
		{
			worldObj.playSoundAtEntity(this, "mob.attack", 1F, (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1F);
		}
    }
	
	@Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer && getRNG().nextInt(40) == 0)
        {
            entityDropItem(new ItemStack(mod_LionKing.hyenaHeadItem.itemID, 1, 3), 0F);
		}
	}
	
	@Override
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
