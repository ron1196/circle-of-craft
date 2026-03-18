package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.registry.MenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SimbaInventoryMenu extends AbstractContainerMenu {

    private final ItemStackHandler simbaInventory;

    public SimbaInventoryMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(9));
    }

    public SimbaInventoryMenu(int containerId, Inventory playerInv, ItemStackHandler simbaInventory) {
        super(MenuTypes.SIMBA_INVENTORY_MENU.get(), containerId);
        this.simbaInventory = simbaInventory;

        // Simba inventory slots (9 slots in a single row)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new SlotItemHandler(simbaInventory, col, 8 + col * 18, 31));
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 9) {
                if (!this.moveItemStackTo(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 9, false)) {
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
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
