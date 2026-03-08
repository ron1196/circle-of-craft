package lionking.common;

import net.minecraft.world.World;
import java.util.Random;
import cpw.mods.fml.relauncher.*;

public class LKBiomeGenSavannah extends LKBiomeGenSavannahBase
{
    public LKBiomeGenSavannah(int i)
    {
        super(i);
    }
	
	@Override
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		if (random.nextInt(4) == 0)
		{
			lkDecorator.treesPerChunk++;
			if (random.nextInt(5) == 0)
			{
				lkDecorator.treesPerChunk += random.nextInt(3) + 1;
			}
		}
		
		if (random.nextInt(70) == 0)
		{
			lkDecorator.mangoPerChunk++;
		}
		
		super.decoratePrideLands(world, random, i, j);
		
		lkDecorator.treesPerChunk = 0;
		lkDecorator.mangoPerChunk = 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBiomeGrassColor()
    {
		return 0xFFD726;
    }
}
