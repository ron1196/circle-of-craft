package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.registry.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FlamingoEntity extends LionKingAnimal {

    public FlamingoEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FLAMINGO_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FLAMINGO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FLAMINGO_DEATH.get();
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.PINK_DYE, 3 + QUEST_RANDOM.nextInt(4));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }
}
