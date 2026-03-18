package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * A read-only trade slot in Timon's shop.
 * Picking up an item costs {@code bugCost} bugs from the player's inventory.
 * The slot immediately restocks after purchase (infinite supply).
 */
public class TimonTradeSlot extends SlotItemHandler {

    private final ItemStackHandler handler;
    private final int handlerIndex;
    private final int bugCost;

    public TimonTradeSlot(ItemStackHandler handler, int index, int x, int y, int bugCost) {
        super(handler, index, x, y);
        this.handler = handler;
        this.handlerIndex = index;
        this.bugCost = bugCost;
    }

    public int getBugCost() {
        return bugCost;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return player.getAbilities().instabuild || countBugsInInventory(player) >= bugCost;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack taken) {
        if (!player.getAbilities().instabuild) {
            deductBugs(player, bugCost);
        }
        handler.setStackInSlot(handlerIndex, new ItemStack(taken.getItem()));
    }

    private int countBugsInInventory(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(LKItems.BUG.get())) count += stack.getCount();
        }
        return count;
    }

    private void deductBugs(Player player, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(LKItems.BUG.get())) {
                int deduct = Math.min(stack.getCount(), remaining);
                stack.shrink(deduct);
                remaining -= deduct;
            }
        }
    }
}
