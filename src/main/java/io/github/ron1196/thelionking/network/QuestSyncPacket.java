package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class QuestSyncPacket {

    private final String questId;
    private final String stageId;
    private final boolean checked;
    private final boolean delayed;

    public QuestSyncPacket(String questId, String stageId, boolean checked, boolean delayed) {
        this.questId = questId;
        this.stageId = stageId;
        this.checked = checked;
        this.delayed = delayed;
    }

    public QuestSyncPacket(FriendlyByteBuf buf) {
        this.questId = buf.readUtf();
        this.stageId = buf.readUtf();
        this.checked = buf.readBoolean();
        this.delayed = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(questId);
        buf.writeUtf(stageId);
        buf.writeBoolean(checked);
        buf.writeBoolean(delayed);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(
                () -> ClientWorldState.questStates.put(questId, new QuestlineState(stageId, checked, delayed)));
        context.setPacketHandled(true);
    }
}
