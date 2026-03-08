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

public class LKOutlandsDecorator
{
    private World currentWorld;
    private Random randomGenerator;
    private int chunk_X;
    private int chunk_Z;
    private LKOutlandsBiome biome;
    private WorldGenerator nukaGen;
	private WorldGenerator kivuliteGen;
    private WorldGenerator termiteAsOreGen;
	public int treesPerChunk;
	public int deadBushPerChunk;

    public LKOutlandsDecorator(LKOutlandsBiome outlandsbiome)
    {
        nukaGen = new LKWorldGenMinable(mod_LionKing.prideCoal.blockID, 9, 1);
		kivuliteGen = new LKWorldGenMinable(mod_LionKing.oreSilver.blockID, 4, 1);
        termiteAsOreGen = new LKWorldGenMinable(mod_LionKing.termite.blockID, 20);
        treesPerChunk = 0;
		deadBushPerChunk = 0;
        biome = outlandsbiome;
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
        generateOres();
		
        for (int l1 = 0; l1 < treesPerChunk; l1++)
        {
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
            WorldGenerator worldgenerator = biome.getOutlandsTreeGen(randomGenerator);
            worldgenerator.generate(currentWorld, randomGenerator, j6, randomGenerator.nextInt(128), k10);
        }
		
        for (int k2 = 0; k2 < deadBushPerChunk; k2++)
        {
            int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j15 = randomGenerator.nextInt(128);
            int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new WorldGenDeadBush(Block.deadBush.blockID)).generate(currentWorld, randomGenerator, k11, j15, l17);
        }
		
        for (int k2 = 0; k2 < 2; k2++)
        {
            int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j15 = randomGenerator.nextInt(64);
            int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new WorldGenFlowers(mod_LionKing.outshroom.blockID)).generate(currentWorld, randomGenerator, k11, j15, l17);
        }
		
        for (int i3 = 0; i3 < 60; i3++)
        {
            int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j15 = randomGenerator.nextInt(128);
            int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            new LKWorldGenTermiteMound().generate(currentWorld, randomGenerator, k11, j15, l17);
        }
		
        for (int i3 = 0; i3 < 80; i3++)
        {
            int k11 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j15 = randomGenerator.nextInt(128);
            int l17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            new LKWorldGenLiquids(Block.lavaMoving.blockID).generate(currentWorld, randomGenerator, k11, j15, l17);
        }
    }

    private void generateOre(int i, WorldGenerator worldgenerator, int j, int k)
    {
        for (int l = 0; l < i; l++)
        {
            int i1 = chunk_X + randomGenerator.nextInt(16);
            int j1 = randomGenerator.nextInt(k - j) + j;
            int k1 = chunk_Z + randomGenerator.nextInt(16);
            worldgenerator.generate(currentWorld, randomGenerator, i1, j1, k1);
        }
    }

    private void generateOres()
    {
        generateOre(18, nukaGen, 0, 128);
		generateOre(8, kivuliteGen, 0, 48);
        generateOre(18, termiteAsOreGen, 0, 128);
    }
}
