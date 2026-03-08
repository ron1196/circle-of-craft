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

public class LKBlockRafikiWood extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] treeIcons;
	@SideOnly(Side.CLIENT)
	private Icon[] corruptTreeIcons;
	@SideOnly(Side.CLIENT)
	private Icon[] christmasIcons;
	private String[] woodTypes = {"side", "log", "top"};
	
    public LKBlockRafikiWood(int i)
    {
        super(i, Material.wood);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		for (int j = 0; j < 3; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
	
	@Override
	public int damageDropped(int i)
	{
		return i;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        treeIcons = new Icon[3];
		corruptTreeIcons = new Icon[3];
		christmasIcons = new Icon[2];
		for (int i = 0; i < 3; i++)
		{
			treeIcons[i] = iconregister.registerIcon("lionking:rafikiWood_" + woodTypes[i]);
			corruptTreeIcons[i] = iconregister.registerIcon("lionking:rafikiWood_" + woodTypes[i] + "_corrupt");
		}
		christmasIcons[0] = iconregister.registerIcon("lionking:rafikiWood_christmas_top");
        christmasIcons[1] = iconregister.registerIcon("lionking:rafikiWood_christmas_side");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		boolean ziraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
		Icon[] icons = ziraOccupied ? corruptTreeIcons : treeIcons;
		if (j == 1)
		{
			if (i == 0 || i == 1)
			{
				return icons[1];
			}
		}
		if (j == 2)
		{
			return icons[2];
		}
		return icons[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int i, int j, int k, int side)
	{
		int meta = world.getBlockMetadata(i, j, k);
		boolean ziraOccupied = LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19;
		Icon[] icons = ziraOccupied ? corruptTreeIcons : treeIcons;
		if (meta == 1)
		{
			if (side == 0 || side == 1)
			{
				return icons[1];
			}
		}
		if (meta == 2)
		{
			if (ziraOccupied)
			{
				return icons[2];
			}
			if (LKIngame.isChristmas())
			{
				if (side == 1)
				{
					return christmasIcons[1];
				}
				if (side > 1 && !world.isBlockOpaqueCube(i, j + 1, k))
				{
					return christmasIcons[0];
				}
			}
			return icons[2];
		}
		return icons[0];
	}
	
	@Override
    public int getMobilityFlag()
    {
        return 2;
    }
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int i, int j, int k)
	{
		return false;
	}
}
