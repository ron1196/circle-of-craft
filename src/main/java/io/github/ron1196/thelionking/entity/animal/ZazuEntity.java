package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.entity.ai.ZazuMateGoal;
import io.github.ron1196.thelionking.registry.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ZazuEntity extends LionKingAnimal {

    public ZazuEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.PanicGoal(this, 1.5));
        this.goalSelector.addGoal(3, new ZazuMateGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZAZU_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZAZU_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZAZU_HURT.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }
}
