package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.item.NoteItem;
import io.github.ron1196.circleofcraft.menu.BongoDrumMenu;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class BongoDrumBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler noteSlots = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof NoteItem;
        }
    };

    private int note = 0;

    public BongoDrumBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.BONGO_DRUM.get(), pos, state);
    }

    public void cycleNote() {
        note = (note + 1) % 25;
        setChanged();
    }

    public int getNote() {
        return note;
    }

    public ItemStackHandler getNoteSlots() {
        return noteSlots;
    }

    public int calculateEnchantmentPower() {
        int totalNoteValue = 0;
        for (int i = 0; i < noteSlots.getSlots(); i++) {
            ItemStack stack = noteSlots.getStackInSlot(i);
            if (stack.getItem() instanceof NoteItem noteItem) {
                totalNoteValue += stack.getCount() * noteItem.getNoteValue();
            }
        }
        return totalNoteValue / 7;
    }

    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < noteSlots.getSlots(); i++) {
            ItemStack stack = noteSlots.getStackInSlot(i);
            if (!stack.isEmpty()) drops.add(stack);
        }
        return drops;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("NoteSlots", noteSlots.serializeNBT());
        tag.putInt("Note", note);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        noteSlots.deserializeNBT(tag.getCompound("NoteSlots"));
        note = tag.getInt("Note");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.circleofcraft.bongo_drum");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
        return new BongoDrumMenu(containerId, playerInv, this);
    }
}
