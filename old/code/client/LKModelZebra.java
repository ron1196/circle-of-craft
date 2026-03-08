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

public class LKModelZebra extends ModelBase
{     
	public ModelRenderer neck;   
	public ModelRenderer body;    
	public ModelRenderer leg1;    
	public ModelRenderer leg2;    
	public ModelRenderer leg3;    
	public ModelRenderer leg4;    
	public ModelRenderer head;    
	public ModelRenderer mane;   
	public ModelRenderer tail;  

	public LKModelZebra()    
	{
		neck = new ModelRenderer(this, 98, 26).setTextureSize(128, 64);
		neck.addBox(-5F, -4F, -6F, 6, 8, 9);
		neck.setRotationPoint(2F, 3F, -6F); 
	
		body = new ModelRenderer(this, 0, 33).setTextureSize(128, 64);
		body.addBox(-6F, -10F, -7F, 12, 10, 21);
		body.setRotationPoint(0F, 12F, -1F);
               
		leg1 = new ModelRenderer(this, 0, 16).setTextureSize(128, 64);       
		leg1.addBox(-3F, 0F, -2F, 4, 12, 4);
		leg1.setRotationPoint(-3F, 12F, 10F);
                      
		leg2 = new ModelRenderer(this, 0, 16).setTextureSize(128, 64);      
		leg2.addBox(-1F, 0F, -2F, 4, 12, 4);        
		leg2.setRotationPoint(3F, 12F, 10F);

        leg3 = new ModelRenderer(this, 0, 16).setTextureSize(128, 64);
        leg3.addBox(-3F, 0F, -3F, 4, 12, 4);
        leg3.setRotationPoint(-3F, 12F, -5F);
	
        leg4 = new ModelRenderer(this, 0, 16).setTextureSize(128, 64);
        leg4.addBox(-1F, 0F, -3F, 4, 12, 4);
        leg4.setRotationPoint(3F, 12F, -5F);

        head = new ModelRenderer(this, 84, 0).setTextureSize(128, 64);
        head.addBox(0F, 0F, 0F, 8, 8, 14);
	    head.setRotationPoint(-4F, -3F, -19F);

        head.setTextureOffset(72, 0).addBox(1F, -3F, 10F, 1, 3, 2);
        head.setTextureOffset(78, 0).addBox(6F, -3F, 10F, 1, 3, 2);

        mane = new ModelRenderer(this, 92, 47).setTextureSize(128, 64);
        mane.addBox(0F, 0F, 0F, 4, 3, 14);
	    mane.setRotationPoint(-2F, 5F, -1F);
		
	    tail = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
        tail.addBox(-1F, -4F, 13.5F, 2, 12, 2);
		tail.setRotationPoint(0F, 12F, -1F);
    }

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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
            mane.render(f5);
            neck.render(f5);
            body.render(f5);
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
            mane.render(f5);
            neck.render(f5);
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
		neck.rotateAngleX = -1.064651F;
		head.rotateAngleX = 0.4014257F;
		mane.rotateAngleX = 2.111848F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		tail.rotateAngleX = 0.296706F;
    }
}