package io.github.ron1196.thelionking.item;

import net.minecraft.world.item.Item;

public class NoteItem extends Item {

    private final int noteValue;

    public NoteItem(int noteValue, Properties properties) {
        super(properties);
        this.noteValue = noteValue;
    }

    public int getNoteValue() {
        return noteValue;
    }
}
