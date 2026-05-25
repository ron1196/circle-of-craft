package io.github.ron1196.thelionking.compat.jade;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum BugTrapProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = new ResourceLocation(TheLionKingMod.MOD_ID, "bug_trap");
    private static final String NBT_NEXT_ATTRACT = "JadeBT_NextAttract";
    private static final int TICKS_PER_SECOND = 20;

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull BlockAccessor accessor, @NotNull IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains(NBT_NEXT_ATTRACT)) {
            tooltip.add(
                    Component.translatable("jade.thelionking.bug_trap.no_bait").withStyle(ChatFormatting.GRAY));
            return;
        }
        int ticks = data.getInt(NBT_NEXT_ATTRACT);
        int seconds = Math.max(1, (ticks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND);
        tooltip.add(Component.translatable("jade.thelionking.bug_trap.next_attract", seconds));
    }

    @Override
    public void appendServerData(@NotNull CompoundTag data, @NotNull BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof BugTrapBlockEntity trap)) {
            return;
        }
        int ticks = trap.getTicksUntilNextAttract();
        if (ticks >= 0) {
            data.putInt(NBT_NEXT_ATTRACT, ticks);
        }
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }
}
