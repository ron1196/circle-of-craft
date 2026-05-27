package io.github.ron1196.circleofcraft.world.feature;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Brick palette used by {@link DungeonFeature} per dimension. Outlands has no
 * corrupt-cracked variant, so the wall accent slot falls back to the wall primary
 * there (the 10% cracked-chance becomes a no-op).
 *
 * <p>Suppliers are lambdas — not {@code RegistryObject} references — so enum init
 * does not load {@code ModBlocks}. This keeps the type usable in pure-Java
 * unit tests without a Forge bootstrap.
 */
public enum DungeonPalette {
    PRIDE(
            () -> ModBlocks.MOSSY_PRIDE_BRICK.get(),
            () -> ModBlocks.PRIDE_BRICK.get(),
            () -> ModBlocks.PRIDE_BRICK.get(),
            () -> ModBlocks.CRACKED_PRIDE_BRICK.get(),
            () -> ModBlocks.PRIDE_PILLAR.get()),
    OUTLANDS(
            () -> ModBlocks.MOSSY_CORRUPT_PRIDE_BRICK.get(),
            () -> ModBlocks.CORRUPT_PRIDE_BRICK.get(),
            () -> ModBlocks.CORRUPT_PRIDE_BRICK.get(),
            () -> ModBlocks.CORRUPT_PRIDE_BRICK.get(),
            () -> ModBlocks.CORRUPT_PRIDE_PILLAR.get());

    private static final ResourceLocation OUTLANDS_DIMENSION_ID = CircleOfCraftMod.id("outlands");

    final Supplier<? extends Block> floorPrimary;
    final Supplier<? extends Block> floorAccent;
    final Supplier<? extends Block> wallPrimary;
    final Supplier<? extends Block> wallAccent;
    final Supplier<? extends Block> pillar;

    DungeonPalette(
            Supplier<? extends Block> floorPrimary,
            Supplier<? extends Block> floorAccent,
            Supplier<? extends Block> wallPrimary,
            Supplier<? extends Block> wallAccent,
            Supplier<? extends Block> pillar) {
        this.floorPrimary = floorPrimary;
        this.floorAccent = floorAccent;
        this.wallPrimary = wallPrimary;
        this.wallAccent = wallAccent;
        this.pillar = pillar;
    }

    public static DungeonPalette forDimension(ResourceLocation dimensionId) {
        return OUTLANDS_DIMENSION_ID.equals(dimensionId) ? OUTLANDS : PRIDE;
    }
}
