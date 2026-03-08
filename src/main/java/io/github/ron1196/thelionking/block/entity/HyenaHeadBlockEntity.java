package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.LKBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HyenaHeadBlockEntity extends BlockEntity {

    private int hyenaType;
    private int rotation;

    public HyenaHeadBlockEntity(BlockPos pos, BlockState state) {
        super(LKBlockEntityTypes.HYENA_HEAD.get(), pos, state);
    }

    public int getHyenaType() {
        return hyenaType;
    }

    public void setHyenaType(int hyenaType) {
        this.hyenaType = Math.max(0, Math.min(7, hyenaType));
        setChanged();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation & 15;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("HyenaType", hyenaType);
        tag.putInt("Rotation", rotation);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        hyenaType = tag.getInt("HyenaType");
        rotation = tag.getInt("Rotation");
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
