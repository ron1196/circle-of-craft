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

import java.util.*;

public class LKWorldGenTicketBooth extends WorldGenerator
{
	public static boolean canGenerateAtPosition(int i, int j, int k)
	{
		if (LKLevelData.ticketBoothLocations.isEmpty())
		{
			return true;
		}
		else
		{
			Iterator it = LKLevelData.ticketBoothLocations.iterator();
			while (it.hasNext())
			{
				ChunkCoordinates coords = (ChunkCoordinates)it.next();
				if (coords.getDistanceSquared(i, j, k) < mod_LionKing.boothLimit * mod_LionKing.boothLimit)
				{
					return false;
				}
			}
			return true;
		}
	}
	
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
      	if (!isBoundingBoxClear(world, i + 2, i + 16, j + 1, j + 5, k - 3, k + 10) || !isBoundingBoxClear(world, i + 1, i + 1, j + 1, j + 3, k - 3, k + 10) || !isBoundingBoxClear(world, i - 3, i, j + 1, j + 3, k - 3, k + 5))
		{
         	return false;
		}
		
		if (!isSolidGround(world, i - 3, j, k) || !isSolidGround(world, i - 3, j, k + 3) || !isSolidGround(world, i + 7, j, k + 3) || !isSolidGround(world, i + 11, j, k + 4) || !isSolidGround(world, i + 3, j, k + 5) || !isSolidGround(world, i + 3, j, k + 2))
		{
			return false;
		}
		
		int woolType = mod_LionKing.randomBooths ? random.nextInt(16) : 14;
		int woodType = 0;
		Block stairBlock = Block.stairsWoodOak;
		if (mod_LionKing.randomBooths)
		{
			int l = random.nextInt(4);
			switch (l)
			{
				case 0:
					stairBlock = Block.stairsWoodOak;
					woodType = 0;
					break;
				case 1:
					stairBlock = Block.stairsWoodSpruce;
					woodType = 1;
					break;
				case 2:
					stairBlock = Block.stairsWoodBirch;
					woodType = 2;
					break;
				case 3:
					stairBlock = Block.stairsWoodJungle;
					woodType = 3;
			}
		}
		Block seatBlock = Block.stairsWoodOak;
		if (mod_LionKing.randomBooths)
		{
			int l = random.nextInt(6);
			switch (l)
			{
				case 0:
					seatBlock = Block.stairsWoodOak;
					break;
				case 1:
					seatBlock = Block.stairsWoodSpruce;
					break;
				case 2:
					seatBlock = Block.stairsWoodBirch;
					break;
				case 3:
					seatBlock = Block.stairsWoodJungle;
					break;
				case 4:
					seatBlock = Block.stairsCobblestone;
					break;
				case 5:
					seatBlock = Block.stairsStoneBrick;
			}
		}
		
		int fillerBlock = world.getWorldChunkManager().getBiomeGenAt(i, k).fillerBlock;
		if (fillerBlock <= 0 || fillerBlock > Block.blocksList.length)
		{
			fillerBlock = Block.dirt.blockID;
		}
		for (int i1 = -2; i1 < 16; i1++)
		{
			for (int k1 = -2; k1 < 10; k1++)
			{
				if (!(k1 > 4 && i1 < 2))
				{
					world.setBlock(i + i1, j, k + k1, Block.cobblestone.blockID, 0, 2);
					for (int j1 = 1; !world.isBlockOpaqueCube(i + i1, j - j1, k + k1); j1++)
					{
						if (!world.isBlockOpaqueCube(i + i1, j - j1, k + k1))
						{
							world.setBlock(i + i1, j - j1, k + k1, fillerBlock, 0, 2);
						}
					}
					for (int j1 = 1; j1 < 4; j1++)
					{
						world.setBlock(i + i1, j + j1, k + k1, Block.planks.blockID, woodType, 2);
					}
					if (i1 > 2)
					{
						for (int j1 = 4; j1 < 6; j1++)
						{
							world.setBlock(i + i1, j + j1, k + k1, Block.planks.blockID, woodType, 2);
						}
					}
					world.setBlock(i + i1, j + 2, k + k1, Block.stoneBrick.blockID, 0, 2);
				}
			}
		}
		
