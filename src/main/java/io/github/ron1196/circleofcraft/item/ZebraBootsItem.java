package io.github.ron1196.circleofcraft.item;

import com.google.common.base.Suppliers;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

public class ZebraBootsItem extends ArmorItem {

    private static final ResourceLocation SPEED_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "zebra_boots_speed");
    private static final double SPEED_BONUS = 0.6D;
    private static final int TICKS_PER_WEAR_POINT = 20;

    private final Supplier<ItemAttributeModifiers> modifiers;

    public ZebraBootsItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.BOOTS, properties);
        this.modifiers = Suppliers.memoize(() -> {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            for (ItemAttributeModifiers.Entry entry :
                    super.getDefaultAttributeModifiers().modifiers()) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
            builder.add(
                    Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(
                            SPEED_MODIFIER_ID, SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                    EquipmentSlotGroup.FEET);
            return builder.build();
        });
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        return modifiers.get();
    }

    /** Ported from the old mod: the speed is paid for in durability, and only while actually running. */
    public static void tickWear(Player player) {
        if (player.tickCount % TICKS_PER_WEAR_POINT != 0) return;

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(boots.getItem() instanceof ZebraBootsItem)) return;
        if (!player.onGround() || player.isInWater() || player.isInLava()) return;
        if (player.walkDist - player.walkDistO <= 0.0F) return;

        boots.hurtAndBreak(1, player, EquipmentSlot.FEET);
    }
}
