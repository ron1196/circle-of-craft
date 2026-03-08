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

public class LKUpendiDecorator
{
    private World currentWorld;
    private Random randomGenerator;
    private int chunk_X;
    private int chunk_Z;
    private LKBiomeGenUpendi biome;
    private WorldGenerator dirtGen;
	private WorldGenerator passionGen;
	private WorldGenerator mangoGen;
    private WorldGenerator whiteFlowerGen;
    private WorldGenerator purpleFlowerGen;
	private WorldGenerator redFlowerGen;
    private WorldGenerator logGen;
    private WorldGenerator zazuGen;
	private WorldGenerator maizeGen;
    public int treesPerChunk;
	public int mangoPerChunk;
	public int grassPerChunk;
	public int whiteFlowersPerChunk;
	public int purpleFlowersPerChunk;
	public int redFlowersPerChunk;
	public int zazuPerChunk;
	public int maizePerChunk;

    public LKUpendiDecorator(LKBiomeGenUpendi upendi)
    {
		dirtGen = new LKWorldGenMinable(Block.dirt.blockID, 32);
		passionGen = new LKWorldGenPassionTrees(false);
		mangoGen = new LKWorldGenMangoTrees(false);
		whiteFlowerGen = new WorldGenFlowers(mod_LionKing.whiteFlower.blockID);
		purpleFlowerGen = new LKWorldGenTallFlowers(false, 0);
		redFlowerGen = new LKWorldGenTallFlowers(false, 1);
		zazuGen = new LKWorldGenZazu();
		maizeGen = new LKWorldGenMaize();
        treesPerChunk = 0;
		mangoPerChunk = 0;
		grassPerChunk = 0;
		whiteFlowersPerChunk = 0;
		purpleFlowersPerChunk = 0;
		redFlowersPerChunk = 0;
		zazuPerChunk = 0;
		maizePerChunk = 0;
        biome = upendi;
    }

    public void decorate(World world, Random random, int i, int j)
    {
        currentWorld = world;
        randomGenerator = random;
        chunk_X = i;
        chunk_Z = j;
        decorate();
        return;
    }

    private void decorate()
    {
        for (int l = 0; l < 20; l++)
        {
            int i1 = chunk_X + randomGenerator.nextInt(16);
            int j1 = randomGenerator.nextInt(128);
            int k1 = chunk_Z + randomGenerator.nextInt(16);
            dirtGen.generate(currentWorld, randomGenerator, i1, j1, k1);
        }
		
		if (randomGenerator.nextInt(20) == 0)
		{
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
			int trees = 3 + randomGenerator.nextInt(4);
			for (int l = 0; l < trees; l++)
			{
				int j7 = j6 + randomGenerator.nextInt(13) - 6;
				int k11 = k10 + randomGenerator.nextInt(13) - 6;
				passionGen.generate(currentWorld, randomGenerator, j7, currentWorld.getHeightValue(j7, k11), k11);
			}
        }
		
        for (int l1 = 0; l1 < treesPerChunk; l1++)
        {
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
			int height = currentWorld.getHeightValue(j6, k10);
			if (height > 75)
			{
				WorldGenerator treeGen = biome.getRandomWorldGenForTrees(randomGenerator);
				treeGen.generate(currentWorld, randomGenerator, j6, height, k10);
			}
        }
		
        for (int j2 = 0; j2 < mangoPerChunk; j2++)
        {
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
			int height = currentWorld.getHeightValue(j6, k10);
			if (height > 75)
			{
				mangoGen.generate(currentWorld, randomGenerator, j6, height, k10);
			}
        }
		
        for (int k2 = 0; k2 < grassPerChunk; k2++)
        {
            int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j15 = randomGenerator.nextInt(128);
            int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            WorldGenerator grassGen = biome.getRandomWorldGenForGrass(randomGenerator);
			grassGen.generate(currentWorld, randomGenerator, k11, j15, l17);
        }

        for (int j2 = 0; j2 < whiteFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            whiteFlowerGen.generate(currentWorld, randomGenerator, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < purpleFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            purpleFlowerGen.generate(currentWorld, randomGenerator, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < redFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            redFlowerGen.generate(currentWorld, randomGenerator, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < zazuPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            zazuGen.generate(currentWorld, randomGenerator, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < maizePerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            maizeGen.generate(currentWorld, randomGenerator, l6, i11, l14);
        }

        for (int i3 = 0; i3 < 400; i3++)
        {
            int i1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j2 = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
            int k3 = chunk_Z + randomGenerator.nextInt(16) + 8;
            new LKWorldGenLiquids(Block.waterMoving.blockID).generate(currentWorld, randomGenerator, i1, j2, k3);
        }
		
		for (int i3 = 0; i3 < 3; i3++)
		{
			int i1 = chunk_X + randomGenerator.nextInt(16) + 8;
			int k3 = chunk_Z + randomGenerator.nextInt(16) + 8;
			if (randomGenerator.nextInt(3) == 0)
			{
				new LKWorldGenLily().generate(currentWorld, randomGenerator, i1, 64, k3);
			}
			else
			{
				new WorldGenWaterlily().generate(currentWorld, randomGenerator, i1, 64, k3);
			}
		}
    }
}
