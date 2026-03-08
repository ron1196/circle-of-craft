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

public class LKEntityVulture extends EntityMob
{
    public boolean field_753_a;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;

    public LKEntityVulture(World world)
    {
        super(world);
        setSize(0.8F, 1.5F);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(16D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(3D);
    }
	
	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    protected void fall(float f) {}
	
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

	}

	@Override
    protected int getDropItemId()
    {
        return mod_LionKing.featherBlack.itemID;
    }
	
	@Override
    public boolean getCanSpawnHere()
    {
		if (posY < 60 && getRNG().nextInt(3) != 0)
		{
			return false;
		}
        return worldObj.difficultySetting > 0 && worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }
	
	@Override
    protected String getLivingSound()
    {
        return "lionking:vulture";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:vulturehurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:vulturehurt";
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
