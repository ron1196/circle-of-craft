package io.github.ron1196.circleofcraft.data;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCriteriaTriggers {

    public static final PlayerTrigger SHOOT_DART = new PlayerTrigger();
    public static final PlayerTrigger QUEST_COMPLETE = new PlayerTrigger();
    public static final PlayerTrigger COMPLETE_RAFIKI_QUEST = new PlayerTrigger();
    public static final PlayerTrigger TRADE_PUMBAA = new PlayerTrigger();
    public static final PlayerTrigger FEED_ANIMAL = new PlayerTrigger();
    public static final PlayerTrigger TELEPORT_SIMBA = new PlayerTrigger();
    public static final UseGrindingBowlTrigger USE_GRINDING_BOWL = new UseGrindingBowlTrigger();
    public static final PlayerTrigger RIDE_GIRAFFE = new PlayerTrigger();
    public static final PlayerTrigger PLAY_BONGO_DRUM = new PlayerTrigger();
    public static final PlayerTrigger ENTER_PRIDE_LANDS = new PlayerTrigger();
    public static final PlayerTrigger ENTER_OUTLANDS = new PlayerTrigger();
    public static final PlayerTrigger ENTER_UPENDI = new PlayerTrigger();
    public static final PlayerTrigger BEHEAD_HYENA = new PlayerTrigger();
    public static final PlayerTrigger KILL_SCAR = new PlayerTrigger();
    public static final PlayerTrigger KILL_ZIRA = new PlayerTrigger();
    public static final PlayerTrigger USE_RHINO_HORN = new PlayerTrigger();
    public static final PlayerTrigger SPEAK_TO_ZAZU = new PlayerTrigger();
    public static final PlayerTrigger SIMBA_IN_WATER = new PlayerTrigger();

    @SubscribeEvent
    static void register(RegisterEvent event) {
        event.register(Registries.TRIGGER_TYPE, helper -> {
            helper.register(CircleOfCraftMod.id("shoot_dart"), SHOOT_DART);
            helper.register(CircleOfCraftMod.id("quest_complete"), QUEST_COMPLETE);
            helper.register(CircleOfCraftMod.id("complete_rafiki_quest"), COMPLETE_RAFIKI_QUEST);
            helper.register(CircleOfCraftMod.id("trade_pumbaa"), TRADE_PUMBAA);
            helper.register(CircleOfCraftMod.id("feed_animal"), FEED_ANIMAL);
            helper.register(CircleOfCraftMod.id("teleport_simba"), TELEPORT_SIMBA);
            helper.register(CircleOfCraftMod.id("use_grinding_bowl"), USE_GRINDING_BOWL);
            helper.register(CircleOfCraftMod.id("ride_giraffe"), RIDE_GIRAFFE);
            helper.register(CircleOfCraftMod.id("play_bongo_drum"), PLAY_BONGO_DRUM);
            helper.register(CircleOfCraftMod.id("enter_pride_lands"), ENTER_PRIDE_LANDS);
            helper.register(CircleOfCraftMod.id("enter_outlands"), ENTER_OUTLANDS);
            helper.register(CircleOfCraftMod.id("enter_upendi"), ENTER_UPENDI);
            helper.register(CircleOfCraftMod.id("behead_hyena"), BEHEAD_HYENA);
            helper.register(CircleOfCraftMod.id("kill_scar"), KILL_SCAR);
            helper.register(CircleOfCraftMod.id("kill_zira"), KILL_ZIRA);
            helper.register(CircleOfCraftMod.id("use_rhino_horn"), USE_RHINO_HORN);
            helper.register(CircleOfCraftMod.id("speak_to_zazu"), SPEAK_TO_ZAZU);
            helper.register(CircleOfCraftMod.id("simba_in_water"), SIMBA_IN_WATER);
        });
    }
}
