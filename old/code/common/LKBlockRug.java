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
import java.util.ArrayList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.ForgeDirection;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockRug extends BlockContainer
{
	public static String[] colourNames = {"Default", "White", "Blue", "Yellow", "Red", "Purple", "Light Blue", "Green", "Orange", "Light Grey", "Grey", "Black", "Outlander", "Violet", "Pink", "Light Green"};
	public static String[] colourNames_US = {"Default", "White", "Blue", "Yellow", "Red", "Purple", "Light Blue", "Green", "Orange", "Light Gray", "Gray", "Black", "Outlander", "Violet", "Pink", "Light Green"};
	
	@SideOnly(Side.CLIENT)
	private Icon[] rugIcons;
	private String[] rugTypes = {"white", "blue", "yellow", "red", "purple", "lightBlue", "green", "orange", "lightGrey", "grey", "black", "outlander", "violet", "pink", "lightGreen"};
	
    public LKBlockRug(int i)
    {
        super(i, Material.cloth);
		setBlockBounds(0F, 0F, 0F, 1F, 0.0625F, 1F);
		setCreativeTab(LKCreativeTabs.tabDeco);
    }
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new LKTileEntityFurRug();
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
		int direction = ((LKTileEntityFurRug)world.getBlockTileEntity(i, j, k)).direction;
		float f = 0.0625F;
		switch (direction)
		{
			case 0:
				setBlockBounds(0F, 0F, 0F, 1F, f, 1F);
				break;
			case 1:
				setBlockBounds(0F, 1F - f, 0F, 1F, 1F, 1F);
				break;
			case 2:
				setBlockBounds(0F, 0F, 0F, 1F, 1F, f);
				break;
			case 3:
				setBlockBounds(0F, 0F, 1F - f, 1F, 1F, 1F);
				break;
			case 4:
				setBlockBounds(0F, 0F, 0F, f, 1F, 1F);
				break;
			case 5:
				setBlockBounds(1F - f, 0F, 0F, 1F, 1F, 1F);
		}
    }
	
	@Override
	public void setBlockBoundsForItemRender()
    {
        float f = 0.03125F;
        setBlockBounds(0F, 0.5F - f, 0F, 1F, 0.5F + f, 1F);
    }
	
	@Override
    public int damageDropped(int i)
    {
        return i;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		if (j == 0)
		{
			return super.getIcon(i, j);
		}
		return rugIcons[j - 1];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		super.registerIcons(iconregister);
        rugIcons = new Icon[15];
		for (int i = 0; i < 15; i++)
		{
			rugIcons[i] = iconregister.registerIcon(getTextureName() + "_" + rugTypes[i]);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		for (int j = 0; j < 16; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
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
    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
		ForgeDirection dir = ForgeDirection.getOrientation(l).getOpposite();
        return world.isBlockSolidOnSide(i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, dir.getOpposite());
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
		int direction = ((LKTileEntityFurRug)world.getBlockTileEntity(i, j, k)).direction;
		ForgeDirection dir = ForgeDirection.getOrientation(direction);
        return world.isBlockSolidOnSide(i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, dir.getOpposite());
    }
	
	@Override
    public int getMobilityFlag()
    {
        return 1;
    }
}
