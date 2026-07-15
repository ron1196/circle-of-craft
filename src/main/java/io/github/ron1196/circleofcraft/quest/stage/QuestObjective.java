package io.github.ron1196.circleofcraft.quest.stage;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record QuestObjective(String objectiveText, List<ItemRequirement> requirements) {
    public QuestObjective(String objectiveText) {
        this(objectiveText, List.of());
    }

    public enum Source {
        MAIN_HAND,
        INVENTORY
    }

    public record ItemRequirement(Supplier<Item> item, TagKey<Item> tag, int count, Source source, boolean consume) {
        public ItemRequirement {
            if ((item == null) == (tag == null)) {
                throw new IllegalArgumentException("ItemRequirement needs exactly one of item or tag");
            }
        }

        public ItemRequirement(Supplier<Item> item, int count) {
            this(item, null, count, Source.MAIN_HAND, true);
        }

        public ItemRequirement(Supplier<Item> item, int count, Source source) {
            this(item, null, count, source, true);
        }

        public ItemRequirement(Supplier<Item> item, int count, Source source, boolean consume) {
            this(item, null, count, source, consume);
        }

        public ItemRequirement(TagKey<Item> tag, int count, Source source) {
            this(null, tag, count, source, true);
        }

        public boolean matches(ItemStack stack) {
            return tag != null ? stack.is(tag) : stack.is(item.get());
        }
    }
}
