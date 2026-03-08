package lionking.common;

import net.minecraftforge.common.Configuration;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.biome.*;
import java.util.Random;

public abstract class LKPrideLandsBiome extends BiomeGenBase
{
	public static BiomeGenBase savannah;
	public static BiomeGenBase rainforest;
	public static BiomeGenBase mountains;
	public static BiomeGenBase woodedSavannah;
	public static BiomeGenBase rainforestHills;
	public static BiomeGenBase river;
	public static BiomeGenBase aridSavannah;
	public static BiomeGenBase desert;
	public static BiomeGenBase grasslandSavannah;
	public static BiomeGenBase bananaForest;
	
	public static void initBiomes(Configuration config)
	{
		int idBiomeSavannah = config.get("biome", "Savannah", 80).getInt();
		int idBiomeOutlands = config.get("biome", "Outlands", 81).getInt();
		int idBiomeRainforest = config.get("biome", "Rainforest", 82).getInt();
		int idBiomeMountains = config.get("biome", "Mountains", 83).getInt();
		int idBiomeWoodedSavannah = config.get("biome", "Wooded Savannah", 84).getInt();
		int idBiomeRainforestHills = config.get("biome", "Rainforest Hills", 85).getInt();
		int idBiomeRiver = config.get("biome", "River", 86).getInt();
		int idBiomeOutlandsMountains = config.get("biome", "Outlands Mountains", 87).getInt();
		int idBiomeOutlandsRiver = config.get("biome", "Outlands River", 88).getInt();
		int idBiomeAridSavannah = config.get("biome", "Arid Savannah", 89).getInt();
		int idBiomeUpendi = config.get("biome", "Upendi", 90).getInt();
		int idBiomeDesert = config.get("biome", "Desert", 91).getInt();
		int idBiomeGrasslandSavannah = config.get("biome", "Grassland Savannah", 92).getInt();
		int idBiomeBananaForest = config.get("biome", "Banana Forest", 93).getInt();
	
		savannah = new LKBiomeGenSavannah(idBiomeSavannah).setTemperatureRainfall(0.9F, 0.4F).setMinMaxHeight(0.1F, 0.3F).setColor(0xC9C94E).setBiomeName("Savannah");
		rainforest = new LKBiomeGenRainforest(idBiomeRainforest).setTemperatureRainfall(1.0F, 0.9F).setMinMaxHeight(0.05F, 0.55F).setColor(0x5A912A).setBiomeName("Rainforest");
		mountains = new LKBiomeGenMountains(idBiomeMountains).setTemperatureRainfall(0.6F, 0.4F).setMinMaxHeight(0.7F, 1.1F).setColor(0x519964).setBiomeName("Mountains");
		woodedSavannah = new LKBiomeGenWoodedSavannah(idBiomeWoodedSavannah).setTemperatureRainfall(0.9F, 0.5F).setMinMaxHeight(0.2F, 0.4F).setColor(0x9AC44C).setBiomeName("Wooded Savannah");
		rainforestHills = new LKBiomeGenRainforest(idBiomeRainforestHills).setTemperatureRainfall(1.0F, 0.9F).setMinMaxHeight(0.6F, 1.6F).setColor(0x6BAD32).setBiomeName("Rainforest Hills");
		river = new LKBiomeGenRiver(idBiomeRiver).setTemperatureRainfall(0.7F, 0.7F).setMinMaxHeight(-0.5F, 0.0F).setColor(0x0000FF).setBiomeName("River");
		aridSavannah = new LKBiomeGenAridSavannah(idBiomeAridSavannah).setTemperatureRainfall(1.0F, 0.2F).setMinMaxHeight(0.08F, 0.13F).setColor(0xD3AE56).setBiomeName("Arid Savannah");
		desert = new LKBiomeGenDesert(idBiomeDesert).setTemperatureRainfall(2F, 0F).setMinMaxHeight(0.2F, 0.4F).setDisableRain().setColor(0xF2D185).setBiomeName("Desert");
		grasslandSavannah = new LKBiomeGenGrasslandSavannah(idBiomeGrasslandSavannah).setTemperatureRainfall(1F, 0.3F).setMinMaxHeight(0.1F, 0.1F).setColor(0xD8C158).setBiomeName("Grassland Savannah");
		bananaForest = new LKBiomeGenBananaForest(idBiomeBananaForest).setTemperatureRainfall(0.9F, 0.8F).setMinMaxHeight(0.4F, 0.4F).setColor(0xFFDE4F).setBiomeName("Banana Forest");
		
		LKOutlandsBiome.outlands = new LKBiomeGenOutlands(idBiomeOutlands).setTemperatureRainfall(1.6F, 0.0F).setMinMaxHeight(0.16F, 0.2F).setColor(0xB59B63).setBiomeName("Outlands");
		LKOutlandsBiome.outlandsMountains = new LKBiomeGenOutlandsMountains(idBiomeOutlandsMountains).setTemperatureRainfall(1.4F, 0.0F).setMinMaxHeight(0.8F, 2.0F).setColor(0x897140).setBiomeName("Outlands Mountains");
		LKOutlandsBiome.outlandsRiver = new LKBiomeGenOutlandsRiver(idBiomeOutlandsRiver).setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(-0.5F, 0.0F).setColor(0xFF710C).setBiomeName("Outlands River");
		
		LKBiomeGenUpendi.upendi = new LKBiomeGenUpendi(idBiomeUpendi).setTemperatureRainfall(1.0F, 1.0F).setMinMaxHeight(-0.05F, 2.0F).setColor(0x3DB714).setBiomeName("Upendi");
	}
	
	protected LKBiomeDecorator lkDecorator;
	
    public LKPrideLandsBiome(int i)
    {
        super(i);
        spawnableMonsterList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		spawnableCreatureList.add(new SpawnListEntry(LKEntityLion.class, 4, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityLioness.class, 4, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityZebra.class, 8, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityRhino.class, 8, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LKEntityGemsbok.class, 8, 4, 4));
		
        spawnableMonsterList.add(new SpawnListEntry(LKEntityHyena.class, 8, 4, 6));
		spawnableMonsterList.add(new SpawnListEntry(LKEntityCrocodile.class, 3, 4, 4));
		
		lkDecorator = new LKBiomeDecorator(this);
    }
	
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		lkDecorator.decorate(world, random, i, j);
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random random)
	{
		return new LKWorldGenTrees(false);
	}
	
	public WorldGenerator getRandomWorldGenForGrass(Random random)
	{
		return new WorldGenTallGrass(Block.tallGrass.blockID, 1);
	}
}
