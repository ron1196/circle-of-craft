package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LKPlayerDataProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<LKPlayerData> CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    public static final ResourceLocation IDENTIFIER =
            new ResourceLocation(TheLionKingMod.MOD_ID, "player_data");

    private final LKPlayerData data = new LKPlayerData();
    private final LazyOptional<LKPlayerData> optional = LazyOptional.of(() -> data);

    // ── Capability methods ──────────────────────────────────────────────────────

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.deserializeNBT(nbt);
    }

    // ── Helper ──────────────────────────────────────────────────────────────────

    public static LKPlayerData get(Player player) {
        return player.getCapability(CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("LKPlayerData capability missing on player"));
    }

    // ── Events ──────────────────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(IDENTIFIER, new LKPlayerDataProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        try {
            LKPlayerData original = get(event.getOriginal());
            LKPlayerData clone = get(event.getEntity());
            clone.copyFrom(original);
        } finally {
            event.getOriginal().invalidateCaps();
        }
    }
}
