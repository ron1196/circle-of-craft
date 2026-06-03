package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import io.github.ron1196.circleofcraft.entity.animal.favor.AnimalFavorEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum AnimalFavorProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    public static final ResourceLocation UID = CircleOfCraftMod.id("animal_favor");
    private static final String NBT_ITEM = "JadeAF_Item";
    private static final String NBT_AMOUNT = "JadeAF_Amount";

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull EntityAccessor accessor, @NotNull IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains(NBT_ITEM)) {
            return;
        }
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(data.getString(NBT_ITEM)));
        int amount = data.getInt(NBT_AMOUNT);
        tooltip.add(Component.translatable("jade.circleofcraft.animal_favor.wants", amount, item.getDescription())
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void appendServerData(@NotNull CompoundTag data, @NotNull EntityAccessor accessor) {
        if (!(accessor.getEntity() instanceof ModAnimal animal)) {
            return;
        }
        if (accessor.getPlayer() == null) {
            return;
        }
        AnimalFavorEntry entry = animal.getFavorEntryFor(accessor.getPlayer().getUUID());
        if (entry == null) {
            return;
        }
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(entry.requiredItem());
        data.putString(NBT_ITEM, itemKey.toString());
        data.putInt(NBT_AMOUNT, entry.requiredAmount());
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }
}
