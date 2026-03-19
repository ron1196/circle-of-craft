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
public class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {

  public static final Capability<PlayerData> CAPABILITY =
      CapabilityManager.get(new CapabilityToken<>() {});

  public static final ResourceLocation IDENTIFIER =
      new ResourceLocation(TheLionKingMod.MOD_ID, "player_data");

  private final PlayerData data = new PlayerData();
  private final LazyOptional<PlayerData> optional = LazyOptional.of(() -> data);

  // ── Capability methods ──────────────────────────────────────────────────────

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(
      @NotNull Capability<T> cap, @Nullable Direction side) {
    return CAPABILITY.orEmpty(cap, optional);
  }

  @Override
  public CompoundTag serializeNBT() {
    return data.serializeNBT();
  }

  @Override
  public void deserializeNBT(@NotNull CompoundTag nbt) {
    data.deserializeNBT(nbt);
  }

  // ── Helper ──────────────────────────────────────────────────────────────────

  public static PlayerData get(Player player) {
    return player
        .getCapability(CAPABILITY)
        .orElseThrow(() -> new IllegalStateException("PlayerData capability missing on player"));
  }

  // ── Events ──────────────────────────────────────────────────────────────────

  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      event.addCapability(IDENTIFIER, new PlayerDataProvider());
    }
  }

  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    event.getOriginal().reviveCaps();
    try {
      PlayerData original = get(event.getOriginal());
      PlayerData clone = get(event.getEntity());
      clone.copyFrom(original);
    } finally {
      event.getOriginal().invalidateCaps();
    }
  }
}
