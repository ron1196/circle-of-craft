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
import lionking.client.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockVase extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] vaseIcons;
	@SideOnly(Side.CLIENT)
	private static Icon purpleFlowerIcon;
	
    public LKBlockVase(int i)
    {
        super(i, Material.circuits);
		setCreativeTab(null);
		float f = 0.1875F;
		setBlockBounds(0.0F + f, 0.0F, 0.0F + f, 1.0F - f, 0.75F, 1.0F - f);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return i == 1 ? vaseIcons[1] : (i == 0 ? mod_LionKing.pridestone.getIcon(2, 0) : vaseIcons[0]);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        vaseIcons = new Icon[2];
		vaseIcons[0] = iconregister.registerIcon("lionking:vase_side");
        vaseIcons[1] = iconregister.registerIcon("lionking:vase_top");
		purpleFlowerIcon = iconregister.registerIcon("lionking:vase_purpleFlower");
    }
	
	@SideOnly(Side.CLIENT)
	public static Icon getPlantTextureFromMetadata(int i)
	{
		if (i == 0)
		{
			return mod_LionKing.whiteFlower.getIcon(2, 0);
		}
		if (i == 1)
		{
			return mod_LionKing.blueFlower.getIcon(2, 0);
		}
		if (i == 2)
		{
			return purpleFlowerIcon;
		}
		if (i == 3)
		{
			return mod_LionKing.flowerTop.getIcon(2, 1);
		}
		if (i == 4)
		{
			return mod_LionKing.sapling.getIcon(2, 0);
		}
		if (i == 5)
		{
			return mod_LionKing.forestSapling.getIcon(2, 0);
		}
		if (i == 6)
		{
			return mod_LionKing.mangoSapling.getIcon(2, 0);
		}
		if (i == 7)
		{
			return mod_LionKing.outshroom.getIcon(2, 0);
		}
		if (i == 8)
		{
			return mod_LionKing.outshroomGlowing.getIcon(2, 0);
		}
		if (i == 9)
		{
			return mod_LionKing.passionSapling.getIcon(2, 0);
		}
		if (i == 10)
		{
			return mod_LionKing.bananaSapling.getIcon(2, 0);
		}
		return mod_LionKing.pumbaaBox.getIcon(1, 0);
	}
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public int getRenderType()
    {
		return mod_LionKing.proxy.getVaseRenderID();
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.vase.itemID;
    }
	
	@Override
    public int damageDropped(int i)
    {
        return i;
    }
	
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return world.doesBlockHaveSolidTopSurface(i, j - 1, k) && super.canPlaceBlockAt(world, i, j, k) && world.isAirBlock(i, j + 1, k);
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        checkVaseChange(world, i, j, k);
    }

    protected final void checkVaseChange(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }

	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return world.doesBlockHaveSolidTopSurface(i, j - 1, k) && world.isAirBlock(i, j + 1, k);
    }
	
	@Override
    public int getLightValue(IBlockAccess world, int i, int j, int k) 
	{
		int l = world.getBlockMetadata(i, j, k);
	    if (l == 3 || l == 9)
		{
			return 11;
		}
	    if (l == 8)
		{
			return 13;
		}
		return super.getLightValue(world, i, j, k);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.vase.itemID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if (world.getBlockMetadata(i, j, k) == 9)
		{
			for (int l = 0; l < 4; l++)
			{
				double d = (float)i + random.nextFloat();
				double d1 = (float)j + 0.75F + random.nextFloat();
				double d2 = (float)k + random.nextFloat();
				double d3 = (-0.5F + random.nextFloat()) * 0.01F;
				double d4 = random.nextFloat() * 0.01F;
				double d5 = (-0.5F + random.nextFloat()) * 0.01F;
				mod_LionKing.proxy.spawnParticle("passion", d, d1, d2, d3, d4, d5);
			}
		}
    }
}
