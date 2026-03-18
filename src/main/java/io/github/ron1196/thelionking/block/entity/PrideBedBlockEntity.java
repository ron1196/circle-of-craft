package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.LKBlockEntityTypes;
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
        return LKBlockEntityTypes.PRIDE_BED.get();
    }
}