		for (int i1 = 3; i1 < 15; i1++)
		{
			for (int k1 = -1; k1 < 9; k1++)
			{
				for (int j1 = 1; j1 < 5; j1++)
				{
					world.setBlock(i + i1, j + j1, k + k1, 0, 0, 2);
				}
				if (!(i1 < 10 && i1 % 2 == 1))
				{
					world.setBlock(i + i1, j, k + k1, Block.cloth.blockID, woolType, 2);
				}
				if (i1 < 10 && i1 % 2 == 1 && k1 != 3 && k1 != 4)
				{
					world.setBlock(i + i1, j + 1, k + k1, seatBlock.blockID, 1, 2);
				}
				if (k1 == 3 || k1 == 4)
				{
					world.setBlock(i + i1, j, k + k1, Block.cobblestone.blockID, 0, 2);
				}
			}
		}
		
		for (int j1 = 0; j1 < 5; j1++)
		{
			for (int k1 = 2; k1 < 6; k1++)
			{
				if (!(j1 > 0 && j1 < 4 && k1 > 2 && k1 < 5))
				{
					world.setBlock(i + 14, j + j1, k + k1, mod_LionKing.lionPortalFrame.blockID, 0, 2);
				}
			}
		}
		
		for (int i1 = -2; i1 < 3; i1++)
		{
			for (int j1 = 1; j1 < 3; j1++)
			{
				world.setBlock(i + i1, j + j1, k + 3, 0, 0, 2);
			}
		}
		
		for (int i1 = -1; i1 < 1; i1++)
		{
			for (int j1 = 1; j1 < 3; j1++)
			{
				for (int k1 = -1; k1 < 2; k1++)
				{
					world.setBlock(i + i1, j + j1, k + k1, 0, 0, 2);
					if (i1 == -1 && j1 == 2)
					{
						world.setBlock(i + i1 - 1, j + j1, k + k1, 0, 0, 2);
					}
					if (i1 == 0 && j1 == 2 && k1 != 0)
					{
						world.setBlock(i + i1, j + j1, k + k1, Block.torchWood.blockID, k1 == -1 ? 3 : 2, 2);
					}
					if (i1 == -1 && j1 == 1 && k1 == 0)
					{
						world.setBlock(i + i1 - 1, j + j1, k + k1, Block.fence.blockID, 0, 2);
					}
					if (i1 == -1 && j1 == 2 && k1 != 0)
					{
						world.setBlock(i + i1 - 1, j + j1, k + k1, Block.thinGlass.blockID, 0, 2);
					}
				}
			}
		}
		
		for (int i1 = 4; i1 < 15; i1++)
		{
			world.setBlock(i + i1, j + 4, k - 1, stairBlock.blockID, 7, 2);
			world.setBlock(i + i1, j + 4, k + 8, stairBlock.blockID, 6, 2);
		}
		
		for (int k1 = 0; k1 < 8; k1++)
		{
			world.setBlock(i + 3, j + 4, k + k1, stairBlock.blockID, 5, 2);
			if (k1 < 2 || k1 > 5)
			{
				world.setBlock(i + 14, j + 4, k + k1, stairBlock.blockID, 4, 2);
			}
		}
		
		for (int j1 = 0; j1 < 5; j1++)
		{
			int[] id = j1 == 2 ? new int[] {Block.glowStone.blockID, 0} : new int[] {Block.planks.blockID, woodType};
			world.setBlock(i + 3, j + j1, k - 1, id[0], id[1], 2);
			world.setBlock(i + 14, j + j1, k - 1, id[0], id[1], 2);
			world.setBlock(i + 3, j + j1, k + 8, id[0], id[1], 2);
			world.setBlock(i + 14, j + j1, k + 8, id[0], id[1], 2);
		}
		
		for (int k1 = -2; k1 < 5; k1++)
		{
			if (k1 == 3)
			{
				world.setBlock(i - 3, j + 3, k + k1, Block.planks.blockID, woodType, 2);
			}
			else
			{
				world.setBlock(i - 3, j + 3, k + k1, stairBlock.blockID, 0, 2);
			}
		}
		
