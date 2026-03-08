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
import net.minecraft.client.renderer.texture.TextureMap;
import java.nio.ByteBuffer;
import cpw.mods.fml.common.network.*;

import net.minecraft.src.*;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LKGuiQuests extends GuiScreen
{
	private static final ResourceLocation texturePageLeft = new ResourceLocation("lionking:gui/book_left.png");
	private static final ResourceLocation texturePageRight = new ResourceLocation("lionking:gui/book_right.png");
	private static final ResourceLocation texturePageMenu = new ResourceLocation("lionking:gui/book_menu.png");
	
    private RenderItem itemRenderer = new RenderItem();
    private int xSize;
    private int ySize;
    private Container inventorySlots;
    private int guiLeft;
    private int guiTop;
	private List matchedRecipes;
	private boolean craftGuiOpen;
	private int recipeIndex;
	private ItemStack prevLoreStack;
	
	private static int page = -1;
	private static LKQuestBase selectedQuest;
	public static int flashTimer;
	
	private EntityPlayer thePlayer;

    public LKGuiQuests(EntityPlayer entityplayer)
    {
		inventorySlots = new LKContainerItemInfo(entityplayer);
		xSize = 540;
		ySize = 256;
		thePlayer = entityplayer;
    }

	@Override
    public void initGui()
    {
        super.initGui();
        mc.thePlayer.openContainer = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
		updatePage();
		buttonList.add(new LKGuiQuestsButton(-1, guiLeft - 20, guiTop + 8, null));
		for (int i = 0; i < LKQuestBase.orderedQuests.size(); i++)
		{
			LKQuestBase quest = (LKQuestBase)LKQuestBase.orderedQuests.get(i);
			if (quest != null)
			{
				buttonList.add(new LKGuiQuestsButton(i, guiLeft - 20, guiTop + 8 + ((i + 1) * 28), quest));
			}
		}
    }
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		if (page == -1)
		{
			if (inventorySlots != null && !thePlayer.worldObj.isRemote)
			{
				ItemStack itemstack = inventorySlots.getSlot(0).getStack();
				if (itemstack != null)
				{
					if (!thePlayer.inventory.addItemStackToInventory(itemstack))
					{
						thePlayer.dropPlayerItem(itemstack);
					}
					inventorySlots.getSlot(0).putStack(null);
				}
			}
		}
		craftGuiOpen = false;
		recipeIndex = 0;
        page = button.id;
		updatePage();
    }
	
	private void updatePage()
	{
		if (page == -1)
		{
			selectedQuest = null;
		}
		else
		{
			selectedQuest = (LKQuestBase)LKQuestBase.orderedQuests.get(page);
			if (selectedQuest.canStart())
			{
				byte[] data = new byte[1];
				data[0] = (byte)selectedQuest.questIndex;
				Packet250CustomPayload packet = new Packet250CustomPayload("lk.sendQCheck", data);
				PacketDispatcher.sendPacketToServer(packet);
			}
		}
	}

	private void drawBackground(int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texturePageLeft);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2 - 15;
        drawTexturedModalRect(l + 150, i1, 0, 0, 202, ySize);
        mc.getTextureManager().bindTexture(texturePageRight);
        drawTexturedModalRect(l + 352, i1, 0, 0, 202, ySize);
		mc.getTextureManager().bindTexture(texturePageMenu);
		drawTexturedModalRect(l + 15, i1, 0, 0, 65, ySize);
		
		if (page == -1)
		{
			drawTexturedModalRect(l + 178, i1 + 115, 80, 92, 24, 24);
			drawTexturedModalRect(l + 174, i1 + 146, 80, 0, 176, 90);
			
			if (inventorySlots != null)
			{
				ItemStack itemstack = inventorySlots.getSlot(0).getStack();
				if (itemstack != null)
				{
					List recipes = getMatchingRecipes(itemstack);
					if (!recipes.isEmpty())
					{
						matchedRecipes = recipes;
					}
					else
					{
						matchedRecipes = null;
						craftGuiOpen = false;
						recipeIndex = 0;
					}
				}
			}
			
			if (matchedRecipes != null)
			{
				boolean mouseOver = i >= l + 181 && j >= i1 + 94 && i < l + 199 && j < i1 + 112;
				drawTexturedModalRect(l + 181, i1 + 94, 80, mouseOver ? 138 : 118, 18, 18);
			}
		}
	}
	
	private void drawForeground()
	{
		drawCenteredString(mc.fontRenderer, translate(getPageTitle()), 352, 9, 0x120C01);
		
		itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, getPageIcon(), 182, 5);
		itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, getPageIcon(), 182, 5);
		itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, getPageIcon(), 507, 5);
		itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, getPageIcon(), 507, 5);
		
		if (selectedQuest != null)
		{
			if (selectedQuest.canStart())
			{
				int i = selectedQuest.getQuestStage();
				if (i == selectedQuest.getNumStages())
				{
					drawCenteredString(mc.fontRenderer, "Quest complete!", 352, 46, 0x120C01);
				}
				else
				{
					drawCenteredString(mc.fontRenderer, "Current objective:", 352, 37, 0x4B3A21);
					String s = translate(selectedQuest.getObjectiveByStage(i));
					if (selectedQuest.isDelayed())
					{
						s = "";
					}
					drawCenteredString(mc.fontRenderer, s, 352, 51, flashTimer > 14 ? 0x6A4E10 : 0x120C01);
				}
				
				for (int j = selectedQuest.isDelayed() ? i : i - 1; j >= 0; j--)
				{
					drawCenteredString(mc.fontRenderer, translate(selectedQuest.getObjectiveByStage(j)) + " - Done", 352, 63 + 13 * (i - j), 0x4B3A21);
				}
			}
			else
			{
				drawCenteredString(mc.fontRenderer, "You are not able to start this quest yet.", 352, 51, 0x120C01);
				drawCenteredString(mc.fontRenderer, "Requirements:", 352, 76, 0x4B3A21);
				
				String[] requirements = selectedQuest.getRequirements();
				for (int i = 0; i < requirements.length; i++)
				{
					drawCenteredString(mc.fontRenderer, translate(requirements[i]), 352, 89 + 13 * i, 0x4B3A21);
				}
			}
		}
		else
		{
			String s = "Portal location: " + LKLevelData.homePortalX + ", " + LKLevelData.homePortalY + ", " + LKLevelData.homePortalZ;
			drawCenteredString(mc.fontRenderer, s, 352, 42, 0x4B3A21);
			
			String s1 = "If you lose this book, bring an ordinary book and some";
			String s2 = "lion fur to Rafiki to receive another one.";
			drawCenteredString(mc.fontRenderer, s1, 352, 63, 0x4B3A21);
			drawCenteredString(mc.fontRenderer, s2, 352, 73, 0x4B3A21);
			
			ItemStack itemstack = ((Slot)inventorySlots.getSlot(0)).getStack();
			if (itemstack != null)
			{
				mc.fontRenderer.drawString(itemstack.getItem().getItemDisplayName(itemstack), 208, 108, 0x120C01);
				
				String[] info = LKItemInfo.getItemInfo(itemstack);
				for (int i = 0; i < info.length; i++)
				{
					drawCenteredString(mc.fontRenderer, translate(info[i]), 443, 116 + (i * 10), 0x120C01);
				}
			}
		}
	}
	
	private void drawCraftGui(int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texturePageMenu);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2 - 15;
        drawTexturedModalRect(l + 287, i1 + 103, 126, 92, 130, 90);
		
		if (matchedRecipes.size() > 1)
		{
			boolean mouseOver = i >= l + 396 && j >= i1 + 108 && i < l + 402 && j < i1 + 119;
			drawTexturedModalRect(l + 396, i1 + 108, 107, mouseOver ? 105 : 92, 6, 11);
			mouseOver = i >= l + 406 && j >= i1 + 108 && i < l + 412 && j < i1 + 119;
			drawTexturedModalRect(l + 406, i1 + 108, 117, mouseOver ? 105 : 92, 6, 11);
		}
		
		String title = "";
		ItemStack itemstack = inventorySlots.getSlot(0).getStack();
		Object obj = (Object)matchedRecipes.get(recipeIndex);
		if (obj instanceof ShapedRecipes)
		{
			title = "Crafting";
			ShapedRecipes shapedRecipe = (ShapedRecipes)obj;
			int width = shapedRecipe.recipeWidth;
			ItemStack[] ingredients = shapedRecipe.recipeItems;
			for (int k = 0; k < shapedRecipe.getRecipeSize(); k++)
			{
				ItemStack ingredient = ingredients[k];
				if (ingredient == null)
				{
					continue;
				}
				int xPos = l + 299 + ((k % width) * 18);
				int yPos = i1 + 129 + (MathHelper.floor_double(k / width) * 18);
				if (ingredient.itemID == itemstack.itemID && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == itemstack.getItemDamage()))
				{
					mc.getTextureManager().bindTexture(texturePageMenu);
					drawTexturedModalRect(xPos, yPos, 80, 158, 16, 16);
				}
				setupItemRendering();
				itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ingredient.itemID, 1, ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (ingredient.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : ingredient.getItemDamage()), xPos, yPos);
				itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ingredient.itemID, 1, ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (ingredient.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : ingredient.getItemDamage()), xPos, yPos);
				finishItemRendering();
			}
			ItemStack result = shapedRecipe.getRecipeOutput();
			int xPos = l + 387;
			int yPos = i1 + 147;
			if (result.itemID == itemstack.itemID && (result.getItem().isItemTool(result) || result.getItemDamage() == itemstack.getItemDamage()))
			{
				mc.getTextureManager().bindTexture(texturePageMenu);
				drawTexturedModalRect(xPos - 1, yPos - 1, 80, 158, 18, 18);
			}
			setupItemRendering();
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(result.itemID, result.stackSize, result.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (result.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : result.getItemDamage()), xPos, yPos);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(result.itemID, result.stackSize, result.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (result.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : result.getItemDamage()), xPos, yPos);
			finishItemRendering();
		}
		else if (obj instanceof ShapelessRecipes)
		{
			title = "Crafting";
			ShapelessRecipes shapelessRecipe = (ShapelessRecipes)obj;
			List ingredients = shapelessRecipe.recipeItems;
			for (int k = 0; k < shapelessRecipe.getRecipeSize(); k++)
			{
				ItemStack ingredient = (ItemStack)ingredients.get(k);
				int xPos = l + 299 + ((k % 3) * 18);
				int yPos = i1 + 129 + (MathHelper.floor_double(k / 3) * 18);
				if (ingredient.itemID == itemstack.itemID && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == itemstack.getItemDamage()))
				{
					mc.getTextureManager().bindTexture(texturePageMenu);
					drawTexturedModalRect(xPos, yPos, 80, 158, 16, 16);
				}
				setupItemRendering();
				itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ingredient.itemID, 1, ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (ingredient.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : ingredient.getItemDamage()), xPos, yPos);
				itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(ingredient.itemID, 1, ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (ingredient.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : ingredient.getItemDamage()), xPos, yPos);
				finishItemRendering();
			}
			ItemStack result = shapelessRecipe.getRecipeOutput();
			int xPos = l + 387;
			int yPos = i1 + 147;
			if (result.itemID == itemstack.itemID && (result.getItem().isItemTool(result) || result.getItemDamage() == itemstack.getItemDamage()))
			{
				mc.getTextureManager().bindTexture(texturePageMenu);
				drawTexturedModalRect(xPos - 1, yPos - 1, 80, 158, 18, 18);
			}
			setupItemRendering();
			itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(result.itemID, result.stackSize, result.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (result.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : result.getItemDamage()), xPos, yPos);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(result.itemID, result.stackSize, result.getItemDamage() == OreDictionary.WILDCARD_VALUE ? (result.itemID == itemstack.itemID ? itemstack.getItemDamage() : 0) : result.getItemDamage()), xPos, yPos);
			finishItemRendering();
		}
		
		drawCenteredString(mc.fontRenderer, title, l + 352, i1 + 109, 0x120C01);
	}
	
	@Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k)
    {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }
	
	private String getPageTitle()
	{
		if (page == -1)
		{
			return "The Pride Lands Book of Quests";
		}
		return selectedQuest.getName();
	}
	
	private ItemStack getPageIcon()
	{
		if (selectedQuest != null)
		{
			return selectedQuest.getIcon();
		}
		else
		{
			return new ItemStack(mod_LionKing.questBook, 1, 1);
		}
	}
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        int var4 = guiLeft;
        int var5 = guiTop;
        drawBackground(par1, par2);
		super.drawScreen(par1, par2, par3);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var4, (float)var5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        Slot var6 = null;
        short var7 = 240;
        short var8 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var7 / 1.0F, (float)var8 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var10;
		
		if (page == -1)
		{
			for (int var20 = 0; var20 < inventorySlots.inventorySlots.size(); ++var20)
			{
				Slot var23 = (Slot)inventorySlots.inventorySlots.get(var20);
				drawSlotInventory(var23);

				if (isMouseOverSlot(var23, par1, par2))
				{
					var6 = var23;
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					int var9 = var23.xDisplayPosition;
					var10 = var23.yDisplayPosition;
					drawGradientRect(var9, var10, var9 + 16, var10 + 16, -2130706433, -2130706433);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
				}
			}
		}

        drawForeground();
		
		if (page == -1)
		{
			InventoryPlayer var21 = mc.thePlayer.inventory;

			if (var21.getItemStack() != null)
			{
				GL11.glTranslatef(0.0F, 0.0F, 32.0F);
				zLevel = 200.0F;
				itemRenderer.zLevel = 200.0F;
				itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, var21.getItemStack(), par1 - var4 - 8, par2 - var5 - 8);
				itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, var21.getItemStack(), par1 - var4 - 8, par2 - var5 - 8);
				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
			}
			
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			if (var21.getItemStack() == null && var6 != null && var6.getHasStack())
			{
				ItemStack var22 = var6.getStack();
				List var24 = var22.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

				if (var24.size() > 0)
				{
					var10 = 0;
					int var11;
					int var12;

					for (var11 = 0; var11 < var24.size(); ++var11)
					{
						var12 = fontRenderer.getStringWidth((String)var24.get(var11));

						if (var12 > var10)
						{
							var10 = var12;
						}
					}

					var11 = par1 - var4 + 12;
					var12 = par2 - var5 - 12;
					int var14 = 8;

					if (var24.size() > 1)
					{
						var14 += 2 + (var24.size() - 1) * 10;
					}

					zLevel = 300.0F;
					itemRenderer.zLevel = 300.0F;
					int var15 = -267386864;
					drawGradientRect(var11 - 3, var12 - 4, var11 + var10 + 3, var12 - 3, var15, var15);
					drawGradientRect(var11 - 3, var12 + var14 + 3, var11 + var10 + 3, var12 + var14 + 4, var15, var15);
					drawGradientRect(var11 - 3, var12 - 3, var11 + var10 + 3, var12 + var14 + 3, var15, var15);
					drawGradientRect(var11 - 4, var12 - 3, var11 - 3, var12 + var14 + 3, var15, var15);
					drawGradientRect(var11 + var10 + 3, var12 - 3, var11 + var10 + 4, var12 + var14 + 3, var15, var15);
					int var16 = 1347420415;
					int var17 = (var16 & 16711422) >> 1 | var16 & -16777216;
					drawGradientRect(var11 - 3, var12 - 3 + 1, var11 - 3 + 1, var12 + var14 + 3 - 1, var16, var17);
					drawGradientRect(var11 + var10 + 2, var12 - 3 + 1, var11 + var10 + 3, var12 + var14 + 3 - 1, var16, var17);
					drawGradientRect(var11 - 3, var12 - 3, var11 + var10 + 3, var12 - 3 + 1, var16, var16);
					drawGradientRect(var11 - 3, var12 + var14 + 2, var11 + var10 + 3, var12 + var14 + 3, var17, var17);

					for (int var18 = 0; var18 < var24.size(); ++var18)
					{
						String var19 = (String)var24.get(var18);

						if (var18 == 0)
						{
							var19 = "\u00a7" + Integer.toHexString(var22.getRarity().rarityColor) + var19;
						}
						else
						{
							var19 = "\u00a77" + var19;
						}

						fontRenderer.drawStringWithShadow(var19, var11, var12, -1);

						if (var18 == 0)
						{
							var12 += 2;
						}

						var12 += 10;
					}

					zLevel = 0.0F;
					itemRenderer.zLevel = 0.0F;
				}
			}
			
			if (craftGuiOpen)
			{
				try
				{
					GL11.glTranslatef((float)-var4, (float)-var5, 0.0F);
					drawDefaultBackground();
					drawCraftGui(par1, par2);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawSlotInventory(Slot par1Slot)
    {
        int var2 = par1Slot.xDisplayPosition;
        int var3 = par1Slot.yDisplayPosition;
        ItemStack var4 = par1Slot.getStack();
        boolean var5 = false;
        zLevel = 100.0F;
        itemRenderer.zLevel = 100.0F;

        if (var4 == null)
        {
            Icon icon = par1Slot.getBackgroundIconIndex();

            if (icon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                drawTexturedModelRectFromIcon(var2, var3, icon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                var5 = true;
            }
        }

        if (!var5)
        {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, var4, var2, var3);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, var4, var2, var3);
        }

        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    private Slot getSlotAtPosition(int par1, int par2)
    {
        for (int var3 = 0; var3 < inventorySlots.inventorySlots.size(); ++var3)
        {
            Slot var4 = (Slot)inventorySlots.inventorySlots.get(var3);

            if (isMouseOverSlot(var4, par1, par2))
            {
                return var4;
            }
        }

        return null;
    }

	@Override
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
		boolean flag = k == mc.gameSettings.keyBindPickBlock.keyCode + 100;

        if (k == 0 || k == 1 || flag)
        {
			int l = (width - xSize) / 2;
			int i1 = (height - ySize) / 2 - 15;
			if (matchedRecipes != null && i >= l + 181 && j >= i1 + 94 && i < l + 199 && j < i1 + 112)
			{
				craftGuiOpen = !craftGuiOpen;
				recipeIndex = 0;
				mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			}
			
			if (craftGuiOpen && matchedRecipes.size() > 1)
			{
				if (i >= l + 396 && j >= i1 + 108 && i < l + 402 && j < i1 + 119)
				{
					int newIndex = recipeIndex - 1;
					if (newIndex < 0)
					{
						newIndex = matchedRecipes.size() - 1;
					}
					recipeIndex = newIndex;
					mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}
				
				if (i >= l + 406 && j >= i1 + 108 && i < l + 412 && j < i1 + 119)
				{
					int newIndex = recipeIndex + 1;
					if (newIndex == matchedRecipes.size())
					{
						newIndex = 0;
					}
					recipeIndex = newIndex;
					mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
				}
			}
			
            Slot var4 = getSlotAtPosition(i, j);
            int var5 = guiLeft;
            int var6 = guiTop;
            boolean var7 = i < var5 || j < var6 || i >= var5 + xSize || j >= var6 + ySize;
            int var8 = -1;

            if (var4 != null)
            {
                var8 = var4.slotNumber;
            }

            if (var7)
            {
                var8 = -999;
            }

            if (var8 != -1)
            {
				if (flag)
				{
					handleMouseClick(var4, var8, k, 3);
				}
                boolean var9 = var8 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                handleMouseClick(var4, var8, k, var9 ? 1 : 0);
            }
        }
    }

    private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
    {
		if (page > -1)
		{
			return false;
		}
        int var4 = guiLeft;
        int var5 = guiTop;
        par2 -= var4;
        par3 -= var5;
        return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1 && par3 < par1Slot.yDisplayPosition + 16 + 1;
    }

    protected void handleMouseClick(Slot slot, int i, int j, int k)
    {
		if (page == -1)
		{
			if (slot != null)
			{
				i = slot.slotNumber;
			}

			mc.playerController.windowClick(inventorySlots.windowId, i, j, k, mc.thePlayer);
		}
    }

	@Override
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
			if (craftGuiOpen)
			{
				craftGuiOpen = false;
				recipeIndex = 0;
			}
			else
			{
				thePlayer.closeScreen();
			}
        }
    }

	@Override
    public void onGuiClosed()
    {
        if (thePlayer != null)
        {
            inventorySlots.onContainerClosed(thePlayer);
        }
    }

	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

	@Override
    public void updateScreen()
    {
        super.updateScreen();
        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
		if (flashTimer == 19)
		{
			flashTimer = 0;
		}
		else
		{
			flashTimer++;
		}
		
		if (matchedRecipes == null)
		{
			craftGuiOpen = false;
			recipeIndex = 0;
		}
		
		ItemStack itemstack = inventorySlots.getSlot(0).getStack();
		if (itemstack == null || (prevLoreStack != null && itemstack != prevLoreStack))
		{
			craftGuiOpen = false;
			recipeIndex = 0;
			matchedRecipes = null;
		}
		if (itemstack != null)
		{
			prevLoreStack = itemstack;
		}
    }
	
	private String translate(String text)
	{
		if (mc.getLanguageManager().getCurrentLanguage().getLanguageCode().equals("en_US"))
		{
			text = replace(text, "centre", "center");
			text = replace(text, "armour", "armor");
			text = replace(text, "colour", "color");
			text = replace(text, "honour", "honor");
			text = replace(text, "favour", "favor");
			text = replace(text, "neighbour", "neighbor");
			text = replace(text, "labour", "labor");
			text = replace(text, "customise", "customize");
			text = replace(text, "savannah", "savanna");
		}
		return text;
	}

	private String replace(String text, String pattern, String replace)
	{
		int s = 0;
		int e = 0;
		StringBuffer newText = new StringBuffer();

		while ((e = text.indexOf(pattern, s)) >= 0)
		{
			newText.append(text.substring(s, e));
			newText.append(replace);
			s = e + pattern.length();
		}
		
		newText.append(text.substring(s));
		return newText.toString();
	}
	
	private List getMatchingRecipes(ItemStack itemstack)
	{
		List matchingRecipes = new ArrayList();
		List recipes = CraftingManager.getInstance().getRecipeList();
		try
		{
			recipeLoop:
			for (int i = 0; i < recipes.size(); i++)
			{
				Object obj = (Object)recipes.get(i);
				if (obj instanceof ShapedRecipes)
				{
					ShapedRecipes shapedRecipe = (ShapedRecipes)obj;
					ItemStack[] ingredients = shapedRecipe.recipeItems;
					for (int j = 0; j < shapedRecipe.getRecipeSize(); j++)
					{
						ItemStack ingredient = ingredients[j];
						if (ingredient == null)
						{
							continue;
						}
						if (ingredient.itemID == itemstack.itemID && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == itemstack.getItemDamage()))
						{
							matchingRecipes.add(shapedRecipe);
							continue recipeLoop;
						}
					}
					ItemStack result = shapedRecipe.getRecipeOutput();
					if (result.itemID == itemstack.itemID && (result.getItemDamage() == itemstack.getItemDamage() || result.getItem().isItemTool(result)))
					{
						matchingRecipes.add(shapedRecipe);
					}
				}
				else if (obj instanceof ShapelessRecipes)
				{
					ShapelessRecipes shapelessRecipe = (ShapelessRecipes)obj;
					List ingredients = shapelessRecipe.recipeItems;
					for (int j = 0; j < ingredients.size(); j++)
					{
						ItemStack ingredient = (ItemStack)ingredients.get(j);
						if (ingredient.itemID == itemstack.itemID && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE || ingredient.getItemDamage() == itemstack.getItemDamage()))
						{
							matchingRecipes.add(shapelessRecipe);
							continue recipeLoop;
						}
					}
					ItemStack result = shapelessRecipe.getRecipeOutput();
					if (result.itemID == itemstack.itemID && (result.getItemDamage() == itemstack.getItemDamage() || result.getItem().isItemTool(result)))
					{
						matchingRecipes.add(shapelessRecipe);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return matchingRecipes;
	}
	
	private void setupItemRendering()
	{
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void finishItemRendering()
	{
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();
	}
}
