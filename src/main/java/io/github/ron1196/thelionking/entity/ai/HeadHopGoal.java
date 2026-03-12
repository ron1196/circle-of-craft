package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaHeadEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Custom hop goal that makes the skeletal hyena head jump toward its target,
 * matching the old mod's slime-like bouncing behavior.
 */
public class HeadHopGoal extends Goal {

    private final SkeletalHyenaHeadEntity head;
    private int jumpDelay;

    public HeadHopGoal(SkeletalHyenaHeadEntity head) {
        this.head = head;
        this.jumpDelay = head.getRandom().nextInt(20) + 10;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return head.onGround();
    }

    @Override
    public void tick() {
        if (--jumpDelay > 0) {
            return;
        }
        jumpDelay = head.getRandom().nextInt(20) + 10;

        LivingEntity target = head.getTarget();
        if (target != null) {
            jumpDelay /= 3;
            double dx = target.getX() - head.getX();
            double dz = target.getZ() - head.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            // Rotate to face target
            float targetYRot = (float) (Math.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
            head.setYRot(targetYRot);
            head.yBodyRot = targetYRot;
            head.yHeadRot = targetYRot;
            head.getLookControl().setLookAt(target, 360.0F, 360.0F);

            if (dist > 0.01D) {
                double speed = 0.6D;
                head.setDeltaMovement(
                        (dx / dist) * speed,
                        0.42D,
                        (dz / dist) * speed
                );
            }
        } else {
            double angle = head.getRandom().nextDouble() * Math.PI * 2.0D;
            head.setDeltaMovement(
                    Math.cos(angle) * 0.3D,
                    0.42D,
                    Math.sin(angle) * 0.3D
            );
        }

        head.hasImpulse = true;
        SoundEventHelper.playHurtSound(head);
    }

    private static class SoundEventHelper {
        static void playHurtSound(SkeletalHyenaHeadEntity head) {
            net.minecraft.sounds.SoundEvent sound = net.minecraft.sounds.SoundEvents.SKELETON_HURT;
            head.playSound(sound, 0.4F,
                    ((head.getRandom().nextFloat() - head.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }
}
