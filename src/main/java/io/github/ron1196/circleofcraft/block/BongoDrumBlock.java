package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.block.entity.BongoDrumBlockEntity;
import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.registry.ModItems;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class BongoDrumBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);

    public BongoDrumBlock(BlockBehaviour.Properties properties) {
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
        return new BongoDrumBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BongoDrumBlockEntity drum) {
            // If holding a staff, open enchanting GUI
            if (player.getItemInHand(hand).is(ModItems.RHYTHM_STAFF.get())) {
                if (!level.isClientSide()) {
                    NetworkHooks.openScreen((ServerPlayer) player, drum, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            // Otherwise, play a note
            if (!level.isClientSide()) {
                drum.cycleNote();
                ModCriteriaTriggers.PLAY_BONGO_DRUM.trigger((ServerPlayer) player);
                float pitch = (float) Math.pow(2.0D, (double) (drum.getNote() - 12) / 12.0D);
                level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BASEDRUM.get(), SoundSource.BLOCKS, 3.0F, pitch);
            }
            if (level.isClientSide()) {
                double noteColor = (double) drum.getNote() / 24.0D;
                level.addParticle(
                        ParticleTypes.NOTE,
                        pos.getX() + 0.5D,
                        pos.getY() + 1.2D,
                        pos.getZ() + 0.5D,
                        noteColor,
                        0.0D,
                        0.0D);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState newState,
            boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof BongoDrumBlockEntity drum) {
                Containers.dropContents(level, pos, drum.getDrops());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
