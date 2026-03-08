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

import net.minecraft.server.MinecraftServer;

public class LKEntitySimba extends EntityCreature implements LKCharacter, IAnimals
{
	public LKInventorySimba inventory;
	private int eatingTick;

    public LKEntitySimba(World world)
    {
        super(world);
        setSize(1.3F, 1.6F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new LKEntityAISimbaAttack(this, 1.3D, true));
        tasks.addTask(3, new LKEntityAISimbaFollow(this, 1.3D, 10.0F, 2.0F));
        tasks.addTask(4, new LKEntityAISimbaWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LKEntityAISimbaAttackPlayerAttacker(this));
        targetTasks.addTask(2, new LKEntityAISimbaAttackPlayerTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		inventory = new LKInventorySimba(this);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(isChild() ? 15D : 30D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }

	@Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(12, new Integer(0));
		dataWatcher.addObject(13, "");
        dataWatcher.addObject(14, new Integer(0));
        dataWatcher.addObject(15, new Integer(0));
		dataWatcher.addObject(17, new Byte((byte)0));
		dataWatcher.addObject(18, new Byte((byte)0));
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	public boolean hasCharm()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setHasCharm(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	public boolean isSitting()
	{
		return dataWatcher.getWatchableObjectByte(18) == (byte)1;
	}
	
	public void setSitting(boolean flag)
	{
		dataWatcher.updateObject(18, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	public EntityPlayer getOwner()
	{
		return worldObj.getPlayerEntityByName(getOwnerName());
	}
	
	public String getOwnerName()
	{
		return dataWatcher.getWatchableObjectString(13);
	}
	
	public void setOwnerName(String s)
	{
		dataWatcher.updateObject(13, s);
	}
    
	@Override
    public void onDeath(DamageSource damagesource)
    {
		if (!worldObj.isRemote)
		{
			inventory.dropAllItems();
		}
		if (hasCharm() && !worldObj.isRemote)
		{
			entityDropItem(new ItemStack(mod_LionKing.charm, 1, 1), 0.0F);
		}
		if (!worldObj.isRemote)
		{
			for (int i = 0; i < 32; i++)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = (getRNG().nextGaussian() * 0.02D) + (double)(getRNG().nextFloat() * 0.25F);
				double d2 = getRNG().nextGaussian() * 0.02D;
				LKIngame.spawnCustomFX(worldObj, 64 + getRNG().nextInt(4), 16, true, (posX + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), posY + 0.25F + (double)(getRNG().nextFloat() * height), (posZ + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), d, d1, d2);
			}
		}
        super.onDeath(damagesource);
    }
	
	public void applyTeleportationEffects(Entity entity)
	{
		for (int i = 0; i < 12; i++)
		{
			double d = getRNG().nextGaussian() * 0.02D;
			double d1 = getRNG().nextGaussian() * 0.02D;
			double d2 = getRNG().nextGaussian() * 0.02D;
			worldObj.spawnParticle("portal", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
		}
		
		if (entity instanceof EntityPlayer)
		{
			((EntityPlayer)entity).triggerAchievement(LKAchievementList.teleportSimba);
		}
	}
	
	public boolean canUsePortal(EntityPlayer entityplayer)
	{
		return hasCharm() && !isSitting() && getOwner() == entityplayer;
	}

	@Override
    public int getAge()
    {
        return dataWatcher.getWatchableObjectInt(12);
    }

    public void setAge(int i)
    {
        dataWatcher.updateObject(12, Integer.valueOf(i));
    }
	
    public int getFishingRecharge()
    {
        return dataWatcher.getWatchableObjectInt(14);
    }
	
    public void setFishingRecharge(int i)
    {
        dataWatcher.updateObject(14, Integer.valueOf(i));
    }
	
    public int getFishingCount()
    {
        return dataWatcher.getWatchableObjectInt(15);
    }
	
    public void setFishingCount(int i)
    {
        dataWatcher.updateObject(15, Integer.valueOf(i));
    }
	
	@Override
	public void onLivingUpdate()
    {
        super.onLivingUpdate();
		
		if (!(getOwnerName().equals("")))
		{
			if (MinecraftServer.getServer() != null)
			{
				EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwnerName());
				if (entityplayer != null)
				{
					if (isEntityAlive())
					{
						LKLevelData.setHasSimba(entityplayer, true);
					}
					else
					{
						LKLevelData.setHasSimba(entityplayer, false);
					}
				}
			}
		}
		
		if (!worldObj.isRemote)
		{
			if (isSitting())
			{
				getNavigator().clearPathEntity();
			}
			
			int l = getAge();
			if (l < -1)
			{
				l++;
				setAge(l);
			}
			if (l == -1)
			{
				setAge(0);
				getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(30D);
				setHealth(getMaxHealth());
			}
			if (getFishingCount() == -1)
			{
				int i1 = getFishingRecharge();
				i1++;
				setFishingRecharge(i1);
			}
			if (getFishingRecharge() == 24000)
			{
				setFishingCount(0);
				setFishingRecharge(0);
			}
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);
			if (!isSitting() && worldObj.getBlockId(i, j, k) == Block.waterStill.blockID)
			{
				if (getFishingRecharge() == 0)
				{
					if (getFishingCount() == 0)
					{
						if (getRNG().nextInt(160) == 0)
						{
							setFishingCount(1);
							catchFishAt(i, j, k);
						}
					}
					if (getFishingCount() == 1)
					{
						if (getRNG().nextInt(160) == 0)
						{
							catchFishAt(i, j, k);
							if (getRNG().nextInt(4) == 0)
							{
								setFishingCount(-1);
							}
							else
							{
								setFishingCount(2);
							}
						}
					}
					if (getFishingCount() == 2)
					{
						if (getRNG().nextInt(200) == 0)
						{
							catchFishAt(i, j, k);
							if (getRNG().nextInt(3) == 0)
							{
								setFishingCount(-1);
							}
							else
							{
								setFishingCount(3);
							}
						}
					}
					if (getFishingCount() == 3)
					{
						if (getRNG().nextInt(200) == 0)
						{
							catchFishAt(i, j, k);
							if (getRNG().nextInt(2) == 0)
							{
								setFishingCount(-1);
							}
							else
							{
								setFishingCount(4);
							}
						}
					}
					if (getFishingCount() == 4)
					{
						if (getRNG().nextInt(220) == 0)
						{
							catchFishAt(i, j, k);
							setFishingCount(-1);
						}
					}
				}		
			}
			if (eatingTick > 0)
			{
				if (eatingTick % 4 == 0)
				{
					worldObj.playSoundAtEntity(this, "random.eat", 0.8F + 0.5F * (float)getRNG().nextInt(2), (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F);
				}
				eatingTick--;
			}
		}
    }
	
	private void catchFishAt(int i, int j, int k)
	{
        EntityItem fish = new EntityItem(worldObj, (double)i, (double)j, (double)k, new ItemStack(Item.fishRaw));
        fish.delayBeforeCanPickup = 10;
		fish.addVelocity(0F, 0.3D + ((double)(getRNG().nextFloat() / 3)), 0F);
        worldObj.spawnEntityInWorld(fish);
	}
	
	@Override
	public boolean isChild()
    {
        return getAge() < 0;
    }

	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	protected boolean canTriggerWalking()
    {
        return false;
    }
	
	@Override
    public void setAttackTarget(EntityLivingBase target)
    {
		if (target instanceof LKEntityLionBase || target instanceof EntityTameable || target instanceof LKEntityTicketLion || target instanceof LKCharacter || target instanceof EntityPlayer || isSitting())
		{
			return;
		}
        super.setAttackTarget(target);
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), isChild() ? 3F : 4F);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Age", getAge());
		nbt.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        nbt.setInteger("FishingRecharge", getFishingRecharge());
        nbt.setInteger("FishingCount", getFishingCount());
		nbt.setBoolean("Sitting", isSitting());
		nbt.setBoolean("Charm", hasCharm());
		
        if (getOwnerName() == null)
        {
            nbt.setString("Owner", "");
        }
        else
        {
            nbt.setString("Owner", getOwnerName());
        }
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setAge(nbt.getInteger("Age"));
		NBTTagList list = nbt.getTagList("Inventory");
        inventory.readFromNBT(list);
        setFishingRecharge(nbt.getInteger("FishingRecharge"));
        setFishingCount(nbt.getInteger("FishingCount"));
		setSitting(nbt.getBoolean("Sitting"));
		setHasCharm(nbt.getBoolean("Charm"));
		
        String s = nbt.getString("Owner");

        if (s.length() > 0)
        {
            setOwnerName(s);
        }
    }
	
	@Override
	protected boolean isMovementCeased()
    {
        return isSitting();
    }
	
	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        Entity entity = damagesource.getEntity();
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow) && !(entity instanceof LKEntityDart) && !(entity instanceof LKEntitySpear))
        {
            f = (f + 1F) / 2F;
        }
		setSitting(false);
        if (super.attackEntityFrom(damagesource, f))
        {
			if (entity != this && entity != null && !(entity instanceof EntityPlayer))
            {
                entityToAttack = entity;
            }
            return true;
        }
        else
        {
            return false;
        }
    }

	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
		if (!isEntityAlive() || entityplayer != getOwner())
		{
			return false;
		}
		
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.itemID == mod_LionKing.charm.itemID && itemstack.getItemDamage() == 0 && !hasCharm())
		{
			if (itemstack.stackSize == 1)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			}
			else
			{
				itemstack.stackSize--;
			}
			for (int i = 0; i < 12; i++)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = getRNG().nextGaussian() * 0.02D;
				double d2 = getRNG().nextGaussian() * 0.02D;
				worldObj.spawnParticle("portal", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}
			if (!worldObj.isRemote)
			{
				setHasCharm(true);
			}
			return true;
		}
		
        if (itemstack != null && itemstack.itemID == mod_LionKing.hyenaBone.itemID && getHealth() < getMaxHealth())
        {
            itemstack.stackSize--;
            heal(1);
            if (itemstack.stackSize <= 0)
            {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            }
			for (int i = 0; i < 7; i++)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = getRNG().nextGaussian() * 0.02D;
				double d2 = getRNG().nextGaussian() * 0.02D;
				worldObj.spawnParticle("smoke", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}
			eatingTick = 12;
            return true;
        }
		else if (itemstack != null && itemstack.getItem() != mod_LionKing.lionRaw && itemstack.getItem() != mod_LionKing.lionCooked && (Item.itemsList[itemstack.itemID] instanceof ItemFood) && getHealth() < getMaxHealth())
        {
			ItemFood food = (ItemFood)Item.itemsList[itemstack.itemID];
			if (food.isWolfsFavoriteMeat() || food == Item.fishRaw || food == Item.fishCooked)
			{
                itemstack.stackSize--;
                heal(food.getHealAmount());
                if (itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }
				for (int i = 0; i < 7; i++)
				{
					double d = getRNG().nextGaussian() * 0.02D;
					double d1 = getRNG().nextGaussian() * 0.02D;
					double d2 = getRNG().nextGaussian() * 0.02D;
					worldObj.spawnParticle("smoke", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
				}
				eatingTick = 12;
                return true;
			}
        }
		
		if (isChild())
		{
			if (!worldObj.isRemote)
			{
				setSitting(!isSitting());
			}
			return true;
		}
		else
		{
			if (!worldObj.isRemote)
			{
				entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_SIMBA, worldObj, entityId, 0, 0);
			}
			return true;
		}
	}

	@Override
    protected String getHurtSound()
    {
        return "lionking:lionroar";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:liondeath";
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
