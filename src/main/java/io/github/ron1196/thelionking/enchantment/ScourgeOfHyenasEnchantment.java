package io.github.ron1196.thelionking.enchantment;

import io.github.ron1196.thelionking.entity.hostile.HyenaEntity;
import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ScourgeOfHyenasEnchantment extends Enchantment {

    public ScourgeOfHyenasEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 20;
    }

    @Override
    public float getDamageBonus(int level, MobType mobType) {
        // MobType-based bonus doesn't apply here since hyenas use default MobType.
        // The actual per-entity check is done via getDamageBonus with entity parameter
        // which we handle in the event-based approach below.
        return 0.0F;
    }

    /**
     * Returns the bonus damage against a specific entity. This is checked by calling code to add
     * extra damage to hyena-type mobs. Since Enchantment doesn't have a built-in per-entity
     * getDamageBonus override, we expose this as a utility method to be called from an event handler.
     */
    public static float getBonusDamageForEntity(int level, net.minecraft.world.entity.LivingEntity target) {
        if (target instanceof HyenaEntity || target instanceof SkeletalHyenaEntity) {
            return level * 4.0F;
        }
        return 0.0F;
    }
}
