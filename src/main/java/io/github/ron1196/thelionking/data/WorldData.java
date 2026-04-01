package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class WorldData extends SavedData {

    private static final String DATA_NAME = TheLionKingMod.MOD_ID + "_data";

    private final QuestlineManager questManager = new QuestlineManager(this);
    private int ziraTreeTalkCount = 0;
    private int pumbaaTalkCount = 0;
    private int flatulenceExplosionsRemaining = 0;

    public WorldData() {}

    public QuestlineManager getQuestManager() {
        return questManager;
    }

    private static final Set<OutlandsQuestline.Stage> TREE_OCCUPATION_STAGES = EnumSet.of(
            OutlandsQuestline.Stage.ZIRA_OCCUPIES_TREE,
            OutlandsQuestline.Stage.TALK_TO_PUMBAA,
            OutlandsQuestline.Stage.GATHER_PUMBAA_INGREDIENTS,
            OutlandsQuestline.Stage.USE_PUMBAA_BOX);

    public boolean isZiraOccupiesTree() {
        return TREE_OCCUPATION_STAGES.contains(questManager.getStage("outlands", OutlandsQuestline.Stage.class));
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

    public int getFlatulenceExplosionsRemaining() {
        return flatulenceExplosionsRemaining;
    }

    public void setFlatulenceExplosionsRemaining(int count) {
        this.flatulenceExplosionsRemaining = count;
        setDirty();
    }

    public void decrementFlatulenceExplosions() {
        if (this.flatulenceExplosionsRemaining > 0) {
            this.flatulenceExplosionsRemaining--;
            setDirty();
        }
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
        data.flatulenceExplosionsRemaining = tag.getInt("FlatulenceExplosionsRemaining");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        questManager.writeToNBT(tag);
        tag.putInt("ZiraTreeTalkCount", ziraTreeTalkCount);
        tag.putInt("PumbaaTalkCount", pumbaaTalkCount);
        tag.putInt("FlatulenceExplosionsRemaining", flatulenceExplosionsRemaining);
        return tag;
    }
}
