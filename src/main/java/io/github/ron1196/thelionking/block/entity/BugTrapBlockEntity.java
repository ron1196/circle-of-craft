package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.menu.BugTrapMenu;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BugTrapBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler items = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 4) return false; // output slot
            return stack.getItem().isEdible();
        }
    };

    private int trapTimer = 0;
    private static final int TRAP_INTERVAL = 600; // 30 seconds

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? trapTimer : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) trapTimer = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public BugTrapBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.BUG_TRAP.get(), pos, state);
    }

    public void serverTick() {
        if (level == null || level.isClientSide()) return;

        boolean hasBait = false;
        for (int i = 0; i < 4; i++) {
            if (!items.getStackInSlot(i).isEmpty()) {
                hasBait = true;
                break;
            }
        }
        if (!hasBait) return;

        trapTimer++;
        if (trapTimer >= 5) {
            trapTimer = 0;

            ItemStack output = items.getStackInSlot(4);
            if (output.isEmpty() || (output.is(Items.BUG.get()) && output.getCount() < output.getMaxStackSize())) {
                float chance = 0.0F;
                int baitCount = 0;
                for (int i = 0; i < 4; i++) {
                    if (!items.getStackInSlot(i).isEmpty()) baitCount++;
                }
                chance = 0.25F * baitCount; // 25% per bait slot filled

                if (level.random.nextFloat() < chance) {
                    // Consume one bait item from a random filled slot
                    int slot = -1;
                    for (int attempts = 0; attempts < 10; attempts++) {
                        int s = level.random.nextInt(4);
                        if (!items.getStackInSlot(s).isEmpty()) {
                            slot = s;
                            break;
                        }
                    }
                    if (slot >= 0) {
                        items.getStackInSlot(slot).shrink(1);

                        // Add bug to output
                        if (output.isEmpty()) {
                            items.setStackInSlot(4, new ItemStack(Items.BUG.get()));
                        } else {
                            output.grow(1);
                        }
                        setChanged();
                    }
                }
            }
        }
    }

    public ItemStackHandler getInventory() {
        return items;
    }

    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (!stack.isEmpty()) drops.add(stack);
        }
        return drops;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.putInt("TrapTimer", trapTimer);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("Items"));
        trapTimer = tag.getInt("TrapTimer");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.thelionking.bug_trap");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInv, @NotNull Player player) {
        return new BugTrapMenu(containerId, playerInv, this);
    }
}
