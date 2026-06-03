package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.PlayerData;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent server to client when player data changes (reward claimed, quest book received, simba
 * spawned, home portal set).
 */
public record PlayerDataSyncPacket(
        boolean receivedQuestBook,
        int homePortalX,
        int homePortalY,
        int homePortalZ,
        boolean hasSimba,
        Set<String> claimedRewards)
        implements CustomPacketPayload {

    public static final Type<PlayerDataSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "player_data_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerDataSyncPacket> STREAM_CODEC = StreamCodec.of(
            (buf, pkt) -> {
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
                boolean receivedQuestBook = buf.readBoolean();
                int homePortalX = buf.readInt();
                int homePortalY = buf.readInt();
                int homePortalZ = buf.readInt();
                boolean hasSimba = buf.readBoolean();
                int count = buf.readVarInt();
                Set<String> claimedRewards = new HashSet<>(count);
                for (int i = 0; i < count; i++) {
                    claimedRewards.add(buf.readUtf());
                }
                return new PlayerDataSyncPacket(
                        receivedQuestBook, homePortalX, homePortalY, homePortalZ, hasSimba, claimedRewards);
            });

    public static PlayerDataSyncPacket of(PlayerData playerData) {
        return new PlayerDataSyncPacket(
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
