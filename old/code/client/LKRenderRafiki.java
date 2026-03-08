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

public class LKRenderRafiki extends RenderLiving
{
    private LKModelRafiki modelRafiki = new LKModelRafiki();
	private static final ResourceLocation texture = new ResourceLocation("lionking:mob/rafiki.png");

    public LKRenderRafiki(LKModelRafiki model)
    {
        super(model, 0.5F);
		modelRafiki = model;
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }

    public void doRenderLiving(EntityLiving entity, double d, double d1, double d2, float f, float f1)
    {
        super.doRenderLiving(entity, d, d1, d2, f, f1);
        renderLivingLabel(entity, "Rafiki", d, d1, d2, 64); 
    }

	@Override
    protected void renderEquippedItems(EntityLivingBase entityliving, float f)
    {
        super.renderEquippedItems(entityliving, f);
        ItemStack itemstack = entityliving.getHeldItem();
        if(itemstack != null)
        {
            GL11.glPushMatrix();
            modelRafiki.getRightArm().postRender(0.0625F);
            GL11.glTranslatef(0.0625F, 0.4375F, 0.2625F);
			
            if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f3 = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f3, -f3, f3);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
			else
            {
                float f4 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f4, f4, f4);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }
			
            renderManager.itemRenderer.renderItem(entityliving, itemstack, 0);
            GL11.glPopMatrix();
        }
    }
}