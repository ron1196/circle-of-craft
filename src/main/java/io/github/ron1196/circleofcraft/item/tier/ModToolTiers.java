package io.github.ron1196.circleofcraft.item.tier;

import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public final class ModToolTiers {

    public static final Tier PRIDESTONE = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            150,
            4.0F,
            1.0F,
            5,
            () -> Ingredient.of(ModItems.PRIDESTONE_BLOCK_ITEM.get()));

    public static final Tier SILVER = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 490, 6.0F, 2.0F, 16, () -> Ingredient.of(ModItems.SILVER_INGOT.get()));

    public static final Tier PEACOCK = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1475, 8.0F, 3.0F, 9, () -> Ingredient.of(ModItems.PEACOCK_GEM.get()));

    public static final Tier KIVULITE = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 100, 6.0F, 0.0F, 3, () -> Ingredient.of(ModItems.KIVULITE.get()));

    public static final Tier CORRUPT_PRIDESTONE = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            120,
            5.5F,
            0.0F,
            7,
            () -> Ingredient.of(ModItems.CORRUPT_PRIDESTONE_BLOCK_ITEM.get()));

    private ModToolTiers() {}
}
