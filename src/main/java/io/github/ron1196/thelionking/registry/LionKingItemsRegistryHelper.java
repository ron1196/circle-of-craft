package io.github.ron1196.thelionking.registry;

import java.util.Objects;
import java.util.function.Function;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

/**
 * Shared property builders and registration shortcuts for {@link LionKingItems}.
 */
final class LionKingItemsRegistryHelper {

    private LionKingItemsRegistryHelper() {}

    // ========== Property Builders ==========

    static Item.Properties itemProps(int stackSize) {
        return new Item.Properties().stacksTo(stackSize);
    }

    static FoodProperties foodProps(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation)
                .build();
    }

    static FoodProperties meatProps(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation)
                .meat()
                .build();
    }

    // ========== Registration Shortcuts ==========

    static <T extends Item> RegistryObject<Item> registerItem(String name, Function<Item.Properties, T> factory) {
        return LionKingItems.ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    static <T extends Item> RegistryObject<Item> registerItem(
            String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        return LionKingItems.ITEMS.register(name, () -> factory.apply(properties));
    }

    static RegistryObject<Item> simpleItem(String name) {
        return registerItem(name, Item::new);
    }

    static RegistryObject<Item> simpleItem(String name, int stackSize) {
        return registerItem(name, Item::new, itemProps(stackSize));
    }

    static RegistryObject<BlockItem> registerBlockItem(String name, RegistryObject<? extends Block> block) {
        return LionKingItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static RegistryObject<Item> foodItem(String name, int nutrition, float saturation) {
        return registerItem(name, Item::new, new Item.Properties().food(foodProps(nutrition, saturation)));
    }

    static RegistryObject<Item> meatItem(String name, int nutrition, float saturation) {
        return registerItem(name, Item::new, new Item.Properties().food(meatProps(nutrition, saturation)));
    }

    static RegistryObject<Item> meatEffectItem(
            String name, int nutrition, float saturation, int effectDuration, float effectChance) {
        return registerItem(
                name,
                Item::new,
                new Item.Properties()
                        .food(new FoodProperties.Builder()
                                .nutrition(nutrition)
                                .saturationMod(saturation)
                                .meat()
                                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, effectDuration, 0), effectChance)
                                .build()));
    }

    static RegistryObject<Item> spawnEgg(RegistryObject<? extends EntityType<? extends Mob>> type, int bg, int fg) {
        String entityName = Objects.requireNonNull(type.getId()).getPath();
        return LionKingItems.ITEMS.register(
                entityName + "_spawn_egg", () -> new ForgeSpawnEggItem(type, bg, fg, new Item.Properties()));
    }

    static RegistryObject<ArmorItem> armorItem(String name, ArmorMaterial material, ArmorItem.Type type) {
        return LionKingItems.ITEMS.register(name, () -> new ArmorItem(material, type, new Item.Properties()));
    }
}
