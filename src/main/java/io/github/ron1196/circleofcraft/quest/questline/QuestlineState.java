package io.github.ron1196.circleofcraft.quest.questline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class QuestlineState {

    public static final Codec<QuestlineState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("Stage", "").forGetter(QuestlineState::getCurrentStageId),
                    Codec.BOOL.optionalFieldOf("Checked", false).forGetter(QuestlineState::isChecked))
            .apply(instance, QuestlineState::new));

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
}
