package io.github.ron1196.thelionking.quest;

import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public record LKQuestStage(
        String objectiveText,
        List<ItemRequirement> requirements
) {
    public LKQuestStage(String objectiveText) {
        this(objectiveText, List.of());
    }

    public enum Source {
        MAIN_HAND,
        INVENTORY
    }

    public record ItemRequirement(Supplier<Item> item, int count, Source source) {
        public ItemRequirement(Supplier<Item> item, int count) {
            this(item, count, Source.MAIN_HAND);
        }
    }
}
