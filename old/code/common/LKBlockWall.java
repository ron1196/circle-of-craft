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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

public class LKBlockWall extends BlockWall
{
	public LKBlockWall(int i)
	{
		super(i, mod_LionKing.pridestone);
		setCreativeTab(LKCreativeTabs.tabBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
    {
        if (j == 1)
		{
			return mod_LionKing.prideBrick.getIcon(i, 0);
		}
        if (j == 2)
		{
			return mod_LionKing.prideBrickMossy.getIcon(i, 0);
		}
        if (j == 3)
		{
			return mod_LionKing.pridestone.getIcon(i, 1);
		}
        if (j == 4)
		{
			return mod_LionKing.prideBrick.getIcon(i, 1);
		}
		return mod_LionKing.pridestone.getIcon(i, 0);
    }
	
	@Override
    public float getBlockHardness(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
		if (l == 3 || l == 4)
		{
			return blockHardness * 0.7F;
		}
		return blockHardness;
    }
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		for (int j = 0; j <= 4; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
}
