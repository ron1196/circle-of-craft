package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.data.LKPlayerData;
import io.github.ron1196.thelionking.data.LKPlayerDataProvider;
import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.QuestSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class LKQuestManager {

    private final Map<String, LKQuestState> states = new HashMap<>();
    private final SavedData owner;

    public LKQuestManager(SavedData owner) {
        this.owner = owner;
        for (LKQuestline quest : LKQuestRegistry.getOrdered()) {
            states.put(quest.getId(), new LKQuestState());
        }
    }

    public LKQuestState getState(String questId) {
        return states.computeIfAbsent(questId, k -> new LKQuestState());
    }

    public int getStage(String questId) {
        return getState(questId).getCurrentStage();
    }

    public boolean isComplete(String questId) {
        LKQuestline quest = LKQuestRegistry.get(questId);
        if (quest == null) return false;
        return getStage(questId) >= quest.getNumStages();
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

        LKQuestState state = getState(questId);
        int currentStage = state.getCurrentStage();

        if (currentStage >= quest.getNumStages()) return false;

        LKQuestTrigger expected = quest.getTriggerForStage(currentStage);
        if (expected == null || expected != trigger) return false;

        LKQuestStage stageDef = quest.getStage(currentStage);

        if (!checkRequirements(player, stageDef.requirements())) return false;
        consumeRequirements(player, stageDef.requirements());

        BiConsumer<ServerPlayer, LKQuestManager> custom = quest.getCustomTransition(currentStage);
        if (custom != null) {
            custom.accept(player, this);
        }

        // Advance stage
        state.setCurrentStage(currentStage + 1);
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
        int currentStage = getStage(questId);
        for (int stage = 0; stage < currentStage; stage++) {
            for (LKClaimableReward reward : quest.getClaimableRewards(stage)) {
                if (playerData.hasClaimedReward(reward.rewardKey())) continue;
                player.addItem(new ItemStack(reward.item().get(), reward.count()));
                playerData.claimReward(reward.rewardKey());
                return stage;
            }
        }
        return -1;
    }

    private void claimRewards(LKQuestline quest, int completedStage, ServerPlayer player) {
        LKPlayerData playerData = LKPlayerDataProvider.get(player);
        for (LKClaimableReward reward : quest.getClaimableRewards(completedStage)) {
            if (!playerData.hasClaimedReward(reward.rewardKey())) {
                player.addItem(new ItemStack(reward.item().get(), reward.count()));
                playerData.claimReward(reward.rewardKey());
            }
        }
    }

    private boolean checkRequirements(ServerPlayer player, List<LKQuestStage.ItemRequirement> requirements) {
        for (LKQuestStage.ItemRequirement req : requirements) {
            if (!hasRequirement(player, req)) return false;
        }
        return true;
    }

    private boolean hasRequirement(ServerPlayer player, LKQuestStage.ItemRequirement req) {
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

    private void consumeRequirements(ServerPlayer player, List<LKQuestStage.ItemRequirement> requirements) {
        for (LKQuestStage.ItemRequirement req : requirements) {
            consumeRequirement(player, req);
        }
    }

    private void consumeRequirement(ServerPlayer player, LKQuestStage.ItemRequirement req) {
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
            LKQuestState state = getState(quest.getId());
            if (quest.canStart(this) && !state.isChecked()) return true;
        }
        return false;
    }

    // ── Sync ────────────────────────────────────────────────────────────────────

    public void syncToPlayer(ServerPlayer player) {
        for (LKQuestline quest : LKQuestRegistry.getOrdered()) {
            LKQuestState state = getState(quest.getId());
            LKNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new QuestSyncPacket(quest.getId(), state.getCurrentStage(), state.isChecked())
            );
        }
    }

    public void syncToAllPlayers(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            syncToPlayer(player);
        }
    }

    // ── NBT ─────────────────────────────────────────────────────────────────────

    public void writeToNBT(CompoundTag tag) {
        CompoundTag questsTag = new CompoundTag();
        for (Map.Entry<String, LKQuestState> entry : states.entrySet()) {
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
            LKQuestState state = LKQuestState.readFromNBT(questsTag.getCompound(key));
            states.put(key, state);
        }
    }
}
