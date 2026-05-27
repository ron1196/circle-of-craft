package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.quest.questline.QuestStateLookup;
import io.github.ron1196.circleofcraft.quest.questline.Questline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import io.github.ron1196.circleofcraft.quest.stage.StageId;

/**
 * Client-side {@link QuestStateLookup} that reads from {@link ClientWorldState#questStates}.
 * Lets canStart predicates run on the client without dragging in the server-only
 * {@code QuestlineManager}.
 */
public final class ClientQuestStateLookup implements QuestStateLookup {

    public static final ClientQuestStateLookup INSTANCE = new ClientQuestStateLookup();

    private ClientQuestStateLookup() {}

    @Override
    public String getStageId(String questId) {
        QuestlineState state = ClientWorldState.questStates.get(questId);
        return state != null ? state.getCurrentStageId() : "";
    }

    @Override
    public boolean isStarted(String questId) {
        Questline quest = QuestlineRegistry.get(questId);
        return quest != null && quest.isStarted(getStageId(questId));
    }

    @Override
    public boolean isComplete(String questId) {
        Questline quest = QuestlineRegistry.get(questId);
        return quest != null && quest.isComplete(getStageId(questId));
    }

    @Override
    public boolean isStageAtOrPast(String questId, StageId target) {
        Questline quest = QuestlineRegistry.get(questId);
        if (quest == null) return false;
        String stageId = getStageId(questId);
        if (stageId.isEmpty()) {
            return quest.getStageIndex(quest.getFirstStage()) >= quest.getStageIndex(target);
        }
        return quest.isAtOrPast(stageId, target);
    }
}
