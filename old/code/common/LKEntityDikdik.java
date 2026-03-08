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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKEntityDikdik extends EntityAmbientCreature
{
	public LKEntityDikdik(World world)
	{
		super(world);
        setSize(0.6F, 1F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LKEntityAIAmbientAvoid(this, LKEntityHyena.class, 8.0F, 1D, 1.5D));
        tasks.addTask(2, new LKEntityAIAmbientPanic(this, 1.5D));
        tasks.addTask(3, new LKEntityAIAmbientWander(this, 1.2D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 4.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(8D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Byte.valueOf((byte)getRNG().nextInt(3)));
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	public byte getDikdikType()
	{
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public void setDikdikType(byte b)
	{
		dataWatcher.updateObject(20, Byte.valueOf(b));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("DikdikType", getDikdikType());
	}
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
       	super.readEntityFromNBT(nbt);
		setDikdikType(nbt.getByte("DikdikType"));
	}
	
	@Override
    public boolean getCanSpawnHere()
    {
		if (worldObj.provider.dimensionId != mod_LionKing.idUpendi && getRNG().nextInt(3) > 0)
		{
			return false;
		}
		
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return (worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID || worldObj.getBlockId(i, j - 1, k) == Block.sand.blockID) && worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
