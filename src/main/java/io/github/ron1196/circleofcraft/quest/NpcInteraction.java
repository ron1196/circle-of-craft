package io.github.ron1196.circleofcraft.quest;

import io.github.ron1196.circleofcraft.data.PlayerData;
import io.github.ron1196.circleofcraft.data.PlayerDataProvider;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.network.Networking;
import io.github.ron1196.circleofcraft.network.PlayerDataSyncPacket;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.quest.stage.StageId;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates the quest interaction protocol shared by all quest NPCs. Assembles the quest context
 * (WorldData, QuestlineManager, PlayerData) in one shot and enforces the load-bearing
 * claim-then-advance-then-sync ordering.
 */
public record NpcInteraction(
        @NotNull ServerPlayer serverPlayer,
        @NotNull ServerLevel serverLevel,
        @NotNull WorldData worldData,
        @NotNull QuestlineManager quests,
        @NotNull PlayerData playerData) {

    /**
     * Creates an NpcInteraction context from a player. Returns null if the interaction is
     * client-side, the player is not a ServerPlayer, or the level is not a ServerLevel.
     */
    @Nullable
    public static NpcInteraction tryCreate(@NotNull Player player) {
        if (player.level().isClientSide()) return null;
        if (!(player instanceof ServerPlayer serverPlayer)) return null;
        if (!(player.level() instanceof ServerLevel serverLevel)) return null;

        WorldData worldData = WorldData.get(serverLevel);
        QuestlineManager quests = worldData.getQuestManager();
        PlayerData playerData = PlayerDataProvider.get(serverPlayer);
        return new NpcInteraction(serverPlayer, serverLevel, worldData, quests, playerData);
    }

    /**
     * Returns the typed enum stage for the given quest.
     */
    public <T extends Enum<T> & StageId> @NotNull T stage(@NotNull String questId, @NotNull Class<T> stageClass) {
        return quests.getStage(questId, stageClass);
    }

    /**
     * The standard quest interaction path: try to claim the next unclaimed reward, then try to
     * advance the quest. Syncs player data to the client on success.
     *
     * <p>The ordering is load-bearing: rewards must be claimed before advancing, because advancing
     * may generate new rewards that should not be immediately claimed in the same interaction.
     *
     * @param questId    the quest identifier (e.g., RafikiQuestline.QUEST_ID, OutlandsQuestline.QUEST_ID)
     * @param stageClass the stage enum class for typed stage lookup
     * @param trigger    the quest trigger to attempt advancement with
     * @param onClaim    optional callback invoked with the current stage after a reward is claimed;
     *                   use this to show claim-specific dialogue distinct from stage advancement
     * @param onAdvance  optional callback invoked with the new stage after the quest advances
     * @return true if the quest progressed (reward claimed or stage advanced)
     */
    public <T extends Enum<T> & StageId> boolean tryClaimOrAdvance(
            @NotNull String questId,
            @NotNull Class<T> stageClass,
            @NotNull QuestTrigger trigger,
            @Nullable Consumer<T> onClaim,
            @Nullable Consumer<T> onAdvance) {
        // Try claiming unclaimed rewards first
        int claimedIndex = quests.tryClaimNextReward(questId, serverPlayer);
        if (claimedIndex >= 0) {
            T newStage = quests.getStage(questId, stageClass);
            if (onClaim != null) onClaim.accept(newStage);
            syncPlayerData();
            return true;
        }

        // Try advancing the quest
        if (quests.tryAdvance(questId, serverPlayer, trigger)) {
            T newStage = quests.getStage(questId, stageClass);
            if (onAdvance != null) onAdvance.accept(newStage);
            syncPlayerData();
            return true;
        }

        return false;
    }

    /**
     * Syncs player data to the client via network packet.
     */
    public void syncPlayerData() {
        Networking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerDataSyncPacket(playerData));
    }
}
