package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import io.github.ron1196.thelionking.entity.animal.BugEntity;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BugFindTrapGoal extends Goal {

    private static final int SEARCH_RANGE = 32;
    private static final int SEARCH_VERTICAL = 8;
    private static final double WALK_SPEED = 1.0;

    private final BugEntity bug;
    private BlockPos trapPos;
    private int recheckTimer;

    public BugFindTrapGoal(BugEntity bug) {
        this.bug = bug;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bug.targetTrap != null && isValidBaitedTrap(bug.targetTrap)) {
            trapPos = bug.targetTrap;
            return true;
        }
        if (bug.trapTick >= 0) return false;
        if (bug.getRandom().nextInt(20) != 0) return false;
        trapPos = findNearestBaitedTrap();
        if (trapPos != null) {
            bug.targetTrap = trapPos;
        }
        return trapPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (trapPos == null) return false;
        if (bug.trapTick >= 0) return true;
        if (!isValidBaitedTrap(trapPos)) return false;
        return !bug.getNavigation().isDone();
    }

    @Override
    public void start() {
        recheckTimer = 0;
    }

    @Override
    public void tick() {
        if (trapPos == null) return;
        if (bug.trapTick >= 0) {
            bug.getNavigation().stop();
            return;
        }
        if (--recheckTimer <= 0) {
            recheckTimer = 20;
            bug.getNavigation().moveTo(trapPos.getX() + 0.5, trapPos.getY(), trapPos.getZ() + 0.5, WALK_SPEED);
        }
    }

    @Override
    public void stop() {
        trapPos = null;
        bug.getNavigation().stop();
    }

    private boolean isValidBaitedTrap(BlockPos pos) {
        if (!bug.level().getBlockState(pos).is(LionKingBlocks.BUG_TRAP.get())) return false;
        BlockEntity be = bug.level().getBlockEntity(pos);
        if (!(be instanceof BugTrapBlockEntity trapEntity)) return false;
        for (int i = 0; i < 4; i++) {
            if (!trapEntity.getInventory().getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    private BlockPos findNearestBaitedTrap() {
        BlockPos bugPos = bug.blockPosition();
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(
                bugPos.offset(-SEARCH_RANGE, -SEARCH_VERTICAL, -SEARCH_RANGE),
                bugPos.offset(SEARCH_RANGE, SEARCH_VERTICAL, SEARCH_RANGE))) {
            if (!isValidBaitedTrap(pos)) continue;
            double dist = bugPos.distSqr(pos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = pos.immutable();
            }
        }
        return nearest;
    }
}
