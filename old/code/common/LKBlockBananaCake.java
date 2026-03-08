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
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockBananaCake extends BlockCake
{
	@SideOnly(Side.CLIENT)
	private Icon iconBottom;
	@SideOnly(Side.CLIENT)
	private Icon iconTop;
	@SideOnly(Side.CLIENT)
	private Icon iconSide;
	@SideOnly(Side.CLIENT)
	private Icon iconEaten;
	
	public LKBlockBananaCake(int i)
	{
		super(i);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
        if (i == 0)
		{
			return iconBottom;
		}
        else if (i == 1)
		{
			return iconTop;
		}
		else if (j > 0 && i == 4)
		{
			return iconEaten;
		}
		return iconSide;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        iconBottom = iconregister.registerIcon(getTextureName() + "_bottom");
		iconTop = iconregister.registerIcon(getTextureName() + "_top");
		iconSide = iconregister.registerIcon(getTextureName() + "_side");
		iconEaten = iconregister.registerIcon(getTextureName() + "_eaten");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.bananaCake.itemID;
    }
}
