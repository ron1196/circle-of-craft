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
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockTermite extends LKBlock
{
    public LKBlockTermite(int i)
    {
        super(i, Material.ground);
    }

	@Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        if (l == 0 && !world.isRemote && world.rand.nextBoolean())
        {
			int i1 = world.rand.nextInt(3);
			for (int i2 = 0; i2 < i1; i2++)
			{
            	spawnTermite(world, i, j, k);
			}
        }
    }
	
	@Override
	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
	{
		if (meta == 0 && !world.isRemote && world.rand.nextBoolean())
		{
			spawnTermite(world, i, j, k);
		}
		super.dropBlockAsItemWithChance(world, i, j, k, meta, f, fortune);
	}
	
	private void spawnTermite(World world, int i, int j, int k)
	{
		LKEntityTermite termite = new LKEntityTermite(world);
		termite.setLocationAndAngles((double)i + 0.5D, j, (double)k + 0.5D, 0.0F, 0.0F);
		world.spawnEntityInWorld(termite);
	}
	
	@Override
    public int damageDropped(int i)
    {
		return i == 1 ? 1 : 0;
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
    	return meta == 1 ? 1 : 0;
    }

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return i == 1 ? blockID : 0;
    }
	
	@Override
    protected ItemStack createStackedBlock(int i)
	{
		return new ItemStack(this, 1, 1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		for (int j = 0; j < 2; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
}
