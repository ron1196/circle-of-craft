package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent server→client when a Pumbaa bomb explodes near the player. Triggers the flatulence overlay.
 */
public record FlatulencePacket() implements CustomPacketPayload {

    public static final FlatulencePacket INSTANCE = new FlatulencePacket();

    public static final Type<FlatulencePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "flatulence"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlatulencePacket> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
