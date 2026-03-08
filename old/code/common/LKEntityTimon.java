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

public class LKEntityTimon extends EntityCreature implements LKCharacter
{
	private int talkTick;
	public LKInventoryTimon inventory;
	private int invRefreshTick;

    public LKEntityTimon(World world)
    {
        super(world);
        setSize(0.4F, 0.9F);
		talkTick = 120;
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
		inventory = new LKInventoryTimon(this);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(100D);
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
		if (!worldObj.isRemote && getHealth() < getMaxHealth())
		{
        	setHealth(getMaxHealth());
		}
		if (talkTick < 120)
		{
			talkTick++;
		} 
		if (invRefreshTick < 20)
		{
			invRefreshTick++;
		} 
		if (invRefreshTick == 20)
		{
			if (inventory.getStackInSlot(0) == null)
			{
				inventory.setInventorySlotContents(0, new ItemStack(mod_LionKing.tunnahDiggah));
			}
			if (inventory.getStackInSlot(1) == null)
			{
				inventory.setInventorySlotContents(1, new ItemStack(mod_LionKing.pumbaaBomb));
			}
			if (inventory.getStackInSlot(2) == null)
			{
				inventory.setInventorySlotContents(2, new ItemStack(mod_LionKing.crystal));
			}
			if (inventory.getStackInSlot(3) == null)
			{
				inventory.setInventorySlotContents(3, new ItemStack(mod_LionKing.xpGrub));
			}
			if (inventory.getStackInSlot(4) == null)
			{
				inventory.setInventorySlotContents(4, new ItemStack(mod_LionKing.amulet));
			}
			invRefreshTick = 0;
		}

		if (hasSpoken() && canTalk() && !hasPumbaaSpoken())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fI don't know. Do you think we can eat him?");
			}
			dataWatcher.updateObject(14, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasPumbaaSpoken() && canTalk() && !hasTimonSpokenAgain())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fOf course we can't eat him, you idiot. Lie down before you hurt yourself.");
			}
			dataWatcher.updateObject(15, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasTimonSpokenAgain() && canTalk() && !hasTimonSpokenYetAgain())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fAlthough now that you mention it...");
			}
			dataWatcher.updateObject(16, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasTimonSpokenYetAgain() && canTalk() && !hasPumbaaSpokenAgain())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fKid, we're really hungry. Do you think you could find us something to eat?");
			}
			dataWatcher.updateObject(17, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasPumbaaSpokenAgain() && canTalk() && !hasTimonDismissedIdea())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fPumbaa, that's an awful idea. It'll never work.");
			}
			dataWatcher.updateObject(18, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasTimonDismissedIdea() && canTalk() && !hasTimonClaimedIdea())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fWait a minute. I've got a great idea! You! You there! Do you think you could find us something to eat?");
			}
			dataWatcher.updateObject(19, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasTimonClaimedIdea() && canTalk() && !hasPumbaaSpokenFood())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fOur favourite food is bugs. Especially those stripy blue ones that hide under logs!");
			}
			dataWatcher.updateObject(20, Byte.valueOf((byte)1));
			talkTick = 0;
		}
		if (hasPumbaaSpokenFood() && canTalk() && !hasTimonSpokenFood())
		{
			if (worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fStop it Pumbaa, you're making my mouth water! Anyway, you there, if you bring us some bugs to eat, I might have some things to offer you in return.");
			}
			dataWatcher.updateObject(21, Byte.valueOf((byte)1));
			talkTick = 0;
		}
	}
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(13, Byte.valueOf((byte)0));
        dataWatcher.addObject(14, Byte.valueOf((byte)0));
        dataWatcher.addObject(15, Byte.valueOf((byte)0));
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
        dataWatcher.addObject(19, Byte.valueOf((byte)0));
        dataWatcher.addObject(20, Byte.valueOf((byte)0));
        dataWatcher.addObject(21, Byte.valueOf((byte)0));
        dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }
	
	@Override
   	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        if (dataWatcher.getWatchableObjectByte(13) == 1)
        {
      		nbttagcompound.setBoolean("HasSpoken", true);
        }
        if (dataWatcher.getWatchableObjectByte(14) == 1)
        {
      		nbttagcompound.setBoolean("HasPumbaaSpoken", true);
        }
        if (dataWatcher.getWatchableObjectByte(15) == 1)
        {
      		nbttagcompound.setBoolean("HasTimonSpokenAgain", true);
        }
        if (dataWatcher.getWatchableObjectByte(16) == 1)
        {
      		nbttagcompound.setBoolean("HasTimonSpokenYetAgain", true);
        }
        if (dataWatcher.getWatchableObjectByte(17) == 1)
        {
      		nbttagcompound.setBoolean("HasPumbaaSpokenAgain", true);
        }
        if (dataWatcher.getWatchableObjectByte(18) == 1)
        {
      		nbttagcompound.setBoolean("HasTimonDismissedIdea", true);
        }
        if (dataWatcher.getWatchableObjectByte(19) == 1)
        {
      		nbttagcompound.setBoolean("HasTimonClaimedIdea", true);
        }
        if (dataWatcher.getWatchableObjectByte(20) == 1)
        {
      		nbttagcompound.setBoolean("HasPumbaaSpokenFood", true);
        }
        if (dataWatcher.getWatchableObjectByte(21) == 1)
        {
      		nbttagcompound.setBoolean("HasTimonSpokenFood", true);
        }
        if (dataWatcher.getWatchableObjectByte(22) == 1)
        {
      		nbttagcompound.setBoolean("HasEaten", true);
        }
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
        inventory.readFromNBT(nbttaglist);
        dataWatcher.updateObject(13, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpoken") ? 1 : 0)));
        dataWatcher.updateObject(14, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasPumbaaSpoken") ? 1 : 0)));
        dataWatcher.updateObject(15, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTimonSpokenAgain") ? 1 : 0)));
        dataWatcher.updateObject(16, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTimonSpokenYetAgain") ? 1 : 0)));
        dataWatcher.updateObject(17, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasPumbaaSpokenAgain") ? 1 : 0)));
        dataWatcher.updateObject(18, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTimonDismissedIdea") ? 1 : 0)));
        dataWatcher.updateObject(19, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTimonClaimedIdea") ? 1 : 0)));
        dataWatcher.updateObject(20, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasPumbaaSpokenFood") ? 1 : 0)));
        dataWatcher.updateObject(21, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTimonSpokenFood") ? 1 : 0)));
        dataWatcher.updateObject(22, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasEaten") ? 1 : 0)));
    }
	
	private boolean hasSpoken()
    {
        return dataWatcher.getWatchableObjectByte(13) == 1;
    }
	
	private boolean hasPumbaaSpoken()
    {
        return dataWatcher.getWatchableObjectByte(14) == 1;
    }
	
	private boolean hasTimonSpokenAgain()
    {
        return dataWatcher.getWatchableObjectByte(15) == 1;
    }
	
	private boolean hasTimonSpokenYetAgain()
    {
        return dataWatcher.getWatchableObjectByte(16) == 1;
    }
	
	private boolean hasPumbaaSpokenAgain()
    {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }
	
	private boolean hasTimonDismissedIdea()
    {
        return dataWatcher.getWatchableObjectByte(18) == 1;
    }
	
	private boolean hasTimonClaimedIdea()
    {
        return dataWatcher.getWatchableObjectByte(19) == 1;
    }
	
	private boolean hasPumbaaSpokenFood()
    {
        return dataWatcher.getWatchableObjectByte(20) == 1;
    }
	
	private boolean hasTimonSpokenFood()
    {
        return dataWatcher.getWatchableObjectByte(21) == 1;
    }
	
	private boolean hasEaten()
    {
        return dataWatcher.getWatchableObjectByte(22) == 1;
    }
	
	public void setFollowable()
	{
	    dataWatcher.updateObject(12, Byte.valueOf((byte)0));
	}
	
	public void setEaten()
	{
	    dataWatcher.updateObject(22, Byte.valueOf((byte)1));
	}

    private boolean canTalk()
    {
		return talkTick == 120;
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (entityplayer.capabilities.isCreativeMode)
		{
			entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_TIMON, worldObj, entityId, 0, 0);
			return true;
		}
		if (talkTick >= 40)
		{
			if (!hasSpoken())
			{
				if (worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fHey, Pumbaa. Who's this?");
				}
				dataWatcher.updateObject(13, Byte.valueOf((byte)1));
				talkTick = 0;
				return true;
			}
			
			if (hasTimonSpokenFood())
			{
				if (entityplayer.inventory.hasItem(mod_LionKing.bug.itemID))
				{
					entityplayer.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_TIMON, worldObj, entityId, 0, 0);
					return true;
				}
				else
				{
					if (!hasEaten())
					{
						if (worldObj.isRemote)
						{
							LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.BUGS));
						}
						talkTick = 0;
						return true;
					}
					else
					{
						if (worldObj.isRemote)
						{
							LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.MORE_BUGS));
						}
						talkTick = 0;
						return true;
					}
				}
			}
		}
		return super.interact(entityplayer);
	}
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		return false;
	}
	
	@Override
    protected void kill()
    {
        setDead();
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		if (posY < 60 && j < posY)
		{
			return -999999F;
		}
        return worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID ? 10.0F : worldObj.getLightBrightness(i, j, k) - 0.5F;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
