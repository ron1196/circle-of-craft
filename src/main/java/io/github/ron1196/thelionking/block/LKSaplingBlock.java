package io.github.ron1196.thelionking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class LKSaplingBlock extends SaplingBlock {

    public LKSaplingBlock(AbstractTreeGrower grower, Properties properties) {
        super(grower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(net.minecraft.world.level.block.Blocks.SAND);
    }
}
