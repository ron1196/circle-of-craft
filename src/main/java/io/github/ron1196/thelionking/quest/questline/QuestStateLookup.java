package io.github.ron1196.thelionking.quest.questline;

import io.github.ron1196.thelionking.quest.stage.StageId;

/**
 * Read-only view of quest state — the subset of {@link QuestlineManager} that questline
 * {@code canStart} predicates need. Implemented server-side by {@link QuestlineManager} and
 * client-side by {@code ClientQuestStateLookup} so the same predicate works on either side.
 */
public interface QuestStateLookup {

    String getStageId(String questId);

    boolean isStarted(String questId);

    boolean isComplete(String questId);

    boolean isStageAtOrPast(String questId, StageId target);
}
