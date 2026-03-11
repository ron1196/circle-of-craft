package io.github.ron1196.thelionking.entity.hostile;

import io.github.ron1196.thelionking.entity.ai.SwellGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TermiteEntity extends Monster implements SwellGoal.Swellable {

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(
            TermiteEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EXPLODING = SynchedEntityData.defineId(
            TermiteEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int FUSE_TIME = 20;
    private static final float EXPLOSION_RADIUS = 1.7F;
    private static final double SWELL_TRIGGER_DISTANCE = 3.0;
    private static final double MELEE_SPEED = 1.0;
    private static final double WANDER_SPEED = 0.8;
    private static final float LOOK_DISTANCE = 8.0F;
    private static final double MAX_HEALTH = 9.0;
    private static final double MOVEMENT_SPEED = 0.25;
    private static final int EXPERIENCE_REWARD = 3;
    private static final float FUSE_SOUND_VOLUME = 1.0F;
    private static final float FUSE_SOUND_PITCH = 0.5F;

    private int timeSinceIgnited;

    public TermiteEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR, -1);
        this.entityData.define(DATA_EXPLODING, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SwellGoal(this, SWELL_TRIGGER_DISTANCE, this::isExploding));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, MELEE_SPEED, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, WANDER_SPEED));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, LOOK_DISTANCE));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isExploding() {
        return this.entityData.get(DATA_EXPLODING);
    }

    public void setExploding(boolean exploding) {
        this.entityData.set(DATA_EXPLODING, exploding);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public int getExperienceReward() {
        return EXPERIENCE_REWARD;
    }

    @Override
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    @Override
    public void setSwellDir(int dir) {
        this.entityData.set(DATA_SWELL_DIR, dir);
    }

    @Override
    public void tick() {
        if (this.isAlive() && this.isExploding()) {
            int swellDir = this.getSwellDir();
            if (swellDir > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.TNT_PRIMED, FUSE_SOUND_VOLUME, FUSE_SOUND_PITCH);
            }
            this.timeSinceIgnited += swellDir;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
            if (this.timeSinceIgnited >= FUSE_TIME) {
                this.timeSinceIgnited = FUSE_TIME;
                if (!this.level().isClientSide) {
                    this.level().explode(
                            this,
                            this.getX(), this.getY(), this.getZ(),
                            EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB
                    );
                    this.discard();
                }
            }
        }
        super.tick();
    }

    /** Returns 0..1 flash progress for rendering. */
    public float getSwelling(float partialTick) {
        if (!this.isExploding()) return 0.0F;
        return (this.timeSinceIgnited + partialTick) / (float) FUSE_TIME;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short) this.timeSinceIgnited);
        tag.putBoolean("Exploding", this.isExploding());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fuse")) {
            this.timeSinceIgnited = tag.getShort("Fuse");
        }
        if (tag.contains("Exploding")) {
            this.setExploding(tag.getBoolean("Exploding"));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED);
    }
}
