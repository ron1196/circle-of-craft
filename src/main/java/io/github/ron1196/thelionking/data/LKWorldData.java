package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.quest.LKQuestlineManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class LKWorldData extends SavedData {

    private static final String DATA_NAME = TheLionKingMod.MOD_ID + "_data";

    // Quest-related state
    public int ziraStage = 0;
    public int pumbaaStage = 0;
    public boolean outlandersHostile = false;
    public boolean defeatedScar = false;

    // Quest manager
    private final LKQuestlineManager questManager = new LKQuestlineManager(this);

    public LKWorldData() {
    }

    public LKQuestlineManager getQuestManager() {
        return questManager;
    }

    public static LKWorldData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(LKWorldData::load, LKWorldData::new, DATA_NAME);
    }

    public static LKWorldData load(CompoundTag tag) {
        LKWorldData data = new LKWorldData();
        data.ziraStage = tag.getInt("ZiraStage");
        data.pumbaaStage = tag.getInt("PumbaaStage");
        data.outlandersHostile = tag.getBoolean("OutlandersHostile");
        data.defeatedScar = tag.getBoolean("DefeatedScar");

        // Load quests
        data.questManager.readFromNBT(tag);

        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putInt("ZiraStage", ziraStage);
        tag.putInt("PumbaaStage", pumbaaStage);
        tag.putBoolean("OutlandersHostile", outlandersHostile);
        tag.putBoolean("DefeatedScar", defeatedScar);

        // Save quests
        questManager.writeToNBT(tag);

        return tag;
    }
}
