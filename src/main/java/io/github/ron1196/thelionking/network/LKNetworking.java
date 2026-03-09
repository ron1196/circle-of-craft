package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class LKNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(TheLionKingMod.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        CHANNEL.messageBuilder(SimbaSitPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(SimbaSitPacket::encode)
                .decoder(SimbaSitPacket::new)
                .consumerMainThread(SimbaSitPacket::handle)
                .add();

        CHANNEL.messageBuilder(QuestSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(QuestSyncPacket::encode)
                .decoder(QuestSyncPacket::new)
                .consumerMainThread(QuestSyncPacket::handle)
                .add();

        CHANNEL.messageBuilder(QuestCheckPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(QuestCheckPacket::encode)
                .decoder(QuestCheckPacket::new)
                .consumerMainThread(QuestCheckPacket::handle)
                .add();
    }
}
