package io.github.ron1196.thelionking.item.tier;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum LKArmorMaterials implements ArmorMaterial {
    SILVER("silver", 19, new int[]{2, 5, 7, 2}, 16,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(LKItems.SILVER_INGOT.get())),
    GEMSBOK("gemsbok", 8, new int[]{1, 4, 5, 2}, 8,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            () -> Ingredient.of(LKItems.GEMSBOK_HIDE.get())),
    PEACOCK("peacock", 31, new int[]{3, 6, 8, 3}, 9,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F,
            () -> Ingredient.of(LKItems.PEACOCK_GEM.get())),
    OUTLANDS("outlands", 12, new int[]{2, 5, 6, 2}, 0,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(LKItems.OUTLANDER_FUR.get())),
    TICKET_LION("ticket_lion", 0, new int[]{0, 0, 0, 0}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            () -> Ingredient.EMPTY);

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    LKArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections,
                     int enchantmentValue, SoundEvent sound, float toughness,
                     float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.slotProtections[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return TheLionKingMod.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
