package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.entity.ai.AmbientAvoidGoal;
import io.github.ron1196.circleofcraft.entity.ai.AmbientPanicGoal;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class DikDikEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.55F; // already small animals, fawns are large relative
    public static final float SHADOW_RADIUS = 0.3F;

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(DikDikEntity.class, EntityDataSerializers.INT);

    public DikDikEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
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
            @Nullable SpawnGroupData groupData) {
        setVariant(this.random.nextInt(3));
        return super.finalizeSpawn(level, difficulty, spawnType, groupData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.WHEAT) || stack.is(ModItems.CORN.get());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(2, new AmbientAvoidGoal(this));
        addTemptGoal(3, 1.0, Items.WHEAT, ModItems.CORN.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        DikDikEntity fawn = EntityTypes.DIKDIK.get().create(level);
        if (fawn != null) {
            // Breeding skips finalizeSpawn, where the variant is normally rolled.
            DikDikEntity parent = this.random.nextBoolean() && mate instanceof DikDikEntity other ? other : this;
            fawn.setVariant(parent.getVariant());
        }
        return fawn;
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
}
