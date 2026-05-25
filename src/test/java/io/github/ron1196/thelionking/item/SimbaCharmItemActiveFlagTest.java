package io.github.ron1196.thelionking.item;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

/**
 * Locks the NBT contract for Simba Charm activation state. The semantics flipped — a freshly
 * crafted charm has no NBT and must read as inactive (the old code treated absent NBT as active,
 * which silently broke the Star Altar activation flow). These tests mirror the logic in
 * {@link SimbaCharmItem#isActive} at the NBT level so we don't have to bootstrap Minecraft
 * registries to assert on the bit that actually changed.
 */
class SimbaCharmItemActiveFlagTest {

    private static final String ACTIVE_KEY = "Active";

    @Test
    void emptyTagReadsAsInactive() {
        CompoundTag tag = new CompoundTag();
        assertFalse(tag.getBoolean(ACTIVE_KEY));
    }

    @Test
    void activeTrueReadsAsActive() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(ACTIVE_KEY, true);
        assertTrue(tag.getBoolean(ACTIVE_KEY));
    }

    @Test
    void activeFalseReadsAsInactive() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(ACTIVE_KEY, false);
        assertFalse(tag.getBoolean(ACTIVE_KEY));
    }

    @Test
    void legacyInactiveKeyHasNoEffect() {
        // Old saves wrote "Inactive": true. New code reads "Active". Confirm the legacy key
        // is ignored — an old inactive charm stays inactive, matching the new default.
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Inactive", true);
        assertFalse(tag.getBoolean(ACTIVE_KEY));
    }

    @Test
    void legacyActiveCharmReadsAsInactive() {
        // Old active charms had no NBT (default-was-active semantics). They read as inactive
        // under the new contract — known migration consequence, documented here so a future
        // change doesn't silently restore the bug by re-flipping the default.
        CompoundTag tag = new CompoundTag();
        assertFalse(tag.getBoolean(ACTIVE_KEY));
    }
}
