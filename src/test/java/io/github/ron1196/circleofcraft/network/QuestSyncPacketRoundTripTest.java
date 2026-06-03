package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
        QuestSyncPacket.STREAM_CODEC.encode(buf, original);
        QuestSyncPacket decoded = QuestSyncPacket.STREAM_CODEC.decode(buf);

        assertEquals(original.questId(), decoded.questId(), "questId differs after round-trip");
        assertEquals(original.stageId(), decoded.stageId(), "stageId differs after round-trip");
        assertEquals(original.checked(), decoded.checked(), "checked differs after round-trip");
    }
}
