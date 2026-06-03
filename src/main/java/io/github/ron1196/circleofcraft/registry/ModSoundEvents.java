package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, CircleOfCraftMod.MOD_ID);

    // Lion sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_AMBIENT = register("entity.lion.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_ANGRY = register("entity.lion.angry");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_ROAR = register("entity.lion.roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_DEATH = register("entity.lion.death");

    // Zebra sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_AMBIENT = register("entity.zebra.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_HURT = register("entity.zebra.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_DEATH = register("entity.zebra.death");

    // Rhino sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_AMBIENT = register("entity.rhino.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_HURT = register("entity.rhino.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_DEATH = register("entity.rhino.death");

    // Zazu sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> ZAZU_AMBIENT = register("entity.zazu.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZAZU_HURT = register("entity.zazu.hurt");

    // Crocodile sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_AMBIENT = register("entity.crocodile.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_SNAP = register("entity.crocodile.snap");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_DEATH = register("entity.crocodile.death");

    // Vulture sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> VULTURE_AMBIENT = register("entity.vulture.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> VULTURE_HURT = register("entity.vulture.hurt");

    // Rafiki Bird sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> RAFIKI_BIRD_AMBIENT =
            register("entity.rafiki_bird.ambient");

    // Flamingo sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAMINGO_AMBIENT = register("entity.flamingo.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAMINGO_HURT = register("entity.flamingo.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAMINGO_DEATH = register("entity.flamingo.death");

    // Block sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> BONGO_DRUM_HIT = register("block.bongo_drum.hit");

    // Item sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> FLATULENCE = register("item.flatulence");
    public static final DeferredHolder<SoundEvent, SoundEvent> POP = register("item.pop");

    // Music
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_CIRCLE_OF_LIFE = register("music.circle_of_life");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_CANT_WAIT_TO_BE_KING =
            register("music.cant_wait_to_be_king");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_BE_PREPARED = register("music.be_prepared");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_HAKUNA_MATATA = register("music.hakuna_matata");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_CAN_YOU_FEEL_THE_LOVE =
            register("music.can_you_feel_the_love");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(CircleOfCraftMod.id(name)));
    }
}
