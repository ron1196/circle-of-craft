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

public class LKBlockOreStorage extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon kivuliteIcon;
	
    public LKBlockOreStorage(int i)
    {
        super(i, Material.iron);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		if (blockID == mod_LionKing.blockSilver.blockID && j == 1)
		{
			return kivuliteIcon;
		}
		return super.getIcon(i, j);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        super.registerIcons(iconregister);
		if (blockID == mod_LionKing.blockSilver.blockID)
		{
			kivuliteIcon = iconregister.registerIcon("lionking:blockKivulite");
		}
    }
	
	@Override
    public int damageDropped(int i)
    {
        if (blockID == mod_LionKing.blockSilver.blockID)
		{
			return i;
		}
		return 0;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		if (i == mod_LionKing.blockSilver.blockID)
		{
			for (int j = 0; j < 2; j++)
			{
				list.add(new ItemStack(i, 1, j));
			}
		}
		else
		{
			super.getSubBlocks(i, tab, list);
		}
    }
	
	@Override
    public boolean isBeaconBase(World world, int i, int j, int k, int beaconX, int beaconY, int beaconZ)
    {
        return true;
    }
}
