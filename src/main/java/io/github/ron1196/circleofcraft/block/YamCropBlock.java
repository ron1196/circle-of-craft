package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Yam crop that can grow on grass and dirt (for worldgen) in addition to farmland.
 * The old mod placed wild yams on grass blocks at full maturity.
 */
public class YamCropBlock extends CropBlock {

    public YamCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModItems.YAM.get();
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.DIRT);
    }
}
