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
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockLog extends LKBlock
{
    public LKBlockLog(int i)
    {
        super(i, Material.wood);
		setCreativeTab(null);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
		return mod_LionKing.prideWood.getIcon(i, 8);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister) {}
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.prideWood.blockID;
    }
	
	@Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        if (!world.isRemote && world.rand.nextBoolean())
        {
			LKEntityBug bug = new LKEntityBug(world);
			bug.setLocationAndAngles((double)i + 0.5D, j, (double)k + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
			world.spawnEntityInWorld(bug);
		}
	}
	
	@Override
    protected boolean canSilkHarvest()
    {
        return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.prideWood.blockID;
    }
}
