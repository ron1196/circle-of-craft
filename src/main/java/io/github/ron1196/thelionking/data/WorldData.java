package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class WorldData extends SavedData {

    private static final String DATA_NAME = TheLionKingMod.MOD_ID + "_data";

    private final QuestlineManager questManager = new QuestlineManager(this);
    public static final int TIMON_RALLY_INTRO_LINE_COUNT = 3;

    private int ziraTreeTalkCount = 0;
    private int pumbaaTalkCount = 0;
    private int timonRafikiTalkCount = 0;

    public WorldData() {}

    public QuestlineManager getQuestManager() {
        return questManager;
    }

    public int getZiraTreeTalkCount() {
        return ziraTreeTalkCount;
    }

    public void incrementZiraTreeTalkCount() {
        this.ziraTreeTalkCount++;
        setDirty();
    }

    public void resetZiraTreeTalkCount() {
        this.ziraTreeTalkCount = 0;
        setDirty();
    }

    public int getPumbaaTalkCount() {
        return pumbaaTalkCount;
    }

    public void incrementPumbaaTalkCount() {
        this.pumbaaTalkCount++;
        setDirty();
    }

    public void resetPumbaaTalkCount() {
        this.pumbaaTalkCount = 0;
        setDirty();
    }

    public int getTimonRafikiTalkCount() {
        return timonRafikiTalkCount;
    }

    public void incrementTimonRafikiTalkCount() {
        this.timonRafikiTalkCount++;
        setDirty();
    }

    public void resetTimonRafikiTalkCount() {
        this.timonRafikiTalkCount = 0;
        setDirty();
    }

    public boolean isTimonRafikiIntroDone() {
        return timonRafikiTalkCount >= TIMON_RALLY_INTRO_LINE_COUNT;
    }

    @SuppressWarnings("resource") // ServerLevel is managed by the server, never closed manually
    public static WorldData get(ServerLevel level) {
        // Always use overworld data storage so quest state is shared across all dimensions
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(WorldData::load, WorldData::new, DATA_NAME);
    }

    public static WorldData load(CompoundTag tag) {
        WorldData data = new WorldData();
        data.questManager.readFromNBT(tag);
        data.ziraTreeTalkCount = tag.getInt("ZiraTreeTalkCount");
        data.pumbaaTalkCount = tag.getInt("PumbaaTalkCount");
        data.timonRafikiTalkCount = tag.getInt("TimonRafikiTalkCount");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        questManager.writeToNBT(tag);
        tag.putInt("ZiraTreeTalkCount", ziraTreeTalkCount);
        tag.putInt("PumbaaTalkCount", pumbaaTalkCount);
        tag.putInt("TimonRafikiTalkCount", timonRafikiTalkCount);
        return tag;
    }
}
