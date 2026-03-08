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

public class LKBlockOre extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon nukaIcon;
	@SideOnly(Side.CLIENT)
	private Icon kivuliteIcon;
	
    public LKBlockOre(int i)
    {
        super(i, Material.rock);
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return blockID == mod_LionKing.prideCoal.blockID ? (i == 0 ? Item.coal.itemID : mod_LionKing.nukaShard.itemID) : blockID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		if (blockID == mod_LionKing.prideCoal.blockID && j == 1)
		{
			return nukaIcon;
		}
		if (blockID == mod_LionKing.oreSilver.blockID && j == 1)
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
		if (blockID == mod_LionKing.prideCoal.blockID)
		{
			nukaIcon = iconregister.registerIcon("lionking:oreNuka");
		}
		if (blockID == mod_LionKing.oreSilver.blockID)
		{
			kivuliteIcon = iconregister.registerIcon("lionking:oreKivulite");
		}
	}
	
	@Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
    {
        super.dropBlockAsItemWithChance(world, i, j, k, meta, f, fortune);
		int amount = 0;
		
		if (blockID == mod_LionKing.prideCoal.blockID && meta == 0)
		{
			amount = MathHelper.getRandomIntegerInRange(world.rand, 0, 2);
		}
		else if (blockID == mod_LionKing.prideCoal.blockID && meta == 1)
		{
			amount = MathHelper.getRandomIntegerInRange(world.rand, 0, 1);
		}
		
		if (amount > 0)
		{
			dropXpOnBlockBreak(world, i, j, k, amount);
		}
    }
	
	@Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
		if (blockID == mod_LionKing.prideCoal.blockID && l == 1 && !EnchantmentHelper.getSilkTouchModifier(entityplayer))
		{
			Random random = world.rand;
			int i1 = random.nextInt(10) == 0 ? 1 : (random.nextInt(3) == 0 ? 2 : 3);
			for (int j1 = 0; j1 < i1; j1++)
			{
				super.harvestBlock(world, entityplayer, i, j, k, l);
			}
		}
		else
		{
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}
    }
	
	@Override
    public int damageDropped(int i)
    {
        if (blockID == mod_LionKing.oreSilver.blockID)
		{
			return i;
		}
		return 0;
    }
	
	@Override
    public int quantityDroppedWithBonus(int i, Random random)
    {
        if (i > 0 && blockID == mod_LionKing.prideCoal.blockID)
        {
            int j = random.nextInt(i + 2) - 1;
            if (j < 0)
            {
                j = 0;
            }
            return quantityDropped(random) * (j + 1);
        }
        else
        {
            return quantityDropped(random);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int i, CreativeTabs tab, List list)
    {
		if (i == mod_LionKing.prideCoal.blockID || i == mod_LionKing.oreSilver.blockID)
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
    public int getDamageValue(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k);
    }
}
