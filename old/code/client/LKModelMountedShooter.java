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

public class LKModelMountedShooter extends ModelBase
{
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	
	public LKModelMountedShooter()
	{
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-1F, -2F, -8F, 4, 4, 16);
		body.setRotationPoint(-1F, 16F, 0F);
		
		leg1 = new ModelRenderer(this, 0, 0);
		leg1.addBox(-0.5F, 0F, -0.5F, 1, 8, 1);
		leg1.setRotationPoint(-1.5F, 17F, -1F);
		leg1.rotateAngleZ = 25F / 180F * (float)Math.PI;
		
		leg2 = new ModelRenderer(this, 0, 0);
		leg2.addBox(-0.5F, 0F, -0.5F, 1, 8, 1);
		leg2.setRotationPoint(1.5F, 17F, -1F);
		leg2.rotateAngleZ = -25F / 180F * (float)Math.PI;
		leg2.mirror = true;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		body.rotateAngleX = f4 / 180F * (float)Math.PI;;
		leg1.render(f5);
		leg2.render(f5);
		body.render(f5);
	}
}
