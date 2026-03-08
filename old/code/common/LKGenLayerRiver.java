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

public class LKGenLayerRiver extends GenLayer
{
	private BiomeGenBase riverBiome;
	
    public LKGenLayerRiver(long par1, GenLayer par3GenLayer, int i)
    {
        super(par1);
        super.parent = par3GenLayer;
		riverBiome = i == 0 ? LKPrideLandsBiome.river : LKOutlandsBiome.outlandsRiver;
    }

	@Override
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int var5 = par1 - 1;
        int var6 = par2 - 1;
        int var7 = par3 + 2;
        int var8 = par4 + 2;
        int[] var9 = parent.getInts(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(par3 * par4);

        for (int var11 = 0; var11 < par4; ++var11)
        {
            for (int var12 = 0; var12 < par3; ++var12)
            {
                int var13 = var9[var12 + 0 + (var11 + 1) * var7];
                int var14 = var9[var12 + 2 + (var11 + 1) * var7];
                int var15 = var9[var12 + 1 + (var11 + 0) * var7];
                int var16 = var9[var12 + 1 + (var11 + 2) * var7];
                int var17 = var9[var12 + 1 + (var11 + 1) * var7];

                if (var17 != 0 && var13 != 0 && var14 != 0 && var15 != 0 && var16 != 0)
                {
                    if (var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16)
                    {
                        var10[var12 + var11 * par3] = -1;
                    }
                    else
                    {
                        var10[var12 + var11 * par3] = riverBiome.biomeID;
                    }
                }
                else
                {
                    var10[var12 + var11 * par3] = riverBiome.biomeID;
                }
            }
        }

        return var10;
    }
}
