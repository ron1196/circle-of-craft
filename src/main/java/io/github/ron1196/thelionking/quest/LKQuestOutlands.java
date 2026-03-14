package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class LKQuestOutlands extends LKQuestBase {

    // Stage constants
    public static final int NOT_STARTED = 0;
    public static final int FIND_ZIRA = 1;
    public static final int COLLECT_INGOTS = 2;
    public static final int THROW_IN_OUTWATER = 3;
    public static final int COLLECT_FEATHERS = 4;
    public static final int FOLLOW_OUTLANDERS = 5;
    public static final int STAGE_6 = 6;
    public static final int STAGE_7 = 7;
    public static final int STAGE_8 = 8;
    public static final int DEFEAT_ZIRA = 9;
    public static final int COMPLETE = 10;

    public LKQuestOutlands(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return LKQuests.RAFIKI_QUEST.isComplete();
    }

    @Override
    public String[] getRequirements() {
        return new String[]{"Complete Rafiki's Quest"};
    }

    @Override
    public int getNumStages() {
        return 10;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.WAYWARD_FEATHER.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "Enter the Outlands";
            case FIND_ZIRA -> "Find Zira in the Outlands";
            case COLLECT_INGOTS -> "Bring Zira 5 kivulite and 2 silver ingots";
            case THROW_IN_OUTWATER -> "Throw the ingots into the Outwater";
            case COLLECT_FEATHERS -> "Bring Zira 3 wayward feathers";
            case FOLLOW_OUTLANDERS -> "Follow the Outlanders";
            case STAGE_6, STAGE_7, STAGE_8 -> "Continue exploring the Outlands";
            case DEFEAT_ZIRA -> "Defeat Zira";
            case COMPLETE -> "Quest complete!";
            default -> "";
        };
    }

    /**
     * Attempts to advance the quest based on current stage and context.
     */
    public boolean tryAdvanceStage(ServerPlayer player, LKLevelData data, String context) {
        int stage = getQuestStage();

        switch (stage) {
            case NOT_STARTED:
                if ("enter_outlands".equals(context)
                        && player.level().dimension() == LKDimensions.OUTLANDS_LEVEL) {
                    progress(FIND_ZIRA);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;

            case FIND_ZIRA:
                if ("zira_talk".equals(context)) {
                    progress(COLLECT_INGOTS);
                    LKQuestBase.updateAllQuests();
                    data.setDirty();
                    return true;
                }
                break;

            case COLLECT_INGOTS:
                if ("zira_talk".equals(context)) {
                    ItemStack held = player.getMainHandItem();
                    if (held.is(LKItems.KIVULITE.get()) && held.getCount() >= 5) {
                        for (ItemStack stack : player.getInventory().items) {
                            if (stack.is(LKItems.SILVER_INGOT.get()) && stack.getCount() >= 2) {
                                held.shrink(5);
                                stack.shrink(2);
                                progress(THROW_IN_OUTWATER);
                                LKQuestBase.updateAllQuests();
                                data.setDirty();
                                return true;
                            }
                        }
                    }
                }
                break;

            case COLLECT_FEATHERS:
                if ("zira_talk".equals(context)) {
                    ItemStack held = player.getMainHandItem();
                    if (held.is(LKItems.WAYWARD_FEATHER.get()) && held.getCount() >= 3) {
                        held.shrink(3);
                        progress(FOLLOW_OUTLANDERS);
                        LKQuestBase.updateAllQuests();
                        data.setDirty();
                        return true;
                    }
                }
                break;

            case DEFEAT_ZIRA:
                if ("zira_killed".equals(context)) {
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
