package io.github.ron1196.thelionking.quest.questline;

import io.github.ron1196.thelionking.quest.stage.ClaimableReward;
import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.quest.stage.LKQuestTrigger;
import io.github.ron1196.thelionking.quest.stage.LKStage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LKQuestline {

    private final String id;
    private final String displayName;
    private final Supplier<ItemStack> icon;
    private final List<IStageId> stageOrder;
    private final Map<IStageId, LKStage> stageData;
    private final Predicate<LKQuestlineManager> canStart;
    private final String[] prerequisites;
    private final Map<IStageId, LKQuestTrigger> triggerByStage;
    private final Map<IStageId, BiConsumer<ServerPlayer, LKQuestlineManager>> customTransitions;
    private final Map<IStageId, List<ClaimableReward>> claimableRewards;

    private LKQuestline(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.icon = builder.icon;
        this.stageOrder = List.copyOf(builder.stageOrder);

        this.stageData = Map.copyOf(builder.stageData);
        this.canStart = builder.canStart;
        this.prerequisites = builder.prerequisites;
        this.triggerByStage = Map.copyOf(builder.triggerByStage);
        this.customTransitions = Map.copyOf(builder.customTransitions);

        Map<IStageId, List<ClaimableReward>> rewardsCopy = new HashMap<>();
        for (Map.Entry<IStageId, List<ClaimableReward>> entry : builder.claimableRewards.entrySet()) {
            rewardsCopy.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        this.claimableRewards = Collections.unmodifiableMap(rewardsCopy);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getIcon() {
        return icon.get();
    }

    public List<IStageId> getStageOrder() {
        return stageOrder;
    }

    public int getNumStages() {
        return stageOrder.size();
    }

    @Nullable
    public LKStage getStageData(IStageId stage) {
        return stageData.get(stage);
    }

    public String getObjectiveByStage(IStageId stage) {
        LKStage data = stageData.get(stage);
        return data != null ? data.objectiveText() : "";
    }

    /**
     * Get the objective text for a stage identified by its string name.
     */
    public String getObjectiveByStageId(String stageId) {
        IStageId stage = findStageByName(stageId);
        return stage != null ? getObjectiveByStage(stage) : "";
    }

    public boolean canStart(LKQuestlineManager manager) {
        return canStart.test(manager);
    }

    public String[] getPrerequisites() {
        return prerequisites;
    }

    @Nullable
    public LKQuestTrigger getTriggerForStage(IStageId stage) {
        return triggerByStage.get(stage);
    }

    @Nullable
    public BiConsumer<ServerPlayer, LKQuestlineManager> getCustomTransition(IStageId stage) {
        return customTransitions.get(stage);
    }

    public List<ClaimableReward> getClaimableRewards(IStageId stage) {
        return claimableRewards.getOrDefault(stage, List.of());
    }

    /**
     * Returns the first stage in this questline's progression.
     */
    public IStageId getFirstStage() {
        return stageOrder.get(0);
    }

    /**
     * Returns the next stage after {@code current}, or null if current is the last stage.
     */
    @Nullable
    public IStageId getNextStage(IStageId current) {
        int idx = stageOrder.indexOf(current);
        if (idx < 0 || idx >= stageOrder.size() - 1) return null;
        return stageOrder.get(idx + 1);
    }

    /**
     * Returns true if the given stage is the last stage in this questline.
     */
    public boolean isLastStage(IStageId stage) {
        return !stageOrder.isEmpty() && stageOrder.get(stageOrder.size() - 1).equals(stage);
    }

    /**
     * Returns true if the given stageId corresponds to the last stage (COMPLETE).
     */
    public boolean isComplete(String stageId) {
        if (stageId.isEmpty() || stageOrder.isEmpty()) return false;
        return stageOrder.get(stageOrder.size() - 1).name().equals(stageId);
    }

    /**
     * Returns true if the stageId is non-empty (quest has been initialized).
     */
    public boolean isStarted(String stageId) {
        return !stageId.isEmpty();
    }

    /**
     * Returns the index of a stage by its string name, or -1 if not found.
     */
    public int getStageIndex(String stageId) {
        if (stageId.isEmpty()) return -1;
        for (int i = 0; i < stageOrder.size(); i++) {
            if (stageOrder.get(i).name().equals(stageId)) return i;
        }
        return -1;
    }

    /**
     * Returns the index of a stage in the progression order, or -1 if not found.
     */
    public int getStageIndex(IStageId stage) {
        return stageOrder.indexOf(stage);
    }

    /**
     * Returns true if {@code current} is at or past {@code target} in the stage order.
     */
    public boolean isAtOrPast(String currentStageId, IStageId target) {
        int currentIdx = getStageIndex(currentStageId);
        int targetIdx = getStageIndex(target);
        return currentIdx >= 0 && targetIdx >= 0 && currentIdx >= targetIdx;
    }

    /**
     * Find a stage enum value by its string name.
     */
    @Nullable
    public IStageId findStageByName(String stageId) {
        if (stageId.isEmpty()) return null;
        for (IStageId stage : stageOrder) {
            if (stage.name().equals(stageId)) return stage;
        }
        return null;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        private final String id;
        private String displayName = "";
        private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
        private final List<IStageId> stageOrder = new ArrayList<>();
        private final Map<IStageId, LKStage> stageData = new LinkedHashMap<>();
        private Predicate<LKQuestlineManager> canStart = m -> true;
        private String[] prerequisites = null;
        private final Map<IStageId, LKQuestTrigger> triggerByStage = new HashMap<>();
        private final Map<IStageId, BiConsumer<ServerPlayer, LKQuestlineManager>> customTransitions = new HashMap<>();
        private final Map<IStageId, List<ClaimableReward>> claimableRewards = new HashMap<>();

        private Builder(String id) {
            this.id = id;
        }

        public Builder displayName(String name) {
            this.displayName = name;
            return this;
        }

        public Builder icon(Supplier<ItemStack> icon) {
            this.icon = icon;
            return this;
        }

        public Builder stage(IStageId stageId, LKStage data) {
            this.stageOrder.add(stageId);
            this.stageData.put(stageId, data);
            return this;
        }

        public Builder canStart(Predicate<LKQuestlineManager> predicate) {
            this.canStart = predicate;
            return this;
        }

        public Builder prerequisites(String... prereqs) {
            this.prerequisites = prereqs;
            return this;
        }

        public Builder trigger(IStageId stage, LKQuestTrigger trigger) {
            this.triggerByStage.put(stage, trigger);
            return this;
        }

        public Builder customTransition(IStageId stage, BiConsumer<ServerPlayer, LKQuestlineManager> action) {
            this.customTransitions.put(stage, action);
            return this;
        }

        public Builder claimableReward(IStageId stage, ClaimableReward reward) {
            this.claimableRewards.computeIfAbsent(stage, k -> new ArrayList<>()).add(reward);
            return this;
        }

        public LKQuestline build() {
            return new LKQuestline(this);
        }
    }
}
