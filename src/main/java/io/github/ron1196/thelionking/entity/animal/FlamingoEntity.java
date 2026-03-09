package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.registry.LKSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class FlamingoEntity extends LKAnimal {

    public FlamingoEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LKAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LKSoundEvents.FLAMINGO_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LKSoundEvents.FLAMINGO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LKSoundEvents.FLAMINGO_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return null;
    }
}
