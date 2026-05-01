package io.github.ron1196.thelionking.entity.animal;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender fromIsFemale(boolean isFemale) {
        return isFemale ? FEMALE : MALE;
    }

    public boolean isFemale() {
        return this == FEMALE;
    }
}
