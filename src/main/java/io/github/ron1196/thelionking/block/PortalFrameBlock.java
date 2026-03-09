package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PortalFrameBlock extends Block {

    private final boolean isOutlands;

    public PortalFrameBlock(Properties properties, boolean isOutlands) {
        super(properties);
        this.isOutlands = isOutlands;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);

        // Activate with the appropriate item
        if (isOutlands) {
            // Outlands portal activated with Zira coin
            if (stack.is(io.github.ron1196.thelionking.registry.LKItems.ZIRA_COIN.get())) {
                return tryCreatePortal(level, pos);
            }
        } else {
            // Pride Lands portal activated with Rafiki's staff
            if (stack.is(io.github.ron1196.thelionking.registry.LKItems.STAFF.get())) {
                return tryCreatePortal(level, pos);
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult tryCreatePortal(Level level, BlockPos pos) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        LKPortalBlock portalBlock = isOutlands
                ? (LKPortalBlock) LKBlocks.OUTLANDS_PORTAL.get()
                : (LKPortalBlock) LKBlocks.PRIDE_LANDS_PORTAL.get();

        // Try all inner positions adjacent to this frame block
        for (BlockPos testPos : new BlockPos[]{
                pos.above(), pos.below(),
                pos.north(), pos.south(), pos.east(), pos.west()
        }) {
            if (level.getBlockState(testPos).isAir()) {
                if (portalBlock.trySpawnPortal(level, testPos)) {
                    level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
