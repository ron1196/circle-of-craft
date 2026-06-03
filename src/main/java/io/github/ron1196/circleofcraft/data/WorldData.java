package io.github.ron1196.circleofcraft.data;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class WorldData extends SavedData {

    private static final String DATA_NAME = CircleOfCraftMod.MOD_ID + "_data";

    private final QuestlineManager questManager = new QuestlineManager(this);
    public static final int TIMON_RALLY_INTRO_LINE_COUNT = 3;

    private int ziraTreeTalkCount = 0;
    private int pumbaaTalkCount = 0;
    private int timonRafikiTalkCount = 0;

    /** Ticks since Lion Dust ceremony started. 0 = not active, >0 = active. */
    private int rafikiCeremonyTick = 0;

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

    public int getRafikiCeremonyTick() {
        return rafikiCeremonyTick;
    }

    public void setRafikiCeremonyTick(int tick) {
        this.rafikiCeremonyTick = tick;
        setDirty();
    }

    public boolean isCeremonyActive() {
        return rafikiCeremonyTick > 0;
    }

    @SuppressWarnings("resource") // ServerLevel is managed by the server, never closed manually
    public static WorldData get(ServerLevel level) {
        // Always use overworld data storage so quest state is shared across all dimensions
        ServerLevel overworld = level.getServer().overworld();
        return overworld
                .getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(WorldData::new, WorldData::load), DATA_NAME);
    }

    public static WorldData load(CompoundTag tag, HolderLookup.Provider lookup) {
        WorldData data = new WorldData();
        data.questManager.readFromNBT(tag);
        data.ziraTreeTalkCount = tag.getInt("ZiraTreeTalkCount");
        data.pumbaaTalkCount = tag.getInt("PumbaaTalkCount");
        data.timonRafikiTalkCount = tag.getInt("TimonRafikiTalkCount");
        data.rafikiCeremonyTick = tag.getInt("RafikiCeremonyTick");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider lookup) {
        questManager.writeToNBT(tag);
        tag.putInt("ZiraTreeTalkCount", ziraTreeTalkCount);
        tag.putInt("PumbaaTalkCount", pumbaaTalkCount);
        tag.putInt("TimonRafikiTalkCount", timonRafikiTalkCount);
        tag.putInt("RafikiCeremonyTick", rafikiCeremonyTick);
        return tag;
    }
}
