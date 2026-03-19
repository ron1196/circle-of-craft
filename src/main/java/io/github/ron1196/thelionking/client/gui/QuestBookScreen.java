package io.github.ron1196.thelionking.client.gui;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.QuestCheckPacket;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.stage.IStageId;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QuestBookScreen extends Screen {

  private static final ResourceLocation BOOK_LEFT =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_left.png");
  private static final ResourceLocation BOOK_RIGHT =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/book_right.png");

  private static final int BOOK_WIDTH = 202;
  private static final int BOOK_HEIGHT = 256;
  private static final int TEXTURE_SIZE = 256;

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
    int buttonY = topY + 30;
    for (int i = 0; i < quests.size(); i++) {
      Questline quest = quests.get(i);
      final int questIdx = i;
      addRenderableWidget(
          Button.builder(
                  Component.literal(quest.getDisplayName()),
                  btn -> {
                    selectedQuest = questIdx;
                    // Mark as checked on server
                    Networking.CHANNEL.sendToServer(new QuestCheckPacket(quest.getId()));
                  })
              .bounds(centerX + 15, buttonY, 170, 20)
              .build());
      buttonY += 24;
    }
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(graphics);

    int centerX = (this.width - BOOK_WIDTH * 2) / 2;
    int topY = (this.height - BOOK_HEIGHT) / 2;

    // Draw book pages
    graphics.blit(
        BOOK_LEFT, centerX, topY, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
    graphics.blit(
        BOOK_RIGHT,
        centerX + BOOK_WIDTH,
        topY,
        0,
        0,
        BOOK_WIDTH,
        BOOK_HEIGHT,
        TEXTURE_SIZE,
        TEXTURE_SIZE);

    // Left page title
    graphics.drawCenteredString(
        font, "\u00a7lQuests", centerX + BOOK_WIDTH / 2, topY + 12, 0x140C02);

    List<Questline> quests = QuestlineRegistry.getOrdered();

    // Draw quest status indicators
    int buttonY = topY + 30;
    for (Questline quest : quests) {
      String stageId = ClientWorldState.getQuestStageId(quest.getId());
      boolean complete = quest.isComplete(stageId);
      boolean started = quest.isStarted(stageId) && !complete;
      // A quest is "in progress" if started but not complete, and past the first stage
      boolean inProgress = started && quest.getStageIndex(stageId) > 0;
      String status;
      int color;
      if (complete) {
        status = "\u2714";
        color = 0x00AA00;
      } else if (inProgress) {
        status = "\u25B6";
        color = 0xFFAA00;
      } else if (canStartOnClient(quest)) {
        status = "\u25CB";
        color = 0x5555FF;
      } else {
        status = "\u2716";
        color = 0xAA0000;
      }
      graphics.drawString(font, status, centerX + 188, buttonY + 6, color, false);
      buttonY += 24;
    }

    // Right page: quest details
    if (selectedQuest >= 0 && selectedQuest < quests.size()) {
      Questline quest = quests.get(selectedQuest);
      String stageId = ClientWorldState.getQuestStageId(quest.getId());
      boolean complete = quest.isComplete(stageId);
      int stageIndex = quest.getStageIndex(stageId);
      int rightX = centerX + BOOK_WIDTH + 15;
      int textY = topY + 15;

      // Quest name
      graphics.drawString(font, "\u00a7l" + quest.getDisplayName(), rightX, textY, 0x140C02, false);
      textY += 16;

      // Status
      String statusText;
      if (complete) {
        statusText = "\u00a72Complete";
      } else if (stageIndex > 0) {
        statusText =
            "\u00a76In Progress (Stage " + stageIndex + "/" + (quest.getNumStages() - 1) + ")";
      } else if (canStartOnClient(quest)) {
        statusText = "\u00a79Available";
      } else {
        statusText = "\u00a74Locked";
      }
      graphics.drawString(font, statusText, rightX, textY, 0x140C02, false);
      textY += 16;

      // Current objective
      if (!complete && stageIndex >= 0) {
        IStageId currentStage = stageIndex >= 0 ? quest.getStageOrder().get(stageIndex) : null;
        if (currentStage != null) {
          String objective = quest.getObjectiveByStage(currentStage);
          if (!objective.isEmpty()) {
            graphics.drawString(font, "\u00a7nObjective:", rightX, textY, 0x140C02, false);
            textY += 12;
            for (var line :
                font.getSplitter()
                    .splitLines(objective, 170, net.minecraft.network.chat.Style.EMPTY)) {
              graphics.drawString(font, line.getString(), rightX, textY, 0x404040, false);
              textY += 10;
            }
          }
        }
      }

      // Requirements
      if (!canStartOnClient(quest) && stageIndex <= 0) {
        String[] prereqs = quest.getPrerequisites();
        if (prereqs != null) {
          textY += 4;
          graphics.drawString(font, "\u00a7nRequirements:", rightX, textY, 0x140C02, false);
          textY += 12;
          for (String req : prereqs) {
            graphics.drawString(font, "- " + req, rightX, textY, 0x404040, false);
            textY += 10;
          }
        }
      }

      // Completed stages
      if (stageIndex > 0) {
        textY += 8;
        graphics.drawString(font, "\u00a7nCompleted:", rightX, textY, 0x140C02, false);
        textY += 12;
        List<IStageId> stages = quest.getStageOrder();
        for (int s = 0; s < stageIndex; s++) {
          String stageObj = quest.getObjectiveByStage(stages.get(s));
          if (stageObj != null && !stageObj.isEmpty() && !stageObj.equals("Quest complete")) {
            String line = "\u00a72\u2714 " + stageObj;
            for (var wrappedLine :
                font.getSplitter().splitLines(line, 170, net.minecraft.network.chat.Style.EMPTY)) {
              graphics.drawString(font, wrappedLine.getString(), rightX, textY, 0x404040, false);
              textY += 10;
            }
          }
        }
      }
    } else {
      // No quest selected
      int rightX = centerX + BOOK_WIDTH + 15;
      graphics.drawString(font, "Select a quest from", rightX, topY + 40, 0x404040, false);
      graphics.drawString(font, "the left page.", rightX, topY + 52, 0x404040, false);
    }

    super.render(graphics, mouseX, mouseY, partialTick);
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
