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

import java.nio.ByteBuffer;

public class LKTileEntityFurRug extends TileEntity
{
	public byte direction;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("Direction", (byte)direction);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		direction = nbt.getByte("Direction");
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		byte[] posX = ByteBuffer.allocate(4).putInt(xCoord).array();
		byte[] posY = ByteBuffer.allocate(4).putInt(yCoord).array();
		byte[] posZ = ByteBuffer.allocate(4).putInt(zCoord).array();
		byte[] data = new byte[13];
		for (int i = 0; i < 4; i++)
		{
			data[i] = posX[i];
			data[i + 4] = posY[i];
			data[i + 8] = posZ[i];
		}
		data[12] = direction;
		return new Packet250CustomPayload("lk.tileEntity", data);
    }
}
