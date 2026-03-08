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
import net.minecraftforge.common.*;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockTilledSand extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] tilledSandIcons;
	
    public LKBlockTilledSand(int i)
    {
        super(i, Material.sand);
        setTickRandomly(true);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        setLightOpacity(255);
		setCreativeTab(null);
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getAABBPool().getAABB((double)(i + 0), (double)(j + 0), (double)(k + 0), (double)(i + 1), (double)(j + 1), (double)(k + 1));
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
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		if (i == 1)
		{
			return j > 0 ? tilledSandIcons[1] : tilledSandIcons[0];
		}
		else
		{
			return Block.sand.getIcon(i, 0);
		}
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        tilledSandIcons = new Icon[3];
		tilledSandIcons[0] = iconregister.registerIcon("lionking:tilledSand");
        tilledSandIcons[1] = iconregister.registerIcon("lionking:tilledSand_hydrated");
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return Block.sand.blockID;
    }

	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
		tryToFall(world, i, j, k);
        if (!isWaterNearby(world, i, j, k) && !world.canLightningStrikeAt(i, j + 1, k))
        {
            int l = world.getBlockMetadata(i, j, k);

            if (l > 0)
            {
                world.setBlockMetadataWithNotify(i, j, k, l - 1, 3);
            }
            else if (!isCropsNearby(world, i, j, k))
            {
                world.setBlock(i, j, k, Block.sand.blockID, 0, 3);
            }
        }
        else
        {
            world.setBlockMetadataWithNotify(i, j, k, 7, 3);
        }
    }

	@Override
    public void onFallenUpon(World world, int i, int j, int k, Entity entity, float f)
    {
        if (world.rand.nextFloat() < f - 0.5F)
        {
            world.setBlock(i, j, k, Block.sand.blockID, 0, 3);
        }
    }

    private boolean isCropsNearby(World world, int i, int j, int k)
    {
        for (int i1 = i; i1 <= i; i1++)
        {
            for (int k1 = k; k1 <= k; k1++)
            {
                int var8 = world.getBlockId(i1, j + 1, k1);

                if (var8 == mod_LionKing.kiwanoStem.blockID)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWaterNearby(World world, int i, int j, int k)
    {
        for (int i1 = i - 4; i1 <= i + 4; ++i1)
        {
            for (int j1 = j; j1 <= j + 1; ++j1)
            {
                for (int k1 = k - 4; k1 <= k + 4; ++k1)
                {
                    if (world.getBlockMaterial(i1, j1, k1) == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
	
	@Override
	public boolean canSustainPlant(World world, int i, int j, int k, ForgeDirection direction, IPlantable plant)
	{
		return plant.getPlantID(world, i, j + 1, k) == mod_LionKing.kiwanoStem.blockID;
	}

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
		tryToFall(world, i, j, k);
        Material material = world.getBlockMaterial(i, j + 1, k);

        if (material.isSolid())
        {
            world.setBlock(i, j, k, Block.sand.blockID, 0, 3);
        }
    }
	
	@Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        tryToFall(world, i, j, k);
    }
	
	private void tryToFall(World world, int i, int j, int k)
    {
        if (BlockSand.canFallBelow(world, i, j - 1, k) && j >= 0)
        {
            byte var8 = 32;

            if (world.checkChunksExist(i - 32, j - 32, k - 32, i + 32, j + 32, k + 32))
            {
                if (!world.isRemote)
                {
					world.setBlock(i, j, k, Block.sand.blockID, 0, 3);
                }
            }
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return Block.sand.blockID;
    }
}
