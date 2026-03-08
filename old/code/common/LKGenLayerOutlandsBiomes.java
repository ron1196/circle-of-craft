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

public class LKGenLayerOutlandsBiomes extends GenLayer
{
    private BiomeGenBase[] biomesForGeneration = {LKOutlandsBiome.outlands};

    public LKGenLayerOutlandsBiomes(long l, GenLayer genlayer)
    {
        super(l);
        parent = genlayer;
    }
	
    public LKGenLayerOutlandsBiomes(long l)
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
