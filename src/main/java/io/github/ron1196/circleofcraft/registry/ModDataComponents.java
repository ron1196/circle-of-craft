package io.github.ron1196.circleofcraft.registry;

import com.mojang.serialization.Codec;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CircleOfCraftMod.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> THUNDER_COOLDOWN =
            register("thunder_cooldown", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final Supplier<DataComponentType<Boolean>> SCAR_NEARBY =
            register("scar_nearby", b -> b.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final Supplier<DataComponentType<Boolean>> ASTRAL_ACTIVE =
            register("astral_active", b -> b.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    private static <T> Supplier<DataComponentType<T>> register(
            String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(
                name, () -> builder.apply(DataComponentType.builder()).build());
    }

    private ModDataComponents() {}
}
