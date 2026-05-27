package io.github.ron1196.circleofcraft.item.tier;

import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {

    public static final ForgeTier PRIDESTONE = new ForgeTier(
            1,
            150,
            4.0F,
            1.0F,
            5,
            BlockTags.NEEDS_STONE_TOOL,
            () -> Ingredient.of(ModItems.PRIDESTONE_BLOCK_ITEM.get()));

    public static final ForgeTier SILVER = new ForgeTier(
            2, 490, 6.0F, 2.0F, 16, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(ModItems.SILVER_INGOT.get()));

    public static final ForgeTier PEACOCK = new ForgeTier(
            3, 1475, 8.0F, 3.0F, 9, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.PEACOCK_GEM.get()));

    public static final ForgeTier KIVULITE = new ForgeTier(
            2, 100, 6.0F, 0.0F, 3, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(ModItems.KIVULITE.get()));

    public static final ForgeTier CORRUPT_PRIDESTONE = new ForgeTier(
            1,
            120,
            5.5F,
            0.0F,
            7,
            BlockTags.NEEDS_STONE_TOOL,
            () -> Ingredient.of(ModItems.CORRUPT_PRIDESTONE_BLOCK_ITEM.get()));
}
