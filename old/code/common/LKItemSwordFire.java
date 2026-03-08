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

public class LKItemSwordFire extends LKItemSword
{
    public LKItemSwordFire(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
		if (!entityliving.isImmuneToFire() && entityliving.getHealth() > 0)
		{
			entityliving.setFire(3 + itemRand.nextInt(3));
			for (int i = 0; i < 8; i++)
			{
				double d = itemRand.nextGaussian() * 0.02D;
				double d1 = itemRand.nextGaussian() * 0.02D;
				double d2 = itemRand.nextGaussian() * 0.02D;
				entityliving.worldObj.spawnParticle("flame", (entityliving.posX + (((double)(itemRand.nextFloat() * entityliving.width * 2.0F)) - (double)entityliving.width) * 0.75F), entityliving.posY + 0.25F + (double)(itemRand.nextFloat() * entityliving.height), (entityliving.posZ + (((double)(itemRand.nextFloat() * entityliving.width * 2.0F)) - (double)entityliving.width) * 0.75F), d, d1, d2);
			}
			itemstack.damageItem(1, entityliving1);
			return true;
		}
		return super.hitEntity(itemstack, entityliving, entityliving1);
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        int i1 = world.getBlockId(i, j, k);
        if (i1 == Block.snow.blockID)
        {
            l = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID && (Block.blocksList[i1] != null && !Block.blocksList[i1].isBlockReplaceable(world, i, j, k)))
        {
            if (l == 0)
            {
                --j;
            }
            if (l == 1)
            {
                ++j;
            }
            if (l == 2)
            {
                --k;
            }
            if (l == 3)
            {
                ++k;
            }
            if (l == 4)
            {
                --i;
            }
            if (l == 5)
            {
                ++i;
            }
        }
        if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }
		else
		{
            if (world.isAirBlock(i, j, k))
            {
                world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlock(i, j, k, Block.fire.blockID, 0, 3);
            }
            itemstack.damageItem(1, entityplayer);
            return true;
		}
	}
}
