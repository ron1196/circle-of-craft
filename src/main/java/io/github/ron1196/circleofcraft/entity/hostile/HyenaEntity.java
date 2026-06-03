package io.github.ron1196.circleofcraft.entity.hostile;

import io.github.ron1196.circleofcraft.entity.ai.HyenaFollowScarGoal;
import io.github.ron1196.circleofcraft.entity.animal.*;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class HyenaEntity extends Monster {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(HyenaEntity.class, EntityDataSerializers.INT);

    public HyenaEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
    }

    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(DATA_VARIANT, variant);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull DifficultyInstance difficulty,
            @NotNull MobSpawnType spawnType,
            @Nullable SpawnGroupData groupData,
            @Nullable CompoundTag tag) {
        setVariant(this.random.nextInt(3));
        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setVariant(tag.getInt("Variant"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // Flee from lions when alone (not in a pack of 3+)
        this.goalSelector.addGoal(
                1,
                new AvoidEntityGoal<>(
                        this,
                        LivingEntity.class,
                        12.0F,
                        1.0D,
                        1.5D,
                        e -> e instanceof LionEntity
                                && this.level()
                                                .getEntitiesOfClass(
                                                        HyenaEntity.class,
                                                        this.getBoundingBox().inflate(16.0))
                                                .size()
                                        < 3));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(3, new HyenaFollowScarGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

        // Hyenas only attack lions when in a pack (3+ nearby)
        this.targetSelector.addGoal(
                3,
                new NearestAttackableTargetGoal<>(
                        this,
                        LivingEntity.class,
                        2,
                        true,
                        false,
                        e -> e instanceof LionEntity
                                && this.level()
                                                .getEntitiesOfClass(
                                                        HyenaEntity.class,
                                                        this.getBoundingBox().inflate(16.0))
                                                .size()
                                        >= 3));

        this.targetSelector.addGoal(
                4,
                new NearestAttackableTargetGoal<>(
                        this,
                        LivingEntity.class,
                        2,
                        true,
                        false,
                        e -> e instanceof ZebraEntity
                                || e instanceof DikDikEntity
                                || e instanceof GemsbokEntity
                                || e instanceof FlamingoEntity
                                || e instanceof ZazuEntity
                                || e instanceof BugEntity));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (this.random.nextInt(20) <= looting) {
            ItemStack head = new ItemStack(ModBlocks.HYENA_HEAD.get());
            CompoundTag blockEntityTag = new CompoundTag();
            blockEntityTag.putInt("HyenaType", getVariant());
            BlockItem.setBlockEntityData(head, BlockEntityTypes.HYENA_HEAD.get(), blockEntityTag);
            this.spawnAtLocation(head);
        }
    }
}
