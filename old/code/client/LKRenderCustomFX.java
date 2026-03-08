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

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class LKRenderCustomFX extends Render
{
	public static float ticks;
	
	public LKRenderCustomFX()
	{
		shadowSize = 0.0F;
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return LKClientProxy.iconsTexture;
    }
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float ticks, float yaw)
	{
		LKEntityCustomFX entityfx = (LKEntityCustomFX)entity;
		EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;
        float f = ActiveRenderInfo.rotationX;
        float f1 = ActiveRenderInfo.rotationZ;
        float f2 = ActiveRenderInfo.rotationYZ;
        float f3 = ActiveRenderInfo.rotationXY;
        float f4 = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)ticks;
        EntityFX.interpPosY = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)ticks;
        EntityFX.interpPosZ = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)ticks;
		
		bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.setBrightness(entityfx.getBrightnessForRender(ticks));
		entityfx.renderParticle(tessellator, ticks, f, f4, f1, f2, f3);

		tessellator.draw();
    }
}
