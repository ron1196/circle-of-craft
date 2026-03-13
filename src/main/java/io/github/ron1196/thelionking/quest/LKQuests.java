package io.github.ron1196.thelionking.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry of all quest instances and lookup collections.
 */
public class LKQuests {
    public static final LKQuestBase[] ALL_QUESTS = new LKQuestBase[16];
    public static final List<LKQuestBase> ORDERED_QUESTS = new ArrayList<>();

    public static final LKQuestBase RAFIKI_QUEST = new LKQuestRafiki(0).setName("Rafiki's Quest");
    public static final LKQuestBase OUTLANDS_QUEST = new LKQuestOutlands(1).setName("An Outlandish Scheme");

    static {
        ORDERED_QUESTS.add(RAFIKI_QUEST);
        ORDERED_QUESTS.add(OUTLANDS_QUEST);
    }
}
