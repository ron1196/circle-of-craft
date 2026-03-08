package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import java.util.Random;

public class LKBiomeGenDesert extends LKPrideLandsBiome
{
    public LKBiomeGenDesert(int i)
    {
        super(i);
		
		spawnableCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		topBlock = (byte)Block.sand.blockID;
		fillerBlock = (byte)Block.sand.blockID;
        lkDecorator.treesPerChunk = 0;
		lkDecorator.mangoPerChunk = 0;
		lkDecorator.grassPerChunk = 0;
		lkDecorator.whiteFlowersPerChunk = 0;
		lkDecorator.purpleFlowersPerChunk = 0;
		lkDecorator.blueFlowersPerChunk = 0;
		lkDecorator.logsPerChunk = 0;
		lkDecorator.maizePerChunk = 0;
		lkDecorator.zazuPerChunk = 0;
    }

	@Override
	public void decoratePrideLands(World world, Random random, int i, int k)
	{
		super.decoratePrideLands(world, random, i, k);
		
        if (random.nextInt(4) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new WorldGenCactus().generate(world, random, i1, j1, k1);
        }

		for (int l = 0; l < 2; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
			new WorldGenDeadBush(Block.deadBush.blockID).generate(world, random, i1, j1, k1);
		}
	}
}
