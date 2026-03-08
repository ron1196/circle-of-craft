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

public class LKGuiTimon extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/timon.png");
	
	public LKGuiTimon(EntityPlayer entityplayer, LKEntityTimon entitytimon) 
	{  
		super(new LKContainerTimon(entityplayer, entitytimon));
	}  
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)  
	{  
		int textcolour = 0x7F472F;
		fontRenderer.drawString("Timon & Pumbaa", 51, 13, textcolour);
		fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, textcolour);
		
		for (int k = 0; k < 5; k++)
		{
			String s = Integer.valueOf(((LKSlotTimon)inventorySlots.inventorySlots.get(k)).cost).toString();
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.getTextureManager(), new ItemStack(mod_LionKing.bug), s.length() == 1 ? 18 + 33 * k : 23 + 33 * k, 51);
			fontRenderer.drawString(s, s.length() == 1 ? 11 + 33 * k : 10 + 33 * k, 55, textcolour);
		}
	}  
	 
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int l = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(l, k, 0, 0, xSize, ySize + 24);
	}
}
