package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.quest.LKQuests;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from client to server when the client acknowledges a quest stage check.
 */
public class QuestCheckPacket {

    private final int questIndex;

    public QuestCheckPacket(int questIndex) {
        this.questIndex = questIndex;
    }

    public QuestCheckPacket(FriendlyByteBuf buf) {
        this.questIndex = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(questIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            if (questIndex < 0 || questIndex >= LKQuests.ALL_QUESTS.length) return;

            LKQuestBase quest = LKQuests.ALL_QUESTS[questIndex];
            if (quest == null) return;

            quest.setChecked(true);

            // Persist the change
            LKLevelData data = LKLevelData.get(sender.serverLevel());
            data.setDirty();
        });
        context.setPacketHandled(true);
    }
}
