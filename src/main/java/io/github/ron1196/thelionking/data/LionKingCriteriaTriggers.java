package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

public class LionKingCriteriaTriggers {

    public static final PlayerTrigger SHOOT_DART = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "shoot_dart"));
    public static final PlayerTrigger QUEST_COMPLETE = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "quest_complete"));
    public static final PlayerTrigger USE_GRINDING_BOWL = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "use_grinding_bowl"));
    public static final PlayerTrigger RIDE_GIRAFFE = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "ride_giraffe"));
    public static final PlayerTrigger PLAY_BONGO_DRUM = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "play_bongo_drum"));
    public static final PlayerTrigger ENTER_PRIDE_LANDS = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "enter_pride_lands"));
    public static final PlayerTrigger ENTER_OUTLANDS = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "enter_outlands"));
    public static final PlayerTrigger ENTER_UPENDI = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "enter_upendi"));
    public static final PlayerTrigger BEHEAD_HYENA = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "behead_hyena"));
    public static final PlayerTrigger KILL_SCAR = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "kill_scar"));
    public static final PlayerTrigger KILL_ZIRA = new PlayerTrigger(
            new ResourceLocation(TheLionKingMod.MOD_ID, "kill_zira"));

    public static void register() {
        CriteriaTriggers.register(SHOOT_DART);
        CriteriaTriggers.register(QUEST_COMPLETE);
        CriteriaTriggers.register(USE_GRINDING_BOWL);
        CriteriaTriggers.register(RIDE_GIRAFFE);
        CriteriaTriggers.register(PLAY_BONGO_DRUM);
        CriteriaTriggers.register(ENTER_PRIDE_LANDS);
        CriteriaTriggers.register(ENTER_OUTLANDS);
        CriteriaTriggers.register(ENTER_UPENDI);
        CriteriaTriggers.register(BEHEAD_HYENA);
        CriteriaTriggers.register(KILL_SCAR);
        CriteriaTriggers.register(KILL_ZIRA);
    }
}
