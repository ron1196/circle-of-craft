package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.TimonMerchantMenu;
import io.github.ron1196.circleofcraft.menu.TimonTradeSlot;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TimonMerchantScreen extends AbstractContainerScreen<TimonMerchantMenu> {

    private static final Identifier TEXTURE = CircleOfCraftMod.id("textures/gui/timon.png");

    // GUI is 176 wide, 190 tall (166 logical + 24px decorative strip, matching old mod)
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 190;
    private static final int INVENTORY_LABEL_Y = 72; // old mod: ySize(166) - 96 + 2

    // Text colour matching old mod: dark brown
    private static final int TEXT_COLOR = 0x7F472F;

    public TimonMerchantScreen(TimonMerchantMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, "Timon & Pumbaa", 51, 13, TEXT_COLOR, false);
        graphics.text(font, playerInventoryTitle, 8, INVENTORY_LABEL_Y, TEXT_COLOR, false);

        // Draw bug icon and cost number below each trade slot
        ItemStack bugStack = new ItemStack(ModItems.BUG.get());
        for (int i = 0; i < 5; i++) {
            if (!(this.menu.slots.get(i) instanceof TimonTradeSlot tradeSlot)) continue;
            String cost = String.valueOf(tradeSlot.getBugCost());
            int bugX = cost.length() == 1 ? 18 + 33 * i : 23 + 33 * i;
            graphics.item(bugStack, bugX, 51);
            int numX = cost.length() == 1 ? 11 + 33 * i : 10 + 33 * i;
            graphics.text(font, cost, numX, 55, TEXT_COLOR, false);
        }
    }
}
