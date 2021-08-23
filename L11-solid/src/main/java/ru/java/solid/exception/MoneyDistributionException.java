package ru.java.solid.exception;

public class MoneyDistributionException extends RuntimeException {
    public MoneyDistributionException() {
        super("Distribution error. Try to request a another amount");
    }
}
