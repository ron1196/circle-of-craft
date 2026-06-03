package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.entity.ai.LionAttackGoal;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class LionEntity extends ModAnimal implements GenderedAnimal {

    public static final float BABY_SCALE = 0.4F;
    public static final float SHADOW_RADIUS = 0.7F;

    public static final float FEMALE_SKEW = 0.70F;

    private static final String NBT_GENDER = "Gender";
    private static final byte GENDER_MALE = 0;
    private static final byte GENDER_FEMALE = 1;

    private static final UUID FEMALE_HEALTH_MODIFIER_UUID = UUID.fromString("b0767ea1-cf19-438d-b30d-ebe793542be5");
    private static final UUID FEMALE_ATTACK_MODIFIER_UUID = UUID.fromString("2918936f-5a75-466f-a524-e7facbaa83bb");
    private static final double FEMALE_HEALTH_DELTA = -4.0;
    private static final double FEMALE_ATTACK_DELTA = -1.0;

    private static final EntityDataAccessor<Boolean> DATA_IS_FEMALE =
            SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);

    public LionEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_FEMALE, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTemptGoal(3, 1.0, Items.BEEF, Items.PORKCHOP, Items.MUTTON, Items.RABBIT, Items.CHICKEN);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LionAttackGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ModSoundEvents.LION_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEvents.LION_ANGRY.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEvents.LION_DEATH.get();
    }

    @Override
    protected CharacterSpeech getCharacterSpeech() {
        return CharacterSpeech.LION;
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.GOLD_INGOT, 2 + QUEST_RANDOM.nextInt(3));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        if (!stack.getItem().isEdible()) return false;
        var food = stack.getItem().getFoodProperties();
        return food != null && food.isMeat();
    }

    @Override
    public boolean canMate(@NotNull net.minecraft.world.entity.animal.Animal other) {
        if (!super.canMate(other)) return false;
        if (other instanceof GenderedAnimal gendered) {
            return canBreedWith(gendered);
        }
        return true;
    }

    @Override
    public @NotNull Gender getGender() {
        return Gender.fromIsFemale(this.entityData.get(DATA_IS_FEMALE));
    }

    public void setGender(@NotNull Gender gender) {
        this.entityData.set(DATA_IS_FEMALE, gender.isFemale());
        applyGenderModifiers();
    }

    private void applyGenderModifiers() {
        AttributeInstance health = getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attack = getAttribute(Attributes.ATTACK_DAMAGE);
        if (health != null) health.removeModifier(FEMALE_HEALTH_MODIFIER_UUID);
        if (attack != null) attack.removeModifier(FEMALE_ATTACK_MODIFIER_UUID);
        if (getGender() == Gender.FEMALE) {
            if (health != null) {
                health.addPermanentModifier(new AttributeModifier(
                        FEMALE_HEALTH_MODIFIER_UUID,
                        "lion_female_health",
                        FEMALE_HEALTH_DELTA,
                        AttributeModifier.Operation.ADDITION));
                setHealth(Math.min(getHealth(), (float) health.getValue()));
            }
            if (attack != null) {
                attack.addPermanentModifier(new AttributeModifier(
                        FEMALE_ATTACK_MODIFIER_UUID,
                        "lion_female_attack",
                        FEMALE_ATTACK_DELTA,
                        AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte(NBT_GENDER, getGender().isFemale() ? GENDER_FEMALE : GENDER_MALE);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(NBT_GENDER)) {
            setGender(tag.getByte(NBT_GENDER) == GENDER_FEMALE ? Gender.FEMALE : Gender.MALE);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull DifficultyInstance difficulty,
            @NotNull MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnData,
            @Nullable CompoundTag dataTag) {
        initializeGender(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        LionEntity cub = EntityTypes.LION.get().create(level);
        if (cub != null) {
            cub.initializeGender(level);
        }
        return cub;
    }

    public void initializeGender(@NotNull ServerLevelAccessor level) {
        Gender rolled = level.getRandom().nextFloat() < FEMALE_SKEW ? Gender.FEMALE : Gender.MALE;
        setGender(rolled);
    }

    public boolean shouldShowMane() {
        return !isBaby() && getGender() == Gender.MALE;
    }
}
