package lionking.common;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.*;
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

public class LKEntityZira extends EntityCreature implements LKCharacter, LKAngerable
{
	private int talkTick;
	public int selfTalkTick;
	
	public LKEntityZira(World world)
	{
		super(world);
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, 1.7D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
		talkTick = 40;
		selfTalkTick = 80;
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(300D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
    }
	
	public boolean isHostile()
	{
		return LKLevelData.ziraStage >= 27;
	}
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 0;
	}
	
	public void spawnOutlandersInMound()
	{
		LKLevelData.setOutlandersHostile(1);
		
		for (int i1 = 0; i1 < 6; i1++)
		{
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);

			LKEntityOutlander outlander = getRNG().nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
			outlander.inMound = true;
			outlander.setLocationAndAngles(i + 0.5D, j, k + 0.5D, getRNG().nextFloat() * 360F, 0F);

			if (!worldObj.isRemote)
			{
				worldObj.spawnEntityInWorld(outlander);
			}
			outlander.spawnExplosionParticle();
		}
	}
	
	public void spawnOutlandersInTree()
	{
		for (int i1 = 0; i1 < 6; i1++)
		{
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);

			LKEntityOutlander outlander = getRNG().nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
			outlander.inMound = true;
			outlander.setLocationAndAngles(i + 0.5D, j, k + 0.5D, getRNG().nextFloat() * 360F, 0F);

			if (!worldObj.isRemote)
			{
				worldObj.spawnEntityInWorld(outlander);
			}
			outlander.spawnExplosionParticle();
		}
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
		
		if (!worldObj.isRemote && getHealth() < getMaxHealth() && LKLevelData.ziraStage < 27)
		{
        	setHealth(getMaxHealth());
		}
		if (talkTick < 40)
		{
			talkTick++;
		}
		if (selfTalkTick < 80)
		{
			selfTalkTick++;
		}
		
		EntityPlayer closestPlayer = worldObj.getClosestPlayerToEntity(this, 24D);
		if (closestPlayer != null && LKQuestBase.outlandsQuest.getQuestStage() == 0 && LKQuestBase.outlandsQuest.isDelayed())
		{
			LKQuestBase.outlandsQuest.setDelayed(false);
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fDo not attack! Let me deal with this fool.");
			}
			LKLevelData.setOutlandersHostile(0);
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i) instanceof LKEntityOutlander)
				{
					LKEntityOutlander outlander = (LKEntityOutlander)list.get(i);
					if (outlander.inMound && outlander.getAttackTarget() instanceof EntityPlayer)
					{
						outlander.setAttackTarget(null);
					}
				}
			}
		}
		
		if (getRNG().nextInt(400) == 0 && !worldObj.isRemote)
		{
			int outlanders = 0;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i) instanceof LKEntityOutlander)
				{
					LKEntityOutlander outlander = (LKEntityOutlander)list.get(i);
					if (outlander.inMound)
					{
						outlanders++;
					}
				}
			}
		
			if (outlanders < 16)
			{
				boolean spawnedOutlander = false;
				int attempts = 0;
				while (!spawnedOutlander && attempts < 256)
				{
					int i; 
					int j;
					int k;
					Block spawnBlock;
					if (LKLevelData.ziraStage < 14)
					{
						i = MathHelper.floor_double(posX) + getRNG().nextInt(33) - 16;
						j = MathHelper.floor_double(posY) + getRNG().nextInt(33) - 10;
						k = MathHelper.floor_double(posZ) + getRNG().nextInt(33) - 16;
						spawnBlock = mod_LionKing.outlandsPortalFrame;
					}
					else
					{
						i = MathHelper.floor_double(posX) + getRNG().nextInt(9) - 4;
						j = MathHelper.floor_double(posY) + getRNG().nextInt(3) - 1;
						k = MathHelper.floor_double(posZ) + getRNG().nextInt(9) - 4;
						spawnBlock = mod_LionKing.rafikiWood;
					}
					if (worldObj.getBlockId(i, j, k) == spawnBlock.blockID && worldObj.isAirBlock(i, j + 1, k))
					{
						LKEntityOutlander outlander = getRNG().nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
						outlander.inMound = true;
						outlander.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, getRNG().nextFloat() * 360F, 0F);
						if (outlander.getCanSpawnHere())
						{
							worldObj.spawnEntityInWorld(outlander);
							outlander.spawnExplosionParticle();
							spawnedOutlander = true;
						}
					}
					else
					{
						attempts++;
					}
				}
			}
		}
		
		if (LKLevelData.ziraStage == 11 && selfTalkTick == 80 && !worldObj.isRemote)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fNow I can finally leave this accursed wasteland! Come, Outlanders, and let us reclaim what was once ours.");
			LKLevelData.setZiraStage(12);
			selfTalkTick = 0;
		}
		
		if (LKLevelData.ziraStage == 12 && selfTalkTick == 80)
		{
			LKLevelData.setZiraStage(13);
			selfTalkTick = 0;
			LKQuestBase.outlandsQuest.setDelayed(false);
			worldObj.playSoundAtEntity(this, "portal.travel", 1.0F, getRNG().nextFloat() * 0.4F + 0.8F);
			for (int i = 0; i < 24; i++)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = (getRNG().nextGaussian() * 0.02D) + (double)(getRNG().nextFloat() * 0.5F);
				double d2 = getRNG().nextGaussian() * 0.02D;
				worldObj.spawnParticle("flame", (posX + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), posY + 0.25F + (double)(getRNG().nextFloat() * height), (posZ + (((double)(getRNG().nextFloat() * width * 2.0F)) - (double)width) * 0.75F), d, d1, d2);
			}
		}
		
		if (LKLevelData.ziraStage == 13 && !worldObj.isRemote)
		{
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(64D, 64D, 64D));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i) instanceof LKEntityOutlander)
				{
					LKEntityOutlander outlander = (LKEntityOutlander)list.get(i);
					if (outlander.inMound)
					{
						for (int j = 0; j < 24; j++)
						{
							double d = getRNG().nextGaussian() * 0.02D;
							double d1 = (getRNG().nextGaussian() * 0.02D) + (double)(getRNG().nextFloat() * 0.5F);
							double d2 = getRNG().nextGaussian() * 0.02D;
							worldObj.spawnParticle("flame", (outlander.posX + (((double)(getRNG().nextFloat() * outlander.width * 2.0F)) - (double)outlander.width) * 0.75F), outlander.posY + 0.25F + (double)(getRNG().nextFloat() * outlander.height), (outlander.posZ + (((double)(getRNG().nextFloat() * outlander.width * 2.0F)) - (double)outlander.width) * 0.75F), d, d1, d2);
						}
						outlander.setDead();
					}
				}
			}
			
			setDead();
		}
		
		if (LKQuestBase.outlandsQuest.getQuestStage() == 8 && LKLevelData.ziraStage == 17 && !worldObj.isRemote)
		{
			EntityPlayer chatPlayer = (EntityPlayer)worldObj.playerEntities.get(0);
			if (chatPlayer != null && getDistanceToEntity(chatPlayer) < 16F && chatPlayer.inventory.hasItem(mod_LionKing.pumbaaBox.blockID))
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fWhat? You have a weapon from the warthog? How dare you turn against us! Outlanders, attack this traitor!");
				LKLevelData.setZiraStage(18);
				LKLevelData.setOutlandersHostile(1);
			}
		}
		
		if (LKLevelData.ziraStage == 19)
		{
			if (!worldObj.isRemote)
			{
				setDead();
			}
		}
		
		if (LKLevelData.ziraStage == 23 && !worldObj.isRemote && selfTalkTick == 40)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fYou dare to return and challenge me?");
			LKLevelData.setZiraStage(24);
			selfTalkTick = 0;
		}
		
		if (LKLevelData.ziraStage == 24 && !worldObj.isRemote && selfTalkTick == 80)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fYou are a traitor, and I shall dispose of you. Now face the wrath of the Termite Queen!");
			LKLevelData.setZiraStage(25);
			selfTalkTick = 0;
		}

		if (LKLevelData.ziraStage == 25 && selfTalkTick > 40)
		{		
			if (!worldObj.isRemote)
			{
				LKEntityTermiteQueen queen = new LKEntityTermiteQueen(worldObj);
				queen.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
				worldObj.spawnEntityInWorld(queen);
				mountEntity(queen);
				queen.motionY = 1D;
				LKLevelData.setZiraStage(26);
			}
		}
		
		if (LKLevelData.ziraStage == 26 && ridingEntity == null)
		{
			List mounts = worldObj.getEntitiesWithinAABB(LKEntityTermiteQueen.class, boundingBox.expand(64D, 64D, 64D));
			if (!mounts.isEmpty())
			{
				LKEntityTermiteQueen mount = (LKEntityTermiteQueen)mounts.get(0);
				if (mount != null && mount.riddenByEntity == null)
				{
					mountEntity(mount);
				}
			}
		}
		
		if (LKLevelData.ziraStage > 25)
		{
			if (getRNG().nextInt(3) == 0)
			{
				double d = getRNG().nextGaussian() * 0.02D;
				double d1 = getRNG().nextGaussian() * 0.02D;
				double d2 = getRNG().nextGaussian() * 0.02D;
				worldObj.spawnParticle((getRNG().nextInt(3) == 0 ? "smoke" : "flame"), (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.0D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}
		}
		
		if (LKLevelData.ziraStage == 27 && getHealth() <= 120F && !worldObj.isRemote)
		{
			LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fOutlanders! Finish this!");

			for (int i1 = 0; i1 < 4; i1++)
			{
				int i = MathHelper.floor_double(posX) - 6 + getRNG().nextInt(13);
				int k = MathHelper.floor_double(posZ) - 6 + getRNG().nextInt(13);
				
				LKEntityOutlander outlander = getRNG().nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
				outlander.inMound = true;
				outlander.setLocationAndAngles(i, worldObj.getHeightValue(i, k), k, 0, 0);
				worldObj.spawnEntityInWorld(outlander);
				worldObj.spawnEntityInWorld(new LKEntityLightning(null, worldObj, (double)i, (double)worldObj.getHeightValue(i, k), (double)k, 0));
			}
			
			LKLevelData.setZiraStage(28);
		}
		
		if (LKLevelData.ziraStage == 28 && !worldObj.isRemote && getRNG().nextInt(160) == 0)
		{
			int outlanders = 0;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i) instanceof LKEntityOutlander)
				{
					LKEntityOutlander outlander = (LKEntityOutlander)list.get(i);
					if (outlander.inMound)
					{
						outlanders++;
					}
				}
			}
		
			if (outlanders < 6)
			{
				int i = MathHelper.floor_double(posX) - 6 + getRNG().nextInt(13);
				int k = MathHelper.floor_double(posZ) - 6 + getRNG().nextInt(13);
				
				LKEntityOutlander outlander = getRNG().nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
				outlander.inMound = true;
				outlander.setLocationAndAngles(i, worldObj.getHeightValue(i, k), k, 0, 0);
				worldObj.spawnEntityInWorld(outlander);
				worldObj.createExplosion(outlander, i, worldObj.getHeightValue(i, k) + 1, k, 0F, false);
			}
		}
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (talkTick == 40 && !worldObj.isRemote)
		{
			if (LKLevelData.ziraStage == 0 && LKQuestBase.outlandsQuest.getQuestStage() == 1)
			{	
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fSo you're the one who killed Scar.");
				LKLevelData.setZiraStage(1);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 1)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fI had sworn revenge against whoever vanquished him... but you look like a dangerous enemy.");
				LKLevelData.setZiraStage(2);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 2)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fPerhaps you could assist me in a little - er, mission - instead?");
				LKLevelData.setZiraStage(3);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 3)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fThere might be some rewards in it for you too - and of course, if you don't want to help, I'm sure these hungry, hungry Outlanders will be more than willing to take care of you.");
				LKLevelData.setZiraStage(4);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 4)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fI knew you'd accept my offer. You can start by getting me five kivulite and two silver ingots.");
				LKLevelData.setZiraStage(5);
				talkTick = 0;
				LKQuestBase.outlandsQuest.progress(2);
				return true;
			}
			if (LKLevelData.ziraStage == 5)
			{
				ItemStack[] inv = entityplayer.inventory.mainInventory;
				int silver = 0;
				int kivulite = 0;
				for (int i = 0; i < inv.length; i++)
				{
					ItemStack itemstack = inv[i];
					if (itemstack != null && itemstack.itemID == mod_LionKing.silver.itemID)
					{
						silver += itemstack.stackSize;
					}
					if (itemstack != null && itemstack.itemID == mod_LionKing.kivulite.itemID)
					{
						kivulite += itemstack.stackSize;
					}
				}
				
				if (silver >= 2 && kivulite >= 5)
				{
					LKWorldGenZiraMound.clearPoolCover(worldObj);
					LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fExcellent! Now, I've opened up a lower cavern in this mound. There's a pool of Outwater in there - throw the ingots in and come back.");
					LKLevelData.setZiraStage(6);
					talkTick = 0;
					LKQuestBase.outlandsQuest.progress(3);
					return true;
				}
				else
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.ZIRA_INGOTS));
					talkTick = 0;
					return true;
				}
			}
			if (LKLevelData.ziraStage == 6)
			{
				if (entityplayer.inventory.hasItem(mod_LionKing.outlandsHelm.itemID))
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fAh, the Outlandish Helm! That has some very useful tricks.");
					LKLevelData.setZiraStage(7);
					talkTick = 0;
					LKQuestBase.outlandsQuest.setDelayed(true);
					LKQuestBase.outlandsQuest.progress(4);
					return true;
				}
				else
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fGo and throw the kivulite and silver into the Outwater.");
					talkTick = 0;
					return true;
				}
			}
			if (LKLevelData.ziraStage == 7)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fDid you know that wild Outlanders won't attack while you're wearing it?");
				LKLevelData.setZiraStage(8);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 8)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fNow what was it I wanted? Oh yes, three Wayward Feathers.");
				LKLevelData.setZiraStage(9);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 9)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fYou'll need to gather blue, yellow and red feathers from Zazus, and black feathers from vultures, and throw them all into the Outwater. What are you waiting for?");
				LKLevelData.setZiraStage(10);
				talkTick = 0;
				LKQuestBase.outlandsQuest.setDelayed(false);
				return true;
			}
			if (LKLevelData.ziraStage == 10)
			{
				ItemStack[] inv = entityplayer.inventory.mainInventory;
				int feathers = 0;
				for (int i = 0; i < inv.length; i++)
				{
					ItemStack itemstack = inv[i];
					if (itemstack != null && itemstack.itemID == mod_LionKing.outlandsFeather.itemID)
					{
						feathers += itemstack.stackSize;
					}
				}
				
				if (feathers >= 3)
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fWell, you've done better than I expected.");
					for (int i = 0; i < 3; i++)
					{
						entityplayer.inventory.consumeInventoryItem(mod_LionKing.outlandsFeather.itemID);
					}
					LKLevelData.setZiraStage(11);
					talkTick = 0;
					selfTalkTick = 0;
					LKQuestBase.outlandsQuest.setDelayed(true);
					LKQuestBase.outlandsQuest.progress(5);
					return true;
				}
				else
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.ZIRA_FEATHERS));
					talkTick = 0;
					return true;
				}
			}
			if (LKLevelData.ziraStage == 14)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fAh, the Pride Lands! Just as I remember them. This tree will serve well as the starting point for our conquest.");
				LKLevelData.setZiraStage(15);
				talkTick = 0;
				LKQuestBase.outlandsQuest.setDelayed(true);
				LKQuestBase.outlandsQuest.progress(6);
				return true;
			}
			if (LKLevelData.ziraStage == 15)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7fAnd don't worry, I disposed of that idiotic Rafiki who lived here. Would you like to hear the old fool's last words before we removed him?");
				LKLevelData.setZiraStage(16);
				talkTick = 0;
				return true;
			}
			if (LKLevelData.ziraStage == 16)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Zira> \u00a7f'Find Timon and Pumbaa! They'll know what to do!' It almost sounded as if he were leaving you an instruction - but I know you wouldn't even consider betraying the Outlanders.");
				LKLevelData.setZiraStage(17);
				talkTick = 0;
				LKQuestBase.outlandsQuest.setDelayed(false);
				return true;
			}
			if (LKLevelData.ziraStage == 17)
			{
				LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.ZIRA_CONQUEST));
				talkTick = 0;
				return true;
			}
		}
		return false;
	}
	
	@Override
    protected Entity findPlayerToAttack()
    {
        if (!isHostile())
        {
            return null;
        }
		else
        {
        	EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);
       		if (entityplayer != null && canEntityBeSeen(entityplayer))
        	{
            	return entityplayer;
        	}
			else
        	{
            	return null;
        	}
        }
    }
	
	@Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if (damagesource.getEntity() instanceof EntityPlayer && LKLevelData.ziraStage == 28)
        {
			LKLevelData.setZiraStage(29);
			LKQuestBase.outlandsQuest.progress(10);
            EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			entityplayer.triggerAchievement(LKAchievementList.killZira);
			
			List outlanders = worldObj.getEntitiesWithinAABB(LKEntityOutlander.class, boundingBox.addCoord(18F, 18F, 18F).addCoord(-18F, -18F, -18F));
			{
				for (int l = 0; l < outlanders.size(); l++)
				{
					LKEntityOutlander outlander = (LKEntityOutlander)outlanders.get(l);
					if (!worldObj.isRemote && outlander.inMound)
					{
						outlander.setDead();
					}
				}
			}
			List termites = worldObj.getEntitiesWithinAABB(LKEntityTermite.class, boundingBox.addCoord(18F, 18F, 18F).addCoord(-18F, -18F, -18F));
			{
				for (int l = 0; l < termites.size(); l++)
				{
					LKEntityTermite termite = (LKEntityTermite)termites.get(l);
					if (!worldObj.isRemote)
					{
						termite.setDead();
					}
				}
			}
			
			if (!worldObj.isRemote)
			{
				worldObj.createExplosion(this, posX, posY, posZ, 0F, false);

				for (int i1 = 0; i1 < getRNG().nextInt(7) + 7; i1++)
				{
					dropItem(mod_LionKing.outlanderFur.itemID, 1);
				}
				for (int i1 = 0; i1 < getRNG().nextInt(7) + 7; i1++)
				{
					dropItem(mod_LionKing.outlanderMeat.itemID, 1);
				}
				
				for (int i1 = 0; i1 < 20; i1++)
				{
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, getRNG().nextInt(30) + 36));
				}
				
				dropItem(mod_LionKing.ziraRug.itemID, 1);
				
				for (int i1 = 0; i1 < 5; i1++)
				{
					int i = MathHelper.floor_double(posX) - 12 + getRNG().nextInt(25);
					int k = MathHelper.floor_double(posZ) - 12 + getRNG().nextInt(25);
					worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, (double)i, (double)worldObj.getHeightValue(i, k), (double)k));
				}
				
				setDead();
			}
        }
    }
	
	@Override
    protected void attackEntity(Entity entity, float f)
    {
        if (attackTime <= 0 && f < 2.4F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            attackEntityAsMob(entity);
        }
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5F);
    }
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (LKLevelData.ziraStage < 27)
		{
			return false;
		}
		if (damagesource == DamageSource.outOfWorld || (damagesource.getEntity() != null && damagesource.getEntity() instanceof EntityPlayer))
		{
			return super.attackEntityFrom(damagesource, f);
		}
		return false;
	}
	
	@Override
    protected void kill()
    {
        setDead();
    }
	
	@Override
	protected String getLivingSound()
    {
        return getRNG().nextBoolean() ? "lionking:lion" : null;
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:lionroar";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:lionangry";
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		if (LKLevelData.ziraStage < 14)
		{
			if (j > posY && posY > (double)LKLevelData.moundY + 24)
			{
				return -999999F;
			}
			if (j < posY && posY < (double)LKLevelData.moundY + 16)
			{
				return -999999F;
			}
			if (worldObj.getBlockId(i, j - 1, k) == mod_LionKing.outlandsPortalFrame.blockID && worldObj.getBlockMetadata(i, j - 1, k) == 1 && worldObj.isAirBlock(i, j, k))
			{
				return 10.0F;
			}
		}
		if (LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19)
		{
			return worldObj.getBlockId(i, j - 1, k) == mod_LionKing.rafikiWood.blockID && worldObj.getBlockMetadata(i, j - 1, k) == 2 && worldObj.isAirBlock(i, j, k) ? 10.0F : -999999F;
		}
		return 0.0F;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
