package ru.java.solid;

import ru.java.solid.exception.NotSupportedNominalException;

import java.util.Arrays;

public enum Nominal {

    NOMINAL_10(10),
    NOMINAL_50(50),
    NOMINAL_100(100),
    NOMINAL_200(200),
    NOMINAL_500(500),
    NOMINAL_1000(1000),
    NOMINAL_2000(2000),
    NOMINAL_5000(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public Nominal get() { return this; }

    public static Nominal of(int value) {
        return Arrays.stream(Nominal.values())
                .filter(it -> it.value == value)
                .findFirst()
                .orElseThrow(() -> new NotSupportedNominalException(value));
    }

    public int getValue() {
        return value;
    }
}
