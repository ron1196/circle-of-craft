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

public class LKItemLily extends LKItemMetadata
{
    public LKItemLily(int i)
    {
        super(i);
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        MovingObjectPosition m = this.getMovingObjectPositionFromPlayer(world, entityplayer, true);

        if (m == null)
        {
            return itemstack;
        }
        else
        {
            if (m.typeOfHit == EnumMovingObjectType.TILE)
            {
                int i1 = m.blockX;
                int j1 = m.blockY;
                int k1 = m.blockZ;

                if (!world.canMineBlock(entityplayer, i1, j1, k1))
                {
                    return itemstack;
                }

                if (!entityplayer.canPlayerEdit(i1, j1, k1, m.sideHit, itemstack))
                {
                    return itemstack;
                }

                if (world.getBlockMaterial(i1, j1, k1) == Material.water && world.getBlockMetadata(i1, j1, k1) == 0 && world.isAirBlock(i1, j1 + 1, k1))
                {
                    world.setBlock(i1, j1 + 1, k1, mod_LionKing.lily.blockID, getMetadata(itemstack.getItemDamage()), 3);

                    if (!entityplayer.capabilities.isCreativeMode)
                    {
                        --itemstack.stackSize;
                    }
                }
            }

            return itemstack;
        }
    }
}
