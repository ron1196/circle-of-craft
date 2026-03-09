package io.github.ron1196.thelionking.world.dimension;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class LKDimensions {

    public static final ResourceKey<Level> PRIDE_LANDS_LEVEL =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(TheLionKingMod.MOD_ID, "pride_lands"));

    public static final ResourceKey<Level> OUTLANDS_LEVEL =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(TheLionKingMod.MOD_ID, "outlands"));

    public static final ResourceKey<Level> UPENDI_LEVEL =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(TheLionKingMod.MOD_ID, "upendi"));

    public static final ResourceKey<DimensionType> PRIDE_LANDS_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(TheLionKingMod.MOD_ID, "pride_lands"));

    public static final ResourceKey<DimensionType> OUTLANDS_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(TheLionKingMod.MOD_ID, "outlands"));

    public static final ResourceKey<DimensionType> UPENDI_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(TheLionKingMod.MOD_ID, "upendi"));
}
