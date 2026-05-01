package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QuestBookMenuButton extends Button {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 16;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_menu.png");

    private static final int TEXTURE_SIZE = 256;
    private static final int LABEL_COLOR = 0x120C01;

    private static final int STATE_U = 121;
    private static final int STATE_V_NORMAL = 216;

    public QuestBookMenuButton(int x, int y, @NotNull Component label, @NotNull OnPress onPress) {
        super(x, y, WIDTH, HEIGHT, label, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(TEXTURE, getX(), getY(), STATE_U, STATE_V_NORMAL, WIDTH, HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        Minecraft mc = Minecraft.getInstance();
        String label = fitText(mc.font, getMessage().getString(), WIDTH - 6);
        int labelX = getX() + (WIDTH - mc.font.width(label)) / 2;
        int labelY = getY() + (HEIGHT - 8) / 2;
        graphics.drawString(mc.font, label, labelX, labelY, LABEL_COLOR, false);
    }

    private static String fitText(net.minecraft.client.gui.Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) return text;
        String ellipsis = "...";
        int budget = maxWidth - font.width(ellipsis);
        return font.plainSubstrByWidth(text, budget) + ellipsis;
    }
}
