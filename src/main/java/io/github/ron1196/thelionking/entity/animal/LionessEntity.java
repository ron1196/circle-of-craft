package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class LionessEntity extends LKAnimal {

    public LionessEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LKAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LKSoundEvents.LION_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LKSoundEvents.LION_ANGRY.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LKSoundEvents.LION_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return LKEntityTypes.LION.get().create(level);
    }
}
