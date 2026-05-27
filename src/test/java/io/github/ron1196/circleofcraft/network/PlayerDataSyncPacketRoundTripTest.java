package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ron1196.circleofcraft.data.PlayerData;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link PlayerDataSyncPacket}. Decode then re-encode must produce
 * identical bytes — any change to ordering or field types breaks save-to-client sync silently.
 * Tracked in issue #64.
 */
class PlayerDataSyncPacketRoundTripTest {

    @Test
    void defaultsRoundTrip() {
        assertRoundTrip(new PlayerData());
    }

    @Test
    void allBooleansTrueRoundTrips() {
        PlayerData data = new PlayerData();
        data.setReceivedQuestBook(true);
        data.setHasSimba(true);
        assertRoundTrip(data);
    }

    @Test
    void homePortalCoordinatesRoundTrip() {
        PlayerData data = new PlayerData();
        data.setHomePortalX(-1234);
        data.setHomePortalY(64);
        data.setHomePortalZ(5678);
        assertRoundTrip(data);
    }

    @Test
    void multipleClaimedRewardsRoundTrip() {
        PlayerData data = new PlayerData();
        data.claimReward("rafiki:bones");
        data.claimReward("outlands:first_clear");
        data.claimReward("simba:summon");
        assertRoundTrip(data);
    }

    @Test
    void emptyClaimedRewardsSetRoundTrips() {
        // Empty set must still serialize cleanly — common case on a fresh player.
        PlayerData data = new PlayerData();
        data.setReceivedQuestBook(true);
        assertRoundTrip(data);
    }

    private static void assertRoundTrip(PlayerData data) {
        PlayerDataSyncPacket original = new PlayerDataSyncPacket(data);

        FriendlyByteBuf buf1 = new FriendlyByteBuf(Unpooled.buffer());
        original.encode(buf1);
        byte[] bytes1 = readAll(buf1);

        PlayerDataSyncPacket decoded = new PlayerDataSyncPacket(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes1)));

        FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
        decoded.encode(buf2);
        byte[] bytes2 = readAll(buf2);

        assertEquals(bytes1.length, bytes2.length, "wire size must be stable across round-trip");
        assertTrue(java.util.Arrays.equals(bytes1, bytes2), "wire bytes must be identical across round-trip");
    }

    private static byte[] readAll(FriendlyByteBuf buf) {
        byte[] out = new byte[buf.readableBytes()];
        buf.readBytes(out);
        return out;
    }
}
