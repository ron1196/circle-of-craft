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

public class LKWorldGenTallFlowers extends WorldGenerator
{
	private int metadata;
	
    public LKWorldGenTallFlowers(boolean flag, int i)
    {
		super(flag);
		metadata = i;
    }

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < 64; l++)
        {
            int i1 = (i + random.nextInt(8)) - random.nextInt(8);
            int j1 = (j + random.nextInt(4)) - random.nextInt(4);
            int k1 = (k + random.nextInt(8)) - random.nextInt(8);
            if (mod_LionKing.flowerBase.canBlockStay(world, i1, j1, k1) && world.isAirBlock(i1, j1, k1) && world.isAirBlock(i1, j1 + 1, k1))
            {
                setBlockAndMetadata(world, i1, j1, k1, mod_LionKing.flowerBase.blockID, metadata);
				setBlockAndMetadata(world, i1, j1 + 1, k1, mod_LionKing.flowerTop.blockID, metadata);
            }
        }

        return true;
    }
}
