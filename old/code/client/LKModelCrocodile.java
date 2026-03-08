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

public class LKModelCrocodile extends ModelBase
{
    ModelRenderer body;
    ModelRenderer tail1;
    ModelRenderer tail2;
    ModelRenderer tail3;
    ModelRenderer jaw;
    ModelRenderer head;
    ModelRenderer legFrontLeft;
    ModelRenderer legBackLeft;
    ModelRenderer legFrontRight;
    ModelRenderer legBackRight;
    ModelRenderer spines;
      
    public LKModelCrocodile()
	{
        body = new ModelRenderer(this, 18, 83).setTextureSize(128, 128);
        body.addBox(-8F, -5F, 0F, 16, 9, 36);
        body.setRotationPoint(0F, 17F, -16F);

        tail1 = new ModelRenderer(this, 0, 28).setTextureSize(128, 128);
        tail1.addBox(-7F, 0F, 0F, 14, 7, 19);
        tail1.setRotationPoint(0F, 13F, 18F);

        tail2 = new ModelRenderer(this, 0, 55).setTextureSize(128, 128);
        tail2.addBox(-6F, 1.5F, 17F, 12, 5, 16);
        tail2.setRotationPoint(0F, 13F, 18F);
		
        tail3 = new ModelRenderer(this, 0, 77).setTextureSize(128, 128);
        tail3.addBox(-5F, 3F, 31F, 10, 3, 14);
        tail3.setRotationPoint(0F, 13F, 18F);

        jaw = new ModelRenderer(this, 58, 18).setTextureSize(128, 128);
        jaw.addBox(-6.5F, 0.3F, -19F, 13, 4, 19);
        jaw.setRotationPoint(0F, 17F, -16F);
		
        head = new ModelRenderer(this, 0, 0).setTextureSize(128, 128);
        head.addBox(-7.5F, -6F, -21F, 15, 6, 21);
        head.setRotationPoint(0F, 18.5F, -16F);

        legFrontLeft = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legFrontLeft.addBox(0F, 0F, -3F, 16, 3, 6);
        legFrontLeft.setRotationPoint(6F, 15F, -11F);

        legBackLeft = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legBackLeft.addBox(0F, 0F, -3F, 16, 3, 6);
        legBackLeft.setRotationPoint(6F, 15F, 15F);

        legFrontRight = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legFrontRight.addBox(-16F, 0F, -3F, 16, 3, 6);
        legFrontRight.setRotationPoint(-6F, 15F, -11F);
        legFrontRight.mirror = true;
		
        legBackRight = new ModelRenderer(this, 2, 104).setTextureSize(128, 128);
        legBackRight.addBox(-16F, 0F, -3F, 16, 3, 6);
        legBackRight.setRotationPoint(-6F, 15F, 15F);
        legBackRight.mirror = true;
		
        spines = new ModelRenderer(this, 46, 45).setTextureSize(128, 128);
        spines.addBox(-5F, 0F, 0F, 10, 4, 32);
        spines.setRotationPoint(0F, 9.5F, -14F);
    }

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        tail1.render(f5);
        tail2.render(f5);
		tail3.render(f5);
        jaw.render(f5);
        head.render(f5);
        legFrontLeft.render(f5);
        legBackLeft.render(f5);
        legFrontRight.render(f5);
        legBackRight.render(f5);
        spines.render(f5);
    }
	
	@Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2)
	{
        legBackRight.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legBackLeft.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legFrontRight.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
        legFrontLeft.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1;
		
		tail1.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.5F;
		tail2.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.5625F;
		tail3.rotateAngleY = MathHelper.cos(f * 0.6662F) * f1 * 0.59375F;
		
		head.rotateAngleX = (((LKEntityCrocodile)entityliving).getSnapTime() / 20F) * (float)Math.PI * -0.3F;
	}

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		spines.rotateAngleX = ((float)Math.PI / 180F) * -2F;
		legBackLeft.rotateAngleZ = ((float)Math.PI / 180F) * 25F;
		legBackRight.rotateAngleZ = ((float)Math.PI / 180F) * -25F;
		legFrontLeft.rotateAngleZ = ((float)Math.PI / 180F) * 25F;
		legFrontRight.rotateAngleZ = ((float)Math.PI / 180F) * -25F;
    }
}
