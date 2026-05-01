package io.github.ron1196.thelionking.entity.animal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BreedingRulesTest {

    @Test
    void maleAndFemaleCanBreed() {
        assertTrue(BreedingRules.canBreed(Gender.MALE, Gender.FEMALE));
    }

    @Test
    void femaleAndMaleCanBreed() {
        assertTrue(BreedingRules.canBreed(Gender.FEMALE, Gender.MALE));
    }

    @Test
    void twoMalesCannotBreed() {
        assertFalse(BreedingRules.canBreed(Gender.MALE, Gender.MALE));
    }

    @Test
    void twoFemalesCannotBreed() {
        assertFalse(BreedingRules.canBreed(Gender.FEMALE, Gender.FEMALE));
    }
}
