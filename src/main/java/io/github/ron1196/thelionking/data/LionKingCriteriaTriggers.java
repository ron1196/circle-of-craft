package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

public class LionKingCriteriaTriggers {

    public static final PlayerTrigger SHOOT_DART = createTrigger("shoot_dart");
    public static final PlayerTrigger QUEST_COMPLETE = createTrigger("quest_complete");
    public static final PlayerTrigger COMPLETE_RAFIKI_QUEST = createTrigger("complete_rafiki_quest");
    public static final PlayerTrigger TRADE_PUMBAA = createTrigger("trade_pumbaa");
    public static final PlayerTrigger FEED_ANIMAL = createTrigger("feed_animal");
    public static final PlayerTrigger TELEPORT_SIMBA = createTrigger("teleport_simba");
    public static final UseGrindingBowlTrigger USE_GRINDING_BOWL = new UseGrindingBowlTrigger();
    public static final PlayerTrigger RIDE_GIRAFFE = createTrigger("ride_giraffe");
    public static final PlayerTrigger PLAY_BONGO_DRUM = createTrigger("play_bongo_drum");
    public static final PlayerTrigger ENTER_PRIDE_LANDS = createTrigger("enter_pride_lands");
    public static final PlayerTrigger ENTER_OUTLANDS = createTrigger("enter_outlands");
    public static final PlayerTrigger ENTER_UPENDI = createTrigger("enter_upendi");
    public static final PlayerTrigger BEHEAD_HYENA = createTrigger("behead_hyena");
    public static final PlayerTrigger KILL_SCAR = createTrigger("kill_scar");
    public static final PlayerTrigger KILL_ZIRA = createTrigger("kill_zira");
    public static final PlayerTrigger USE_RHINO_HORN = createTrigger("use_rhino_horn");
    public static final PlayerTrigger SPEAK_TO_ZAZU = createTrigger("speak_to_zazu");

    private static PlayerTrigger createTrigger(String name) {
        return new PlayerTrigger(new ResourceLocation(TheLionKingMod.MOD_ID, name));
    }

    public static void register() {
        CriteriaTriggers.register(SHOOT_DART);
        CriteriaTriggers.register(QUEST_COMPLETE);
        CriteriaTriggers.register(COMPLETE_RAFIKI_QUEST);
        CriteriaTriggers.register(TRADE_PUMBAA);
        CriteriaTriggers.register(FEED_ANIMAL);
        CriteriaTriggers.register(TELEPORT_SIMBA);
        CriteriaTriggers.register(USE_GRINDING_BOWL);
        CriteriaTriggers.register(RIDE_GIRAFFE);
        CriteriaTriggers.register(PLAY_BONGO_DRUM);
        CriteriaTriggers.register(ENTER_PRIDE_LANDS);
        CriteriaTriggers.register(ENTER_OUTLANDS);
        CriteriaTriggers.register(ENTER_UPENDI);
        CriteriaTriggers.register(BEHEAD_HYENA);
        CriteriaTriggers.register(KILL_SCAR);
        CriteriaTriggers.register(KILL_ZIRA);
        CriteriaTriggers.register(USE_RHINO_HORN);
        CriteriaTriggers.register(SPEAK_TO_ZAZU);
    }
}
