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

public class LKGuiSimbaButton extends GuiButton
{
	private LKEntitySimba theSimba;
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/simba.png");
	
	public LKGuiSimbaButton(int i, int j, int k, LKEntitySimba simba)
	{
		super(i, j, k, 22, 22, "Simba");
		theSimba = simba;
	}
	
	@Override
    public void drawButton(Minecraft mc, int i, int j)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int u = getHoverState(i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height) == 2 ? 202 : 178;
		int v = theSimba.isSitting() ? 24 : 0;
		drawTexturedModalRect(xPosition, yPosition, u, v, width, height);
		mouseDragged(mc, i, j);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
