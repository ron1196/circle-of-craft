package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

/**
 * When Simba's owner is attacked, Simba targets the attacker.
 */
public class SimbaAttackPlayerAttackerGoal extends TargetGoal {

    private final SimbaEntity simba;
    private LivingEntity attacker;
    private int timestamp;

    public SimbaAttackPlayerAttackerGoal(SimbaEntity simba) {
        super(simba, false);
        this.simba = simba;
    }

    @Override
    public boolean canUse() {
        if (simba.isSitting()) return false;
        Player owner = simba.getOwner();
        if (owner == null) return false;
        attacker = owner.getLastHurtByMob();
        int time = owner.getLastHurtByMobTimestamp();
        if (time == timestamp || attacker == null) return false;
        // Don't attack the owner themselves
        if (attacker == owner) return false;
        return true;
    }

    @Override
    public void start() {
        mob.setTarget(attacker);
        Player owner = simba.getOwner();
        if (owner != null) {
            timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
