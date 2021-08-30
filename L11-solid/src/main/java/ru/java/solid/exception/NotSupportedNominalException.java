package ru.java.solid.exception;

public class NotSupportedNominalException extends AtmException {
    public NotSupportedNominalException(int nominal) {
        super(
                String.format(
                        "Not supported nominal value: %s. Available denominations of banknotes: 5000, 2000, 1000, 500, 200, 100, 50, 10.",
                        nominal
                )
        );
    }
}
