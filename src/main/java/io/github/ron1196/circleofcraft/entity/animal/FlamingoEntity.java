package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
import javax.annotation.Nullable;
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

public class FlamingoEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.3F; // chicks are small fluffy things
    public static final float SHADOW_RADIUS = 0.3F;

    public FlamingoEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        addTemptGoal(3, 1.0, Items.COD, Items.SALMON);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ModSoundEvents.FLAMINGO_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEvents.FLAMINGO_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEvents.FLAMINGO_DEATH.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.FLAMINGO.get().create(level);
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.PINK_DYE, 3 + QUEST_RANDOM.nextInt(4));
    }
}
