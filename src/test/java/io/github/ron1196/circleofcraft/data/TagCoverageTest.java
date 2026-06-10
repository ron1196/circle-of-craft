package io.github.ron1196.circleofcraft.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Guards the 1.21-specific item/enchantment tags that wire mod content into vanilla systems
 * (lions/wolves eating mod meat, mod gear being enchantable). These tags do not exist pre-1.21,
 * so this file must NOT be cherry-picked to earlier MC-version branches.
 */
class TagCoverageTest {

    private static final Path ITEM_TAGS = Path.of("src/main/resources/data/minecraft/tags/item");
    private static final Path ENCH_TAGS = Path.of("src/main/resources/data/minecraft/tags/enchantment");

    private static final Pattern VALUE_PATTERN = Pattern.compile("\"([^\"]+)\"");
    private static final Pattern VALUES_BLOCK = Pattern.compile("\"values\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
    private static final Pattern RESOURCE_ID = Pattern.compile("^[a-z0-9_.-]+:[a-z0-9_./-]+$");

    private static final List<String> MOD_MEATS = List.of(
            "circleofcraft:lion_raw",
            "circleofcraft:lion_cooked",
            "circleofcraft:zebra_raw",
            "circleofcraft:zebra_cooked",
            "circleofcraft:rhino_raw",
            "circleofcraft:rhino_cooked",
            "circleofcraft:outlander_meat",
            "circleofcraft:crocodile_meat");

    private static final List<String> CUSTOM_ENCHANTS = List.of(
            "circleofcraft:scourge_of_hyenas",
            "circleofcraft:rafiki_damage",
            "circleofcraft:rafiki_durability",
            "circleofcraft:rafiki_thunder",
            "circleofcraft:biggah_diggah",
            "circleofcraft:precision");

    private static final List<String> MOD_SWORDS = List.of(
            "circleofcraft:pridestone_sword",
            "circleofcraft:silver_sword",
            "circleofcraft:peacock_sword",
            "circleofcraft:kivulite_sword",
            "circleofcraft:corrupt_sword");

    private static final List<String> MOD_MINING_TOOLS = List.of(
            "circleofcraft:pridestone_pickaxe",
            "circleofcraft:silver_pickaxe",
            "circleofcraft:peacock_pickaxe",
            "circleofcraft:kivulite_pickaxe",
            "circleofcraft:corrupt_pickaxe",
            "circleofcraft:pridestone_axe",
            "circleofcraft:silver_axe",
            "circleofcraft:peacock_axe",
            "circleofcraft:kivulite_axe",
            "circleofcraft:corrupt_axe",
            "circleofcraft:pridestone_shovel",
            "circleofcraft:silver_shovel",
            "circleofcraft:peacock_shovel",
            "circleofcraft:kivulite_shovel",
            "circleofcraft:corrupt_shovel",
            "circleofcraft:pridestone_hoe",
            "circleofcraft:silver_hoe",
            "circleofcraft:peacock_hoe",
            "circleofcraft:kivulite_hoe",
            "circleofcraft:corrupt_hoe",
            "circleofcraft:tunnah_diggah");

    private static Set<String> tagValues(Path tagFile) {
        try {
            String content = Files.readString(tagFile);
            Matcher block = VALUES_BLOCK.matcher(content);
            if (!block.find()) {
                throw new IllegalStateException("no \"values\" array in " + tagFile);
            }
            Set<String> values = new LinkedHashSet<>();
            Matcher entry = VALUE_PATTERN.matcher(block.group(1));
            while (entry.find()) {
                values.add(entry.group(1));
            }
            return values;
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + tagFile, e);
        }
    }

    private static void assertContainsAll(Path tagFile, List<String> expected) {
        Set<String> actual = tagValues(tagFile);
        for (String id : expected) {
            assertTrue(actual.contains(id), tagFile + " is missing " + id + " (has " + actual + ")");
        }
    }

    @Test
    void modMeatsAreInMeatAndWolfFoodTags() {
        assertContainsAll(ITEM_TAGS.resolve("meat.json"), MOD_MEATS);
        assertContainsAll(ITEM_TAGS.resolve("wolf_food.json"), MOD_MEATS);
    }

    @Test
    void customEnchantsAreInEnchantingTableTag() {
        assertContainsAll(ENCH_TAGS.resolve("in_enchanting_table.json"), CUSTOM_ENCHANTS);
    }

    @Test
    void modSwordsAreInSharpWeaponWeaponAndSwordTags() {
        assertContainsAll(ITEM_TAGS.resolve("enchantable/sharp_weapon.json"), MOD_SWORDS);
        assertContainsAll(ITEM_TAGS.resolve("enchantable/weapon.json"), MOD_SWORDS);
        assertContainsAll(ITEM_TAGS.resolve("enchantable/sword.json"), MOD_SWORDS);
    }

    @Test
    void modMiningToolsAreInEnchantableMiningTag() {
        assertContainsAll(ITEM_TAGS.resolve("enchantable/mining.json"), MOD_MINING_TOOLS);
    }

    @Test
    void everyTagValueIsAWellFormedResourceId() {
        List<Path> guarded = List.of(
                ITEM_TAGS.resolve("meat.json"),
                ITEM_TAGS.resolve("wolf_food.json"),
                ITEM_TAGS.resolve("enchantable/sharp_weapon.json"),
                ITEM_TAGS.resolve("enchantable/weapon.json"),
                ITEM_TAGS.resolve("enchantable/sword.json"),
                ITEM_TAGS.resolve("enchantable/mining.json"));
        for (Path tag : guarded) {
            for (String id : tagValues(tag)) {
                assertTrue(RESOURCE_ID.matcher(id).matches(), tag + " has malformed entry: '" + id + "'");
            }
        }
    }
}
