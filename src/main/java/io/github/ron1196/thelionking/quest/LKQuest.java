package io.github.ron1196.thelionking.quest;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LKQuest {

    private final String id;
    private final String displayName;
    private final Supplier<ItemStack> icon;
    private final List<LKQuestStage> stages;
    private final Predicate<LKQuestManager> canStart;
    private final String[] prerequisites;
    private final Map<Integer, LKQuestTrigger> triggerByStage;
    private final Map<Integer, BiConsumer<ServerPlayer, LKQuestManager>> customTransitions;
    private final Map<Integer, List<ClaimableReward>> claimableRewards;

    private LKQuest(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.icon = builder.icon;
        this.stages = List.copyOf(builder.stages);
        this.canStart = builder.canStart;
        this.prerequisites = builder.prerequisites;
        this.triggerByStage = Map.copyOf(builder.triggerByStage);
        this.customTransitions = Map.copyOf(builder.customTransitions);
        // Deep-copy claimable rewards to unmodifiable lists
        Map<Integer, List<ClaimableReward>> rewardsCopy = new HashMap<>();
        for (Map.Entry<Integer, List<ClaimableReward>> entry : builder.claimableRewards.entrySet()) {
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

    public int getNumStages() {
        return stages.size();
    }

    public LKQuestStage getStage(int index) {
        return stages.get(index);
    }

    public String getObjectiveByStage(int stage) {
        if (stage < 0 || stage >= stages.size()) return "";
        return stages.get(stage).objectiveText();
    }

    public boolean canStart(LKQuestManager manager) {
        return canStart.test(manager);
    }

    public String[] getPrerequisites() {
        return prerequisites;
    }

    public LKQuestTrigger getTriggerForStage(int stage) {
        return triggerByStage.get(stage);
    }

    public BiConsumer<ServerPlayer, LKQuestManager> getCustomTransition(int stage) {
        return customTransitions.get(stage);
    }

    public List<ClaimableReward> getClaimableRewards(int stage) {
        return claimableRewards.getOrDefault(stage, List.of());
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        private final String id;
        private String displayName = "";
        private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
        private final List<LKQuestStage> stages = new ArrayList<>();
        private Predicate<LKQuestManager> canStart = m -> true;
        private String[] prerequisites = null;
        private final Map<Integer, LKQuestTrigger> triggerByStage = new HashMap<>();
        private final Map<Integer, BiConsumer<ServerPlayer, LKQuestManager>> customTransitions = new HashMap<>();
        private final Map<Integer, List<ClaimableReward>> claimableRewards = new HashMap<>();

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

        public Builder stage(LKQuestStage stage) {
            this.stages.add(stage);
            return this;
        }

        public Builder canStart(Predicate<LKQuestManager> predicate) {
            this.canStart = predicate;
            return this;
        }

        public Builder prerequisites(String... prereqs) {
            this.prerequisites = prereqs;
            return this;
        }

        public Builder trigger(int stageIndex, LKQuestTrigger trigger) {
            this.triggerByStage.put(stageIndex, trigger);
            return this;
        }

        public Builder customTransition(int stageIndex, BiConsumer<ServerPlayer, LKQuestManager> action) {
            this.customTransitions.put(stageIndex, action);
            return this;
        }

        public Builder claimableReward(int stageIndex, ClaimableReward reward) {
            this.claimableRewards.computeIfAbsent(stageIndex, k -> new ArrayList<>()).add(reward);
            return this;
        }

        public LKQuest build() {
            return new LKQuest(this);
        }
    }
}
