package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.entity.ai.BugFindTrapGoal;
import io.github.ron1196.thelionking.entity.ai.BugFleePlayerGoal;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BugEntity extends LionKingAnimal {

    public static final float BABY_SCALE = 0.5F;
    public static final float SHADOW_RADIUS = 0.15F;

    public static final int PANIC_DURATION_TICKS = 200;
    public static final int CONSUME_DURATION_TICKS = 40;

    public int panicTicks = 0;

    @Nullable
    public BlockPos targetTrap = null;

    public int trapTick = -1;

    public BugEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BugFleePlayerGoal(this));
        this.goalSelector.addGoal(2, new BugFindTrapGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    public void startPanic() {
        this.panicTicks = PANIC_DURATION_TICKS;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.panicTicks > 0) {
            this.panicTicks--;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PanicTicks", panicTicks);
        tag.putInt("TrapTick", trapTick);
        if (targetTrap != null) {
            tag.putInt("TargetTrapX", targetTrap.getX());
            tag.putInt("TargetTrapY", targetTrap.getY());
            tag.putInt("TargetTrapZ", targetTrap.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        panicTicks = tag.getInt("PanicTicks");
        trapTick = tag.contains("TrapTick") ? tag.getInt("TrapTick") : -1;
        if (tag.contains("TargetTrapX")) {
            targetTrap = new BlockPos(tag.getInt("TargetTrapX"), tag.getInt("TargetTrapY"), tag.getInt("TargetTrapZ"));
        } else {
            targetTrap = null;
        }
    }
}
