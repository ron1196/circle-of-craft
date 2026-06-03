package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.network.LoginSyncPacket.QuestEntry;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import net.minecraft.client.Minecraft;

/**
 * Client-only payload handler bodies. Referenced only from within {@code enqueueWork} lambdas of
 * playToClient packets, so the JVM never links this class on a dedicated server.
 */
final class ClientPayloadHandlers {

    private static final float SPINNING_RAMP_RATE = 0.0125F;

    static void handleQuestSync(QuestSyncPacket pkt) {
        ClientWorldState.questStates.put(pkt.questId(), new QuestlineState(pkt.stageId(), pkt.checked()));
    }

    static void handleLoginSync(LoginSyncPacket pkt) {
        ClientWorldState.questStates.clear();
        for (QuestEntry entry : pkt.questEntries()) {
            ClientWorldState.questStates.put(entry.questId(), new QuestlineState(entry.stageId(), entry.checked()));
        }
        applyPlayerData(
                pkt.receivedQuestBook(),
                pkt.homePortalX(),
                pkt.homePortalY(),
                pkt.homePortalZ(),
                pkt.hasSimba(),
                pkt.claimedRewards());
    }

    static void handlePlayerDataSync(PlayerDataSyncPacket pkt) {
        applyPlayerData(
                pkt.receivedQuestBook(),
                pkt.homePortalX(),
                pkt.homePortalY(),
                pkt.homePortalZ(),
                pkt.hasSimba(),
                pkt.claimedRewards());
    }

    static void handleFlatulence() {
        ClientWorldState.flatulenceTimer = 60;
    }

    static void handlePortalOverlay(PortalOverlayPacket pkt) {
        ClientWorldState.portalOverlayTicks = pkt.ticks();
        ClientWorldState.portalBlockName = pkt.portalBlockName();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            ClientWorldState.portalLastUpdateTick = mc.level.getGameTime();
        }
        if (mc.player != null) {
            float intensity = Math.min(pkt.ticks() * SPINNING_RAMP_RATE, 1.0F);
            mc.player.oSpinningEffectIntensity = mc.player.spinningEffectIntensity;
            mc.player.spinningEffectIntensity = intensity;
        }
    }

    private static void applyPlayerData(
            boolean receivedQuestBook,
            int homePortalX,
            int homePortalY,
            int homePortalZ,
            boolean hasSimba,
            java.util.Set<String> claimedRewards) {
        ClientWorldState.receivedQuestBook = receivedQuestBook;
        ClientWorldState.playerHomePortalX = homePortalX;
        ClientWorldState.playerHomePortalY = homePortalY;
        ClientWorldState.playerHomePortalZ = homePortalZ;
        ClientWorldState.hasSimba = hasSimba;
        ClientWorldState.claimedRewards.clear();
        ClientWorldState.claimedRewards.addAll(claimedRewards);
    }

    private ClientPayloadHandlers() {}
}
