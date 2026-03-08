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

public class LKModelVulture extends ModelBase
{
	ModelRenderer body;
	ModelRenderer tail;
	ModelRenderer legLeft;
	ModelRenderer legRight;
	ModelRenderer neck;
	ModelRenderer head;
	ModelRenderer wingLeft;
	ModelRenderer wingRight;
  
	public LKModelVulture()
	{
		body = new ModelRenderer(this, 0, 46).setTextureSize(64, 64);
		body.addBox(0F, 0F, 0F, 8, 10, 8);
		body.setRotationPoint(-4F, 8F, -4F);
   
		tail = new ModelRenderer(this, 44, 50).setTextureSize(64, 64);
		tail.addBox(0F, 0F, 0F, 6, 10, 4);
		tail.setRotationPoint(-3F, 13F, 4F);

		legLeft = new ModelRenderer(this, 40, 0).setTextureSize(64, 64);
		legLeft.addBox(0F, 0F, 0F, 2, 12, 2);
		legLeft.setRotationPoint(1F, 12F, 2F);
 
		legRight = new ModelRenderer(this, 40, 0).setTextureSize(64, 64);
		legRight.addBox(0F, 0F, 0F, 2, 12, 2);
		legRight.setRotationPoint(-3F, 12F, 2F);
		legRight.mirror = true;
  
		neck = new ModelRenderer(this, 0, 14).setTextureSize(64, 64);
		neck.addBox(0F, 0F, 0F, 4, 5, 4);
		neck.setRotationPoint(-2F, 3F, -4F);
 
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-3F, -2F, -3F, 6, 6, 6);
		head.setRotationPoint(0.5F, 0F, -2.5F);
	  
		head.setTextureOffset(28, 0).addBox(-1.5F, 1.5F, -6.9F, 3, 2, 2); 
		head.setTextureOffset(46, 29).addBox(-2F, 0.5F, -7F, 4, 2, 5); 
   
		wingLeft = new ModelRenderer(this, 0, 26).setTextureSize(64, 64);
		wingLeft.addBox(0F, 0F, 0F, 2, 10, 7);
		wingLeft.setRotationPoint(4F, 5F, -2F);

		wingRight = new ModelRenderer(this, 0, 26).setTextureSize(64, 64);
		wingRight.addBox(-2F, 0F, 0F, 2, 10, 7);
		wingRight.setRotationPoint(-4F, 5F, -2F);
		wingRight.mirror = true;
	}	
  
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		tail.render(f5);
		legLeft.render(f5);
		legRight.render(f5);
		neck.render(f5);
		head.render(f5);
		wingLeft.render(f5);
		wingRight.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		head.rotateAngleX = -(f4 / 57.29578F);
		head.rotateAngleY = f3 / 57.29578F;
		body.rotateAngleX = 0.698131F;
		tail.rotateAngleX = body.rotateAngleX;
		neck.rotateAngleX = 0.4364323F;
		legRight.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		legLeft.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		wingRight.rotateAngleZ = f2;
		wingLeft.rotateAngleZ = -f2;
	}
}
