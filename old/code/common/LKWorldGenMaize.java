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

public class LKWorldGenMaize extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < 20; ++l)
        {
            int i1 = i + random.nextInt(4) - random.nextInt(4);
            int j1 = j;
            int k1 = k + random.nextInt(4) - random.nextInt(4);
            if (world.isAirBlock(i1, j, k1) && (world.getBlockMaterial(i1 - 1, j - 1, k1) == Material.water || world.getBlockMaterial(i1 + 1, j - 1, k1) == Material.water || world.getBlockMaterial(i1, j - 1, k1 - 1) == Material.water || world.getBlockMaterial(i1, j - 1, k1 + 1) == Material.water))
            {
                int l1 = 2 + random.nextInt(random.nextInt(3) + 1);
                for (int l2 = 0; l2 < l1; ++l2)
                {
					int i2 = l2 == 0 ? 0 : 1;
                    if (mod_LionKing.maize.canBlockStay(world, i1, j1 + l2, k1))
                    {
                        world.setBlock(i1, j1 + l2, k1, mod_LionKing.maize.blockID, i2, 2);
                    }
                }
            }
        }
        return true;
    }
}
