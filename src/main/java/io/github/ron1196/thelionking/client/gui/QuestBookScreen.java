package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.data.ItemInfo;
import io.github.ron1196.thelionking.menu.QuestBookMenu;
import io.github.ron1196.thelionking.network.ClientQuestStateLookup;
import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.QuestCheckPacket;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import io.github.ron1196.thelionking.quest.stage.StageId;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

public class QuestBookScreen extends AbstractContainerScreen<QuestBookMenu> {

    private static final ResourceLocation BOOK_LEFT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_left.png");
    private static final ResourceLocation BOOK_RIGHT =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_right.png");
    private static final ResourceLocation BOOK_MENU =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_menu.png");

    private static final int X_SIZE = 450;
    private static final int Y_SIZE_TOTAL = 230;
    private static final int TEXTURE_SIZE = 256;

    private static final int PAGE_WIDTH = 202;
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
    private static final int LEFT_PAGE_CENTER_X = 250 + CONTENT_X_SHIFT;
    private static final int RIGHT_PAGE_CENTER_X = 454 + CONTENT_X_SHIFT;

    private static final int PAGE_TEXT_PRIMARY = 0xFF120C01;
    private static final int PAGE_TEXT_SECONDARY = 0xFF4B3A21;
    private static final int PAGE_TEXT_FLASH = 0xFF6A4E10;

    private static final int INV_GRID_X = 174 + CONTENT_X_SHIFT;
    private static final int INV_GRID_Y = 131;
    private static final int INV_GRID_W = 176;
    private static final int INV_GRID_H = 90;
    private static final int INV_GRID_U = 80;
    private static final int INV_GRID_V = 0;

    private static final int RECIPE_PANEL_X = 287 + CONTENT_X_SHIFT;
    private static final int RECIPE_PANEL_Y = 88;
    private static final int RECIPE_PANEL_W = 130;
    private static final int RECIPE_PANEL_H = 90;
    private static final int RECIPE_PANEL_U = 126;
    private static final int RECIPE_PANEL_V = 92;

    private static final int MAGNIFIER_X = 181 + CONTENT_X_SHIFT;
    private static final int MAGNIFIER_Y = 79;
    private static final int MAGNIFIER_W = 18;
    private static final int MAGNIFIER_H = 18;

    private int selectedQuest = -1;
    private int flashTimer = 0;

    private boolean recipeViewOpen = false;
    private int recipeIndex = 0;
    private List<Recipe<?>> matchingRecipes = Collections.emptyList();
    private ItemStack lastInspected = ItemStack.EMPTY;
    private Button magnifierButton;
    private Button recipePrevButton;
    private Button recipeNextButton;

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
        addRenderableWidget(
                new QuestBookMenuButton(buttonX, topPos + BUTTON_FIRST_Y, Component.literal("Main Page"), btn -> {
                    selectedQuest = -1;
                    closeRecipeView();
                }));

        List<Questline> quests = QuestlineRegistry.getOrdered();
        for (int i = 0; i < quests.size(); i++) {
            Questline quest = quests.get(i);
            final int questIdx = i;
            int rowY = topPos + BUTTON_FIRST_Y + ((i + 1) * BUTTON_VERTICAL_SPACING);
            addRenderableWidget(
                    new QuestBookMenuButton(buttonX, rowY, Component.literal(quest.getDisplayName()), btn -> {
                        selectedQuest = questIdx;
                        closeRecipeView();
                        Networking.CHANNEL.sendToServer(new QuestCheckPacket(quest.getId()));
                    }));
        }

        magnifierButton = Button.builder(Component.literal("?"), btn -> toggleRecipeView())
                .bounds(leftPos + MAGNIFIER_X, topPos + MAGNIFIER_Y, MAGNIFIER_W, MAGNIFIER_H)
                .build();
        addRenderableWidget(magnifierButton);

