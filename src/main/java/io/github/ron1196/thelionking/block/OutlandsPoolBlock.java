package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.block.entity.OutlandsPoolBlockEntity;
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

public class OutlandsPoolBlock extends BaseEntityBlock {

  private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

  public OutlandsPoolBlock(BlockBehaviour.Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    return SHAPE;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new OutlandsPoolBlockEntity(pos, state);
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (level.isClientSide()) return;

    // Collect dropped items
    if (entity instanceof ItemEntity itemEntity && !itemEntity.getItem().isEmpty()) {
      if (level.getBlockEntity(pos) instanceof OutlandsPoolBlockEntity pool) {
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
    }

    // Damage living entities (fire damage)
    if (entity instanceof LivingEntity living && entity.tickCount % 20 == 0) {
      living.hurt(level.damageSources().hotFloor(), 2.0F);
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    if (!level.isClientSide()) {
      return (lvl, pos, st, be) -> {
        if (be instanceof OutlandsPoolBlockEntity pool) pool.serverTick();
      };
    }
    return null;
  }
}
