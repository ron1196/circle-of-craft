package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.BongoDrumMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BongoDrumScreen extends AbstractContainerScreen<BongoDrumMenu> {

    private static final Identifier TEXTURE = CircleOfCraftMod.id("textures/gui/drum.png");

    public BongoDrumScreen(BongoDrumMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Render 3 enchantment buttons
        for (int i = 0; i < 3; i++) {
            int level = this.menu.enchantLevels[i];
            int buttonX = this.leftPos + 72;
            int buttonY = this.topPos + 25 + i * 18;

            int texV;
            if (level == 0) {
                texV = 0; // disabled
            } else if (mouseX >= buttonX && mouseX < buttonX + 73 && mouseY >= buttonY && mouseY < buttonY + 17) {
                texV = 36; // hover
            } else {
                texV = 18; // available
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, buttonX, buttonY, 183, texV, 73, 17, 256, 256);

            if (level > 0) {
                String levelStr = String.valueOf(level);
                int textX = buttonX + 70 - this.font.width(levelStr);
                graphics.text(this.font, levelStr, textX, buttonY + 5, 0x1F190B, false);
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 60, 7, 0x1F190B, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        for (int i = 0; i < 3; i++) {
            int buttonX = this.leftPos + 72;
            int buttonY = this.topPos + 25 + i * 18;
            if (event.x() >= buttonX && event.x() < buttonX + 73 && event.y() >= buttonY && event.y() < buttonY + 17) {
                if (this.menu.enchantLevels[i] > 0) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
}
