package ru.java.solid.exception;

import ru.java.solid.Nominal;

public class NotSupportedNominalByCellException extends RuntimeException{
    public NotSupportedNominalByCellException(Nominal addedNominal, Nominal availableNominal) {
        super(String.format(
                "Not supported nominal value: %s by cell. Available nominal: %s.",
                addedNominal.getValue(),
                availableNominal.getValue()
        ));
    }
}
