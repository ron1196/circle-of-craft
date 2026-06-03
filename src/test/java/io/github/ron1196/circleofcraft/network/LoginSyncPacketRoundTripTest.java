package io.github.ron1196.circleofcraft.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.Set;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.junit.jupiter.api.Test;

/**
 * Locks the wire format of {@link LoginSyncPacket}. Encode via STREAM_CODEC, decode, assert
 * field-equality. Tracked in issue #64.
 */
class LoginSyncPacketRoundTripTest {

    @Test
    void emptyQuestsAndRewardsRoundTrips() {
        assertRoundTrip(new LoginSyncPacket(List.of(), false, 0, 0, 0, false, Set.of()));
    }

    @Test
    void singleQuestRoundTrips() {
        assertRoundTrip(new LoginSyncPacket(
                List.of(new LoginSyncPacket.QuestEntry("rafiki", "STAGE_0", true)),
                true,
                -160,
                67,
                240,
                true,
                Set.of("reward.simba")));
    }

    @Test
    void multipleQuestsWithMixedFlagsRoundTrip() {
        assertRoundTrip(new LoginSyncPacket(
                List.of(
                        new LoginSyncPacket.QuestEntry("rafiki", "STAGE_0", true),
                        new LoginSyncPacket.QuestEntry("outlands", "STAGE_1", false),
                        new LoginSyncPacket.QuestEntry("zira", "STAGE_2", true)),
                false,
                1,
                2,
                3,
                false,
                Set.of("reward.a")));
    }

    private static void assertRoundTrip(LoginSyncPacket original) {
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
        LoginSyncPacket.STREAM_CODEC.encode(buf, original);
        LoginSyncPacket decoded = LoginSyncPacket.STREAM_CODEC.decode(buf);

        assertEquals(original.questEntries().size(), decoded.questEntries().size(), "questEntries size");
        for (int i = 0; i < original.questEntries().size(); i++) {
            LoginSyncPacket.QuestEntry exp = original.questEntries().get(i);
            LoginSyncPacket.QuestEntry got = decoded.questEntries().get(i);
            assertEquals(exp.questId(), got.questId(), "questId at " + i);
            assertEquals(exp.stageId(), got.stageId(), "stageId at " + i);
            assertEquals(exp.checked(), got.checked(), "checked at " + i);
        }
        assertEquals(original.receivedQuestBook(), decoded.receivedQuestBook(), "receivedQuestBook");
        assertEquals(original.homePortalX(), decoded.homePortalX(), "homePortalX");
        assertEquals(original.homePortalY(), decoded.homePortalY(), "homePortalY");
        assertEquals(original.homePortalZ(), decoded.homePortalZ(), "homePortalZ");
        assertEquals(original.hasSimba(), decoded.hasSimba(), "hasSimba");
        assertEquals(original.claimedRewards(), decoded.claimedRewards(), "claimedRewards");
    }
}
