package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.EnumSet;

/**
 * Bugs pathfind to nearby baited BugTrap blocks within 16 blocks.
 * The bug will walk towards the trap if it contains bait (any non-empty input slot).
 */
public class BugFindTrapGoal extends Goal {

    private final PathfinderMob bug;
    private BlockPos trapPos;
    private static final int SEARCH_RANGE = 16;
    private int recheckTimer;

    public BugFindTrapGoal(PathfinderMob bug) {
        this.bug = bug;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bug.getRandom().nextInt(60) != 0) return false; // Don't search every tick
        trapPos = findNearestBaitedTrap();
        return trapPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (trapPos == null) return false;
        // Stop if we've reached the trap (within 2 blocks)
        if (bug.blockPosition().closerThan(trapPos, 2.0)) return false;
        // Verify trap still exists and has bait
        if (!bug.level().getBlockState(trapPos).is(LKBlocks.BUG_TRAP.get())) return false;
        return true;
    }

    @Override
    public void start() {
        recheckTimer = 0;
    }

    @Override
    public void tick() {
        if (trapPos == null) return;
        if (--recheckTimer <= 0) {
            recheckTimer = 20;
            bug.getNavigation().moveTo(
                    trapPos.getX() + 0.5, trapPos.getY(), trapPos.getZ() + 0.5, 1.0D);
        }
    }

    @Override
    public void stop() {
        trapPos = null;
        bug.getNavigation().stop();
    }

    private BlockPos findNearestBaitedTrap() {
        BlockPos bugPos = bug.blockPosition();
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(
                bugPos.offset(-SEARCH_RANGE, -4, -SEARCH_RANGE),
                bugPos.offset(SEARCH_RANGE, 4, SEARCH_RANGE))) {
            if (!bug.level().getBlockState(pos).is(LKBlocks.BUG_TRAP.get())) continue;

            // Check if trap has bait
            BlockEntity be = bug.level().getBlockEntity(pos);
            if (be instanceof BugTrapBlockEntity trapEntity) {
                boolean hasBait = false;
                for (int i = 0; i < 4; i++) {
                    if (!trapEntity.getInventory().getStackInSlot(i).isEmpty()) {
                        hasBait = true;
                        break;
                    }
                }
                if (!hasBait) continue;
            } else {
                continue;
            }

            double dist = bugPos.distSqr(pos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = pos.immutable();
            }
        }
        return nearest;
    }
}
