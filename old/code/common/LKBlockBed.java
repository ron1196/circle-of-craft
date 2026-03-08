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

public class LKBlockBed extends BlockBed
{
    @SideOnly(Side.CLIENT)
    private Icon[] bedEndIcons;
    @SideOnly(Side.CLIENT)
    private Icon[] bedSideIcons;
    @SideOnly(Side.CLIENT)
    private Icon[] bedTopIcons;
	
	public LKBlockBed(int i)
	{
		super(i);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
        if (i == 0)
		{
			return mod_LionKing.prideWood.getIcon(2, 0);
		}
        else
        {
            int k = getDirection(j);
            int l = Direction.bedDirection[k][i];
            int i1 = isBlockHeadOfBed(j) ? 1 : 0;
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? bedTopIcons[i1] : bedSideIcons[i1]) : bedEndIcons[i1];
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        bedTopIcons = new Icon[] {iconregister.registerIcon("lionking:bed_feet_top"), iconregister.registerIcon("lionking:bed_head_top")};
        bedEndIcons = new Icon[] {iconregister.registerIcon("lionking:bed_feet_end"), iconregister.registerIcon("lionking:bed_head_end")};
        bedSideIcons = new Icon[] {iconregister.registerIcon("lionking:bed_feet_side"), iconregister.registerIcon("lionking:bed_head_side")};
    }
	
	@Override
    public int idDropped(int i, Random random, int j)
    {
        return isBlockHeadOfBed(i) ? 0 : mod_LionKing.bed.itemID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World world, int i, int j, int k)
    {
        return mod_LionKing.bed.itemID;
    }
	
	@Override
    public boolean isBed(World world, int x, int y, int z, EntityLivingBase player)
    {
        return true;
    }
}
