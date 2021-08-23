package ru.java.solid.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(long requestedAmount, long availableAmount) {
        super(String.format(
                "Not enough money: requestedAmount %s is greater than the available amount %s",
                requestedAmount, availableAmount
        ));
    }

    public NotEnoughMoneyException() {
        super("Not enough money");
    }
}
