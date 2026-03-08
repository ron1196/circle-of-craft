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

public class LKEntityBug extends EntityCreature implements IAnimals
{
	public int trapTick = -1;
	public LKTileEntityBugTrap targetTrap;
	
    public LKEntityBug(World world)
    {
        super(world);
        setSize(0.4F, 0.4F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
		tasks.addTask(2, new LKEntityAIBugFindTrap(this, 1.2D));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(2D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (trapTick > -1)
		{
			if (trapTick % 4 == 1 && trapTick < 30)
			{
				worldObj.playSoundAtEntity(this, "random.eat", getSoundVolume(), (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F);
			}
			if (trapTick == 38)
			{
				worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume() * 2.0F, (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F);
			}
			trapTick++;
		}
		if (targetTrap == null)
		{
			trapTick = -1;
		}
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Eating", trapTick);
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        trapTick = nbt.getInteger("Eating");
    }
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 1 + worldObj.rand.nextInt(2);
    }

	@Override
    protected boolean canDespawn()
    {
        return false;
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
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
		if (!worldObj.isRemote)
		{
			dropItem(mod_LionKing.bug.itemID, 1);
		}
		setDead();
    }
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
