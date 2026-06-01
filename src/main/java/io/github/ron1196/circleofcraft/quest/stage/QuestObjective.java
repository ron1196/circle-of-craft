package io.github.ron1196.circleofcraft.quest.stage;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public record QuestObjective(String objectiveText, List<ItemRequirement> requirements) {
    public QuestObjective(String objectiveText) {
        this(objectiveText, List.of());
    }

    public enum Source {
        MAIN_HAND,
        INVENTORY
    }

    public record ItemRequirement(Supplier<Item> item, int count, Source source, boolean consume) {
        public ItemRequirement(Supplier<Item> item, int count) {
            this(item, count, Source.MAIN_HAND, true);
        }

        public ItemRequirement(Supplier<Item> item, int count, Source source) {
            this(item, count, source, true);
        }
    }
}
