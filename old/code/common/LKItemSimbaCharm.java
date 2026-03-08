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
import lionking.client.*;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKItemSimbaCharm extends LKItem
{
	@SideOnly(Side.CLIENT)
	private Icon[] charmIcons;
	private String[] charmTypes = {"active", "inactive"};
	
    public LKItemSimbaCharm(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
		if (i >= charmTypes.length)
		{
			i = 0;
		}
		return charmIcons[i];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		charmIcons = new Icon[charmTypes.length];
		for (int i = 0; i < charmTypes.length; i++)
		{
			charmIcons[i] = iconregister.registerIcon("lionking:charm_" + charmTypes[i]);
		}
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        int i1 = world.getBlockId(i, j, k);
        if (i1 == mod_LionKing.starAltar.blockID && itemstack.getItemDamage() == 1)
		{
			itemstack.stackSize--;
			EntityItem item = new EntityItem(world, (double)i + 0.25D + (double)(itemRand.nextFloat() / 2.0F), (double)j, (double)k + 0.25D + (double)(itemRand.nextFloat() / 2.0F), new ItemStack(this));
			item.delayBeforeCanPickup = 10;
			item.motionX = 0.0D;
			item.motionY = 0.4D + (double)(itemRand.nextFloat() / 10.0F);
			item.motionZ = 0.0D;
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(item);
			}
			
			LKEntityLightning bolt = new LKEntityLightning(entityplayer, world, (double)i, (double)j, (double)k, 0);
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(bolt);
				for (int j1 = 0; j1 < 64; j1++)
				{
					double d = (float)i - 0.5F + world.rand.nextFloat() * 2.0F;
					double d1 = (float)j - 0.5F + world.rand.nextFloat() * 2.0F;
					double d2 = (float)k - 0.5F + world.rand.nextFloat() * 2.0F;
					double d3 = 0.0D;
					double d4 = 0.0D;
					double d5 = 0.0D;
					int k1 = world.rand.nextInt(2) * 2 - 1;
					d3 = ((double)world.rand.nextFloat() - 0.5D) * 0.5D;
					d4 = ((double)world.rand.nextFloat() - 0.5D) * 0.5D;
					d5 = ((double)world.rand.nextFloat() - 0.5D) * 0.5D;
					LKIngame.spawnCustomFX(world, 64 + world.rand.nextInt(4), 16, true, d, d1, d2, d3, d4, d5);
				}
			}
			return true;
		}
		return false;
    }

	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + charmTypes[itemstack.getItemDamage()];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(int i, CreativeTabs tab, List list)
    {    	
		for (int j = 0; j < 2; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
}
