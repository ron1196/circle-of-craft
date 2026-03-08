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

public class LKModelHyena extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	private ModelRenderer tail;
	
	public LKModelHyena()
	{
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -3F, -2F, 6, 6, 6);
		head.setTextureOffset(0, 15).addBox(-3F, -5F, 1F, 1, 2, 2);
		head.setTextureOffset(6, 15).addBox(2F, -5F, 1F, 1, 2, 2);
		head.setRotationPoint(-1F, 13.5F, -9F);  
      
		body = new ModelRenderer(this, 28, 11);
		body.addBox(-4F, -8F, -3F, 6, 15, 6);
		body.setTextureOffset(16, 20).addBox(-2F, -8F, 3F, 2, 11, 1);    
		body.setRotationPoint(0.0F, 14F, 2.0F);
      
		leg1 = new ModelRenderer(this, 0, 22);
		leg1.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg1.setRotationPoint(-2.5F, 16F, 7F);    
      
		leg2 = new ModelRenderer(this, 0, 22);
		leg2.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg2.setRotationPoint(0.5F, 16F, 7F);     
      
		leg3 = new ModelRenderer(this, 0, 22);
		leg3.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg3.setRotationPoint(-2.5F, 16F, -4F);  

		leg4 = new ModelRenderer(this, 0, 22);
		leg4.addBox(-1F, 0F, -1F, 2, 8, 2);
		leg4.setRotationPoint(0.5F, 16F, -4F);

		tail = new ModelRenderer(this, 16, 20);
		tail.addBox(-1F, 1.5F, -1F, 2, 9, 1);
		tail.setRotationPoint(-1F, 12F, 8F);
	}
 
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity instanceof LKEntitySkeletalHyena)
		{
			if (!((LKEntitySkeletalHyena)entity).isHeadless())
			{
				head.render(f5);
			}
		}
		else
		{
			head.render(f5);
		}
        body.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
		tail.render(f5);
    }

	@Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2)
    {
        tail.rotateAngleY = 0.0F;
        body.rotateAngleX = 1.570796F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        tail.rotateAngleX = 1.2F;
    }

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = f4 / 57.29578F;
        head.rotateAngleY = f3 / 57.29578F;
    }
}
