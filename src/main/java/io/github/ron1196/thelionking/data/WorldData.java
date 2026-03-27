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

  public WorldData() {}

  public QuestlineManager getQuestManager() {
    return questManager;
  }

  public static WorldData get(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(WorldData::load, WorldData::new, DATA_NAME);
  }

  public static WorldData load(CompoundTag tag) {
    WorldData data = new WorldData();
    data.questManager.readFromNBT(tag);
    return data;
  }

  @Override
  public @NotNull CompoundTag save(CompoundTag tag) {
    questManager.writeToNBT(tag);
    return tag;
  }
}
