package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.PlayerData;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.quest.questline.Questline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LoginSyncPacket(
        List<QuestEntry> questEntries,
        boolean receivedQuestBook,
        int homePortalX,
        int homePortalY,
        int homePortalZ,
        boolean hasSimba,
        Set<String> claimedRewards)
        implements CustomPacketPayload {

    public record QuestEntry(String questId, String stageId, boolean checked) {}

    public static final Type<LoginSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "login_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LoginSyncPacket> STREAM_CODEC = StreamCodec.of(
            (buf, pkt) -> {
                buf.writeVarInt(pkt.questEntries.size());
                for (QuestEntry entry : pkt.questEntries) {
                    buf.writeUtf(entry.questId());
                    buf.writeUtf(entry.stageId());
                    buf.writeBoolean(entry.checked());
                }
                buf.writeBoolean(pkt.receivedQuestBook);
                buf.writeInt(pkt.homePortalX);
                buf.writeInt(pkt.homePortalY);
                buf.writeInt(pkt.homePortalZ);
                buf.writeBoolean(pkt.hasSimba);
                buf.writeVarInt(pkt.claimedRewards.size());
                for (String reward : pkt.claimedRewards) {
                    buf.writeUtf(reward);
                }
            },
            buf -> {
                int questCount = buf.readVarInt();
                List<QuestEntry> entries = new ArrayList<>(questCount);
                for (int i = 0; i < questCount; i++) {
                    String questId = buf.readUtf();
                    String stageId = buf.readUtf();
                    boolean checked = buf.readBoolean();
                    entries.add(new QuestEntry(questId, stageId, checked));
                }
                boolean receivedQuestBook = buf.readBoolean();
                int homePortalX = buf.readInt();
                int homePortalY = buf.readInt();
                int homePortalZ = buf.readInt();
                boolean hasSimba = buf.readBoolean();
                int rewardCount = buf.readVarInt();
                Set<String> claimedRewards = new HashSet<>(rewardCount);
                for (int i = 0; i < rewardCount; i++) {
                    claimedRewards.add(buf.readUtf());
                }
                return new LoginSyncPacket(
                        entries, receivedQuestBook, homePortalX, homePortalY, homePortalZ, hasSimba, claimedRewards);
            });

    public static LoginSyncPacket of(WorldData worldData, PlayerData playerData) {
        List<QuestEntry> entries = new ArrayList<>();
        for (Questline quest : QuestlineRegistry.getOrdered()) {
            QuestlineState state = worldData.getQuestManager().getState(quest.getId());
            entries.add(new QuestEntry(quest.getId(), state.getCurrentStageId(), state.isChecked()));
        }
        return new LoginSyncPacket(
                entries,
                playerData.hasReceivedQuestBook(),
                playerData.getHomePortalX(),
                playerData.getHomePortalY(),
                playerData.getHomePortalZ(),
                playerData.hasSimba(),
                new HashSet<>(playerData.getClaimedRewards()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
