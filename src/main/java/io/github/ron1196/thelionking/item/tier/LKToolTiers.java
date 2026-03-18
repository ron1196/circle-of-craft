package io.github.ron1196.thelionking.item.tier;

import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class LKToolTiers {

    public static final ForgeTier PRIDESTONE = new ForgeTier(
            1, 150, 4.0F, 1.0F, 5, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.PRIDESTONE_ITEM.get()));

    public static final ForgeTier SILVER = new ForgeTier(
            2, 490, 6.0F, 2.0F, 16, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.SILVER_INGOT.get()));

    public static final ForgeTier PEACOCK = new ForgeTier(
            3, 1475, 8.0F, 3.0F, 9, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.PEACOCK_GEM.get()));

    public static final ForgeTier KIVULITE =
            new ForgeTier(2, 70, 6.0F, 0.0F, 3, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.KIVULITE.get()));

    public static final ForgeTier CORRUPT_PRIDESTONE = new ForgeTier(
            1,
            120,
            5.5F,
            0.0F,
            7,
            BlockTags.NEEDS_STONE_TOOL,
            () -> Ingredient.of(Items.CORRUPT_PRIDESTONE_ITEM.get()));
}
