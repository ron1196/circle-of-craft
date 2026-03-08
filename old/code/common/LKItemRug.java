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

import net.minecraftforge.common.ForgeDirection;

public class LKItemRug extends LKItemMetadata
{
	public LKItemRug(int i)
	{
		super(i);
	}
	
	@Override
    public boolean placeBlockAt(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ, int metadata)
    {
		boolean flag = super.placeBlockAt(itemstack, entityplayer, world, i, j, k, side, hitX, hitY, hitZ, metadata);
		if (flag && world.getBlockId(i, j, k) == getBlockID())
		{
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LKTileEntityFurRug)
			{
				ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
				((LKTileEntityFurRug)world.getBlockTileEntity(i, j, k)).direction = (byte)dir.ordinal();
			}
		}
		return flag;
    }
}
