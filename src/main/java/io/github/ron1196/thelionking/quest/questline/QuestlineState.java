package io.github.ron1196.thelionking.quest.questline;

import net.minecraft.nbt.CompoundTag;

public class QuestlineState {

    private String currentStageId;
    private boolean checked;
    private boolean delayed;

    public QuestlineState() {
        this("", false, false);
    }

    public QuestlineState(String stageId, boolean checked) {
        this(stageId, checked, false);
    }

    public QuestlineState(String stageId, boolean checked, boolean delayed) {
        this.currentStageId = stageId;
        this.checked = checked;
        this.delayed = delayed;
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

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putString("Stage", currentStageId);
        tag.putBoolean("Checked", checked);
        tag.putBoolean("Delayed", delayed);
    }

    public static QuestlineState readFromNBT(CompoundTag tag) {
        String stageId;
        if (tag.contains("Stage", 8)) {
            stageId = tag.getString("Stage");
        } else if (tag.contains("Stage", 3)) {
            stageId = "";
        } else {
            stageId = "";
        }
        boolean checked = tag.getBoolean("Checked");
        boolean delayed = tag.getBoolean("Delayed");
        return new QuestlineState(stageId, checked, delayed);
    }
}
