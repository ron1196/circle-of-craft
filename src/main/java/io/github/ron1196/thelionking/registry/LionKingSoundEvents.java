package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LionKingSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TheLionKingMod.MOD_ID);

    // Lion sounds
    public static final RegistryObject<SoundEvent> LION_AMBIENT = register("entity.lion.ambient");
    public static final RegistryObject<SoundEvent> LION_ANGRY = register("entity.lion.angry");
    public static final RegistryObject<SoundEvent> LION_ROAR = register("entity.lion.roar");
    public static final RegistryObject<SoundEvent> LION_DEATH = register("entity.lion.death");

    // Zebra sounds
    public static final RegistryObject<SoundEvent> ZEBRA_AMBIENT = register("entity.zebra.ambient");
    public static final RegistryObject<SoundEvent> ZEBRA_HURT = register("entity.zebra.hurt");
    public static final RegistryObject<SoundEvent> ZEBRA_DEATH = register("entity.zebra.death");

    // Rhino sounds
    public static final RegistryObject<SoundEvent> RHINO_AMBIENT = register("entity.rhino.ambient");
    public static final RegistryObject<SoundEvent> RHINO_HURT = register("entity.rhino.hurt");
    public static final RegistryObject<SoundEvent> RHINO_DEATH = register("entity.rhino.death");

    // Zazu sounds
    public static final RegistryObject<SoundEvent> ZAZU_AMBIENT = register("entity.zazu.ambient");
    public static final RegistryObject<SoundEvent> ZAZU_HURT = register("entity.zazu.hurt");

    // Crocodile sounds
    public static final RegistryObject<SoundEvent> CROCODILE_AMBIENT = register("entity.crocodile.ambient");
    public static final RegistryObject<SoundEvent> CROCODILE_SNAP = register("entity.crocodile.snap");
    public static final RegistryObject<SoundEvent> CROCODILE_DEATH = register("entity.crocodile.death");

    // Vulture sounds
    public static final RegistryObject<SoundEvent> VULTURE_AMBIENT = register("entity.vulture.ambient");
    public static final RegistryObject<SoundEvent> VULTURE_HURT = register("entity.vulture.hurt");

    // Rafiki Bird sounds
    public static final RegistryObject<SoundEvent> RAFIKI_BIRD_AMBIENT = register("entity.rafiki_bird.ambient");

    // Flamingo sounds
    public static final RegistryObject<SoundEvent> FLAMINGO_AMBIENT = register("entity.flamingo.ambient");
    public static final RegistryObject<SoundEvent> FLAMINGO_HURT = register("entity.flamingo.hurt");
    public static final RegistryObject<SoundEvent> FLAMINGO_DEATH = register("entity.flamingo.death");

    // Block sounds
    public static final RegistryObject<SoundEvent> BONGO_DRUM_HIT = register("block.bongo_drum.hit");

    // Item sounds
    public static final RegistryObject<SoundEvent> FLATULENCE = register("item.flatulence");
    public static final RegistryObject<SoundEvent> POP = register("item.pop");

    // Music
    public static final RegistryObject<SoundEvent> MUSIC_CIRCLE_OF_LIFE = register("music.circle_of_life");
    public static final RegistryObject<SoundEvent> MUSIC_CANT_WAIT_TO_BE_KING = register("music.cant_wait_to_be_king");
    public static final RegistryObject<SoundEvent> MUSIC_BE_PREPARED = register("music.be_prepared");
    public static final RegistryObject<SoundEvent> MUSIC_HAKUNA_MATATA = register("music.hakuna_matata");
    public static final RegistryObject<SoundEvent> MUSIC_CAN_YOU_FEEL_THE_LOVE =
            register("music.can_you_feel_the_love");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(
                name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(TheLionKingMod.MOD_ID, name)));
    }
}
