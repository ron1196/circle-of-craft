package io.github.ron1196.thelionking.quest.questline;

import static io.github.ron1196.thelionking.quest.questline.OutlandsQuestline.Stage.*;
import static io.github.ron1196.thelionking.quest.stage.Stage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.stage.Stage.Source;
import static io.github.ron1196.thelionking.quest.stage.StageTrigger.*;

import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.quest.stage.StageTrigger;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class OutlandsQuestline {

    public enum Stage implements IStageId {
        ENTER_OUTLANDS,
        FIND_ZIRA,
        COLLECT_INGOTS,
        THROW_IN_OUTWATER,
        COLLECT_FEATHERS,
        FOLLOW_OUTLANDERS,
        STAGE_6,
        STAGE_7,
        STAGE_8,
        DEFEAT_ZIRA,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder("outlands")
                .displayName("An Outlandish Scheme")
                .icon(() -> new ItemStack(LionKingItems.WAYWARD_FEATHER.get()))
                .canStart(manager -> manager.isComplete("rafiki"))
                .prerequisites("Complete Rafiki's Quest")
                .stage(
                        OutlandsQuestline.Stage.ENTER_OUTLANDS,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Enter the Outlands"))
                .stage(FIND_ZIRA, new io.github.ron1196.thelionking.quest.stage.Stage("Find Zira in the Outlands"))
                .stage(
                        COLLECT_INGOTS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Zira 5 kivulite and 2 silver ingots",
                                List.of(
                                        new ItemRequirement(LionKingItems.KIVULITE, 5, Source.MAIN_HAND),
                                        new ItemRequirement(LionKingItems.SILVER_INGOT, 2, Source.INVENTORY))))
                .stage(
                        THROW_IN_OUTWATER,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Throw the ingots into the Outwater"))
                .stage(
                        COLLECT_FEATHERS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Zira 3 wayward feathers",
                                List.of(new ItemRequirement(LionKingItems.WAYWARD_FEATHER, 3))))
                .stage(FOLLOW_OUTLANDERS, new io.github.ron1196.thelionking.quest.stage.Stage("Follow the Outlanders"))
                .stage(STAGE_6, new io.github.ron1196.thelionking.quest.stage.Stage("Continue exploring the Outlands"))
                .stage(STAGE_7, new io.github.ron1196.thelionking.quest.stage.Stage("Continue exploring the Outlands"))
                .stage(STAGE_8, new io.github.ron1196.thelionking.quest.stage.Stage("Continue exploring the Outlands"))
                .stage(DEFEAT_ZIRA, new io.github.ron1196.thelionking.quest.stage.Stage("Defeat Zira"))
                .stage(
                        OutlandsQuestline.Stage.COMPLETE,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Quest complete"))
                .trigger(OutlandsQuestline.Stage.ENTER_OUTLANDS, StageTrigger.ENTER_OUTLANDS)
                .trigger(FIND_ZIRA, ZIRA_TALK)
                .trigger(COLLECT_INGOTS, ZIRA_TALK)
                .trigger(COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(DEFEAT_ZIRA, ZIRA_KILLED)
                .build();
    }
}
