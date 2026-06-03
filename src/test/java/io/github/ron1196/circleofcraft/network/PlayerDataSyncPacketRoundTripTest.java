package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.ron1196.circleofcraft.data.PlayerData;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link PlayerDataSyncPacket}. Decode then re-encode must produce
 * identical bytes — any change to ordering or field types breaks save-to-client sync silently.
 * Tracked in issue #64.
 */
class PlayerDataSyncPacketRoundTripTest {

    @Test
    void defaultsRoundTrip() {
        assertRoundTrip(PlayerDataSyncPacket.of(new PlayerData()));
    }

    @Test
    void allBooleansTrueRoundTrips() {
        PlayerData data = new PlayerData();
        data.setReceivedQuestBook(true);
        data.setHasSimba(true);
        assertRoundTrip(PlayerDataSyncPacket.of(data));
    }

    @Test
    void homePortalCoordinatesRoundTrip() {
        PlayerData data = new PlayerData();
        data.setHomePortalX(-1234);
        data.setHomePortalY(64);
        data.setHomePortalZ(5678);
        assertRoundTrip(PlayerDataSyncPacket.of(data));
    }

    @Test
    void multipleClaimedRewardsRoundTrip() {
        PlayerData data = new PlayerData();
        data.claimReward("rafiki:bones");
        data.claimReward("outlands:first_clear");
        data.claimReward("simba:summon");
        assertRoundTrip(PlayerDataSyncPacket.of(data));
    }

    @Test
    void emptyClaimedRewardsSetRoundTrips() {
        PlayerData data = new PlayerData();
        data.setReceivedQuestBook(true);
        assertRoundTrip(PlayerDataSyncPacket.of(data));
    }

    private static void assertRoundTrip(PlayerDataSyncPacket original) {
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
        PlayerDataSyncPacket.STREAM_CODEC.encode(buf, original);
        PlayerDataSyncPacket decoded = PlayerDataSyncPacket.STREAM_CODEC.decode(buf);

        assertEquals(original.receivedQuestBook(), decoded.receivedQuestBook(), "receivedQuestBook");
        assertEquals(original.homePortalX(), decoded.homePortalX(), "homePortalX");
        assertEquals(original.homePortalY(), decoded.homePortalY(), "homePortalY");
        assertEquals(original.homePortalZ(), decoded.homePortalZ(), "homePortalZ");
        assertEquals(original.hasSimba(), decoded.hasSimba(), "hasSimba");
        assertEquals(original.claimedRewards(), decoded.claimedRewards(), "claimedRewards must be stable across round-trip");
    }
}
