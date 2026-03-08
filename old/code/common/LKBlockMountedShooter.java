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
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKBlockMountedShooter extends BlockContainer
{
	public LKBlockMountedShooter(int i)
	{
		super(i, Material.circuits);
		setCreativeTab(null);
		setBlockBounds(0.25F, 0F, 0F, 0.75F, 0.75F, 1F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return Block.planks.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister) {}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		if (l % 2 == 0)
		{
			setBlockBounds(0.25F, 0F, 0F, 0.75F, 0.75F, 1F);
		}
		else
		{
			setBlockBounds(0F, 0F, 0.25F, 1F, 0.75F, 0.75F);
		}
	}
		
	@Override
    public int tickRate(World world)
    {
        return 4;
    }
	
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int id)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, 0, 0);
            world.setBlockToAir(i, j, k);
			return;
        }
        if (id > 0 && Block.blocksList[id].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            if (flag)
            {
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
            }
        }
    }
	
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
        {
            tryFireDart(world, i, j, k, random);
        }
    }
	
    private void tryFireDart(World world, int i, int j, int k, Random random)
    {
        LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter)world.getBlockTileEntity(i, j, k);
		if (shooter != null && Item.itemsList[shooter.dartID] != null && shooter.dartStackSize > 0)
		{
			int l = world.getBlockMetadata(i, j, k);
			int xVelocity = 0;
			int zVelocity = 0;

			if (l == 3)
			{
				xVelocity = -1;
			}
			else if (l == 2)
			{
				zVelocity = 1;
			}
			else if (l == 1)
			{
				xVelocity = 1;
			}
			else
			{
				zVelocity = -1;
			}
			
			double d = (double)i + (double)xVelocity * 0.6D + 0.5D;
			double d1 = (double)j + 0.5D;
			double d2 = (double)k + (double)zVelocity * 0.6D + 0.5D;
			
			LKEntityDart dart = new LKEntityBlueDart(world, d, d1, d2);
			if (shooter.dartID == mod_LionKing.dartYellow.itemID)
			{
				dart = new LKEntityYellowDart(world, d, d1, d2);
			}
			else if (shooter.dartID == mod_LionKing.dartRed.itemID)
			{
				dart = new LKEntityRedDart(world, d, d1, d2);
			}
			else if (shooter.dartID == mod_LionKing.dartBlack.itemID)
			{
				dart = new LKEntityBlackDart(world, d, d1, d2);
			}
			else if (shooter.dartID == mod_LionKing.dartPink.itemID)
			{
				dart = new LKEntityPinkDart(world, d, d1, d2);
			}
			if (getDamageValue(world, i, j, k) == 1)
			{
				dart.silverFired = true;
			}
			
            dart.setDartHeading(xVelocity * 1.5, 0.05D, zVelocity * 1.5, 2F, 1F);
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(dart);
			}
			world.playSoundAtEntity(dart, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 0.25F);
			
			if (!world.isRemote)
			{
				shooter.dartStackSize--;
				if (shooter.dartStackSize == 0)
				{
					shooter.dartID = 0;
				}
				
				if (world instanceof WorldServer)
				{
					updateClientFireCounter((WorldServer)world, i, j, k);
				}
			}
		}
    }
	
	private void updateClientFireCounter(WorldServer world, int i, int j, int k)
	{
		byte[] posX = ByteBuffer.allocate(4).putInt(i).array();
		byte[] posY = ByteBuffer.allocate(4).putInt(j).array();
		byte[] posZ = ByteBuffer.allocate(4).putInt(k).array();
		byte[] data = new byte[13];
		for (int l = 0; l < 4; l++)
		{
			data[l] = posX[l];
			data[l + 4] = posY[l];
			data[l + 8] = posZ[l];
		}
		data[12] = (byte)-1;
		Packet250CustomPayload packet = new Packet250CustomPayload("lk.tileEntity", data);
		PlayerInstance player = world.getPlayerManager().getOrCreateChunkWatcher(i >> 4, k >> 4, false);
		if (player != null)
		{
			player.sendToAllPlayersWatchingChunk(packet);
		}
	}
	
	@Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return world.doesBlockHaveSolidTopSurface(i, j - 1, k) && super.canPlaceBlockAt(world, i, j, k);
    }

	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
    }
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float f, float f1, float f2)
	{
		LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter)world.getBlockTileEntity(i, j, k);
		if (shooter != null)
		{
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (Item.itemsList[shooter.dartID] != null)
			{
				if (!world.isRemote)
				{
					dropBlockAsItem_do(world, i, j, k, new ItemStack(shooter.dartID, shooter.dartStackSize, 0));
					shooter.dartID = 0;
					shooter.dartStackSize = 0;
				}
				return true;
			}
			else if (Item.itemsList[shooter.dartID] == null && shooter.dartStackSize == 0 && itemstack != null && (itemstack.itemID == mod_LionKing.dartBlue.itemID || itemstack.itemID == mod_LionKing.dartYellow.itemID || itemstack.itemID == mod_LionKing.dartRed.itemID || itemstack.itemID == mod_LionKing.dartBlack.itemID || itemstack.itemID == mod_LionKing.dartPink.itemID))
			{
				shooter.dartID = itemstack.itemID;
				shooter.dartStackSize = itemstack.stackSize;
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				return true;
			}
		}
		return false;
	}
	
	@Override
    public int getRenderType()
    {
        return -1;
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
	
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving)
	{
		int i1 = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world.setBlockMetadataWithNotify(i, j, k, i1, 2);
	}

	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new LKTileEntityMountedShooter();
    }
	
	@Override
	public int idDropped(int i, Random random, int j)
	{
		return mod_LionKing.mountedShooterItem.itemID;
	}

	@Override
	public int damageDropped(int i)
	{
		return i;
	}

 	@Override
	@SideOnly(Side.CLIENT)
	public int idPicked(World world, int i, int j, int k)
	{
		return mod_LionKing.mountedShooterItem.itemID;
	}
	
	@Override
	public int getDamageValue(World world, int i, int j, int k)
	{
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LKTileEntityMountedShooter)
		{
			return ((LKTileEntityMountedShooter)tileentity).getShooterType();
		}
		return 0;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {}

	@Override
	public void onBlockHarvested(World world, int i, int j, int k, int l, EntityPlayer entityplayer)
	{
		if (entityplayer.capabilities.isCreativeMode)
		{
			l |= 8;
			world.setBlockMetadataWithNotify(i, j, k, l, 4);
		}
		super.onBlockHarvested(world, i, j, k, l, entityplayer);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int l, int i1)
	{
		if (!world.isRemote)
		{
			dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_LionKing.mountedShooterItem.itemID, 1, getDamageValue(world, i, j, k)));
		}
		LKTileEntityMountedShooter shooter = (LKTileEntityMountedShooter)world.getBlockTileEntity(i, j, k);
		if (!world.isRemote && shooter != null && Item.itemsList[shooter.dartID] != null)
		{
			dropBlockAsItem_do(world, i, j, k, new ItemStack(shooter.dartID, shooter.dartStackSize, 0));
			shooter.dartID = 0;
			shooter.dartStackSize = 0;
		}
		super.breakBlock(world, i, j, k, l, i1);
	}
}
