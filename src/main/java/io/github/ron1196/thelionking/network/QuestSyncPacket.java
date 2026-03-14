package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.questline.LKQuestlineState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestSyncPacket {

    private final String questId;
    private final String stageId;
    private final boolean checked;

    public QuestSyncPacket(String questId, String stageId, boolean checked) {
        this.questId = questId;
        this.stageId = stageId;
        this.checked = checked;
    }

    public QuestSyncPacket(FriendlyByteBuf buf) {
        this.questId = buf.readUtf();
        this.stageId = buf.readUtf();
        this.checked = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(questId);
        buf.writeUtf(stageId);
        buf.writeBoolean(checked);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> ClientWorldState.questStates.put(questId, new LKQuestlineState(stageId, checked)));
        context.setPacketHandled(true);
    }
}
