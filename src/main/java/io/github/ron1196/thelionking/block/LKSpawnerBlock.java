package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.LKSpawnerBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LKSpawnerBlock extends BaseEntityBlock {

    public LKSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LKSpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof LKSpawnerBlockEntity spawner) spawner.serverTick();
            };
        }
        return null;
    }

    @Override
    public int getExpDrop(
            BlockState state,
            net.minecraft.world.level.LevelReader level,
            net.minecraft.util.RandomSource random,
            BlockPos pos,
            int fortuneLevel,
            int silkTouchLevel) {
        return 15 + random.nextInt(15) + random.nextInt(15);
    }
}
