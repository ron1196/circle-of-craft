package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.*;
import java.util.Random;
import cpw.mods.fml.relauncher.*;

public class LKBiomeGenRainforest extends LKPrideLandsBiome
{
    public LKBiomeGenRainforest(int i)
    {
        super(i);
        lkDecorator.treesPerChunk = 15;
		lkDecorator.mangoPerChunk = 2;
		lkDecorator.grassPerChunk = 20;
		lkDecorator.whiteFlowersPerChunk = 10;
		lkDecorator.purpleFlowersPerChunk = 5;
		lkDecorator.blueFlowersPerChunk = 0;
		lkDecorator.logsPerChunk = 40;
		lkDecorator.maizePerChunk = 20;
		lkDecorator.zazuPerChunk = 80;
		waterColorMultiplier = 0x9BFF8C;
		spawnableMonsterList.add(new SpawnListEntry(LKEntityCrocodile.class, 3, 4, 4));
    }
	
	@Override
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		super.decoratePrideLands(world, random, i, j);
		
		for (int i3 = 0; i3 < 3; i3++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k3 = j + random.nextInt(16) + 8;
			if (random.nextInt(3) == 0)
			{
				new LKWorldGenLily().generate(world, random, i1, 64, k3);
			}
			else
			{
				new WorldGenWaterlily().generate(world, random, i1, 64, k3);
			}
		}
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random random)
	{
		if (random.nextInt(3) == 0)
		{
			return new LKWorldGenTrees(false);
		}
		else
		{
			if (random.nextInt(25) == 0)
			{
				return new LKWorldGenHugeRainforestTrees(false);
			}
			else
			{
				return new LKWorldGenRainforestTrees(false);	
			}
		}
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random)
	{
		return new WorldGenTallGrass(Block.tallGrass.blockID, random.nextInt(5) == 0 ? 2 : 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor()
    {
		return 0x097705;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBiomeGrassColor()
    {
		return 0x18CE21;
    }
}
