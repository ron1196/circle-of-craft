package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.LKBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FurRugBlockEntity extends BlockEntity {

    private int direction;

    public FurRugBlockEntity(BlockPos pos, BlockState state) {
        super(LKBlockEntityTypes.FUR_RUG.get(), pos, state);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction & 3;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Direction", direction);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        direction = tag.getInt("Direction");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
