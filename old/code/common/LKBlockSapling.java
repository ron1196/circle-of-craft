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
import lionking.client.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockSapling extends LKBlockFlower
{
    public LKBlockSapling(int i)
    {
        super(i);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setCreativeTab(LKCreativeTabs.tabDeco);
    }

	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
		WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
        if (worldchunkmanager != null)
		{
			boolean canGrow = LKIngame.isLKWorld(world.provider.dimensionId);
            if (blockID == mod_LionKing.passionSapling.blockID)
			{
				canGrow = worldchunkmanager.getBiomeGenAt(i, k) instanceof LKBiomeGenUpendi;
			}
			
			if (canGrow)
			{
        		if (world.isRemote)
        		{
            		return;
        		}
				
        		super.updateTick(world, i, j, k, random);
        		if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(7) == 0)
        		{
					incrementGrowth(world, i, j, k, random);
				}
			}
		}
	}
	
	public void incrementGrowth(World world, int i, int j, int k, Random random)
	{
		int l = world.getBlockMetadata(i, j, k);
		if ((l & 8) == 0)
		{
			world.setBlockMetadataWithNotify(i, j, k, l | 8, 4);
		}
		else
		{
			growTree(world, i, j, k, random);
		}
    }

    public void growTree(World world, int i, int j, int k, Random random)
    {
        WorldGenerator tree = null;
        int i1 = 0;
        int j1 = 0;
        boolean growHugeTree = false;

	    if (blockID == mod_LionKing.bananaSapling.blockID)
        {
            tree = new LKWorldGenBananaTrees(true);
        }
        else if (blockID == mod_LionKing.passionSapling.blockID)
        {
            tree = new LKWorldGenPassionTrees(true);
        }
        else if (blockID == mod_LionKing.mangoSapling.blockID)
        {
            tree = new LKWorldGenMangoTrees(true);
        }
        else if (blockID == mod_LionKing.forestSapling.blockID || blockID == mod_LionKing.sapling.blockID)
        {
            for (i1 = 0; i1 >= -1; --i1)
            {
                for (j1 = 0; j1 >= -1; --j1)
                {
                    if (world.getBlockId(i + i1, j, k + j1) == blockID && world.getBlockId(i + i1 + 1, j, k + j1) == blockID && world.getBlockId(i + i1, j, k + j1 + 1) == blockID && world.getBlockId(i + i1 + 1, j, k + j1 + 1) == blockID)
                    {
						if (blockID == mod_LionKing.forestSapling.blockID)
						{
							tree = new LKWorldGenHugeRainforestTrees(true);
						}
						else if (blockID == mod_LionKing.sapling.blockID)
						{
							tree = new LKWorldGenTrees(true).setLarge();
						}
                        growHugeTree = true;
                        break;
                    }
                }

                if (tree != null)
                {
                    break;
                }
            }

            if (tree == null)
            {
                j1 = 0;
                i1 = 0;
				if (blockID == mod_LionKing.forestSapling.blockID)
				{
					tree = new LKWorldGenRainforestTrees(true);
				}
				else if (blockID == mod_LionKing.sapling.blockID)
				{
					tree = new LKWorldGenTrees(true);
				}
            }
        }

        if (growHugeTree)
        {
            world.setBlock(i + i1, j, k + j1, 0, 0, 4);
            world.setBlock(i + i1 + 1, j, k + j1, 0, 0, 4);
            world.setBlock(i + i1, j, k + j1 + 1, 0, 0, 4);
            world.setBlock(i + i1 + 1, j, k + j1 + 1, 0, 0, 4);
        }
        else
        {
            world.setBlock(i, j, k, 0, 0, 4);
        }
		
		if (tree != null)
		{
			if (!((WorldGenerator)tree).generate(world, random, i + i1, j, k + j1))
			{
				if (growHugeTree)
				{
					world.setBlock(i + i1, j, k + j1, blockID, 0, 4);
					world.setBlock(i + i1 + 1, j, k + j1, blockID, 0, 4);
					world.setBlock(i + i1, j, k + j1 + 1, blockID, 0, 4);
					world.setBlock(i + i1 + 1, j, k + j1 + 1, blockID, 0, 4);
				}
				else
				{
					world.setBlock(i, j, k, blockID, 0, 4);
				}
			}
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if (blockID == mod_LionKing.passionSapling.blockID)
		{
			for (int l = 0; l < 4; l++)
			{
				double d = (float)i + random.nextFloat();
				double d1 = (float)j + random.nextFloat();
				double d2 = (float)k + random.nextFloat();
				double d3 = (-0.5F + random.nextFloat()) * 0.01F;
				double d4 = random.nextFloat() * 0.01F;
				double d5 = (-0.5F + random.nextFloat()) * 0.01F;
				mod_LionKing.proxy.spawnParticle("passion", d, d1, d2, d3, d4, d5);
			}
		}
    }
}
