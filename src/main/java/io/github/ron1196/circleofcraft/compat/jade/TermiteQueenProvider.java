package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteEntity;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import io.github.ron1196.circleofcraft.entity.npc.ZiraEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum TermiteQueenProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = CircleOfCraftMod.id("termite_queen");
    private static final String NBT_SPAWN_COOLDOWN = "JadeTQ_SpawnCooldown";
    private static final int TICKS_PER_SECOND = 20;

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull EntityAccessor accessor, @NotNull IPluginConfig config) {
        if (!(accessor.getEntity() instanceof TermiteQueenEntity queen)) {
            return;
        }

        int hpPct = Math.round(queen.getHealth() / queen.getMaxHealth() * 100);
        tooltip.add(Component.translatable("jade.circleofcraft.termite_queen.hp", hpPct));

        int nearby = queen.level()
                .getEntitiesOfClass(
                        TermiteEntity.class, queen.getBoundingBox().inflate(TermiteQueenEntity.TERMITE_SEARCH_RADIUS))
                .size();
        tooltip.add(Component.translatable(
                "jade.circleofcraft.termite_queen.minions", nearby, TermiteQueenEntity.MAX_NEARBY_TERMITES));

        CompoundTag data = accessor.getServerData();
        if (data.contains(NBT_SPAWN_COOLDOWN) && queen.getTarget() != null) {
            int cooldownTicks = data.getInt(NBT_SPAWN_COOLDOWN);
            if (cooldownTicks > 0) {
                int seconds = Math.max(1, (cooldownTicks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND);
                tooltip.add(Component.translatable("jade.circleofcraft.termite_queen.next_spawn", seconds));
            }
        }

        for (Entity passenger : queen.getPassengers()) {
            if (passenger instanceof ZiraEntity) {
                tooltip.add(Component.translatable("jade.circleofcraft.termite_queen.zira_mounted")
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
                break;
            }
        }
    }

    @Override
    public void appendServerData(@NotNull CompoundTag data, @NotNull EntityAccessor accessor) {
        if (accessor.getEntity() instanceof TermiteQueenEntity queen) {
            data.putInt(NBT_SPAWN_COOLDOWN, queen.getSpawnCooldown());
        }
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }
}
