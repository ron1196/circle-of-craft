package io.github.ron1196.circleofcraft.entity.animal;

import org.jetbrains.annotations.NotNull;

public interface GenderedAnimal {

    @NotNull
    Gender getGender();

    default boolean canBreedWith(@NotNull GenderedAnimal other) {
        return BreedingRules.canBreed(getGender(), other.getGender());
    }
}
