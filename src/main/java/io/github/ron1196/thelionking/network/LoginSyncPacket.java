package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from server to client on player login to sync all world/quest state.
 * Mirrors old mod's "lk.login" packet from LKLevelData.getLoginPacket().
 */
public class LoginSyncPacket {

    // World state
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean defeatedScar;
    private final int ziraStage;
    private final int pumbaaStage;
    private final boolean outlandersHostile;

    // Quest data for all 16 quest slots
    private final int[] questStages;
    private final int[] questDelayed;
    private final int[] questChecked;
    private final int[][] questStagesCompleted;

    public LoginSyncPacket(LKLevelData data) {
        this.homePortalX = data.homePortalX;
        this.homePortalY = data.homePortalY;
        this.homePortalZ = data.homePortalZ;
        this.defeatedScar = data.defeatedScar;
        this.ziraStage = data.ziraStage;
        this.pumbaaStage = data.pumbaaStage;
        this.outlandersHostile = data.outlandersHostile;

        this.questStages = new int[16];
        this.questDelayed = new int[16];
        this.questChecked = new int[16];
        this.questStagesCompleted = new int[16][];

        for (int i = 0; i < 16; i++) {
            LKQuestBase quest = LKQuestBase.ALL_QUESTS[i];
            if (quest != null) {
                questStages[i] = quest.currentStage;
                questDelayed[i] = quest.stagesDelayed;
                questChecked[i] = quest.checked;
                questStagesCompleted[i] = quest.stagesCompleted.clone();
            } else {
                questStagesCompleted[i] = new int[0];
            }
        }
    }

    public LoginSyncPacket(FriendlyByteBuf buf) {
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.defeatedScar = buf.readBoolean();
        this.ziraStage = buf.readVarInt();
        this.pumbaaStage = buf.readVarInt();
        this.outlandersHostile = buf.readBoolean();

        this.questStages = new int[16];
        this.questDelayed = new int[16];
        this.questChecked = new int[16];
        this.questStagesCompleted = new int[16][];

        for (int i = 0; i < 16; i++) {
            questStages[i] = buf.readVarInt();
            questDelayed[i] = buf.readVarInt();
            questChecked[i] = buf.readVarInt();
            int completedLen = buf.readVarInt();
            questStagesCompleted[i] = new int[completedLen];
            for (int j = 0; j < completedLen; j++) {
                questStagesCompleted[i][j] = buf.readVarInt();
            }
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(homePortalX);
        buf.writeInt(homePortalY);
        buf.writeInt(homePortalZ);
        buf.writeBoolean(defeatedScar);
        buf.writeVarInt(ziraStage);
        buf.writeVarInt(pumbaaStage);
        buf.writeBoolean(outlandersHostile);

        for (int i = 0; i < 16; i++) {
            buf.writeVarInt(questStages[i]);
            buf.writeVarInt(questDelayed[i]);
            buf.writeVarInt(questChecked[i]);
            buf.writeVarInt(questStagesCompleted[i].length);
            for (int val : questStagesCompleted[i]) {
                buf.writeVarInt(val);
            }
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // Apply world state to client-side quest data
            for (int i = 0; i < 16; i++) {
                LKQuestBase quest = LKQuestBase.ALL_QUESTS[i];
                if (quest == null) continue;
                quest.currentStage = questStages[i];
                quest.stagesDelayed = questDelayed[i];
                quest.checked = questChecked[i];
                int len = Math.min(questStagesCompleted[i].length, quest.stagesCompleted.length);
                for (int j = 0; j < len; j++) {
                    quest.stagesCompleted[j] = questStagesCompleted[i][j];
                }
            }

            // Store world state on client via ClientWorldState
            ClientWorldState.homePortalX = homePortalX;
            ClientWorldState.homePortalY = homePortalY;
            ClientWorldState.homePortalZ = homePortalZ;
            ClientWorldState.defeatedScar = defeatedScar;
            ClientWorldState.ziraStage = ziraStage;
            ClientWorldState.pumbaaStage = pumbaaStage;
            ClientWorldState.outlandersHostile = outlandersHostile;
        });
        context.setPacketHandled(true);
    }
}
