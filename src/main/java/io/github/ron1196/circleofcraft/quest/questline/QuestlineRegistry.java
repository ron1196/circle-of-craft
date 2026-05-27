package io.github.ron1196.circleofcraft.quest.questline;

import java.util.LinkedHashMap;
import java.util.List;

public class QuestlineRegistry {

    private static final LinkedHashMap<String, Questline> QUESTS = new LinkedHashMap<>();

    public static final Questline RAFIKI = RafikiQuestline.build();
    public static final Questline OUTLANDS = OutlandsQuestline.build();

    static {
        QUESTS.put(RAFIKI.getId(), RAFIKI);
        QUESTS.put(OUTLANDS.getId(), OUTLANDS);
    }

    public static Questline get(String id) {
        return QUESTS.get(id);
    }

    public static List<Questline> getOrdered() {
        return List.copyOf(QUESTS.values());
    }
}
