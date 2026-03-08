package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.enchantment.ScourgeOfHyenasEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TheLionKingMod.MOD_ID);

    private static final EquipmentSlot[] MAINHAND = new EquipmentSlot[]{EquipmentSlot.MAINHAND};

    // Scourge of Hyenas - extra damage to hyenas, max level 5, weapon type
    public static final RegistryObject<Enchantment> SCOURGE_OF_HYENAS = ENCHANTMENTS.register("scourge_of_hyenas",
            ScourgeOfHyenasEnchantment::new);

    // Rafiki Damage - sharpness for rafiki stick only, max level 5
    public static final RegistryObject<Enchantment> RAFIKI_DAMAGE = ENCHANTMENTS.register("rafiki_damage",
            () -> new Enchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, MAINHAND) {
                @Override
                public int getMaxLevel() {
                    return 5;
                }

                @Override
                public int getMinCost(int level) {
                    return 1 + (level - 1) * 11;
                }

                @Override
                public int getMaxCost(int level) {
                    return getMinCost(level) + 20;
                }
            });

    // Rafiki Durability - unbreaking for rafiki stick only, max level 3
    public static final RegistryObject<Enchantment> RAFIKI_DURABILITY = ENCHANTMENTS.register("rafiki_durability",
            () -> new Enchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, MAINHAND) {
                @Override
                public int getMaxLevel() {
                    return 3;
                }

                @Override
                public int getMinCost(int level) {
                    return 5 + (level - 1) * 8;
                }

                @Override
                public int getMaxCost(int level) {
                    return getMinCost(level) + 50;
                }
            });

    // Rafiki Thunder - lightning on hit for rafiki stick only, max level 3
    public static final RegistryObject<Enchantment> RAFIKI_THUNDER = ENCHANTMENTS.register("rafiki_thunder",
            () -> new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, MAINHAND) {
                @Override
                public int getMaxLevel() {
                    return 3;
                }

                @Override
                public int getMinCost(int level) {
                    return 15 + (level - 1) * 9;
                }

                @Override
                public int getMaxCost(int level) {
                    return getMinCost(level) + 50;
                }
            });

    // Biggah Diggah - mining speed for tunnah diggah only, max level 1
    public static final RegistryObject<Enchantment> BIGGAH_DIGGAH = ENCHANTMENTS.register("biggah_diggah",
            () -> new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, MAINHAND) {
                @Override
                public int getMaxLevel() {
                    return 1;
                }

                @Override
                public int getMinCost(int level) {
                    return 15;
                }

                @Override
                public int getMaxCost(int level) {
                    return getMinCost(level) + 50;
                }
            });

    // Precision - silk touch variant for tunnah diggah only, max level 1
    public static final RegistryObject<Enchantment> PRECISION = ENCHANTMENTS.register("precision",
            () -> new Enchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, MAINHAND) {
                @Override
                public int getMaxLevel() {
                    return 1;
                }

                @Override
                public int getMinCost(int level) {
                    return 15;
                }

                @Override
                public int getMaxCost(int level) {
                    return getMinCost(level) + 50;
                }
            });
}
