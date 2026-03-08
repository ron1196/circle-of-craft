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

public class LKEntityFlamingo extends EntityAnimal
{
    public boolean field_753_a;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
	public int fishingTick;

    public LKEntityFlamingo(World world)
    {
        super(world);
        field_753_a = false;
        field_752_b = 0.0F;
        destPos = 0.0F;
        field_755_h = 5.0F;
        setSize(0.6F, 1.8F);
        getNavigator().setAvoidsWater(false);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.3D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Item.fishRaw.itemID, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.2D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(10D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
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
        destPos += (double)((onGround || inWater) ? -1 : 4) * 0.29999999999999999D;
        if (destPos < 0.0F)
        {
            destPos = 0.0F;
        }
        if (destPos > 5.0F)
        {
            destPos = 1.0F;
        }
        if (!(onGround || inWater) && field_755_h < 1.0F)
        {
            field_755_h = 1.0F;
        }
        field_755_h *= 0.90000000000000002D;
        if (!(onGround || inWater) && motionY < 0.0D)
        {
            motionY *= 0.59999999999999998D;
        }
        field_752_b += field_755_h * 2.0F;
		
		if (!isChild() && !isInLove() && fishingTick == 0 && getRNG().nextInt(600) == 0)
		{
			int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY), MathHelper.floor_double(posZ));
			if (i == Block.waterStill.blockID)
			{
				fishingTick = 120;
			}
		}
		if (fishingTick > 0)
		{
			fishingTick--;
			for (int i = 0; i < 3; i++)
			{
				double d = posX - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
				double d1 = boundingBox.minY - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
				double d2 = posZ - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
				worldObj.spawnParticle("bubble", d, d1, d2, 0, 0, 0);
			}
		}
		
		if (isInLove() && fishingTick > 20)
		{
			fishingTick = 20;
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float f)
	{
		if (fishingTick > 20)
		{
			fishingTick = 20;
		}
		return super.attackEntityFrom(source, f);
	}

	@Override
    protected void fall(float f) {}

	@Override
    protected String getLivingSound()
    {
        return "lionking:flamingo";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:flamingohurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:flamingodeath";
    }

	@Override
    protected int getDropItemId()
    {
		return mod_LionKing.featherPink.itemID;
    }

	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.itemID == Item.fishRaw.itemID;
    }
	
	@Override
    public boolean getCanSpawnHere()
    {
		for (int i = -8; i < 9; i++)
		{
			for (int j = -8; j < 9; j++)
			{
				for (int k = -8; k < 9; k++)
				{
					int i1 = MathHelper.floor_double(posX) + i;
					int j1 = MathHelper.floor_double(posY) + j;
					int k1 = MathHelper.floor_double(posZ) + k;
					Block block = Block.blocksList[worldObj.getBlockId(i1, j1, k1)];
					if (block != null && block.blockMaterial == Material.water && super.getCanSpawnHere())
					{
						return true;
					}
				}
			}
		}
		return false;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LKEntityFlamingo(worldObj);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}
