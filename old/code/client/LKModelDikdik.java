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

public class LKModelDikdik extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer neck;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;
	
	public LKModelDikdik()
	{
		head = new ModelRenderer(this, 42, 23);
		head.addBox(-2F, -9F, -3F, 4, 4, 5);
		head.setRotationPoint(0F, 11F, -4.5F);
		head.setTextureOffset(18, 28).addBox(-1F, -7.3F, -5F, 2, 2, 2);
		head.setTextureOffset(0, 27).addBox(-2.8F, -11F, 0.5F, 1, 3, 2);
		head.setTextureOffset(8, 27).addBox(1.8F, -11F, 0.5F, 1, 3, 2);
		head.setTextureOffset(0, 21).addBox(-1.5F, -11F, 0F, 1, 2, 1);
		head.setTextureOffset(0, 21).addBox(0.5F, -11F, 0F, 1, 2, 1);
		head.setTextureOffset(28, 22).addBox(-1.5F, -8F, -2F, 3, 7, 3);
		
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-3F, 0F, 0F, 6, 6, 14);
		body.setRotationPoint(0F, 9F, -7F);
		
		leg1 = new ModelRenderer(this, 56, 0);
		leg1.addBox(-1F, 0F, -1F, 2, 10, 2);
		leg1.setRotationPoint(-1.7F, 14F, 5F);
		
		leg2 = new ModelRenderer(this, 56, 0);
		leg2.addBox(-1F, 0F, -1F, 2, 10, 2);
		leg2.setRotationPoint(1.7F, 14F, 5F);
		
		leg3 = new ModelRenderer(this, 56, 0);
		leg3.addBox(-1F, 0F, -1F, 2, 10, 2);
		leg3.setRotationPoint(-1.7F, 14F, -5F);
		
		leg4 = new ModelRenderer(this, 56, 0);
		leg4.addBox(-1F, 0F, -1F, 2, 10, 2);
		leg4.setRotationPoint(1.7F, 14F, -5F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		head.render(f5);
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
		leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
		leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
