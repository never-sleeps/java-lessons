package ru.java.exception;

public class DataTemplateException extends RuntimeException {
    public DataTemplateException(Exception ex) {
        super(ex);
    }

    public DataTemplateException(String message) {
        super(message);
    }
}
