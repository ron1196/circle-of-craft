package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.registry.LKMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TimonMerchantMenu extends AbstractContainerMenu {

    private final ItemStackHandler merchantInventory;

    public TimonMerchantMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(5));
    }

    public TimonMerchantMenu(int containerId, Inventory playerInv, ItemStackHandler merchantInventory) {
        super(LKMenuTypes.TIMON_MERCHANT_MENU.get(), containerId);
        this.merchantInventory = merchantInventory;

        // Merchant trade slots (5 slots)
        for (int i = 0; i < 5; i++) {
            this.addSlot(new SlotItemHandler(merchantInventory, i, 44 + i * 18, 20));
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 51 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 109));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(stack, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 5, false)) {
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
    public boolean stillValid(Player player) {
        return true;
    }
}
