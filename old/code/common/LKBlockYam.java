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
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IconRegister;

import java.util.*;

public class LKBlockYam extends BlockCrops
{
	@SideOnly(Side.CLIENT)
	private Icon[] yamIcons;
	
    public LKBlockYam(int i)
    {
        super(i);
    }
	
	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
		if (world.getBlockMetadata(i, j, k) == 8)
		{
			return (world.getBlockId(i, j - 1, k) == Block.dirt.blockID || world.getBlockId(i, j - 1, k) == Block.grass.blockID) && (world.getFullBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k));
		}
		else
		{
			return super.canBlockStay(world, i, j, k);
		}
    }
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (LKIngame.isLKWorld(world.provider.dimensionId))
		{
			super.updateTick(world, i, j, k, random);
		}
	}
	
	@Override
    public void fertilize(World world, int i, int j, int k)
    {
        if (LKIngame.isLKWorld(world.provider.dimensionId))
		{
			super.fertilize(world, i, j, k);
		}
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
        if (j < 7)
        {
            if (j == 6)
            {
                j = 5;
            }
            return yamIcons[j >> 1];
        }
        else
        {
            return yamIcons[3];
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
	{
		yamIcons = new Icon[4];
        for (int i = 0; i < 4; i++)
        {
            yamIcons[i] = iconregister.registerIcon(getTextureName() + "_" + i);
        }
    }

	@Override
    protected int getSeedItem()
    {
        return mod_LionKing.yam.itemID;
    }

	@Override
    protected int getCropItem()
    {
        return mod_LionKing.yam.itemID;
    }
	
    @Override 
    public ArrayList getBlockDropped(World world, int i, int j, int k, int metadata, int fortune)
    {
        if (metadata == 8)
		{
			ArrayList drops = new ArrayList();
			if (world.rand.nextInt(3) > 0)
			{
				drops.add(new ItemStack(mod_LionKing.yam));
			}
			return drops;
		}
		else
		{
			return super.getBlockDropped(world, i, j, k, metadata, fortune);
		}
    }
}
