package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.LKPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Sent server to client when player data changes (reward claimed, quest book received,
 * simba spawned, home portal set).
 */
public class PlayerDataSyncPacket {

    private final boolean receivedQuestBook;
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean hasSimba;
    private final Set<String> claimedRewards;

    public PlayerDataSyncPacket(LKPlayerData playerData) {
        this.receivedQuestBook = playerData.hasReceivedQuestBook();
        this.homePortalX = playerData.getHomePortalX();
        this.homePortalY = playerData.getHomePortalY();
        this.homePortalZ = playerData.getHomePortalZ();
        this.hasSimba = playerData.hasSimba();
        this.claimedRewards = new HashSet<>(playerData.getClaimedRewards());
    }

    public PlayerDataSyncPacket(FriendlyByteBuf buf) {
        this.receivedQuestBook = buf.readBoolean();
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.hasSimba = buf.readBoolean();

        int count = buf.readVarInt();
        this.claimedRewards = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            claimedRewards.add(buf.readUtf());
        }
    }

    public void encode(FriendlyByteBuf buf) {
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
