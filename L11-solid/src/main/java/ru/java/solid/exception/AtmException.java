package ru.java.solid.exception;

public abstract class AtmException extends RuntimeException{
    public AtmException(String message) {
        super(message);
    }
}
