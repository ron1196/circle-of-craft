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

public class LKWorldGenMangoTrees extends WorldGenerator
{
    public LKWorldGenMangoTrees(boolean flag)
    {
        super(flag);
    }

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int l = random.nextInt(4) + 4;
        boolean flag = true;
        if (j < 1 || j + l + 1 > 256)
        {
            return false;
        }
        for (int i1 = j; i1 <= j + 1 + l; i1++)
        {
            byte byte0 = 1;
            if (i1 == j)
            {
                byte0 = 0;
            }
            if (i1 >= (j + 1 + l) - 2)
            {
                byte0 = 2;
            }
            for (int i2 = i - byte0; i2 <= i + byte0 && flag; i2++)
            {
                for (int l2 = k - byte0; l2 <= k + byte0 && flag; l2++)
                {
                    if (i1 >= 0 && i1 < 256)
                    {
                        int j3 = world.getBlockId(i2, i1, l2);
                        if (j3 != 0 && j3 != mod_LionKing.mangoLeaves.blockID)
                        {
                            flag = false;
                        }
                    }
					else
                    {
                        flag = false;
                    }
                }

            }

        }

        if (!flag)
        {
            return false;
        }
        int j1 = world.getBlockId(i, j - 1, k);
        if (j1 != Block.grass.blockID && j1 != Block.dirt.blockID || j >= 256 - l - 1)
        {
            return false;
        }
        world.setBlock(i, j - 1, k, Block.dirt.blockID, 0, 2);
        for (int k1 = (j - 3) + l; k1 <= j + l; k1++)
        {
            int j2 = k1 - (j + l);
            int i3 = 1 - j2 / 2;
            for (int k3 = i - i3; k3 <= i + i3; k3++)
            {
                int l3 = k3 - i;
                for (int i4 = k - i3; i4 <= k + i3; i4++)
                {
                    int j4 = i4 - k;
                    if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && !Block.opaqueCubeLookup[world.getBlockId(k3, k1, i4)])
                    {
                        setBlockAndMetadata(world, k3, k1, i4, mod_LionKing.mangoLeaves.blockID, 0);
                    }
                }

            }

        }

        for (int l1 = 0; l1 < l; l1++)
        {
            int k2 = world.getBlockId(i, j + l1, k);
            if (k2 == 0 || k2 == mod_LionKing.mangoLeaves.blockID)
            {
                setBlockAndMetadata(world, i, j + l1, k, mod_LionKing.prideWood.blockID, 2);
            }
        }

        return true;
    }
}
