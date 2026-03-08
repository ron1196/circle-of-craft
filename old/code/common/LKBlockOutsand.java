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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.ForgeDirection;

public class LKBlockOutsand extends LKBlock
{
    public LKBlockOutsand(int i)
    {
        super(i, Material.sand);
    }
	
	@Override
	public boolean isFireSource(World world, int i, int j, int k, int metadata, ForgeDirection side)
	{
		return side == ForgeDirection.UP;
	}

	@Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
    }

	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        tryToFall(world, i, j, k);
    }

    private void tryToFall(World world, int i, int j, int k)
    {
        int l = i;
        int i1 = j;
        int j1 = k;
        if (canFallBelow(world, l, i1 - 1, j1) && i1 >= 0)
        {
            byte byte0 = 32;
            if (BlockSand.fallInstantly || !world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0))
            {
                world.setBlockToAir(i, j, k);
                for (; canFallBelow(world, i, j - 1, k) && j > 0; j--) { }
                if (j > 0)
                {
                    world.setBlock(i, j, k, blockID, 0, 3);
                }
            }
            else if (!world.isRemote)
            {
                LKEntityOutsand entity = new LKEntityOutsand(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, blockID);
                world.spawnEntityInWorld(entity);
            }
        }
    }

	@Override
    public int tickRate(World world)
    {
        return 3;
    }

    public static boolean canFallBelow(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);
        if (l == 0)
        {
            return true;
        }
        if (l == Block.fire.blockID)
        {
            return true;
        }
        Material material = Block.blocksList[l].blockMaterial;
        if (material == Material.water)
        {
            return true;
        }
        return material == Material.lava;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        double d = (float)i + random.nextFloat();
        double d1 = (float)j + random.nextFloat();
        double d2 = (float)k + random.nextFloat();
        double d3 = 0.0D;
        double d4 = 0.0D;
        double d5 = 0.0D;
        int i1 = random.nextInt(2) * 2 - 1;
        d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
        d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
        d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
        d = (double)i + 0.5D + 0.25D * (double)i1;
        d3 = random.nextFloat() * 2.0F * (float)i1;
        world.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
    }
	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
		if (entity instanceof EntityLivingBase && !entity.isImmuneToFire() && world.rand.nextBoolean())
		{
			entity.attackEntityFrom(DamageSource.inFire, 2F);
		}
    }
	
	@Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
		if (entity instanceof EntityLivingBase && !entity.isImmuneToFire() && world.rand.nextBoolean())
		{
			entity.attackEntityFrom(DamageSource.inFire, 2F);
		}
    }
}
