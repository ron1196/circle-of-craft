package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.data.WorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class RafikiWoodBlock extends RotatedPillarBlock {

    public static final BooleanProperty CORRUPT = BooleanProperty.create("corrupt");
    private static final int CHECK_INTERVAL = 20;

    public RafikiWoodBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CORRUPT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORRUPT);
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
        boolean shouldBeCorrupt = WorldData.get(level).isZiraOccupiesTree();
        if (state.getValue(CORRUPT) != shouldBeCorrupt) {
            level.setBlock(pos, state.setValue(CORRUPT, shouldBeCorrupt), 3);
        }
    }
}
