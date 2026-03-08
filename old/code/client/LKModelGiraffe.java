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

import org.lwjgl.opengl.GL11;

public class LKModelGiraffe extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer neck;
	private ModelRenderer head;
	private ModelRenderer tail;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	public ModelRenderer tie;

	public LKModelGiraffe(float f)
	{
		body = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		body.addBox(-6F, -8F, -13F, 12, 16, 26, f);
		body.setRotationPoint(0F, -11F, 0F);
		
		neck = new ModelRenderer(this, 0, 44).setTextureSize(128, 64);
		neck.addBox(-4.5F, -13F, -4.5F, 9, 11, 9, f);
		neck.setTextureOffset(78, 0).addBox(-3F, -37F, -3F, 6, 40, 6, f);
		neck.setRotationPoint(0F, -14F, -7F);
		
		tie = new ModelRenderer(this, 78, 0).setTextureSize(128, 64);
		tie.addBox(-3F, -37F, -3F, 6, 40, 6, 0.5F);
		tie.setRotationPoint(0F, -14F, -7F);
		tie.showModel = false;
		
		head = new ModelRenderer(this, 96, 48).setTextureSize(128, 64);
		head.addBox(-3F, -43F, -6F, 6, 6, 10, f);
		head.setTextureOffset(10, 0).addBox(-4F, -45F, 1.5F, 1, 3, 2, f);
		head.setTextureOffset(17, 0).addBox(3F, -45F, 1.5F, 1, 3, 2, f);
		head.setTextureOffset(0, 0).addBox(-2.5F, -47F, 0F, 1, 4, 1, f);
		head.setTextureOffset(5, 0).addBox(1.5F, -47F, 0F, 1, 4, 1, f);
		head.setTextureOffset(76, 56).addBox(-2F, -41F, -11F, 4, 3, 5, f);
		head.setRotationPoint(0F, -14F, -7F);
		
		tail = new ModelRenderer(this, 104, 0).setTextureSize(128, 64);
		tail.addBox(-0.5F, 0F, 0F, 1, 24, 1, f);
		tail.setRotationPoint(0F, -12F, 13F);
		
		leg1 = new ModelRenderer(this, 112, 0).setTextureSize(128, 64);
		leg1.addBox(-2F, 0F, -2F, 4, 27, 4, f);
		leg1.setRotationPoint(-3.9F, -3F, 8F);
		
		leg2 = new ModelRenderer(this, 112, 0).setTextureSize(128, 64);
		leg2.addBox(-2F, 0F, -2F, 4, 27, 4, f);
		leg2.setRotationPoint(3.9F, -3F, 8F);
		leg2.mirror = true;
		
		leg3 = new ModelRenderer(this, 112, 0).setTextureSize(128, 64);
		leg3.addBox(-2F, 0F, -2F, 4, 27, 4, f);
		leg3.setRotationPoint(-3.9F, -3F, -7F);
		
		leg4 = new ModelRenderer(this, 112, 0).setTextureSize(128, 64);
		leg4.addBox(-2F, 0F, -2F, 4, 27, 4, f);
		leg4.setRotationPoint(3.9F, -3F, -7F);
		leg4.mirror = true;
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity.riddenByEntity != null && entity.riddenByEntity instanceof EntityPlayer)
		{
			neck.rotateAngleX = 0.5F * (float)Math.PI;
			neck.rotateAngleY = 0F;
			tie.rotateAngleX = 0.5F * (float)Math.PI;
			tie.rotateAngleY = 0F;
			head.rotateAngleX = 0F;
			head.rotateAngleY = 0F;
			head.setRotationPoint(0F, 25F, -48F);
		}
		else
		{
			setHeadAndNeckRotationAngles(f, f1, f2, f3, f4, f5);
			head.setRotationPoint(0F, -14F, -7F);
		}
        if (isChild)
        {
            float f6 = 2F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 8.0F * f5, 4.0F * f5);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24F * f5, 0.0F);
			head.render(f5);
			body.render(f5);
			neck.render(f5);
			tie.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
			tail.render(f5);
            GL11.glPopMatrix();
        }
		else
		{
			head.render(f5);
			body.render(f5);
			neck.render(f5);
			tie.render(f5);
			leg1.render(f5);
			leg2.render(f5);
			leg3.render(f5);
			leg4.render(f5);
			tail.render(f5);
		}
    }

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        leg1.rotateAngleX = 0.5F * MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = 0.5F * MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        leg3.rotateAngleX = 0.5F * MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        leg4.rotateAngleX = 0.5F * MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		tail.rotateAngleZ = 0.2F * MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
    }
	
	private void setHeadAndNeckRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		neck.rotateAngleX = (10F / 180F * (float)Math.PI) + (f4 / 57.29578F);
		head.rotateAngleX =(10F / 180F * (float)Math.PI) + (f4 / 57.29578F);
		tie.rotateAngleX = (10F / 180F * (float)Math.PI) + (f4 / 57.29578F);
		neck.rotateAngleY = f3 / 57.29578F;
		head.rotateAngleY = f3 / 57.29578F;
		tie.rotateAngleY = f3 / 57.29578F;
	}
}
