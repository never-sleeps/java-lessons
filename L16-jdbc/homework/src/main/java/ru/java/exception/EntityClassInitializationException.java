package ru.java.exception;

public class EntityClassInitializationException extends RuntimeException {
    public EntityClassInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
