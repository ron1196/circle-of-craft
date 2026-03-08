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

public class LKEntityBlueDart extends LKEntityDart
{
	public LKEntityBlueDart(World world)
	{
		super(world);
	}
	
	public LKEntityBlueDart(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
    public LKEntityBlueDart(World world, EntityLivingBase entityliving, float f, boolean flag)
    {
		super(world, entityliving, f, flag);
	}
	
	public ItemStack getDartItem()
	{
		return new ItemStack(mod_LionKing.dartBlue);
	}
	
	public int getDamage()
	{
		return 7;
	}
	
	public void onHitEntity(Entity hitEntity) {}
	
	public void spawnParticles() {}
	
	public float getSpeedReduction()
	{
		return 0.05F;
	}
}
