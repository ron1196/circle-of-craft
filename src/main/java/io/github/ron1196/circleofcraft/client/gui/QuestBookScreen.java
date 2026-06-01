package io.github.ron1196.circleofcraft.client.gui;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.ItemInfo;
import io.github.ron1196.circleofcraft.menu.QuestBookMenu;
import io.github.ron1196.circleofcraft.network.ClientQuestStateLookup;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import io.github.ron1196.circleofcraft.network.Networking;
import io.github.ron1196.circleofcraft.network.QuestCheckPacket;
import io.github.ron1196.circleofcraft.quest.questline.Questline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import io.github.ron1196.circleofcraft.quest.stage.StageId;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QuestBookScreen extends AbstractContainerScreen<QuestBookMenu> {

    private static final ResourceLocation BOOK_LEFT = CircleOfCraftMod.id("textures/gui/book_left.png");
    private static final ResourceLocation BOOK_RIGHT = CircleOfCraftMod.id("textures/gui/book_right.png");
    private static final ResourceLocation BOOK_MENU = CircleOfCraftMod.id("textures/gui/book_menu.png");

    private static final int X_SIZE = 450;
    private static final int Y_SIZE_TOTAL = 230;
    private static final int TEXTURE_SIZE = 256;

    private static final int PAGE_WIDTH = 202;
    private static final int OBJECTIVE_MAX_WIDTH = 186;
    private static final int OBJECTIVE_LINE_HEIGHT = 11;
    private static final int PAGE_HEIGHT = 256;
    private static final int MENU_WIDTH = 65;

    private static final int CONTENT_X_SHIFT = -70;

    private static final int LEFT_PAGE_X = 150 + CONTENT_X_SHIFT;
    private static final int RIGHT_PAGE_X = 352 + CONTENT_X_SHIFT;
    private static final int MENU_STRIP_X = 15 + CONTENT_X_SHIFT + 5;

    private static final int BACKGROUND_Y_OFFSET = -15;

    private static final int BUTTON_COLUMN_X_OFFSET = -85;
    private static final int BUTTON_FIRST_Y = 8;
    private static final int BUTTON_VERTICAL_SPACING = 28;

    private static final int SPINE_X = 352 + CONTENT_X_SHIFT;
    private static final int RIGHT_PAGE_CENTER_X = 443 + CONTENT_X_SHIFT;

    private static final int PAGE_TEXT_PRIMARY = 0xFF120C01;
    private static final int PAGE_TEXT_SECONDARY = 0xFF4B3A21;

    private static final int INV_GRID_X = 174 + CONTENT_X_SHIFT;
    private static final int INV_GRID_Y = 131;
    private static final int INV_GRID_W = 176;
    private static final int INV_GRID_H = 90;
    private static final int INV_GRID_U = 80;
    private static final int INV_GRID_V = 0;

    private static final int INFO_FRAME_X = QuestBookMenu.INFO_SLOT_X - 4;
    private static final int INFO_FRAME_Y = QuestBookMenu.INFO_SLOT_Y - 4;
    private static final int INFO_FRAME_SIZE = 24;
    private static final int INFO_FRAME_U = 80;
    private static final int INFO_FRAME_V = 92;

    private int selectedQuest = -1;

    /**
     * Cycles 0..19 while the book is open. Drives tab-button flashing for unviewed quests.
     */
    static int flashTimer = 0;

    public QuestBookScreen(QuestBookMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = X_SIZE;
        this.imageHeight = Y_SIZE_TOTAL;
        this.titleLabelX = -10000;
        this.titleLabelY = -10000;
        this.inventoryLabelX = -10000;
        this.inventoryLabelY = -10000;
    }

    @Override
    protected void init() {
        super.init();

        int buttonX = leftPos + BUTTON_COLUMN_X_OFFSET;
        addRenderableWidget(new QuestBookMenuButton(
                buttonX, topPos + BUTTON_FIRST_Y, Component.literal("Main Page"), btn -> selectedQuest = -1));

        List<Questline> quests = QuestlineRegistry.getOrdered();
        for (int i = 0; i < quests.size(); i++) {
            Questline quest = quests.get(i);
            final int questIdx = i;
            int rowY = topPos + BUTTON_FIRST_Y + ((i + 1) * BUTTON_VERTICAL_SPACING);
            addRenderableWidget(new QuestBookMenuButton(
                    buttonX, rowY, Component.literal(quest.getDisplayName()), quest.getId(), btn -> {
                        selectedQuest = questIdx;
                        Networking.CHANNEL.sendToServer(new QuestCheckPacket(quest.getId()));
                    }));
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        flashTimer = (flashTimer + 1) % 20;
        menu.slotsVisible = selectedQuest < 0;
    }

    private static final float RENDER_SCALE = 0.65f;

    private double toLogicalX(double mx) {
        double cx = leftPos + imageWidth / 2.0;
        return (mx - cx) / RENDER_SCALE + cx;
    }

    private double toLogicalY(double my) {
        double cy = topPos + imageHeight / 2.0;
        return (my - cy) / RENDER_SCALE + cy;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        menu.slotsVisible = selectedQuest < 0;
        renderBackground(graphics);

        double cx = leftPos + imageWidth / 2.0;
        double cy = topPos + imageHeight / 2.0;
        graphics.pose().pushPose();
        graphics.pose().translate(cx, cy, 0);
        graphics.pose().scale(RENDER_SCALE, RENDER_SCALE, 1f);
        graphics.pose().translate(-cx, -cy, 0);

        int adjMouseX = (int) toLogicalX(mouseX);
        int adjMouseY = (int) toLogicalY(mouseY);
        super.render(graphics, adjMouseX, adjMouseY, partialTick);

        renderTooltip(graphics, adjMouseX, adjMouseY);

        graphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(toLogicalX(mouseX), toLogicalY(mouseY), button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(toLogicalX(mouseX), toLogicalY(mouseY), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return super.mouseDragged(toLogicalX(mouseX), toLogicalY(mouseY), button, dx / RENDER_SCALE, dy / RENDER_SCALE);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(toLogicalX(mouseX), toLogicalY(mouseY), delta);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderRibbon(graphics);
        renderPanels(graphics);

        if (selectedQuest < 0) {
            graphics.blit(
                    BOOK_MENU,
                    leftPos + INFO_FRAME_X,
                    topPos + INFO_FRAME_Y,
                    INFO_FRAME_U,
                    INFO_FRAME_V,
                    INFO_FRAME_SIZE,
                    INFO_FRAME_SIZE,
                    TEXTURE_SIZE,
                    TEXTURE_SIZE);

            graphics.blit(
                    BOOK_MENU,
                    leftPos + INV_GRID_X,
                    topPos + INV_GRID_Y,
                    INV_GRID_U,
                    INV_GRID_V,
                    INV_GRID_W,
                    INV_GRID_H,
                    TEXTURE_SIZE,
                    TEXTURE_SIZE);
        }

        renderForeground(graphics);
    }

    private void renderForeground(@NotNull GuiGraphics graphics) {
        if (selectedQuest < 0) {
            renderMainPage(graphics);
        } else {
            List<Questline> quests = QuestlineRegistry.getOrdered();
            if (selectedQuest < quests.size()) {
                renderQuestPage(graphics, quests.get(selectedQuest));
            }
        }
    }

    private void renderMainPage(@NotNull GuiGraphics graphics) {
        drawCentered(graphics, Component.literal("The Pride Lands Book of Quests"), SPINE_X, 9, PAGE_TEXT_PRIMARY);

        renderCornerIcons(graphics, ItemStack.EMPTY);

        String portal = "Portal location: " + ClientWorldState.playerHomePortalX + ", "
                + ClientWorldState.playerHomePortalY + ", "
                + ClientWorldState.playerHomePortalZ;
        drawCentered(graphics, Component.literal(portal), SPINE_X, 42, PAGE_TEXT_SECONDARY);

        drawCentered(
                graphics,
                Component.literal("If you lose this book, bring an ordinary book and some"),
                SPINE_X,
                63,
                PAGE_TEXT_SECONDARY);
        drawCentered(
                graphics,
                Component.literal("lion fur to Rafiki to receive another one."),
                SPINE_X,
                73,
                PAGE_TEXT_SECONDARY);

        ItemStack inspected = menu.getInspectedStack();
        if (!inspected.isEmpty()) {
            graphics.drawString(
                    font,
                    inspected.getHoverName(),
                    leftPos + 208 + CONTENT_X_SHIFT,
                    topPos + 108,
                    PAGE_TEXT_PRIMARY,
                    false);

            String[] info = ItemInfo.get(inspected);
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    drawCentered(
                            graphics,
                            Component.literal(info[i]),
                            RIGHT_PAGE_CENTER_X,
                            116 + (i * 10),
                            PAGE_TEXT_PRIMARY);
                }
            }
        }
    }

    private void renderQuestPage(@NotNull GuiGraphics graphics, @NotNull Questline quest) {
        drawCentered(graphics, Component.literal(quest.getDisplayName()), SPINE_X, 9, PAGE_TEXT_PRIMARY);

        renderCornerIcons(graphics, quest.getIcon());

        if (!quest.canStart(ClientQuestStateLookup.INSTANCE)) {
            drawCentered(
                    graphics,
                    Component.literal("You are not able to start this quest yet."),
                    SPINE_X,
                    51,
                    PAGE_TEXT_PRIMARY);
            drawCentered(graphics, Component.literal("Requirements:"), SPINE_X, 76, PAGE_TEXT_SECONDARY);
            String[] reqs = quest.getPrerequisites();
            if (reqs != null) {
                for (int i = 0; i < reqs.length; i++) {
                    drawCentered(graphics, Component.literal(reqs[i]), SPINE_X, 89 + 13 * i, PAGE_TEXT_SECONDARY);
                }
            }
            return;
        }

        QuestlineState state = ClientWorldState.questStates.get(quest.getId());
        if (state == null) {
            drawCentered(graphics, Component.literal("Quest not yet started."), SPINE_X, 51, PAGE_TEXT_PRIMARY);
            return;
        }

        StageId stage = quest.findStageByName(state.getCurrentStageId());
        int currentIdx = stage != null ? quest.getStageIndex(stage) : -1;
        boolean isComplete = stage != null && quest.isLastStage(stage);

        if (isComplete) {
            drawCentered(graphics, Component.literal("Quest complete!"), SPINE_X, 46, PAGE_TEXT_PRIMARY);
            return;
        }

        drawCentered(graphics, Component.literal("Current objective:"), SPINE_X, 37, PAGE_TEXT_SECONDARY);
        String objective = stage != null ? quest.getObjectiveByStage(stage) : "";
        List<FormattedCharSequence> objectiveLines = font.split(Component.literal(objective), OBJECTIVE_MAX_WIDTH);
        for (int i = 0; i < objectiveLines.size(); i++) {
            FormattedCharSequence line = objectiveLines.get(i);
            graphics.drawString(
                    font,
                    line,
                    leftPos + SPINE_X - font.width(line) / 2,
                    topPos + 51 + i * OBJECTIVE_LINE_HEIGHT,
                    PAGE_TEXT_PRIMARY,
                    false);
        }

        int doneBaseY = 51 + objectiveLines.size() * OBJECTIVE_LINE_HEIGHT + 14;
        for (int j = currentIdx - 1; j >= 0; j--) {
            StageId prev = quest.getStageOrder().get(j);
            String prevText = quest.getObjectiveByStage(prev) + " - Done";
            drawCentered(
                    graphics,
                    Component.literal(prevText),
                    SPINE_X,
                    doneBaseY + 13 * (currentIdx - 1 - j),
                    PAGE_TEXT_SECONDARY);
        }
    }

    private void renderCornerIcons(@NotNull GuiGraphics graphics, ItemStack icon) {
        if (icon == null || icon.isEmpty()) return;
        graphics.renderItem(icon, leftPos + 182 + CONTENT_X_SHIFT, topPos + 5);
        graphics.renderItem(icon, leftPos + 507 + CONTENT_X_SHIFT, topPos + 5);
    }

    private void drawCentered(@NotNull GuiGraphics graphics, @NotNull Component text, int centerX, int y, int color) {
        int x = leftPos + centerX - font.width(text) / 2;
        graphics.drawString(font, text, x, topPos + y, color, false);
    }

    private void renderPanels(@NotNull GuiGraphics graphics) {
        int bgY = topPos + BACKGROUND_Y_OFFSET;
        graphics.blit(
                BOOK_LEFT,
                leftPos + LEFT_PAGE_X,
                bgY,
                PAGE_WIDTH,
                PAGE_HEIGHT,
                0f,
                0f,
                PAGE_WIDTH,
                PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
        graphics.blit(
                BOOK_RIGHT,
                leftPos + RIGHT_PAGE_X,
                bgY,
                PAGE_WIDTH,
                PAGE_HEIGHT,
                0f,
                0f,
                PAGE_WIDTH,
                PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
    }

    private void renderRibbon(@NotNull GuiGraphics graphics) {
        int bgY = topPos + BACKGROUND_Y_OFFSET;
        graphics.blit(
                BOOK_MENU,
                leftPos + MENU_STRIP_X,
                bgY,
                MENU_WIDTH,
                PAGE_HEIGHT,
                0f,
                0f,
                MENU_WIDTH,
                PAGE_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
    }
}
