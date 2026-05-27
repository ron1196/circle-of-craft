package io.github.ron1196.circleofcraft.entity.ai;

import io.github.ron1196.circleofcraft.block.entity.BugTrapBlockEntity;
import io.github.ron1196.circleofcraft.entity.animal.BugEntity;
import io.github.ron1196.circleofcraft.registry.ModBlockTags;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class BugFindTrapGoal extends Goal {

    private static final double WALK_SPEED = 1.6;
    private static final double CLOSE_SWITCH_DIST_SQR = 3.0 * 3.0;
    private static final double INSIDE_OFFSET = 0.4;
    private static final int SCAN_RADIUS = 32;
    private static final int SCAN_VERTICAL = 8;

    private final BugEntity bug;
    private BlockPos trapPos;
    private Direction face;
    private BlockPos approachPos;
    private int recheckTimer;

    public BugFindTrapGoal(BugEntity bug) {
        this.bug = bug;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bug.targetTrap == null || bug.targetFace == null || !isFaceBaited(bug.targetTrap, bug.targetFace)) {
            scanForBaitedTrap();
        }
        if (bug.targetTrap == null || bug.targetFace == null) return false;
        if (!isFaceBaited(bug.targetTrap, bug.targetFace)) return false;
        trapPos = bug.targetTrap;
        face = bug.targetFace;
        approachPos = trapPos.relative(face);
        return true;
    }

    private void scanForBaitedTrap() {
        BlockPos here = bug.blockPosition();
        BlockPos bestTrap = null;
        Direction bestFace = null;
        double bestDistSqr = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                here.offset(-SCAN_RADIUS, -SCAN_VERTICAL, -SCAN_RADIUS),
                here.offset(SCAN_RADIUS, SCAN_VERTICAL, SCAN_RADIUS))) {
            if (!bug.level().getBlockState(pos).is(ModBlockTags.BUG_TRAPS)) continue;
            if (!(bug.level().getBlockEntity(pos) instanceof BugTrapBlockEntity trap)) continue;
            for (Direction f : Direction.Plane.HORIZONTAL) {
                int slot = BugEntity.slotForFace(f);
                if (slot < 0 || trap.getInventory().getStackInSlot(slot).isEmpty()) continue;
                BlockPos approach = pos.relative(f);
                double d = bug.position().distanceToSqr(approach.getX() + 0.5, approach.getY(), approach.getZ() + 0.5);
                if (d < bestDistSqr) {
                    bestDistSqr = d;
                    bestTrap = pos.immutable();
                    bestFace = f;
                }
            }
        }
        bug.targetTrap = bestTrap;
        bug.targetFace = bestFace;
    }

    @Override
    public boolean canContinueToUse() {
        if (trapPos == null || face == null) return false;
        if (bug.trapTick >= 0) return true;
        return isFaceBaited(trapPos, face);
    }

    @Override
    public void tick() {
        if (approachPos == null || trapPos == null || face == null) return;

        Vec3 trapCenter = new Vec3(trapPos.getX() + 0.5, trapPos.getY(), trapPos.getZ() + 0.5);
        double distToTrapSqr = bug.position().distanceToSqr(trapCenter);

        if (distToTrapSqr <= CLOSE_SWITCH_DIST_SQR || bug.trapTick >= 0) {
            bug.getNavigation().stop();
            Vec3 inside = new Vec3(
                    trapPos.getX() + 0.5 + face.getStepX() * INSIDE_OFFSET,
                    trapPos.getY(),
                    trapPos.getZ() + 0.5 + face.getStepZ() * INSIDE_OFFSET);
            bug.getMoveControl().setWantedPosition(inside.x, inside.y, inside.z, WALK_SPEED);
        } else if (bug.getNavigation().isDone() || --recheckTimer <= 0) {
            recheckTimer = 20;
            Vec3 approachCenter = new Vec3(approachPos.getX() + 0.5, approachPos.getY(), approachPos.getZ() + 0.5);
            bug.getNavigation().moveTo(approachCenter.x, approachCenter.y, approachCenter.z, WALK_SPEED);
        }
    }

    @Override
    public void stop() {
        if (bug.trapTick < 0) {
            bug.targetTrap = null;
            bug.targetFace = null;
        }
        trapPos = null;
        face = null;
        approachPos = null;
        bug.getNavigation().stop();
    }

    private boolean isFaceBaited(BlockPos pos, Direction face) {
        if (!bug.level().getBlockState(pos).is(ModBlockTags.BUG_TRAPS)) return false;
        BlockEntity be = bug.level().getBlockEntity(pos);
        if (!(be instanceof BugTrapBlockEntity trapEntity)) return false;
        int slot = BugEntity.slotForFace(face);
        if (slot < 0) return false;
        return !trapEntity.getInventory().getStackInSlot(slot).isEmpty();
    }
}
