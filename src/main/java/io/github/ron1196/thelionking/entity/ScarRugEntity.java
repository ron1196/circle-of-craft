package io.github.ron1196.thelionking.entity;

import io.github.ron1196.thelionking.quest.LKCharacterSpeech;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.registry.LKSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ScarRugEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(ScarRugEntity.class, EntityDataSerializers.INT);

    public static final int TYPE_SCAR = 0;
    public static final int TYPE_ZIRA = 1;

    private int talkCooldown = 40;

    public ScarRugEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public ScarRugEntity(Level level, int rugType) {
        super(LKEntityTypes.SCAR_RUG.get(), level);
        setRugType(rugType);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_TYPE, TYPE_SCAR);
    }

    public int getRugType() {
        return this.entityData.get(DATA_TYPE);
    }

    public void setRugType(int type) {
        this.entityData.set(DATA_TYPE, type);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (talkCooldown < 40) {
            talkCooldown++;
        }

        xo = getX();
        yo = getY();
        zo = getZ();

        // Gravity
        setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        move(net.minecraft.world.entity.MoverType.SELF, getDeltaMovement());

        // Friction
        double friction = 0.98D;
        if (onGround()) {
            friction = 0.588D;
        }

        setDeltaMovement(
                getDeltaMovement().x * friction,
                getDeltaMovement().y * 0.98D,
                getDeltaMovement().z * friction
        );

        if (onGround()) {
            setDeltaMovement(
                    getDeltaMovement().x,
                    getDeltaMovement().y * -0.5D,
                    getDeltaMovement().z
            );
        }
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (talkCooldown >= 40) {
            level().playSound(null, this, LKSoundEvents.LION_ROAR.get(), SoundSource.NEUTRAL,
                    1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            if (!level().isClientSide) {
                LKCharacterSpeech speech = getRugType() == TYPE_SCAR
                        ? LKCharacterSpeech.RUG_SCAR
                        : LKCharacterSpeech.RUG_ZIRA;
                player.sendSystemMessage(Component.literal(LKCharacterSpeech.giveSpeech(speech)));
            }
            talkCooldown = 0;
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    public void dropAsItem() {
        level().playSound(null, this, LKSoundEvents.LION_ANGRY.get(), SoundSource.NEUTRAL,
                1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        if (!level().isClientSide) {
            spawnAtLocation(getRugItemStack(), 0.0F);
        }
        discard();
    }

    @Override
    public @NotNull ItemStack getPickResult() {
        return getRugItemStack();
    }

    private ItemStack getRugItemStack() {
        return new ItemStack(getRugType() == TYPE_SCAR
                ? LKItems.SCAR_RUG.get()
                : LKItems.ZIRA_RUG.get());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        setRugType(tag.getInt("Type"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putInt("Type", getRugType());
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return getBoundingBox();
    }
}
