package lionking.common;

import net.minecraft.world.World;
import net.minecraft.world.biome.SpawnListEntry;
import java.util.Random;

public class LKBiomeGenSavannahBase extends LKPrideLandsBiome
{
    public LKBiomeGenSavannahBase(int i)
    {
        super(i);
		
        lkDecorator.treesPerChunk = 0;
		lkDecorator.mangoPerChunk = 0;
		lkDecorator.grassPerChunk = 13;
		lkDecorator.whiteFlowersPerChunk = 4;
		lkDecorator.purpleFlowersPerChunk = 0;
		lkDecorator.blueFlowersPerChunk = 0;
		lkDecorator.logsPerChunk = 3;
		lkDecorator.maizePerChunk = 15;
		lkDecorator.zazuPerChunk = 150;
		
		spawnableCreatureList.add(new SpawnListEntry(LKEntityGiraffe.class, 2, 2, 4));
		
		spawnableCaveCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 10, 4, 4));
    }
	
	@Override
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		if (random.nextInt(5) == 0)
		{
			lkDecorator.purpleFlowersPerChunk++;
		}
		
		super.decoratePrideLands(world, random, i, j);
		
		lkDecorator.purpleFlowersPerChunk = 0;
		
        if (random.nextInt(6) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = j + random.nextInt(16) + 8;
            new LKWorldGenYams().generate(world, random, i1, j1, k1);
        }
	}
}
