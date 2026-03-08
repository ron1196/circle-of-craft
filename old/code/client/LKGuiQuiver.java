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

public class LKGuiQuiver extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/quiver.png");
	private static final ResourceLocation chestTexture = new ResourceLocation("textures/gui/container/generic_54.png");
	
	public LKGuiQuiver(EntityPlayer entityplayer, LKInventoryQuiver quiver)
	{
		super(new LKContainerQuiver(entityplayer, quiver));
		xSize = 251;
		ySize = 100;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{
		fontRenderer.drawString("Inventory", 8, 6, 4210752);
		fontRenderer.drawString("Quiver", 209, 16, 0x3B4229);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(chestTexture);
		int l = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(l, k, 0, 0, 176, 3);
		drawTexturedModalRect(l, k + 3, 0, 125, 176, 97);
		
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(l + 201, k, 0, 0, 50, ySize);
	}
}
