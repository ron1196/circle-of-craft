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

public class LKItemBed extends LKItem
{
    public LKItemBed(int i)
    {
        super(i);
        setCreativeTab(LKCreativeTabs.tabDeco);
    }

	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        if (l != 1)
        {
            return false;
        }
        else
        {
            j++;
            BlockBed block = (BlockBed)mod_LionKing.blockBed;
            int i1 = MathHelper.floor_double((double)(entityplayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte byte0 = 0;
            byte byte1 = 0;

            if (i1 == 0)
            {
                byte1 = 1;
            }

            if (i1 == 1)
            {
                byte0 = -1;
            }

            if (i1 == 2)
            {
                byte1 = -1;
            }

            if (i1 == 3)
            {
                byte0 = 1;
            }

            if (entityplayer.canPlayerEdit(i, j, k, i1, itemstack) && entityplayer.canPlayerEdit(i + byte0, j, k + byte1, i1, itemstack))
            {
                if (world.isAirBlock(i, j, k) && world.isAirBlock(i + byte0, j, k + byte1) && world.doesBlockHaveSolidTopSurface(i, j - 1, k) && world.doesBlockHaveSolidTopSurface(i + byte0, j - 1, k + byte1))
                {
                    world.setBlock(i, j, k, block.blockID, i1, 3);

                    if (world.getBlockId(i, j, k) == block.blockID)
                    {
                        world.setBlock(i + byte0, j, k + byte1, block.blockID, i1 + 8, 3);
                    }

                    --itemstack.stackSize;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
