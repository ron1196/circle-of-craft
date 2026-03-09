package io.github.ron1196.thelionking.entity.ai;

import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;

/**
 * Breeding goal that is suppressed when the animal is angry.
 * Intended for NeutralMob animals (e.g., rhinos, gemsboks) that
 * won't breed while they have a revenge target.
 */
public class AngerableMateGoal extends BreedGoal {

    private final Animal animal;

    public AngerableMateGoal(Animal animal, double speedModifier) {
        super(animal, speedModifier);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        if (animal instanceof NeutralMob neutralMob) {
            if (neutralMob.isAngry()) return false;
        }
        // Also check if we have a revenge target set
        if (animal.getLastHurtByMob() != null) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (animal instanceof NeutralMob neutralMob) {
            if (neutralMob.isAngry()) return false;
        }
        if (animal.getLastHurtByMob() != null) return false;
        return super.canContinueToUse();
    }
}
