package io.github.ron1196.thelionking.item.tier;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import io.github.ron1196.thelionking.registry.LKItems;

public class LKToolTiers {

    public static final ForgeTier PRIDESTONE = new ForgeTier(
            1, 150, 4.0F, 1.0F, 5,
            null, () -> Ingredient.of(LKItems.PRIDESTONE_ITEM.get()));

    public static final ForgeTier SILVER = new ForgeTier(
            2, 490, 6.0F, 2.0F, 16,
            null, () -> Ingredient.of(LKItems.SILVER_INGOT.get()));

    public static final ForgeTier PEACOCK = new ForgeTier(
            3, 1475, 8.0F, 3.0F, 9,
            null, () -> Ingredient.of(LKItems.PEACOCK_GEM.get()));

    public static final ForgeTier KIVULITE = new ForgeTier(
            2, 70, 6.0F, 0.0F, 3,
            null, () -> Ingredient.of(LKItems.KIVULITE.get()));

    public static final ForgeTier CORRUPT_PRIDESTONE = new ForgeTier(
            1, 120, 5.5F, 0.0F, 7,
            null, () -> Ingredient.of(LKItems.CORRUPT_PRIDESTONE_ITEM.get()));
}
