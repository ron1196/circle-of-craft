package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Pool Cover — indestructible block sealing the Outwater pool in Zira's Mound.
 * Removed by quest progression (COLLECT_INGOTS → THROW_IN_OUTWATER).
 * Self-destructs if the quest has already passed that stage.
 */
public class PoolCoverBlock extends Block {

    private static final Set<OutlandsQuestline.Stage> POOL_SEALED_STAGES = EnumSet.of(
            OutlandsQuestline.Stage.ENTER_OUTLANDS,
            OutlandsQuestline.Stage.FIND_ZIRA,
            OutlandsQuestline.Stage.COLLECT_INGOTS);

    public PoolCoverBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public void randomTick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        OutlandsQuestline.Stage stage =
                WorldData.get(level).getQuestManager().getStage("outlands", OutlandsQuestline.Stage.class);
        if (!POOL_SEALED_STAGES.contains(stage)) {
            level.destroyBlock(pos, false);
        }
    }
}
