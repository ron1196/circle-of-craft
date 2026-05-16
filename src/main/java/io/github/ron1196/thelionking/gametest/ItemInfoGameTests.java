package io.github.ron1196.thelionking.gametest;

import io.github.ron1196.thelionking.data.ItemInfo;
import io.github.ron1196.thelionking.registry.LionKingItems;
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
 * with lore and exact content for a handful of canonical entries — any drift after the refactor
 * triggers a failure. Tracked in issue #63.
 *
 * <p>Why this is a Game Test, not JUnit: {@link ItemInfo#get} resolves {@link RegistryObject}s,
 * which requires the Forge registry to be populated.
 */
@GameTestHolder("thelionking")
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
                LionKingItems.LION_FUR,
                "Dropped by Lions and Lionesses.",
                "",
                "Used in crafting beds and various",
                "other items.");
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void hyenaBoneLoreIsExact(GameTestHelper helper) {
        assertLoreEquals(
                helper,
                LionKingItems.HYENA_BONE,
                "Dropped by Hyenas.",
                "",
                "Used in crafting recipes,",
                "grinding, and breeding lions.");
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiDustLoreIsExact(GameTestHelper helper) {
        assertLoreEquals(
                helper,
                LionKingItems.RAFIKI_DUST,
                "Obtained from Rafiki in exchange",
                "for one Ground Mango and one",
                "Ground Termite.",
                "",
                "Can be used on a Star Altar to",
                "summon Simba, or crafted with",
                "four silver ingots into an",
                "Astral Charm.");
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
        for (RegistryObject<? extends Item> reg : LionKingItems.ITEMS.getEntries()) {
            String[] lore = ItemInfo.get(new ItemStack(reg.get()));
            if (lore != null) count++;
        }
        return count;
    }

    private static List<String> collectAllLore() {
        List<String> out = new ArrayList<>();
        for (RegistryObject<? extends Item> reg : LionKingItems.ITEMS.getEntries()) {
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
