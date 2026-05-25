package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.GrindingBowlMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GrindingBowlScreen extends AbstractContainerScreen<GrindingBowlMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/grind.png");

    public GrindingBowlScreen(GrindingBowlMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        io.github.ron1196.thelionking.TheLionKingMod.LOGGER.info(
                "[JEI-DBG] GrindingBowlScreen opened with menu class={}, classLoader={}",
                menu.getClass(),
                menu.getClass().getClassLoader());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Draw progress arrow
        int grindTime = menu.getGrindTime();
        int maxGrindTime = menu.getMaxGrindTime();
        if (maxGrindTime > 0 && grindTime > 0) {
            int progressWidth = grindTime * 45 / maxGrindTime;
            graphics.blit(TEXTURE, leftPos + 62, topPos + 35, 176, 0, progressWidth, 15);
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
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x140C02, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x140C02, false);
    }
}
