package io.github.ron1196.thelionking.compat.jade;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.SpawnerBlockEntity;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SpawnerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = new ResourceLocation(TheLionKingMod.MOD_ID, "lk_spawner");
    private static final String NBT_ENTITY = "JadeLKS_Entity";
    private static final String NBT_DELAY = "JadeLKS_Delay";
    private static final String NBT_CAP = "JadeLKS_Cap";
    private static final String NBT_RANGE = "JadeLKS_Range";
    private static final int TICKS_PER_SECOND = 20;
    private static final int VERTICAL_SEARCH_HEIGHT = 4;

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull BlockAccessor accessor, @NotNull IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains(NBT_ENTITY)) {
            tooltip.add(
                    Component.translatable("jade.thelionking.spawner.inactive").withStyle(ChatFormatting.GRAY));
            return;
        }
        ResourceLocation entityId = new ResourceLocation(data.getString(NBT_ENTITY));
        Optional<EntityType<?>> type = EntityType.byString(entityId.toString());
        Component name = type.map(EntityType::getDescription).orElseGet(() -> Component.literal(entityId.toString()));
        tooltip.add(Component.translatable("jade.thelionking.spawner.spawns", name));

        int cap = data.getInt(NBT_CAP);
        int range = data.getInt(NBT_RANGE);
        int nearby = type.map(t -> countNearby(accessor.getLevel(), accessor.getPosition(), t, range))
                .orElse(0);
        tooltip.add(Component.translatable("jade.thelionking.spawner.nearby", nearby, cap));

        int delay = data.getInt(NBT_DELAY);
        if (delay > 0) {
            int seconds = Math.max(1, (delay + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND);
            tooltip.add(Component.translatable("jade.thelionking.spawner.next_in", seconds));
        }
    }

    @Override
    public void appendServerData(@NotNull CompoundTag data, @NotNull BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof SpawnerBlockEntity spawner)) {
            return;
        }
        ResourceLocation entityId = spawner.getEntityId();
        if (entityId == null) {
            return;
        }
        data.putString(NBT_ENTITY, entityId.toString());
        data.putInt(NBT_DELAY, spawner.getDelay());
        data.putInt(NBT_CAP, spawner.getMaxNearbyEntities());
        data.putInt(NBT_RANGE, spawner.getSpawnRange());
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    private static int countNearby(Level level, BlockPos pos, EntityType<?> type, int range) {
        AABB area = new AABB(pos).inflate(range * 2.0, VERTICAL_SEARCH_HEIGHT, range * 2.0);
        return level.getEntitiesOfClass(Entity.class, area, e -> e.getType() == type)
                .size();
    }
}
