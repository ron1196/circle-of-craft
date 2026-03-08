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

import java.util.Random;

public class LKBiomeGenMountains extends LKPrideLandsBiome
{
    public LKBiomeGenMountains(int i)
    {
        super(i);
        lkDecorator.treesPerChunk = 0;
		lkDecorator.mangoPerChunk = 0;
		lkDecorator.grassPerChunk = 1;
		lkDecorator.whiteFlowersPerChunk = 0;
		lkDecorator.purpleFlowersPerChunk = 0;
		lkDecorator.blueFlowersPerChunk = 1;
		lkDecorator.logsPerChunk = 6;
		lkDecorator.maizePerChunk = 4;
		lkDecorator.zazuPerChunk = 80;
		spawnableCreatureList.add(new SpawnListEntry(LKEntityGiraffe.class, 3, 2, 4));
    }
	
	public void decoratePrideLands(World world, Random random, int i, int j)
	{
		if (random.nextInt(6) == 0)
		{
			lkDecorator.treesPerChunk++;
			if (random.nextInt(5) == 0)
			{
				lkDecorator.treesPerChunk += random.nextInt(2) + 1;
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
}
