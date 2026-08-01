package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.entity.ai.AmbientAvoidGoal;
import io.github.ron1196.circleofcraft.entity.ai.AmbientPanicGoal;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ZebraEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.5F; // foals are relatively large at birth
    public static final float SHADOW_RADIUS = 0.7F;

    public ZebraEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(2, new AmbientAvoidGoal(this));
        addTemptGoal(3, 1.0, net.minecraft.world.item.Items.WHEAT, ModItems.CORN.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ModSoundEvents.ZEBRA_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSoundEvents.ZEBRA_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSoundEvents.ZEBRA_DEATH.get();
    }

    @Override
    protected CharacterSpeech getCharacterSpeech() {
        return CharacterSpeech.ZEBRA;
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(Items.LEATHER, 3 + QUEST_RANDOM.nextInt(3));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(ModItems.JAR_EMPTY.get()) && !isBaby()) {
            player.playSound(ModSoundEvents.ZEBRA_AMBIENT.get(), 1.0F, 1.0F);
            ItemStack milkJar = new ItemStack(ModItems.JAR_MILK.get());
            player.setItemInHand(hand, ItemUtils.createFilledResult(held, player, milkJar));
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.WHEAT) || stack.is(ModItems.CORN.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.ZEBRA.get().create(level);
    }
}
