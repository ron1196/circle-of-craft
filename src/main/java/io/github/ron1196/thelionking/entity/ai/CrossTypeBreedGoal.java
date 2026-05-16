package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.animal.GenderedAnimal;
import io.github.ron1196.thelionking.item.GroundRhinoHornItem;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;

/**
 * Breed goal that supports cross-type breeding pairs defined in GroundRhinoHornItem.
 * Falls back to vanilla same-class matching for animals not in the breeding partners map.
 */
public class CrossTypeBreedGoal extends BreedGoal {

    private static final TargetingConditions PARTNER_TARGETING =
            TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();

    public CrossTypeBreedGoal(Animal animal, double speedModifier) {
        super(animal, speedModifier);
    }

    @Override
    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        }

        EntityType<?> mateType = GroundRhinoHornItem.getPartnerType(this.animal.getType());
        if (mateType == this.animal.getType()) {
            return super.canUse();
        }

        this.partner = findCrossTypePartner(mateType);
        return this.partner != null;
    }

    private Animal findCrossTypePartner(EntityType<?> mateType) {
        List<Animal> nearby = this.animal
                .level()
                .getNearbyEntities(
                        Animal.class,
                        PARTNER_TARGETING,
                        this.animal,
                        this.animal.getBoundingBox().inflate(8.0));

        double closestDist = Double.MAX_VALUE;
        Animal closest = null;
        for (Animal candidate : nearby) {
            if (candidate.getType() == mateType && candidate.isInLove() && !candidate.isBaby()) {
                if (this.animal instanceof GenderedAnimal a
                        && candidate instanceof GenderedAnimal b
                        && !a.canBreedWith(b)) {
                    continue;
                }
                double dist = this.animal.distanceToSqr(candidate);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = candidate;
                }
            }
        }

        return closest;
    }
}
