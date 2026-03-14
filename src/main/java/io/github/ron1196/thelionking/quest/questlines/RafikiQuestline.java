package io.github.ron1196.thelionking.quest.questlines;

import io.github.ron1196.thelionking.quest.LKClaimableReward;
import io.github.ron1196.thelionking.quest.LKQuestTrigger;
import io.github.ron1196.thelionking.quest.LKQuestline;
import io.github.ron1196.thelionking.quest.LKStage;
import io.github.ron1196.thelionking.quest.LKStageId;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static io.github.ron1196.thelionking.quest.LKQuestTrigger.*;
import static io.github.ron1196.thelionking.quest.LKStage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.questlines.RafikiQuestline.Stage.*;

public class RafikiQuestline {

    public enum Stage implements LKStageId {
        FIND_RAFIKI,
        COLLECT_BONES,
        DEFEAT_SCAR,
        RETURN_AFTER_SCAR,
        COLLECT_TERMITES,
        COLLECT_MANGOES,
        USE_STAR_ALTAR,
        COMPLETE
    }

    public static LKQuestline build() {
        return LKQuestline.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(LKItems.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new LKStage("Find Rafiki and speak to him"))
                .stage(COLLECT_BONES, new LKStage(
                        "Bring Rafiki 64 hyena bones",
                        List.of(new ItemRequirement(LKItems.HYENA_BONE, 64)))
                )
                .stage(DEFEAT_SCAR, new LKStage("Defeat Scar"))
                .stage(RETURN_AFTER_SCAR, new LKStage("Return to Rafiki"))
                .stage(COLLECT_TERMITES, new LKStage(
                        "Bring Rafiki 4 ground termites",
                        List.of(new ItemRequirement(LKItems.TERMITE_DUST, 4)))
                )
                .stage(COLLECT_MANGOES, new LKStage(
                        "Bring Rafiki 4 ground mangoes",
                        List.of(new ItemRequirement(LKItems.MANGO_DUST, 4)))
                )
                .stage(USE_STAR_ALTAR, new LKStage("Craft a Star Altar and use Rafiki Dust on it"))
                .stage(Stage.COMPLETE, new LKStage("Quest complete"))
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
}
