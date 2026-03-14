package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.quest.questlines.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questlines.RafikiQuestline;

import java.util.LinkedHashMap;
import java.util.List;

public class LKQuestRegistry {

    private static final LinkedHashMap<String, LKQuestline> QUESTS = new LinkedHashMap<>();

    public static final LKQuestline RAFIKI = RafikiQuestline.build();
    public static final LKQuestline OUTLANDS = OutlandsQuestline.build();

    static {
        QUESTS.put(RAFIKI.getId(), RAFIKI);
        QUESTS.put(OUTLANDS.getId(), OUTLANDS);
    }

    public static LKQuestline get(String id) {
        return QUESTS.get(id);
    }

    public static List<LKQuestline> getOrdered() {
        return List.copyOf(QUESTS.values());
    }
}
