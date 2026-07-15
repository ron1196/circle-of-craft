package io.github.ron1196.circleofcraft.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The Pumbaa Box quest accepts any {@code #minecraft:planks} item (see OutlandsQuestline /
 * PumbaaEntity). That only works if the mod's own planks are actually in the planks item tag —
 * drop them here and the quest silently regresses to vanilla-only planks (the original bug report).
 */
class PlanksTagCoverageTest {

    private static final List<String> MOD_PLANKS = List.of(
            "circleofcraft:pride_acacia_planks",
            "circleofcraft:rainforest_planks",
            "circleofcraft:mango_planks",
            "circleofcraft:passion_planks",
            "circleofcraft:banana_planks",
            "circleofcraft:deadwood_planks");

    @Test
    void modPlanksAreInVanillaPlanksItemTag() {
        // 1.21 uses tags/item/, 1.20.1 uses tags/items/ — accept whichever exists so this ports cleanly.
        Path tag = Path.of("src/main/resources/data/minecraft/tags/item/planks.json");
        if (!Files.exists(tag)) {
            tag = Path.of("src/main/resources/data/minecraft/tags/items/planks.json");
        }
        String content = read(tag);
        for (String id : MOD_PLANKS) {
            assertTrue(content.contains(id), tag + " is missing " + id);
        }
    }

    private static String read(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + file, e);
        }
    }
}
