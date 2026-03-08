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

public class LKModelTimon extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer hat;
	private ModelRenderer body;
	private ModelRenderer rightarm;
	private ModelRenderer leftarm;
	private ModelRenderer rightleg;
	private ModelRenderer leftleg;
	private ModelRenderer tail;
	private ModelRenderer leftear;
	private ModelRenderer rightear;
  
	public LKModelTimon()
	{
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4F, -8F, -4F, 8, 7, 6);
		head.setRotationPoint(0F, 1F, 1F);
		head.setTextureOffset(46, 7).addBox(-3.5F, -9F, -2F, 7, 1, 2);
		head.setTextureOffset(0, 0).addBox(-1F, -5F, -5F, 2, 2, 1);

		hat = new ModelRenderer(this, 0, 31).setTextureSize(64, 64);
		hat.addBox(-4F, -17F, -1F, 8, 9, 1);
		hat.setRotationPoint(0F, 1F, 1F);

		body = new ModelRenderer(this, 40, 12).setTextureSize(64, 64);
		body.addBox(-4F, 0F, -2F, 8, 15, 4);
		body.setRotationPoint(0F, 0F, 0F);

		rightarm = new ModelRenderer(this, 20, 13).setTextureSize(64, 64);
		rightarm.addBox(-3F, -2F, -2F, 3, 12, 3);
		rightarm.setRotationPoint(-4F, 3F, 0.5F);

		leftarm = new ModelRenderer(this, 20, 13).setTextureSize(64, 64);
		leftarm.addBox(-1F, -2F, -2F, 3, 12, 3);
		leftarm.setRotationPoint(5F, 3F, 0.5F);

		rightleg = new ModelRenderer(this, 0, 13).setTextureSize(64, 64);
		rightleg.addBox(-2F, 0F, -2F, 3, 9, 3);
		rightleg.setRotationPoint(-1.7F, 15F, 0.5F);
		rightleg.setTextureOffset(44, 0).addBox(-2.7F, 8F, -3.9F, 4, 1, 6);

		leftleg = new ModelRenderer(this, 0, 13).setTextureSize(64, 64);
		leftleg.addBox(-2F, 0F, -2F, 3, 9, 3);
		leftleg.setRotationPoint(2.7F, 15F, 0.5F);
		leftleg.setTextureOffset(44, 0).addBox(-2.3F, 8F, -3.9F, 4, 1, 6);

		tail = new ModelRenderer(this, 0, 13).setTextureSize(64, 64);
		tail.addBox(-1F, 0F, 0F, 2, 2, 16);
		tail.setRotationPoint(0F, 9F, 1F);

		leftear = new ModelRenderer(this, 36, 0).setTextureSize(64, 64);
		leftear.addBox(-7F, -5F, -1.5F, 3, 5, 1);
		leftear.setRotationPoint(0F, 1F, 1F);

		rightear = new ModelRenderer(this, 36, 0).setTextureSize(64, 64);
		rightear.addBox(4F, -5F, -1.5F, 3, 5, 1);
		rightear.setRotationPoint(0F, 1F, 1F);
    }
  
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
		if (LKIngame.isChristmas())
		{
			hat.render(f5);
		}
		body.render(f5);
		rightarm.render(f5);
		leftarm.render(f5);
		rightleg.render(f5);
		leftleg.render(f5);
		tail.render(f5);
		leftear.render(f5);
		rightear.render(f5);
	}
  
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
        head.rotateAngleY = f3 / 57.29578F;
        head.rotateAngleX = f4 / 57.29578F;
        hat.rotateAngleY = f3 / 57.29578F;
        hat.rotateAngleX = f4 / 57.29578F;
        rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		tail.rotateAngleX = -0.908F + (f4 / 57.29578F);
		leftear.rotateAngleZ = 0.38397F;
		leftear.rotateAngleY = head.rotateAngleY;
		leftear.rotateAngleX = head.rotateAngleX;
		rightear.rotateAngleZ = -0.38397F;
		rightear.rotateAngleY = head.rotateAngleY;
		rightear.rotateAngleX = head.rotateAngleX;
	}
}
