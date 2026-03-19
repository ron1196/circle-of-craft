package io.github.ron1196.thelionking.client.sound;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.registry.SoundEvents;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/**
 * Plays "Circle of Life" when the player enters the Pride Lands dimension. Tracks the previous
 * dimension on the client and triggers the song on dimension change.
 */
@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DimensionMusicHandler {

    private static @Nullable ResourceKey<Level> previousDimension;
    private static @Nullable SoundInstance currentMusic;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            previousDimension = null;
            return;
        }

        ResourceKey<Level> currentDimension = mc.level.dimension();
        if (previousDimension != null && previousDimension != currentDimension) {
            onDimensionChanged(mc, currentDimension);
        }
        previousDimension = currentDimension;
    }

    private static void onDimensionChanged(Minecraft mc, ResourceKey<Level> newDimension) {
        if (newDimension == Dimensions.PRIDE_LANDS_LEVEL) {
            stopCurrentMusic(mc);
            currentMusic = SimpleSoundInstance.forMusic(SoundEvents.MUSIC_CIRCLE_OF_LIFE.get());
            mc.getSoundManager().play(currentMusic);
        }
    }

    private static void stopCurrentMusic(Minecraft mc) {
        if (currentMusic != null) {
            mc.getSoundManager().stop(currentMusic);
            currentMusic = null;
        }
    }
}
