package lionking.common;

import net.minecraft.world.gen.feature.*;
import java.util.Random;

public class LKBiomeGenBananaForest extends LKPrideLandsBiome
{
    public LKBiomeGenBananaForest(int i)
    {
        super(i);
        lkDecorator.treesPerChunk = 7;
		lkDecorator.mangoPerChunk = 0;
		lkDecorator.grassPerChunk = 5;
		lkDecorator.whiteFlowersPerChunk = 6;
		lkDecorator.purpleFlowersPerChunk = 6;
		lkDecorator.blueFlowersPerChunk = 0;
		lkDecorator.logsPerChunk = 10;
		lkDecorator.maizePerChunk = 20;
		lkDecorator.zazuPerChunk = 20;
    }
	
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random random)
	{
		if (random.nextInt(4) == 0)
		{
			return super.getRandomWorldGenForTrees(random);
		}
		return new LKWorldGenBananaTrees(false);
	}
}
