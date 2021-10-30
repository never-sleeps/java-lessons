package ru.java.appcontainer.exception;

public class NoSuchAppComponentsContainer extends RuntimeException{
    public NoSuchAppComponentsContainer(String message) {
        super(message);
    }
}
