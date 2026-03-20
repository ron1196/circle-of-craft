package io.github.ron1196.thelionking.registry;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.world.feature.BananaTreeFeature;
import io.github.ron1196.thelionking.world.feature.DeadTreeFeature;
import io.github.ron1196.thelionking.world.feature.LilyPadFeature;
import io.github.ron1196.thelionking.world.feature.MangoTreeFeature;
import io.github.ron1196.thelionking.world.feature.PassionTreeFeature;
import io.github.ron1196.thelionking.world.feature.PrideDungeonFeature;
import io.github.ron1196.thelionking.world.feature.RafikiTreeFeature;
import io.github.ron1196.thelionking.world.feature.RainforestTreeFeature;
import io.github.ron1196.thelionking.world.feature.TermiteMoundFeature;
import io.github.ron1196.thelionking.world.feature.TicketBoothFeature;
import io.github.ron1196.thelionking.world.feature.TimonPumbaaLodgeFeature;
import io.github.ron1196.thelionking.world.feature.TreasureMoundFeature;
import io.github.ron1196.thelionking.world.feature.ZiraMoundFeature;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Features {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, TheLionKingMod.MOD_ID);

    // ── Helper ─────────────────────────────────────────────────────────────

    private static RegistryObject<Feature<NoneFeatureConfiguration>> register(
            String name, Function<Codec<NoneFeatureConfiguration>, Feature<NoneFeatureConfiguration>> factory) {
        return FEATURES.register(name, () -> factory.apply(NoneFeatureConfiguration.CODEC));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(TheLionKingMod.MOD_ID, name));
    }

    // ── Trees ──────────────────────────────────────────────────────────────

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> BANANA_TREE =
            register("banana_tree", BananaTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DEAD_TREE =
            register("dead_tree", DeadTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> RAINFOREST_TREE =
            register("rainforest_tree", RainforestTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MANGO_TREE =
            register("mango_tree", MangoTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PASSION_TREE =
            register("passion_tree", PassionTreeFeature::new);

    // ── Structures ─────────────────────────────────────────────────────────

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TERMITE_MOUND =
            register("termite_mound", TermiteMoundFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> RAFIKI_TREE =
            register("rafiki_tree", RafikiTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ZIRA_MOUND =
            register("zira_mound", ZiraMoundFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TICKET_BOOTH =
            register("ticket_booth", TicketBoothFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TIMON_PUMBAA_LODGE =
            register("timon_pumbaa_lodge", TimonPumbaaLodgeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TREASURE_MOUND =
            register("treasure_mound", TreasureMoundFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LILY_PAD =
            register("lily_pad", LilyPadFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PRIDE_DUNGEON =
            register("pride_dungeon", PrideDungeonFeature::new);

    // ── Configured Feature Keys (referenced by tree growers and placed features) ──

    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIDE_ACACIA_TREE_KEY = configuredKey("pride_acacia_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RAINFOREST_TREE_KEY = configuredKey("rainforest_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_RAINFOREST_TREE_KEY =
            configuredKey("mega_rainforest_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGO_TREE_KEY = configuredKey("mango_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PASSION_TREE_KEY = configuredKey("passion_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BANANA_TREE_KEY = configuredKey("banana_tree");
}
