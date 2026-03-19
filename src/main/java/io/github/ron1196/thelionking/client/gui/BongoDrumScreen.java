package io.github.ron1196.thelionking.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.BongoDrumMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BongoDrumScreen extends AbstractContainerScreen<BongoDrumMenu> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/drum.png");

  public BongoDrumScreen(BongoDrumMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    // Render 3 enchantment buttons
    for (int i = 0; i < 3; i++) {
      int level = this.menu.enchantLevels[i];
      int buttonX = this.leftPos + 72;
      int buttonY = this.topPos + 25 + i * 18;

      int texV;
      if (level == 0) {
        texV = 0; // disabled
      } else if (mouseX >= buttonX
          && mouseX < buttonX + 73
          && mouseY >= buttonY
          && mouseY < buttonY + 17) {
        texV = 36; // hover
      } else {
        texV = 18; // available
      }
      graphics.blit(TEXTURE, buttonX, buttonY, 183, texV, 73, 17);

      if (level > 0) {
        String levelStr = String.valueOf(level);
        int textX = buttonX + 70 - this.font.width(levelStr);
        graphics.drawString(this.font, levelStr, textX, buttonY + 5, 0x1F190B, false);
      }
    }
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.drawString(this.font, this.title, 60, 7, 0x1F190B, false);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (int i = 0; i < 3; i++) {
      int buttonX = this.leftPos + 72;
      int buttonY = this.topPos + 25 + i * 18;
      if (mouseX >= buttonX
          && mouseX < buttonX + 73
          && mouseY >= buttonY
          && mouseY < buttonY + 17) {
        if (this.menu.enchantLevels[i] > 0) {
          this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
          return true;
        }
      }
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }
}
