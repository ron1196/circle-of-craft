package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.world.biome.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import java.util.Random;
import cpw.mods.fml.relauncher.*;

public class LKBiomeGenUpendi extends BiomeGenBase
{
	public static BiomeGenBase upendi;
	
	protected LKUpendiDecorator upendiDecorator;
	
    public LKBiomeGenUpendi(int i)
    {
        super(i);
        spawnableMonsterList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		spawnableCreatureList.add(new SpawnListEntry(LKEntityLion.class, 4, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityLioness.class, 4, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityFlamingo.class, 3, 4, 4));
		
		spawnableCaveCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 10, 4, 4));
		
		upendiDecorator = new LKUpendiDecorator(this);
        upendiDecorator.treesPerChunk = 13;
		upendiDecorator.mangoPerChunk = 3;
		upendiDecorator.grassPerChunk = 20;
		upendiDecorator.whiteFlowersPerChunk = 10;
		upendiDecorator.purpleFlowersPerChunk = 8;
		upendiDecorator.redFlowersPerChunk = 4;
		upendiDecorator.zazuPerChunk = 100;
		upendiDecorator.maizePerChunk = 35;
		waterColorMultiplier = 0xFF1144;
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float f)
    {
        return 0x8E84FF;
    }
	
	public void decorateUpendi(World world, Random random, int i, int j)
	{
		upendiDecorator.decorate(world, random, i, j);
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
			if (random.nextInt(8) == 0)
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
		return new WorldGenTallGrass(Block.tallGrass.blockID, random.nextInt(4) == 0 ? 2 : 1);
	}
}
