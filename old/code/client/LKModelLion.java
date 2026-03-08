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

public class LKModelLion extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer headwear;
    private ModelRenderer mane;
    private ModelRenderer body;
    private ModelRenderer leg1;
    private ModelRenderer leg2;
    private ModelRenderer leg3;
    private ModelRenderer leg4;
	private ModelRenderer hat;
	private boolean isOutlander;
	
	public LKModelLion()
	{
		this(false);
	}

    public LKModelLion(boolean flag)
    {
		isOutlander = flag;

        head = new ModelRenderer(this, 0, 0).setTextureSize(64, 96);
        head.addBox(-4F, -4F, -7F, 8, 8, 8, 0.0F); 
        head.setRotationPoint(0.0F, 4F, -9F);
		head.setTextureOffset(52, 34).addBox(-2F, 0F, -9F, 4, 4, 2);
        headwear = new ModelRenderer(this, 32, 0).setTextureSize(64, 96);
        headwear.addBox(-4F, -4F, -7F, 8, 8, 8, 0.5F);
        headwear.setRotationPoint(0.0F, 4F, -9F); 
        mane = new ModelRenderer(this, 0, 36).setTextureSize(64, 96);
        mane.addBox(-7F, -7F, -5F, 14, 14, 9, 0.0F);  
        mane.setRotationPoint(0.0F, 4F, -9F); 
        body = new ModelRenderer(this, 0, 68).setTextureSize(64, 96);
        body.addBox(isOutlander ? -5F : -6F, -10F, -7F, isOutlander ? 10 : 12, 18, 10, 0.0F);
		body.setRotationPoint(0.0F, 5F, 2.0F);
        leg1 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg1.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0.0F);
        leg1.setRotationPoint(-4F, 12F, 7F);
        leg2 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg2.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0.0F);
        leg2.setRotationPoint(4F, 12F, 7F);
        leg3 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg3.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0.0F);
        leg3.setRotationPoint(-4F, 12F, -5F);
        leg4 = new ModelRenderer(this, 0, 19).setTextureSize(64, 96);
        leg4.addBox(-2F, 0.0F, -2F, 4, 12, 4, 0.0F);
        leg4.setRotationPoint(4F, 12F, -5F);
		hat = new ModelRenderer(this, 17, 25).setTextureSize(64, 96);
		hat.addBox(-4F, -16F, 0F, 8, 9, 1);
		hat.setRotationPoint(0F, 4F, -9F);
		
		if (isOutlander)
		{
			leg1.rotationPointX += 1.0F;
			leg2.rotationPointX -= 1.0F;
			leg3.rotationPointX += 1.0F;
			leg4.rotationPointX -= 1.0F;
		}
    }

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 8F * f5, 4F * f5);
            head.render(f5);
			headwear.render(f5);
			if (LKIngame.isChristmas())
			{
				GL11.glTranslatef(0F, 3F, 0F);
				hat.render(f5);
			}
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
        }
		else
        {
            head.render(f5);
			headwear.render(f5);
            mane.render(f5);
			if (LKIngame.isChristmas())
			{
				hat.render(f5);
			}
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
        head.rotateAngleX = f4 / 57.29578F;
        head.rotateAngleY = f3 / 57.29578F;
        headwear.rotateAngleX = f4 / 57.29578F;
        headwear.rotateAngleY = f3 / 57.29578F;
        mane.rotateAngleX = f4 / 57.29578F;
        mane.rotateAngleY = f3 / 57.29578F;
        hat.rotateAngleX = f4 / 57.29578F;
        hat.rotateAngleY = f3 / 57.29578F;
		body.rotateAngleX = ((float)Math.PI) / 2.0F;
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
