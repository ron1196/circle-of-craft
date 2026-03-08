package lionking.common;

import net.minecraft.block.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class LKItemHyenaMeal extends LKItem
{
	public LKItemHyenaMeal(int i)
	{
		super(i);
		setCreativeTab(LKCreativeTabs.tabMaterials);
	}
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
		if (useHyenaMeal(itemstack, entityplayer, world, i, j, k, l))
		{
			if (!world.isRemote)
			{
				world.playAuxSFX(2005, i, j, k, 0);
			}
			return true;
		}
		return false;
	}
	
	private boolean useHyenaMeal(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
	{
        if (world.isRemote || !LKIngame.isLKWorld(world.provider.dimensionId) || !entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }
		
        int i1 = world.getBlockId(i, j, k);				
        if (i1 == mod_LionKing.sapling.blockID)
        {		
			itemstack.stackSize--;
			if ((double)world.rand.nextFloat() < 0.45D)
			{
				((LKBlockSapling)mod_LionKing.sapling).growTree(world, i, j, k, world.rand);
			}
			return true;
        }
		
        if (i1 == mod_LionKing.forestSapling.blockID)
        {		
			itemstack.stackSize--;
			if ((double)world.rand.nextFloat() < 0.45D)
			{
				((LKBlockSapling)mod_LionKing.forestSapling).growTree(world, i, j, k, world.rand);
			}
			return true;
        }
		
        if (i1 == mod_LionKing.mangoSapling.blockID)
        {		
			itemstack.stackSize--;
			if ((double)world.rand.nextFloat() < 0.45D)
			{
				((LKBlockSapling)mod_LionKing.mangoSapling).growTree(world, i, j, k, world.rand);
			}
			return true;
        }
		
        if (i1 == mod_LionKing.passionSapling.blockID)
        {		
			itemstack.stackSize--;
			if ((double)world.rand.nextFloat() < 0.45D)
			{
				((LKBlockSapling)mod_LionKing.passionSapling).growTree(world, i, j, k, world.rand);
			}
			return true;
        }
		
	    if (i1 == mod_LionKing.bananaSapling.blockID)
        {		
			itemstack.stackSize--;
			if ((double)world.rand.nextFloat() < 0.45D)
			{
				((LKBlockSapling)mod_LionKing.bananaSapling).growTree(world, i, j, k, world.rand);
			}
			return true;
        }
		
		if ((i1 == Block.crops.blockID || i1 == mod_LionKing.yamCrops.blockID) && world.getBlockMetadata(i, j, k) < 7)
		{
			((BlockCrops)Block.blocksList[i1]).fertilize(world, i, j, k);
			itemstack.stackSize--;
			return true;
		}
		
		if (i1 == mod_LionKing.kiwanoStem.blockID && world.getBlockMetadata(i, j, k) < 7)
		{
			((LKBlockKiwanoStem)mod_LionKing.kiwanoStem).fertilizePartially(world, i, j, k);
			itemstack.stackSize--;
			return true;
		}
		
        if (i1 == Block.grass.blockID)
        {	
			BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(i, k);
			world.notifyBlockChange(i, j, k, world.getBlockId(i, j, k));
            label0:
            for (int l1 = 0; l1 < 128; ++l1)
            {
                int i2 = i;
                int j2 = j + 1;
                int k2 = k;
                for (int l2 = 0; l2 < l1 / 16; ++l2)
                {
                    i2 += itemRand.nextInt(3) - 1;
                    j2 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                    k2 += itemRand.nextInt(3) - 1;
                    if (world.getBlockId(i2, j2 - 1, k2) != Block.grass.blockID || world.isBlockNormalCube(i2, j2, k2))
                    {
                        continue label0;
                    }
                }
                if (world.isAirBlock(i2, j2, k2))
                {
                    if (itemRand.nextInt(7) != 0 && Block.tallGrass.canBlockStay(world, i2, j2, k2))
                    {
						int j3 = 1;
						if ((biome instanceof LKBiomeGenRainforest || biome instanceof LKBiomeGenUpendi) && itemRand.nextInt(5) == 0)
						{
							j3 = 2;
						}
						world.setBlock(i2, j2, k2, Block.tallGrass.blockID, j3, 3);
                    }
					else
					{
						if (biome instanceof LKBiomeGenUpendi)
						{
							int i3 = itemRand.nextInt(6);
							if (i3 == 0 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.flowerBase.blockID, 1, 3);
								world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop.blockID, 1, 3);
							}
							else if (i3 > 3 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.flowerBase.blockID, 0, 3);
								world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop.blockID, 0, 3);
							}
							else if (mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower.blockID, 0, 3);
							}
						}
						if (biome instanceof LKBiomeGenMountains && mod_LionKing.blueFlower.canBlockStay(world, i2, j2, k2))
						{
							world.setBlock(i2, j2, k2, mod_LionKing.blueFlower.blockID, 0, 3);
						}
						if (biome instanceof LKBiomeGenRainforest)
						{
							int i3 = itemRand.nextInt(5);
							if (i3 < 2 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.flowerBase.blockID, 0, 3);
								world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop.blockID, 0, 3);
							}
							else if (mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower.blockID, 0, 3);
							}
						}
						if (biome instanceof LKBiomeGenSavannahBase || biome instanceof LKBiomeGenRiver || biome instanceof LKBiomeGenAridSavannah)
						{
							int i3 = itemRand.nextInt(5);
							if (i3 == 0 && world.isAirBlock(i2, j2 + 1, k2) && mod_LionKing.flowerBase.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.flowerBase.blockID, 0, 3);
								world.setBlock(i2, j2 + 1, k2, mod_LionKing.flowerTop.blockID, 0, 3);
							}
							else if (mod_LionKing.whiteFlower.canBlockStay(world, i2, j2, k2))
							{
								world.setBlock(i2, j2, k2, mod_LionKing.whiteFlower.blockID, 0, 3);
							}
						}
					}
                }
            }
			itemstack.stackSize--;
			return true;
		}
		
        if (i1 == Block.sand.blockID)
        {
			BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(i, k);
			if (biome instanceof LKBiomeGenAridSavannah || biome instanceof LKBiomeGenDesert)
			{
				world.notifyBlockChange(i, j, k, world.getBlockId(i, j, k));
				label0:
				for (int l1 = 0; l1 < 128; ++l1)
				{
					int i2 = i;
					int j2 = j + 1;
					int k2 = k;
					for (int l2 = 0; l2 < l1 / 16; ++l2)
					{
						i2 += itemRand.nextInt(3) - 1;
						j2 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
						k2 += itemRand.nextInt(3) - 1;
						if (world.getBlockId(i2, j2 - 1, k2) != Block.sand.blockID || world.isBlockNormalCube(i2, j2, k2))
						{
							continue label0;
						}
					}
					if (world.isAirBlock(i2, j2, k2))
					{
						if (world.rand.nextInt(16) == 0 && Block.deadBush.canBlockStay(world, i2, j2, k2))
						{
							world.setBlock(i2, j2, k2, Block.deadBush.blockID, 0, 3);
						}
						else if (mod_LionKing.aridGrass.canBlockStay(world, i2, j2, k2))
						{
							world.setBlock(i2, j2, k2, mod_LionKing.aridGrass.blockID, 0, 3);
						}
					}
				}
				itemstack.stackSize--;
				return true;
			}
		}
		
		return false;
    }
}