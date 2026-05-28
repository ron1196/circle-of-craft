package io.github.ron1196.circleofcraft.compat.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * One NPC trade as JEI displays it: up to two input stacks plus one output.
 * Mirrors the hard-coded trade rules in RafikiEntity#trySideInteraction — keep in sync.
 */
public record NpcTradeRecipe(@NotNull ItemStack input1, @NotNull ItemStack input2, @NotNull ItemStack output) {

    public static NpcTradeRecipe single(ItemStack input, ItemStack output) {
        return new NpcTradeRecipe(input, ItemStack.EMPTY, output);
    }

    public List<ItemStack> inputs() {
        return input2.isEmpty() ? List.of(input1) : List.of(input1, input2);
    }
}
