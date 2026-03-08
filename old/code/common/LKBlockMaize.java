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

public class LKBlockMaize extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon cornIcon;
	
    public LKBlockMaize(int i)
    {
        super(i, Material.plants);
        float f = 0.375F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTickRandomly(true);
		setCreativeTab(null);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		if (j == 1)
		{
			return cornIcon;
		}
		return super.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        super.registerIcons(iconregister);
		cornIcon = iconregister.registerIcon("lionking:maize_corn");
    }
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
		checkBlockCoordValid(world, i, j, k);
		
		if (LKIngame.isLKWorld(world.provider.dimensionId))
		{
			int l;
			for (l = 1; world.getBlockId(i, j - l, k) == blockID; l++) {}
			
			if (l < 4)
			{
				int growthRate = 8;
				for (int l1 = 1; l1 < 5; l1++)
				{
					if (world.getBlockId(i, j - l1, k) == Block.tilledField.blockID)
					{
						growthRate = world.getBlockMetadata(i, j - l1, k) > 0 ? 2 : 4;
					}
				}
				
				if (random.nextInt(growthRate) == 0)
				{
					if (world.isAirBlock(i, j + 1, k) && random.nextInt(22) == 0)
					{
						world.setBlock(i, j + 1, k, blockID, 1, 3);
					}
					if (world.getBlockMetadata(i, j, k) == 0 && world.getBlockId(i, j - 1, k) == blockID && random.nextInt(25) == 0)
					{
						world.setBlockMetadataWithNotify(i, j, k, 1, 3);
					}
				}
			}
		}
    }
	
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);
        return (l == blockID || l == Block.tilledField.blockID) ? true : (l != Block.grass.blockID && l != Block.dirt.blockID ? false : (world.getBlockMaterial(i - 1, j - 1, k) == Material.water ? true : (world.getBlockMaterial(i + 1, j - 1, k) == Material.water ? true : (world.getBlockMaterial(i, j - 1, k - 1) == Material.water ? true : world.getBlockMaterial(i, j - 1, k + 1) == Material.water))));
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        checkBlockCoordValid(world, i, j, k);
    }

    private void checkBlockCoordValid(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, 0, 0);
            world.setBlockToAir(i, j, k);
        }
    }

	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return canPlaceBlockAt(world, i, j, k);
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.cornStalk.itemID;
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
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
    {
        if (!world.isRemote)
		{
            if (world.getBlockMetadata(i, j, k) == 1)
			{
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
				dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_LionKing.corn));
				if (world.rand.nextInt(4) == 0)
				{
					dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_LionKing.corn));
				}
				return true;
			}
        }
		return false;
    }
	
	@Override
	public void breakBlock(World world, int i, int j, int k, int l, int i1)
	{
        if (!world.isRemote)
		{
            if (world.getBlockMetadata(i, j, k) == 1)
			{
				dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_LionKing.corn));
				if (world.rand.nextInt(4) == 0)
				{
					dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_LionKing.corn));
				}
			}
        }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.cornStalk.itemID;
    }
}
