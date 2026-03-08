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

public class LKGenLayerWorld
{
	public static GenLayer[] createPrideLands(long seed, WorldType worldType)
	{
        byte scale = 4;
        if (worldType == WorldType.LARGE_BIOMES)
        {
            scale = 6;
        }
		
		GenLayer layer = new LKGenLayerPrideLandsBiomes(200L);
		layer = new GenLayerFuzzyZoom(2000L, layer);
		layer = new GenLayerZoom(2001L, layer);
		layer = new GenLayerZoom(2002L, layer);
		layer = new GenLayerZoom(2003L, layer);
		
		GenLayer rivers = GenLayerZoom.magnify(1000L, layer, 0);
		rivers = new GenLayerRiverInit(100L, rivers);
		rivers = GenLayerZoom.magnify(1000L, rivers, scale + 2);
		rivers = new LKGenLayerRiver(1L, rivers, 0);
		rivers = new GenLayerSmooth(1000L, rivers);
		
		GenLayer biomes = GenLayerZoom.magnify(1000L, layer, 0);
		biomes = new LKGenLayerPrideLandsBiomes(200L, biomes);
		biomes = GenLayerZoom.magnify(1000L, biomes, 2);
		
		layer = new LKGenLayerHills(1000L, biomes);
		
        for (int i = 0; i < scale; ++i)
        {
            layer = new GenLayerZoom(1000L + (long)i, layer);
        }
		
		layer = new GenLayerSmooth(1000L, layer);
		layer = new GenLayerRiverMix(100L, layer, rivers);
		GenLayer layer1 = new GenLayerVoronoiZoom(10L, layer);

        layer.initWorldGenSeed(seed + 1994L);
        layer1.initWorldGenSeed(seed + 1994L);
		
        return new GenLayer[] {layer, layer1, layer};
	}
	
	public static GenLayer[] createOutlands(long seed, WorldType worldType)
	{
        byte scale = 4;
		
		GenLayer layer = new LKGenLayerOutlandsBiomes(200L);
		layer = new GenLayerFuzzyZoom(2000L, layer);
		layer = new GenLayerZoom(2001L, layer);
		layer = new GenLayerZoom(2002L, layer);
		layer = new GenLayerZoom(2003L, layer);
		
		GenLayer rivers = GenLayerZoom.magnify(1000L, layer, 0);
		rivers = new GenLayerRiverInit(100L, rivers);
		rivers = GenLayerZoom.magnify(1000L, rivers, scale + 2);
		rivers = new LKGenLayerRiver(1L, rivers, 1);
		rivers = new GenLayerSmooth(1000L, rivers);
		
		GenLayer biomes = GenLayerZoom.magnify(1000L, layer, 0);
		biomes = new LKGenLayerOutlandsBiomes(200L, biomes);
		biomes = GenLayerZoom.magnify(1000L, biomes, 2);
		
		layer = new LKGenLayerHills(1000L, biomes);
		
        for (int i = 0; i < scale; ++i)
        {
            layer = new GenLayerZoom((long)(1000 + i), layer);
        }
		
		layer = new GenLayerSmooth(1000L, layer);
		layer = new GenLayerRiverMix(100L, layer, rivers);
		GenLayer layer1 = new GenLayerVoronoiZoom(10L, layer);

        layer.initWorldGenSeed(seed + 1994L);
        layer1.initWorldGenSeed(seed + 1994L);
		
        return new GenLayer[] {layer, layer1, layer};
	}
}
