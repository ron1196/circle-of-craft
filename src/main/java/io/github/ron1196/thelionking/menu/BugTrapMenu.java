package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import io.github.ron1196.thelionking.registry.LKMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BugTrapMenu extends AbstractContainerMenu {

    private final BugTrapBlockEntity blockEntity;

    // Client constructor
    public BugTrapMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, null);
    }

    public BugTrapMenu(int containerId, Inventory playerInv, BugTrapBlockEntity blockEntity) {
        super(LKMenuTypes.BUG_TRAP_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        ItemStackHandler handler = blockEntity != null ? blockEntity.getInventory() : new ItemStackHandler(5);

        // 4 bait slots (2x2 grid)
        addSlot(new SlotItemHandler(handler, 0, 40, 28));
        addSlot(new SlotItemHandler(handler, 1, 58, 28));
        addSlot(new SlotItemHandler(handler, 2, 40, 46));
        addSlot(new SlotItemHandler(handler, 3, 58, 46));

        // Output slot (read-only)
        addSlot(new SlotItemHandler(handler, 4, 109, 32) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(stack, 5, 41, true)) return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stack, 0, 4, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return blockEntity == null
                || player.distanceToSqr(
                                blockEntity.getBlockPos().getX() + 0.5,
                                blockEntity.getBlockPos().getY() + 0.5,
                                blockEntity.getBlockPos().getZ() + 0.5)
                        <= 64.0;
    }
}
