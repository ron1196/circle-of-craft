package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.*;
import java.util.Random;

public class LKBiomeGenAridSavannah extends LKPrideLandsBiome
{
    public LKBiomeGenAridSavannah(int i)
    {
        super(i);
		spawnableCaveCreatureList.clear();
		topBlock = (byte)Block.sand.blockID;
        lkDecorator.treesPerChunk = 0;
		lkDecorator.mangoPerChunk = 0;
		lkDecorator.grassPerChunk = 14;
		lkDecorator.whiteFlowersPerChunk = 0;
		lkDecorator.purpleFlowersPerChunk = 0;
		lkDecorator.blueFlowersPerChunk = 0;
		lkDecorator.logsPerChunk = 1;
		lkDecorator.maizePerChunk = 0;
		lkDecorator.zazuPerChunk = 40;
		
		spawnableCaveCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 10, 4, 4));
    }
	
	@Override
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		if (random.nextInt(6) == 0)
		{
			lkDecorator.treesPerChunk++;
			if (random.nextInt(6) == 0)
			{
				lkDecorator.treesPerChunk += random.nextInt(2) + 1;
			}
		}
		
		super.decoratePrideLands(world, random, i, j);
		
		lkDecorator.treesPerChunk = 0;
		
        if (random.nextInt(10) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = j + random.nextInt(16) + 8;
            new LKWorldGenKiwano().generate(world, random, i1, j1, k1);
        }

		for (int i1 = 0; i1 < 2; i1++)
		{
			int i2 = i + random.nextInt(16) + 8;
			int j2 = random.nextInt(128);
			int k2 = j + random.nextInt(16) + 8;
			new WorldGenDeadBush(Block.deadBush.blockID).generate(world, random, i2, j2, k2);
		}
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random)
	{
		return new WorldGenTallGrass(mod_LionKing.aridGrass.blockID, 0);
	}
}
