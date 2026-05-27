package io.github.ron1196.circleofcraft.world.dimension;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class Dimensions {

    public static final String PRIDE_LANDS_PATH = "pride_lands";
    public static final String OUTLANDS_PATH = "outlands";
    public static final String UPENDI_PATH = "upendi";

    public static final ResourceKey<Level> PRIDE_LANDS_LEVEL = levelKey(PRIDE_LANDS_PATH);
    public static final ResourceKey<Level> OUTLANDS_LEVEL = levelKey(OUTLANDS_PATH);
    public static final ResourceKey<Level> UPENDI_LEVEL = levelKey(UPENDI_PATH);

    public static final ResourceKey<DimensionType> PRIDE_LANDS_TYPE = typeKey(PRIDE_LANDS_PATH);
    public static final ResourceKey<DimensionType> OUTLANDS_TYPE = typeKey(OUTLANDS_PATH);
    public static final ResourceKey<DimensionType> UPENDI_TYPE = typeKey(UPENDI_PATH);

    private static ResourceKey<Level> levelKey(String path) {
        return ResourceKey.create(Registries.DIMENSION, CircleOfCraftMod.id(path));
    }

    private static ResourceKey<DimensionType> typeKey(String path) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, CircleOfCraftMod.id(path));
    }
}
