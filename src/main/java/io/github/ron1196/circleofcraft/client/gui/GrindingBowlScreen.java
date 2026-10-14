package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.GrindingBowlMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class GrindingBowlScreen extends AbstractContainerScreen<GrindingBowlMenu> {

    private static final Identifier TEXTURE = CircleOfCraftMod.id("textures/gui/grind.png");

    private static final int TITLE_BAR_CENTER_X = 81;

    public GrindingBowlScreen(GrindingBowlMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        // Draw progress arrow
        int grindTime = menu.getGrindTime();
        int maxGrindTime = menu.getMaxGrindTime();
        if (maxGrindTime > 0 && grindTime > 0) {
            int progressWidth = grindTime * 45 / maxGrindTime;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 62, topPos + 35, 176, 0, progressWidth, 15, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int titleX = TITLE_BAR_CENTER_X - font.width(title) / 2;
        graphics.text(font, title, titleX, titleLabelY, 0x140C02, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x140C02, false);
    }
}
