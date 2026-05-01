package io.github.ron1196.thelionking.entity.animal;

import org.jetbrains.annotations.NotNull;

public final class BreedingRules {

    private BreedingRules() {}

    public static boolean canBreed(@NotNull Gender a, @NotNull Gender b) {
        return a != b;
    }
}
