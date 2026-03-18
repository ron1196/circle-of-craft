package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.registry.MenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class GrindingBowlMenu extends AbstractContainerMenu {

    private final ItemStackHandler handler;
    private final ContainerData data;

    // Client constructor
    public GrindingBowlMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(2), new SimpleContainerData(2));
    }

    // Server constructor
    public GrindingBowlMenu(int containerId, Inventory playerInv, ItemStackHandler handler, ContainerData data) {
        super(MenuTypes.GRINDING_BOWL_MENU.get(), containerId);
        this.handler = handler;
        this.data = data;

        // Input slot
        addSlot(new SlotItemHandler(handler, 0, 40, 35));

        // Output slot - no manual insertion, triggers advancement on extract
        addSlot(new SlotItemHandler(handler, 1, 116, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);
                if (player instanceof ServerPlayer serverPlayer) {
                    LionKingCriteriaTriggers.USE_GRINDING_BOWL.trigger(serverPlayer, stack);
                }
            }
        });

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    public int getGrindTime() {
        return data.get(0);
    }

    public int getMaxGrindTime() {
        return data.get(1);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();

            // From block entity slots (0-1) to player inventory (2-37)
            if (index < 2) {
                if (!moveItemStackTo(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, result);
            }
            // From player inventory to input slot only
            else {
                if (!moveItemStackTo(slotStack, 0, 1, false)) {
                    // Player inventory to hotbar or vice versa
                    if (index < 29) {
                        if (!moveItemStackTo(slotStack, 29, 38, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return result;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
