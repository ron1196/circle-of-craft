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

public class LKEntityThrownTermite extends EntityThrowable
{
    public LKEntityThrownTermite(World world)
    {
        super(world);
    }

    public LKEntityThrownTermite(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
    }

    public LKEntityThrownTermite(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

	@Override
    protected void onImpact(MovingObjectPosition movingobjectposition)
    {
        if (movingobjectposition.entityHit != null)
        {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0F);
        }
        if (!worldObj.isRemote)
        {
            worldObj.createExplosion(this, posX, posY, posZ, 1.8F, true);
        }
        for (int i = 0; i < 8; i++)
        {
            worldObj.spawnParticle("smoke", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }
        if (!worldObj.isRemote)
        {
            setDead();
        }
    }
}
