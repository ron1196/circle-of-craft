package io.github.ron1196.thelionking.world.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

class DungeonFeaturePaletteTest {

    @Test
    void outlandsDimensionMapsToCorruptPalette() {
        assertEquals(DungeonPalette.OUTLANDS, DungeonPalette.forDimension(new ResourceLocation("thelionking", "outlands")));
    }

    @Test
    void prideLandsMapsToPridePalette() {
        assertEquals(DungeonPalette.PRIDE, DungeonPalette.forDimension(new ResourceLocation("thelionking", "pride_lands")));
    }

    @Test
    void upendiFallsBackToPridePalette() {
        // Upendi has no dungeon feature attached today, but if one is added it must not
        // silently inherit the Outlands corrupt palette.
        assertEquals(DungeonPalette.PRIDE, DungeonPalette.forDimension(new ResourceLocation("thelionking", "upendi")));
    }

    @Test
    void vanillaOverworldFallsBackToPridePalette() {
        assertEquals(DungeonPalette.PRIDE, DungeonPalette.forDimension(new ResourceLocation("minecraft", "overworld")));
    }

    @Test
    void prideAndOutlandsPalettesAreDistinct() {
        assertNotEquals(DungeonPalette.PRIDE, DungeonPalette.OUTLANDS);
    }
}
