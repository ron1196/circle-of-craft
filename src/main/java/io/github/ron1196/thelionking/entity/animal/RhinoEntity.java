package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RhinoEntity extends LionKingAnimal {

    public static final float BABY_SCALE = 0.35F; // tiny calves, massive adults
    public static final float SHADOW_RADIUS = 0.9F;

    public RhinoEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTemptGoal(2, 1.0, Items.WHEAT, LionKingItems.CORN.get());
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.RHINO_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.RHINO_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.RHINO_DEATH.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.WHEAT) || stack.is(LionKingItems.CORN.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.RHINO.get().create(level);
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.IRON_INGOT, 3 + QUEST_RANDOM.nextInt(3));
    }
}
