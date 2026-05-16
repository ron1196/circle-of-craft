package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.registry.MenuTypes;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QuestBookMenu extends AbstractContainerMenu {

    public static final int INFO_SLOT_X = 112;
    public static final int INFO_SLOT_Y = 104;

    public static final int INV_X = 112;
    public static final int INV_Y = 139;
    public static final int HOTBAR_Y = 197;

    private final SimpleContainer infoSlot;

    public QuestBookMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(1));
    }

    public QuestBookMenu(int containerId, Inventory playerInv, SimpleContainer infoSlot) {
        super(MenuTypes.QUEST_BOOK_MENU.get(), containerId);
        this.infoSlot = infoSlot;

        addSlot(new Slot(infoSlot, 0, INFO_SLOT_X, INFO_SLOT_Y));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIdx = 9 + row * 9 + col;
                addSlot(new Slot(playerInv, slotIdx, INV_X + col * 18, INV_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, INV_X + col * 18, HOTBAR_Y));
        }
    }

    public ItemStack getInspectedStack() {
        return infoSlot.getItem(0);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index == 0) {
                if (!moveItemStackTo(stack, 1, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        clearContainer(player, infoSlot);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
