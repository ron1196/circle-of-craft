package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import io.github.ron1196.thelionking.entity.ai.BugFindTrapGoal;
import io.github.ron1196.thelionking.entity.ai.BugFleePlayerGoal;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

    private static final int TRAP_SCAN_INTERVAL = 5;
    private static final int TRAP_SCAN_RADIUS = 32;
    private static final int TRAP_SCAN_VERTICAL = 8;

    public int panicTicks = 0;

    @Nullable
    public BlockPos targetTrap = null;

    @Nullable
    public Direction targetFace = null;

    public int trapTick = -1;

    public static int slotForFace(@NotNull Direction face) {
        return switch (face) {
            case NORTH -> 0;
            case WEST -> 1;
            case EAST -> 2;
            case SOUTH -> 3;
            default -> -1;
        };
    }

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
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    public void startPanic() {
        this.panicTicks = PANIC_DURATION_TICKS;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;
        if (this.panicTicks > 0) this.panicTicks--;
        if (this.targetTrap == null && this.trapTick < 0 && this.tickCount % TRAP_SCAN_INTERVAL == 0) {
            scanForBaitedTrap();
        }
    }

    private void scanForBaitedTrap() {
        BlockPos here = this.blockPosition();
        BlockPos bestTrap = null;
        Direction bestFace = null;
        double bestDistSqr = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                here.offset(-TRAP_SCAN_RADIUS, -TRAP_SCAN_VERTICAL, -TRAP_SCAN_RADIUS),
                here.offset(TRAP_SCAN_RADIUS, TRAP_SCAN_VERTICAL, TRAP_SCAN_RADIUS))) {
            if (!this.level().getBlockState(pos).is(LionKingBlocks.BUG_TRAP.get())) continue;
            if (!(this.level().getBlockEntity(pos) instanceof BugTrapBlockEntity trap)) continue;
            for (Direction face : Direction.Plane.HORIZONTAL) {
                int slot = slotForFace(face);
                if (slot < 0 || trap.getInventory().getStackInSlot(slot).isEmpty()) continue;
                BlockPos approach = pos.relative(face);
                double d = this.position().distanceToSqr(approach.getX() + 0.5, approach.getY(), approach.getZ() + 0.5);
                if (d < bestDistSqr) {
                    bestDistSqr = d;
                    bestTrap = pos.immutable();
                    bestFace = face;
                }
            }
        }
        if (bestTrap != null) {
            this.targetTrap = bestTrap;
            this.targetFace = bestFace;
        }
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
        if (targetFace != null) {
            tag.putString("TargetFace", targetFace.getSerializedName());
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
        targetFace = tag.contains("TargetFace") ? Direction.byName(tag.getString("TargetFace")) : null;
    }
}
