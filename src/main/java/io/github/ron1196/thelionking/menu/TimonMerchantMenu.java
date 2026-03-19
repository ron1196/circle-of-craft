package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.MenuTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class TimonMerchantMenu extends AbstractContainerMenu {

    public static final String TITLE_KEY = "container.thelionking.timon_merchant";

    public static final MenuProvider PROVIDER = new MenuProvider() {
        @Override
        public @NotNull Component getDisplayName() {
            return Component.translatable(TITLE_KEY);
        }

        @Override
        public @NotNull AbstractContainerMenu createMenu(
                int containerId, @NotNull Inventory inv, @NotNull Player player) {
            return new TimonMerchantMenu(containerId, inv);
        }
    };

    private record TradeEntry(RegistryObject<Item> item, int bugCost) {}

    // Each entry is one trade slot: the item sold and its bug cost, matching old mod
    private static final List<TradeEntry> TRADES = List.of(
            new TradeEntry(LionKingItems.TUNNAH_DIGGAH, 5),
            new TradeEntry(LionKingItems.PUMBAA_BOMB, 6),
            new TradeEntry(LionKingItems.CRYSTAL, 7),
            new TradeEntry(LionKingItems.EXPERIENCE_GRUB, 4),
            new TradeEntry(LionKingItems.AMULET, 10));

    private static final int TRADE_SLOT_X_START = 15;
    private static final int TRADE_SLOT_X_STEP = 33;
    private static final int TRADE_SLOT_Y = 32;
    private static final int PLAYER_INV_X = 8;
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    public TimonMerchantMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, buildTradeInventory());
    }

    public TimonMerchantMenu(int containerId, Inventory playerInv, ItemStackHandler tradeInventory) {
        super(MenuTypes.TIMON_MERCHANT_MENU.get(), containerId);

        // Trade slots — read-only, deduct bugs on pickup, restock automatically
        for (int i = 0; i < TRADES.size(); i++) {
            this.addSlot(new TimonTradeSlot(
                    tradeInventory,
                    i,
                    TRADE_SLOT_X_START + i * TRADE_SLOT_X_STEP,
                    TRADE_SLOT_Y,
                    TRADES.get(i).bugCost()));
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, PLAYER_INV_X + col * 18, PLAYER_INV_Y + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, PLAYER_INV_X + col * 18, HOTBAR_Y));
        }
    }

    private static ItemStackHandler buildTradeInventory() {
        ItemStackHandler handler = new ItemStackHandler(TRADES.size());
        for (int i = 0; i < TRADES.size(); i++) {
            handler.setStackInSlot(i, new ItemStack(TRADES.get(i).item().get()));
        }
        return handler;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        // Trade slots are read-only — shift-click into player inventory only
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack result = stack.copy();

        if (index < 5) {
            return ItemStack.EMPTY; // Trade slot — can't shift-click out
        } else if (index < 32) {
            if (!this.moveItemStackTo(stack, 32, this.slots.size(), false)) return ItemStack.EMPTY;
        } else {
            if (!this.moveItemStackTo(stack, 5, 32, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        return result;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
