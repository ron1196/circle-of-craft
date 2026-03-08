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

public class LKBiomeDecorator
{
    private World worldObj;
    private Random rand;
    private int chunk_X;
    private int chunk_Z;
    private LKPrideLandsBiome biome;
    private WorldGenerator dirtGen;
    private WorldGenerator coalGen;
    private WorldGenerator silverGen;
    private WorldGenerator peacockGen;
	private WorldGenerator mangoGen;
    private WorldGenerator whiteFlowerGen;
    private WorldGenerator purpleFlowerGen;
    private WorldGenerator blueFlowerGen;
    private WorldGenerator logGen;
    private WorldGenerator zazuGen;
	private WorldGenerator maizeGen;
    public int treesPerChunk;
	public int mangoPerChunk;
	public int grassPerChunk;
	public int whiteFlowersPerChunk;
	public int purpleFlowersPerChunk;
	public int blueFlowersPerChunk;
	public int logsPerChunk;
	public int maizePerChunk;
	public int zazuPerChunk;

    public LKBiomeDecorator(LKPrideLandsBiome pridelandsbiome)
    {
        dirtGen = new LKWorldGenMinable(Block.dirt.blockID, 32);
        coalGen = new LKWorldGenMinable(mod_LionKing.prideCoal.blockID, 16);
        silverGen = new LKWorldGenMinable(mod_LionKing.oreSilver.blockID, 6);
        peacockGen = new LKWorldGenMinable(mod_LionKing.orePeacock.blockID, 6);
		mangoGen = new LKWorldGenMangoTrees(false);
		whiteFlowerGen = new WorldGenFlowers(mod_LionKing.whiteFlower.blockID);
		purpleFlowerGen = new LKWorldGenTallFlowers(false, 0);
		blueFlowerGen = new WorldGenFlowers(mod_LionKing.blueFlower.blockID);
		zazuGen = new LKWorldGenZazu();
		logGen = new LKWorldGenLogs();
		maizeGen = new LKWorldGenMaize();
        treesPerChunk = 0;
		mangoPerChunk = 0;
		grassPerChunk = 0;
		whiteFlowersPerChunk = 0;
		purpleFlowersPerChunk = 0;
		blueFlowersPerChunk = 0;
		logsPerChunk = 0;
		maizePerChunk = 0;
        biome = pridelandsbiome;
    }

    public void decorate(World world, Random random, int i, int j)
    {
        worldObj = world;
        rand = random;
        chunk_X = i;
        chunk_Z = j;
        decorate();
        return;
    }

    private void decorate()
    {
        generateOres();

        for (int l1 = 0; l1 < treesPerChunk; l1++)
        {
            int j6 = chunk_X + rand.nextInt(16) + 8;
            int k10 = chunk_Z + rand.nextInt(16) + 8;
            WorldGenerator treeGen = biome.getRandomWorldGenForTrees(rand);
            treeGen.generate(worldObj, rand, j6, worldObj.getHeightValue(j6, k10), k10);
        }
		
        for (int j2 = 0; j2 < mangoPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            mangoGen.generate(worldObj, rand, l6, worldObj.getHeightValue(l6, l14), l14);
        }
		
        for (int k2 = 0; k2 < grassPerChunk; k2++)
        {
            int k11 = chunk_X + rand.nextInt(16) + 8;
            int j15 = rand.nextInt(128);
            int l17 = chunk_Z + rand.nextInt(16) + 8;
            WorldGenerator grassGen = biome.getRandomWorldGenForGrass(rand);
			grassGen.generate(worldObj, rand, k11, j15, l17);
        }

        for (int j2 = 0; j2 < whiteFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int i11 = rand.nextInt(128);
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            whiteFlowerGen.generate(worldObj, rand, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < purpleFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int i11 = rand.nextInt(128);
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            purpleFlowerGen.generate(worldObj, rand, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < blueFlowersPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int i11 = rand.nextInt(128);
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            blueFlowerGen.generate(worldObj, rand, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < zazuPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int i11 = rand.nextInt(70) + 64;
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            zazuGen.generate(worldObj, rand, l6, i11, l14);
        }
		
		for (int j2 = 0; j2 < logsPerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
			int i11 = rand.nextInt(128);
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            logGen.generate(worldObj, rand, l6, i11, l14);
        }
		
        for (int j2 = 0; j2 < maizePerChunk; j2++)
        {
            int l6 = chunk_X + rand.nextInt(16) + 8;
            int i11 = rand.nextInt(128);
            int l14 = chunk_Z + rand.nextInt(16) + 8;
            maizeGen.generate(worldObj, rand, l6, i11, l14);
        }

		for (int i3 = 0; i3 < 50; i3++)
		{
			int i1 = chunk_X + rand.nextInt(16) + 8;
			int j2 = rand.nextInt(rand.nextInt(120) + 8);
			int k3 = chunk_Z + rand.nextInt(16) + 8;
			new LKWorldGenLiquids(Block.waterMoving.blockID).generate(worldObj, rand, i1, j2, k3);
		}
		
        for (int i3 = 0; i3 < 18; i3++)
        {
            int i1 = chunk_X + rand.nextInt(16) + 8;
            int j2 = rand.nextInt((64) + 16);
            int k3 = chunk_Z + rand.nextInt(16) + 8;
            new LKWorldGenLiquids(Block.lavaMoving.blockID).generate(worldObj, rand, i1, j2, k3);
        }
    }

    private void generateOre(int i, WorldGenerator worldgenerator, int j, int k)
    {
        for (int l = 0; l < i; l++)
        {
            int i1 = chunk_X + rand.nextInt(16);
            int j1 = rand.nextInt(k - j) + j;
            int k1 = chunk_Z + rand.nextInt(16);
            worldgenerator.generate(worldObj, rand, i1, j1, k1);
        }
    }

    private void generateOres()
    {
        generateOre(20, dirtGen, 0, 128);
        generateOre(20, coalGen, 0, 128);
		generateOre(8, silverGen, 0, 36);
		generateOre(1, peacockGen, 0, 18);
    }
}
