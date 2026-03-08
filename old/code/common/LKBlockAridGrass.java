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

import net.minecraftforge.common.IShearable;

public class LKBlockAridGrass extends BlockFlower implements IShearable
{
	private static Material material = (new MaterialLogic(MapColor.sandColor)).setReplaceable();
	
    public LKBlockAridGrass(int i)
    {
        super(i, material);
        setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.5F, 0.9F);
		setCreativeTab(LKCreativeTabs.tabDeco);
    }
	
	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);
        return ((world.getFullBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k)) && (l == Block.sand.blockID || l == mod_LionKing.tilledSand.blockID));
    }
	
	@Override
    public boolean isBlockReplaceable(World world, int i, int j, int k)
    {
	    return true;
    }
	
	@Override
    public int getRenderType()
    {
		return mod_LionKing.proxy.getAridGrassRenderID();
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return Block.tallGrass.idDropped(i, random, j);
    }

	@Override
    public int quantityDroppedWithBonus(int i, Random random)
    {
        return Block.tallGrass.quantityDroppedWithBonus(i, random);
    }
	
	@Override
    public ArrayList getBlockDropped(World world, int i, int j, int k, int meta, int fortune)
    {
        return Block.tallGrass.getBlockDropped(world, i, j, k, meta, fortune);
    }

	@Override
    public boolean isShearable(ItemStack item, World world, int i, int j, int k) 
    {
        return Block.tallGrass.isShearable(item, world, i, j, k);
	}

	@Override
    public ArrayList onSheared(ItemStack item, World world, int i, int j, int k, int fortune)
    {
		ArrayList list = new ArrayList();
		list.add(new ItemStack(this));
		return list;
    }
	
	@Override
    public int getMobilityFlag()
    {
        return 1;
    }
}
