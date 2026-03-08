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

public class LKModelPumbaa extends ModelBase
{
	private ModelRenderer snout;
	private ModelRenderer leftear;
	private ModelRenderer rightear;
	private ModelRenderer tail;
	private ModelRenderer mane;
	private ModelRenderer hair;
	private ModelRenderer lefthorn;
	private ModelRenderer righthorn;
	private ModelRenderer head;
	private ModelRenderer hat;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
  
	public LKModelPumbaa()
	{
		snout = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		snout.addBox(-3F, -2.6F, -11F, 6, 4, 3);
		snout.setRotationPoint(0F, 9F, -5F);

		leftear = new ModelRenderer(this, 0, 31).setTextureSize(64, 64);
		leftear.addBox(-4.5F, -9F, -3F, 1, 4, 2);
		leftear.setRotationPoint(0F, 9F, -5F);

		rightear = new ModelRenderer(this, 6, 31).setTextureSize(64, 64);
		rightear.addBox(3.5F, -9F, -3F, 1, 4, 2);
		rightear.setRotationPoint(0F, 9F, -5F);

		tail = new ModelRenderer(this, 60, 55).setTextureSize(64, 64);
		tail.addBox(-0.5F, -1.5F, 1F, 1, 8, 1);
		tail.setRotationPoint(0F, 11F, 8F);

		mane = new ModelRenderer(this, 36, 18).setTextureSize(64, 64);
		mane.addBox(-3.5F, -4F, -2F, 5, 4, 9);
		mane.setRotationPoint(1F, 9F, -7F);

		hair = new ModelRenderer(this, 0, 10).setTextureSize(64, 64);
		hair.addBox(-2F, -7.3F, -4.7F, 4, 5, 5);
		hair.setRotationPoint(0F, 9F, -5F);

		lefthorn = new ModelRenderer(this, 54, 31).setTextureSize(64, 64);
		lefthorn.addBox(-9F, -2F, -7F, 4, 1, 1);
		lefthorn.setRotationPoint(0F, 9F, -5F);

		righthorn = new ModelRenderer(this, 54, 31).setTextureSize(64, 64);
		righthorn.addBox(5F, -2F, -7F, 4, 1, 1);
		righthorn.setRotationPoint(0F, 9F, -5F);

		head = new ModelRenderer(this, 28, 0).setTextureSize(64, 64);
		head.addBox(-5F, -6F, -8F, 10, 10, 8);
		head.setRotationPoint(0F, 9F, -5F);
		
		hat = new ModelRenderer(this, 34, 36).setTextureSize(64, 64);
		hat.addBox(-4F, -15F, -4.5F, 8, 9, 1);
		hat.setRotationPoint(0F, 9F, -5F);

		body = new ModelRenderer(this, 0, 37).setTextureSize(64, 64);
		body.addBox(-5F, -10F, -7F, 12, 17, 10);
		body.setRotationPoint(-1F, 11F, 2F);

		leg1 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg1.addBox(-2F, 0F, -2F, 3, 8, 3);
		leg1.setRotationPoint(-3F, 16F, 6F);

		leg2 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg2.addBox(-1.5F, 0F, -2F, 3, 8, 3);
		leg2.setRotationPoint(3F, 16F, 6F);

		leg3 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg3.addBox(-2F, 0F, -2F, 3, 8, 3);
		leg3.setRotationPoint(-3F, 16F, -4F); 

		leg4 = new ModelRenderer(this, 0, 20).setTextureSize(64, 64);
		leg4.addBox(-1F, 0F, -2F, 3, 8, 3);
		leg4.setRotationPoint(3F, 16F, -4F); 
	}
  
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		snout.render(f5);
		leftear.render(f5);
		rightear.render(f5);
		tail.render(f5);
		mane.render(f5);
		hair.render(f5);
		lefthorn.render(f5);
		righthorn.render(f5);
		head.render(f5);
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
  
	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        head.rotateAngleX = f4 / 57.29578F;
        head.rotateAngleY = f3 / 57.29578F;
        hat.rotateAngleX = f4 / 57.29578F;
        hat.rotateAngleY = f3 / 57.29578F;
        snout.rotateAngleX = f4 / 57.29578F;
        snout.rotateAngleY = f3 / 57.29578F;
        hair.rotateAngleX = f4 / 57.29578F;
        hair.rotateAngleY = f3 / 57.29578F;
        body.rotateAngleX = 1.570796F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		tail.rotateAngleX = 0.698132F;
		righthorn.rotateAngleX = head.rotateAngleX;
		righthorn.rotateAngleY = 0.436332F + head.rotateAngleY;
		lefthorn.rotateAngleX = head.rotateAngleX;
		lefthorn.rotateAngleY = -0.436332F + head.rotateAngleY;
		mane.rotateAngleX = -0.417716F;
		hair.rotateAngleX = -0.104719F + head.rotateAngleX;
		hair.rotateAngleY = head.rotateAngleY;
		rightear.rotateAngleY = head.rotateAngleY;
		rightear.rotateAngleX = head.rotateAngleX;
		leftear.rotateAngleY = head.rotateAngleY;
		leftear.rotateAngleX = head.rotateAngleX;
    }
}
