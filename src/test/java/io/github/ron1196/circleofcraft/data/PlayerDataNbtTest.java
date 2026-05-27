package io.github.ron1196.circleofcraft.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

/**
 * Locks the on-disk NBT shape of {@link PlayerData} so per-player save files written by previous
 * mod versions still load identically. Pure JUnit — no Forge runtime needed because PlayerData
 * doesn't transitively load registries. Tracked in issue #64.
 */
class PlayerDataNbtTest {

    @Test
    void freshPlayerDataRoundTripsAsDefaults() {
        PlayerData reloaded = roundTrip(new PlayerData());
        assertFalse(reloaded.hasReceivedQuestBook());
        assertEquals(0, reloaded.getHomePortalX());
        assertEquals(0, reloaded.getHomePortalY());
        assertEquals(0, reloaded.getHomePortalZ());
        assertFalse(reloaded.hasSimba());
        assertFalse(reloaded.hasEnteredPrideLands());
        assertFalse(reloaded.hasEnteredOutlands());
        assertFalse(reloaded.hasEnteredUpendi());
        assertTrue(reloaded.getClaimedRewards().isEmpty());
    }

    @Test
    void everyBooleanRoundTrips() {
        PlayerData data = new PlayerData();
        data.setReceivedQuestBook(true);
        data.setHasSimba(true);
        data.setEnteredPrideLands(true);
        data.setEnteredOutlands(true);
        data.setEnteredUpendi(true);

        PlayerData reloaded = roundTrip(data);
        assertTrue(reloaded.hasReceivedQuestBook());
        assertTrue(reloaded.hasSimba());
        assertTrue(reloaded.hasEnteredPrideLands());
        assertTrue(reloaded.hasEnteredOutlands());
        assertTrue(reloaded.hasEnteredUpendi());
    }

    @Test
    void homePortalCoordinatesRoundTrip() {
        PlayerData data = new PlayerData();
        data.setHomePortalX(-1234);
        data.setHomePortalY(64);
        data.setHomePortalZ(5678);

        PlayerData reloaded = roundTrip(data);
        assertEquals(-1234, reloaded.getHomePortalX());
        assertEquals(64, reloaded.getHomePortalY());
        assertEquals(5678, reloaded.getHomePortalZ());
    }

    @Test
    void claimedRewardsSetRoundTrips() {
        PlayerData data = new PlayerData();
        data.claimReward("rafiki:hyena_bones");
        data.claimReward("outlands:first_outlander");
        data.claimReward("simba:summoned");

        PlayerData reloaded = roundTrip(data);
        assertTrue(reloaded.hasClaimedReward("rafiki:hyena_bones"));
        assertTrue(reloaded.hasClaimedReward("outlands:first_outlander"));
        assertTrue(reloaded.hasClaimedReward("simba:summoned"));
        assertEquals(3, reloaded.getClaimedRewards().size());
    }

    @Test
    void duplicateClaimsAreIdempotent() {
        PlayerData data = new PlayerData();
        data.claimReward("foo");
        data.claimReward("foo");
        data.claimReward("foo");

        PlayerData reloaded = roundTrip(data);
        assertEquals(1, reloaded.getClaimedRewards().size());
    }

    @Test
    void legacySaveWithoutFieldsLoadsAsDefaults() {
        // Saves from before any of these fields existed have no corresponding tags.
        // Loading must default rather than crashing — guards forward compatibility.
        CompoundTag legacyTag = new CompoundTag();

        PlayerData loaded = new PlayerData();
        loaded.deserializeNBT(legacyTag);

        assertFalse(loaded.hasReceivedQuestBook());
        assertEquals(0, loaded.getHomePortalX());
        assertEquals(0, loaded.getHomePortalY());
        assertEquals(0, loaded.getHomePortalZ());
        assertFalse(loaded.hasSimba());
        assertFalse(loaded.hasEnteredPrideLands());
        assertTrue(loaded.getClaimedRewards().isEmpty());
    }

    @Test
    void copyFromReplacesAllFields() {
        PlayerData source = new PlayerData();
        source.setReceivedQuestBook(true);
        source.setHomePortalX(100);
        source.setHasSimba(true);
        source.claimReward("a");
        source.claimReward("b");

        PlayerData target = new PlayerData();
        target.claimReward("ghost"); // pre-existing entry must be cleared

        target.copyFrom(source);

        assertTrue(target.hasReceivedQuestBook());
        assertEquals(100, target.getHomePortalX());
        assertTrue(target.hasSimba());
        assertEquals(2, target.getClaimedRewards().size());
        assertFalse(target.hasClaimedReward("ghost"));
    }

    private static PlayerData roundTrip(PlayerData original) {
        CompoundTag tag = original.serializeNBT();
        PlayerData reloaded = new PlayerData();
        reloaded.deserializeNBT(tag);
        return reloaded;
    }
}
