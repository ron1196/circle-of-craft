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
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

import java.util.List;
import java.util.Random;

public class LKChunkProviderUpendi
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
    private double noiseField[];
    private double stoneNoise[] = new double[256];
    double noise3[];
    double noise1[];
    double noise2[];
    double noise5[];
    double noise6[];
    float[] noiseFloatArray;
    int unusedIntArray32x32[][] = new int[32][32];

    public LKChunkProviderUpendi(World world, long l)
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

    private void generateTerrain(int i, int j, byte[] byteArray)
    {
        byte byte0 = 4;
        byte byte1 = 16;
        byte byte2 = 63;
        int k = byte0 + 1;
        byte byte3 = 17;
        int l = byte0 + 1;
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

    private void replaceBlocksForBiome(int i, int j, byte abyte0[])
    {
        int k = 63;
        double d = 0.03125D;
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for (int l = 0; l < 16; l++)
        {
            for (int i1 = 0; i1 < 16; i1++)
            {
                int j1 = (int)(stoneNoise[l + i1 * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int k1 = -1;
                byte byte0 = (byte)Block.grass.blockID;
                byte byte1 = (byte)Block.dirt.blockID;
                for (int l1 = 127; l1 >= 0; l1--)
                {
                    int i2 = (i1 * 16 + l) * 128 + l1;
                    if (l1 <= 0 + rand.nextInt(5))
                    {
                        abyte0[i2] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    byte byte2 = abyte0[i2];
                    if (byte2 == 0)
                    {
                        k1 = -1;
                        continue;
                    }
                    if (byte2 != (byte)mod_LionKing.pridestone.blockID)
                    {
                        continue;
                    }
                    if (k1 == -1)
                    {
                        if (j1 <= 0)
                        {
                            byte0 = 0;
                            byte1 = (byte)mod_LionKing.pridestone.blockID;
                        }
						else if (l1 >= k - 4 && l1 <= k + 1)
                        {
                            byte0 = (byte)Block.grass.blockID;
                            byte1 = (byte)Block.dirt.blockID;
                        }
                        if (l1 < k && byte0 == 0)
                        {
                            byte0 = (byte)Block.waterStill.blockID;
                        }
                        k1 = j1;
                        if (l1 >= k - 1)
                        {
                            abyte0[i2] = byte0;
                        }
						else
                        {
                            abyte0[i2] = byte1;
                        }
                        continue;
                    }
                    if (k1 <= 0)
                    {
                        continue;
                    }
                    k1--;
                    abyte0[i2] = byte1;
                }
				
				for (int k2 = 44; k2 < 66; k2++)
				{
					int l1 = (i1 * 16 + l) * 128 + k2;
					if (abyte0[l1] == (byte)Block.dirt.blockID || abyte0[l1] == (byte)Block.grass.blockID)
					{
						abyte0[l1] = (byte)Block.sand.blockID;
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
        replaceBlocksForBiome(i, j, blocks);
        Chunk chunk = new Chunk(worldObj, blocks, i, j);
        byte[] biomes = chunk.getBiomeArray();
        for (int k = 0; k < biomes.length; k++)
        {
            biomes[k] = (byte)LKBiomeGenUpendi.upendi.biomeID;
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
                float var16 = 1.0F;
                float var17 = 0.1F;
                float var18 = 0.4F;
                byte var19 = 2;
                BiomeGenBase var20 = LKBiomeGenUpendi.upendi;

                for (int var21 = -var19; var21 <= var19; ++var21)
                {
                    for (int var22 = -var19; var22 <= var19; ++var22)
                    {
                        BiomeGenBase var23 = LKBiomeGenUpendi.upendi;
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
        boolean flag = false;

        if (rand.nextInt(10) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j2 = rand.nextInt(128);
            int k3 = l + rand.nextInt(16) + 8;
            new WorldGenLakes(Block.waterStill.blockID).generate(worldObj, rand, i1, j2, k3);
        }
		
		if (biomegenbase instanceof LKBiomeGenUpendi)
		{
			((LKBiomeGenUpendi)biomegenbase).decorateUpendi(worldObj, rand, k, l);
		}
		SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, k + 8, l + 8, 16, 16, rand);

        BlockSand.fallInstantly = false;
    }

	@Override
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
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
        return "RandomUpendiLevelSource";
    }

	@Override
    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();
        if (worldchunkmanager == null)
        {
            return null;
        }
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(i, k);
        if (biomegenbase == null)
        {
            return null;
        }
		else
        {
            return biomegenbase.getSpawnableList(enumcreaturetype);
        }
    }

	@Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
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
