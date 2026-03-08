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
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockHangingBanana extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] bananaIcons;
	private String[] bananaSides = {"top", "side", "bottom"};
	
	public LKBlockHangingBanana(int i)
	{
		super(i, Material.plants);
		setTickRandomly(true);
		setCreativeTab(null);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		if (i == 0)
		{
			return bananaIcons[2];
		}
		if (i == 1)
		{
			return bananaIcons[0];
		}
		return bananaIcons[1];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        bananaIcons = new Icon[3];
		for (int i = 0; i < 3; i++)
		{
			bananaIcons[i] = iconregister.registerIcon("lionking:banana_" + bananaSides[i]);
		}
    }
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
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
        int l = world.getBlockMetadata(i, j, k);
		ForgeDirection dir = ForgeDirection.getOrientation(l + 2);
		int id = world.getBlockId(i + dir.offsetX, j, k + dir.offsetZ);
		int meta = world.getBlockMetadata(i + dir.offsetX, j, k + dir.offsetZ);
        return id == mod_LionKing.prideWood2.blockID && BlockLog.limitToValidMetadata(meta) == 0;
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        int dir = world.getBlockMetadata(i, j, k);
        switch (dir)
        {
            case 0:
                setBlockBounds(0.375F, 0.1875F, 0F, 0.625F, 0.9375F, 0.25F);
                break;
            case 1:
                setBlockBounds(0.375F, 0.1875F, 0.75F, 0.625F, 0.9375F, 1F);
                break;
            case 2:
                setBlockBounds(0F, 0.1875F, 0.375F, 0.25F, 0.9375F, 0.625F);
                break;
            case 3:
                setBlockBounds(0.75F, 0.1875F, 0.375F, 1F, 0.9375F, 0.625F);
        }
    }

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.banana.itemID;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.banana.itemID;
    }
}
