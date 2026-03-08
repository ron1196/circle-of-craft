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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKItemHyenaHead extends LKItem
{
	@SideOnly(Side.CLIENT)
	private Icon[] headIcons;
	
	public LKItemHyenaHead(int i)
	{
		super(i);
		setCreativeTab(LKCreativeTabs.tabDeco);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
		if (i >= 4)
		{
			i = 0;
		}
		return headIcons[i];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		headIcons = new Icon[4];
		for (int i = 0; i < 4; i++)
		{
			headIcons[i] = iconregister.registerIcon("lionking:hyenaHead_" + i);
		}
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
	{
		if (l == 0 || !world.getBlockMaterial(i, j, k).isSolid())
		{
			return false;
		}

		if (l == 1)
		{
			j++;
		}
		if (l == 2)
		{
			k--;
		}
		if (l == 3)
		{
			k++;
		}
		if (l == 4)
		{
			i--;
		}
		if (l == 5)
		{
			i++;
		}

		if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack) || !mod_LionKing.hyenaHead.canPlaceBlockAt(world, i, j, k))
		{
			return false;
		}

		world.setBlock(i, j, k, mod_LionKing.hyenaHead.blockID, l, 3);

		int rotation = 0;
		if (l == 1)
		{
			rotation = MathHelper.floor_double(entityplayer.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;
		}

		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LKTileEntityHyenaHead)
		{
			((LKTileEntityHyenaHead)tileentity).setHyenaType(itemstack.getItemDamage());
			((LKTileEntityHyenaHead)tileentity).setRotation(rotation);
		}

		itemstack.stackSize--;
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tab, List list)
	{
		for (int j = 0; j < 4; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
	}
}
