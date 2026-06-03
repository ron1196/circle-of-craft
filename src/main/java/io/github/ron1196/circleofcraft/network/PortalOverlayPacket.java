package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent server→client every tick while a player stands in a Lion King portal. Drives the portal
 * overlay on the client, mirroring the old mod's gradual screen tint.
 */
public record PortalOverlayPacket(int ticks, String portalBlockName) implements CustomPacketPayload {

    public static final Type<PortalOverlayPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "portal_overlay"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PortalOverlayPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PortalOverlayPacket::ticks,
            ByteBufCodecs.STRING_UTF8,
            PortalOverlayPacket::portalBlockName,
            PortalOverlayPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
