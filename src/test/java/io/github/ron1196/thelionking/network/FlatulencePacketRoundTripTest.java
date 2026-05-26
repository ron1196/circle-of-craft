package io.github.ron1196.thelionking.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link FlatulencePacket}. Tracked in issue #64.
 */
class FlatulencePacketRoundTripTest {

    @Test
    void packetRoundTripsCleanly() {
        FlatulencePacket original = new FlatulencePacket();

        FriendlyByteBuf buf1 = new FriendlyByteBuf(Unpooled.buffer());
        original.encode(buf1);
        byte[] bytes1 = readAll(buf1);

        FlatulencePacket decoded = new FlatulencePacket(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes1)));

        FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
        decoded.encode(buf2);
        byte[] bytes2 = readAll(buf2);

        assertEquals(bytes1.length, bytes2.length);
        assertTrue(java.util.Arrays.equals(bytes1, bytes2));
    }

    private static byte[] readAll(FriendlyByteBuf buf) {
        byte[] out = new byte[buf.readableBytes()];
        buf.readBytes(out);
        return out;
    }
}
