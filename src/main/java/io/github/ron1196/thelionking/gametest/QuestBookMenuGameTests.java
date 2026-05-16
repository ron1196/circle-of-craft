package io.github.ron1196.thelionking.gametest;

import io.github.ron1196.thelionking.menu.QuestBookMenu;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Pins down the {@link QuestBookMenu} container shape and shift-click behaviour. Tracked in
 * issue #65. Uses a fake player from {@link GameTestHelper#makeMockSurvivalPlayer()} since
 * {@link net.minecraft.world.entity.player.Inventory} requires a {@code Player} which requires a
 * {@code Level}.
 */
@GameTestHolder("thelionking")
@PrefixGameTestTemplate(false)
public class QuestBookMenuGameTests {

    private static final String EMPTY = "empty";

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void menuHasExactly37Slots(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());

        if (menu.slots.size() != 37) {
            helper.fail("expected 37 slots, got " + menu.slots.size());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void slotZeroIsInfoSlotAtExpectedPosition(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());

        Slot slot0 = menu.slots.get(0);
        if (slot0.x != QuestBookMenu.INFO_SLOT_X || slot0.y != QuestBookMenu.INFO_SLOT_Y) {
            helper.fail("slot 0 at (" + slot0.x + "," + slot0.y + "), expected (" + QuestBookMenu.INFO_SLOT_X + ","
                    + QuestBookMenu.INFO_SLOT_Y + ")");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void mainInventoryRowsMapToPlayerSlots9Through35(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int menuIdx = 1 + row * 9 + col;
                Slot slot = menu.slots.get(menuIdx);
                int expectedInvIdx = 9 + row * 9 + col;
                if (slot.getContainerSlot() != expectedInvIdx) {
                    helper.fail("menu slot " + menuIdx + " points to inv " + slot.getContainerSlot() + ", expected "
                            + expectedInvIdx);
                    return;
                }
            }
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void hotbarRowMapsToPlayerSlots0Through8(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());

        for (int col = 0; col < 9; col++) {
            int menuIdx = 28 + col;
            Slot slot = menu.slots.get(menuIdx);
            if (slot.getContainerSlot() != col) {
                helper.fail("hotbar menu slot " + menuIdx + " points to inv " + slot.getContainerSlot() + ", expected "
                        + col);
                return;
            }
            if (slot.y != QuestBookMenu.HOTBAR_Y) {
                helper.fail("hotbar slot " + menuIdx + " y=" + slot.y + ", expected " + QuestBookMenu.HOTBAR_Y);
                return;
            }
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void shiftClickFromHotbarMovesItemIntoInfoSlot(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        player.getInventory().setItem(0, new ItemStack(Items.STICK));

        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());
        menu.quickMoveStack(player, 28);

        ItemStack info = menu.getInspectedStack();
        if (info.isEmpty() || !info.is(Items.STICK)) {
            helper.fail("info slot did not receive the stick after shift-click; got " + info);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void shiftClickFromInfoSlotMovesItemBackToInventory(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        QuestBookMenu menu = new QuestBookMenu(1, player.getInventory());
        menu.slots.get(0).set(new ItemStack(Items.APPLE));

        menu.quickMoveStack(player, 0);

        if (!menu.getInspectedStack().isEmpty()) {
            helper.fail("info slot still has " + menu.getInspectedStack() + " after shift-click out");
        }
        if (!player.getInventory().contains(new ItemStack(Items.APPLE))) {
            helper.fail("player inventory does not contain the apple after shift-click out");
        }
        helper.succeed();
    }
}
