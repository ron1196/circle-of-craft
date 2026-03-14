package io.github.ron1196.thelionking.quest;

import net.minecraft.nbt.CompoundTag;

public class LKQuestState {

    private int currentStage;
    private boolean checked;

    public LKQuestState() {
        this(0, false);
    }

    public LKQuestState(int currentStage, boolean checked) {
        this.currentStage = currentStage;
        this.checked = checked;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int stage) {
        this.currentStage = stage;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putInt("Stage", currentStage);
        tag.putBoolean("Checked", checked);
    }

    public static LKQuestState readFromNBT(CompoundTag tag) {
        int stage = tag.getInt("Stage");
        boolean checked = tag.getBoolean("Checked");
        return new LKQuestState(stage, checked);
    }
}
