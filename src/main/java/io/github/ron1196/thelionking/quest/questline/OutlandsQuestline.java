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
import net.minecraft.world.item.Items;

public class OutlandsQuestline {

    public enum Stage implements IStageId {
        ENTER_OUTLANDS,
        FIND_ZIRA,
        COLLECT_INGOTS,
        THROW_IN_OUTWATER,
        COLLECT_FEATHERS,
        FOLLOW_OUTLANDERS,
        ZIRA_OCCUPIES_TREE,
        TALK_TO_PUMBAA,
        GATHER_PUMBAA_INGREDIENTS,
        USE_PUMBAA_BOX,
        RAFIKI_RETURNS,
        ZIRA_RETURNS,
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
                .stage(
                        FIND_ZIRA,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Find Zira in the Outlands"))
                .stage(
                        COLLECT_INGOTS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Zira 5 kivulite and 2 silver ingots",
                                List.of(
                                        new ItemRequirement(
                                                LionKingItems.KIVULITE, 5, Source.MAIN_HAND),
                                        new ItemRequirement(
                                                LionKingItems.SILVER_INGOT, 2, Source.INVENTORY))))
                .stage(
                        THROW_IN_OUTWATER,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Throw the ingots into the Outwater"))
                .stage(
                        COLLECT_FEATHERS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Zira 3 wayward feathers",
                                List.of(new ItemRequirement(LionKingItems.WAYWARD_FEATHER, 3))))
                .stage(
                        FOLLOW_OUTLANDERS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Follow the Outlanders to the Pride Lands"))
                .stage(
                        ZIRA_OCCUPIES_TREE,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Zira has taken over Rafiki's tree"))
                .stage(
                        TALK_TO_PUMBAA,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Speak to Timon and Pumbaa"))
                .stage(
                        GATHER_PUMBAA_INGREDIENTS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Pumbaa 16 bugs, planks, a jar of lava, and a thrown termite",
                                List.of(
                                        new ItemRequirement(LionKingItems.BUG, 16, Source.INVENTORY),
                                        new ItemRequirement(
                                                () -> Items.OAK_PLANKS, 1, Source.INVENTORY),
                                        new ItemRequirement(
                                                LionKingItems.JAR_LAVA, 1, Source.INVENTORY),
                                        new ItemRequirement(
                                                LionKingItems.TERMITE_THROWN,
                                                1,
                                                Source.INVENTORY))))
                .stage(
                        USE_PUMBAA_BOX,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Expel the Outlanders from Rafiki's tree"))
                .stage(
                        RAFIKI_RETURNS,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Rafiki returns"))
                .stage(
                        ZIRA_RETURNS,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Return to the Outlands to confront Zira"))
                .stage(
                        DEFEAT_ZIRA,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Defeat Zira"))
                .stage(
                        COMPLETE,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Quest complete"))
                .trigger(OutlandsQuestline.Stage.ENTER_OUTLANDS, StageTrigger.ENTER_OUTLANDS)
                .trigger(FIND_ZIRA, ZIRA_TALK)
                .trigger(COLLECT_INGOTS, ZIRA_TALK)
                .trigger(COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(TALK_TO_PUMBAA, PUMBAA_TALK)
                .trigger(GATHER_PUMBAA_INGREDIENTS, PUMBAA_TALK)
                .trigger(USE_PUMBAA_BOX, PUMBAA_BOX_USED)
                .trigger(ZIRA_RETURNS, ZIRA_SPAWN_EVENT)
                .trigger(DEFEAT_ZIRA, ZIRA_KILLED)
                .build();
    }
}
