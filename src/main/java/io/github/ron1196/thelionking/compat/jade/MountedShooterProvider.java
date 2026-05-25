package io.github.ron1196.thelionking.compat.jade;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.MountedShooterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum MountedShooterProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation UID = new ResourceLocation(TheLionKingMod.MOD_ID, "mounted_shooter");

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull BlockAccessor accessor, @NotNull IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MountedShooterBlockEntity shooter)) {
            return;
        }
        ItemStack ammo = shooter.getDartStack();
        if (ammo.isEmpty()) {
            tooltip.add(Component.translatable("jade.thelionking.mounted_shooter.empty")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable(
                    "jade.thelionking.mounted_shooter.ammo", ammo.getHoverName(), ammo.getCount()));
        }
        MountedShooterBlockEntity.FireMode mode = shooter.getFireMode();
        tooltip.add(Component.translatable(
                "jade.thelionking.mounted_shooter.mode", Component.literal(mode.getDescription())));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }
}
