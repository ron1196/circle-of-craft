package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Sent from client to server when the player right-clicks their Simba to toggle sit. */
public record SimbaSitPacket(int entityId) implements CustomPacketPayload {

    public static final Type<SimbaSitPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "simba_sit"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SimbaSitPacket> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, SimbaSitPacket::entityId, SimbaSitPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
