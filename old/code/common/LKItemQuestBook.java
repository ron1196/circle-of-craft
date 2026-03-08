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

public class LKItemQuestBook extends LKItem
{
	public LKItemQuestBook(int i)
	{
		super(i);
		setMaxStackSize(1);
		setCreativeTab(LKCreativeTabs.tabQuest);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!world.isRemote)
		{
			entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_QUESTS, world, 0, 0, 0);
		}
		return itemstack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
    {
		if (LKQuestBase.anyUncheckedQuests())
		{
			list.add("New quests available");
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemstack, int pass)
	{
		return itemstack.getItemDamage() == 0 && LKQuestBase.anyUncheckedQuests();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack)
    {
		return EnumRarity.uncommon;
    }
}
