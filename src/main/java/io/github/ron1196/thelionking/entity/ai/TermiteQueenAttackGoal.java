package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.hostile.TermiteEntity;
import io.github.ron1196.thelionking.registry.EntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

/**
 * Boss AI goal for the Termite Queen.
 * - Targets nearest player
 * - Melee attacks when close (within 3 blocks)
 * - Spawns termite minions when far (every 5 seconds, up to 8 nearby)
 */
public class TermiteQueenAttackGoal extends Goal {

    private final Mob queen;
    private LivingEntity target;
    private int attackCooldown;
    private int spawnCooldown;
    private static final double MELEE_RANGE_SQ = 9.0;  // 3 blocks
    private static final int SPAWN_INTERVAL = 100;       // 5 seconds
    private static final int MAX_NEARBY_TERMITES = 8;
    private static final double SEARCH_RANGE = 32.0;

    public TermiteQueenAttackGoal(Mob queen) {
        this.queen = queen;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = queen.level().getNearestPlayer(queen, SEARCH_RANGE);
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive()) return false;
        return queen.distanceToSqr(target) < SEARCH_RANGE * SEARCH_RANGE;
    }

    @Override
    public void start() {
        attackCooldown = 0;
        spawnCooldown = 0;
    }

    @Override
    public void tick() {
        if (target == null) return;

        queen.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distSq = queen.distanceToSqr(target);

        if (distSq <= MELEE_RANGE_SQ) {
            // Melee attack
            if (attackCooldown <= 0) {
                queen.doHurtTarget(target);
                attackCooldown = 20; // 1 second cooldown
            }
        } else {
            // Move towards target
            queen.getNavigation().moveTo(target, 1.0D);
        }

        // Spawn termite minions periodically when target is far
        if (distSq > MELEE_RANGE_SQ) {
            spawnCooldown--;
            if (spawnCooldown <= 0) {
                spawnCooldown = SPAWN_INTERVAL;
                spawnTermites();
            }
        }

        if (attackCooldown > 0) attackCooldown--;
    }

    @Override
    public void stop() {
        target = null;
        queen.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void spawnTermites() {
        if (!(queen.level() instanceof ServerLevel serverLevel)) return;

        // Count nearby termites
        List<TermiteEntity> nearby = queen.level().getEntitiesOfClass(
                TermiteEntity.class,
                queen.getBoundingBox().inflate(16.0));
        if (nearby.size() >= MAX_NEARBY_TERMITES) return;

        // Spawn 1-3 termites near the queen
        int count = 1 + queen.getRandom().nextInt(3);
        for (int i = 0; i < count; i++) {
            TermiteEntity termite = EntityTypes.TERMITE.get().create(serverLevel);
            if (termite != null) {
                double offsetX = queen.getX() + (queen.getRandom().nextDouble() - 0.5) * 4.0;
                double offsetZ = queen.getZ() + (queen.getRandom().nextDouble() - 0.5) * 4.0;
                termite.moveTo(offsetX, queen.getY(), offsetZ, queen.getRandom().nextFloat() * 360.0F, 0.0F);
                if (target instanceof Player) {
                    termite.setTarget(target);
                }
                serverLevel.addFreshEntity(termite);
            }
        }
    }
}
