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

public class LKModelRhino extends ModelBase
{
	  ModelRenderer horn;
	  ModelRenderer backhorn;
	  ModelRenderer leftear;
	  ModelRenderer rightear;
	  ModelRenderer head;
	  ModelRenderer body;
	  ModelRenderer leg1;
	  ModelRenderer leg2;
	  ModelRenderer leg3;
	  ModelRenderer leg4;
    protected float field_40331_g;
    protected float field_40332_n;
  
  public LKModelRhino()
  {
      field_40331_g = 8F;
      field_40332_n = 4F;
	  
      horn = new ModelRenderer(this, 56, 0).setTextureSize(64, 64);
      horn.addBox(-1F, -9F, -4.5F, 2, 6, 2);
      horn.setRotationPoint(0F, 9F, -11F);

      backhorn = new ModelRenderer(this, 60, 8).setTextureSize(64, 64);
      backhorn.addBox(-0.5F, -6.5F, 0F, 1, 3, 1);
      backhorn.setRotationPoint(0F, 9F, -11F);
 
      leftear = new ModelRenderer(this, 44, 0).setTextureSize(64, 64);
      leftear.addBox(-4.6F, -6F, 3F, 1, 3, 2);
      leftear.setRotationPoint(0F, 9F, -11F);
 
      rightear = new ModelRenderer(this, 50, 0).setTextureSize(64, 64);
      rightear.addBox(3.6F, -6F, 3F, 1, 3, 2);
      rightear.setRotationPoint(0F, 9F, -11F);
  
      head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
      head.addBox(-4F, -4F, -6F, 8, 8, 12);
      head.setRotationPoint(0F, 9F, -11F);

      body = new ModelRenderer(this, 0, 32).setTextureSize(64, 64);
      body.addBox(-5.466667F, -10F, -8F, 13, 22, 10);
      body.setRotationPoint(-1F, 9F, 1F);

      leg1 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
      leg1.addBox(-3F, 0F, -2F, 4, 8, 4);
      leg1.setRotationPoint(-3F, 16F, 10F);
   
      leg2 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
      leg2.addBox(-1F, 0F, -2F, 4, 8, 4);
      leg2.setRotationPoint(3F, 16F, 10F);
  
      leg3 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
      leg3.addBox(-3F, 0F, -3F, 4, 8, 4);
      leg3.setRotationPoint(-3F, 16F, -5F);

      leg4 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
      leg4.addBox(-1F, 0F, -3F, 4, 8, 4);
      leg4.setRotationPoint(3F, 16F, -5F);  
  }
  
  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	if (isChild)
    {
        float f6 = 2.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, field_40331_g * f5, field_40332_n * f5);
        head.render(f5);
        leftear.render(f5);
        rightear.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
        GL11.glTranslatef(0.0F, 24F * f5, 0.0F);
        body.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
        GL11.glPopMatrix();
    } else
    {
        head.render(f5);
        horn.render(f5);
        backhorn.render(f5);
        leftear.render(f5);
        rightear.render(f5);
        body.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
    }
  }
  
  @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = (f4 / 57.29578F) + 0.279256F;
        head.rotateAngleY = f3 / 57.29578F;
        horn.rotateAngleX = head.rotateAngleX;
        horn.rotateAngleY = head.rotateAngleY;
        backhorn.rotateAngleX = head.rotateAngleX;
        backhorn.rotateAngleY = head.rotateAngleY;
        leftear.rotateAngleX = head.rotateAngleX;
        leftear.rotateAngleY = head.rotateAngleY;
        rightear.rotateAngleX = head.rotateAngleX;
        rightear.rotateAngleY = head.rotateAngleY;
        body.rotateAngleX = 1.570796F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }

}
