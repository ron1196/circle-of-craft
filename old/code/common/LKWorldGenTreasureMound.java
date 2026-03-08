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

public class LKWorldGenTreasureMound extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		for (int i1 = 1; i1 < 5; i1++)
		{
			for (int k1 = 1; k1 < 5; k1++)
			{
				if (world.getBlockId(i + i1, j - 1, k + k1) != Block.sand.blockID || !world.isAirBlock(i + i1, j, k + k1))
				{
					return false;
				}
			}
		}
		
		for (int i1 = 1; i1 < 5; i1++)
		{
			for (int k1 = 1; k1 < 5; k1++)
			{
				world.setBlock(i + i1, j - 1, k + k1, mod_LionKing.outsand.blockID, 0, 2);
			}
		}
		
		for (int i1 = 0; i1 < 6; i1++)
		{
			for (int k1 = 0; k1 < 6; k1++)
			{
				for (int j1 = 0; j1 < 3 + random.nextInt(2); j1++)
				{
					world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite.blockID, 0, 2);
				}
				
				for (int j1 = -1; world.isAirBlock(i + i1, j + j1, k + k1); j1--)
				{
					world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite.blockID, 0, 2);
				}
				
				if (i1 > 0 && i1 < 5 && k1 > 0 && k1 < 5)
				{
					for (int j1 = 0; j1 < 6 + random.nextInt(4); j1++)
					{
						world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite.blockID, 0, 2);
					}
				}
				
				if (i1 > 1 && i1 < 4 && k1 > 1 && k1 < 4)
				{
					for (int j1 = 0; j1 < 8 + random.nextInt(6); j1++)
					{
						world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite.blockID, 0, 2);
					}
				}
			}
		}
		
		for (int i1 = 1; i1 < 5; i1++)
		{
			for (int k1 = 1; k1 < 5; k1++)
			{
				for (int j1 = 0; j1 < 2 + random.nextInt(2); j1++)
				{
					if (world.isBlockOpaqueCube(i + i1, j + j1 + 1, k + k1))
					{
						world.setBlock(i + i1, j + j1, k + k1, 0, 0, 2);
					}
				}
			}
		}
		
		for (int j1 = 0; world.isAirBlock(i + 1, j + j1, k + 1); j1++)
		{
			world.setBlock(i + 1, j + j1, k + 1, mod_LionKing.prideBrick.blockID, 1, 2);
		}
		for (int j1 = 0; world.isAirBlock(i + 1, j + j1, k + 4); j1++)
		{
			world.setBlock(i + 1, j + j1, k + 4, mod_LionKing.prideBrick.blockID, 1, 2);
		}
		for (int j1 = 0; world.isAirBlock(i + 4, j + j1, k + 1); j1++)
		{
			world.setBlock(i + 4, j + j1, k + 1, mod_LionKing.prideBrick.blockID, 1, 2);
		}
		for (int j1 = 0; world.isAirBlock(i + 4, j + j1, k + 4); j1++)
		{
			world.setBlock(i + 4, j + j1, k + 4, mod_LionKing.prideBrick.blockID, 1, 2);
		}
		
		int direction = random.nextInt(4);
		
		ChunkCoordinates[] chestLocations = null;
		
		if (direction == 0)
		{
			world.setBlock(i, j + 1, k + 2, 0, 0, 2);
			world.setBlock(i, j + 1, k + 3, 0, 0, 2);
			world.setBlock(i, j + 2, k + 2, mod_LionKing.termite.blockID, 0, 2);
			world.setBlock(i, j + 2, k + 3, mod_LionKing.termite.blockID, 0, 2);
			
			chestLocations = new ChunkCoordinates[] { new ChunkCoordinates(i + 4, j, k + 2), new ChunkCoordinates(i + 4, j, k + 3) };
		}
		
		if (direction == 1)
		{
			world.setBlock(i + 5, j + 1, k + 2, 0, 0, 2);
			world.setBlock(i + 5, j + 1, k + 3, 0, 0, 2);
			world.setBlock(i + 5, j + 2, k + 2, mod_LionKing.termite.blockID, 0, 2);
			world.setBlock(i + 5, j + 2, k + 3, mod_LionKing.termite.blockID, 0, 2);
			
			chestLocations = new ChunkCoordinates[] { new ChunkCoordinates(i + 1, j, k + 2), new ChunkCoordinates(i + 1, j, k + 3) };
		}
		
		if (direction == 2)
		{
			world.setBlock(i + 2, j + 1, k, 0, 0, 2);
			world.setBlock(i + 3, j + 1, k, 0, 0, 2);
			world.setBlock(i + 2, j + 2, k, mod_LionKing.termite.blockID, 0, 2);
			world.setBlock(i + 3, j + 2, k, mod_LionKing.termite.blockID, 0, 2);
			
			chestLocations = new ChunkCoordinates[] { new ChunkCoordinates(i + 2, j, k + 4), new ChunkCoordinates(i + 3, j, k + 4) };
		}
		
		if (direction == 3)
		{
			world.setBlock(i + 2, j + 1, k + 5, 0, 0, 2);
			world.setBlock(i + 3, j + 1, k + 5, 0, 0, 2);
			world.setBlock(i + 2, j + 2, k + 5, mod_LionKing.termite.blockID, 0, 2);
			world.setBlock(i + 3, j + 2, k + 5, mod_LionKing.termite.blockID, 0, 2);
			
			chestLocations = new ChunkCoordinates[] { new ChunkCoordinates(i + 2, j, k + 1), new ChunkCoordinates(i + 3, j, k + 1) };
		}
		
		for (int i1 = 0; i1 < 2; i1++)
		{
			int i2 = chestLocations[i1].posX;
			int j2 = chestLocations[i1].posY;
			int k2 = chestLocations[i1].posZ;
			world.setBlock(i2, j2, k2, Block.chest.blockID, 0, 2);
			TileEntityChest tileentitychest = (TileEntityChest)world.getBlockTileEntity(i2, j2, k2);
			if (tileentitychest == null)
			{
				continue;
			}
			for (int i3 = 0; i3 < 4; i3++)
			{
				ItemStack itemstack = pickLoot(random);
				if (itemstack != null)
				{
					if (itemstack.getItem().getItemEnchantability() > 0 && random.nextInt(3) == 0)
					{
						EnchantmentHelper.addRandomEnchantment(random, itemstack, 3);
					}
					tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
				}
			}
		}
		
		return true;
	}
	
	private ItemStack pickLoot(Random random)
	{
		int i = random.nextInt(7);
		ItemStack defaultStack = new ItemStack(mod_LionKing.itemTermite, 2 + random.nextInt(5));
		
		if (i == 0)
		{
			return defaultStack;
		}
		if (i == 1)
		{
			return new ItemStack(mod_LionKing.dartBlack, 4 + random.nextInt(4));
		}
		if (i == 2)
		{
			return new ItemStack(mod_LionKing.nukaShard, 3 + random.nextInt(7));
		}
		if (i == 3)
		{
			return new ItemStack(mod_LionKing.featherBlack, 2 + random.nextInt(3));
		}
		if (i == 4)
		{
			return new ItemStack(mod_LionKing.lionCooked, 2 + random.nextInt(3));
		}
		if (i == 5)
		{
			int j = random.nextInt(5);
			
			switch (j)
			{
				case 0: return new ItemStack(mod_LionKing.swordCorrupt);
				case 1: return new ItemStack(mod_LionKing.pickaxeCorrupt);
				case 2: return new ItemStack(mod_LionKing.swordKivulite);
				case 3: return new ItemStack(mod_LionKing.pickaxeKivulite);
				case 4: return new ItemStack(mod_LionKing.axeKivulite);
			}
		}
		if (i == 6)
		{
			return new ItemStack(mod_LionKing.kivulite, 1 + random.nextInt(3));
		}
		
		return defaultStack;
	}
}
