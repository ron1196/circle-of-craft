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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.ForgeDirection;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockStarAltar extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] altarIcons;
	
    public LKBlockStarAltar(int i)
    {
        super(i, Material.iron);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		setLightValue(0.5F);
        setTickRandomly(true);
		setCreativeTab(LKCreativeTabs.tabQuest);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return i == 1 ? altarIcons[1] : altarIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        altarIcons = new Icon[2];
		altarIcons[0] = iconregister.registerIcon("lionking:starAltar_side");
        altarIcons[1] = iconregister.registerIcon("lionking:starAltar_top");
    }

	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return canBlockStay(world, i, j, k) && super.canPlaceBlockAt(world, i, j, k);
    }	
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        checkAltarChange(world, i, j, k);
    }
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        checkAltarChange(world, i, j, k);
    }
	
    protected final void checkAltarChange(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
        }
    }
	
	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return (world.isBlockNormalCube(i, j - 1, k) || world.doesBlockHaveSolidTopSurface(i, j - 1, k)) && world.canBlockSeeTheSky(i, j + 1, k) && isRoom(world, i, j, k) && world.provider.dimensionId == mod_LionKing.idPrideLands;
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
	
	@Override
    public int getRenderType()
    {
		return mod_LionKing.proxy.getStarAltarRenderID();
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
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
        d = (double)i + 0.5D + 0.25D * (double)i1;
        d3 = random.nextFloat() * 2.0F * (float)i1;
        world.spawnParticle("portal", d, d1, d2, d3, d4, d5);
    }
	
	private boolean isRoom(World world, int i, int j, int k)
	{
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j + 1; j1 <= j + 2; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					if (!world.isAirBlock(i1, j1, k1))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
    public int getMobilityFlag()
    {
        return 1;
    }
}
