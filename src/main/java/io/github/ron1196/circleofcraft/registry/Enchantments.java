package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class Enchantments {

    public static final ResourceKey<Enchantment> SCOURGE_OF_HYENAS =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("scourge_of_hyenas"));

    public static final ResourceKey<Enchantment> RAFIKI_DAMAGE =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("rafiki_damage"));

    public static final ResourceKey<Enchantment> RAFIKI_DURABILITY =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("rafiki_durability"));

    public static final ResourceKey<Enchantment> RAFIKI_THUNDER =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("rafiki_thunder"));

    public static final ResourceKey<Enchantment> BIGGAH_DIGGAH =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("biggah_diggah"));

    public static final ResourceKey<Enchantment> PRECISION =
            ResourceKey.create(Registries.ENCHANTMENT, CircleOfCraftMod.id("precision"));
}
