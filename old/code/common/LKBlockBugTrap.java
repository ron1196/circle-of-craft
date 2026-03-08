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

import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockBugTrap extends BlockContainer
{
	private Random rand = new Random();
	
	public LKBlockBugTrap(int i)
	{
		super(i, Material.wood);
		setCreativeTab(LKCreativeTabs.tabDeco);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return mod_LionKing.prideWood.getIcon(2, 0);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister) {}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new LKTileEntityBugTrap();
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
    {
        if (!world.isRemote)
        {
			entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_TRAP, world, i, j, k);
		}
		return true;
    }
	
	@Override
    public void breakBlock(World world, int i, int j, int k, int i3, int j3)
    {
        LKTileEntityBugTrap tileentity = (LKTileEntityBugTrap)world.getBlockTileEntity(i, j, k);
        if (tileentity != null)
        {
			label0:
            for (int l = 0; l < tileentity.getSizeInventory(); l++)
            {
                ItemStack itemstack = tileentity.getStackInSlot(l);
                if (itemstack == null)
                {
                    continue;
                }
                float f = rand.nextFloat() * 0.8F + 0.1F;
                float f1 = rand.nextFloat() * 0.8F + 0.1F;
                float f2 = rand.nextFloat() * 0.8F + 0.1F;
                do
                {
                    if (itemstack.stackSize <= 0)
                    {
                        continue label0;
                    }
                    int i1 = rand.nextInt(21) + 10;
                    if (i1 > itemstack.stackSize)
                    {
                        i1 = itemstack.stackSize;
                    }
                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (float)rand.nextGaussian() * f3;
                    entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float)rand.nextGaussian() * f3;
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
                    world.spawnEntityInWorld(entityitem);
                }
				while (true);
            }
		}
        super.breakBlock(world, i, j, k, i3, j3);
    }
	
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return super.canPlaceBlockAt(world, i, j, k) && world.doesBlockHaveSolidTopSurface(i, j - 1, k);
    }
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k))
		{
			world.setBlockToAir(i, j, k);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(this));
		}
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
		return mod_LionKing.proxy.getBugTrapRenderID();
    }
}
