package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.entity.ai.ZazuLayEggGoal;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ZazuEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.4F; // hornbill chicks are small
    public static final float SHADOW_RADIUS = 0.25F;

    private static final int TALK_COOLDOWN = 300;

    private int talkTick = TALK_COOLDOWN;

    public ZazuEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        addTemptGoal(3, 1.0, Items.WHEAT_SEEDS, ModItems.CORN_KERNELS.get());
        this.goalSelector.addGoal(3, new ZazuLayEggGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
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
        ItemStack held = player.getItemInHand(hand);
        if (isFood(held)) {
            return super.mobInteract(player, hand);
        }
        if (level().isClientSide || isBaby()) {
            return super.mobInteract(player, hand);
        }
        if (talkTick >= TALK_COOLDOWN) {
            if (isMorning()) {
                player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(CharacterSpeech.MORNING_REPORT)));
                if (player instanceof ServerPlayer serverPlayer) {
                    ModCriteriaTriggers.SPEAK_TO_ZAZU.trigger(serverPlayer);
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
        return ModSoundEvents.ZAZU_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEvents.ZAZU_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEvents.ZAZU_HURT.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.WHEAT_SEEDS) || stack.is(ModItems.CORN_KERNELS.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.ZAZU.get().create(level);
    }
}
