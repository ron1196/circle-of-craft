package io.github.ron1196.thelionking.quest.questline;

import net.minecraft.nbt.CompoundTag;

public class QuestlineState {

  private String currentStageId;
  private boolean checked;

  public QuestlineState() {
    this("", false);
  }

  public QuestlineState(String stageId, boolean checked) {
    this.currentStageId = stageId;
    this.checked = checked;
  }

  public String getCurrentStageId() {
    return currentStageId;
  }

  public void setCurrentStageId(String stageId) {
    this.currentStageId = stageId;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public void writeToNBT(CompoundTag tag) {
    tag.putString("Stage", currentStageId);
    tag.putBoolean("Checked", checked);
  }

  public static QuestlineState readFromNBT(CompoundTag tag) {
    String stageId;
    if (tag.contains("Stage", 8)) {
      // New string-based format
      stageId = tag.getString("Stage");
    } else if (tag.contains("Stage", 3)) {
      // Legacy int-based format — leave empty so the questline defaults to its first stage
      stageId = "";
    } else {
      stageId = "";
    }
    boolean checked = tag.getBoolean("Checked");
    return new QuestlineState(stageId, checked);
  }
}
