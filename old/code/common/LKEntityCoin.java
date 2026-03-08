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
import net.minecraftforge.common.DimensionManager;

public class LKEntityCoin extends EntityThrowable
{
    public LKEntityCoin(World world)
    {
        super(world);
    }

    public LKEntityCoin(World world, EntityLivingBase entityliving, byte b)
    {
        super(world, entityliving);
		setCoinType(b);
    }

    public LKEntityCoin(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(12, Byte.valueOf((byte)0));
	}
	
	public void setCoinType(byte b)
	{
		dataWatcher.updateObject(12, Byte.valueOf(b));
	}
	
	public byte getCoinType()
	{
		return dataWatcher.getWatchableObjectByte(12);
	}

	@Override
    protected void onImpact(MovingObjectPosition movingobjectposition)
    {
		if (!worldObj.isRemote)
		{
			if (getThrower() instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)getThrower();
				breakCoin(entityplayer);
			}
			setDead();
		}
    }

    private void breakCoin(EntityPlayer entityplayer)
    {
		entityplayer.fallDistance = 0F;
		double[] position = new double[3];
		float[] rotation = new float[2];
		
		if (getCoinType() == 0)
		{
			position = new double[]{0, 103, 0};
			rotation = new float[]{worldObj.rand.nextFloat() * 360F, 0F};
		}
		else if (getCoinType() == 1)
		{
			position = new double[]{LKLevelData.moundX + 0.5D, LKLevelData.moundY + 10D, LKLevelData.moundZ + 0.5D};
			rotation = new float[]{90F, 0F};
		}
		
		DimensionManager.getWorld(entityplayer.dimension).theChunkProviderServer.loadChunk(MathHelper.floor_double(position[0]) >> 4, MathHelper.floor_double(position[2]) >> 4);
		MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(entityplayer.username).playerNetServerHandler.setPlayerLocation(position[0], position[1], position[2], rotation[0], rotation[1]);
        worldObj.playSoundAtEntity(entityplayer, "random.glass", 4F, 1F);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
		nbt.setByte("Type", getCoinType());
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		setCoinType(nbt.getByte("Type"));
    }
}
