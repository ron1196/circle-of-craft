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

public class LKItemGiraffeTie extends LKItem
{
	@SideOnly(Side.CLIENT)
	private Icon[] tieIcons;
	private String[] tieTypes = {"base", "white", "blue", "yellow", "red", "purple", "green", "black"};
	
    public LKItemGiraffeTie(int i)
    {
        super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
		if (i >= tieTypes.length)
		{
			i = 0;
		}
		return tieIcons[i];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		tieIcons = new Icon[tieTypes.length];
		for (int i = 0; i < tieTypes.length; i++)
		{
			tieIcons[i] = iconregister.registerIcon("lionking:tie_" + tieTypes[i]);
		}
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tab, List list)
	{
		for (int j = 0; j < 8; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
	}

	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityplayer, EntityLivingBase entityliving)
    {
        if (entityliving instanceof LKEntityGiraffe)
        {
            LKEntityGiraffe giraffe = (LKEntityGiraffe)entityliving;

            if (giraffe.getTie() == -1 && !giraffe.isChild() && giraffe.getSaddled())
            {
                giraffe.setTie(itemstack.getItemDamage());
                itemstack.stackSize--;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
