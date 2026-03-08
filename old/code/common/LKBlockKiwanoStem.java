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

import java.util.ArrayList;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockKiwanoStem extends BlockFlower
{
    private Block fruitType;

    public LKBlockKiwanoStem(int i)
    {
        super(i);
        fruitType = mod_LionKing.kiwanoBlock;
        setTickRandomly(true);
        float f = 0.125F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
		setCreativeTab(null);
    }

	@Override
    protected boolean canThisPlantGrowOnThisBlockID(int i)
    {
        return i == mod_LionKing.tilledSand.blockID;
    }

	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        super.updateTick(world, i, j, k, random);

        if (LKIngame.isLKWorld(world.provider.dimensionId) && world.getBlockLightValue(i, j + 1, k) >= 9)
        {
            float f = getGrowthModifier(world, i, j, k);

            if (random.nextInt((int)(25.0F / f) + 1) == 0)
            {
                int l = world.getBlockMetadata(i, j, k);

                if (l < 7)
                {
                    ++l;
                    world.setBlockMetadataWithNotify(i, j, k, l, 3);
                }
                else
                {
                    if (world.getBlockId(i - 1, j, k) == fruitType.blockID)
                    {
                        return;
                    }

                    if (world.getBlockId(i + 1, j, k) == fruitType.blockID)
                    {
                        return;
                    }

                    if (world.getBlockId(i, j, k - 1) == fruitType.blockID)
                    {
                        return;
                    }

                    if (world.getBlockId(i, j, k + 1) == fruitType.blockID)
                    {
                        return;
                    }

                    int l1 = random.nextInt(4);
                    int i1 = i;
                    int k1 = k;

                    if (l1 == 0)
                    {
                        i1 = i - 1;
                    }

                    if (l1 == 1)
                    {
                        ++i1;
                    }

                    if (l1 == 2)
                    {
                        k1 = k - 1;
                    }

                    if (l1 == 3)
                    {
                        ++k1;
                    }

                    int l2 = world.getBlockId(i1, j - 1, k1);

                    if (world.getBlockId(i1, j, k1) == 0 && (l2 == mod_LionKing.tilledSand.blockID || l2 == Block.sand.blockID || l2 == Block.dirt.blockID || l2 == Block.grass.blockID))
                    {
                        world.setBlock(i1, j, k1, fruitType.blockID, 0, 3);
                    }
                }
            }
        }
    }

    public void fertilize(World world, int i, int j, int k)
    {
        world.setBlockMetadataWithNotify(i, j, k, 7, 3);
    }
	
    public void fertilizePartially(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k) + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);

        if (l > 7)
        {
            l = 7;
        }

        world.setBlockMetadataWithNotify(i, j, k, l, 2);
    }

    private float getGrowthModifier(World world, int i, int j, int k)
    {
        float f = 0.8F;
        int i1 = world.getBlockId(i, j, k - 1);
        int j1 = world.getBlockId(i, j, k + 1);
        int k1 = world.getBlockId(i - 1, j, k);
        int l1 = world.getBlockId(i + 1, j, k);
        int i2 = world.getBlockId(i - 1, j, k - 1);
        int j2 = world.getBlockId(i + 1, j, k - 1);
        int k2 = world.getBlockId(i + 1, j, k + 1);
        int l2 = world.getBlockId(i - 1, j, k + 1);
        boolean flag = k1 == blockID || l1 == blockID;
        boolean flag1 = i1 == blockID || j1 == blockID;
        boolean flag2 = i2 == blockID || j2 == blockID || k2 == blockID || l2 == blockID;

        for (int i3 = i - 1; i3 <= i + 1; ++i3)
        {
            for (int j3 = k - 1; j3 <= k + 1; ++j3)
            {
                int k3 = world.getBlockId(i3, j - 1, j3);
                float f1 = 0.0F;

                if (k3 == mod_LionKing.tilledSand.blockID)
                {
                    f1 = 1.0F;

                    if (world.getBlockMetadata(i3, j - 1, j3) > 0)
                    {
                        f1 = 3.0F;
                    }
                }

                if (i3 != i || j3 != k)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1)
        {
            f /= 2.0F;
        }

        return f;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int i)
    {
        int j = i * 32;
        int k = 255 - i * 8;
        int l = i * 4;
        return j << 16 | k << 8 | l;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
        return getRenderColor(world.getBlockMetadata(i, j, k));
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
        return Block.pumpkinStem.getIcon(i, j);
    }

	@Override
    public void setBlockBoundsForItemRender()
    {
        float f = 0.125F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        maxY = (double)((float)(world.getBlockMetadata(i, j, k) * 2 + 2) / 16.0F);
        float f = 0.125F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, (float)maxY, 0.5F + f);
    }

	@Override
    public int getRenderType()
    {
        return mod_LionKing.proxy.getKiwanoStemRenderID();
    }

    public int getState(IBlockAccess world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        return l < 7 ? -1 : (world.getBlockId(i - 1, j, k) == fruitType.blockID ? 0 : (world.getBlockId(i + 1, j, k) == fruitType.blockID ? 1 : (world.getBlockId(i, j, k - 1) == fruitType.blockID ? 2 : (world.getBlockId(i, j, k + 1) == fruitType.blockID ? 3 : -1))));
    }

    @Override 
    public ArrayList getBlockDropped(World world, int i, int j, int k, int metadata, int fortune)
    {
        ArrayList drops = new ArrayList();
        for (int l = 0; l < 3; l++)
        {
            if (world.rand.nextInt(15) <= metadata)
            {
                drops.add(new ItemStack(mod_LionKing.kiwanoSeeds));
            }
        }
        return drops;
    }

	@Override
    public int idDropped(int i, Random random, int j)
    {
		return mod_LionKing.kiwanoSeeds.itemID;
    }

	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return 0;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {}
}
