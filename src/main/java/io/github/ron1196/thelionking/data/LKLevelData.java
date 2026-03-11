package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LKLevelData extends SavedData {

    private static final String DATA_NAME = TheLionKingMod.MOD_ID + "_data";

    // Quest-related state
    public int ziraStage = 0;
    public int pumbaaStage = 0;
    public boolean outlandersHostile = false;
    public boolean defeatedScar = false;
    public boolean receivedQuestBook = false;
    // Home portal location
    public int homePortalX = 0, homePortalY = 0, homePortalZ = 0;

    // Players who own Simbas
    public Map<String, Integer> simbas = new HashMap<>();

    public LKLevelData() {
    }

    public static LKLevelData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(LKLevelData::load, LKLevelData::new, DATA_NAME);
    }

    public static LKLevelData load(CompoundTag tag) {
        LKLevelData data = new LKLevelData();
        data.ziraStage = tag.getInt("ZiraStage");
        data.pumbaaStage = tag.getInt("PumbaaStage");
        data.outlandersHostile = tag.getBoolean("OutlandersHostile");
        data.defeatedScar = tag.getBoolean("DefeatedScar");
        data.receivedQuestBook = tag.getBoolean("ReceivedQuestBook");
        data.homePortalX = tag.getInt("HomePortalX");
        data.homePortalY = tag.getInt("HomePortalY");
        data.homePortalZ = tag.getInt("HomePortalZ");

        // Load simbas
        if (tag.contains("Simbas")) {
            CompoundTag simbaNbt = tag.getCompound("Simbas");
            for (String key : simbaNbt.getAllKeys()) {
                data.simbas.put(key, simbaNbt.getInt(key));
            }
        }

        // Load quests
        LKQuestBase.readAllQuestsFromNBT(tag);

        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putInt("ZiraStage", ziraStage);
        tag.putInt("PumbaaStage", pumbaaStage);
        tag.putBoolean("OutlandersHostile", outlandersHostile);
        tag.putBoolean("DefeatedScar", defeatedScar);
        tag.putBoolean("ReceivedQuestBook", receivedQuestBook);
        tag.putInt("HomePortalX", homePortalX);
        tag.putInt("HomePortalY", homePortalY);
        tag.putInt("HomePortalZ", homePortalZ);

        // Save simbas
        CompoundTag simbaNbt = new CompoundTag();
        for (Map.Entry<String, Integer> entry : simbas.entrySet()) {
            simbaNbt.putInt(entry.getKey(), entry.getValue());
        }
        tag.put("Simbas", simbaNbt);

        // Save quests
        LKQuestBase.writeAllQuestsToNBT(tag);

        return tag;
    }
}
