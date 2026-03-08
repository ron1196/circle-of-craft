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

public class LKModelSimbaCharm extends ModelBase
{
	private ModelRenderer body;
	
	public LKModelSimbaCharm()
	{
        body = new ModelRenderer(this, 0, 0).setTextureSize(64, 32);
        body.addBox(-6F, -20F, -39F, 12, 1, 15, 0.0F);
		body.setRotationPoint(0.0F, 5F, 2.0F);
	}
	
	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (isChild)
        {
            GL11.glPushMatrix();
            GL11.glScalef(0.3F, 0.3F, 0.3F);
			if (((LKEntitySimba)entity).isSitting())
			{
				GL11.glTranslatef(0.0F, 1.3F, 0.0F);
			}
			else
			{
				GL11.glTranslatef(0.0F, 1.6F, 0.2F);
			}
            body.render(f5);
            GL11.glPopMatrix();
        }
		else
        {
			GL11.glPushMatrix();
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			if (((LKEntitySimba)entity).isSitting())
			{
				GL11.glTranslatef(0.0F, -0.3F, -0.5F);
			}
			else
			{
				GL11.glTranslatef(0.0F, -0.2F, -0.2F);
			}
            body.render(f5);
			GL11.glPopMatrix();
		}
    }

	@Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		body.rotateAngleX = ((float)Math.PI) / 2.0F;
		body.rotationPointY = 5.0F;
    }
}
