package io.github.ron1196.thelionking.menu;

import io.github.ron1196.thelionking.block.entity.BongoDrumBlockEntity;
import io.github.ron1196.thelionking.registry.MenuTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BongoDrumMenu extends AbstractContainerMenu {

  private final BongoDrumBlockEntity blockEntity;
  // Enchant slot is a separate 1-slot handler (not stored in block entity)
  private final ItemStackHandler enchantSlot =
      new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
          return 1;
        }
      };
  public final int[] enchantLevels = new int[3];
  private final RandomSource random = RandomSource.create();

  // Client constructor
  public BongoDrumMenu(int containerId, Inventory playerInv) {
    this(containerId, playerInv, null);
  }

  public BongoDrumMenu(int containerId, Inventory playerInv, BongoDrumBlockEntity blockEntity) {
    super(MenuTypes.BONGO_DRUM_MENU.get(), containerId);
    this.blockEntity = blockEntity;

    ItemStackHandler noteHandler =
        blockEntity != null ? blockEntity.getNoteSlots() : new ItemStackHandler(8);

    // Enchant slot (center)
    addSlot(
        new SlotItemHandler(enchantSlot, 0, 43, 43) {
          @Override
          public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.isEnchantable();
          }

          @Override
          public int getMaxStackSize() {
            return 1;
          }

          @Override
          public void setChanged() {
            super.setChanged();
            slotsChanged(null);
          }
        });

    // Note slots - left column (slots 0-3)
    for (int i = 0; i < 4; i++) {
      addSlot(new SlotItemHandler(noteHandler, i, 8, 5 + i * 20));
    }

    // Note slots - right column (slots 4-7)
    for (int i = 0; i < 4; i++) {
      addSlot(new SlotItemHandler(noteHandler, i + 4, 152, 5 + i * 20));
    }

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

    recalculateEnchantments();
  }

  public void slotsChanged(net.minecraft.world.Container container) {
    recalculateEnchantments();
  }

  private void recalculateEnchantments() {
    ItemStack item = enchantSlot.getStackInSlot(0);
    int power = blockEntity != null ? blockEntity.calculateEnchantmentPower() : 0;

    if (item.isEmpty() || !item.isEnchantable() || power <= 0) {
      enchantLevels[0] = 0;
      enchantLevels[1] = 0;
      enchantLevels[2] = 0;
    } else {
      random.setSeed(System.nanoTime());
      for (int i = 0; i < 3; i++) {
        enchantLevels[i] = EnchantmentHelper.getEnchantmentCost(random, i, power, item);
        if (enchantLevels[i] < i + 1) enchantLevels[i] = 0;
      }
    }
    broadcastChanges();
  }

  public boolean clickEnchantButton(Player player, int button) {
    if (button < 0 || button > 2) return false;
    int level = enchantLevels[button];
    if (level <= 0) return false;

    ItemStack item = enchantSlot.getStackInSlot(0);
    if (item.isEmpty()) return false;

    if (!player.isCreative() && player.experienceLevel < level) return false;

    if (!player.level().isClientSide()) {
      // Apply enchantments
      var enchantments = EnchantmentHelper.selectEnchantment(random, item, level, false);
      if (!enchantments.isEmpty()) {
        if (!player.isCreative()) {
          player.giveExperienceLevels(-level);
        }
        for (var instance : enchantments) {
          item.enchant(instance.enchantment, instance.level);
        }

        // Consume notes proportionally
        consumeNotes(level);
        recalculateEnchantments();
      }
    }
    return true;
  }

  private void consumeNotes(int level) {
    if (blockEntity == null) return;
    int toConsume = level;
    ItemStackHandler noteHandler = blockEntity.getNoteSlots();
    for (int i = 0; i < noteHandler.getSlots() && toConsume > 0; i++) {
      ItemStack stack = noteHandler.getStackInSlot(i);
      if (!stack.isEmpty()) {
        int remove = Math.min(stack.getCount(), toConsume);
        stack.shrink(remove);
        toConsume -= remove;
      }
    }
    blockEntity.setChanged();
  }

  @Override
  public boolean clickMenuButton(@NotNull Player player, int id) {
    return clickEnchantButton(player, id);
  }

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
    ItemStack result = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot.hasItem()) {
      ItemStack stack = slot.getItem();
      result = stack.copy();
      if (index < 9) {
        // From container to player
        if (!this.moveItemStackTo(stack, 9, 45, true)) return ItemStack.EMPTY;
      } else {
        // From player to container — try enchant slot first, then notes
        if (!this.moveItemStackTo(stack, 0, 9, false)) return ItemStack.EMPTY;
      }
      if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
      else slot.setChanged();
    }
    return result;
  }

  @Override
  public void removed(@NotNull Player player) {
    super.removed(player);
    // Drop enchant slot item back to player
    ItemStack enchantItem = enchantSlot.getStackInSlot(0);
    if (!enchantItem.isEmpty()) {
      if (!player.isAlive()
          || (player instanceof net.minecraft.server.level.ServerPlayer sp
              && sp.hasDisconnected())) {
        player.drop(enchantItem, false);
      } else {
        player.getInventory().placeItemBackInInventory(enchantItem);
      }
    }
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
