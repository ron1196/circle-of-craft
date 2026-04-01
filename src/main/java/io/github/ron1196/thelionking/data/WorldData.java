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
    private boolean scarSpawned = false;
    private boolean scarDefeated = false;
    private boolean ziraOccupiesTree = false;
    private int ziraTreeTalkCount = 0;
    private int pumbaaTalkCount = 0;

    public WorldData() {}

    public QuestlineManager getQuestManager() {
        return questManager;
    }

    public boolean isScarSpawned() {
        return scarSpawned;
    }

    public void setScarSpawned(boolean spawned) {
        this.scarSpawned = spawned;
        setDirty();
    }

    public boolean isScarDefeated() {
        return scarDefeated;
    }

    public void setScarDefeated(boolean defeated) {
        this.scarDefeated = defeated;
        setDirty();
    }

    public boolean isZiraOccupiesTree() {
        return ziraOccupiesTree;
    }

    public void setZiraOccupiesTree(boolean occupies) {
        this.ziraOccupiesTree = occupies;
        setDirty();
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

    @SuppressWarnings("resource") // ServerLevel is managed by the server, never closed manually
    public static WorldData get(ServerLevel level) {
        // Always use overworld data storage so quest state is shared across all dimensions
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(WorldData::load, WorldData::new, DATA_NAME);
    }

    public static WorldData load(CompoundTag tag) {
        WorldData data = new WorldData();
        data.questManager.readFromNBT(tag);
        data.scarSpawned = tag.getBoolean("ScarSpawned");
        data.scarDefeated = tag.getBoolean("ScarDefeated");
        data.ziraOccupiesTree = tag.getBoolean("ZiraOccupiesTree");
        data.ziraTreeTalkCount = tag.getInt("ZiraTreeTalkCount");
        data.pumbaaTalkCount = tag.getInt("PumbaaTalkCount");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        questManager.writeToNBT(tag);
        tag.putBoolean("ScarSpawned", scarSpawned);
        tag.putBoolean("ScarDefeated", scarDefeated);
        tag.putBoolean("ZiraOccupiesTree", ziraOccupiesTree);
        tag.putInt("ZiraTreeTalkCount", ziraTreeTalkCount);
        tag.putInt("PumbaaTalkCount", pumbaaTalkCount);
        return tag;
    }
}
