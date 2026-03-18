package io.github.ron1196.thelionking.quest.questline;

import io.github.ron1196.thelionking.quest.stage.ClaimableReward;
import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static io.github.ron1196.thelionking.quest.stage.StageTrigger.*;
import static io.github.ron1196.thelionking.quest.stage.Stage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage.*;

public class RafikiQuestline {

    public enum Stage implements IStageId {
        FIND_RAFIKI,
        COLLECT_BONES,
        DEFEAT_SCAR,
        RETURN_AFTER_SCAR,
        COLLECT_TERMITES,
        COLLECT_MANGOES,
        USE_STAR_ALTAR,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(Items.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new io.github.ron1196.thelionking.quest.stage.Stage("Find Rafiki and speak to him"))
                .stage(COLLECT_BONES, new io.github.ron1196.thelionking.quest.stage.Stage(
                        "Bring Rafiki 64 hyena bones",
                        List.of(new ItemRequirement(Items.HYENA_BONE, 64)))
                )
                .stage(DEFEAT_SCAR, new io.github.ron1196.thelionking.quest.stage.Stage("Defeat Scar"))
                .stage(RETURN_AFTER_SCAR, new io.github.ron1196.thelionking.quest.stage.Stage("Return to Rafiki"))
                .stage(COLLECT_TERMITES, new io.github.ron1196.thelionking.quest.stage.Stage(
                        "Bring Rafiki 4 ground termites",
                        List.of(new ItemRequirement(Items.TERMITE_DUST, 4)))
                )
                .stage(COLLECT_MANGOES, new io.github.ron1196.thelionking.quest.stage.Stage(
                        "Bring Rafiki 4 ground mangoes",
                        List.of(new ItemRequirement(Items.MANGO_DUST, 4)))
                )
                .stage(USE_STAR_ALTAR, new io.github.ron1196.thelionking.quest.stage.Stage("Craft a Star Altar and use Rafiki Dust on it"))
                .stage(RafikiQuestline.Stage.COMPLETE, new io.github.ron1196.thelionking.quest.stage.Stage("Quest complete"))
                .claimableReward(COLLECT_BONES, new ClaimableReward(Items.RAFIKI_STICK, 1))
                .trigger(FIND_RAFIKI, RAFIKI_TALK)
                .trigger(COLLECT_BONES, RAFIKI_TALK)
                .trigger(DEFEAT_SCAR, SCAR_KILLED)
                .trigger(RETURN_AFTER_SCAR, RAFIKI_TALK)
                .trigger(COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(COLLECT_MANGOES, RAFIKI_TALK)
                .trigger(USE_STAR_ALTAR, STAR_ALTAR_USED)
                .build();
    }
}
