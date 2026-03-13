package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.quest.LKQuests;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from server to client to synchronize quest state.
 */
public class QuestSyncPacket {

    private final int questIndex;
    private final int stage;
    private final int checked;

    public QuestSyncPacket(int questIndex, int stage, int checked) {
        this.questIndex = questIndex;
        this.stage = stage;
        this.checked = checked;
    }

    public QuestSyncPacket(FriendlyByteBuf buf) {
        this.questIndex = buf.readVarInt();
        this.stage = buf.readVarInt();
        this.checked = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(questIndex);
        buf.writeVarInt(stage);
        buf.writeVarInt(checked);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (questIndex < 0 || questIndex >= LKQuests.ALL_QUESTS.length) return;

            LKQuestBase quest = LKQuests.ALL_QUESTS[questIndex];
            if (quest == null) return;

            quest.currentStage = stage;
            quest.checked = checked;
        });
        context.setPacketHandled(true);
    }
}
