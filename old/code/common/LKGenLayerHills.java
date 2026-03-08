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

public class LKGenLayerHills extends GenLayer
{
    public LKGenLayerHills(long l, GenLayer layer)
    {
        super(l);
        this.parent = layer;
    }

	@Override
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] var5 = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] var6 = IntCache.getIntCache(par3 * par4);

        for (int var7 = 0; var7 < par4; ++var7)
        {
            for (int var8 = 0; var8 < par3; ++var8)
            {
                this.initChunkSeed((long)(var8 + par1), (long)(var7 + par2));
                int var9 = var5[var8 + 1 + (var7 + 1) * (par3 + 2)];

                if (this.nextInt(3) == 0)
                {
                    int var10 = var9;

                    if (var9 == LKPrideLandsBiome.rainforest.biomeID)
                    {
                        var10 = LKPrideLandsBiome.rainforestHills.biomeID;
                    }
                    if (var9 == LKOutlandsBiome.outlands.biomeID)
                    {
                        var10 = LKOutlandsBiome.outlandsMountains.biomeID;
					}

                    if (var10 != var9)
                    {
                        int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (par3 + 2)];
                        int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (par3 + 2)];
                        int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (par3 + 2)];
                        int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (par3 + 2)];

                        if (var11 == var9 && var12 == var9 && var13 == var9 && var14 == var9)
                        {
                            var6[var8 + var7 * par3] = var10;
                        }
                        else
                        {
                            var6[var8 + var7 * par3] = var9;
                        }
                    }
                    else
                    {
                        var6[var8 + var7 * par3] = var9;
                    }
                }
                else
                {
                    var6[var8 + var7 * par3] = var9;
                }
            }
        }

        return var6;
    }
}