		for (int i1 = -2; i1 < 4; i1++)
		{
			world.setBlock(i + i1, j + 3, k - 3, stairBlock.blockID, 2, 2);
		}
		
		for (int i1 = -2; i1 < 1; i1++)
		{
			world.setBlock(i + i1, j + 3, k + 5, stairBlock.blockID, 3, 2);
		}
		
		generateSupports(world, i + 1, j + 3, k + 5, stairBlock, 0, woodType);
		world.setBlock(i + 1, j + 3, k + 5, Block.planks.blockID, woodType, 2);
		
		for (int k1 = 6; k1 < 10; k1++)
		{
			world.setBlock(i + 1, j + 3, k + k1, stairBlock.blockID, 0, 2);
		}
		
		for (int k1 = -2; k1 < 10; k1++)		
		{
			world.setBlock(i + 2, j + 5, k + k1, stairBlock.blockID, 0, 2);
			world.setBlock(i + 16, j + 5, k + k1, stairBlock.blockID, 1, 2);
		}
		
		for (int i1 = 3; i1 < 16; i1++)
		{
			world.setBlock(i + i1, j + 5, k - 3, stairBlock.blockID, 2, 2);
			world.setBlock(i + i1, j + 5, k + 10, stairBlock.blockID, 3, 2);
		}
		
		world.setBlock(i + 2, j + 3, k + 10, stairBlock.blockID, 3, 2);
		world.setBlock(i + 3, j + 3, k + 10, stairBlock.blockID, 3, 2);
		
		generateSupports(world, i - 3, j + 3, k - 3, stairBlock, 0, woodType);
		generateSupports(world, i - 3, j + 3, k + 5, stairBlock, 0, woodType);
		generateSupports(world, i + 1, j + 3, k + 10, stairBlock, 0, woodType);
		generateSupports(world, i + 4, j + 3, k + 10, stairBlock, 3, woodType);
		generateSupports(world, i + 4, j + 3, k - 3, stairBlock, 1, woodType);
		world.setBlock(i + 2, j + 5, k - 3, stairBlock.blockID, 0, 2);
		world.setBlock(i + 2, j + 5, k + 10, stairBlock.blockID, 0, 2);
		generateSupports(world, i + 16, j + 5, k - 3, stairBlock, 2, woodType);
		generateSupports(world, i + 16, j + 5, k + 10, stairBlock, 3, woodType);
		
        world.setBlock(i - 2, j + 1 , k + 3, Block.doorWood.blockID, 0, 2);
        world.setBlock(i - 2, j + 2 , k + 3, Block.doorWood.blockID, 12, 2);
 
		for (int i1 = 5; i1 < 13; i1++)
		{
			if (i1 % 3 != 1)
			{
				world.setBlock(i + i1, j + 2, k - 1, Block.torchWood.blockID, 0, 2);
				world.setBlock(i + i1, j + 2, k + 8, Block.torchWood.blockID, 0, 2);
			}
		}
		
		for (int k1 = 1; k1 < 7; k1++)
		{
			if (k1 < 2 || k1 > 5)
			{
				world.setBlock(i + 14, j + 2, k + k1, Block.torchWood.blockID, 0, 2);
			}
			if (k1 < 3 || k1 > 4)
			{
				world.setBlock(i + 3, j + 2, k + k1, Block.torchWood.blockID, 0, 2);
			}
		}
		
		world.setBlock(i - 3, j + 2, k - 2, Block.torchWood.blockID, 0, 2);
		world.setBlock(i - 3, j + 2, k + 2, Block.torchWood.blockID, 0, 2);
		world.setBlock(i - 3, j + 2, k + 4, Block.torchWood.blockID, 0, 2);

		world.setBlock(i - 4, j + 3, k + 3, Block.signWall.blockID, 4, 2);
        TileEntitySign sign = (TileEntitySign)world.getBlockTileEntity(i - 4, j + 3, k + 3);
		sign.signText[0] = ("---------------");
        sign.signText[1] = ("Now showing:");
        sign.signText[2] = ("The Lion King");
		sign.signText[3] = ("---------------");

