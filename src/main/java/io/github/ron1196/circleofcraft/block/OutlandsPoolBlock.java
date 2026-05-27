package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.block.entity.OutlandsPoolBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class OutlandsPoolBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
    private static final int MIN_ITEM_AGE = 5;

    public OutlandsPoolBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new OutlandsPoolBlockEntity(pos, state);
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide()) return;

        if (entity instanceof ItemEntity itemEntity
                && !itemEntity.getItem().isEmpty()
                && !itemEntity.isRemoved()
                && itemEntity.tickCount > MIN_ITEM_AGE) {

            if (!(level.getBlockEntity(pos) instanceof OutlandsPoolBlockEntity pool)) return;

            // Absorb all items — invalid ones get spat out at the altar during processing
            pool.collectItem(itemEntity.getItem().copy());
            itemEntity.discard();
            level.playSound(
                    null,
                    pos,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    0.7F,
                    1.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
        }

        // Damage living entities (fire damage)
        if (entity instanceof LivingEntity living && entity.tickCount % 20 == 0) {
            living.hurt(level.damageSources().hotFloor(), 2.0F);
        }
    }

    // ── Comparator support ──────────────────────────────────────────────────

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof OutlandsPoolBlockEntity pool) {
            if (pool.isProcessing()) return 15;
            int count = pool.getItemCount();
            return Math.min(count, 14);
        }
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof OutlandsPoolBlockEntity pool) pool.serverTick();
        };
    }
}
