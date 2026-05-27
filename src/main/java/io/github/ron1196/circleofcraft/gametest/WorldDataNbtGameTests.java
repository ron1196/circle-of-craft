package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.WorldData;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Locks the on-disk NBT shape of {@link WorldData} so save files written by previous mod versions
 * keep loading identically. Lives under gametest/ because constructing {@code WorldData} triggers
 * {@code QuestlineRegistry} static init, which pulls in {@code ModItems} — Forge registries
 * are only available in the Game Test runtime, not in plain JUnit.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class WorldDataNbtGameTests {

    private static final String EMPTY = "empty";

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void freshWorldDataRoundTripsAsZeros(GameTestHelper helper) {
        WorldData reloaded = roundTrip(new WorldData());
        expect(helper, 0, reloaded.getZiraTreeTalkCount(), "ZiraTreeTalkCount");
        expect(helper, 0, reloaded.getPumbaaTalkCount(), "PumbaaTalkCount");
        expect(helper, 0, reloaded.getTimonRafikiTalkCount(), "TimonRafikiTalkCount");
        expect(helper, 0, reloaded.getRafikiCeremonyTick(), "RafikiCeremonyTick");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void ziraTreeTalkCountRoundTrips(GameTestHelper helper) {
        WorldData data = new WorldData();
        data.incrementZiraTreeTalkCount();
        data.incrementZiraTreeTalkCount();
        data.incrementZiraTreeTalkCount();
        expect(helper, 3, roundTrip(data).getZiraTreeTalkCount(), "ZiraTreeTalkCount");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void pumbaaTalkCountRoundTrips(GameTestHelper helper) {
        WorldData data = new WorldData();
        data.incrementPumbaaTalkCount();
        data.incrementPumbaaTalkCount();
        expect(helper, 2, roundTrip(data).getPumbaaTalkCount(), "PumbaaTalkCount");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void timonRafikiTalkCountRoundTrips(GameTestHelper helper) {
        WorldData data = new WorldData();
        data.incrementTimonRafikiTalkCount();
        data.incrementTimonRafikiTalkCount();
        data.incrementTimonRafikiTalkCount();
        data.incrementTimonRafikiTalkCount();
        expect(helper, 4, roundTrip(data).getTimonRafikiTalkCount(), "TimonRafikiTalkCount");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiCeremonyTickRoundTrips(GameTestHelper helper) {
        WorldData data = new WorldData();
        data.setRafikiCeremonyTick(120);
        expect(helper, 120, roundTrip(data).getRafikiCeremonyTick(), "RafikiCeremonyTick");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void resetMethodsClearFields(GameTestHelper helper) {
        WorldData data = new WorldData();
        data.incrementZiraTreeTalkCount();
        data.incrementPumbaaTalkCount();
        data.incrementTimonRafikiTalkCount();
        data.resetZiraTreeTalkCount();
        data.resetPumbaaTalkCount();
        data.resetTimonRafikiTalkCount();

        WorldData reloaded = roundTrip(data);
        expect(helper, 0, reloaded.getZiraTreeTalkCount(), "ZiraTreeTalkCount after reset");
        expect(helper, 0, reloaded.getPumbaaTalkCount(), "PumbaaTalkCount after reset");
        expect(helper, 0, reloaded.getTimonRafikiTalkCount(), "TimonRafikiTalkCount after reset");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void legacySaveWithoutCounterTagsDefaultsToZero(GameTestHelper helper) {
        // Saves from before the talk-count fields existed have no corresponding tags. Loading must
        // default to zero rather than crashing — guards forward compatibility.
        WorldData loaded = WorldData.load(new CompoundTag());
        expect(helper, 0, loaded.getZiraTreeTalkCount(), "ZiraTreeTalkCount on legacy save");
        expect(helper, 0, loaded.getPumbaaTalkCount(), "PumbaaTalkCount on legacy save");
        expect(helper, 0, loaded.getTimonRafikiTalkCount(), "TimonRafikiTalkCount on legacy save");
        expect(helper, 0, loaded.getRafikiCeremonyTick(), "RafikiCeremonyTick on legacy save");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void allFieldsRoundTripSimultaneously(GameTestHelper helper) {
        WorldData data = new WorldData();
        for (int i = 0; i < 5; i++) data.incrementZiraTreeTalkCount();
        for (int i = 0; i < 7; i++) data.incrementPumbaaTalkCount();
        for (int i = 0; i < 2; i++) data.incrementTimonRafikiTalkCount();
        data.setRafikiCeremonyTick(99);

        WorldData reloaded = roundTrip(data);
        expect(helper, 5, reloaded.getZiraTreeTalkCount(), "ZiraTreeTalkCount");
        expect(helper, 7, reloaded.getPumbaaTalkCount(), "PumbaaTalkCount");
        expect(helper, 2, reloaded.getTimonRafikiTalkCount(), "TimonRafikiTalkCount");
        expect(helper, 99, reloaded.getRafikiCeremonyTick(), "RafikiCeremonyTick");
        helper.succeed();
    }

    private static WorldData roundTrip(WorldData data) {
        CompoundTag tag = new CompoundTag();
        data.save(tag);
        return WorldData.load(tag);
    }

    private static void expect(GameTestHelper helper, int expected, int actual, String field) {
        if (expected != actual) {
            helper.fail(field + " expected " + expected + " but was " + actual);
        }
    }
}
