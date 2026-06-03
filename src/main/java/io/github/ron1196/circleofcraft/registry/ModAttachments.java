package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.PlayerData;
import java.util.function.Supplier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, CircleOfCraftMod.MOD_ID);

    public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA = ATTACHMENTS.register(
            "player_data",
            () -> AttachmentType.serializable(PlayerData::new).copyOnDeath().build());

    private ModAttachments() {}
}
