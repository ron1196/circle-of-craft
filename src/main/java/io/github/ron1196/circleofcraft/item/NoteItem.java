package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Musical note item for the bongo drum. Each note has a pitch value. Always has enchantment glint.
 */
public class NoteItem extends Item {

    private final int noteValue;

    public NoteItem(int noteValue, Properties properties) {
        super(properties);
        this.noteValue = noteValue;
    }

    public int getNoteValue() {
        return noteValue;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}
