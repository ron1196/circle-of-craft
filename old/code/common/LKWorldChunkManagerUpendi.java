package lionking.common;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LKWorldChunkManagerUpendi extends WorldChunkManager
{
    private BiomeGenBase theBiome;
    private float temperature;
    private float rainfall;

    public LKWorldChunkManagerUpendi()
    {
		BiomeGenBase biome = LKBiomeGenUpendi.upendi;
        theBiome = biome;
        temperature = biome.temperature;
        rainfall = biome.rainfall;
    }

	@Override
    public BiomeGenBase getBiomeGenAt(int i, int j)
    {
        return theBiome;
    }

	@Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int i, int j, int k, int l)
    {
        if (biomes == null || biomes.length < k * l)
        {
            biomes = new BiomeGenBase[k * l];
        }

        Arrays.fill(biomes, 0, k * l, theBiome);
        return biomes;
    }

	@Override
    public float[] getTemperatures(float[] floats, int i, int j, int k, int l)
    {
        if (floats == null || floats.length < k * l)
        {
            floats = new float[k * l];
        }

        Arrays.fill(floats, 0, k * l, temperature);
        return floats;
    }

	@Override
    public float[] getRainfall(float[] floats, int i, int j, int k, int l)
    {
        if (floats == null || floats.length < k * l)
        {
            floats = new float[k * l];
        }

        Arrays.fill(floats, 0, k * l, rainfall);
        return floats;
    }

	@Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int i, int j, int k, int l)
    {
        if (biomes == null || biomes.length < k * l)
        {
            biomes = new BiomeGenBase[k * l];
        }

        Arrays.fill(biomes, 0, k * l, theBiome);
        return biomes;
    }
	
	@Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int i, int j, int k, int l, boolean flag)
    {
        return loadBlockGeneratorData(biomes, i, j, k, l);
    }

	@Override
    public ChunkPosition findBiomePosition(int i, int j, int k, List list, Random random)
    {
        return list.contains(theBiome) ? new ChunkPosition(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1)) : null;
    }

	@Override
    public boolean areBiomesViable(int i, int j, int k, List list)
    {
        return list.contains(theBiome);
    }
}
