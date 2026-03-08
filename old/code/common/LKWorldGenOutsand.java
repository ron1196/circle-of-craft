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

public class LKWorldGenOutsand extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int l = random.nextInt(4) + 2;
        for (int i1 = i - l; i1 <= i + l; i1++)
        {
            for (int j1 = k - l; j1 <= k + l; j1++)
            {
                int k1 = i1 - i;
                int l1 = j1 - k;
                if (k1 * k1 + l1 * l1 > l * l)
                {
                    continue;
                }
                for (int i2 = j - 3; i2 <= j + 3; i2++)
                {
                    int j2 = world.getBlockId(i1, i2, j1);
                    if (j2 == Block.sand.blockID)
                    {
                        world.setBlock(i1, i2, j1, mod_LionKing.outsand.blockID, 0, 3);
						if (random.nextInt(8) == 0 && world.isAirBlock(i1, i2, j1))
						{
							world.setBlock(i1, i2, j1, Block.fire.blockID, 0, 3);
						}
                    }
                }

            }

        }

        return true;
    }
	
	public boolean isRadiusClearOfOutsand(World world, int i, int j, int k, int radius)
	{
		for (int i1 = i - radius; i1 <= i + radius; i1++)
		{
			for (int j1 = j - radius; j1 <= j + radius; j1++)
			{
				for (int k1 = k - radius; k1 <= k + radius; k1++)
				{
					if (world.getBlockId(i1, j1, k1) == mod_LionKing.outsand.blockID)
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
