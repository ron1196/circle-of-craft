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
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockKiwano extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] kiwanoIcons;
	
    public LKBlockKiwano(int i)
    {
        super(i, Material.pumpkin);
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		setCreativeTab(LKCreativeTabs.tabDeco);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int i, int j)
    {
        if (j == -1)
		{
			return kiwanoIcons[1];
		}
		return kiwanoIcons[0];
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        kiwanoIcons = new Icon[3];
		kiwanoIcons[0] = iconregister.registerIcon("lionking:kiwano");
		kiwanoIcons[1] = iconregister.registerIcon("lionking:kiwano_spikes");
    }

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return mod_LionKing.kiwano.itemID;
    }

	@Override
    public int quantityDropped(Random random)
    {
        return 2 + random.nextInt(5);
    }

	@Override
    public int quantityDroppedWithBonus(int i, Random random)
    {
        int j = quantityDropped(random) + random.nextInt(1 + i);

        if (j > 9)
        {
            j = 9;
        }

        return j;
    }
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k))
		{
			world.setBlockToAir(i, j, k);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(this));
		}
    }
	
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return world.doesBlockHaveSolidTopSurface(i, j - 1, k) && super.canPlaceBlockAt(world, i, j, k);
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
        return mod_LionKing.proxy.getKiwanoBlockRenderID();
    }
}
