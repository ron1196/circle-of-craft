package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.LKQuestState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestSyncPacket {

    private final String questId;
    private final int stage;
    private final boolean checked;

    public QuestSyncPacket(String questId, int stage, boolean checked) {
        this.questId = questId;
        this.stage = stage;
        this.checked = checked;
    }

    public QuestSyncPacket(FriendlyByteBuf buf) {
        this.questId = buf.readUtf();
        this.stage = buf.readVarInt();
        this.checked = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(questId);
        buf.writeVarInt(stage);
        buf.writeBoolean(checked);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> ClientWorldState.questStates.put(questId, new LKQuestState(stage, checked)));
        context.setPacketHandled(true);
    }
}
