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

public class LKEntityZazuEgg extends EntityThrowable
{
    public LKEntityZazuEgg(World world)
    {
        super(world);
    }

    public LKEntityZazuEgg(World world, EntityLivingBase entityliiving)
    {
        super(world, entityliiving);
    }

    public LKEntityZazuEgg(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

	@Override
    protected void onImpact(MovingObjectPosition object)
    {
        if (object.entityHit != null)
        {
            object.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0F);
        }

        if (!worldObj.isRemote && worldObj.rand.nextBoolean())
        {
			LKEntityZazu zazu = new LKEntityZazu(worldObj);
			zazu.setGrowingAge(-24000);
			zazu.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
			worldObj.spawnEntityInWorld(zazu);
        }
		
        for (int i = 0; i < 8; ++i)
        {
            worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }
}
