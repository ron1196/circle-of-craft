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

public class LKItemTallFlower extends LKItem
{
	private int flowerMetadata;
	
    public LKItemTallFlower(int i, int j)
    {
       	super(i);
		flowerMetadata = j;
		setCreativeTab(LKCreativeTabs.tabDeco);
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        int i1 = world.getBlockId(i, j, k);
        if (i1 == Block.snow.blockID)
        {
            l = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID)
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
		Block block = mod_LionKing.flowerBase;
        if (j < 255 && (world.getBlockId(i, j-1, k) == Block.grass.blockID || world.getBlockId(i, j-1, k) == Block.dirt.blockID || world.getBlockId(i, j-1, k) == Block.tilledField.blockID) && isReplaceableBlock(world, i, j, k) && isReplaceableBlock(world, i, j+1, k))
        {		
            if (!world.isRemote)
            {
				world.setBlock(i, j, k, mod_LionKing.flowerBase.blockID, flowerMetadata, 3);
				world.setBlock(i, j+1, k, mod_LionKing.flowerTop.blockID, flowerMetadata, 3);
                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
				return true;
			}
		}	
		return false;
    }
	
	private boolean isReplaceableBlock(World world, int i, int j, int k)
	{
		int l = world.getBlockId(i, j, k);
        return l == 0 || Block.blocksList[l].blockMaterial.isReplaceable();
	}
}