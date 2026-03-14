package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;

import static io.github.ron1196.thelionking.quest.LKQuestStage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.LKQuestStage.Source;
import static io.github.ron1196.thelionking.quest.LKQuestTrigger.*;
import static io.github.ron1196.thelionking.quest.RafikiStage.*;
import static io.github.ron1196.thelionking.quest.OutlandsStage.*;

public class LKQuestRegistry {

    private static final LinkedHashMap<String, LKQuestline> QUESTS = new LinkedHashMap<>();

    public static final LKQuestline RAFIKI = buildRafikiQuest();
    public static final LKQuestline OUTLANDS = buildOutlandsQuest();

    static {
        QUESTS.put(RAFIKI.getId(), RAFIKI);
        QUESTS.put(OUTLANDS.getId(), OUTLANDS);
    }

    public static LKQuestline get(String id) {
        return QUESTS.get(id);
    }

    public static List<LKQuestline> getOrdered() {
        return List.copyOf(QUESTS.values());
    }

    // -- Quest definitions --------------------------------------------------

    private static LKQuestline buildRafikiQuest() {
        return LKQuestline.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(LKItems.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new LKQuestStage("Find Rafiki and speak to him"))
                .stage(COLLECT_BONES, new LKQuestStage(
                        "Bring Rafiki 64 hyena bones",
                        List.of(new ItemRequirement(LKItems.HYENA_BONE, 64)))
                )
                .stage(DEFEAT_SCAR, new LKQuestStage("Defeat Scar"))
                .stage(RETURN_AFTER_SCAR, new LKQuestStage("Return to Rafiki"))
                .stage(COLLECT_TERMITES, new LKQuestStage(
                        "Bring Rafiki 4 ground termites",
                        List.of(new ItemRequirement(LKItems.TERMITE_DUST, 4)))
                )
                .stage(COLLECT_MANGOES, new LKQuestStage(
                        "Bring Rafiki 4 ground mangoes",
                        List.of(new ItemRequirement(LKItems.MANGO_DUST, 4)))
                )
                .stage(USE_STAR_ALTAR, new LKQuestStage("Craft a Star Altar and use Rafiki Dust on it"))
                .stage(RafikiStage.COMPLETE, new LKQuestStage("Quest complete"))
                .claimableReward(COLLECT_BONES, new LKClaimableReward(LKItems.RAFIKI_STICK, 1))
                .trigger(FIND_RAFIKI, RAFIKI_TALK)
                .trigger(COLLECT_BONES, RAFIKI_TALK)
                .trigger(DEFEAT_SCAR, SCAR_KILLED)
                .trigger(RETURN_AFTER_SCAR, RAFIKI_TALK)
                .trigger(COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(COLLECT_MANGOES, RAFIKI_TALK)
                .trigger(USE_STAR_ALTAR, STAR_ALTAR_USED)
                .build();
    }

    private static LKQuestline buildOutlandsQuest() {
        return LKQuestline.builder("outlands")
                .displayName("An Outlandish Scheme")
                .icon(() -> new ItemStack(LKItems.WAYWARD_FEATHER.get()))
                .canStart(manager -> manager.isComplete("rafiki"))
                .prerequisites("Complete Rafiki's Quest")
                .stage(OutlandsStage.ENTER_OUTLANDS, new LKQuestStage("Enter the Outlands"))
                .stage(OutlandsStage.FIND_ZIRA, new LKQuestStage("Find Zira in the Outlands"))
                .stage(OutlandsStage.COLLECT_INGOTS, new LKQuestStage(
                        "Bring Zira 5 kivulite and 2 silver ingots",
                        List.of(
                                new ItemRequirement(LKItems.KIVULITE, 5, Source.MAIN_HAND),
                                new ItemRequirement(LKItems.SILVER_INGOT, 2, Source.INVENTORY)))
                )
                .stage(OutlandsStage.THROW_IN_OUTWATER, new LKQuestStage("Throw the ingots into the Outwater"))
                .stage(OutlandsStage.COLLECT_FEATHERS, new LKQuestStage(
                        "Bring Zira 3 wayward feathers",
                        List.of(new ItemRequirement(LKItems.WAYWARD_FEATHER, 3)))
                )
                .stage(OutlandsStage.FOLLOW_OUTLANDERS, new LKQuestStage("Follow the Outlanders"))
                .stage(OutlandsStage.STAGE_6, new LKQuestStage("Continue exploring the Outlands"))
                .stage(OutlandsStage.STAGE_7, new LKQuestStage("Continue exploring the Outlands"))
                .stage(OutlandsStage.STAGE_8, new LKQuestStage("Continue exploring the Outlands"))
                .stage(OutlandsStage.DEFEAT_ZIRA, new LKQuestStage("Defeat Zira"))
                .stage(OutlandsStage.COMPLETE, new LKQuestStage("Quest complete"))
                .trigger(OutlandsStage.ENTER_OUTLANDS, LKQuestTrigger.ENTER_OUTLANDS)
                .trigger(OutlandsStage.FIND_ZIRA, ZIRA_TALK)
                .trigger(OutlandsStage.COLLECT_INGOTS, ZIRA_TALK)
                .trigger(OutlandsStage.COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(OutlandsStage.DEFEAT_ZIRA, ZIRA_KILLED)
                .build();
    }
}
