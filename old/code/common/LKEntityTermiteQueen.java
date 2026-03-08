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

public class LKEntityTermiteQueen extends EntityCreature implements LKCharacter, IMob
{
	private boolean exploded;
	
	public LKEntityTermiteQueen(World world)
	{
		super(world);
		isImmuneToFire = true;
		rescale();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAITermiteQueenAttack(this, EntityPlayer.class, 1.4D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(25D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(32D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Float.valueOf(25F));
	}
	
	private void rescale()
	{
		setSize(1.5F * getScale(), 1.2F * getScale());
		setPosition(posX, posY, posZ);
	}
	
	private void setScale(float f)
	{
		dataWatcher.updateObject(20, Float.valueOf(f));
	}
	
	public float getScale()
	{
		return 3.0F - ((25F - dataWatcher.getWatchableObjectFloat(20)) * 0.07F);
	}
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 0;
    }
	
	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);
        return entityplayer != null && canEntityBeSeen(entityplayer) ? entityplayer : null;
    }
	
	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
		if (damagesource == DamageSource.outOfWorld)
		{
			return super.attackEntityFrom(damagesource, f);
		}
		
        Entity entity = damagesource.getEntity();
        if (!(entity instanceof EntityPlayer))
        {
			return false;
		}
		float currentHealth = getHealth();
		boolean flag = super.attackEntityFrom(damagesource, 1F);
		if (getHealth() > 0)
		{
			if (!worldObj.isRemote)
			{
				setScale(getHealth());
			}
			rescale();
			if (getHealth() < currentHealth)
			{
				Entity termite = new LKEntityTermite(worldObj);
				termite.setLocationAndAngles(posX, posY, posZ, rand.nextFloat() * 360F, 0F);
				if (!worldObj.isRemote)
				{
					worldObj.spawnEntityInWorld(termite);
				}
			}
		}
		
		return flag;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (riddenByEntity != null && riddenByEntity instanceof LKEntityZira && !worldObj.isRemote)
		{
			riddenByEntity.rotationYaw = rotationYaw;
			riddenByEntity.rotationPitch = rotationPitch;
		}
		
		if (getHealth() == 1)
		{
			if (riddenByEntity != null && riddenByEntity instanceof LKEntityZira)
			{
				LKLevelData.setZiraStage(27);
				((LKEntityZira)riddenByEntity).attackEntityFrom(DamageSource.outOfWorld, 100);
				if (!worldObj.isRemote)
				{
					riddenByEntity.mountEntity(null);
				}
			}
			
			if (!worldObj.isRemote && !exploded)
			{
				worldObj.createExplosion(this, posX, posY + 3D, posZ, 3F, worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
				exploded = true;
			}
			
			attackEntityFrom(DamageSource.outOfWorld, 1);
		}
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        int attack = 3;

        if (isPotionActive(Potion.damageBoost))
        {
            attack += 3 << getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }

        if (isPotionActive(Potion.weakness))
        {
            attack -= 2 << getActivePotionEffect(Potion.weakness).getAmplifier();
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("HasExploded", exploded);
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		exploded = nbt.getBoolean("HasExploded");
		setScale(getHealth());
		rescale();
    }
	
	@Override
    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
