package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class Networking {

    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SimbaSitPacket.TYPE, SimbaSitPacket.STREAM_CODEC, Networking::handleSimbaSit);
        registrar.playToServer(QuestCheckPacket.TYPE, QuestCheckPacket.STREAM_CODEC, Networking::handleQuestCheck);
        registrar.playToClient(QuestSyncPacket.TYPE, QuestSyncPacket.STREAM_CODEC, Networking::handleQuestSync);
        registrar.playToClient(LoginSyncPacket.TYPE, LoginSyncPacket.STREAM_CODEC, Networking::handleLoginSync);
        registrar.playToClient(
                PlayerDataSyncPacket.TYPE, PlayerDataSyncPacket.STREAM_CODEC, Networking::handlePlayerDataSync);
        registrar.playToClient(FlatulencePacket.TYPE, FlatulencePacket.STREAM_CODEC, Networking::handleFlatulence);
        registrar.playToClient(
                PortalOverlayPacket.TYPE, PortalOverlayPacket.STREAM_CODEC, Networking::handlePortalOverlay);
    }

    private static void handleSimbaSit(final SimbaSitPacket pkt, final IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer sender)) return;
        Entity entity = sender.serverLevel().getEntity(pkt.entityId());
        if (!(entity instanceof io.github.ron1196.circleofcraft.entity.npc.SimbaEntity simba)) return;
        if (simba.isOwnedBy(sender)) {
            simba.toggleSitting(sender);
        }
    }

    private static void handleQuestCheck(final QuestCheckPacket pkt, final IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer sender)) return;
        WorldData data = WorldData.get(sender.serverLevel());
        QuestlineState state = data.getQuestManager().getState(pkt.questId());
        if (state.isChecked()) return;
        state.setChecked(true);
        data.setDirty();
        data.getQuestManager().syncToPlayer(sender);
    }

    private static void handleQuestSync(final QuestSyncPacket pkt, final IPayloadContext context) {
        context.enqueueWork(() -> ClientPayloadHandlers.handleQuestSync(pkt));
    }

    private static void handleLoginSync(final LoginSyncPacket pkt, final IPayloadContext context) {
        context.enqueueWork(() -> ClientPayloadHandlers.handleLoginSync(pkt));
    }

    private static void handlePlayerDataSync(final PlayerDataSyncPacket pkt, final IPayloadContext context) {
        context.enqueueWork(() -> ClientPayloadHandlers.handlePlayerDataSync(pkt));
    }

    private static void handleFlatulence(final FlatulencePacket pkt, final IPayloadContext context) {
        context.enqueueWork(ClientPayloadHandlers::handleFlatulence);
    }

    private static void handlePortalOverlay(final PortalOverlayPacket pkt, final IPayloadContext context) {
        context.enqueueWork(() -> ClientPayloadHandlers.handlePortalOverlay(pkt));
    }

    private Networking() {}
}
