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
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.network.*;

public class LKGuiSimba extends GuiContainer
{ 
	private LKEntitySimba theSimba;
	private static final ResourceLocation texture = new ResourceLocation("lionking:gui/simba.png");

    public LKGuiSimba(EntityPlayer entityplayer, LKEntitySimba simba)
    {  
        super(new LKContainerSimba(entityplayer, simba));
		theSimba = simba;
    }  

	@Override
    public void initGui()
    {
		super.initGui();
        buttonList.add(new LKGuiSimbaButton(0, guiLeft + 77, guiTop + 54, theSimba));
    }
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
		{
			byte[] data = new byte[5];
			byte[] id = ByteBuffer.allocate(4).putInt(theSimba.entityId).array();
			for (int i = 0; i < 4; i++)
			{
				data[i] = id[i];
			}
			data[4] = (byte)mc.thePlayer.dimension;
			Packet250CustomPayload packet = new Packet250CustomPayload("lk.simbaSit", data);
			PacketDispatcher.sendPacketToServer(packet);
		}
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int i, int j)  
    {
		int textcolour = 0x7A2804;
		fontRenderer.drawString("Simba", 74, 13, textcolour);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, textcolour);
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
