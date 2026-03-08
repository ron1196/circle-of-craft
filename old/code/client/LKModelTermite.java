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

public class LKModelTermite extends ModelBase
{
	ModelRenderer body;
	ModelRenderer head;
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg5;
	ModelRenderer leg4;
	ModelRenderer leg6;
	ModelRenderer rightfeeler;
	ModelRenderer leftfeeler;
	
	public LKModelTermite()
	{
		body = new ModelRenderer(this, 10, 5);
		body.addBox(0F, 0F, 0F, 6, 6, 21);
		body.setRotationPoint(-3F, 17F, -5F);
		setRotation(body, 0F, 0F, 0F); 
		head = new ModelRenderer(this, 0, 0);
		head.addBox(0F, 0F, 0F, 8, 8, 7);
		head.setRotationPoint(-4F, 14F, -10F);
		setRotation(head, 0F, 0F, 0F); 
		leg1 = new ModelRenderer(this, 34, 0);
		leg1.addBox(0F, 0F, -2F, 13, 2, 2);
		leg1.setRotationPoint(0F, 19F, -1F);
		setRotation(leg1, 0F, 0.311111F, 0.311111F);   
		leg2 = new ModelRenderer(this, 34, 0);
		leg2.addBox(0F, 0F, 0F, 13, 2, 2);
		leg2.setRotationPoint(0F, 19F, 2F);
		setRotation(leg2, 0F, 0F, 0.311111F); 
		leg3 = new ModelRenderer(this, 34, 0);
		leg3.addBox(0F, 0F, 0F, 13, 2, 2);
		leg3.setRotationPoint(0F, 19F, 4F);
		setRotation(leg3, 0F, -0.6222222F, 0.311111F);   
		leg4 = new ModelRenderer(this, 34, 0);
		leg4.addBox(0F, 0F, 0F, 13, 2, 2);
		leg4.setRotationPoint(0F, 19F, -1F);
		setRotation(leg4, 0F, 2.85F, -0.311111F);
		leg5 = new ModelRenderer(this, 34, 0);
		leg5.addBox(0F, 0F, -2F, 13, 2, 2);
		leg5.setRotationPoint(0F, 19F, 2F);
		setRotation(leg5, 0F, 3.11F, -0.311111F);  
		leg6 = new ModelRenderer(this, 34, 0);
		leg6.addBox(0F, 0F, -2F, 13, 2, 2);
		leg6.setRotationPoint(0F, 19F, 4F); 
		setRotation(leg6, 0F, 3.75F, -0.311111F); 
		rightfeeler = new ModelRenderer(this, 50, 18);
		rightfeeler.addBox(0F, 0F, -8F, 1, 1, 6);
		rightfeeler.setRotationPoint(-3F, 15F, -8F); 
		setRotation(rightfeeler, 0F, 0F, -0.1F);    
		leftfeeler = new ModelRenderer(this, 50, 18);
		leftfeeler.addBox(0F, 0F, -8F, 1, 1, 6);
		leftfeeler.setRotationPoint(2F, 15F, -8F);  
		setRotation(leftfeeler, 0F, 0F, 0.1F);   
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		head.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		leg5.render(f5);
		leg6.render(f5);
		rightfeeler.render(f5);
		leftfeeler.render(f5);
	}
	
	@Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2)
	{
        leg1.rotateAngleZ = 0.311111F + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
        leg2.rotateAngleZ = 0.311111F + MathHelper.cos(f * 0.6662F) * f1 * -0.25F;
        leg3.rotateAngleZ = 0.311111F + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
		
        leg4.rotateAngleZ = -0.311111F + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
        leg5.rotateAngleZ = -0.311111F + MathHelper.cos(f * 0.6662F) * f1 * -0.25F;
        leg6.rotateAngleZ = -0.311111F + MathHelper.cos(f * 0.6662F) * f1 * 0.25F;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
