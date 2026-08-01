package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.entity.ai.AmbientPanicGoal;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GemsbokEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.5F; // antelope calves are proportional
    public static final float SHADOW_RADIUS = 0.6F;

    public GemsbokEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(
                2, new AvoidEntityGoal<>(this, LivingEntity.class, 12.0F, 1.0D, 1.5D, e -> e instanceof LionEntity));
        addTemptGoal(3, 1.0, Items.WHEAT, ModItems.CORN.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23);
    }

    @Override
    protected CharacterSpeech getCharacterSpeech() {
        return CharacterSpeech.GEMSBOK;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.WHEAT) || stack.is(ModItems.CORN.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.GEMSBOK.get().create(level);
    }
}
