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


import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;

public class LKGuiDrum extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/drum.png");
	
    public LKGuiDrum(EntityPlayer entityplayer, World world, LKTileEntityDrum drum)
    {
        super(new LKContainerDrum(entityplayer, world, drum));
    }
	
	@Override
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;

        for (int j1 = 0; j1 < 3; j1++)
        {
			if (i >= l + 72 && i < l + 145 && j >= i1 + 25 + j1 * 18 && j < i1 + 42 + j1 * 18)
			{
				mc.playerController.sendEnchantPacket(inventorySlots.windowId, j1);
			}
        }
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
		fontRenderer.drawString("Bongo Drum", 60, 7, 0x1F190B);
		
		ItemStack itemstack = inventorySlots.getSlot(0).getStack();
		if (itemstack != null && itemstack.isItemEnchantable())
		{
			for (int k = 0; k < 3; k++)
			{
				int level = ((LKContainerDrum)inventorySlots).enchantLevels[k];
				if (level == 0)
				{
					return;
				}
				String s = String.valueOf(level);
				fontRenderer.drawString(s, 142 - fontRenderer.getStringWidth(s), 30 + k * 18, 0x1F190B);
			}
		}
    }

	@Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        int l = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(l, k, 0, 0, xSize, ySize);
		
		if (inventorySlots.getSlot(0).getStack() != null)
		{
			for (int i1 = 0; i1 < 3; i1++)
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(texture);
				
				int level = ((LKContainerDrum)inventorySlots).enchantLevels[i1];
				
				if (level == 0)
				{
					drawTexturedModalRect(l + 72, k + 25 + i1 * 18, 183, 0, 73, 17);
				}
				else
				{
					if (mc.thePlayer.experienceLevel < level && !mc.thePlayer.capabilities.isCreativeMode)
					{
						drawTexturedModalRect(l + 72, k + 25 + i1 * 18, 183, 0, 73, 17);
					}
					else
					{
						if (i >= l + 72 && i < l + 145 && j >= k + 25 + i1 * 18 && j < k + 42 + i1 * 18)
						{
							drawTexturedModalRect(l + 72, k + 25 + i1 * 18, 183, 36, 73, 17);
						}
						else
						{
							drawTexturedModalRect(l + 72, k + 25 + i1 * 18, 183, 18, 73, 17);
						}
					}
				}
			}
		}
    }
}
