package io.github.ron1196.thelionking.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

/** Ambient wandering for prey/passive animals at a slower pace (0.8x speed). */
public class AmbientWanderGoal extends WaterAvoidingRandomStrollGoal {

  public AmbientWanderGoal(PathfinderMob mob) {
    super(mob, 0.8D);
  }

  public AmbientWanderGoal(PathfinderMob mob, double speedModifier) {
    super(mob, speedModifier);
  }
}
