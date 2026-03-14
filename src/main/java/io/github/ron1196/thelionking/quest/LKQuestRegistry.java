package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.ron1196.thelionking.quest.LKQuestStage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.LKQuestStage.Source;
import static io.github.ron1196.thelionking.quest.LKQuestTrigger.*;

public class LKQuestRegistry {

    // ── Rafiki quest stage indices (for use in entity dialogue) ──────────────
    public static final int RAFIKI_NOT_STARTED = 0;
    public static final int RAFIKI_COLLECT_BONES = 1;
    public static final int RAFIKI_DEFEAT_SCAR = 2;
    public static final int RAFIKI_RETURN_AFTER_SCAR = 3;
    public static final int RAFIKI_COLLECT_TERMITES = 4;
    public static final int RAFIKI_COLLECT_MANGOES = 5;
    public static final int RAFIKI_USE_STAR_ALTAR = 6;
    public static final int RAFIKI_COMPLETE = 7;

    // ── Outlands quest stage indices ─────────────────────────────────────────
    public static final int OUTLANDS_NOT_STARTED = 0;
    public static final int OUTLANDS_FIND_ZIRA = 1;
    public static final int OUTLANDS_COLLECT_INGOTS = 2;
    public static final int OUTLANDS_THROW_IN_OUTWATER = 3;
    public static final int OUTLANDS_COLLECT_FEATHERS = 4;
    public static final int OUTLANDS_FOLLOW_OUTLANDERS = 5;
    public static final int OUTLANDS_STAGE_6 = 6;
    public static final int OUTLANDS_STAGE_7 = 7;
    public static final int OUTLANDS_STAGE_8 = 8;
    public static final int OUTLANDS_DEFEAT_ZIRA = 9;
    public static final int OUTLANDS_COMPLETE = 10;

    private static final LinkedHashMap<String, LKQuest> QUESTS = new LinkedHashMap<>();

    public static final LKQuest RAFIKI = buildRafikiQuest();
    public static final LKQuest OUTLANDS = buildOutlandsQuest();

    static {
        QUESTS.put(RAFIKI.getId(), RAFIKI);
        QUESTS.put(OUTLANDS.getId(), OUTLANDS);
    }

    public static LKQuest get(String id) {
        return QUESTS.get(id);
    }

    public static List<LKQuest> getOrdered() {
        return List.copyOf(QUESTS.values());
    }

    public static Map<Integer, String> getLegacyIndexMap() {
        return Map.of(0, "rafiki", 1, "outlands");
    }

    // ── Quest definitions ───────────────────────────────────────────────────

    private static LKQuest buildRafikiQuest() {
        return LKQuest.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(LKItems.RHYTHM_STAFF.get()))
                .stage(new LKQuestStage("Find Rafiki and speak to him"))
                .stage(new LKQuestStage(
                        "Bring Rafiki 64 hyena bones",
                        List.of(new ItemRequirement(() -> LKItems.HYENA_BONE.get(), 64))))
                .stage(new LKQuestStage("Defeat Scar"))
                .stage(new LKQuestStage("Return to Rafiki"))
                .stage(new LKQuestStage(
                        "Bring Rafiki 4 ground termites",
                        List.of(new ItemRequirement(() -> LKItems.TERMITE_DUST.get(), 4))))
                .stage(new LKQuestStage(
                        "Bring Rafiki 4 ground mangoes",
                        List.of(new ItemRequirement(() -> LKItems.MANGO_DUST.get(), 4))))
                .stage(new LKQuestStage("Craft a Star Altar and use Rafiki Dust on it"))
                .claimableReward(1, new ClaimableReward(() -> LKItems.RHYTHM_STAFF.get(), 1, "rafiki:1"))
                .trigger(RAFIKI_NOT_STARTED, RAFIKI_TALK)
                .trigger(RAFIKI_COLLECT_BONES, RAFIKI_TALK)
                .trigger(RAFIKI_DEFEAT_SCAR, SCAR_KILLED)
                .trigger(RAFIKI_RETURN_AFTER_SCAR, RAFIKI_TALK)
                .trigger(RAFIKI_COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(RAFIKI_COLLECT_MANGOES, RAFIKI_TALK)
                .trigger(RAFIKI_USE_STAR_ALTAR, STAR_ALTAR_USED)
                .build();
    }

    private static LKQuest buildOutlandsQuest() {
        return LKQuest.builder("outlands")
                .displayName("An Outlandish Scheme")
                .icon(() -> new ItemStack(LKItems.WAYWARD_FEATHER.get()))
                .canStart(manager -> manager.isComplete("rafiki"))
                .prerequisites("Complete Rafiki's Quest")
                .stage(new LKQuestStage("Enter the Outlands"))
                .stage(new LKQuestStage("Find Zira in the Outlands"))
                .stage(new LKQuestStage(
                        "Bring Zira 5 kivulite and 2 silver ingots",
                        List.of(
                                new ItemRequirement(() -> LKItems.KIVULITE.get(), 5, Source.MAIN_HAND),
                                new ItemRequirement(() -> LKItems.SILVER_INGOT.get(), 2, Source.INVENTORY))))
                .stage(new LKQuestStage("Throw the ingots into the Outwater"))
                .stage(new LKQuestStage(
                        "Bring Zira 3 wayward feathers",
                        List.of(new ItemRequirement(() -> LKItems.WAYWARD_FEATHER.get(), 3))))
                .stage(new LKQuestStage("Follow the Outlanders"))
                .stage(new LKQuestStage("Continue exploring the Outlands"))
                .stage(new LKQuestStage("Continue exploring the Outlands"))
                .stage(new LKQuestStage("Continue exploring the Outlands"))
                .stage(new LKQuestStage("Defeat Zira"))
                .trigger(OUTLANDS_NOT_STARTED, ENTER_OUTLANDS)
                .trigger(OUTLANDS_FIND_ZIRA, ZIRA_TALK)
                .trigger(OUTLANDS_COLLECT_INGOTS, ZIRA_TALK)
                .trigger(OUTLANDS_COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(OUTLANDS_DEFEAT_ZIRA, ZIRA_KILLED)
                .build();
    }
}
