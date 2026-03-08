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

import java.util.List;

import net.minecraft.client.Minecraft;

public class LKEntityRafiki extends EntityCreature implements LKCharacter
{
    private int talkTick;
	private boolean hasSpawnedOneScar;
    private int processTick;
    private int talkPortalTick;
    private int talkDustTick;
	public boolean isThisTheRealRafiki;

    public LKEntityRafiki(World world)
    {
        super(world);
		setSize(0.7F, 1.6F);
		talkTick = 40;
		hasSpawnedOneScar = false;
		processTick = 100;
		talkPortalTick = 120;
		talkDustTick = 120;
		isThisTheRealRafiki = false;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIWander(this, 1D));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(3, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(100D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
    }

	@Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
        dataWatcher.addObject(19, Byte.valueOf((byte)0));
        dataWatcher.addObject(20, Byte.valueOf((byte)0));
        dataWatcher.addObject(21, Byte.valueOf((byte)0));
        dataWatcher.addObject(22, Byte.valueOf((byte)0));
        dataWatcher.addObject(23, Byte.valueOf((byte)0));
        dataWatcher.addObject(24, Byte.valueOf((byte)0));
        dataWatcher.addObject(25, Byte.valueOf((byte)0));
        dataWatcher.addObject(26, Byte.valueOf((byte)0));
        dataWatcher.addObject(27, Byte.valueOf((byte)0));
        dataWatcher.addObject(28, Byte.valueOf((byte)0));
        dataWatcher.addObject(29, Byte.valueOf((byte)0));
        dataWatcher.addObject(30, Byte.valueOf((byte)0));
        dataWatcher.addObject(31, Byte.valueOf((byte)0));
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public void setJumping(boolean flag) {}
	
	@Override
    protected void jump() {}

	@Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
		if (LKLevelData.ziraStage == 14 && !worldObj.isRemote)
		{
			setDead();
		}
		if (isThisTheRealRafiki && worldObj.getWorldInfo().getWorldTime() % 100L == 0L)
		{
			ChunkCoordinates currentPos = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
			double distanceFromHome = Math.sqrt(currentPos.getDistanceSquared(0, 103, 0));
			if (distanceFromHome > 20D)
			{
				for (int i = 0; i < 12; i++)
				{
					double d = getRNG().nextGaussian() * 0.02D;
					double d1 = getRNG().nextGaussian() * 0.02D;
					double d2 = getRNG().nextGaussian() * 0.02D;
					worldObj.spawnParticle("portal", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
				}
				setLocationAndAngles(0, 103, 0, rotationYaw, 0.0F);
			}
		}
		
		boolean flag = false;
		for (int i = -16; i < 17; i++)
		{
			for (int j = -5; j < 6; j++)
			{
				for (int k = -16; k < 17; k++)
				{
					if (worldObj.getBlockId(MathHelper.floor_double(posX) + i, MathHelper.floor_double(posY) + j, MathHelper.floor_double(posZ) + k) == mod_LionKing.flowerVase.blockID)
					{
						flag = true;
					}
				}
			}
		}
		if (flag && getRNG().nextInt(150) == 0)
		{
			addHeartFX();
		}
		
		if (!worldObj.isRemote && getHealth() < getMaxHealth())
		{
        	setHealth(getMaxHealth());
		}
		
		if (talkTick < 40)
		{
			talkTick++;
		} 
		if (processTick < 100)
		{
			processTick++;
		} 
		if (talkPortalTick < 120)
		{
			talkPortalTick++;
		} 
		if (talkDustTick < 120)
		{
			talkDustTick++;
		}
		
		if (canProcess() && getHasBegunPortal() && !getHasFinishedPortal())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fThis portal will take you to the Outlands. I want you to go there, collect four termites, and grind them up.");
			}
			processTick = 0;
			LKQuestBase.rafikiQuest.setDelayed(false);
        	dataWatcher.updateObject(22, Byte.valueOf((byte)1));
			
			worldObj.setBlockToAir(-9, 105, -1);
			worldObj.setBlockToAir(-9, 105, 0);
			worldObj.setBlockToAir(-9, 106, -1);
			worldObj.setBlockToAir(-9, 106, 0);
			worldObj.setBlockToAir(-9, 107, -1);
			worldObj.setBlockToAir(-9, 107, 0);
			
			worldObj.createExplosion(this, -6, 106, -0.5, 0.0F, false);
			LKBlockOutlandsPortal.tryToCreatePortal(worldObj, -13, 105, -1);
			talkPortalTick = 0;
		}
		if (getHasFinishedPortal() && canTalkPortal() && !getHasSpokenAboutPortal())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fMay Mufasa guide you in that dreadful place...");
			}
        	dataWatcher.updateObject(23, Byte.valueOf((byte)1));
		}
		if (getHasTakenMango() && canTalkDust() && !getHasSpokenDust())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fI have long known that the great kings of the past are up there in the stars, watching over us.");
			}
        	dataWatcher.updateObject(26, Byte.valueOf((byte)1));
			talkDustTick = 0;
		}
		if (getHasSpokenDust() && canTalkDust() && !getHasSpokenMagic())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fBut it is only recently that I have discovered a way to bring them back to the Pride Lands.");
			}
        	dataWatcher.updateObject(27, Byte.valueOf((byte)1));
			talkDustTick = 0;
		}
		if (getHasSpokenMagic() && canTalkDust() && !getHasSpokenMagicDust())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fWith the materials you have given me, I can create a dust so magical that you can use it to undo Scar's work and return our king from the stars!");
			}
        	dataWatcher.updateObject(28, Byte.valueOf((byte)1));
			talkDustTick = 0;
		}
		if (getHasSpokenMagicDust() && canTalkDust() && !getHasGivenMagicDust())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere it is! Now, listen carefully and I will tell you what to do with it.");
				dropItem(mod_LionKing.lionDust.itemID, 4);
			}
        	dataWatcher.updateObject(29, Byte.valueOf((byte)1));
			talkDustTick = 0;
		}
		if (getHasGivenMagicDust() && canTalkDust() && !getHasSpokenAltar())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fYou need to use three of the dust and three silver ingots to craft a Star Altar.");
			}
        	dataWatcher.updateObject(30, Byte.valueOf((byte)1));
			talkDustTick = 0;
		}
		if (getHasSpokenAltar() && canTalkDust() && !getHasSpokenAltarUse())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fThen place it outside, and use the last dust on it. If you need more dust, bring me the two ingredients again.");
			}
        	dataWatcher.updateObject(31, Byte.valueOf((byte)1));
			talkDustTick = 0;
			LKQuestBase.rafikiQuest.setDelayed(false);
		}
		
		if (LKLevelData.ziraStage == 20 && !worldObj.isRemote)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fOhoho! Old Rafiki was never gone for good! But you've kicked up quite a stink here, haven't you?");
			LKLevelData.setZiraStage(21);
			processTick = 0;
		}
		
		if (LKLevelData.ziraStage == 21 && processTick > 80 && !worldObj.isRemote)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fNow's your chance to put things right. Go through that portal and put an end to Zira's outlandish scheme!");
			LKLevelData.setZiraStage(22);
			LKQuestBase.outlandsQuest.setDelayed(false);
		}
    }
	
	public void addHeartFX()
	{
		for (int i = 0; i < 7; i++)
		{
			double d = getRNG().nextGaussian() * 0.02D;
			double d1 = getRNG().nextGaussian() * 0.02D;
			double d2 = getRNG().nextGaussian() * 0.02D;
			worldObj.spawnParticle("heart", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
		}
	}

    private boolean canTalk()
    {
		return talkTick == 40;
    }

    private boolean canProcess()
    {
		return processTick == 100;
    }

    private boolean canTalkPortal()
    {
		return talkPortalTick == 120;
    }

    private boolean canTalkDust()
    {
		return talkDustTick == 120;
    }

	@Override
   	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("TheRealRafiki", isThisTheRealRafiki);
        if (dataWatcher.getWatchableObjectByte(17) == 1)
        {
			nbttagcompound.setBoolean("HasInteracted", true);
        }	
        if (dataWatcher.getWatchableObjectByte(18) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAboutStick", true);
        }
        if (dataWatcher.getWatchableObjectByte(19) == 1)
        {
            nbttagcompound.setBoolean("HasObtainedStick", true);
        }
        if (dataWatcher.getWatchableObjectByte(20) == 1)
        {
            nbttagcompound.setBoolean("HasSpawnedScar", true);
        }
        if (dataWatcher.getWatchableObjectByte(21) == 1)
        {
      		nbttagcompound.setBoolean("HasBegunPortal", true);
        }
        if (dataWatcher.getWatchableObjectByte(22) == 1)
        {
       		nbttagcompound.setBoolean("HasFinishedPortal", true);
        }
        if (dataWatcher.getWatchableObjectByte(23) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAboutPortal", true);
        }
       	if (dataWatcher.getWatchableObjectByte(24) == 1)
        {
        	nbttagcompound.setBoolean("HasTakenTermites", true);
        }
        if (dataWatcher.getWatchableObjectByte(25) == 1)
        {
            nbttagcompound.setBoolean("HasTakenMango", true);
        }
        if (dataWatcher.getWatchableObjectByte(26) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAboutDust", true);
        }
        if (dataWatcher.getWatchableObjectByte(27) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAboutMagic", true);
        }
        if (dataWatcher.getWatchableObjectByte(28) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAboutMagicDust", true);
        }
        if (dataWatcher.getWatchableObjectByte(29) == 1)
        {
            nbttagcompound.setBoolean("HasGivenMagicDust", true);
        }
        if (dataWatcher.getWatchableObjectByte(30) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAltar", true);
        }
        if (dataWatcher.getWatchableObjectByte(31) == 1)
        {
            nbttagcompound.setBoolean("HasSpokenAltarUse", true);
        }
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
		isThisTheRealRafiki = nbttagcompound.getBoolean("TheRealRafiki");
        dataWatcher.updateObject(17, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasInteracted") ? 1 : 0)));
        dataWatcher.updateObject(18, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAboutStick") ? 1 : 0)));
        dataWatcher.updateObject(19, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasObtainedStick") ? 1 : 0)));
        dataWatcher.updateObject(20, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpawnedScar") ? 1 : 0)));
        dataWatcher.updateObject(21, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasBegunPortal") ? 1 : 0)));
        dataWatcher.updateObject(22, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasFinishedPortal") ? 1 : 0)));
        dataWatcher.updateObject(23, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAboutPortal") ? 1 : 0)));
        dataWatcher.updateObject(24, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTakenTermites") ? 1 : 0)));
        dataWatcher.updateObject(25, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasTakenMango") ? 1 : 0)));
        dataWatcher.updateObject(26, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAboutDust") ? 1 : 0)));
        dataWatcher.updateObject(27, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAboutMagic") ? 1 : 0)));
        dataWatcher.updateObject(28, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAboutMagicDust") ? 1 : 0)));
        dataWatcher.updateObject(29, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasGivenMagicDust") ? 1 : 0)));
        dataWatcher.updateObject(30, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAltar") ? 1 : 0)));
        dataWatcher.updateObject(31, Byte.valueOf((byte)(nbttagcompound.getBoolean("HasSpokenAltarUse") ? 1 : 0)));
    }

    public boolean getHasInteracted()
    {
       	return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public boolean getHasSpokenAboutStick()
    {
       	return dataWatcher.getWatchableObjectByte(18) == 1 && getHasInteracted();
    }

    public boolean getHasObtainedStick()
    {
       	return dataWatcher.getWatchableObjectByte(19) == 1 && getHasSpokenAboutStick();
    }

    public boolean getHasSpawnedScar()
    {
       	return dataWatcher.getWatchableObjectByte(20) == 1 && getHasObtainedStick();
    }

    public boolean getHasBegunPortal()
    {
        return dataWatcher.getWatchableObjectByte(21) == 1 && getHasSpawnedScar();
    }

    public boolean getHasFinishedPortal()
    {
        return dataWatcher.getWatchableObjectByte(22) == 1 && getHasBegunPortal();
    }

    public boolean getHasSpokenAboutPortal()
    {
        return dataWatcher.getWatchableObjectByte(23) == 1 && getHasFinishedPortal();
    }

    public boolean getHasTakenTermites()
    {
        return dataWatcher.getWatchableObjectByte(24) == 1 && getHasSpokenAboutPortal();
    }

    public boolean getHasTakenMango()
    {
        return dataWatcher.getWatchableObjectByte(25) == 1 && getHasTakenTermites();
    }

    public boolean getHasSpokenDust()
    {
        return dataWatcher.getWatchableObjectByte(26) == 1 && getHasTakenMango();
    }

    public boolean getHasSpokenMagic()
    {
        return dataWatcher.getWatchableObjectByte(27) == 1 && getHasSpokenDust();
    }

    public boolean getHasSpokenMagicDust()
    {
        return dataWatcher.getWatchableObjectByte(28) == 1 && getHasSpokenMagic();
    }

    public boolean getHasGivenMagicDust()
    {
       	return dataWatcher.getWatchableObjectByte(29) == 1 && getHasSpokenMagicDust();
    }

    public boolean getHasSpokenAltar()
    {
       	return dataWatcher.getWatchableObjectByte(30) == 1 && getHasGivenMagicDust();
    }

    public boolean getHasSpokenAltarUse()
    {
        return dataWatcher.getWatchableObjectByte(31) == 1 && getHasSpokenAltar();
    }

	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (!getHasInteracted() && canTalk())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fOhoho! Welcome to the Pride Lands!");
			}
        	dataWatcher.updateObject(17, Byte.valueOf((byte)1));
			talkTick = 0;
			return true;
		}
		if (getHasInteracted() && canTalk() && itemstack != null)
		{
			if (itemstack.itemID == mod_LionKing.silver.itemID)
			{
				if (itemstack.stackSize > 3)
				{
					itemstack.stackSize -= 3;
					if (!worldObj.isRemote)
					{
						dropItem(mod_LionKing.rafikiCoin.itemID, 1);
						LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fThrow a coin on the ground and it will bring you to me!");
					}
					itemstack.stackSize -= 3;
				}
				else if (itemstack.stackSize == 3)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fThrow a coin on the ground and it will bring you to me!");
						dropItem(mod_LionKing.rafikiCoin.itemID, 1);
					}
				}
				else
				{
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fYou'll need three silver ingots if you want a coin.");
					}
				}
				talkTick = 0;
				return true;
			}
			if (itemstack.itemID == mod_LionKing.fur.itemID && entityplayer.inventory.hasItem(Item.book.itemID))
			{
				if (itemstack.stackSize > 1)
				{
					itemstack.stackSize--;
				}
				else
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
				entityplayer.inventory.consumeInventoryItem(Item.book.itemID);
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere's another Book of Quests.");
					dropItem(mod_LionKing.questBook.itemID, 1);
				}
				talkTick = 0;
				return true;
			}
			if (itemstack.itemID == Item.book.itemID && entityplayer.inventory.hasItem(mod_LionKing.fur.itemID))
			{
				if (itemstack.stackSize > 1)
				{
					itemstack.stackSize--;
				}
				else
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.fur.itemID);
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere's another Book of Quests.");
					dropItem(mod_LionKing.questBook.itemID, 1);
				}
				talkTick = 0;
				return true;
			}
		}
		if (!getHasSpokenAboutStick() && canTalk())
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fWhat's that? You like my stick? Well, bring old Rafiki a stack of hyena bones and you can have one yourself!");
			}
        	dataWatcher.updateObject(18, Byte.valueOf((byte)1));
			talkTick = 0;
			LKQuestBase.rafikiQuest.progress(1);
			return true;
		}
		if (!getHasObtainedStick() && canTalk())
		{
			if (itemstack != null && itemstack.itemID == mod_LionKing.hyenaBone.itemID && itemstack.stackSize == 64)
			{
            	entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(mod_LionKing.rafikiStick));
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere you go! If you want one again, bring me another stack of hyena bones.");
				}
        		dataWatcher.updateObject(19, Byte.valueOf((byte)1));
				entityplayer.triggerAchievement(LKAchievementList.getStick);
				talkTick = 0;
				LKQuestBase.rafikiQuest.setDelayed(true);
				LKQuestBase.rafikiQuest.progress(2);
				return true;
			}	
			else
			{
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.HYENA_BONES));
				}
				talkTick = 0;
				return true;
			}
		}
		if (getHasObtainedStick() && canTalk())
		{
			if (itemstack != null && itemstack.itemID == mod_LionKing.hyenaBone.itemID && itemstack.stackSize == 64)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(mod_LionKing.rafikiStick));
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere's another stick.");
				}
				talkTick = 0;
				return true;
			}
			else if (!getHasSpawnedScar())
			{
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fI hear Scar has returned to the Pride Lands, and he has trapped our king in the Star Realm! You must find Scar and kill him. My stick is the only weapon that can harm him!");
				}
        		dataWatcher.updateObject(20, Byte.valueOf((byte)1));
				talkTick = 0;
				
				if (worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && dimension == 0)
				{
					if (!worldObj.isRemote)
					{
						LKEntityScar scar = new LKEntityScar(worldObj);
						scar.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
						worldObj.spawnEntityInWorld(scar);
					}
				}
				else
				{
					while (!hasSpawnedOneScar)
					{
						int i = MathHelper.floor_double(posX);
						int j = MathHelper.floor_double(posY);
						int k = MathHelper.floor_double(posZ);

						int i1 = i + getRNG().nextInt(80) - getRNG().nextInt(80);
						int j1 = 18 + getRNG().nextInt(18);
						int k1 = k + getRNG().nextInt(80) - getRNG().nextInt(80);

						tryToSpawnScar(worldObj, i1, j1, k1);
					}
				}
				
				LKQuestBase.rafikiQuest.setDelayed(false);
				return true;
			}
		}
		if (!getHasBegunPortal() && canTalk())
		{
			if (LKQuestBase.rafikiQuest.getQuestStage() == 3)
			{
        		dataWatcher.updateObject(21, Byte.valueOf((byte)1));
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fAsante sana, squash banana, wewe nugu, mimi hapana...");
				}
				talkTick = 0;
				processTick = 0;
				LKQuestBase.rafikiQuest.setDelayed(true);
				LKQuestBase.rafikiQuest.progress(4);
				return true;
			}
			else
			{
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.MENTION_SCAR));
				}
				talkTick = 0;
				return true;
			}
		}
		if (getHasSpokenAboutPortal() && !getHasTakenTermites() && canTalk())
		{
			if (itemstack != null && itemstack.itemID == mod_LionKing.termiteDust.itemID && itemstack.stackSize >= 4)
			{
            	entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
            	entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
            	entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
            	entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
				for (int i = 0; i < 7; i++)
				{
					double d = getRNG().nextGaussian() * 0.02D;
					double d1 = getRNG().nextGaussian() * 0.02D;
					double d2 = getRNG().nextGaussian() * 0.02D;
					worldObj.spawnParticle("smoke", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
				}
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fWonderful! Now I just need four ground mango dust, and it will be ready...");
				}
        		dataWatcher.updateObject(24, Byte.valueOf((byte)1));
				talkTick = 0;
				LKQuestBase.rafikiQuest.progress(5);
				return true;
			}	
			else
			{
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.TERMITES));
				}
				talkTick = 0;
				return true;
			}
		}
		if (getHasTakenTermites() && !getHasTakenMango() && canTalk())
		{
			if (itemstack != null && itemstack.itemID == mod_LionKing.mangoDust.itemID && itemstack.stackSize >= 4)
			{
            	entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
				for (int i = 0; i < 7; i++)
				{
					double d = getRNG().nextGaussian() * 0.02D;
					double d1 = getRNG().nextGaussian() * 0.02D;
					double d2 = getRNG().nextGaussian() * 0.02D;
					worldObj.spawnParticle("smoke", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
				}
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fNow it is time!");
				}
        		dataWatcher.updateObject(25, Byte.valueOf((byte)1));
				talkTick = 0;
				talkDustTick = 0;
				LKQuestBase.rafikiQuest.setDelayed(true);
				LKQuestBase.rafikiQuest.progress(6);
				return true;
			}	
			else
			{
				if (!worldObj.isRemote)
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.MANGOES));
				}
				talkTick = 0;
				return true;
			}
		}
		if (getHasSpokenAltarUse() && canTalk())
		{
			if (itemstack != null && itemstack.itemID == mod_LionKing.mangoDust.itemID)
			{
				if (entityplayer.inventory.hasItem(mod_LionKing.termiteDust.itemID))
				{
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere's some more dust!");
						dropItem(mod_LionKing.lionDust.itemID, 1);
					}
					talkTick = 0;
					return true;
				}
			}
			if (itemstack != null && itemstack.itemID == mod_LionKing.termiteDust.itemID)
			{
				if (entityplayer.inventory.hasItem(mod_LionKing.mangoDust.itemID))
				{
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust.itemID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust.itemID);
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fHere's some more dust!");
						dropItem(mod_LionKing.lionDust.itemID, 1);
					}
					talkTick = 0;
					return true;
				}
			}
			else
			{
				if (!LKQuestBase.rafikiQuest.isComplete())
				{
					if (!worldObj.isRemote)
					{
						LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.STAR_ALTAR));
					}
					talkTick = 0;
					return true;
				}				
			}
		}
		if (canTalk() && LKQuestBase.rafikiQuest.isComplete() && (LKLevelData.ziraStage < 19 || LKLevelData.ziraStage > 21))
		{
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.HINT));
			}
			talkTick = 0;
			return true;				
		}
		return false;
	}

	private void tryToSpawnScar(World world, int i, int j, int k)
	{		
		if (canScarSpawnHere(world, i, j, k))
		{
			if (!worldObj.isRemote)
			{
				LKEntityScar scar = new LKEntityScar(world);
				scar.setLocationAndAngles(i, j, k, 0F, 0F);
				world.spawnEntityInWorld(scar);
			}
			hasSpawnedOneScar = true;
		}	
	}
	
	@Override
	protected String getLivingSound()
    {
        return getRNG().nextBoolean() ? "lionking:rafiki" : null;
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:rafiki";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:rafiki";
    }

	private boolean canScarSpawnHere(World world, int i, int j, int k)
	{
		return world.isAirBlock(i, j, k) && world.isAirBlock(i, j + 1, k) && world.isBlockOpaqueCube(i, j - 1, k);
	}

	@Override
    protected boolean canDespawn()
    {	
        return false;
    }

    public ItemStack getHeldItem()
    {
        return new ItemStack(mod_LionKing.rafikiStick);
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
        return worldObj.getBlockId(i, j - 1, k) == mod_LionKing.rafikiWood.blockID && worldObj.getBlockMetadata(i, j - 1, k) == 2 && worldObj.isAirBlock(i, j, k) ? 20.0F : -999999F;
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
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
