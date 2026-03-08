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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockPumbaaBox extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon[] boxIcons;
	
	public LKBlockPumbaaBox(int i)
	{
		super(i, Material.wood);
		setCreativeTab(null);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return i < 2 ? boxIcons[1] : boxIcons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        boxIcons = new Icon[2];
		boxIcons[0] = iconregister.registerIcon("lionking:pumbbox_side");
        boxIcons[1] = iconregister.registerIcon("lionking:pumbbox_top");
    }
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
	{
		if (LKQuestBase.outlandsQuest.getQuestStage() == 8 && LKLevelData.ziraStage == 18 && world.provider.dimensionId == mod_LionKing.idPrideLands && world.getBlockId(i, j - 1, k) == mod_LionKing.rafikiWood.blockID && world.getBlockMetadata(i, j - 1, k) == 2 && j == 103 && i > -4 && i < 15 && k > -10 && k < 9)
		{
			explode(world, i, j, k);
			return true;
		}
		
		double d;
		double d1;
		double d2;
		for (int i1 = 0; i1 < 8; i1++)
		{
			d = world.rand.nextGaussian() * 0.02D;
			d1 = world.rand.nextGaussian() * 0.02D;
			d2 = world.rand.nextGaussian() * 0.02D;
			world.spawnParticle("smoke", (i + (((double)(world.rand.nextFloat() * 2.0F)) - 1.0D) * 0.75F) + 0.5D, j + 0.9F + (double)world.rand.nextFloat(), (k + (((double)(world.rand.nextFloat() * 2.0F)) - 1.0D) * 0.75F) + 0.5D, d, d1, d2);
		}
		return true;
	}
	
	private void explode(World world, int i, int j, int k)
	{
		world.setBlockToAir(i, j, k);
		world.spawnParticle("hugeexplosion", i + 0.5D, j + 2.0D, k + 0.5D, 0.0D, 0.0D, 0.0D);
		world.playSoundEffect(i + 0.5D, j + 2.0D, k + 0.5D, "lionking:flatulence", 4F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		if (!world.isRemote)
		{
			LKIngame.startFlatulenceExplosion(world);
		}
	}
}
