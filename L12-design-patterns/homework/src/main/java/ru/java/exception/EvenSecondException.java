package ru.java.exception;

public class EvenSecondException extends RuntimeException {
    public EvenSecondException() {
        super("Current second is even");
    }
}