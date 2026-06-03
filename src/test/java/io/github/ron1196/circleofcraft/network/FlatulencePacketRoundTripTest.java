package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link FlatulencePacket}. Tracked in issue #64.
 */
class FlatulencePacketRoundTripTest {

    @Test
    void packetRoundTripsCleanly() {
        FlatulencePacket original = FlatulencePacket.INSTANCE;

        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
        FlatulencePacket.STREAM_CODEC.encode(buf, original);
        FlatulencePacket decoded = FlatulencePacket.STREAM_CODEC.decode(buf);

        assertEquals(original, decoded);
    }
}
