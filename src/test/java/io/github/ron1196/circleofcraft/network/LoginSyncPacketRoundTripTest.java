package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link LoginSyncPacket}. Builds a hand-crafted byte buffer, decodes it
 * via the byte-buffer constructor, re-encodes, and asserts byte-equality. Tracked in issue #64.
 */
class LoginSyncPacketRoundTripTest {

    @Test
    void emptyQuestsAndRewardsRoundTrips() {
        byte[] wire = wireFor(new String[0], new boolean[0], false, 0, 0, 0, false, new String[0]);
        assertRoundTrip(wire);
    }

    @Test
    void singleQuestRoundTrips() {
        byte[] wire = wireFor(
                new String[] {"rafiki"},
                new boolean[] {true},
                true,
                -160,
                67,
                240,
                true,
                new String[] {"reward.simba"});
        assertRoundTrip(wire);
    }

    @Test
    void multipleQuestsWithMixedFlagsRoundTrip() {
        byte[] wire = wireFor(
                new String[] {"rafiki", "outlands", "zira"},
                new boolean[] {true, false, true},
                false,
                1,
                2,
                3,
                false,
                new String[] {"reward.a"});
        assertRoundTrip(wire);
    }

    private static byte[] wireFor(
            String[] questIds,
            boolean[] checked,
            boolean receivedBook,
            int homeX,
            int homeY,
            int homeZ,
            boolean hasSimba,
            String[] rewards) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(questIds.length);
        for (int i = 0; i < questIds.length; i++) {
            buf.writeUtf(questIds[i]);
            buf.writeUtf("STAGE_" + i);
            buf.writeBoolean(checked[i]);
        }
        buf.writeBoolean(receivedBook);
        buf.writeInt(homeX);
        buf.writeInt(homeY);
        buf.writeInt(homeZ);
        buf.writeBoolean(hasSimba);
        buf.writeVarInt(rewards.length);
        for (String r : rewards) {
            buf.writeUtf(r);
        }

        byte[] out = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), out);
        return out;
    }

    private static void assertRoundTrip(byte[] wire) {
        FriendlyByteBuf in = new FriendlyByteBuf(Unpooled.wrappedBuffer(wire));
        LoginSyncPacket decoded = new LoginSyncPacket(in);

        FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
        decoded.encode(out);
        byte[] reEncoded = new byte[out.readableBytes()];
        out.getBytes(out.readerIndex(), reEncoded);

        assertArrayEquals(wire, reEncoded);
    }
}
