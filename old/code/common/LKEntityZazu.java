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

public class LKEntityZazu extends EntityAnimal
{
    public boolean field_753_a;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    private int talkTick;

    public LKEntityZazu(World world)
    {
        super(world);
        field_753_a = false;
        field_752_b = 0.0F;
        destPos = 0.0F;
        field_755_h = 5.0F;
        setSize(0.6F, 0.7F);
        talkTick = 300;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.5D));
        tasks.addTask(2, new LKEntityAIZazuMate(this, 1.3D));
        tasks.addTask(3, new EntityAITempt(this, 1.3D, Item.seeds.itemID, false));
		tasks.addTask(3, new EntityAITempt(this, 1.3D, mod_LionKing.cornKernels.itemID, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.4D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(8D);
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
        field_756_e = field_752_b;
        field_757_d = destPos;
        destPos += (double)(onGround ? -1 : 4) * 0.29999999999999999D;
        if (destPos < 0.0F)
        {
            destPos = 0.0F;
        }
        if (destPos > 5.0F)
        {
            destPos = 1.0F;
        }
        if (!onGround && field_755_h < 1.0F)
        {
            field_755_h = 1.0F;
        }
        field_755_h *= 0.90000000000000002D;
        if (!onGround && motionY < 0.0D)
        {
            motionY *= 0.59999999999999998D;
        }
        field_752_b += field_755_h * 2.0F;
		if (talkTick < 300)
		{
			talkTick++;
		}
    }

    private boolean canTalk()
    {
		return talkTick == 300 && !isChild() && isMorning();
    }
	
    private boolean isMorning()
    {
		long l = worldObj.getWorldTime() % 24000;
		return l > 23000L || (l >= 0L && l < 4500L);
    }

	@Override
    protected void fall(float f) {}

	@Override
    protected String getLivingSound()
    {
        return "lionking:zazulive";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:zazuhurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:zazuhurt";
    }

	@Override
    protected int getDropItemId()
    {
		return mod_LionKing.featherBlue.itemID;
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = getRNG().nextInt(3);
        if (i > 0)
        {
            j += getRNG().nextInt(i + 1);
        }
        for (int k = 0; k < j; k++)
        {
			dropItem(getDropItemId(), 1);
        }
		if (getRNG().nextInt(5) > 1 && j > 0)
		{
			dropItem(mod_LionKing.featherYellow.itemID, 1);
		}
		if (getRNG().nextInt(4) == 0 && j > 0)
		{
			dropItem(mod_LionKing.featherRed.itemID, 1);
		}
    }

	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if ((itemstack == null || !isBreedingItem(itemstack)) && canTalk())
		{
			entityplayer.triggerAchievement(LKAchievementList.morningReport);
			if (!worldObj.isRemote)
			{
				LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.MORNING_REPORT));
			}
			talkTick = 0;
			return true;
        }
		return super.interact(entityplayer);
    }

	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.itemID == Item.seeds.itemID || itemstack.itemID == mod_LionKing.cornKernels.itemID;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LKEntityZazu(worldObj);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
