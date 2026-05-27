package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.world.dimension.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Hyena Meal — bonemeal for Lion King dimensions. Grows saplings (45% chance), fertilizes crops,
 * and spreads vegetation on grass/sand. Only works in Pride Lands, Outlands, and Upendi dimensions.
 */
public class HyenaMealItem extends Item {

    public HyenaMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        if (!isLKDimension(level)) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // Grow saplings and crops (anything bonemealable)
        if (block instanceof BonemealableBlock bonemealable) {
            if (bonemealable.isValidBonemealTarget(level, pos, state, false)) {
                if (bonemealable.isBonemealSuccess(level, level.random, pos, state)) {
                    bonemealable.performBonemeal((ServerLevel) level, level.random, pos, state);
                }
                context.getItemInHand().shrink(1);
                level.levelEvent(2005, pos, 0);
                return InteractionResult.SUCCESS;
            }
        }

        // Spread vegetation on grass blocks
        if (block == Blocks.GRASS_BLOCK) {
            BoneMealItem.growCrop(context.getItemInHand(), level, pos);
            context.getItemInHand().shrink(1);
            level.levelEvent(2005, pos, 0);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static boolean isLKDimension(Level level) {
        return level.dimension() == Dimensions.PRIDE_LANDS_LEVEL
                || level.dimension() == Dimensions.OUTLANDS_LEVEL
                || level.dimension() == Dimensions.UPENDI_LEVEL;
    }
}
