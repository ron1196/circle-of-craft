package lionking.client;
import lionking.common.*;
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
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.model.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;


import net.minecraft.src.*;

public class LKEntityCustomFX extends Entity
{
	private int particleTextureIndex;
	private boolean glow;
    private int particleAge = 0;
    private int particleMaxAge = 0;
    private float particleScale;
    private float particleGravity;
	private float particleScaleOverTime;

    public LKEntityCustomFX(World world, int texture, int age, boolean flag, double d, double d1, double d2, double d3, double d4, double d5)
    {
        super(world);
        setSize(0.2F, 0.2F);
        yOffset = height / 2.0F;
        setPosition(d, d1, d2);
        motionX = 0.009999999776482582D + d3;
        motionY = 0.109999999776482582D + d4;
        motionZ = 0.009999999776482582D + d5;
        particleScale = (world.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		particleScaleOverTime = particleScale;
        particleMaxAge = age;
        particleAge = 0;
		particleTextureIndex = texture;
		glow = flag;
    }

	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    protected void entityInit() {}

	@Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }

        moveEntity(motionX, motionY, motionZ);

        if (posY == prevPosY)
        {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }

        motionX *= 0.8600000143051147D;
        motionY *= 0.8600000143051147D;
        motionZ *= 0.8600000143051147D;

        if (onGround)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }
    }
	
	@Override
    public int getBrightnessForRender(float f)
    {
		if (glow)
		{
			float f1 = ((float)particleAge + f) / (float)particleMaxAge;

			if (f1 < 0.0F)
			{
				f1 = 0.0F;
			}

			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}

			int i = super.getBrightnessForRender(f);
			int j = i & 255;
			int k = i >> 16 & 255;
			j += (int)(f1 * 15.0F * 16.0F);

			if (j > 240)
			{
				j = 240;
			}

			return j | k << 16;
		}
		else
		{
			return super.getBrightnessForRender(f);
		}
    }

	@Override
    public float getBrightness(float f)
    {
		if (glow)
		{
			float f1 = ((float)particleAge + f) / (float)particleMaxAge;

			if (f1 < 0.0F)
			{
				f1 = 0.0F;
			}

			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}

			float f2 = super.getBrightness(f);
			return f2 * f1 + (1.0F - f1);
		}
		else
		{
			return super.getBrightness(f);
		}
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = ((float)particleAge + f) / (float)particleMaxAge * 32.0F;

        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        particleScale = particleScaleOverTime * f6;
		
        float f7 = (float)(particleTextureIndex % 16) / 16.0F;
        float f8 = f7 + 0.0624375F;
        float f9 = (float)(particleTextureIndex / 16) / 16.0F;
        float f10 = f9 + 0.0624375F;
        float f11 = 0.1F * particleScale;
        float f12 = (float)(prevPosX + (posX - prevPosX) * (double)f - EntityFX.interpPosX);
        float f13 = (float)(prevPosY + (posY - prevPosY) * (double)f - EntityFX.interpPosY);
        float f14 = (float)(prevPosZ + (posZ - prevPosZ) * (double)f - EntityFX.interpPosZ);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV((double)(f12 - f1 * f11 - f4 * f11), (double)(f13 - f2 * f11), (double)(f14 - f3 * f11 - f5 * f11), (double)f8, (double)f10);
        tessellator.addVertexWithUV((double)(f12 - f1 * f11 + f4 * f11), (double)(f13 + f2 * f11), (double)(f14 - f3 * f11 + f5 * f11), (double)f8, (double)f9);
        tessellator.addVertexWithUV((double)(f12 + f1 * f11 + f4 * f11), (double)(f13 + f2 * f11), (double)(f14 + f3 * f11 + f5 * f11), (double)f7, (double)f9);
        tessellator.addVertexWithUV((double)(f12 + f1 * f11 - f4 * f11), (double)(f13 - f2 * f11), (double)(f14 + f3 * f11 - f5 * f11), (double)f7, (double)f10);
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt) {}

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt) {}

	@Override
    public boolean canAttackWithItem()
    {
        return false;
    }
}
