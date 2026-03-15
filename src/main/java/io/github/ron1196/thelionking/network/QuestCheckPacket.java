package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestCheckPacket {

    private final String questId;

    public QuestCheckPacket(String questId) {
        this.questId = questId;
    }

    public QuestCheckPacket(FriendlyByteBuf buf) {
        this.questId = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(questId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            WorldData data = WorldData.get(sender.serverLevel());
            QuestlineState state = data.getQuestManager().getState(questId);
            state.setChecked(true);
            data.setDirty();
        });
        context.setPacketHandled(true);
    }
}
