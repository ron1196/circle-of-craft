package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.SimbaInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SimbaInventoryScreen extends AbstractContainerScreen<SimbaInventoryMenu> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/simba.png");

  private static final int TEXT_COLOR = 0x7A2804;

  public SimbaInventoryScreen(SimbaInventoryMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    this.imageWidth = 176;
    this.imageHeight = 166;
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight + 24);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.drawString(font, title, 74, 13, TEXT_COLOR, false);
    graphics.drawString(
        font, playerInventoryTitle, inventoryLabelX, imageHeight - 96 + 2, TEXT_COLOR, false);
  }
}
