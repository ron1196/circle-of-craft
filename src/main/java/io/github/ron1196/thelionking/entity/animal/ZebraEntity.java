package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.entity.ai.AmbientAvoidGoal;
import io.github.ron1196.thelionking.entity.ai.AmbientPanicGoal;
import io.github.ron1196.thelionking.registry.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ZebraEntity extends LionKingAnimal {

    public ZebraEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(2, new AmbientAvoidGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.ZEBRA_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.ZEBRA_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ZEBRA_DEATH.get();
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.LEATHER, 3 + QUEST_RANDOM.nextInt(3));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }
}
