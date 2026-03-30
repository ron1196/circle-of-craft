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

    public static WorldData get(ServerLevel level) {
        // Always use overworld data storage so quest state is shared across all dimensions
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(WorldData::load, WorldData::new, DATA_NAME);
    }

    public static WorldData load(CompoundTag tag) {
        WorldData data = new WorldData();
        data.questManager.readFromNBT(tag);
        data.scarSpawned = tag.getBoolean("ScarSpawned");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        questManager.writeToNBT(tag);
        tag.putBoolean("ScarSpawned", scarSpawned);
        return tag;
    }
}
