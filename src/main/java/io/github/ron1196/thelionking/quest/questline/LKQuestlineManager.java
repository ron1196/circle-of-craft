package io.github.ron1196.thelionking.quest.questline;

import io.github.ron1196.thelionking.data.LKPlayerData;
import io.github.ron1196.thelionking.data.LKPlayerDataProvider;
import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.QuestSyncPacket;
import io.github.ron1196.thelionking.quest.stage.ClaimableReward;
import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.quest.stage.LKQuestTrigger;
import io.github.ron1196.thelionking.quest.stage.LKStage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class LKQuestlineManager {

    private final Map<String, LKQuestlineState> states = new HashMap<>();
    private final SavedData owner;

    public LKQuestlineManager(SavedData owner) {
        this.owner = owner;
        for (LKQuestline quest : LKQuestRegistry.getOrdered()) {
            states.put(quest.getId(), new LKQuestlineState());
        }
    }

    public LKQuestlineState getState(String questId) {
        return states.computeIfAbsent(questId, k -> new LKQuestlineState());
    }

    /**
     * Returns the raw stage ID string for the given quest.
     * Empty string means the quest has not been initialized yet.
     */
    public String getStageId(String questId) {
        return getState(questId).getCurrentStageId();
    }

    /**
     * Returns the typed enum stage for the given quest.
     * If the quest has not been initialized (empty stageId), returns the first stage.
     */
    public <T extends Enum<T> & IStageId> T getStage(String questId, Class<T> stageClass) {
        String stageId = getStageId(questId);
        if (stageId.isEmpty()) {
            return stageClass.getEnumConstants()[0];
        }
        return Enum.valueOf(stageClass, stageId);
    }

    /**
     * Resolves the effective stage for a quest. If the stageId is empty, returns the first stage
     * in the questline's stage order.
     */
    @Nullable
    private IStageId resolveCurrentStage(String questId) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return null;
        String stageId = getStageId(questId);
        if (stageId.isEmpty()) {
            return quest.getFirstStage();
        }
        return quest.findStageByName(stageId);
    }

    public boolean isComplete(String questId) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return false;
        String stageId = getStageId(questId);
        return quest.isComplete(stageId);
    }

    /**
     * Returns true if the current stage for {@code questId} is at or past {@code target}
     * in the questline's stage order.
     */
    public boolean isStageAtOrPast(String questId, IStageId target) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return false;
        String stageId = getStageId(questId);
        if (stageId.isEmpty()) {
            // Not initialized — resolve to first stage
            return quest.getStageIndex(quest.getFirstStage()) >= quest.getStageIndex(target);
        }
        return quest.isAtOrPast(stageId, target);
    }

    public boolean canStart(String questId) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return false;
        return quest.canStart(this);
    }

    public boolean tryAdvance(String questId, ServerPlayer player, LKQuestTrigger trigger) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return false;

        if (!quest.canStart(this)) return false;

        LKQuestlineState state = getState(questId);
        IStageId currentStage = resolveCurrentStage(questId);
        if (currentStage == null) return false;

        // Already at the last stage (complete) — can't advance further
        if (quest.isLastStage(currentStage)) return false;

        LKQuestTrigger expected = quest.getTriggerForStage(currentStage);
        if (expected == null || expected != trigger) return false;

        LKStage stageDef = quest.getStageData(currentStage);
        if (stageDef == null) return false;

        if (!checkRequirements(player, stageDef.requirements())) return false;
        consumeRequirements(player, stageDef.requirements());

        BiConsumer<ServerPlayer, LKQuestlineManager> custom = quest.getCustomTransition(currentStage);
        if (custom != null) {
            custom.accept(player, this);
        }

        // Advance to the next stage
        IStageId nextStage = quest.getNextStage(currentStage);
        if (nextStage != null) {
            state.setCurrentStageId(nextStage.name());
        }
        state.setChecked(false);
        owner.setDirty();

        // Give claimable rewards for the completed stage
        claimRewards(quest, currentStage, player);

        syncToAllPlayers(player.server);

        return true;
    }

    public int tryClaimNextReward(String questId, ServerPlayer player) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return -1;
        LKPlayerData playerData = LKPlayerDataProvider.get(player);
        String currentStageId = getStageId(questId);
        List<IStageId> stages = quest.getStageOrder();
        int currentIndex = quest.getStageIndex(currentStageId);

        // Iterate through completed stages (before the current one)
        for (int i = 0; i < currentIndex; i++) {
            IStageId stage = stages.get(i);
            String rewardKey = questId + ":" + stage.name();
            if (playerData.hasClaimedReward(rewardKey)) continue;
            List<ClaimableReward> rewards = quest.getClaimableRewards(stage);
            if (rewards.isEmpty()) continue;
            for (ClaimableReward reward : rewards) {
                player.addItem(new ItemStack(reward.item().get(), reward.count()));
            }
            playerData.claimReward(rewardKey);
            return i;
        }
        return -1;
    }

    private void claimRewards(LKQuestline quest, IStageId completedStage, ServerPlayer player) {
        List<ClaimableReward> rewards = quest.getClaimableRewards(completedStage);
        if (rewards.isEmpty()) return;
        String rewardKey = quest.getId() + ":" + completedStage.name();
        LKPlayerData playerData = LKPlayerDataProvider.get(player);
        if (playerData.hasClaimedReward(rewardKey)) return;
        for (ClaimableReward reward : rewards) {
            player.addItem(new ItemStack(reward.item().get(), reward.count()));
        }
        playerData.claimReward(rewardKey);
    }

    private boolean checkRequirements(ServerPlayer player, List<LKStage.ItemRequirement> requirements) {
        for (LKStage.ItemRequirement req : requirements) {
            if (!hasRequirement(player, req)) return false;
        }
        return true;
    }

    private boolean hasRequirement(ServerPlayer player, LKStage.ItemRequirement req) {
        return switch (req.source()) {
            case MAIN_HAND -> {
                ItemStack held = player.getMainHandItem();
                yield held.is(req.item().get()) && held.getCount() >= req.count();
            }
            case INVENTORY -> {
                int found = 0;
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(req.item().get())) {
                        found += stack.getCount();
                        if (found >= req.count()) yield true;
                    }
                }
                yield false;
            }
        };
    }

    private void consumeRequirements(ServerPlayer player, List<LKStage.ItemRequirement> requirements) {
        for (LKStage.ItemRequirement req : requirements) {
            consumeRequirement(player, req);
        }
    }

    private void consumeRequirement(ServerPlayer player, LKStage.ItemRequirement req) {
        switch (req.source()) {
            case MAIN_HAND -> player.getMainHandItem().shrink(req.count());
            case INVENTORY -> {
                int remaining = req.count();
                for (ItemStack stack : player.getInventory().items) {
                    if (remaining <= 0) break;
                    if (stack.is(req.item().get())) {
                        int take = Math.min(remaining, stack.getCount());
                        stack.shrink(take);
                        remaining -= take;
                    }
                }
            }
        }
    }

    public boolean anyUnchecked() {
        for (LKQuestline quest : LKQuestRegistry.getOrdered()) {
            LKQuestlineState state = getState(quest.getId());
            if (quest.canStart(this) && !state.isChecked()) return true;
        }
        return false;
    }

    // -- Sync ---------------------------------------------------------------

    public void syncToPlayer(ServerPlayer player) {
        for (LKQuestline quest : LKQuestRegistry.getOrdered()) {
            LKQuestlineState state = getState(quest.getId());
            LKNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new QuestSyncPacket(quest.getId(), state.getCurrentStageId(), state.isChecked())
            );
        }
    }

    public void syncToAllPlayers(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            syncToPlayer(player);
        }
    }

    // -- NBT ----------------------------------------------------------------

    public void writeToNBT(CompoundTag tag) {
        CompoundTag questsTag = new CompoundTag();
        for (Map.Entry<String, LKQuestlineState> entry : states.entrySet()) {
            CompoundTag questTag = new CompoundTag();
            entry.getValue().writeToNBT(questTag);
            questsTag.put(entry.getKey(), questTag);
        }
        tag.put("Quests", questsTag);
    }

    public void readFromNBT(CompoundTag tag) {
        if (!tag.contains("Quests")) {
            return;
        }
        CompoundTag questsTag = tag.getCompound("Quests");
        for (String key : questsTag.getAllKeys()) {
            LKQuestlineState state = LKQuestlineState.readFromNBT(questsTag.getCompound(key));
            states.put(key, state);
        }
    }
}
