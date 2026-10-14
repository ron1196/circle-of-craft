package io.github.ron1196.circleofcraft.network;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record QuestCheckPacket(String questId) implements CustomPacketPayload {

    public static final Type<QuestCheckPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(CircleOfCraftMod.MOD_ID, "quest_check"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuestCheckPacket> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.STRING_UTF8, QuestCheckPacket::questId, QuestCheckPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
