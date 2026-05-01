package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.QuestCheckPacket;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

public class QuestBookScreen extends Screen {

    private static final ResourceLocation BOOK_LEFT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_left.png");
    private static final ResourceLocation BOOK_RIGHT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_right.png");
    private static final ResourceLocation BOOK_MENU =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_menu.png");

    private static final int X_SIZE = 380;
    private static final int Y_SIZE = 180;
    private static final int TEXTURE_SIZE = 256;

    private static final int SRC_PAGE_WIDTH = 202;
    private static final int SRC_PAGE_HEIGHT = 256;
    private static final int SRC_MENU_WIDTH = 65;

    private static final int MENU_STRIP_X = 11;
    private static final int MENU_STRIP_WIDTH = 46;
    private static final int LEFT_PAGE_X = 75;
    private static final int LEFT_PAGE_WIDTH = 130;
    private static final int RIGHT_PAGE_X = 205;
    private static final int RIGHT_PAGE_WIDTH = 165;

    private static final int BACKGROUND_Y_OFFSET = -11;
    private static final int BUTTON_COLUMN_X_OFFSET = -10;
    private static final int BUTTON_COLUMN_Y_OFFSET = 6;
    private static final int BUTTON_VERTICAL_SPACING = 18;

    private static final int PAGE_TEXT_INSET_X = 24;
    private static final int PAGE_TITLE_TOP = 14;
    private static final int PAGE_BODY_TOP = 50;
    private static final int PAGE_TEXT_COLOR = 0xFF2A1A0A;

    private int guiLeft;
    private int guiTop;

    private int selectedQuest = -1;

    public QuestBookScreen() {
        super(Component.literal("Quest Book"));
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (this.width - X_SIZE) / 2;
        guiTop = (this.height - Y_SIZE) / 2;

        int buttonX = guiLeft + BUTTON_COLUMN_X_OFFSET;
        int buttonY = guiTop + BUTTON_COLUMN_Y_OFFSET;

        addRenderableWidget(
                new QuestBookMenuButton(buttonX, buttonY, Component.literal("Main Page"), btn -> selectedQuest = -1));

        List<Questline> quests = QuestlineRegistry.getOrdered();
        for (int i = 0; i < quests.size(); i++) {
            Questline quest = quests.get(i);
            final int questIdx = i;
            int rowY = buttonY + ((i + 1) * BUTTON_VERTICAL_SPACING);
            addRenderableWidget(
                    new QuestBookMenuButton(buttonX, rowY, Component.literal(quest.getDisplayName()), btn -> {
                        selectedQuest = questIdx;
                        Networking.CHANNEL.sendToServer(new QuestCheckPacket(quest.getId()));
                    }));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        renderPanels(graphics);
        renderQuestContent(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderQuestContent(@NotNull GuiGraphics graphics) {
        if (selectedQuest < 0) return;
        List<Questline> quests = QuestlineRegistry.getOrdered();
        if (selectedQuest >= quests.size()) return;
        Questline quest = quests.get(selectedQuest);

        int leftX = guiLeft + LEFT_PAGE_X + PAGE_TEXT_INSET_X;
        int rightX = guiLeft + RIGHT_PAGE_X + PAGE_TEXT_INSET_X;
        int titleY = guiTop + BACKGROUND_Y_OFFSET + PAGE_TITLE_TOP;
        int bodyTop = guiTop + BACKGROUND_Y_OFFSET + PAGE_BODY_TOP;
        int bodyBottom = guiTop + BACKGROUND_Y_OFFSET + Y_SIZE - PAGE_TITLE_TOP;
        int leftTextWidth = LEFT_PAGE_WIDTH - (PAGE_TEXT_INSET_X * 2);
        int rightTextWidth = RIGHT_PAGE_WIDTH - (PAGE_TEXT_INSET_X * 2);

        for (FormattedCharSequence titleLine : font.split(Component.literal(quest.getDisplayName()), leftTextWidth)) {
            graphics.drawString(font, titleLine, leftX, titleY, PAGE_TEXT_COLOR, false);
            titleY += font.lineHeight + 1;
        }

        QuestlineState state = ClientWorldState.questStates.get(quest.getId());
        String stageId = state != null ? state.getCurrentStageId() : null;
        String objective = stageId != null ? quest.getObjectiveByStageId(stageId) : null;
        Component body = Component.literal(objective != null ? objective : "Quest not yet started.");

        int lineStep = font.lineHeight + 1;
        int rightPageY = bodyTop;
        for (FormattedCharSequence line : font.split(body, rightTextWidth)) {
            if (rightPageY + font.lineHeight > bodyBottom) break;
            graphics.drawString(font, line, rightX, rightPageY, PAGE_TEXT_COLOR, false);
            rightPageY += lineStep;
        }
    }

    private void renderPanels(@NotNull GuiGraphics graphics) {
        int bgY = guiTop + BACKGROUND_Y_OFFSET;
        graphics.blit(
                BOOK_LEFT,
                guiLeft + LEFT_PAGE_X,
                bgY,
                LEFT_PAGE_WIDTH,
                Y_SIZE,
                0f,
                0f,
                SRC_PAGE_WIDTH,
                SRC_PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
        graphics.blit(
                BOOK_RIGHT,
                guiLeft + RIGHT_PAGE_X,
                bgY,
                RIGHT_PAGE_WIDTH,
                Y_SIZE,
                0f,
                0f,
                SRC_PAGE_WIDTH,
                SRC_PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
        graphics.blit(
                BOOK_MENU,
                guiLeft + MENU_STRIP_X,
                bgY,
                MENU_STRIP_WIDTH,
                Y_SIZE,
                0f,
                0f,
                SRC_MENU_WIDTH,
                SRC_PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
    }

    public int getSelectedQuest() {
        return selectedQuest;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
