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

import java.util.*;
import net.minecraftforge.common.*;
import lionking.client.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockPortal extends BlockBreakable
{
    public LKBlockPortal(int i)
    {
        super(i, "lionking:portal", LKMaterialPortal.material, false);
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        if (world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
        {
            float f = 0.5F;
            float f2 = 0.125F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f2, 0.5F + f, 1.0F, 0.5F + f2);
        }
		else
        {
            float f1 = 0.125F;
            float f3 = 0.5F;
            setBlockBounds(0.5F - f1, 0.0F, 0.5F - f3, 0.5F + f1, 1.0F, 0.5F + f3);
        }
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public static boolean tryToCreatePortal(World world, int i, int j, int k)
    {
        int l = 0;
        int i1 = 0;
        if (world.getBlockId(i - 1, j, k) == mod_LionKing.lionPortalFrame.blockID || world.getBlockId(i + 1, j, k) == mod_LionKing.lionPortalFrame.blockID)
        {
            l = 1;
        }
        if (world.getBlockId(i, j, k - 1) == mod_LionKing.lionPortalFrame.blockID || world.getBlockId(i, j, k + 1) == mod_LionKing.lionPortalFrame.blockID)
        {
            i1 = 1;
        }
        if (l == i1)
        {
            return false;
        }
        if (world.getBlockId(i - l, j, k - i1) == 0)
        {
            i -= l;
            k -= i1;
        }
        for (int j1 = -1; j1 <= 2; j1++)
        {
            for (int l1 = -1; l1 <= 3; l1++)
            {
                boolean flag = j1 == -1 || j1 == 2 || l1 == -1 || l1 == 3;
                if ((j1 == -1 || j1 == 2) && (l1 == -1 || l1 == 3))
                {
                    continue;
                }
                int j2 = world.getBlockId(i + l * j1, j + l1, k + i1 * j1);
                if (flag)
                {
                    if (j2 != mod_LionKing.lionPortalFrame.blockID)
                    {
                        return false;
                    }
                    continue;
                }
                if (j2 != 0)
                {
                    return false;
                }
            }

        }

        for (int k1 = 0; k1 < 2; k1++)
        {
            for (int i2 = 0; i2 < 3; i2++)
            {
                world.setBlock(i + l * k1, j + i2, k + i1 * k1, mod_LionKing.lionPortal.blockID, 0, 2);
            }

        }

        return true;
    }

	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = 0;
        int j1 = 1;
        if (world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
        {
            i1 = 1;
            j1 = 0;
        }
        int k1;
        for (k1 = j; world.getBlockId(i, k1 - 1, k) == blockID; k1--) { }
        if (world.getBlockId(i, k1 - 1, k) != mod_LionKing.lionPortalFrame.blockID)
        {
            world.setBlockToAir(i, j, k);
            return;
        }
        int l1;
        for (l1 = 1; l1 < 4 && world.getBlockId(i, k1 + l1, k) == blockID; l1++) { }
        if (l1 != 3 || world.getBlockId(i, k1 + l1, k) != mod_LionKing.lionPortalFrame.blockID)
        {
            world.setBlockToAir(i, j, k);
            return;
        }
        boolean flag = world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID;
        boolean flag1 = world.getBlockId(i, j, k - 1) == blockID || world.getBlockId(i, j, k + 1) == blockID;
        if (flag && flag1)
        {
            world.setBlockToAir(i, j, k);
            return;
        }
        if ((world.getBlockId(i + i1, j, k + j1) != mod_LionKing.lionPortalFrame.blockID || world.getBlockId(i - i1, j, k - j1) != blockID) && (world.getBlockId(i - i1, j, k - j1) != mod_LionKing.lionPortalFrame.blockID || world.getBlockId(i + i1, j, k + j1) != blockID))
        {
            world.setBlockToAir(i, j, k);
            return;
        }
		else
        {
            return;
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if (iblockaccess.getBlockId(i, j, k) == blockID)
        {
            return false;
        }
        boolean flag = iblockaccess.getBlockId(i - 1, j, k) == blockID && iblockaccess.getBlockId(i - 2, j, k) != blockID;
        boolean flag1 = iblockaccess.getBlockId(i + 1, j, k) == blockID && iblockaccess.getBlockId(i + 2, j, k) != blockID;
        boolean flag2 = iblockaccess.getBlockId(i, j, k - 1) == blockID && iblockaccess.getBlockId(i, j, k - 2) != blockID;
        boolean flag3 = iblockaccess.getBlockId(i, j, k + 1) == blockID && iblockaccess.getBlockId(i, j, k + 2) != blockID;
        boolean flag4 = flag || flag1;
        boolean flag5 = flag2 || flag3;
        if (flag4 && l == 4)
        {
            return true;
        }
        if (flag4 && l == 5)
        {
            return true;
        }
        if (flag5 && l == 2)
        {
            return true;
        }
        return flag5 && l == 3;
    }

	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (entity.ridingEntity == null && entity.riddenByEntity == null)
        {
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entity;
				mod_LionKing.proxy.setInPrideLandsPortal(entityplayer);
			}
        }
    }
    
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (random.nextInt(100) == 0)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 1.0F, random.nextFloat() * 0.4F + 0.8F);
        }
        for (int l = 0; l < 4; l++)
        {
            double d = (float)i + random.nextFloat();
            double d1 = (float)j + random.nextFloat();
            double d2 = (float)k + random.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            int i1 = random.nextInt(2) * 2 - 1;
            d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            if (world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
            {
                d2 = (double)k + 0.5D + 0.25D * (double)i1;
                d5 = random.nextFloat() * 2.0F * (float)i1;
            }
			else
            {
                d = (double)i + 0.5D + 0.25D * (double)i1;
                d3 = random.nextFloat() * 2.0F * (float)i1;
            }
            
			mod_LionKing.proxy.spawnParticle("prideLandsPortal", d, d1, d2, d3, d4, d5);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return 0;
    }
}