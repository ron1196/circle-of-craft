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

public class LKItemJar extends LKItem
{
    private int isFull;

    public LKItemJar(int i, int j)
    {
        super(i);
        maxStackSize = 1;
        isFull = j;
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        float f = 1.0F;
        double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f;
        double d1 = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f + 1.6200000000000001D) - (double)entityplayer.yOffset;
        double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f;
        boolean flag = isFull == 0;
        MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, entityplayer, flag);
        if (movingobjectposition == null)
        {
            return itemstack;
        }
        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;
            if (!world.canMineBlock(entityplayer, i, j, k))
            {
                return itemstack;
            }
            if (isFull == 0)
            {
                if (!entityplayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemstack))
                {
                    return itemstack;
                }
                if (world.getBlockMaterial(i, j, k) == Material.water && world.getBlockMetadata(i, j, k) == 0)
                {
                    world.setBlockToAir(i, j, k);
                    if (entityplayer.capabilities.isCreativeMode)
                    {
                        return itemstack;
                    }
                    else
                    {
                        return new ItemStack(mod_LionKing.jarWater);
                    }
                }
                if (world.getBlockMaterial(i, j, k) == Material.lava && world.getBlockMetadata(i, j, k) == 0)
                {
                    world.setBlockToAir(i, j, k);
                    if (entityplayer.capabilities.isCreativeMode)
                    {
                        return itemstack;
                    }
                    else
                    {
                        return new ItemStack(mod_LionKing.jarLava);
                    }
                }
            } else
            {
                if (isFull < 0)
                {
                    return new ItemStack(mod_LionKing.jar);
                }
                if (movingobjectposition.sideHit == 0)
                {
                    j--;
                }
                if (movingobjectposition.sideHit == 1)
                {
                    j++;
                }
                if (movingobjectposition.sideHit == 2)
                {
                    k--;
                }
                if (movingobjectposition.sideHit == 3)
                {
                    k++;
                }
                if (movingobjectposition.sideHit == 4)
                {
                    i--;
                }
                if (movingobjectposition.sideHit == 5)
                {
                    i++;
                }
                if (!entityplayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemstack))
                {
                    return itemstack;
                }
                if (world.isAirBlock(i, j, k) || !world.getBlockMaterial(i, j, k).isSolid())
                {
                    if (world.provider.isHellWorld && isFull == Block.waterMoving.blockID)
                    {
                        world.playSoundEffect(d + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                        for (int l = 0; l < 8; l++)
                        {
                            world.spawnParticle("largesmoke", (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                        }

                    }
					else
                    {
                        world.setBlock(i, j, k, isFull, 0, 3);
                    }
                    if (entityplayer.capabilities.isCreativeMode)
                    {
                        return itemstack;
                    }
					else
                    {
                        return new ItemStack(mod_LionKing.jar);
                    }
                }
            }
        }
        else if (isFull == 0 && (movingobjectposition.entityHit instanceof LKEntityZebra))
        {
            return new ItemStack(mod_LionKing.jarMilk);
        }
        return itemstack;
    }
}
