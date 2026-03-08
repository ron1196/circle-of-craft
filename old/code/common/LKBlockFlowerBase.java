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
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockFlowerBase extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] flowerIcons;
	private String[] flowerNames = {"purpleFlower_base", "redFlower_base"};
	
    public LKBlockFlowerBase(int i)
    {
        super(i, Material.plants);
        setTickRandomly(true);
        float f = 0.2F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
		setCreativeTab(null);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		if (j >= flowerNames.length)
		{
			j = 0;
		}
		return flowerIcons[j];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        flowerIcons = new Icon[flowerNames.length];
        for (int i = 0; i < flowerNames.length; i++)
        {
            flowerIcons[i] = iconregister.registerIcon("lionking:" + flowerNames[i]);
        }
    }

	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return super.canPlaceBlockAt(world, i, j, k) && canThisPlantGrowOnThisBlockID(world.getBlockId(i, j - 1, k));
    }

    private boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return i == Block.grass.blockID || i == Block.dirt.blockID || i == Block.tilledField.blockID;
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        checkFlowerChange(world, i, j, k);
    }

	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        checkFlowerChange(world, i, j, k);
    }

    private void checkFlowerChange(World world, int i, int j, int k)
    {
		if (world.getBlockId(i, j+1, k) != mod_LionKing.flowerTop.blockID || !canBlockStay(world, i, j, k))
		{
			world.setBlockToAir(i, j, k);
			world.setBlockToAir(i, j+1, k);
		}
    }
	
	@Override
	public void breakBlock(World world, int i, int j, int k, int l, int i1)
	{
		dropBlockAsItem_do(world, i, j, k, new ItemStack(world.getBlockMetadata(i, j, k) == 0 ? mod_LionKing.purpleFlower : mod_LionKing.redFlower));
	}

	@Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        checkFlowerChange(world, i, j, k);
    }

	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
		boolean flag = world.getBlockMetadata(i, j, k) == 1 || world.getFullBlockLightValue(i, j, k) >= 8 ? true : false;
        return (flag || world.canBlockSeeTheSky(i, j, k)) && canThisPlantGrowOnThisBlockID(world.getBlockId(i, j - 1, k));
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
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
        return 1;
    }

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return 0;
    }

	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k) == 1 ? mod_LionKing.redFlower.itemID : mod_LionKing.purpleFlower.itemID;
    }
}
