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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKEntityHyena extends EntityMob
{
    public LKEntityHyena(World world)
    {
        super(world);
        setSize(0.6F, 0.8F);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.5D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(3D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Byte.valueOf((byte)getRNG().nextInt(3)));
	}
	
	public byte getHyenaType()
	{
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public void setHyenaType(byte b)
	{
		dataWatcher.updateObject(20, Byte.valueOf(b));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("HyenaType", getHyenaType());
	}
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
       	super.readEntityFromNBT(nbt);
		setHyenaType(nbt.getByte("HyenaType"));
	}
	
	@Override
    protected void attackEntity(Entity entity, float f)
    {
        if (attackTime <= 0 && f < 1.7F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            attackEntityAsMob(entity);
        }
    }

	@Override
    public void onLivingUpdate()
    {
        if (damagedBySunlight() && worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getBrightness(1.0F);
            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && getRNG().nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
				attackEntityFrom(DamageSource.generic, (float)(getRNG().nextInt(2) + 1));
            }
        }
        super.onLivingUpdate();
    }

	@Override
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        if (canAttackZazus() && entityToAttack == null && !hasPath() && worldObj.rand.nextInt(300) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(LKEntityZazu.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if (!list.isEmpty())
            {
                setTarget((Entity)list.get(getRNG().nextInt(list.size())));
            }
        }
    }

	@Override
    protected String getLivingSound()
    {
        return "mob.wolf.growl";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

	@Override
    protected int getDropItemId()
    {
        return 0;
    }

	@Override
    public void onDeath(DamageSource damagesource)
    {
        if (damagesource.getEntity() instanceof EntityPlayer && onHyenaKilled((EntityPlayer)damagesource.getEntity()))
        {
            return;
		}
		super.onDeath(damagesource);
	}
	
	public boolean onHyenaKilled(EntityPlayer entityplayer)
	{
		entityplayer.triggerAchievement(LKAchievementList.killHyena);
		int looting = EnchantmentHelper.getLootingModifier(entityplayer);
		if (!worldObj.isRemote)
		{
			int bones = getRNG().nextInt(3) + getRNG().nextInt(1 + looting);
			for (int i = 0; i < bones; i++)
			{
				dropItem(mod_LionKing.hyenaBone.itemID, 1);
			}
			
			if (getRNG().nextInt(40) == 0)
			{
				entityDropItem(new ItemStack(mod_LionKing.hyenaHeadItem.itemID, 1, getHyenaType()), 0F);
			}
		}
		return false;
	}
	
	public boolean canAttackZazus()
	{
		return true;
	}
	
	public boolean damagedBySunlight()
	{
		return true;
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
