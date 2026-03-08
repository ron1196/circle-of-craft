package lionking.common;

import net.minecraft.world.World;
import java.util.Random;

public class LKBiomeGenWoodedSavannah extends LKBiomeGenSavannahBase
{
    public LKBiomeGenWoodedSavannah(int i)
    {
        super(i);
		lkDecorator.treesPerChunk = 2;
		lkDecorator.zazuPerChunk = 50;
		lkDecorator.logsPerChunk = 8;
    }
	
	@Override
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		if (random.nextInt(4) == 0)
		{
			lkDecorator.treesPerChunk++;
		}
		
		if (random.nextInt(40) == 0)
		{
			lkDecorator.mangoPerChunk++;
		}
		
		super.decoratePrideLands(world, random, i, j);
		
		lkDecorator.treesPerChunk = 2;
		lkDecorator.mangoPerChunk = 0;
	}
}
