package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuestBookMenuButton extends Button {

    public static final int WIDTH = 135;
    public static final int HEIGHT = 20;

    private static final ResourceLocation TEXTURE = CircleOfCraftMod.id("textures/gui/book_menu.png");

    private static final int TEXTURE_SIZE = 256;
    private static final int LABEL_COLOR = 0x120C01;

    private static final int STATE_U = 121;
    private static final int STATE_V_NORMAL = 216;
    private static final int STATE_V_FLASH = 236;

    @Nullable
    private final String questId;

    public QuestBookMenuButton(int x, int y, @NotNull Component label, @NotNull OnPress onPress) {
        this(x, y, label, null, onPress);
    }

    public QuestBookMenuButton(
            int x, int y, @NotNull Component label, @Nullable String questId, @NotNull OnPress onPress) {
        super(x, y, WIDTH, HEIGHT, label, onPress, DEFAULT_NARRATION);
        this.questId = questId;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int half = WIDTH / 2;
        int v = isFlashing() ? STATE_V_FLASH : STATE_V_NORMAL;
        graphics.blit(TEXTURE, getX(), getY(), STATE_U, v, half, HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        graphics.blit(TEXTURE, getX() + half, getY(), TEXTURE_SIZE - half, v, half, HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

        Minecraft mc = Minecraft.getInstance();
        String label = fitText(mc.font, getMessage().getString(), WIDTH - 8);
        int labelX = getX() + (WIDTH - mc.font.width(label)) / 2;
        int labelY = getY() + (HEIGHT - 8) / 2;
        graphics.drawString(mc.font, label, labelX, labelY, LABEL_COLOR, false);
    }

    private boolean isFlashing() {
        return questId != null && !ClientWorldState.isQuestChecked(questId) && QuestBookScreen.flashTimer > 14;
    }

    private static String fitText(net.minecraft.client.gui.Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) return text;
        String ellipsis = "...";
        int budget = maxWidth - font.width(ellipsis);
        if (budget <= 0) return ellipsis;
        return font.plainSubstrByWidth(text, budget) + ellipsis;
    }
}