        int arrowsY = topPos + 93;
        recipePrevButton = Button.builder(Component.literal("<"), btn -> cycleRecipe(-1))
                .bounds(leftPos + 396 + CONTENT_X_SHIFT, arrowsY, 6, 11)
                .build();
        recipeNextButton = Button.builder(Component.literal(">"), btn -> cycleRecipe(1))
                .bounds(leftPos + 406 + CONTENT_X_SHIFT, arrowsY, 6, 11)
                .build();
        recipePrevButton.visible = false;
        recipeNextButton.visible = false;
        addRenderableWidget(recipePrevButton);
        addRenderableWidget(recipeNextButton);
    }

    private void toggleRecipeView() {
        if (selectedQuest >= 0) return;
        ItemStack stack = menu.getInspectedStack();
        if (stack.isEmpty()) {
            closeRecipeView();
            return;
        }
        if (recipeViewOpen) {
            closeRecipeView();
        } else {
            matchingRecipes = QuestBookRecipeRenderer.findMatching(stack);
            if (matchingRecipes.isEmpty()) {
                closeRecipeView();
                return;
            }
            recipeIndex = 0;
            lastInspected = stack.copy();
            recipeViewOpen = true;
            updateRecipeArrowVisibility();
        }
    }

    private void closeRecipeView() {
        recipeViewOpen = false;
        recipeIndex = 0;
        matchingRecipes = Collections.emptyList();
        lastInspected = ItemStack.EMPTY;
        updateRecipeArrowVisibility();
    }

    private void cycleRecipe(int delta) {
        if (matchingRecipes.isEmpty()) return;
        recipeIndex = Math.floorMod(recipeIndex + delta, matchingRecipes.size());
    }

    private void updateRecipeArrowVisibility() {
        boolean show = recipeViewOpen && matchingRecipes.size() > 1;
        if (recipePrevButton != null) recipePrevButton.visible = show;
        if (recipeNextButton != null) recipeNextButton.visible = show;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        flashTimer = (flashTimer + 1) % 20;
        ItemStack current = menu.getInspectedStack();
        if (current.isEmpty() || !ItemStack.isSameItem(current, lastInspected)) {
            if (recipeViewOpen) closeRecipeView();
        }
        if (magnifierButton != null) {
            magnifierButton.visible = selectedQuest < 0 && !current.isEmpty();
        }
        updateRecipeArrowVisibility();
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

        graphics.pose().popPose();

        renderTooltip(graphics, mouseX, mouseY);
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
                    leftPos + INV_GRID_X,
                    topPos + INV_GRID_Y,
                    INV_GRID_U,
                    INV_GRID_V,
                    INV_GRID_W,
                    INV_GRID_H,
                    TEXTURE_SIZE,
                    TEXTURE_SIZE);

            if (recipeViewOpen && !matchingRecipes.isEmpty()) {
                graphics.blit(
                        BOOK_MENU,
                        leftPos + RECIPE_PANEL_X,
                        topPos + RECIPE_PANEL_Y,
                        RECIPE_PANEL_U,
                        RECIPE_PANEL_V,
                        RECIPE_PANEL_W,
                        RECIPE_PANEL_H,
                        TEXTURE_SIZE,
                        TEXTURE_SIZE);
            }
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
        int currentColor = state.isDelayed() && flashTimer > 14 ? PAGE_TEXT_FLASH : PAGE_TEXT_PRIMARY;
        String objective = stage != null ? quest.getObjectiveByStage(stage) : "";
        drawCentered(graphics, Component.literal(objective), SPINE_X, 51, currentColor);

        for (int j = currentIdx - 1; j >= 0; j--) {
            StageId prev = quest.getStageOrder().get(j);
            String prevText = quest.getObjectiveByStage(prev) + " - Done";
            drawCentered(
                    graphics, Component.literal(prevText), SPINE_X, 63 + 13 * (currentIdx - j), PAGE_TEXT_SECONDARY);
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

    public int getSelectedQuest() {
        return selectedQuest;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
