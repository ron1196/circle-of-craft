package io.github.ron1196.circleofcraft.quest.questline;

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
        String stageId = tag.contains("Stage", 8) ? tag.getString("Stage") : "";
        boolean checked = tag.getBoolean("Checked");
        return new QuestlineState(stageId, checked);
    }
}
