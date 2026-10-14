package io.github.ron1196.circleofcraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class WorldData extends SavedData {

    public static final Codec<WorldData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.unboundedMap(Codec.STRING, QuestlineState.CODEC)
                            .optionalFieldOf("Quests", Map.of())
                            .forGetter(data -> data.questManager.getStatesForSave()),
                    Codec.INT.optionalFieldOf("ZiraTreeTalkCount", 0).forGetter(data -> data.ziraTreeTalkCount),
                    Codec.INT.optionalFieldOf("PumbaaTalkCount", 0).forGetter(data -> data.pumbaaTalkCount),
                    Codec.INT.optionalFieldOf("TimonRafikiTalkCount", 0)
                            .forGetter(data -> data.timonRafikiTalkCount),
                    Codec.INT.optionalFieldOf("RafikiCeremonyTick", 0).forGetter(data -> data.rafikiCeremonyTick))
            .apply(instance, WorldData::fromSaved));

    public static final SavedDataType<WorldData> TYPE =
            new SavedDataType<>(CircleOfCraftMod.id("data"), WorldData::new, CODEC);

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
        return overworld.getDataStorage().computeIfAbsent(TYPE);
    }

    private static WorldData fromSaved(
            Map<String, QuestlineState> quests,
            int ziraTreeTalkCount,
            int pumbaaTalkCount,
            int timonRafikiTalkCount,
            int rafikiCeremonyTick) {
        WorldData data = new WorldData();
        data.questManager.loadStates(quests);
        data.ziraTreeTalkCount = ziraTreeTalkCount;
        data.pumbaaTalkCount = pumbaaTalkCount;
        data.timonRafikiTalkCount = timonRafikiTalkCount;
        data.rafikiCeremonyTick = rafikiCeremonyTick;
        return data;
    }
}
