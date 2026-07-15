package io.github.ron1196.circleofcraft.quest.stage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.ron1196.circleofcraft.quest.stage.QuestObjective.ItemRequirement;
import io.github.ron1196.circleofcraft.quest.stage.QuestObjective.Source;
import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.junit.jupiter.api.Test;

// Tag-present cases aren't asserted here: instantiating a TagKey<Item> needs a bootstrapped
// registry, which no test in this suite sets up. The guard is symmetric on null-ness, so the
// both-null and single-arg cases below still pin it against removal or an inverted condition.
class ItemRequirementValidationTest {

    @Test
    void rejectsNeitherItemNorTag() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ItemRequirement((Supplier<Item>) null, (TagKey<Item>) null, 1, Source.INVENTORY, true));
    }

    @Test
    void acceptsItemOnly() {
        assertDoesNotThrow(() -> new ItemRequirement(() -> null, 1, Source.INVENTORY));
    }
}
