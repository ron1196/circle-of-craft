package lionking.common;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.*;

public class LKGenLayerPrideLandsBiomes extends GenLayer
{
    private BiomeGenBase[] biomesForGeneration = {LKPrideLandsBiome.savannah, LKPrideLandsBiome.rainforest, LKPrideLandsBiome.mountains, LKPrideLandsBiome.aridSavannah, LKPrideLandsBiome.desert, LKPrideLandsBiome.grasslandSavannah, LKPrideLandsBiome.woodedSavannah, LKPrideLandsBiome.bananaForest};

    public LKGenLayerPrideLandsBiomes(long l, GenLayer genlayer)
    {
        super(l);
        parent = genlayer;
    }
	
    public LKGenLayerPrideLandsBiomes(long l)
    {
        super(l);
    }

	@Override
    public int[] getInts(int i, int j, int k, int l)
    {
        int[] intArray = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < l; i1++)
        {
            for (int j1 = 0; j1 < k; j1++)
            {
                initChunkSeed(j1 + i, i1 + j);
                intArray[j1 + i1 * k] = biomesForGeneration[nextInt(biomesForGeneration.length)].biomeID;
            }
        }
        return intArray;
    }
}
