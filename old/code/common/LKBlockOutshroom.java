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

import java.util.Random;

public class LKBlockOutshroom extends BlockMushroom
{
	private boolean glowing;
	
	public LKBlockOutshroom(int i, boolean flag)
	{
		super(i);
		setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.625F, 0.75F);
		glowing = flag;
		if (glowing)
		{
			setLightValue(0.875F);
		}
		setCreativeTab(LKCreativeTabs.tabDeco);
	}
	
	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		if (!glowing)
		{
			if (random.nextInt(50) == 0)
			{
				int i1 = i + random.nextInt(3) - 1;
				int j1 = j + random.nextInt(2) - random.nextInt(2);
				int k1 = k + random.nextInt(3) - 1;
				
				if (world.isAirBlock(i1, j1, k1) && canBlockStay(world, i1, j1, k1))
				{
					i += random.nextInt(3) - 1;
					k += random.nextInt(3) - 1;

					if (world.isAirBlock(i1, j1, k1) && canBlockStay(world, i1, j1, k1))
					{
						world.setBlock(i1, j1, k1, blockID);
					}
				}
			}
		}
	}
	
	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
		if (glowing)
		{
			return world.isBlockOpaqueCube(i, j - 1, k);
		}
		else
		{
			return super.canBlockStay(world, i, j, k);
		}
    }
}
