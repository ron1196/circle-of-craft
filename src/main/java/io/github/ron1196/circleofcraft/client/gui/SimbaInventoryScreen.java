package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.SimbaInventoryMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SimbaInventoryScreen extends AbstractContainerScreen<SimbaInventoryMenu> {

    private static final Identifier TEXTURE = CircleOfCraftMod.id("textures/gui/simba.png");

    private static final int TEXT_COLOR = 0x7A2804;

    public SimbaInventoryScreen(SimbaInventoryMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight + 24, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, title, 74, 13, TEXT_COLOR, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, imageHeight - 96 + 2, TEXT_COLOR, false);
    }
}
