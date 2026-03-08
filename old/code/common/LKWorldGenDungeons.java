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

public class LKWorldGenDungeons extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        byte byte0 = 3;
        int l = random.nextInt(2) + 2;
        int i1 = random.nextInt(2) + 2;
        int j1 = 0;
		boolean spawnCrocodiles = false;
        for (int k1 = i - l - 1; k1 <= i + l + 1; k1++)
        {
            for (int j2 = j - 1; j2 <= j + byte0 + 1; j2++)
            {
                for (int i3 = k - i1 - 1; i3 <= k + i1 + 1; i3++)
                {
                    Material material = world.getBlockMaterial(k1, j2, i3);
                    if (j2 == j - 1 && !material.isSolid())
                    {
                        return false;
                    }
                    if (j2 == j + byte0 + 1 && !material.isSolid())
                    {
                        return false;
                    }
                    if ((k1 == i - l - 1 || k1 == i + l + 1 || i3 == k - i1 - 1 || i3 == k + i1 + 1) && j2 == j && world.isAirBlock(k1, j2, i3) && world.isAirBlock(k1, j2 + 1, i3))
                    {
                        j1++;
                    }
                }

            }
		}
		
		if (random.nextInt(3) == 0)
		{
			spawnCrocodiles = true;
			l += random.nextInt(2) + 1;
			i1 += random.nextInt(2) + 1;
		}
		
        if (j1 < 1 || j1 > 5)
        {
            return false;
        }
		
		int j5 = random.nextInt(4);
		int j6 = random.nextInt(4);
		if (random.nextInt(4) == 0)
		{
			j5 = -1;
		}
		if (random.nextInt(4) == 0)
		{
			j6 = -1;
		}
		
        for (int l1 = i - l - 1; l1 <= i + l + 1; l1++)
        {
            for (int k2 = j + byte0; k2 >= j - 1; k2--)
            {
                for (int j3 = k - i1 - 1; j3 <= k + i1 + 1; j3++)
                {
                    if (l1 == i - l - 1 || k2 == j - 1 || j3 == k - i1 - 1 || l1 == i + l + 1 || k2 == j + byte0 + 1 || j3 == k + i1 + 1)
                    {
						if (!spawnCrocodiles)
						{
							if (k2 >= 0 && !world.getBlockMaterial(l1, k2 - 1, j3).isSolid())
							{
								world.setBlock(l1, k2, j3, 0, 0, 2);
								continue;
							}
							if (!world.getBlockMaterial(l1, k2, j3).isSolid())
							{
								continue;
							}
						}
                        if (k2 == j - 1 && random.nextInt(4) != 0)
                        {
                            world.setBlock(l1, k2, j3, mod_LionKing.prideBrickMossy.blockID, 0, 2);
                        }
						else
                        {
                            world.setBlock(l1, k2, j3, mod_LionKing.prideBrick.blockID, 0, 2);
                        }
                    }
					else
                    {
                        world.setBlock(l1, k2, j3, 0, 0, 2);
                    }
					if (j5 == 0 || j6 == 0)
					{
						if (l1 == i - l && j3 == k - i1 && k2 >= j)
						{
							world.setBlock(l1, k2, j3, mod_LionKing.pridePillar.blockID, k2 - j == 0 || k2 - j == 3 ? 1 : 2, 2);
						}
					}
					if (j5 == 1 || j6 == 1)
					{
						if (l1 == i + l && j3 == k - i1 && k2 >= j)
						{
							world.setBlock(l1, k2, j3, mod_LionKing.pridePillar.blockID, k2 - j == 0 || k2 - j == 3 ? 1 : 2, 2);
						}
					}
					if (j5 == 2 || j6 == 2)
					{
						if (l1 == i - l && j3 == k + i1 && k2 >= j)
						{
							world.setBlock(l1, k2, j3, mod_LionKing.pridePillar.blockID, k2 - j == 0 || k2 - j == 3 ? 1 : 2, 2);
						}
					}
					if (j5 == 3 || j6 == 3)
					{
						if (l1 == i + l && j3 == k + i1 && k2 >= j)
						{
							world.setBlock(l1, k2, j3, mod_LionKing.pridePillar.blockID, k2 - j == 0 || k2 - j == 3 ? 1 : 2, 2);
						}
					}
					if (spawnCrocodiles && l1 > i - l && l1 < i + l && j3 > k - i1 && j3 < k + i1 && k2 == j - 1)
					{
						if (l1 != i || j3 != k)
						{
							world.setBlock(l1, k2, j3, Block.waterStill.blockID, 0, 2);
						}
						world.setBlock(l1, k2 - 1, j3, random.nextInt(4) != 0 ? mod_LionKing.prideBrickMossy.blockID : mod_LionKing.prideBrick.blockID, 0, 2);
					}
                }
            }
        }

		int j7 = spawnCrocodiles ? 3 : 2;
        for (int i2 = 0; i2 < j7; i2++)
        {
label0:
            for (int l2 = 0; l2 <= j7; l2++)
            {
                int k3 = (i + random.nextInt(l * 2 + 1)) - l;
                int l3 = j;
                int i4 = (k + random.nextInt(i1 * 2 + 1)) - i1;
                if (!world.isAirBlock(k3, l3, i4) && world.getBlockId(k3, l3, i4) != Block.vine.blockID)
                {
                    continue;
                }
                int j4 = 0;
                if (world.getBlockMaterial(k3 - 1, l3, i4).isSolid())
                {
                    j4++;
                }
                if (world.getBlockMaterial(k3 + 1, l3, i4).isSolid())
                {
                    j4++;
                }
                if (world.getBlockMaterial(k3, l3, i4 - 1).isSolid())
                {
                    j4++;
                }
                if (world.getBlockMaterial(k3, l3, i4 + 1).isSolid())
                {
                    j4++;
                }
                if (j4 != 1)
                {
                    continue;
                }
				if (!world.isBlockOpaqueCube(k3, l3 - 1, i4))
				{
					continue;
				}
                world.setBlock(k3, l3, i4, Block.chest.blockID, 0, 2);
                TileEntityChest tileentitychest = (TileEntityChest)world.getBlockTileEntity(k3, l3, i4);
                if (tileentitychest == null)
                {
                    break;
                }
                int k4 = 0;
                do
                {
                    if (k4 >= 8)
                    {
						if (random.nextInt(4) == 0)
						{
							tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), new ItemStack(mod_LionKing.passionSapling));
						}
                        break label0;
                    }
                    ItemStack itemstack = pickCheckLootItem(random);
                    if (itemstack != null)
                    {
						if (itemstack.getItem().getItemEnchantability() > 0 && random.nextInt(3) != 0)
						{
							EnchantmentHelper.addRandomEnchantment(random, itemstack, 3);
						}
                        tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
                    }
                    k4++;
                }
				while (true);
            }
        }

        world.setBlock(i, j, k, mod_LionKing.mobSpawner.blockID, 0, 2);
        LKTileEntityMobSpawner spawner = (LKTileEntityMobSpawner)world.getBlockTileEntity(i, j, k);
        if (spawner != null)
        {
            spawner.setMobID(spawnCrocodiles ? LKEntities.getEntityIDFromClass(LKEntityCrocodile.class) : LKEntities.getEntityIDFromClass(LKEntityHyena.class));
        }
		
		if (spawnCrocodiles)
		{
			for (int l1 = i - l - 1; l1 <= i + l + 1; l1++)
			{
				for (int k2 = j + byte0; k2 >= j - 1; k2--)
				{
					for (int j3 = k - i1 - 1; j3 <= k + i1 + 1; j3++)
					{
						Block block = Block.blocksList[world.getBlockId(l1, k2, j3)];
						if (block != null && (block == mod_LionKing.pridestone || block == mod_LionKing.prideBrick))
						{
							if (random.nextInt(4) == 0 && world.isAirBlock(l1 - 1, k2, j3))
							{
								generateVines(world, random, l1 - 1, k2, j3, 8);
							}
							if (random.nextInt(4) == 0 && world.isAirBlock(l1 + 1, k2, j3))
							{
								generateVines(world, random, l1 + 1, k2, j3, 2);
							}
							if (random.nextInt(4) == 0 && world.isAirBlock(l1, k2, j3 - 1))
							{
								generateVines(world, random, l1, k2, j3 - 1, 2);
							}
							if (random.nextInt(4) == 0 && world.isAirBlock(l1, k2, j3 + 1))
							{
								generateVines(world, random, l1, k2, j3 + 1, 4);
							}
						}
					}
				}
			}
		}
        return true;
    }

    private ItemStack pickCheckLootItem(Random random)
    {
        int i = random.nextInt(12);
        if (i == 0)
        {
            return new ItemStack(mod_LionKing.hyenaBone, random.nextInt(3) + 2);
        }
        if (i == 1)
        {
            return new ItemStack(mod_LionKing.bug, random.nextInt(4) + 2);
        }
        if (i == 2 || i == 3 || i == 4)
        {
			int j = random.nextInt(7);
			switch (j)
			{
				case 0: return new ItemStack(mod_LionKing.dartBlue, random.nextInt(5) + 3);
				case 1: return new ItemStack(mod_LionKing.dartYellow, random.nextInt(4) + 3);
				case 2: return new ItemStack(mod_LionKing.dartRed, random.nextInt(4) + 3);
				case 3: return new ItemStack(mod_LionKing.featherBlue, random.nextInt(4) + 3);
				case 4: return new ItemStack(mod_LionKing.featherYellow, random.nextInt(3) + 3);
				case 5: return new ItemStack(mod_LionKing.featherRed, random.nextInt(3) + 3);
				case 6: return new ItemStack(mod_LionKing.silverDartShooter);
			}
        }
        if (i == 5)
        {
			return new ItemStack(mod_LionKing.chocolateMufasa, random.nextInt(3) + 1);
        }
        if (i == 6)
        {
            return new ItemStack(mod_LionKing.silver, random.nextInt(3) + 2);
        }
        if (i == 7)
        {
			return new ItemStack(mod_LionKing.dartQuiver, 1, LKLevelData.quiverDamage++);
        }
        if (i == 8)
        {
            return new ItemStack(mod_LionKing.mango, random.nextInt(3) + 1);
        }
        if (i == 9)
        {
			int j = random.nextInt(3);
			switch (j)
			{
				case 0: return new ItemStack(mod_LionKing.peacockGem, random.nextInt(2) + 1);
				case 1: return new ItemStack(Item.compass);
				case 2: return new ItemStack(mod_LionKing.jar);
			}
        }
        if (i == 10 || i == 11)
        {
			int j = random.nextInt(7);
			switch (j)
			{
				case 0: return new ItemStack(mod_LionKing.shovelSilver);
				case 1: return new ItemStack(mod_LionKing.pickaxeSilver);
				case 2: return new ItemStack(mod_LionKing.axeSilver);
				case 3: return new ItemStack(mod_LionKing.swordSilver);
				case 4: return new ItemStack(mod_LionKing.helmetSilver);
				case 5: return new ItemStack(mod_LionKing.bootsSilver);
				case 6: return new ItemStack(mod_LionKing.note, 1 + random.nextInt(3), 6);
			}
        }
        return null;
    }
	
    private void generateVines(World world, Random random, int i, int j, int k, int l)
    {
        world.setBlock(i, j, k, Block.vine.blockID, l, 2);
		Block.vine.onNeighborBlockChange(world, i, j, k, 0);
        int i1 = random.nextInt(2) + 4;
        while (true)
        {
            --j;
            if (world.getBlockId(i, j, k) != 0 || i1 <= 0)
            {
                return;
            }
            world.setBlock(i, j, k, Block.vine.blockID, l, 2);
			Block.vine.onNeighborBlockChange(world, i, j, k, 0);
            --i1;
        }
    }
}
