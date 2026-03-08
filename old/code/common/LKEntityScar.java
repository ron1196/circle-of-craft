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

public class LKEntityScar extends EntityCreature implements LKCharacter, LKAngerable, IMob
{
    public LKEntityScar(World world)
    {
       	super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, 1.4D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
        setSize(1.3F, 1.6F);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(250D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }
	
	public boolean isHostile()
	{
		return isScarHostile();
	}
	
	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 0;
    }

	@Override
    protected void entityInit()
    {
        super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
        dataWatcher.addObject(19, Byte.valueOf((byte)0));
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        if (dataWatcher.getWatchableObjectByte(16) == 1)
        {
            nbttagcompound.setBoolean("IsHostile", true);
        }
        if (dataWatcher.getWatchableObjectByte(17) == 1)
        {
            nbttagcompound.setBoolean("HasFirstSpoken", true);
        }
        if (dataWatcher.getWatchableObjectByte(18) == 1)
        {
			nbttagcompound.setBoolean("HasSpawnedHyenas", true);
        }
        if (dataWatcher.getWatchableObjectByte(19) == 1)
        {
            nbttagcompound.setBoolean("HasMadeExplosions", true);
        }
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
       	super.readEntityFromNBT(nbttagcompound);
        dataWatcher.updateObject(16, Byte.valueOf((byte)(nbttagcompound.getBoolean("IsHostile") ? 1 : 0)));
        dataWatcher.updateObject(17, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasFirstSpoken") ? 1 : 0)));
        dataWatcher.updateObject(18, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpawnedHyenas") ? 1 : 0)));
        dataWatcher.updateObject(19, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasMadeExplosions") ? 1 : 0)));
    }
	
	public boolean isScarHostile()
	{
		return dataWatcher.getWatchableObjectByte(16) == 1;
	}
	
	public void setScarHostile(boolean flag)
	{
		dataWatcher.updateObject(16, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}

	@Override
    protected Entity findPlayerToAttack()
    {
        if (!isScarHostile())
        {
            return null;
        }
		else
        {
        	EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);
       		if (entityplayer != null && canEntityBeSeen(entityplayer))
        	{
            	return entityplayer;
        	}
			else
        	{
            	return null;
        	}
        }
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

	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6F);
    }

    public boolean getHasFirstSpoken()
    {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public boolean getHasSpawnedHyenas()
    {
        return dataWatcher.getWatchableObjectByte(18) == 1;
    }

    public boolean getHasMadeExplosions()
    {
        return dataWatcher.getWatchableObjectByte(19) == 1;
    }

	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        Entity entity = damagesource.getEntity();
        if (entity instanceof EntityPlayer)
        {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (itemstack == null || itemstack.itemID != mod_LionKing.rafikiStick.itemID)
			{
        		f = 0F;
			}
        }
		else
		{
			f = 0F;
		}
		if (!getHasFirstSpoken() && entity instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entity;
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Scar> \u00a7fYou think you can fight me and live?");
			}
        	dataWatcher.updateObject(17, Byte.valueOf((byte)1));
			becomeAngryAt(entityplayer);
		}
		return super.attackEntityFrom(damagesource, f);
    }

    private void becomeAngryAt(Entity entity)
    {
        entityToAttack = entity;
        setScarHostile(true);
		worldObj.playSoundAtEntity(this, "lionking:lionangry", getSoundVolume() * 2.0F, ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F) * 1.8F);
    }

