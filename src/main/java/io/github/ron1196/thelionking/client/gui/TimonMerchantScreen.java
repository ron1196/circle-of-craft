package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.TimonMerchantMenu;
import io.github.ron1196.thelionking.menu.TimonTradeSlot;
import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TimonMerchantScreen extends AbstractContainerScreen<TimonMerchantMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/timon.png");

    // GUI is 176 wide, 190 tall (166 logical + 24px decorative strip, matching old mod)
    private static final int GUI_WIDTH           = 176;
    private static final int GUI_HEIGHT          = 190;
    private static final int INVENTORY_LABEL_Y   = 72; // old mod: ySize(166) - 96 + 2

    // Text colour matching old mod: dark brown
    private static final int TEXT_COLOR = 0x7F472F;

    public TimonMerchantScreen(TimonMerchantMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth  = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
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
        graphics.drawString(font, "Timon & Pumbaa", 51, 13, TEXT_COLOR, false);
        graphics.drawString(font, playerInventoryTitle, 8, INVENTORY_LABEL_Y, TEXT_COLOR, false);

        // Draw bug icon and cost number below each trade slot
        ItemStack bugStack = new ItemStack(Items.BUG.get());
        for (int i = 0; i < 5; i++) {
            if (!(this.menu.slots.get(i) instanceof TimonTradeSlot tradeSlot)) continue;
            String cost = String.valueOf(tradeSlot.getBugCost());
            int bugX = cost.length() == 1 ? 18 + 33 * i : 23 + 33 * i;
            graphics.renderItem(bugStack, bugX, 51);
            int numX = cost.length() == 1 ? 11 + 33 * i : 10 + 33 * i;
            graphics.drawString(font, cost, numX, 55, TEXT_COLOR, false);
        }
    }
}
