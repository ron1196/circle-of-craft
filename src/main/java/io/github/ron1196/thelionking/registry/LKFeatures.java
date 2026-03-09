package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.world.feature.BananaTreeFeature;
import io.github.ron1196.thelionking.world.feature.DeadTreeFeature;
import io.github.ron1196.thelionking.world.feature.MangoTreeFeature;
import io.github.ron1196.thelionking.world.feature.PassionTreeFeature;
import io.github.ron1196.thelionking.world.feature.RainforestTreeFeature;
import io.github.ron1196.thelionking.world.feature.TermiteMoundFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, TheLionKingMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> BANANA_TREE =
            FEATURES.register("banana_tree", () -> new BananaTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DEAD_TREE =
            FEATURES.register("dead_tree", () -> new DeadTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> RAINFOREST_TREE =
            FEATURES.register("rainforest_tree", () -> new RainforestTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MANGO_TREE =
            FEATURES.register("mango_tree", () -> new MangoTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PASSION_TREE =
            FEATURES.register("passion_tree", () -> new PassionTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TERMITE_MOUND =
            FEATURES.register("termite_mound", () -> new TermiteMoundFeature(NoneFeatureConfiguration.CODEC));

    // ResourceKeys for configured features (referenced by tree growers and placed features)
    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIDE_ACACIA_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, "pride_acacia_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, "rainforest_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGO_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, "mango_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PASSION_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, "passion_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BANANA_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, "banana_tree"));
}
