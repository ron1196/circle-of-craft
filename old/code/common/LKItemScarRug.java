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

public class LKItemScarRug extends LKItem
{
	private int type;
	
	public LKItemScarRug(int i, int j)
	{
		super(i);
		type = j;
		setCreativeTab(LKCreativeTabs.tabQuest);
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
		if (world.doesBlockHaveSolidTopSurface(i, j - 1, k) || world.isBlockNormalCube(i, j - 1, k))
		{
			if (!world.isRemote)
			{
				LKEntityScarRug rug = new LKEntityScarRug(world, type);
				rug.setLocationAndAngles((double)i + f, (double)j, (double)k + f2, (entityplayer.rotationYaw % 360.0F) + 180.0F, 0.0F);
				if (world.checkNoEntityCollision(rug.boundingBox) && world.getCollidingBoundingBoxes(rug, rug.boundingBox).size() == 0 && !world.isAnyLiquid(rug.boundingBox))
				{
					world.spawnEntityInWorld(rug);
					world.playSoundAtEntity(rug, "lionking:lion", 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
					itemstack.stackSize--;
					return true;
				}
				rug.setDead();
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack)
    {
		return EnumRarity.uncommon;
    }
}
