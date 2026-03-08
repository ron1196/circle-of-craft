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

public class LKItemCoin extends LKItem
{
	private byte type;
	
    public LKItemCoin(int i, byte b)
    {
        super(i);
		setMaxStackSize(16);
		type = b;
		setCreativeTab(LKCreativeTabs.tabQuest);
    }
 
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (!world.isRemote && ((type == 0 && entityplayer.dimension == mod_LionKing.idPrideLands) || (type == 1 && entityplayer.dimension == mod_LionKing.idOutlands)))
		{
			world.spawnEntityInWorld(new LKEntityCoin(world, entityplayer, type));
			world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!entityplayer.capabilities.isCreativeMode)
			{
				itemstack.stackSize--;
			}
		}
        return itemstack;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack)
    {
		return EnumRarity.uncommon;
    }
}