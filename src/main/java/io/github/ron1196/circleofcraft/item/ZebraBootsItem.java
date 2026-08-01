package io.github.ron1196.circleofcraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ZebraBootsItem extends ArmorItem {

    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("8f7b1c2a-4d3e-4a5b-9c6d-2e1f0a3b4c5d");
    private static final double SPEED_BONUS = 0.6D;
    private static final int TICKS_PER_WEAR_POINT = 20;

    private final Multimap<Attribute, AttributeModifier> feetModifiers;

    public ZebraBootsItem(ArmorMaterial material, Properties properties) {
        super(material, Type.BOOTS, properties);
        this.feetModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(EquipmentSlot.FEET))
                .put(
                        Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(
                                SPEED_MODIFIER_UUID,
                                "Zebra boots speed",
                                SPEED_BONUS,
                                AttributeModifier.Operation.MULTIPLY_TOTAL))
                .build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.FEET ? feetModifiers : super.getDefaultAttributeModifiers(slot);
    }

    /** Ported from the old mod: the speed is paid for in durability, and only while actually running. */
    public static void tickWear(Player player) {
        if (player.tickCount % TICKS_PER_WEAR_POINT != 0) return;

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(boots.getItem() instanceof ZebraBootsItem)) return;
        if (!player.onGround() || player.isInWater() || player.isInLava()) return;
        if (player.walkDist - player.walkDistO <= 0.0F) return;

        boots.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.FEET));
    }
}
