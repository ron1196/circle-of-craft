package io.github.ron1196.circleofcraft.item.tier;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, CircleOfCraftMod.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SILVER = register(
            "silver",
            new int[] {2, 5, 7, 2},
            16,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            () -> Ingredient.of(ModItems.SILVER_INGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GEMSBOK = register(
            "gemsbok",
            new int[] {1, 4, 5, 2},
            8,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(ModItems.GEMSBOK_HIDE.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PEACOCK = register(
            "peacock",
            new int[] {3, 6, 8, 3},
            9,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            1.0F,
            0.0F,
            () -> Ingredient.of(ModItems.PEACOCK_GEM.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OUTLANDS = register(
            "outlands",
            new int[] {2, 5, 6, 2},
            0,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            () -> Ingredient.of(ModItems.OUTLANDER_FUR.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TICKET_LION = register(
            "ticket_lion",
            new int[] {0, 0, 0, 0},
            0,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.EMPTY);

    private ModArmorMaterials() {}

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
            String name,
            int[] defensePerSlot,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient) {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            defense.put(type, defensePerSlot[type.getSlot().getIndex()]);
        }
        List<ArmorMaterial.Layer> layers =
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, name)));
        return ARMOR_MATERIALS.register(
                name,
                () -> new ArmorMaterial(
                        defense,
                        enchantmentValue,
                        equipSound,
                        repairIngredient,
                        layers,
                        toughness,
                        knockbackResistance));
    }
}
