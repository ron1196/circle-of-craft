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

public class LKWorldGenTermiteMound extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return random.nextInt(3) == 0 ? generateLargeTermiteMound(world, random, i, j, k) : generateTermiteMound(world, random, i, j, k);
	}
	
	private boolean generateTermiteMound(World world, Random random, int i, int j, int k)
	{
		for (int i1 = 0; i1 < 4; i1++)
		{
			for (int k1 = 0; k1 < 4; k1++)
			{
				if (!world.isBlockOpaqueCube(i + i1, j, k + k1) || !world.isAirBlock(i + i1, j + 1, k + k1))
				{
					return false;
				}
			}
		}

		for (int i1 = 0; i1 < 4; i1++)
		{
			for (int k1 = 0; k1 < 4; k1++)
			{
				generateMoundSection(world, random, i + i1, j, k + k1, 3, 2);
				if (i1 > 0 && i1 < 3 && k1 > 0 && k1 < 3)
				{
					generateMoundSection(world, random, i + i1, j, k + k1, 5, 3);
				}
			}
		}

		return true;
	}
	
	private boolean generateLargeTermiteMound(World world, Random random, int i, int j, int k)
	{
		for (int i1 = 0; i1 < 8; i1++)
		{
			for (int k1 = 0; k1 < 8; k1++)
			{
				if (!world.isBlockOpaqueCube(i + i1, j, k + k1) || !world.isAirBlock(i + i1, j + 1, k + k1))
				{
					return false;
				}
			}
		}

		for (int i1 = 0; i1 < 8; i1++)
		{
			for (int k1 = 0; k1 < 8; k1++)
			{
				generateMoundSection(world, random, i + i1, j, k + k1, 2, 4);
				if (i1 > 0 && i1 < 7 && k1 > 0 && k1 < 7)
				{
					generateMoundSection(world, random, i + i1, j, k + k1, 7, 5);
				}
				if (i1 > 1 && i1 < 6 && k1 > 1 && k1 < 6)
				{
					generateMoundSection(world, random, i + i1, j, k + k1, 13, 7);
				}
				if (i1 > 2 && i1 < 5 && k1 > 2 && k1 < 5)
				{
					generateMoundSection(world, random, i + i1, j, k + k1, 18, 13);
				}
			}
		}

		return true;
	}  

	public void generateMoundSection(World world, Random random, int i, int j, int k, int minHeight, int range)
	{
		int l = minHeight + random.nextInt(range);
		for (int j1 = 0; j1 < l; j1++)
		{
			world.setBlock(i, j + j1, k, mod_LionKing.termite.blockID, 0, 2);
		}
	}
}

