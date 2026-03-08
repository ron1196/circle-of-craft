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

public class LKEntityOutlander extends EntityMob implements LKAngerable
{
    protected int angerLevel;
    public int randomSoundDelay;
	public boolean inMound;

    public LKEntityOutlander(World world)
    {
        super(world);
        angerLevel = 0;
        randomSoundDelay = 0;
        setSize(1.3F, 1.6F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, 1.3D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(4D);
    }
	
	public boolean isHostile()
	{
		if (inMound)
		{
			return LKLevelData.outlandersHostile == 1 || angerLevel > 0;
		}
		else
		{
			return angerLevel > 0;
		}
	}
	
	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return inMound ? 0 : super.getExperiencePoints(entityplayer);
    }

	@Override
    public void onLivingUpdate()
    {
		if (!worldObj.isRemote && LKLevelData.ziraStage == 19 && inMound)
		{
			setDead();
		}
		
        if (randomSoundDelay > 0 && --randomSoundDelay == 0)
        {
            worldObj.playSoundAtEntity(this, "lionking:lionangry", getSoundVolume() * 2.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
		if (!inMound && getRNG().nextInt(4000) == 0)
		{	
			becomeAngry();
		}
        super.onLivingUpdate();
    }

	@Override
    public boolean getCanSpawnHere()
    {
        return worldObj.difficultySetting > 0 && worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setShort("Anger", (short)angerLevel);
		nbt.setBoolean("InMound", inMound);
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        angerLevel = nbt.getShort("Anger");
		inMound = nbt.getBoolean("InMound");
    }

	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        if (damagesource.getEntity() instanceof EntityPlayer)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(24D, 24D, 24D));
            for (int j = 0; j < list.size(); j++)
            {
                Entity entity = (Entity)list.get(j);
                if (!inMound && entity instanceof LKEntityOutlander)
                {
                    LKEntityOutlander outlander = (LKEntityOutlander)entity;
					if (!outlander.inMound)
					{
						outlander.becomeAngry();
					}
                }
            }

            becomeAngry();
        }
        return super.attackEntityFrom(damagesource, f);
    }
	
	@Override
    protected void attackEntity(Entity entity, float f)
    {
        if (attackTime <= 0 && f < 2.4F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            attackEntityAsMob(entity);
        }
    }

    private void becomeAngry()
    {
        angerLevel = 400 + getRNG().nextInt(400);
        randomSoundDelay = getRNG().nextInt(40);
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
		if (inMound)
		{
			return;
		}
		
        int j = getRNG().nextInt(2) + 1 + getRNG().nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
			if (getRNG().nextBoolean())
			{
				dropItem(mod_LionKing.outlanderFur.itemID, 1);
			}
        }
        j = getRNG().nextInt(2) + 1 + getRNG().nextInt(1 + i);
        for (int l = 0; l < j; l++)
        {
			if (getRNG().nextInt(5) < 2)
			{
				if (isBurning())
				{
					dropItem(mod_LionKing.lionCooked.itemID, 1);
				}
				else
				{
					dropItem(mod_LionKing.outlanderMeat.itemID, 1);
				}
			}
        }
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		if (inMound)
		{
			if (LKLevelData.ziraStage < 14)
			{
				if (worldObj.getBlockId(i, j, k) == mod_LionKing.outlandsPool.blockID)
				{
					return -999999F;
				}
				else if (worldObj.getBlockId(i, j - 1, k) == mod_LionKing.outlandsPortalFrame.blockID && worldObj.getBlockMetadata(i, j - 1, k) == 1 && worldObj.isAirBlock(i, j, k))
				{
					return 10.0F;
				}
				else
				{
					return -999999F;
				}
			}
			if (LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19)
			{
				return worldObj.getBlockId(i, j - 1, k) == mod_LionKing.rafikiWood.blockID && worldObj.isAirBlock(i, j, k) ? 10.0F : -999999F;
			}
		}
		return super.getBlockPathWeight(i, j, k);
    }
	
	@Override
    protected boolean canDespawn()
    {
        return !inMound;
    }
	
	@Override
	protected String getLivingSound()
    {
        return "lionking:lion";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:lionroar";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:lionangry";
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
