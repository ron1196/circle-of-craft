package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Kiwano Stem — a crop that grows on tilled sand or vanilla farmland.
 */
public class KiwanoStemBlock extends CropBlock {

    public KiwanoStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return LionKingItems.KIWANO_SEEDS.get();
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(LionKingBlocks.TILLED_SAND.get()) || super.mayPlaceOn(state, level, pos);
    }
}
