package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PrideBedBlockEntity extends BedBlockEntity {

    public PrideBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return BlockEntityTypes.PRIDE_BED.get();
    }
}
