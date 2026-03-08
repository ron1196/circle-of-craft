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

public class LKEntityTicketLion extends EntityCreature implements IAnimals
{
    private int talkTick;

    public LKEntityTicketLion(World world)
    {
        super(world);
        setSize(1.3F, 1.6F);
        talkTick = 40;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIWander(this, 1D));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        tasks.addTask(3, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
    }

	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
		if (talkTick < 40)
		{
			talkTick++;
		} 
    }
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }

	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
		if (talkTick == 40)
		{
			if (LKIngame.isChristmas())
			{
				if (!entityplayer.inventory.hasItem(mod_LionKing.ticket.itemID))
				{
					ItemStack itemstack = entityplayer.inventory.getCurrentItem();
					if (itemstack == null)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(mod_LionKing.ticket, 1, 1));
					}
					else
					{
						entityplayer.inventory.addItemStackToInventory(new ItemStack(mod_LionKing.ticket, 1, 1));
					}
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Ticket Lion> \u00a7fMerry Christmas! Have a cracker!");
					}
					talkTick = 0;
					return true;
				}
			}
			else
			{
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if (itemstack != null && itemstack.itemID == Item.ingotGold.itemID && talkTick == 40)
				{
					if (itemstack.stackSize == 1)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					}
					else
					{
						itemstack.stackSize--;
					}
					entityplayer.inventory.addItemStackToInventory(new ItemStack(mod_LionKing.ticket));
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Ticket Lion> \u00a7fThank you. Use the ticket to open the portal in the room behind me.");
						addEffects(7);
					}
				}
				else if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Ticket Lion> \u00a7fBring me a gold ingot and I will exchange it for a Lion King Ticket.");
				}
				talkTick = 0;
				return true;
			}		
		}
        return false;
    }
	
	@Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
		if (!worldObj.isRemote)
		{
			addEffects(12);
		}
    }
	
	private void addEffects(int amount)
	{
		for (int i = 0; i < amount; i++)
		{
			double d = getRNG().nextGaussian() * 0.02D;
			double d1 = getRNG().nextGaussian() * 0.02D;
			double d2 = getRNG().nextGaussian() * 0.02D;
			LKIngame.spawnCustomFX(worldObj, 48, 32, false, (posX + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), posY + 0.25F + (double)(getRNG().nextFloat() * height), (posZ + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), d, d1, d2);
		}
	}
	
	@Override
	protected String getLivingSound()
    {
        return "lionking:lion";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:lionroar";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:liondeath";
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
	{
		if (worldObj.canBlockSeeTheSky(i, j, k))
		{
			return -999999.0F;
		}
		return super.getBlockPathWeight(i, j, k);
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
