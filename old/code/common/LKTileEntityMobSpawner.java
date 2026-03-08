package lionking.common;

import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import java.nio.ByteBuffer;

public class LKTileEntityMobSpawner extends TileEntity
{
    public int delay = -1;
    private int mobID = 1;
    public double yaw;
    public double yaw2 = 0.0D;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private Entity spawnedMob;
    private int maxNearbyEntities = 6;
    private int requiredPlayerRange = 16;
    private int spawnRange = 4;

    public LKTileEntityMobSpawner()
    {
        delay = 20;
    }

    public int getMobID()
    {
        return mobID;
    }

    public void setMobID(int i)
    {
        mobID = i;
    }

    public boolean anyPlayerInRange()
    {
        return worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, (double)requiredPlayerRange) != null;
    }

    @Override
    public void updateEntity()
    {
        if (anyPlayerInRange())
        {
            double var5;

            if (worldObj.isRemote)
            {
                double var1 = (double)((float)xCoord + worldObj.rand.nextFloat());
                double var3 = (double)((float)yCoord + worldObj.rand.nextFloat());
                var5 = (double)((float)zCoord + worldObj.rand.nextFloat());
                worldObj.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                worldObj.spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

                if (delay > 0)
                {
                    --delay;
                }

                yaw2 = yaw;
                yaw = (yaw + (double)(1000.0F / ((float)delay + 200.0F))) % 360.0D;
            }
            else
            {
                if (delay == -1)
                {
                    updateDelay();
                }

                if (delay > 0)
                {
                    --delay;
                    return;
                }

                boolean var12 = false;

                for (int var2 = 0; var2 < spawnCount; ++var2)
                {
                    Entity var13 = LKEntities.createEntityByID(getMobID(), worldObj);

                    if (var13 == null)
                    {
                        return;
                    }

                    int var4 = worldObj.getEntitiesWithinAABB(var13.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)xCoord, (double)yCoord, (double)zCoord, (double)(xCoord + 1), (double)(yCoord + 1), (double)(zCoord + 1)).expand((double)(spawnRange * 2), 4.0D, (double)(spawnRange * 2))).size();

                    if (var4 >= maxNearbyEntities)
                    {
                        updateDelay();
                        return;
                    }

                    if (var13 != null)
                    {
                        var5 = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)spawnRange;
                        double var7 = (double)(yCoord + worldObj.rand.nextInt(3) - 1);
                        double var9 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)spawnRange;
                        EntityLiving var11 = var13 instanceof EntityLiving ? (EntityLiving)var13 : null;
                        var13.setLocationAndAngles(var5, var7, var9, worldObj.rand.nextFloat() * 360.0F, 0.0F);

                        if (var11 == null || var11.getCanSpawnHere())
                        {
                            writeNBTTagsTo(var13);
                            worldObj.spawnEntityInWorld(var13);
                            worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);

                            if (var11 != null)
                            {
                                var11.spawnExplosionParticle();
                            }

                            var12 = true;
                        }
                    }
                }

                if (var12)
                {
                    updateDelay();
                }
            }

            super.updateEntity();
        }
    }

    public void writeNBTTagsTo(Entity par1Entity)
    {
        if (par1Entity instanceof EntityLiving && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).onSpawnWithEgg(null);
        }
    }

    private void updateDelay()
    {
        if (maxSpawnDelay <= minSpawnDelay)
        {
            delay = minSpawnDelay;
        }
        else
        {
            delay = minSpawnDelay + worldObj.rand.nextInt(maxSpawnDelay - minSpawnDelay);
        }

        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 1, 0);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        mobID = par1NBTTagCompound.getInteger("EntityId");
        delay = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("MinSpawnDelay"))
        {
            minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
            maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
            spawnCount = par1NBTTagCompound.getShort("SpawnCount");
        }

        if (par1NBTTagCompound.hasKey("MaxNearbyEntities"))
        {
            maxNearbyEntities = par1NBTTagCompound.getShort("MaxNearbyEntities");
            requiredPlayerRange = par1NBTTagCompound.getShort("RequiredPlayerRange");
        }

        if (par1NBTTagCompound.hasKey("SpawnRange"))
        {
            spawnRange = par1NBTTagCompound.getShort("SpawnRange");
        }

        if (worldObj != null && worldObj.isRemote)
        {
            spawnedMob = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("EntityId", getMobID());
        par1NBTTagCompound.setShort("Delay", (short)delay);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)minSpawnDelay);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
        par1NBTTagCompound.setShort("SpawnCount", (short)spawnCount);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)maxNearbyEntities);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)requiredPlayerRange);
        par1NBTTagCompound.setShort("SpawnRange", (short)spawnRange);
    }

    public Entity getMobEntity()
    {
        if (spawnedMob == null)
        {
            Entity entity = LKEntities.createEntityByID(getMobID(), null);
            writeNBTTagsTo(entity);
            spawnedMob = entity;
        }

        return spawnedMob;
    }

    @Override
    public Packet getDescriptionPacket()
    {
		byte[] posXData = ByteBuffer.allocate(4).putInt(xCoord).array();
		byte[] posYData = ByteBuffer.allocate(4).putInt(yCoord).array();
		byte[] posZData = ByteBuffer.allocate(4).putInt(zCoord).array();
		byte[] yawData = ByteBuffer.allocate(8).putDouble(yaw).array();
		byte[] yaw2Data = ByteBuffer.allocate(8).putDouble(yaw2).array();
		byte[] idData = ByteBuffer.allocate(4).putInt(mobID).array();
		byte[] data = new byte[32];
		for (int i = 0; i < 4; i++)
		{
			data[i] = posXData[i];
			data[i + 4] = posYData[i];
			data[i + 8] = posZData[i];
			data[i + 28] = idData[i];
		}
		for (int i = 0; i < 8; i++)
		{
			data[i + 12] = yawData[i];
			data[i + 20] = yaw2Data[i];
		}
		return new Packet250CustomPayload("lk.tileEntity", data);
    }

    @Override
    public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1 && worldObj.isRemote)
        {
            delay = minSpawnDelay;
			return true;
        }
		return false;
    }
}
