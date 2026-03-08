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

public class LKEntityRedDart extends LKEntityDart
{
	public LKEntityRedDart(World world)
	{
		super(world);
	}
	
	public LKEntityRedDart(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
    public LKEntityRedDart(World world, EntityLivingBase entityliving, float f, boolean flag)
    {
		super(world, entityliving, f, flag);
	}
	
	public ItemStack getDartItem()
	{
		return new ItemStack(mod_LionKing.dartRed);
	}
	
	public int getDamage()
	{
		return 6;
	}
	
	public void onHitEntity(Entity hitEntity)
	{
		if (shootingEntity != null)
		{
			hitEntity.addVelocity(-MathHelper.sin((shootingEntity.rotationYaw * 3.141593F) / 180F) * 0.45F, 0.10000000000000001D, MathHelper.cos((shootingEntity.rotationYaw * 3.141593F) / 180F) * 0.45F);
		}
		if (!hitEntity.isImmuneToFire())
		{
			hitEntity.setFire(silverFired ? worldObj.rand.nextInt(4) + 3 : worldObj.rand.nextInt(2) + 3);
		}
	}
	
	public void spawnParticles()
	{
		worldObj.spawnParticle("flame", posX, posY, posZ, -motionX * 0.1, -motionY * 0.1, -motionZ * 0.1);
	}
	
	public float getSpeedReduction()
	{
		return 0.02F;
	}
}
