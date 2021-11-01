package ru.java.appcontainer.exception;

public class NoSuchBeanDefinitionException extends RuntimeException {
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
