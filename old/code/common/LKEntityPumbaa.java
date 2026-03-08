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
import java.util.List;

public class LKEntityPumbaa extends EntityCreature implements LKCharacter
{
	private int talkTick;
	private boolean spawningBox;
	
    public LKEntityPumbaa(World world)
    {
        super(world);
        setSize(1.2F, 1.3F);
		talkTick = 140;
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new LKEntityAIPumbaaFollowTimon(this, 1.2D));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
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
		if (worldObj.rand.nextInt(1200) == 0)
		{
			fart();
		}
		if (talkTick < 140)
		{
			talkTick++;
		}
		
		if (!worldObj.isRemote)
		{
			if (LKLevelData.pumbaaStage == 1 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fYou look down. Can we help?");
				LKLevelData.setPumbaaStage(2);
				talkTick = 40;
			}
			if (LKLevelData.pumbaaStage == 2 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fWhat's that you say? Outlanders have taken over Rafiki's tree?");
				LKLevelData.setPumbaaStage(3);
				talkTick = 20;
			}
			if (LKLevelData.pumbaaStage == 3 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fOutlanders? Man, I hate Outlanders. Almost as much as I hate hyenas, and I HATE hyenas.");
				LKLevelData.setPumbaaStage(4);
				talkTick = 20;
			}
			if (LKLevelData.pumbaaStage == 4 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fRafiki is an old friend of ours, but I don't think Timon and I could stand a chance against... wait! Timon!");
				LKLevelData.setPumbaaStage(5);
				talkTick = 0;
			}
			if (LKLevelData.pumbaaStage == 5 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7f*whispers*");
				LKLevelData.setPumbaaStage(6);
				talkTick = 60;
			}
			if (LKLevelData.pumbaaStage == 6 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fPumbaa, that's the most ridiculous idea I've ever heard.");
				LKLevelData.setPumbaaStage(7);
				talkTick = 40;
			}
			if (LKLevelData.pumbaaStage == 7 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fHold on! I just had a great idea! Pumbaa here could - er, pass gas, and those Outlanders would move out of that tree faster than a wildebeest stampede!");
				LKLevelData.setPumbaaStage(8);
				talkTick = 0;
			}
			if (LKLevelData.pumbaaStage == 8 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fI'm not going within a hundred blocks of those vicious lions. And you'd need some very powerful flatulence. I know! I'll give you a Pumbbox!");
				LKLevelData.setPumbaaStage(9);
				talkTick = 0;
			}
			if (LKLevelData.pumbaaStage == 9 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fBring Pumbaa some planks, sixteen bugs, a jar of lava and an Exploding Termite, and we'll cook up some weapons of gas destruction.");
				LKLevelData.setPumbaaStage(10);
				talkTick = 0;
				LKQuestBase.outlandsQuest.progress(7);
			}
			if (LKLevelData.pumbaaStage == 12 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Timon> \u00a7fThere you go! Now take that boom box up to Rafiki's tree and teach those Outlanders not to mess with our Pride Lands!");
				LKLevelData.setPumbaaStage(13);
				talkTick = 0;
			}
		}

		boolean box = spawningBox || LKLevelData.pumbaaStage == 11;
		
		if (box && talkTick > 20 && talkTick < 48 && talkTick % 4 == 0)
		{
			worldObj.playSoundAtEntity(this, "random.eat", 0.8F + 0.5F * (float)getRNG().nextInt(2), (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F);
		}
		
		if (box && talkTick > 100)
		{
			spawnBox();
			if (LKLevelData.pumbaaStage == 11 && !worldObj.isRemote)
			{
				LKLevelData.setPumbaaStage(12);
				LKQuestBase.outlandsQuest.setDelayed(false);
			}
		}
	}
	
	private void spawnBox()
	{
		if (!worldObj.isRemote)
		{
			motionX = 0;
			motionY = 1.5D;
			motionZ = 0;
			EntityItem item = new EntityItem(worldObj, posX + 0.5D, posY + 0.5D, posZ + 0.5D, new ItemStack(mod_LionKing.pumbaaBox));
			worldObj.spawnEntityInWorld(item);
			spawningBox = false;
		}
		fart();
	}
	
