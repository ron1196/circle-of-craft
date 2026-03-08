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
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class LKRenderGrindingStick extends TileEntitySpecialRenderer
{
	private ModelBase model = new LKModelGrindingStick();
	private static final ResourceLocation texture = new ResourceLocation("lionking:item/stick.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LKTileEntityGrindingBowl bowl = (LKTileEntityGrindingBowl)tileentity;
		renderStick((float)d, (float)d1, (float)d2, bowl.stickRotation + (f * 8F));
	}
	
	private void renderStick(float f, float f1, float f2, float rotation)
	{
		bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glDisable(2884);
		GL11.glTranslatef(f + 0.5F, f1 + 1.4F, f2 + 0.5F);
		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glEnable(3008);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}
