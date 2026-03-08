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

public class LKItemDartQuiver extends LKItem
{
	public LKItemDartQuiver(int i)
	{
		super(i);
		setMaxStackSize(1);
		setCreativeTab(LKCreativeTabs.tabCombat);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!world.isRemote)
		{
			entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_QUIVER, world, itemstack.getItemDamage(), 0, 0);
		}
		return itemstack;
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
		if (!world.isRemote)
		{
			return;
		}

		String s = "lk.quiver_" + itemstack.getItemDamage();
		LKInventoryQuiver inv = (LKInventoryQuiver)world.loadItemData(LKInventoryQuiver.class, s);
		if (inv == null)
		{
			inv = new LKInventoryQuiver(s);
			inv.markDirty();
			world.setItemData(s, inv);
		}
	}

	public static LKInventoryQuiver getQuiverInventory(int i, World world)
	{
		String s = "lk.quiver_" + i;
		LKInventoryQuiver inv = (LKInventoryQuiver)world.loadItemData(LKInventoryQuiver.class, s);
		if (inv == null)
		{
			inv = new LKInventoryQuiver(s);
			inv.markDirty();
			world.setItemData(s, inv);
		}
		return inv;
	}
}