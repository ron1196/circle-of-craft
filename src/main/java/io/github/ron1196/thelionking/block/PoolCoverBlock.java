package io.github.ron1196.thelionking.block;

import net.minecraft.world.level.block.Block;

/**
 * Pool Cover — indestructible block sealing the Outwater pool in Zira's Mound.
 * Only removed by quest progression (COLLECT_INGOTS → THROW_IN_OUTWATER).
 * Uses the same texture as ZiraMoundGateBlock.
 */
public class PoolCoverBlock extends Block {

    public PoolCoverBlock(Properties properties) {
        super(properties);
    }
}
