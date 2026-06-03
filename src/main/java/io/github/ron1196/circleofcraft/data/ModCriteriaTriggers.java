package io.github.ron1196.circleofcraft.data;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ModCriteriaTriggers {

    public static final PlayerTrigger SHOOT_DART = register("shoot_dart", new PlayerTrigger());
    public static final PlayerTrigger QUEST_COMPLETE = register("quest_complete", new PlayerTrigger());
    public static final PlayerTrigger COMPLETE_RAFIKI_QUEST = register("complete_rafiki_quest", new PlayerTrigger());
    public static final PlayerTrigger TRADE_PUMBAA = register("trade_pumbaa", new PlayerTrigger());
    public static final PlayerTrigger FEED_ANIMAL = register("feed_animal", new PlayerTrigger());
    public static final PlayerTrigger TELEPORT_SIMBA = register("teleport_simba", new PlayerTrigger());
    public static final UseGrindingBowlTrigger USE_GRINDING_BOWL =
            register("use_grinding_bowl", new UseGrindingBowlTrigger());
    public static final PlayerTrigger RIDE_GIRAFFE = register("ride_giraffe", new PlayerTrigger());
    public static final PlayerTrigger PLAY_BONGO_DRUM = register("play_bongo_drum", new PlayerTrigger());
    public static final PlayerTrigger ENTER_PRIDE_LANDS = register("enter_pride_lands", new PlayerTrigger());
    public static final PlayerTrigger ENTER_OUTLANDS = register("enter_outlands", new PlayerTrigger());
    public static final PlayerTrigger ENTER_UPENDI = register("enter_upendi", new PlayerTrigger());
    public static final PlayerTrigger BEHEAD_HYENA = register("behead_hyena", new PlayerTrigger());
    public static final PlayerTrigger KILL_SCAR = register("kill_scar", new PlayerTrigger());
    public static final PlayerTrigger KILL_ZIRA = register("kill_zira", new PlayerTrigger());
    public static final PlayerTrigger USE_RHINO_HORN = register("use_rhino_horn", new PlayerTrigger());
    public static final PlayerTrigger SPEAK_TO_ZAZU = register("speak_to_zazu", new PlayerTrigger());
    public static final PlayerTrigger SIMBA_IN_WATER = register("simba_in_water", new PlayerTrigger());

    private static <T extends net.minecraft.advancements.CriterionTrigger<?>> T register(String name, T trigger) {
        return CriteriaTriggers.register("circleofcraft:" + name, trigger);
    }

    public static void register() {}
}
