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

import java.util.*;

public abstract class LKOutlandsBiome extends BiomeGenBase
{
	public static BiomeGenBase outlands;
	public static BiomeGenBase outlandsMountains;
	public static BiomeGenBase outlandsRiver;
	
	protected LKOutlandsDecorator outlandsDecorator;
	
    public LKOutlandsBiome(int i)
    {
        super(i);
		
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
		
        spawnableMonsterList.add(new SpawnListEntry(LKEntityOutlander.class, 40, 4, 8));
        spawnableMonsterList.add(new SpawnListEntry(LKEntityOutlandess.class, 40, 4, 8));
		spawnableMonsterList.add(new SpawnListEntry(LKEntityVulture.class, 30, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(LKEntitySkeletalHyena.class, 10, 4, 6));
		
		outlandsDecorator = new LKOutlandsDecorator(this);
		
		topBlock = (byte)Block.sand.blockID;
		fillerBlock = (byte)Block.sand.blockID;
		
        setDisableRain();
    }
	
	public void decorateOutlands(World world, Random random, int i, int j)
	{
		outlandsDecorator.decorate(world, random, i, j);
	}
	
	public WorldGenerator getOutlandsTreeGen(Random random)
	{
		return new LKWorldGenDeadTrees();
	}
}
