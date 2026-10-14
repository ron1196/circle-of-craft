package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.entity.BugTrapBlockEntity;
import io.github.ron1196.circleofcraft.menu.BugTrapMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BugTrapScreen extends AbstractContainerScreen<BugTrapMenu> {

    private static final Identifier TEXTURE = CircleOfCraftMod.id("textures/gui/trap.png");

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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

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
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        graphics.text(
                this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);
    }
}
