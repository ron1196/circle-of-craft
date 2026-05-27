package io.github.ron1196.circleofcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.entity.BugTrapBlockEntity;
import io.github.ron1196.circleofcraft.menu.BugTrapMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BugTrapScreen extends AbstractContainerScreen<BugTrapMenu> {

    private static final ResourceLocation TEXTURE = CircleOfCraftMod.id("textures/gui/trap.png");

    private static final int PROGRESS_X = 91;
    private static final int PROGRESS_Y = 30;
    private static final int PROGRESS_WIDTH = 4;
    private static final int PROGRESS_HEIGHT = 22;
    private static final int PROGRESS_BG_COLOR = 0xFF2A1F12;
    private static final int PROGRESS_FILL_COLOR = 0xFF8FCB4F;

    public BugTrapScreen(BugTrapMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int x = this.leftPos + PROGRESS_X;
        int y = this.topPos + PROGRESS_Y;
        graphics.fill(x, y, x + PROGRESS_WIDTH, y + PROGRESS_HEIGHT, PROGRESS_BG_COLOR);

        int progress = this.menu.getTrapProgress();
        int filled = Math.min(PROGRESS_HEIGHT, progress * PROGRESS_HEIGHT / BugTrapBlockEntity.TRAP_INTERVAL);
        if (filled > 0) {
            graphics.fill(
                    x, y + PROGRESS_HEIGHT - filled, x + PROGRESS_WIDTH, y + PROGRESS_HEIGHT, PROGRESS_FILL_COLOR);
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
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        graphics.drawString(
                this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);
    }
}
