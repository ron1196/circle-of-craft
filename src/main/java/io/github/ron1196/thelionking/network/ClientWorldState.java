package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.LKQuestState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientWorldState {

    // World state
    public static boolean defeatedScar;
    public static int ziraStage;
    public static int pumbaaStage;
    public static boolean outlandersHostile;

    // Quest state
    public static final Map<String, LKQuestState> questStates = new HashMap<>();

    // Player data
    public static boolean receivedQuestBook;
    public static int playerHomePortalX;
    public static int playerHomePortalY;
    public static int playerHomePortalZ;
    public static boolean hasSimba;
    public static final Set<String> claimedRewards = new HashSet<>();

    public static int getQuestStage(String questId) {
        LKQuestState state = questStates.get(questId);
        return state != null ? state.getCurrentStage() : 0;
    }

    public static boolean isQuestChecked(String questId) {
        LKQuestState state = questStates.get(questId);
        return state != null && state.isChecked();
    }

    public static void reset() {
        defeatedScar = false;
        ziraStage = 0;
        pumbaaStage = 0;
        outlandersHostile = false;
        questStates.clear();

        receivedQuestBook = false;
        playerHomePortalX = 0;
        playerHomePortalY = 0;
        playerHomePortalZ = 0;
        hasSimba = false;
        claimedRewards.clear();
    }
}
