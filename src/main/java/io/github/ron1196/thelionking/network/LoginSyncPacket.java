package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LoginSyncPacket {

    // World state
    private final boolean defeatedScar;
    private final int ziraStage;
    private final int pumbaaStage;
    private final boolean outlandersHostile;

    // Quest data
    private final List<QuestEntry> questEntries;

    // Player data
    private final boolean receivedQuestBook;
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean hasSimba;
    private final Set<String> claimedRewards;

    private record QuestEntry(String questId, String stageId, boolean checked) {}

    public LoginSyncPacket(WorldData worldData, PlayerData playerData) {
        // World
        this.defeatedScar = worldData.defeatedScar;
        this.ziraStage = worldData.ziraStage;
        this.pumbaaStage = worldData.pumbaaStage;
        this.outlandersHostile = worldData.outlandersHostile;

        // Quests
        this.questEntries = new ArrayList<>();
        for (Questline quest : QuestlineRegistry.getOrdered()) {
            QuestlineState state = worldData.getQuestManager().getState(quest.getId());
            questEntries.add(new QuestEntry(quest.getId(), state.getCurrentStageId(), state.isChecked()));
        }

        // Player
        this.receivedQuestBook = playerData.hasReceivedQuestBook();
        this.homePortalX = playerData.getHomePortalX();
        this.homePortalY = playerData.getHomePortalY();
        this.homePortalZ = playerData.getHomePortalZ();
        this.hasSimba = playerData.hasSimba();
        this.claimedRewards = new HashSet<>(playerData.getClaimedRewards());
    }

    public LoginSyncPacket(FriendlyByteBuf buf) {
        // World
        this.defeatedScar = buf.readBoolean();
        this.ziraStage = buf.readVarInt();
        this.pumbaaStage = buf.readVarInt();
        this.outlandersHostile = buf.readBoolean();

        // Quests
        int questCount = buf.readVarInt();
        this.questEntries = new ArrayList<>(questCount);
        for (int i = 0; i < questCount; i++) {
            String questId = buf.readUtf();
            String stageId = buf.readUtf();
            boolean checked = buf.readBoolean();
            questEntries.add(new QuestEntry(questId, stageId, checked));
        }

        // Player
        this.receivedQuestBook = buf.readBoolean();
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.hasSimba = buf.readBoolean();

        int rewardCount = buf.readVarInt();
        this.claimedRewards = new HashSet<>(rewardCount);
        for (int i = 0; i < rewardCount; i++) {
            claimedRewards.add(buf.readUtf());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        // World
        buf.writeBoolean(defeatedScar);
        buf.writeVarInt(ziraStage);
        buf.writeVarInt(pumbaaStage);
        buf.writeBoolean(outlandersHostile);

        // Quests
        buf.writeVarInt(questEntries.size());
        for (QuestEntry entry : questEntries) {
            buf.writeUtf(entry.questId());
            buf.writeUtf(entry.stageId());
            buf.writeBoolean(entry.checked());
        }

        // Player
        buf.writeBoolean(receivedQuestBook);
        buf.writeInt(homePortalX);
        buf.writeInt(homePortalY);
        buf.writeInt(homePortalZ);
        buf.writeBoolean(hasSimba);

        buf.writeVarInt(claimedRewards.size());
        for (String reward : claimedRewards) {
            buf.writeUtf(reward);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // World state
            ClientWorldState.defeatedScar = defeatedScar;
            ClientWorldState.ziraStage = ziraStage;
            ClientWorldState.pumbaaStage = pumbaaStage;
            ClientWorldState.outlandersHostile = outlandersHostile;

            // Quest state
            ClientWorldState.questStates.clear();
            for (QuestEntry entry : questEntries) {
                ClientWorldState.questStates.put(entry.questId(), new QuestlineState(entry.stageId(), entry.checked()));
            }

            // Player data
            ClientWorldState.receivedQuestBook = receivedQuestBook;
            ClientWorldState.playerHomePortalX = homePortalX;
            ClientWorldState.playerHomePortalY = homePortalY;
            ClientWorldState.playerHomePortalZ = homePortalZ;
            ClientWorldState.hasSimba = hasSimba;
            ClientWorldState.claimedRewards.clear();
            ClientWorldState.claimedRewards.addAll(claimedRewards);
        });
        context.setPacketHandled(true);
    }
}
