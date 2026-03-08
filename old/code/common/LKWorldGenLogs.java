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

public class LKWorldGenLogs extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		for (int l = 0; l < 7; l++)
		{
			if (!(world.getBlockId(i, j - 1, k + l) == Block.grass.blockID || world.getBlockId(i, j - 1, k + l) == Block.sand.blockID) || !isBlockReplaceable(world, i, j, k + l))
			{
				return false;
			}
		}
		
		for (int k1 = 0; k1 < 7; k1++)
		{
			world.setBlock(i, j, k + k1, mod_LionKing.log.blockID, 0, 2);
			if (world.getBlockId(i, j - 1, k + k1) == Block.grass.blockID)
			{
				world.setBlock(i, j - 1, k + k1, Block.dirt.blockID, 0, 2);
			}
		}
		
		for (int i1 = -5; i1 < 6; i1++)
		{
			for (int j1 = -3; j1 < 3; j1++)
			{
				for (int k1 = -3; k1 < 10; k1++)
				{
					if (world.getBlockId(i + i1, j + j1, k + k1) == Block.grass.blockID && world.isAirBlock(i + i1, j + j1 + 1, k + k1) && random.nextInt(4) != 0)
					{
						world.setBlock(i + i1, j + j1 + 1, k + k1, Block.tallGrass.blockID, 1, 2);
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean isBlockReplaceable(World world, int i, int j, int k)
	{
		int l = world.getBlockId(i, j, k);
		return l == Block.tallGrass.blockID || l == mod_LionKing.aridGrass.blockID || world.isAirBlock(i, j, k);
	}
}