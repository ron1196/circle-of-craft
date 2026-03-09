package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.entity.ai.SimbaAttackGoal;
import io.github.ron1196.thelionking.entity.ai.SimbaAttackPlayerAttackerGoal;
import io.github.ron1196.thelionking.entity.ai.SimbaAttackPlayerTargetGoal;
import io.github.ron1196.thelionking.entity.ai.SimbaFollowOwnerGoal;
import io.github.ron1196.thelionking.entity.ai.SimbaWanderGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class SimbaEntity extends PathfinderMob {

    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_SITTING =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_BABY =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.BOOLEAN);

    public SimbaEntity(EntityType<? extends SimbaEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_SITTING, false);
        this.entityData.define(DATA_BABY, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SimbaAttackGoal(this));
        this.goalSelector.addGoal(4, new SimbaFollowOwnerGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new SimbaWanderGoal(this));
        this.targetSelector.addGoal(1, new SimbaAttackPlayerAttackerGoal(this));
        this.targetSelector.addGoal(2, new SimbaAttackPlayerTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    // Owner management
    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    public Player getOwner() {
        return getOwnerUUID().map(uuid -> level().getPlayerByUUID(uuid)).orElse(null);
    }

    public boolean isSitting() {
        return this.entityData.get(DATA_SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(DATA_SITTING, sitting);
    }

    @Override
    public boolean isBaby() {
        return this.entityData.get(DATA_BABY);
    }

    public void setBaby(boolean baby) {
        this.entityData.set(DATA_BABY, baby);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;

        if (getOwnerUUID().isEmpty()) {
            setOwnerUUID(player.getUUID());
            return InteractionResult.SUCCESS;
        }

        if (player.getUUID().equals(getOwnerUUID().orElse(null))) {
            // Toggle sitting
            setSitting(!isSitting());
            this.navigation.stop();
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();
        if (isSitting()) {
            this.navigation.stop();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        getOwnerUUID().ifPresent(uuid -> tag.putUUID("Owner", uuid));
        tag.putBoolean("Sitting", isSitting());
        tag.putBoolean("Baby", isBaby());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) setOwnerUUID(tag.getUUID("Owner"));
        setSitting(tag.getBoolean("Sitting"));
        setBaby(tag.getBoolean("Baby"));
    }

}