        LKEntityTicketLion lion = new LKEntityTicketLion(world);
		lion.setLocationAndAngles(i, j + 1, k, 0.0F, 0.0F);
		world.spawnEntityInWorld(lion);
		
		world.setBlock(i + 2, j + 1, k, Block.chest.blockID, 5, 2);
		world.setBlock(i + 2, j + 2, k, Block.trapdoor.blockID, 3, 2);
		
		TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(i + 2, j + 1, k);
		if (chest != null)
		{
			int i1 = 2 + random.nextInt(4);
			for (int j1 = 0; j1 < i1; j1++)
			{
				chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), getBasicLoot(random));
			}
			
			Item suitPart = null;
			int l = random.nextInt(4);
			switch (l)
			{
				case 0: suitPart = mod_LionKing.ticketLionHead;
					break;
				case 1: suitPart = mod_LionKing.ticketLionSuit;
					break;
				case 2: suitPart = mod_LionKing.ticketLionLegs;
					break;
				case 3: suitPart = mod_LionKing.ticketLionFeet;
			}
			chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), new ItemStack(suitPart));
		}
		
		return true;
	}
	
	private ItemStack getBasicLoot(Random random)
	{
		int i = random.nextInt(11);
		{
			switch (i)
			{
				default: case 0: return new ItemStack(Item.stick, 2 + random.nextInt(4));
				case 1: return new ItemStack(Item.paper, 1 + random.nextInt(3));
				case 2: return new ItemStack(Item.book, 1 + random.nextInt(2));
				case 3: return new ItemStack(Item.bread, 3 + random.nextInt(2));
				case 4: return new ItemStack(Item.compass);
				case 5: return new ItemStack(Item.goldNugget, 2 + random.nextInt(6));
				case 6: return new ItemStack(Item.appleRed, 1 + random.nextInt(3));
				case 7: return new ItemStack(Item.silk, 2 + random.nextInt(2));
				case 8: return new ItemStack(Item.bowlEmpty, 1 + random.nextInt(4));
				case 9: return new ItemStack(Item.cookie, 1 + random.nextInt(3));
				case 10: return new ItemStack(Item.coal, 1 + random.nextInt(2));
			}
		}
	}
	
	private void generateSupports(World world, int i, int j, int k, Block stairBlock, int stairMetadata, int woodType)
	{
		world.setBlock(i, j, k, stairBlock.blockID, stairMetadata, 2);
		for (int j1 = 1; !world.isBlockOpaqueCube(i, j - j1, k); j1++)
		{
			if (!world.isBlockOpaqueCube(i, j - j1, k))
			{
				int[] id = new int[] {Block.fence.blockID, 0};
				int i1 = world.getBlockId(i, j - j1, k);
				if (Block.blocksList[i1] != null && (Block.blocksList[i1].blockMaterial == Material.water || Block.blocksList[i1].blockMaterial == Material.lava))
				{
					id = new int[] {Block.planks.blockID, woodType};
				}
				world.setBlock(i, j - j1, k, id[0], id[1], 2);
			}
		}
	}
	
	private boolean isBoundingBoxClear(World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ)
	{
		for (int i = minX; i <= maxX; i++)
		{
			for (int j = minY; j <= maxY; j++)
			{
				for (int k = minZ; k <= maxZ; k++)
				{
					if (i == minX || i == maxX || j == minY || j == maxY || k == minZ || k == maxZ)
					{
						int id = world.getBlockId(i, j, k);
						Block block = Block.blocksList[id];
						if (block == null || block instanceof BlockFlower || block == Block.waterStill || block == Block.snow)
						{
							continue;
						}
						else
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean isSolidGround(World world, int i, int j, int k)
	{
		return world.isBlockOpaqueCube(i, j, k) && world.isBlockOpaqueCube(i, j - 1, k) && world.isBlockOpaqueCube(i, j - 2, k);
	}
}
