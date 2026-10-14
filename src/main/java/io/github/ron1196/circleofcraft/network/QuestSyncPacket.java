package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record QuestSyncPacket(String questId, String stageId, boolean checked) implements CustomPacketPayload {

    public static final Type<QuestSyncPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "quest_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuestSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            QuestSyncPacket::questId,
            ByteBufCodecs.STRING_UTF8,
            QuestSyncPacket::stageId,
            ByteBufCodecs.BOOL,
            QuestSyncPacket::checked,
            QuestSyncPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
