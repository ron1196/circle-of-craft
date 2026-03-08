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

import lionking.client.*;

public class LKEntityBlackDart extends LKEntityDart
{
	public LKEntityBlackDart(World world)
	{
		super(world);
	}
	
	public LKEntityBlackDart(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
    public LKEntityBlackDart(World world, EntityLivingBase entityliving, float f, boolean flag)
    {
		super(world, entityliving, f, flag);
	}
	
	public ItemStack getDartItem()
	{
		return new ItemStack(mod_LionKing.dartBlack);
	}
	
	public int getDamage()
	{
		return 7;
	}
	
	public void onHitEntity(Entity hitEntity)
	{
		if (hitEntity instanceof LKEntityTermite && shootingEntity != null && shootingEntity instanceof EntityPlayer)
		{
			((EntityPlayer)shootingEntity).triggerAchievement(LKAchievementList.termite);
		}
		if (!worldObj.isRemote)
		{
			worldObj.createExplosion(null, hitEntity.posX, hitEntity.posY, hitEntity.posZ, silverFired ? 4.0F : 3.0F, true);
		}
		setDead();
	}
	
	public void spawnParticles()
	{
		mod_LionKing.proxy.spawnParticle("outlandsPortal", posX, posY - 0.8D, posZ, -motionX * 0.1, -motionY * 0.1, -motionZ * 0.1);
	}
	
	public float getSpeedReduction()
	{
		return 0.02F;
	}
	
	public void inGroundEvent()
	{
		if (!worldObj.isRemote)
		{
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY);
			int k = MathHelper.floor_double(posZ);
			worldObj.createExplosion(null, i, j, k, silverFired ? 4.0F : 3.0F, true);
		}
		setDead();
	}
}
