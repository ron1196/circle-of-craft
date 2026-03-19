package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.entity.ai.ZazuMateGoal;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.registry.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ZazuEntity extends LionKingAnimal {

    private static final int TALK_COOLDOWN = 300;

    private int talkTick = TALK_COOLDOWN;

    public ZazuEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(3, new ZazuMateGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void tick() {
        super.tick();
        if (talkTick < TALK_COOLDOWN) {
            talkTick++;
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide || isBaby()) {
            return super.mobInteract(player, hand);
        }
        if (talkTick >= TALK_COOLDOWN) {
            if (isMorning()) {
                player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(CharacterSpeech.MORNING_REPORT)));
                if (player instanceof ServerPlayer serverPlayer) {
                    LionKingCriteriaTriggers.SPEAK_TO_ZAZU.trigger(serverPlayer);
                }
            } else {
                player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(CharacterSpeech.ZAZU_SLEEPING)));
            }
            talkTick = 0;
        }
        return InteractionResult.SUCCESS;
    }

    private boolean isMorning() {
        long time = level().getDayTime() % 24000;
        return time > 23000L || time < 4500L;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.ZAZU_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.ZAZU_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.ZAZU_HURT.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }
}
