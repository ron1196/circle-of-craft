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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockWood extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[][] woodIcons;
	private String[] woodNames = {"acacia", "rainforest", "mango", "passion"};
	
    public LKBlockWood(int i)
    {
        super(i, Material.wood);
    }

	@Override
    public void breakBlock(World world, int i, int j, int k, int i3, int j3)
    {
		if (LKCompatibility.isTimberModInstalled)
		{
			LKCompatibility.timber(world, i, j, k, blockID);
		}
		
        byte byte0 = 4;
        int l = byte0 + 1;
        if (world.checkChunksExist(i - l, j - l, k - l, i + l, j + l, k + l))
        {
            for (int i1 = -byte0; i1 <= byte0; i1++)
            {
                for (int j1 = -byte0; j1 <= byte0; j1++)
                {
                    for (int k1 = -byte0; k1 <= byte0; k1++)
                    {
                        int l1 = world.getBlockId(i + i1, j + j1, k + k1);
                        if (!(Block.blocksList[l1] instanceof LKBlockLeaves))
                        {
                            continue;
                        }
                        int i2 = world.getBlockMetadata(i + i1, j + j1, k + k1);
                        if ((i2 & 8) == 0)
                        {
                            world.setBlockMetadataWithNotify(i + i1, j + j1, k + k1, i2 | 8, 4);
                        }
                    }
                }
            }
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		int j1 = j & 12;
		j &= 3;
		
		if (j >= woodNames.length)
		{
			j = 0;
		}
		
		if ((j1 == 0 && (i == 0 || i == 1)) || (j1 == 4 && (i == 4 || i == 5)) || (j1 == 8 && (i == 2 || i == 3)))
		{
			return woodIcons[j][0];
		}
		return woodIcons[j][1];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        woodIcons = new Icon[woodNames.length][2];
        for (int i = 0; i < woodNames.length; i++)
        {
            woodIcons[i][0] = iconregister.registerIcon("lionking:wood_" + woodNames[i] + "_top");
			woodIcons[i][1] = iconregister.registerIcon("lionking:wood_" + woodNames[i] + "_side");
        }
    }
	
	@Override
    public int getRenderType()
    {
        return 31;
    }
	
	@Override
    public int onBlockPlaced(World world, int i, int j, int k, int side, float f, float f1, float f2, int meta)
    {
        int meta1 = meta & 3;
        byte meta2 = 0;

        switch (side)
        {
            case 0:
            case 1:
                meta2 = 0;
                break;
            case 2:
            case 3:
                meta2 = 8;
                break;
            case 4:
            case 5:
                meta2 = 4;
        }

        return meta1 | meta2;
    }
	
	@Override
    public int damageDropped(int i)
    {
        return i & 3;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 3; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
	
	@Override
    protected ItemStack createStackedBlock(int i)
    {
        return new ItemStack(this, 1, damageDropped(i));
    }
}
