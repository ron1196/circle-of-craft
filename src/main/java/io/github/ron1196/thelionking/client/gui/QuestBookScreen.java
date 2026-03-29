package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.QuestCheckPacket;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.stage.StageId;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QuestBookScreen extends Screen {

    private static final ResourceLocation BOOK_LEFT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_left.png");
    private static final ResourceLocation BOOK_RIGHT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_right.png");

    private static final int BOOK_WIDTH = 202;
    private static final int BOOK_HEIGHT = 256;
    private static final int TEXTURE_SIZE = 256;
    private static final int CONTENT_OFFSET_Y = 50;
    private static final int TITLE_OFFSET_Y = 25;
    private static final int LEFT_PAGE_PADDING = 30;
    private static final int RIGHT_PAGE_PADDING = 15;
    private static final int BUTTON_WIDTH = 155;
    private static final int TEXT_WRAP_WIDTH = 170;
    private static final int CHECKMARK_WRAP_WIDTH = 155;
    private static final int LINE_HEIGHT = 10;
    private static final int SECTION_GAP = 8;
    private static final int HEADING_GAP = 12;
    private static final int STATUS_GAP = 16;

    private static final int COLOR_BOOK_TEXT = 0x140C02;
    private static final int COLOR_BODY_TEXT = 0x404040;
    private static final int COLOR_GREEN = 0x00AA00;

    private int selectedQuest = -1;

    public QuestBookScreen() {
        super(Component.literal("Quest Book"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = (this.width - BOOK_WIDTH * 2) / 2;
        int topY = (this.height - BOOK_HEIGHT) / 2;

        List<Questline> quests = QuestlineRegistry.getOrdered();
        int buttonY = topY + CONTENT_OFFSET_Y;
        for (int i = 0; i < quests.size(); i++) {
            Questline quest = quests.get(i);
            final int questIdx = i;
            addRenderableWidget(Button.builder(Component.literal(quest.getDisplayName()), btn -> {
                        selectedQuest = questIdx;
                        Networking.CHANNEL.sendToServer(new QuestCheckPacket(quest.getId()));
                    })
                    .bounds(centerX + LEFT_PAGE_PADDING, buttonY, BUTTON_WIDTH, 20)
                    .build());
            buttonY += 24;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        int centerX = (this.width - BOOK_WIDTH * 2) / 2;
        int topY = (this.height - BOOK_HEIGHT) / 2;

        renderBookPages(graphics, centerX, topY);
        renderLeftPage(graphics, centerX, topY);
        renderRightPage(graphics, centerX, topY);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderBookPages(GuiGraphics graphics, int centerX, int topY) {
        graphics.blit(BOOK_LEFT, centerX, topY, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        graphics.blit(
                BOOK_RIGHT, centerX + BOOK_WIDTH, topY, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    private void renderLeftPage(GuiGraphics graphics, int centerX, int topY) {
        graphics.drawCenteredString(font, "§lQuests", centerX + BOOK_WIDTH / 2, topY + TITLE_OFFSET_Y, COLOR_BOOK_TEXT);

        List<Questline> quests = QuestlineRegistry.getOrdered();
        int buttonY = topY + CONTENT_OFFSET_Y;
        for (Questline quest : quests) {
            renderQuestStatusIcon(graphics, quest, centerX + LEFT_PAGE_PADDING + BUTTON_WIDTH + 5, buttonY + 6);
            buttonY += 24;
        }
    }

    private void renderQuestStatusIcon(GuiGraphics graphics, Questline quest, int x, int y) {
        String stageId = ClientWorldState.getQuestStageId(quest.getId());
        boolean complete = quest.isComplete(stageId);
        boolean started = quest.isStarted(stageId) && !complete;
        boolean inProgress = started && quest.getStageIndex(stageId) > 0;

        String status;
        int color;
        if (complete) {
            status = "✔";
            color = 0x00AA00;
        } else if (inProgress) {
            status = "▶";
            color = 0xFFAA00;
        } else if (canStartOnClient(quest)) {
            status = "○";
            color = 0x5555FF;
        } else {
            status = "✖";
            color = 0xAA0000;
        }

        graphics.drawString(font, status, x, y, color, false);
    }

    private void renderRightPage(GuiGraphics graphics, int centerX, int topY) {
        List<Questline> quests = QuestlineRegistry.getOrdered();
        int rightX = centerX + BOOK_WIDTH + RIGHT_PAGE_PADDING;

        if (selectedQuest < 0 || selectedQuest >= quests.size()) {
            graphics.drawString(font, "Select a quest from", rightX, topY + 60, COLOR_BODY_TEXT, false);
            graphics.drawString(font, "the left page.", rightX, topY + 72, COLOR_BODY_TEXT, false);
            return;
        }

        Questline quest = quests.get(selectedQuest);
        String stageId = ClientWorldState.getQuestStageId(quest.getId());
        boolean complete = quest.isComplete(stageId);
        int stageIndex = quest.getStageIndex(stageId);

        int textY = topY + CONTENT_OFFSET_Y;
        textY = renderQuestHeader(graphics, quest, complete, stageIndex, rightX, textY);
        textY = renderCurrentObjective(graphics, quest, complete, stageIndex, rightX, textY);
        textY = renderRequirements(graphics, quest, stageIndex, rightX, textY);
        renderCompletedStages(graphics, quest, stageIndex, rightX, textY);
    }

    private int renderQuestHeader(
            GuiGraphics graphics, Questline quest, boolean complete, int stageIndex, int x, int y) {
        graphics.drawString(font, "§l" + quest.getDisplayName(), x, y, COLOR_BOOK_TEXT, false);
        y += STATUS_GAP;

        String statusText;
        if (complete) {
            statusText = "§2Complete";
        } else if (stageIndex > 0) {
            statusText = "§6In Progress (Stage " + stageIndex + "/" + (quest.getNumStages() - 1) + ")";
        } else if (canStartOnClient(quest)) {
            statusText = "§9Available";
        } else {
            statusText = "§4Locked";
        }
        graphics.drawString(font, statusText, x, y, COLOR_BOOK_TEXT, false);
        y += STATUS_GAP;

        return y;
    }

    private int renderCurrentObjective(
            GuiGraphics graphics, Questline quest, boolean complete, int stageIndex, int x, int y) {
        if (complete || stageIndex < 0) return y;

        StageId currentStage = quest.getStageOrder().get(stageIndex);
        if (currentStage == null) return y;

        String objective = quest.getObjectiveByStage(currentStage);
        if (objective.isEmpty()) return y;

        graphics.drawString(font, "§nObjective:", x, y, COLOR_BOOK_TEXT, false);
        y += HEADING_GAP;

        for (FormattedText line : font.getSplitter().splitLines(objective, TEXT_WRAP_WIDTH, Style.EMPTY)) {
            graphics.drawString(font, line.getString(), x, y, COLOR_BODY_TEXT, false);
            y += LINE_HEIGHT;
        }

        return y;
    }

    private int renderRequirements(GuiGraphics graphics, Questline quest, int stageIndex, int x, int y) {
        if (canStartOnClient(quest) || stageIndex > 0) return y;

        String[] prereqs = quest.getPrerequisites();
        if (prereqs == null) return y;

        y += 4;
        graphics.drawString(font, "§nRequirements:", x, y, COLOR_BOOK_TEXT, false);
        y += HEADING_GAP;

        for (String req : prereqs) {
            graphics.drawString(font, "- " + req, x, y, COLOR_BODY_TEXT, false);
            y += LINE_HEIGHT;
        }

        return y;
    }

    private void renderCompletedStages(GuiGraphics graphics, Questline quest, int stageIndex, int x, int y) {
        if (stageIndex <= 0) return;

        y += SECTION_GAP;
        graphics.drawString(font, "§nCompleted:", x, y, COLOR_BOOK_TEXT, false);
        y += HEADING_GAP;

        List<StageId> stages = quest.getStageOrder();
        for (int s = 0; s < stageIndex; s++) {
            String stageObj = quest.getObjectiveByStage(stages.get(s));
            if (stageObj == null || stageObj.isEmpty() || stageObj.equals("Quest complete")) continue;

            List<FormattedText> lines = font.getSplitter().splitLines(stageObj, CHECKMARK_WRAP_WIDTH, Style.EMPTY);
            for (int i = 0; i < lines.size(); i++) {
                String prefix = i == 0 ? "§2✔ " : "§2   ";
                graphics.drawString(font, prefix + lines.get(i).getString(), x, y, COLOR_GREEN, false);
                y += LINE_HEIGHT;
            }
        }
    }

    private static boolean canStartOnClient(Questline quest) {
        String[] prereqs = quest.getPrerequisites();
        if (prereqs == null) return true;
        for (String prereqName : prereqs) {
            for (Questline other : QuestlineRegistry.getOrdered()) {
                if (other.getDisplayName().equals(prereqName)
                        || ("Complete " + other.getDisplayName()).equals(prereqName)) {
                    if (!other.isComplete(ClientWorldState.getQuestStageId(other.getId()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
