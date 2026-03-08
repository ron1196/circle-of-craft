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
import java.lang.reflect.Field;
import org.lwjgl.opengl.GL11;

public abstract class LKRenderLiving extends RenderLiving
{
	private static RenderItem itemRenderer = new RenderItem();
	private float scale;
	private String name;
	
	public LKRenderLiving(ModelBase model)
	{
		this(model, 1F, null);
	}
	
	public LKRenderLiving(ModelBase model, float f)
	{
		this(model, f, null);
	}
	
	public LKRenderLiving(ModelBase model, String s)
	{
		this(model, 1F, s);
	}
	
	public LKRenderLiving(ModelBase model, float f, String s)
	{
		super(model, 0.5F * f);
		scale = f;
		name = s;
	}
	
	@Override
    public void doRenderLiving(EntityLiving entity, double d, double d1, double d2, float f, float f1)
    {
        super.doRenderLiving(entity, d, d1, d2, f, f1);
		
		if (name != null)
		{
			renderLivingLabel(entity, name, d, d1, d2, 64);
		}
		
		if (entity instanceof LKEntityQuestAnimal)
		{
			LKEntityQuestAnimal animal = (LKEntityQuestAnimal)entity;
			EntityLivingBase player = renderManager.livingPlayer;
			if (animal instanceof LKEntityLionBase && ((LKEntityLionBase)animal).isHostile())
			{
				return;
			}
			if (animal.quest.hasQuest() && animal.getHealth() > 0 && player instanceof EntityPlayer && LKIngame.hasAmulet((EntityPlayer)player))
			{
				ItemStack itemstack = animal.quest.getRequiredItem();
				if (itemstack.getItem() != null)
				{
					renderQuestItem(animal, d, d1 + animal.getQuestItemRenderOffset(), d2, 64, itemstack);
				}
			}
		}
		
		if (entity instanceof LKEntityScar && ((LKEntityScar)entity).isScarHostile())
		{
			LKTickHandlerClient.scarBoss = (LKEntityScar)entity;
		}
    }
	
	@Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        GL11.glScalef(scale, scale, scale);
    }
	
    private void renderQuestItem(EntityLivingBase entityliving, double d, double d1, double d2, int i, ItemStack itemstack)
    {
        if (entityliving.getDistanceSqToEntity(renderManager.livingPlayer) <= (double)(i * i))
        {
			String s = "x" + itemstack.stackSize;
            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            fontrenderer.drawString(s, -1, 1, 0);
			fontrenderer.drawString(s, -1, 0, 0);
            fontrenderer.drawString(s, -1, -1, 0);
			fontrenderer.drawString(s, 0, 1, 0);
			fontrenderer.drawString(s, 0, -1, 0);
            fontrenderer.drawString(s, 1, 1, 0);
			fontrenderer.drawString(s, 1, 0, 0);
            fontrenderer.drawString(s, 1, -1, 0);
			fontrenderer.drawString(s, 0, 0, -1);
			GL11.glPushMatrix();
			if (itemstack.itemID < Block.blocksList.length && Block.blocksList[itemstack.itemID] != null)
			{
				GL11.glEnable(GL11.GL_LIGHTING);
				itemRenderer.renderItemAndEffectIntoGUI(fontrenderer, renderManager.renderEngine, new ItemStack(itemstack.itemID, 1, itemstack.getItemDamage()), -21, -4);
			}
			else
			{
				itemRenderer.renderItemAndEffectIntoGUI(fontrenderer, renderManager.renderEngine, new ItemStack(itemstack.itemID, 1, itemstack.getItemDamage()), -21, -4);
			}
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
