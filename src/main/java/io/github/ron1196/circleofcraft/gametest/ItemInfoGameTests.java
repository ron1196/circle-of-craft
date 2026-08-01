package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.ItemInfo;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Regression net for {@link ItemInfo} before #57 (lang-file refactor). Verifies the count of items
 * with lore, exact content for a handful of canonical entries, and a full golden snapshot of every
 * registered item's lore. Any drift after the refactor triggers a failure. Tracked in issues #57
 * (refactor target) and #65 (golden snapshot).
 *
 * <p>Why this is a Game Test, not JUnit: {@link ItemInfo#get} resolves {@link RegistryObject}s,
 * which requires the Forge registry to be populated.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class ItemInfoGameTests {

    private static final String EMPTY = "empty";

    /** Expected count of items with lore. Update only when ItemInfo intentionally adds/removes. */
    private static final int EXPECTED_LORE_ITEM_COUNT = 265;

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void loreItemCountMatchesSnapshot(GameTestHelper helper) {
        int actual = countItemsWithLore();
        if (actual != EXPECTED_LORE_ITEM_COUNT) {
            helper.fail("ItemInfo lore count drifted: expected " + EXPECTED_LORE_ITEM_COUNT + ", got " + actual
                    + ". If intentional, update EXPECTED_LORE_ITEM_COUNT.");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void lionFurLoreIsExact(GameTestHelper helper) {
        assertLoreEquals(
                helper,
                ModItems.LION_FUR,
                "Dropped by Lions and Lionesses.",
                "",
                "Used in crafting beds and various",
                "other items.");
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void hyenaBoneLoreIsExact(GameTestHelper helper) {
        assertLoreEquals(
                helper,
                ModItems.HYENA_BONE,
                "Dropped by Hyenas.",
                "",
                "Used in crafting recipes,",
                "grinding, and breeding lions.");
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiDustLoreIsExact(GameTestHelper helper) {
        assertLoreEquals(
                helper,
                ModItems.RAFIKI_DUST,
                "Obtained from Rafiki in exchange",
                "for one Ground Mango and one",
                "Ground Termite.",
                "",
                "Can be used on a Star Altar to",
                "summon Simba, or crafted with",
                "four silver ingots into an",
                "Astral Charm.");
    }

    /**
     * Golden-file snapshot: serialize every registered item's lore as
     * {@code <registry_name>\t<line1><line2>...}, sorted by registry name, and compare
     * against {@code golden/circleofcraft-iteminfo.txt} on the classpath. Drift fails the test and
     * dumps the actual output to {@code run/circleofcraft-iteminfo-actual.txt} for inspection.
     *
     * <p>Set system property {@code -Dcircleofcraft.golden.regenerate=true} to write the actual
     * snapshot to that file and fail the test with a copy-into-src message. The dev then promotes
     * the dumped file to {@code src/main/resources/golden/circleofcraft-iteminfo.txt} and commits.
     */
    @GameTest(template = EMPTY, timeoutTicks = 60)
    public void goldenSnapshotMatchesAllItems(GameTestHelper helper) {
        String actual = serializeAllLore();
        boolean regenerate = Boolean.getBoolean("circleofcraft.golden.regenerate");

        String expected = readGoldenResource();
        if (regenerate || expected == null) {
            Path dump = Path.of("run", "circleofcraft-iteminfo-actual.txt");
            try {
                Files.createDirectories(dump.getParent());
                Files.writeString(dump, actual, StandardCharsets.UTF_8);
            } catch (IOException e) {
                helper.fail("could not write regenerated golden to " + dump + ": " + e.getMessage());
                return;
            }
            helper.fail((expected == null ? "golden missing" : "regenerate requested") + " — wrote actual to "
                    + dump.toAbsolutePath()
                    + ". Move to src/main/resources/golden/circleofcraft-iteminfo.txt and commit.");
            return;
        }

        if (!actual.equals(expected)) {
            Path dump = Path.of("run", "circleofcraft-iteminfo-actual.txt");
            try {
                Files.createDirectories(dump.getParent());
                Files.writeString(dump, actual, StandardCharsets.UTF_8);
            } catch (IOException ignored) {
                // best effort
            }
            helper.fail("ItemInfo golden snapshot drifted. " + firstDiffSummary(expected, actual)
                    + " Actual dumped to " + dump.toAbsolutePath()
                    + " — if intentional, run with -Dcircleofcraft.golden.regenerate=true.");
            return;
        }

        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void everyLoreLookupReturnsConsistentResult(GameTestHelper helper) {
        // Call twice — ensures lazy init isn't returning different arrays.
        List<String> firstPass = collectAllLore();
        List<String> secondPass = collectAllLore();
        if (!firstPass.equals(secondPass)) {
            helper.fail("ItemInfo.get not deterministic across calls");
        }
        helper.succeed();
    }

    private static int countItemsWithLore() {
        int count = 0;
        for (RegistryObject<? extends Item> reg : ModItems.ITEMS.getEntries()) {
            String[] lore = ItemInfo.get(new ItemStack(reg.get()));
            if (lore != null) count++;
        }
        return count;
    }

    private static String serializeAllLore() {
        List<String> lines = new ArrayList<>();
        for (RegistryObject<? extends Item> reg : ModItems.ITEMS.getEntries()) {
            String key = ForgeRegistries.ITEMS.getKey(reg.get()).toString();
            String[] lore = ItemInfo.get(new ItemStack(reg.get()));
            String payload = lore == null ? "" : String.join("", lore);
            lines.add(key + "\t" + payload);
        }
        lines.sort(Comparator.naturalOrder());
        return String.join("\n", lines) + "\n";
    }

    private static String readGoldenResource() {
        try (InputStream in = ItemInfoGameTests.class.getResourceAsStream("/golden/circleofcraft-iteminfo.txt")) {
            if (in == null) return null;
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    private static String firstDiffSummary(String expected, String actual) {
        String[] e = expected.split("\n");
        String[] a = actual.split("\n");
        int len = Math.min(e.length, a.length);
        for (int i = 0; i < len; i++) {
            if (!e[i].equals(a[i])) {
                return "First diff at line " + (i + 1) + ": expected='" + e[i] + "' actual='" + a[i] + "'.";
            }
        }
        if (a.length != e.length) {
            return "Line count differs: expected " + e.length + ", got " + a.length + ".";
        }
        return "(trailing whitespace?)";
    }

    private static List<String> collectAllLore() {
        List<String> out = new ArrayList<>();
        for (RegistryObject<? extends Item> reg : ModItems.ITEMS.getEntries()) {
            String key = ForgeRegistries.ITEMS.getKey(reg.get()).toString();
            String[] lore = ItemInfo.get(new ItemStack(reg.get()));
            if (lore != null) {
                out.add(key + "=" + String.join("", lore));
            }
        }
        out.sort(Comparator.naturalOrder());
        return out;
    }

    private static void assertLoreEquals(
            GameTestHelper helper, RegistryObject<? extends Item> item, String... expected) {
        String[] actual = ItemInfo.get(new ItemStack(item.get()));
        if (actual == null) {
            helper.fail("ItemInfo.get returned null for " + ForgeRegistries.ITEMS.getKey(item.get()));
            return;
        }
        if (actual.length != expected.length) {
            helper.fail(
                    "lore line count for " + item.getId() + ": expected " + expected.length + ", got " + actual.length);
            return;
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                helper.fail("lore mismatch on " + item.getId() + " line " + i + ": expected '" + expected[i]
                        + "', got '" + actual[i] + "'");
                return;
            }
        }
        helper.succeed();
    }
}