	@Override
    public void onLivingUpdate()
    {
		super.onLivingUpdate();
		
		if (!getHasFirstSpoken())
		{
			EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 8D);
			if (entityplayer != null)
			{
				Vec3 vec1 = entityplayer.getLook(1.0F).normalize();
				Vec3 vec2 = worldObj.getWorldVec3Pool().getVecFromPool(posX - entityplayer.posX, boundingBox.minY + (double)(height / 2.0F) - (entityplayer.posY + (double)entityplayer.getEyeHeight()), posZ - entityplayer.posZ);
				double d = vec2.lengthVector();
				vec2 = vec2.normalize();
				double d1 = vec1.dotProduct(vec2);
				if (d1 > 1.0D - 0.025D / d && entityplayer.canEntityBeSeen(this))
				{
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Scar> \u00a7fYou think you can fight me and live?");
					}
					dataWatcher.updateObject(17, Byte.valueOf((byte)1));
					becomeAngryAt(entityplayer);
				}
			}
		}

		if (getHasFirstSpoken())
		{
			if (getRNG().nextInt(3) != 0)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = getRNG().nextGaussian() * 0.02D;
				double d2 = getRNG().nextGaussian() * 0.02D;
				worldObj.spawnParticle((getRNG().nextInt(3) == 0 ? "smoke" : "flame"), (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.0D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}
			
			EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 128D);
			if (entityplayer == null)
			{
				return;
			}
			
			if (getHealth() <= 175F && !getHasSpawnedHyenas())
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(posY);
				int k = MathHelper.floor_double(posZ);
				
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Scar> \u00a7fMinions! Tear this weakling's flesh apart!");

					for (int i1 = 0; i1 < 3; i1++)
					{
						LKEntityHyena hyena = new LKEntityHyena(worldObj);
						hyena.setLocationAndAngles(i, j + 1, k, rotationYaw, rotationPitch);
						worldObj.spawnEntityInWorld(hyena);
					}
				}
				
        		dataWatcher.updateObject(18, Byte.valueOf((byte)1));
			}
			
			if (!worldObj.isRemote && getHasSpawnedHyenas() && getRNG().nextInt(200) == 0)
			{
				List list = worldObj.getEntitiesWithinAABB(LKEntityHyena.class, boundingBox.addCoord(16F, 16F, 16F).addCoord(-16F, -16F, -16F));
				if (list.size() < 6)
				{
					int i = MathHelper.floor_double(posX);
					int j = MathHelper.floor_double(posY);
					int k = MathHelper.floor_double(posZ);
					
					LKEntityHyena hyena = new LKEntityHyena(worldObj);
					hyena.setLocationAndAngles(i, j + 1, k, rotationYaw, rotationPitch);
					worldObj.spawnEntityInWorld(hyena);
				}
			}

			if (getHealth() <= 70F && !getHasMadeExplosions())
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(posY);
				int k = MathHelper.floor_double(posZ);
				
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Scar> \u00a7fI will NOT be defeated!");
				}

				for (int i1 = -8; i1 < 9; i1++)
				{
					for (int j1 = -8; j1 < 9; j1++)
					{
						for (int k1 = -8; k1 < 8; k1++)
						{
							if (worldObj.isBlockOpaqueCube(i+i1, j+j1, k+k1) && worldObj.isAirBlock(i+i1, j+j1+1, k+k1) && getRNG().nextInt(5) == 0)
							{
								worldObj.setBlock(i+i1, j+j1+1, k+k1, Block.fire.blockID, 0, 3);
							}
	
							if (worldObj.isAirBlock(i+i1, j+j1, k+k1) && getRNG().nextInt(40) == 0)	
							{
								worldObj.createExplosion(this, (double)i+i1, (double)j+j1, (double)k+k1, 0F, false);
							}
						}
					}
				}
        		dataWatcher.updateObject(19, Byte.valueOf((byte)1));
			}
		}
    }

	@Override
    protected boolean canDespawn()
    {	
       	return false;
    }

	@Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if (damagesource.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			entityplayer.triggerAchievement(LKAchievementList.killScar);

			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);

			for (int i1 = -5; i1 < 6; i1++)
			{
				for (int j1 = -5; j1 < 6; j1++)
				{
					for (int k1 = -5; k1 < 6; k1++)
					{
						int i2 = worldObj.getBlockId(i+i1, j+j1, k+k1);
						if (i2 == Block.fire.blockID || i2 == Block.lavaMoving.blockID || i2 == Block.lavaStill.blockID)
						{
							worldObj.setBlockToAir(i+i1, j+j1, k+k1);
						}
					}
				}
			}
			
			List list = worldObj.getEntitiesWithinAABB(LKEntityHyena.class, boundingBox.addCoord(18F, 18F, 18F).addCoord(-18F, -18F, -18F));
			{
				for (int l = 0; l < list.size(); l++)
				{
					if (!worldObj.isRemote)
					{
						((LKEntityHyena)list.get(l)).setDead();
					}
				}
			}
			
			for (int i1 = -3; i1 < 4; i1++)
			{
				for (int k1 = -3; k1 < 4; k1++)
				{
					worldObj.setBlock(i + i1, j - 1, k + k1, mod_LionKing.prideBrick.blockID, 0, 3);
				}
			}
			
			if (!worldObj.isRemote)
			{
				worldObj.createExplosion(this, (double)i, (double)j, (double)k, 0F, false);

				for (int i1 = 0; i1 < getRNG().nextInt(7) + 7; i1++)
				{
					dropItem(mod_LionKing.hyenaBone.itemID, 1);
				}
				for (int i1 = 0; i1 < getRNG().nextInt(7) + 7; i1++)
				{
					dropItem(mod_LionKing.lionCooked.itemID, 1);
				}
				
				for (int i1 = 0; i1 < 20; i1++)
				{
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, getRNG().nextInt(30) + 36));
				}
				
				dropItem(mod_LionKing.scarRug.itemID, 1);
			}
			
			if (LKQuestBase.rafikiQuest.getQuestStage() == 2)
			{
				LKLevelData.setDefeatedScar(true);
			}
        }
    }
	
	@Override
    protected void kill()
    {
        setDead();
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
