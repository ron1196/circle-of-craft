package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class HyenaHeadBlockEntity extends BlockEntity {

    private int hyenaType;
    private int rotation;

    public HyenaHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.HYENA_HEAD.get(), pos, state);
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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("HyenaType", hyenaType);
        output.putInt("Rotation", rotation);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        hyenaType = input.getIntOr("HyenaType", 0);
        rotation = input.getIntOr("Rotation", 0);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        saveAdditional(output);
        tag.merge(output.buildResult());
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
