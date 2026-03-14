package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

/**
 * Simba targets whatever entity his owner attacks.
 */
public class SimbaAttackPlayerTargetGoal extends TargetGoal {

    private final SimbaEntity simba;
    private LivingEntity ownerTarget;
    private int timestamp;

    public SimbaAttackPlayerTargetGoal(SimbaEntity simba) {
        super(simba, false);
        this.simba = simba;
    }

    @Override
    public boolean canUse() {
        if (simba.isSitting()) return false;
        Player owner = simba.getOwner();
        if (owner == null) return false;
        ownerTarget = owner.getLastHurtMob();
        int time = owner.getLastHurtMobTimestamp();
        if (time == timestamp || ownerTarget == null) return false;
        return ownerTarget != owner;
    }

    @Override
    public void start() {
        mob.setTarget(ownerTarget);
        Player owner = simba.getOwner();
        if (owner != null) {
            timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
