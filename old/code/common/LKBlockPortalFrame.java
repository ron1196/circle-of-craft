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

public class LKBlockPortalFrame extends LKBlock
{
	@SideOnly(Side.CLIENT)
	private Icon gateIcon;
	
    public LKBlockPortalFrame(int i)
    {
        super(i, Material.rock);
		setBlockUnbreakable();
		setResistance(6000000F);
    }
	
	@Override
    public int getMobilityFlag()
    {
        return 2;
    }
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int i, int j, int k)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		if (blockID == mod_LionKing.outlandsPortalFrame.blockID)
		{
			if (j == 1)
			{
				return mod_LionKing.termite.getIcon(0, 0);
			}
			if (j == 2)
			{
				return gateIcon;
			}
		}
		return super.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        super.registerIcons(iconregister);
		gateIcon = iconregister.registerIcon("lionking:ziraMoundGate");
    }
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
    {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (LKQuestBase.outlandsQuest.canStart() && itemstack != null && itemstack.itemID == mod_LionKing.rafikiStick.itemID && blockID == mod_LionKing.outlandsPortalFrame.blockID && world.getBlockMetadata(i, j, k) == 2)
		{
			world.playAuxSFX(2001, i, j, k, world.getBlockId(i, j, k) + (world.getBlockMetadata(i, j, k) << 12));
			world.setBlockToAir(i, j, k);
			if (world.getBlockId(i - 1, j, k) == blockID && world.getBlockMetadata(i - 1, j, k) == 2)
			{
				onBlockActivated(world, i - 1, j, k, entityplayer, l, f, f1, f2);
			}
			if (world.getBlockId(i + 1, j, k) == blockID && world.getBlockMetadata(i + 1, j, k) == 2)
			{
				onBlockActivated(world, i + 1, j, k, entityplayer, l, f, f1, f2);
			}
			if (world.getBlockId(i, j - 1, k) == blockID && world.getBlockMetadata(i, j - 1, k) == 2)
			{
				onBlockActivated(world, i, j - 1, k, entityplayer, l, f, f1, f2);
			}
			if (world.getBlockId(i, j + 1, k) == blockID && world.getBlockMetadata(i, j + 1, k) == 2)
			{
				onBlockActivated(world, i, j + 1, k, entityplayer, l, f, f1, f2);
			}
			if (world.getBlockId(i, j, k - 1) == blockID && world.getBlockMetadata(i, j, k - 1) == 2)
			{
				onBlockActivated(world, i, j, k - 1, entityplayer, l, f, f1, f2);
			}
			if (world.getBlockId(i, j, k + 1) == blockID && world.getBlockMetadata(i, j, k + 1) == 2)
			{
				onBlockActivated(world, i, j, k + 1, entityplayer, l, f, f1, f2);
			}
			LKQuestBase.outlandsQuest.setDelayed(true);
			LKQuestBase.outlandsQuest.progress(1);
			return true;
		}
		return false;
	}
	
	@Override
    public int damageDropped(int i)
    {
        return i;
    }
}
