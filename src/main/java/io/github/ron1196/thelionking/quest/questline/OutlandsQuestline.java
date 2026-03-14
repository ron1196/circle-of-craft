package io.github.ron1196.thelionking.quest.questline;

import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.quest.stage.LKQuestTrigger;
import io.github.ron1196.thelionking.quest.stage.LKStage;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static io.github.ron1196.thelionking.quest.stage.LKQuestTrigger.*;
import static io.github.ron1196.thelionking.quest.stage.LKStage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.stage.LKStage.Source;
import static io.github.ron1196.thelionking.quest.questline.OutlandsQuestline.Stage.*;

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

    public static LKQuestline build() {
        return LKQuestline.builder("outlands")
                .displayName("An Outlandish Scheme")
                .icon(() -> new ItemStack(LKItems.WAYWARD_FEATHER.get()))
                .canStart(manager -> manager.isComplete("rafiki"))
                .prerequisites("Complete Rafiki's Quest")
                .stage(Stage.ENTER_OUTLANDS, new LKStage("Enter the Outlands"))
                .stage(FIND_ZIRA, new LKStage("Find Zira in the Outlands"))
                .stage(COLLECT_INGOTS, new LKStage(
                        "Bring Zira 5 kivulite and 2 silver ingots",
                        List.of(
                                new ItemRequirement(LKItems.KIVULITE, 5, Source.MAIN_HAND),
                                new ItemRequirement(LKItems.SILVER_INGOT, 2, Source.INVENTORY)))
                )
                .stage(THROW_IN_OUTWATER, new LKStage("Throw the ingots into the Outwater"))
                .stage(COLLECT_FEATHERS, new LKStage(
                        "Bring Zira 3 wayward feathers",
                        List.of(new ItemRequirement(LKItems.WAYWARD_FEATHER, 3)))
                )
                .stage(FOLLOW_OUTLANDERS, new LKStage("Follow the Outlanders"))
                .stage(STAGE_6, new LKStage("Continue exploring the Outlands"))
                .stage(STAGE_7, new LKStage("Continue exploring the Outlands"))
                .stage(STAGE_8, new LKStage("Continue exploring the Outlands"))
                .stage(DEFEAT_ZIRA, new LKStage("Defeat Zira"))
                .stage(Stage.COMPLETE, new LKStage("Quest complete"))
                .trigger(Stage.ENTER_OUTLANDS, LKQuestTrigger.ENTER_OUTLANDS)
                .trigger(FIND_ZIRA, ZIRA_TALK)
                .trigger(COLLECT_INGOTS, ZIRA_TALK)
                .trigger(COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(DEFEAT_ZIRA, ZIRA_KILLED)
                .build();
    }
}
