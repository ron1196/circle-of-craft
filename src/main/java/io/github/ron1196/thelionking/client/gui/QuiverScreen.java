package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.QuiverMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuiverScreen extends AbstractContainerScreen<QuiverMenu> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/quiver.png");

  public QuiverScreen(QuiverMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    this.imageWidth = 176;
    this.imageHeight = 133;
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    graphics.drawString(
        font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
  }
}
