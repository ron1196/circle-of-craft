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

public class LKItemBanana extends LKItemFood
{
	public LKItemBanana(int i, int j, float f, boolean flag)
	{
		super(i, j, f, flag);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ)
	{
		int id = world.getBlockId(i, j, k);
		int meta = world.getBlockMetadata(i, j, k);

		if (id == mod_LionKing.prideWood2.blockID && BlockLog.limitToValidMetadata(meta) == 0)
		{
			if (side == 0 || side == 1)
			{
				return false;
			}
			if (side == 2)
			{
				k--;
			}
			if (side == 3)
			{
				k++;
			}
			if (side == 4)
			{
				i--;
			}
			if (side == 5)
			{
				i++;
			}

			if (world.isAirBlock(i, j, k))
			{
				int bananaMetadata = ForgeDirection.getOrientation(side - 2).getOpposite().ordinal();
				world.setBlock(i, j, k, mod_LionKing.hangingBanana.blockID, bananaMetadata, 3);

				if (!entityplayer.capabilities.isCreativeMode)
				{
					itemstack.stackSize--;
				}
			}
			return true;
		}
		return false;
	}
}
