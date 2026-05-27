package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {

    private static final String PROTOCOL_VERSION = "4";
    public static SimpleChannel CHANNEL;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                CircleOfCraftMod.id("main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

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

        CHANNEL.messageBuilder(LoginSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(LoginSyncPacket::encode)
                .decoder(LoginSyncPacket::new)
                .consumerMainThread(LoginSyncPacket::handle)
                .add();

        CHANNEL.messageBuilder(PlayerDataSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlayerDataSyncPacket::encode)
                .decoder(PlayerDataSyncPacket::new)
                .consumerMainThread(PlayerDataSyncPacket::handle)
                .add();

        CHANNEL.messageBuilder(FlatulencePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FlatulencePacket::encode)
                .decoder(FlatulencePacket::new)
                .consumerMainThread(FlatulencePacket::handle)
                .add();

        CHANNEL.messageBuilder(PortalOverlayPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PortalOverlayPacket::encode)
                .decoder(PortalOverlayPacket::new)
                .consumerMainThread(PortalOverlayPacket::handle)
                .add();
    }
}
