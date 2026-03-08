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
import net.minecraft.world.gen.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

import java.util.List;
import java.util.Random;

public class LKChunkProviderPrideLands
	implements IChunkProvider
{
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    private World worldObj;
    private double[] noiseField;
    private double[] stoneNoise = new double[256];
    private MapGenBase caveGenerator = new LKMapGenCaves();
    private BiomeGenBase[] biomesForGeneration;
    double[] noise3;
    double[] noise1;
    double[] noise2;
    double[] noise5;
    double[] noise6;
    float[] noiseFloatArray;
    int[][] unusedIntArray = new int[32][32];

    public LKChunkProviderPrideLands(World world, long l)
    {
        worldObj = world;
        rand = new Random(l);
        noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        noiseGen4 = new NoiseGeneratorOctaves(rand, 4);
        noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    public void generateTerrain(int i, int j, byte[] byteArray)
    {
        byte byte0 = 4;
        byte byte1 = 16;
        byte byte2 = 63;
        int k = byte0 + 1;
        byte byte3 = 17;
        int l = byte0 + 1;
        biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, i * 4 - 2, j * 4 - 2, k + 5, l + 5);
        noiseField = initializeNoiseField(noiseField, i * byte0, 0, j * byte0, k, byte3, l);

        for (int i1 = 0; i1 < byte0; ++i1)
        {
            for (int j1 = 0; j1 < byte0; ++j1)
            {
                for (int k1 = 0; k1 < byte1; ++k1)
                {
                    double d = 0.125D;
                    double d1 = noiseField[((i1 + 0) * l + j1 + 0) * byte3 + k1 + 0];
                    double d2 = noiseField[((i1 + 0) * l + j1 + 1) * byte3 + k1 + 0];
                    double d3 = noiseField[((i1 + 1) * l + j1 + 0) * byte3 + k1 + 0];
                    double d4 = noiseField[((i1 + 1) * l + j1 + 1) * byte3 + k1 + 0];
                    double d5 = (noiseField[((i1 + 0) * l + j1 + 0) * byte3 + k1 + 1] - d1) * d;
                    double d6 = (noiseField[((i1 + 0) * l + j1 + 1) * byte3 + k1 + 1] - d2) * d;
                    double d7 = (noiseField[((i1 + 1) * l + j1 + 0) * byte3 + k1 + 1] - d3) * d;
                    double d8 = (noiseField[((i1 + 1) * l + j1 + 1) * byte3 + k1 + 1] - d4) * d;

                    for (int l1 = 0; l1 < 8; ++l1)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 4; ++i2)
                        {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            short s = 128;
                            j2 -= s;
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * d14;
                            double d16 = d10 - d15;

                            for (int k2 = 0; k2 < 4; ++k2)
                            {
                                if ((d16 += d15) > 0.0D)
                                {
                                    byteArray[j2 += s] = (byte)mod_LionKing.pridestone.blockID;
                                }
                                else if (k1 * 8 + l1 < byte2)
                                {
                                    byteArray[j2 += s] = (byte)Block.waterStill.blockID;
                                }
                                else
                                {
                                    byteArray[j2 += s] = 0;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBlocksForBiome(int i, int j, byte[] byteArray, BiomeGenBase[] biomeArray)
    {
        byte byte0 = 63;
        double d = 0.03125D;
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0, 16, 16, 1, d * 2.0D, d * 2.0D, d * 2.0D);
        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = biomeArray[l + k * 16];
                int i1 = (int)(stoneNoise[k + l * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = biomegenbase.topBlock;
                byte byte2 = biomegenbase.fillerBlock;
                for (int k1 = 127; k1 >= 0; --k1)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if (k1 <= 0 + rand.nextInt(5))
                    {
                        byteArray[l1] = (byte)Block.bedrock.blockID;
                    }
                    else
                    {
                        byte byte3 = byteArray[l1];
                        if (byte3 == 0)
                        {
                            j1 = -1;
                        }
                        else if (byte3 == (byte)mod_LionKing.pridestone.blockID)
                        {
                            if (j1 == -1)
                            {
                                if (i1 <= 0)
                                {
                                    byte1 = 0;
                                    byte2 = (byte)mod_LionKing.pridestone.blockID;
                                }
                                else if (k1 >= byte0 - 4 && k1 <= byte0 + 1)
                                {
                                    byte1 = biomegenbase.topBlock;
                                    byte2 = biomegenbase.fillerBlock;
                                }
                                if (k1 < byte0 && byte1 == 0)
                                {
									byte1 = (byte)Block.waterStill.blockID;
                                }
                                j1 = i1;
                                if (k1 >= byte0 - 1)
                                {
                                    byteArray[l1] = byte1;
                                }
                                else
                                {
                                    byteArray[l1] = byte2;
                                }
                            }
                            else if (j1 > 0)
                            {
                                --j1;
                                byteArray[l1] = byte2;
                            }
                        }
                    }
                }
				if (biomegenbase instanceof LKBiomeGenRainforest)
				{
					for (int k2 = 48; k2 < 64; k2++)
					{
						int l1 = (l * 16 + k) * 128 + k2;
						if (byteArray[l1] == (byte)Block.dirt.blockID && byteArray[l1 + 1] == (byte)Block.waterStill.blockID)
						{
							byteArray[l1] = (byte)Block.sand.blockID;
						}
					}
				}
			}
		}
        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
				if (biomeArray[l + k * 16] instanceof LKBiomeGenRainforest)
				{
					int l1 = (l * 16 + k) * 128 + 61;
					if (byteArray[l1] == (byte)Block.sand.blockID && byteArray[l1 + 1] == (byte)Block.waterStill.blockID)
					{
						for (int i3 = k - 3; i3 <= k + 3; i3++)
						{
							for(int j3 = 60; j3 < 65; j3++)
							{
								for (int k3 = l - 3; k3 <= l + 3; k3++)
								{
									if (i3 < 0 || i3 > 15 || k3 < 0 || k3 > 15)
									{
										continue;
									}
									if ((i3 == k - 3 && k3 == l - 3) || (i3 == k + 3 && k3 == l - 3))
									{
										continue;
									}
									if ((i3 == k - 3 && k3 == l + 3) || (i3 == k + 3 && k3 == l + 3))
									{
										continue;
									}
									int l2 = (k3 * 16 + i3) * 128 + j3;
									if (byteArray[l2] == (byte)Block.dirt.blockID || byteArray[l2] == (byte)Block.grass.blockID)
									{
										byteArray[l2] = (byte)Block.sand.blockID;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override	
    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

	@Override
    public Chunk provideChunk(int i, int j)
    {
        rand.setSeed((long)i * 341873128712L + (long)j * 132897987541L);
        byte[] blocks = new byte[32768];
        generateTerrain(i, j, blocks);
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        replaceBlocksForBiome(i, j, blocks, biomesForGeneration);
        caveGenerator.generate(this, worldObj, i, j, blocks);
		
        Chunk chunk = new Chunk(worldObj, blocks, i, j);
        byte[] biomes = chunk.getBiomeArray();

        for (int k = 0; k < biomes.length; k++)
        {
            biomes[k] = (byte)biomesForGeneration[k].biomeID;
        }
		
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par1ArrayOfDouble == null)
        {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }
        if (noiseFloatArray == null)
        {
            noiseFloatArray = new float[25];

            for (int var8 = -2; var8 <= 2; ++var8)
            {
                for (int var9 = -2; var9 <= 2; ++var9)
                {
                    float var10 = 10.0F / MathHelper.sqrt_float((float)(var8 * var8 + var9 * var9) + 0.2F);
                    noiseFloatArray[var8 + 2 + (var9 + 2) * 5] = var10;
                }
            }
        }
        double var44 = 684.412D;
        double var45 = 684.412D;
        noise5 = noiseGen5.generateNoiseOctaves(noise5, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
        noise3 = noiseGen3.generateNoiseOctaves(noise3, par2, par3, par4, par5, par6, par7, var44 / 80.0D, var45 / 160.0D, var44 / 80.0D);
        noise1 = noiseGen1.generateNoiseOctaves(noise1, par2, par3, par4, par5, par6, par7, var44, var45, var44);
        noise2 = noiseGen2.generateNoiseOctaves(noise2, par2, par3, par4, par5, par6, par7, var44, var45, var44);
        boolean var43 = false;
        boolean var42 = false;
        int var12 = 0;
        int var13 = 0;

        for (int var14 = 0; var14 < par5; ++var14)
        {
            for (int var15 = 0; var15 < par7; ++var15)
            {
                float var16 = 0.0F;
                float var17 = 0.0F;
                float var18 = 0.0F;
                byte var19 = 2;
                BiomeGenBase var20 = biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];

                for (int var21 = -var19; var21 <= var19; ++var21)
                {
                    for (int var22 = -var19; var22 <= var19; ++var22)
                    {
                        BiomeGenBase var23 = biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
                        float var24 = noiseFloatArray[var21 + 2 + (var22 + 2) * 5] / (var23.minHeight + 2.0F);

                        if (var23.minHeight > var20.minHeight)
                        {
                            var24 /= 2.0F;
                        }

                        var16 += var23.maxHeight * var24;
                        var17 += var23.minHeight * var24;
                        var18 += var24;
                    }
                }
                var16 /= var18;
                var17 /= var18;
                var16 = var16 * 0.9F + 0.1F;
                var17 = (var17 * 4.0F - 1.0F) / 8.0F;
                double var47 = noise6[var13] / 8000.0D;
                if (var47 < 0.0D)
                {
                    var47 = -var47 * 0.3D;
                }
                var47 = var47 * 3.0D - 2.0D;
                if (var47 < 0.0D)
                {
                    var47 /= 2.0D;
                    if (var47 < -1.0D)
                    {
                        var47 = -1.0D;
                    }
                    var47 /= 1.4D;
                    var47 /= 2.0D;
                }
                else
                {
                    if (var47 > 1.0D)
                    {
                        var47 = 1.0D;
                    }
                    var47 /= 8.0D;
                }
                ++var13;
                for (int var46 = 0; var46 < par6; ++var46)
                {
                    double var48 = (double)var17;
                    double var26 = (double)var16;
                    var48 += var47 * 0.2D;
                    var48 = var48 * (double)par6 / 16.0D;
                    double var28 = (double)par6 / 2.0D + var48 * 4.0D;
                    double var30 = 0.0D;
                    double var32 = ((double)var46 - var28) * 12.0D * 128.0D / 128.0D / var26;
                    if (var32 < 0.0D)
                    {
                        var32 *= 4.0D;
                    }
                    double var34 = noise1[var12] / 512.0D;
                    double var36 = noise2[var12] / 512.0D;
                    double var38 = (noise3[var12] / 10.0D + 1.0D) / 2.0D;
                    if (var38 < 0.0D)
                    {
                        var30 = var34;
                    }
                    else if (var38 > 1.0D)
                    {
                        var30 = var36;
                    }
                    else
                    {
                        var30 = var34 + (var36 - var34) * var38;
                    }
                    var30 -= var32;
                    if (var46 > par6 - 4)
                    {
                        double var40 = (double)((float)(var46 - (par6 - 4)) / 3.0F);
                        var30 = var30 * (1.0D - var40) + -10.0D * var40;
                    }
                    par1ArrayOfDouble[var12] = var30;
                    ++var12;
                }
            }
        }
        return par1ArrayOfDouble;
    }

	@Override
    public boolean chunkExists(int i, int j)
    {
        return true;
    }

	@Override
    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
		
		int lakeChance = biomegenbase instanceof LKBiomeGenRainforest ? 3 : biomegenbase instanceof LKBiomeGenAridSavannah ? 15 : 6;
        if (rand.nextInt(lakeChance) == 0 && !(biomegenbase instanceof LKBiomeGenRiver) && !(biomegenbase instanceof LKBiomeGenDesert))
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j2 = rand.nextInt(128);
            int k3 = l + rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, j2, k3);
        }

        if (i == 0 && j == 0)
        {
			new LKWorldGenRafiki().generate(worldObj, rand, 0, 64, 0);
		}
		
		int t = 0;
		int t1 = 0;
		if (worldObj.getSeed() > 0L)
		{
			t = 6;
		}
		else
		{
			t = -6;
		}
		if ((worldObj.getSeed() % 2L) == 0L)
		{
			t1 = 6;
		}
		else
		{
			t1 = -6;
		}
		
		if (i == t && j == t1)
		{
			int i1 = t * 16;
			int j2 = 140;
			int k3 = t1 * 16;
			new LKWorldGenTimonAndPumbaa().generate(worldObj, rand, i1, j2, k3);
		}
		
        for (int i3 = 0; i3 < 10; i3++)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j2 = rand.nextInt(128);
            int k3 = l + rand.nextInt(16) + 8;
            new LKWorldGenDungeons().generate(worldObj, rand, i1, j2, k3);
        }
		
		if (biomegenbase instanceof LKPrideLandsBiome)
		{
			((LKPrideLandsBiome)biomegenbase).decoratePrideLands(worldObj, rand, k, l);
		}
		
		boolean arid = biomegenbase instanceof LKBiomeGenAridSavannah;
		if ((arid && rand.nextBoolean()) || !arid)
		{
			SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, k + 8, l + 8, 16, 16, rand);
		}

        BlockSand.fallInstantly = false;
    }

	@Override
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }
	
	@Override
	public void saveExtraData() {}

	@Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

	@Override
    public boolean canSave()
    {
        return true;
    }

	@Override
    public String makeString()
    {
        return "RandomPrideLandsLevelSource";
    }
	
	@Override
    public List getPossibleCreatures(EnumCreatureType creatureType, int i, int j, int k)
    {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		return biome == null ? null : biome.getSpawnableList(creatureType);
    }

	@Override
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return null;
    }
	
	@Override
    public int getLoadedChunkCount()
    {
        return 0;
    }
	
	@Override
    public void recreateStructures(int i, int j) {}
}
