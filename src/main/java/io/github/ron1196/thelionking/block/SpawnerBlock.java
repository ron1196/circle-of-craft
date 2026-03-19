package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.SpawnerBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SpawnerBlock extends BaseEntityBlock {

    public SpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof SpawnerBlockEntity spawner) spawner.serverTick();
            };
        }
        return null;
    }

    @Override
    public int getExpDrop(
            BlockState state,
            LevelReader level,
            RandomSource random,
            BlockPos pos,
            int fortuneLevel,
            int silkTouchLevel) {
        return 15 + random.nextInt(15) + random.nextInt(15);
    }
}
