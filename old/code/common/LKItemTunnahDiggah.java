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

public class LKItemTunnahDiggah extends LKItemPickaxe
{
    public LKItemTunnahDiggah(int i)
    {
        super(i, EnumToolMaterial.IRON);
		setMaxDamage(690);
		setCreativeTab(LKCreativeTabs.tabQuest);
    }
	
	@Override
    public boolean canHarvestBlock(Block block)
    {
        return block.blockMaterial == Material.rock || block.blockID == Block.dirt.blockID || block.blockID == Block.grass.blockID;
    }
	
	private boolean shouldDamageWithLevel(int level)
	{
		return level == 0 ? true : itemRand.nextFloat() >= 0.18F;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, int i, int j, int k, int l, EntityLivingBase entityliving)
    {
		itemstack.damageItem(1, entityliving);
		if (i == Block.dirt.blockID || i == Block.grass.blockID || i == mod_LionKing.pridestone.blockID || i == Block.stone.blockID || i == Block.netherrack.blockID || i == Block.whiteStone.blockID)
		{
			int level = EnchantmentHelper.getEnchantmentLevel(mod_LionKing.diggahEnchantment.effectId, itemstack) + 1;
			for (int j1 = 0 - level; j1 <= level; j1++)
			{
				for (int k1 = 0 - level; k1 <= level; k1++)
				{
					for (int l1 = 0 - level; l1 <= level; l1++)
					{	
						int i1 = world.getBlockId(j + j1, k + k1, l + l1);
						if (i1 == Block.dirt.blockID || i1 == Block.netherrack.blockID || i1 == Block.whiteStone.blockID)
						{
							world.setBlockToAir(j + j1, k + k1, l + l1);
							dropBlock(itemstack, world, j + j1, k + k1, l + l1, Block.blocksList[i1]);
							if(shouldDamageWithLevel(level))
							{
								itemstack.damageItem(1, entityliving);
							}
						}
						if (i1 == mod_LionKing.pridestone.blockID)
						{
							int metadata = world.getBlockMetadata(j + j1, k + k1, l + l1);
							world.setBlockToAir(j + j1, k + k1, l + l1);
							dropBlock(itemstack, world, j + j1, k + k1, l + l1, mod_LionKing.pridestone, metadata);
							if(shouldDamageWithLevel(level))
							{
								itemstack.damageItem(1, entityliving);
							}
						}
						if (i1 == Block.grass.blockID)
						{
							world.setBlockToAir(j + j1, k + k1, l + l1);
							boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemstack) > 0;
							dropBlock(itemstack, world, j + j1, k + k1, l + l1, flag ? Block.grass : Block.dirt);
							if(shouldDamageWithLevel(level))
							{
								itemstack.damageItem(1, entityliving);
							}
						}
						if (i1 == Block.stone.blockID)
						{
							world.setBlockToAir(j + j1, k + k1, l + l1);
							boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemstack) > 0;
							dropBlock(itemstack, world, j + j1, k + k1, l + l1, flag ? Block.stone : Block.cobblestone);
							if(shouldDamageWithLevel(level))
							{
								itemstack.damageItem(1, entityliving);
							}
						}
					}
				}
			}
		}
		return true;
    }
	
	private void dropBlock(ItemStack itemstack, World world, int i, int j, int k, Block block)
	{
		dropBlock(itemstack, world, i, j, k, block, 0);
	}
	
	private void dropBlock(ItemStack itemstack, World world, int i, int j, int k, Block block, int metadata)
    {
        if (world.isRemote)
        {
            return;
        }
		
		boolean drop = itemRand.nextInt(3) == 0;
		if (EnchantmentHelper.getEnchantmentLevel(mod_LionKing.diggahPrecision.effectId, itemstack) > 0)
		{
			drop = itemRand.nextInt(3) > 0;
		}
		
        if (drop)
        {
            float f = 0.7F;
            double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(block, 1, metadata));
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
}
