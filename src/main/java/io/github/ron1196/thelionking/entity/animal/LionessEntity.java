package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.entity.ai.LionAttackGoal;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
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

public class LionessEntity extends LionKingAnimal {

    public static final float BABY_SCALE = 0.4F; // cubs are small relative to adults
    public static final float SHADOW_RADIUS = 0.6F;

    public LionessEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTemptGoal(2, 1.0, LionKingItems.HYENA_BONE.get());
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LionAttackGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return LionKingSoundEvents.LION_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return LionKingSoundEvents.LION_ANGRY.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return LionKingSoundEvents.LION_DEATH.get();
    }

    @Override
    protected CharacterSpeech getCharacterSpeech() {
        return CharacterSpeech.LIONESS;
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.GOLD_INGOT, 2 + QUEST_RANDOM.nextInt(3));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(LionKingItems.HYENA_BONE.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return (level.random.nextBoolean() ? EntityTypes.LION : EntityTypes.LIONESS)
                .get()
                .create(level);
    }
}
