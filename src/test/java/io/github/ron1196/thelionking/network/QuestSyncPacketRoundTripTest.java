package io.github.ron1196.thelionking.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link QuestSyncPacket}. Decode then re-encode must produce the same
 * bytes. Tracked in issue #64.
 */
class QuestSyncPacketRoundTripTest {

    @Test
    void allFieldsTrueRoundTrips() {
        assertRoundTrip("rafiki", "FIND_RAFIKI", true);
    }

    @Test
    void allFieldsFalseRoundTrips() {
        assertRoundTrip("outlands", "COLLECT_INGOTS", false);
    }

    @Test
    void emptyStringsRoundTrip() {
        assertRoundTrip("", "", false);
    }

    @Test
    void longIdentifiersRoundTrip() {
        String longId = "a".repeat(200);
        assertRoundTrip(longId, longId, true);
    }

    private static void assertRoundTrip(String questId, String stageId, boolean checked) {
        QuestSyncPacket original = new QuestSyncPacket(questId, stageId, checked);

        FriendlyByteBuf buf1 = new FriendlyByteBuf(Unpooled.buffer());
        original.encode(buf1);
        byte[] firstBytes = readAllBytes(buf1);

        FriendlyByteBuf bufForRead = new FriendlyByteBuf(Unpooled.wrappedBuffer(firstBytes));
        QuestSyncPacket decoded = new QuestSyncPacket(bufForRead);

        FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
        decoded.encode(buf2);
        byte[] secondBytes = readAllBytes(buf2);

        assertEquals(firstBytes.length, secondBytes.length, "re-encoded length differs");
        for (int i = 0; i < firstBytes.length; i++) {
            assertEquals(firstBytes[i], secondBytes[i], "byte " + i + " differs after round-trip");
        }
    }

    private static byte[] readAllBytes(FriendlyByteBuf buf) {
        byte[] out = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), out);
        return out;
    }
}
