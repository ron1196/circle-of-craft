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
import net.minecraft.client.Minecraft;

public class LKGuiQuestsButton extends GuiButton
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/book_menu.png");
	
	private LKQuestBase theQuest;
	
	public LKGuiQuestsButton(int i, int j, int k, LKQuestBase quest)
	{
		super(i, j, k, 135, 20, quest == null ? "Main Page" : quest.getName());
		theQuest = quest;
	}
	
	@Override
    public void drawButton(Minecraft mc, int i, int j)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int yPos = theQuest != null && !theQuest.canStart() ? 196 : (theQuest != null && LKGuiQuests.flashTimer > 14 && theQuest.canStart() && theQuest.checked == 0 ? 236 : 216);
		drawTexturedModalRect(xPosition, yPosition, 121, yPos, width / 2, height);
		drawTexturedModalRect(xPosition + width / 2, yPosition, 256 - width / 2, yPos, width / 2, height);
		mouseDragged(mc, i, j);
		drawCenteredString(mc.fontRenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0x120C01);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	@Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k)
    {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }
}
