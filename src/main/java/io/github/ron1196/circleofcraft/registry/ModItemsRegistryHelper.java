package io.github.ron1196.circleofcraft.registry;

import java.util.Objects;
import java.util.function.Function;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Shared property builders and registration shortcuts for {@link ModItems}.
 */
final class ModItemsRegistryHelper {

    private ModItemsRegistryHelper() {}

    // ========== Property Builders ==========

    static Item.Properties itemProps(int stackSize) {
        return new Item.Properties().stacksTo(stackSize);
    }

    static FoodProperties foodProps(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation)
                .build();
    }

    static FoodProperties meatProps(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation)
                .build();
    }

    // ========== Registration Shortcuts ==========

    static <T extends Item> DeferredItem<Item> registerItem(String name, Function<Item.Properties, T> factory) {
        return ModItems.ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    static <T extends Item> DeferredItem<Item> registerItem(
            String name, Function<Item.Properties, T> factory, Item.Properties properties) {
        return ModItems.ITEMS.register(name, () -> factory.apply(properties));
    }

    static DeferredItem<Item> simpleItem(String name) {
        return registerItem(name, Item::new);
    }

    static DeferredItem<Item> simpleItem(String name, int stackSize) {
        return registerItem(name, Item::new, itemProps(stackSize));
    }

    static DeferredItem<BlockItem> registerBlockItem(
            String name, net.neoforged.neoforge.registries.DeferredBlock<? extends Block> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static DeferredItem<Item> foodItem(String name, int nutrition, float saturation) {
        return registerItem(name, Item::new, new Item.Properties().food(foodProps(nutrition, saturation)));
    }

    static DeferredItem<Item> meatItem(String name, int nutrition, float saturation) {
        return registerItem(name, Item::new, new Item.Properties().food(meatProps(nutrition, saturation)));
    }

    static DeferredItem<Item> meatEffectItem(
            String name, int nutrition, float saturation, int effectDuration, float effectChance) {
        return registerItem(
                name,
                Item::new,
                new Item.Properties()
                        .food(new FoodProperties.Builder()
                                .nutrition(nutrition)
                                .saturationModifier(saturation)
                                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, effectDuration, 0), effectChance)
                                .build()));
    }

    static DeferredItem<Item> spawnEgg(
            net.neoforged.neoforge.registries.DeferredHolder<EntityType<?>, ? extends EntityType<? extends Mob>> type,
            int bg,
            int fg) {
        String entityName = Objects.requireNonNull(type.getId()).getPath();
        return ModItems.ITEMS.register(
                entityName + "_spawn_egg", () -> new DeferredSpawnEggItem(type, bg, fg, new Item.Properties()));
    }

    static DeferredItem<ArmorItem> armorItem(
            String name,
            net.minecraft.core.Holder<ArmorMaterial> material,
            ArmorItem.Type type,
            int durabilityMultiplier) {
        return ModItems.ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties();
            if (durabilityMultiplier > 0) {
                properties.durability(type.getDurability(durabilityMultiplier));
            }
            return new ArmorItem(material, type, properties);
        });
    }
}
