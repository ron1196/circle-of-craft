package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from client to server when the player right-clicks their Simba to toggle sit.
 */
public class SimbaSitPacket {

    private final int entityId;

    public SimbaSitPacket(int entityId) {
        this.entityId = entityId;
    }

    public SimbaSitPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            Entity entity = sender.serverLevel().getEntity(entityId);
            if (!(entity instanceof SimbaEntity simba)) return;

            // Only the owner can toggle sitting
            if (simba.isOwnedBy(sender)) {
                simba.toggleSitting(sender);
            }
        });
        context.setPacketHandled(true);
    }
}
