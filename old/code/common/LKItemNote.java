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

public class LKItemNote extends LKItem
{
	@SideOnly(Side.CLIENT)
	private Icon[] noteIcons;
	private String[] noteNames = {"c", "d", "e", "f", "g", "a", "b"};
	
    public LKItemNote(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
		setCreativeTab(LKCreativeTabs.tabMisc);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
		if (i >= noteNames.length)
		{
			i = 0;
		}
		return noteIcons[i];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		noteIcons = new Icon[noteNames.length];
		for (int i = 0; i < noteNames.length; i++)
		{
			noteIcons[i] = iconregister.registerIcon("lionking:note_" + noteNames[i]);
		}
    }
	
	public static int getNoteValue(int damage)
	{
		switch (damage)
		{
			case 0: case 1: default:
				return 1;
			case 2:
				return 2;
			case 3: case 4:
				return 5;
			case 5:
				return 10;
			case 6:
				return 20;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemstack, int pass)
	{
		return true;
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
		for (int j = 0; j < 7; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
}