	public void fart()
	{
		worldObj.playSoundAtEntity(this, "lionking:flatulence", getSoundVolume(), (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		for (int i = 0; i < 14; i++)
		{
            double d = getRNG().nextGaussian() * 0.02D;
            double d1 = getRNG().nextGaussian() * 0.02D;
            double d2 = getRNG().nextGaussian() * 0.02D;
            worldObj.spawnParticle("smoke", (posX + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(getRNG().nextFloat() * height), (posZ + (double)(getRNG().nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (!worldObj.isRemote)
		{
			if (LKQuestBase.outlandsQuest.getQuestStage() == 6 && LKLevelData.pumbaaStage == 0 && talkTick == 140)
			{
				LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fHi there, kid.");
				LKLevelData.setPumbaaStage(1);
				talkTick = 40;
				return true;
			}
			if (LKLevelData.pumbaaStage == 10 && talkTick == 140)
			{
				ItemStack[] inv = entityplayer.inventory.mainInventory;
				int bugs = 0;
				for (int i = 0; i < inv.length; i++)
				{
					ItemStack itemstack = inv[i];
					if (itemstack != null && itemstack.itemID == mod_LionKing.bug.itemID)
					{
						bugs += itemstack.stackSize;
					}
				}
				
				if (bugs >= 16 && entityplayer.inventory.hasItem(mod_LionKing.planks.blockID) && entityplayer.inventory.hasItem(mod_LionKing.jarLava.itemID) && entityplayer.inventory.hasItem(mod_LionKing.itemTermite.itemID))
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fStand back!");
					for (int i = 0; i < 16; i++)
					{
						entityplayer.inventory.consumeInventoryItem(mod_LionKing.bug.itemID);
					}
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.planks.blockID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.jarLava.itemID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.itemTermite.itemID);
					LKLevelData.setPumbaaStage(11);
					talkTick = 0;
					LKQuestBase.outlandsQuest.setDelayed(true);
					LKQuestBase.outlandsQuest.progress(8);
					return true;
				}
				else
				{
					String[] speeches = new String[]
					{
						"\u00a7e<Pumbaa> \u00a7fI'll need sixteen bugs to get myself going, a jar of lava and an Exploding Termite to heat things up, and some planks to put it all in.",
						"\u00a7e<Timon> \u00a7fThe flatulence needs to be extremely powerful for there to be even a hope of this working. Get us those ingredients!",
						"\u00a7e<Timon> \u00a7fBring Pumbaa planks, sixteen bugs, a termite and a jar of lava and he'll give you the flatulence.",
						"\u00a7e<Pumbaa> \u00a7fKid, we want to help, but I just don't have those ingredients yet!"
					};
					LKIngame.sendMessageToAllPlayers(speeches[getRNG().nextInt(speeches.length)]);
					talkTick = 80;
					return true;
				}
			}
			if (LKLevelData.pumbaaStage == 13 && LKQuestBase.outlandsQuest.getQuestStage() == 8)
			{
				ItemStack[] inv = entityplayer.inventory.mainInventory;
				int bugs = 0;
				for (int i = 0; i < inv.length; i++)
				{
					ItemStack itemstack = inv[i];
					if (itemstack != null && itemstack.itemID == mod_LionKing.bug.itemID)
					{
						bugs += itemstack.stackSize;
					}
				}
				
				if (!(entityplayer.inventory.hasItem(mod_LionKing.pumbaaBox.blockID)) && bugs >= 16 && entityplayer.inventory.hasItem(mod_LionKing.planks.blockID) && entityplayer.inventory.hasItem(mod_LionKing.jarLava.itemID) && entityplayer.inventory.hasItem(mod_LionKing.itemTermite.itemID))
				{
					LKIngame.sendMessageToAllPlayers("\u00a7e<Pumbaa> \u00a7fStand back!");
					for (int i = 0; i < 16; i++)
					{
						entityplayer.inventory.consumeInventoryItem(mod_LionKing.bug.itemID);
					}
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.planks.blockID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.jarLava.itemID);
					entityplayer.inventory.consumeInventoryItem(mod_LionKing.itemTermite.itemID);
					spawningBox = true;
					talkTick = 0;
					return true;
				}
			}
		}
		return false;
	}

	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
    protected String getLivingSound()
    {
        return "mob.pig";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.pig";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.pigdeath";
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
