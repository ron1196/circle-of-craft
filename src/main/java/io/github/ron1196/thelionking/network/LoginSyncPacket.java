package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.LKWorldData;
import io.github.ron1196.thelionking.quest.LKQuest;
import io.github.ron1196.thelionking.quest.LKQuestRegistry;
import io.github.ron1196.thelionking.quest.LKQuestState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LoginSyncPacket {

    // World state
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean defeatedScar;
    private final int ziraStage;
    private final int pumbaaStage;
    private final boolean outlandersHostile;

    // Quest data as dynamic list
    private final List<QuestEntry> questEntries;

    private record QuestEntry(String questId, int stage, boolean checked) {}

    public LoginSyncPacket(LKWorldData data) {
        // TODO: use LKPlayerData capability for homePortal coordinates
        this.homePortalX = 0;
        this.homePortalY = 0;
        this.homePortalZ = 0;
        this.defeatedScar = data.defeatedScar;
        this.ziraStage = data.ziraStage;
        this.pumbaaStage = data.pumbaaStage;
        this.outlandersHostile = data.outlandersHostile;

        this.questEntries = new ArrayList<>();
        for (LKQuest quest : LKQuestRegistry.getOrdered()) {
            LKQuestState state = data.getQuestManager().getState(quest.getId());
            questEntries.add(new QuestEntry(quest.getId(), state.getCurrentStage(), state.isChecked()));
        }
    }

    public LoginSyncPacket(FriendlyByteBuf buf) {
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.defeatedScar = buf.readBoolean();
        this.ziraStage = buf.readVarInt();
        this.pumbaaStage = buf.readVarInt();
        this.outlandersHostile = buf.readBoolean();

        int count = buf.readVarInt();
        this.questEntries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String questId = buf.readUtf();
            int stage = buf.readVarInt();
            boolean checked = buf.readBoolean();
            questEntries.add(new QuestEntry(questId, stage, checked));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(homePortalX);
        buf.writeInt(homePortalY);
        buf.writeInt(homePortalZ);
        buf.writeBoolean(defeatedScar);
        buf.writeVarInt(ziraStage);
        buf.writeVarInt(pumbaaStage);
        buf.writeBoolean(outlandersHostile);

        buf.writeVarInt(questEntries.size());
        for (QuestEntry entry : questEntries) {
            buf.writeUtf(entry.questId());
            buf.writeVarInt(entry.stage());
            buf.writeBoolean(entry.checked());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // Apply quest state to client cache
            ClientWorldState.questStates.clear();
            for (QuestEntry entry : questEntries) {
                ClientWorldState.questStates.put(entry.questId(), new LKQuestState(entry.stage(), entry.checked()));
            }

            // Store world state on client
            ClientWorldState.homePortalX = homePortalX;
            ClientWorldState.homePortalY = homePortalY;
            ClientWorldState.homePortalZ = homePortalZ;
            ClientWorldState.defeatedScar = defeatedScar;
            ClientWorldState.ziraStage = ziraStage;
            ClientWorldState.pumbaaStage = pumbaaStage;
            ClientWorldState.outlandersHostile = outlandersHostile;
        });
        context.setPacketHandled(true);
    }
}
