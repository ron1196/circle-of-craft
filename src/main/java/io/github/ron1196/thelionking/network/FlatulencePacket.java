package io.github.ron1196.thelionking.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Sent server→client when a Pumbaa bomb explodes near the player. Triggers the flatulence overlay. */
public class FlatulencePacket {

    public FlatulencePacket() {}

    public FlatulencePacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientWorldState.flatulenceTimer = 60);
        ctx.get().setPacketHandled(true);
    }
}
