package io.github.ron1196.thelionking.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Sent server→client every tick while a player stands in a Lion King portal. Drives the portal
 * overlay on the client, mirroring the old mod's gradual screen tint.
 */
public class PortalOverlayPacket {

    private final int ticks;
    private final String portalBlockName;

    public PortalOverlayPacket(int ticks, String portalBlockName) {
        this.ticks = ticks;
        this.portalBlockName = portalBlockName;
    }

    public PortalOverlayPacket(FriendlyByteBuf buf) {
        this.ticks = buf.readVarInt();
        this.portalBlockName = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(ticks);
        buf.writeUtf(portalBlockName);
    }

    private static final float SPINNING_RAMP_RATE = 0.0125F;

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorldState.portalOverlayTicks = ticks;
            ClientWorldState.portalBlockName = portalBlockName;
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                ClientWorldState.portalLastUpdateTick = mc.level.getGameTime();
            }
            if (mc.player != null) {
                float intensity = Math.min(ticks * SPINNING_RAMP_RATE, 1.0F);
                mc.player.oSpinningEffectIntensity = mc.player.spinningEffectIntensity;
                mc.player.spinningEffectIntensity = intensity;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
