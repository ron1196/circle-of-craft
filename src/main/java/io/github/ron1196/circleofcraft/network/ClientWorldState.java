package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientWorldState {

    // Quest state
    public static final Map<String, QuestlineState> questStates = new HashMap<>();

    // Player data
    public static boolean receivedQuestBook;
    public static int playerHomePortalX;
    public static int playerHomePortalY;
    public static int playerHomePortalZ;
    public static boolean hasSimba;
    public static final Set<String> claimedRewards = new HashSet<>();

    // HUD overlay timers
    public static int flatulenceTimer = 0;

    // Portal overlay state (counts UP from server, reset to 0 when player leaves portal)
    public static int portalOverlayTicks = 0;
    public static String portalBlockName = "";
    public static long portalLastUpdateTick = 0;

    /**
     * Returns the stage ID string for the given quest. Empty string means the quest has not been
     * initialized.
     */
    public static String getQuestStageId(String questId) {
        QuestlineState state = questStates.get(questId);
        return state != null ? state.getCurrentStageId() : "";
    }

    public static boolean isQuestChecked(String questId) {
        QuestlineState state = questStates.get(questId);
        return state != null && state.isChecked();
    }

    public static void reset() {
        questStates.clear();

        receivedQuestBook = false;

        playerHomePortalX = 0;
        playerHomePortalY = 0;
        playerHomePortalZ = 0;

        hasSimba = false;
        claimedRewards.clear();
        flatulenceTimer = 0;

        portalOverlayTicks = 0;
        portalBlockName = "";
        portalLastUpdateTick = 0;
    }
}
