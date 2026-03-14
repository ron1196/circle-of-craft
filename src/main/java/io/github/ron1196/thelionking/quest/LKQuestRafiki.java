package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LKQuestRafiki extends LKQuestBase {

    // Stage constants
    public static final int NOT_STARTED = 0;
    public static final int COLLECT_BONES = 1;
    public static final int DEFEAT_SCAR = 2;
    public static final int RETURN_AFTER_SCAR = 3;
    public static final int COLLECT_TERMITES = 4;
    public static final int COLLECT_MANGOES = 5;
    public static final int USE_STAR_ALTAR = 6;
    public static final int COMPLETE = 7;

    public LKQuestRafiki(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public String[] getRequirements() {
        return null;
    }

    @Override
    public int getNumStages() {
        return 7;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.RHYTHM_STAFF.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "Find Rafiki and speak to him";
            case COLLECT_BONES -> "Bring Rafiki 64 hyena bones";
            case DEFEAT_SCAR -> "Defeat Scar";
            case RETURN_AFTER_SCAR -> "Return to Rafiki";
            case COLLECT_TERMITES -> "Bring Rafiki 4 ground termites";
            case COLLECT_MANGOES -> "Bring Rafiki 4 ground mangoes";
            case USE_STAR_ALTAR -> "Craft a Star Altar and use Rafiki Dust on it";
            case COMPLETE -> "Quest complete!";
            default -> "";
        };
    }

    /**
     * Attempts to advance the quest based on current stage and context.
     * Called from NPC interaction handlers and event handlers.
     *
     * @param context the trigger context (e.g., "rafiki_talk", "scar_killed")
     */
    public boolean tryAdvanceStage(ServerPlayer player, LKLevelData data, String context) {
        int stage = getQuestStage();

        switch (stage) {
            case NOT_STARTED:
                if ("rafiki_talk".equals(context)) {
                    progress(COLLECT_BONES);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;

            case COLLECT_BONES:
                if ("rafiki_talk".equals(context)) {
                    ItemStack held = player.getMainHandItem();
                    if (held.is(LKItems.HYENA_BONE.get()) && held.getCount() >= 64) {
                        held.shrink(64);
                        player.addItem(new ItemStack(LKItems.RHYTHM_STAFF.get()));
                        progress(DEFEAT_SCAR);
                        LKQuestBase.updateAllQuests();
                        data.setDirty();
                        return true;
                    }
                }
                break;

            case DEFEAT_SCAR:
                if ("scar_killed".equals(context)) {
                    progress(RETURN_AFTER_SCAR);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;

            case RETURN_AFTER_SCAR:
                if ("rafiki_talk".equals(context)) {
                    progress(COLLECT_TERMITES);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;

            case COLLECT_TERMITES:
                if ("rafiki_talk".equals(context)) {
                    ItemStack held = player.getMainHandItem();
                    if (held.is(LKItems.TERMITE_DUST.get()) && held.getCount() >= 4) {
                        held.shrink(4);
                        progress(COLLECT_MANGOES);
                        LKQuestBase.updateAllQuests();
                        data.setDirty();
                        return true;
                    }
                }
                break;

            case COLLECT_MANGOES:
                if ("rafiki_talk".equals(context)) {
                    ItemStack held = player.getMainHandItem();
                    if (held.is(LKItems.MANGO_DUST.get()) && held.getCount() >= 4) {
                        held.shrink(4);
                        progress(USE_STAR_ALTAR);
                        LKQuestBase.updateAllQuests();
                        data.setDirty();
                        return true;
                    }
                }
                break;

            case USE_STAR_ALTAR:
                if ("star_altar_used".equals(context)) {
                    progress(COMPLETE);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;
        }
        return false;
    }
}
