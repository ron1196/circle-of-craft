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

public class LKModelTermiteQueen extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer tail1;
	private ModelRenderer tail2;
	private ModelRenderer head;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	private ModelRenderer leg5;
	private ModelRenderer leg6;
	
	public LKModelTermiteQueen()
	{
		body = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
		body.addBox(-5F, -4F, 0F, 10, 8, 26);
		body.setRotationPoint(0F, 13F, -11F);
		
		tail1 = new ModelRenderer(this, 0, 35).setTextureSize(128, 64);
		tail1.addBox(-4F, -3F, 0F, 8, 6, 12);
		tail1.setRotationPoint(0F, 13F, 14F);
		
		tail2 = new ModelRenderer(this, 42, 37).setTextureSize(128, 64);
		tail2.addBox(-3F, -2F, 0F, 6, 4, 12);
		tail2.setRotationPoint(0F, 13F, 25F);
		
		head = new ModelRenderer(this, 47, 11).setTextureSize(128, 64);
		head.addBox(-3F, -3F, -8F, 6, 6, 8);
		head.setRotationPoint(0F, 13F, -11F);
		head.setTextureOffset(47, 4).addBox(-2.5F, -2.5F, -13F, 1, 1, 5);
		head.setTextureOffset(47, 4).addBox(1.5F, -2.5F, -13F, 1, 1, 5);
		
		leg2 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg2.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg2.setRotationPoint(5F, 13F, -8F);
		leg2.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
		
		leg4 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg4.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg4.setRotationPoint(5F, 13F, -1F);
		leg4.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
		
		leg6 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg6.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg6.setRotationPoint(5F, 13F, 10F);
		leg6.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
		
		leg1 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg1.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg1.setRotationPoint(-5F, 13F, -8F);
		leg1.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
		
		leg3 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg3.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg3.setRotationPoint(-5F, 13F, -1F);
		leg3.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
		
		leg5 = new ModelRenderer(this, 96, 6).setTextureSize(128, 64);
		leg5.addBox(-1F, 0F, -1F, 2, 9, 2);
		leg5.setRotationPoint(-5F, 13F, 10F);
		leg5.setTextureOffset(85, 18).addBox(-13.5F, 7.5F, -0.5F, 14, 1, 1);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        tail1.render(f5);
        tail2.render(f5);
		head.render(f5);
        leg2.render(f5);
        leg4.render(f5);
		leg6.render(f5);
        leg1.render(f5);
        leg3.render(f5);
		leg5.render(f5);
    }
	
	@Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2)
	{
        leg2.rotateAngleZ = ((float)Math.PI / 180F * -100F) + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
        leg4.rotateAngleZ = ((float)Math.PI / 180F * -100F) + MathHelper.cos(f * 0.6662F) * f1 * -0.25F;
        leg6.rotateAngleZ = ((float)Math.PI / 180F * -100F) + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
		
        leg1.rotateAngleZ = ((float)Math.PI / 180F * -80F) + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
        leg3.rotateAngleZ = ((float)Math.PI / 180F * -80F) + MathHelper.cos(f * 0.6662F) * f1 * -0.25F;
        leg5.rotateAngleZ = ((float)Math.PI / 180F * -80F) + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
		
		tail1.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1 * 0.15F;
		tail2.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1 * 0.2F;
	}
	
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = f4 / (180F / (float)Math.PI);
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
		
		leg1.rotateAngleX = (float)Math.PI;
		leg3.rotateAngleX = (float)Math.PI;
		leg5.rotateAngleX = (float)Math.PI;
    }
}
