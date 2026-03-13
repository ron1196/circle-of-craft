package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.QuestSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;

public abstract class LKQuestBase {

    public int stagesDelayed;
    private String questName;
    public int currentStage;
    public int[] stagesCompleted;
    public int checked;
    public final int questIndex;

    public LKQuestBase(int index) {
        LKQuests.ALL_QUESTS[index] = this;
        this.questIndex = index;
        this.stagesCompleted = new int[getNumStages() + 1];
    }

    public LKQuestBase setName(String name) {
        this.questName = name;
        return this;
    }

    public String getName() {
        return questName;
    }

    public boolean isComplete() {
        return currentStage == getNumStages();
    }

    public abstract boolean canStart();

    public abstract String[] getRequirements();

    public abstract int getNumStages();

    public abstract ItemStack getIcon();

    public abstract String getObjectiveByStage(int stage);

    public void progress(int stage) {
        if (stage != currentStage + 1) return;
        if (currentStage > 0) {
            for (int i = 0; i < currentStage; i++) {
                if (stagesCompleted[i] == 0) return;
            }
        }
        stagesCompleted[currentStage] = 1;
    }

    public int getQuestStage() {
        return currentStage;
    }

    public void setDelayed(boolean flag) {
        stagesDelayed = flag ? 1 : 0;
    }

    public boolean isDelayed() {
        return stagesDelayed == 1;
    }

    public boolean isStageComplete(int i) {
        return stagesCompleted[i] == 1;
    }

    public boolean isChecked() {
        return checked == 1;
    }

    public void setChecked(boolean flag) {
        checked = flag ? 1 : 0;
    }

    public void resetProgress() {
        currentStage = 0;
        stagesDelayed = 0;
        Arrays.fill(stagesCompleted, 0);
        checked = 0;
    }

    public static boolean anyUncheckedQuests() {
        for (LKQuestBase quest : LKQuests.ALL_QUESTS) {
            if (quest != null && quest.canStart() && !quest.isChecked()) return true;
        }
        return false;
    }

    public static void updateAllQuests() {
        for (LKQuestBase quest : LKQuests.ALL_QUESTS) {
            if (quest == null) continue;
            if (quest.stagesDelayed == 0 && quest.stagesCompleted[quest.currentStage] == 1
                    && quest.currentStage < quest.getNumStages()) {
                quest.currentStage++;
            }
        }
    }

    public static void syncToPlayer(ServerPlayer player) {
        for (LKQuestBase quest : LKQuests.ALL_QUESTS) {
            if (quest == null) continue;
            LKNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new QuestSyncPacket(quest.questIndex, quest.currentStage, quest.checked)
            );
        }
    }

    public static void syncToAllPlayers(net.minecraft.server.MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            syncToPlayer(player);
        }
    }

    public static void writeAllQuestsToNBT(CompoundTag tag) {
        for (int i = 0; i < LKQuests.ALL_QUESTS.length; i++) {
            LKQuestBase quest = LKQuests.ALL_QUESTS[i];
            if (quest == null) continue;
            tag.putInt("Quest_" + i + "_Stage", quest.currentStage);
            tag.putInt("Quest_" + i + "_Delayed", quest.stagesDelayed);
            for (int j = 0; j < quest.stagesCompleted.length; j++) {
                tag.putInt("Quest_" + i + "_CompletedStage_" + j, quest.stagesCompleted[j]);
            }
            tag.putInt("Quest_" + i + "_Checked", quest.checked);
        }
    }

    public static void readAllQuestsFromNBT(CompoundTag tag) {
        for (int i = 0; i < LKQuests.ALL_QUESTS.length; i++) {
            LKQuestBase quest = LKQuests.ALL_QUESTS[i];
            if (quest == null) continue;
            quest.currentStage = tag.getInt("Quest_" + i + "_Stage");
            quest.stagesDelayed = tag.getInt("Quest_" + i + "_Delayed");
            for (int j = 0; j < quest.stagesCompleted.length; j++) {
                quest.stagesCompleted[j] = tag.getInt("Quest_" + i + "_CompletedStage_" + j);
            }
            quest.checked = tag.getInt("Quest_" + i + "_Checked");
        }
    }
}
