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

public class LKWorldGenLiquids extends WorldGenerator
{
    private int liquidBlockId;

    public LKWorldGenLiquids(int i)
    {
        liquidBlockId = i;
    }

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if (world.getBlockId(i, j + 1, k) != mod_LionKing.pridestone.blockID)
        {
            return false;
        }
        if (world.getBlockId(i, j - 1, k) != mod_LionKing.pridestone.blockID)
        {
            return false;
        }
        if (world.getBlockId(i, j, k) != 0 && world.getBlockId(i, j, k) != mod_LionKing.pridestone.blockID)
        {
            return false;
        }
        int l = 0;
        if (world.getBlockId(i - 1, j, k) == mod_LionKing.pridestone.blockID)
        {
            l++;
        }
        if (world.getBlockId(i + 1, j, k) == mod_LionKing.pridestone.blockID)
        {
            l++;
        }
        if (world.getBlockId(i, j, k - 1) == mod_LionKing.pridestone.blockID)
        {
            l++;
        }
        if (world.getBlockId(i, j, k + 1) == mod_LionKing.pridestone.blockID)
        {
            l++;
        }
        int i1 = 0;
        if (world.isAirBlock(i - 1, j, k))
        {
            i1++;
        }
        if (world.isAirBlock(i + 1, j, k))
        {
            i1++;
        }
        if (world.isAirBlock(i, j, k - 1))
        {
            i1++;
        }
        if (world.isAirBlock(i, j, k + 1))
        {
            i1++;
        }
        if (l == 3 && i1 == 1)
        {
            world.setBlock(i, j, k, liquidBlockId, 0, 2);
            world.scheduledUpdatesAreImmediate = true;
            Block.blocksList[liquidBlockId].updateTick(world, i, j, k, random);
            world.scheduledUpdatesAreImmediate = false;
        }
        return true;
    }
}
