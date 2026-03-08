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

import java.util.List;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.ForgeDirection;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockWoodSlab extends BlockHalfSlab
{
    public LKBlockWoodSlab(int i, boolean flag)
    {
        super(i, flag, Material.wood);
        setCreativeTab(LKCreativeTabs.tabBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		int k = j & 7;
		return mod_LionKing.planks.getIcon(i, k);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {}

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.woodSlabSingle.blockID;
    }

	@Override
    protected ItemStack createStackedBlock(int i)
    {
        return new ItemStack(mod_LionKing.woodSlabSingle, 2, i & 7);
    }

	@Override
    public String getFullSlabName(int i)
    {
        return super.getUnlocalizedName() + "." + i;
    }
	
	public static String getSlabName(int i)
	{
		if (i == 0)
		{
			return "Acacia Wood Slab";
		}
		if (i == 1)
		{
			return "Rainforest Wood Slab";
		}
		if (i == 2)
		{
			return "Mango Wood Slab";
		}
		if (i == 3)
		{
			return "Passion Wood Slab";
		}
		if (i == 4)
		{
			return "Banana Wood Slab";
		}
		if (i == 5)
		{
			return "Deadwood Slab";
		}
		return "";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int l)
    {
        if (blockID == mod_LionKing.woodSlabDouble.blockID)
        {
            return super.shouldSideBeRendered(world, i, j, k, l);
        }
        else if (l != 1 && l != 0 && !super.shouldSideBeRendered(world, i, j, k, l))
        {
            return false;
        }
        else
        {
            int i1 = i + Facing.offsetsXForSide[Facing.oppositeSide[l]];
            int j1 = j + Facing.offsetsYForSide[Facing.oppositeSide[l]];
            int k1 = k + Facing.offsetsZForSide[Facing.oppositeSide[l]];
            boolean flag = (world.getBlockMetadata(i1, j1, k1) & 8) != 0;
            return flag ? (l == 0 ? true : (l == 1 && super.shouldSideBeRendered(world, i, j, k, l) ? true : world.getBlockId(i, j, k) != mod_LionKing.woodSlabSingle.blockID || (world.getBlockMetadata(i, j, k) & 8) == 0)) : (l == 1 ? true : (l == 0 && super.shouldSideBeRendered(world, i, j, k, l) ? true : world.getBlockId(i, j, k) != mod_LionKing.woodSlabSingle.blockID || (world.getBlockMetadata(i, j, k) & 8) != 0));
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
        if (i != mod_LionKing.woodSlabDouble.blockID)
        {
            for (int j = 0; j <= 5; j++)
            {
                list.add(new ItemStack(i, 1, j));
            }
        }
    }

	@Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) 
    {
        int meta = world.getBlockMetadata(x, y, z);
        return (((meta & 8) == 8) || isOpaqueCube());
    }
	
	@Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) 
    {
        return (((world.getBlockMetadata(x, y, z) & 8) == 8 && (side.ordinal() == 1)) || isOpaqueCube());
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.woodSlabSingle.blockID;
    }
}
