package io.github.ron1196.thelionking.quest.questline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

/**
 * Locks the on-disk NBT shape of {@link QuestlineState} so save files written by previous mod
 * versions still load identically. Tracked in issue #64.
 */
class QuestlineStateNbtTest {

    @Test
    void emptyStateRoundTrips() {
        assertRoundTrip("", false, false);
    }

    @Test
    void allFieldsTrueRoundTrips() {
        assertRoundTrip("FIND_RAFIKI", true, true);
    }

    @Test
    void onlyCheckedRoundTrips() {
        assertRoundTrip("COLLECT_BONES", true, false);
    }

    @Test
    void onlyDelayedRoundTrips() {
        assertRoundTrip("RETURN_AFTER_SCAR", false, true);
    }

    @Test
    void missingDelayedTagDefaultsToFalse() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Stage", "FIND_RAFIKI");
        tag.putBoolean("Checked", true);

        QuestlineState loaded = QuestlineState.readFromNBT(tag);

        assertEquals("FIND_RAFIKI", loaded.getCurrentStageId());
        assertEquals(true, loaded.isChecked());
        assertEquals(false, loaded.isDelayed());
    }

    @Test
    void missingStageTagDefaultsToEmpty() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Checked", false);

        QuestlineState loaded = QuestlineState.readFromNBT(tag);

        assertEquals("", loaded.getCurrentStageId());
    }

    private static void assertRoundTrip(String stageId, boolean checked, boolean delayed) {
        QuestlineState original = new QuestlineState(stageId, checked, delayed);

        CompoundTag tag = new CompoundTag();
        original.writeToNBT(tag);
        QuestlineState reloaded = QuestlineState.readFromNBT(tag);

        assertEquals(stageId, reloaded.getCurrentStageId());
        assertEquals(checked, reloaded.isChecked());
        assertEquals(delayed, reloaded.isDelayed());
    }
}
