package ru.java.structural.bridge.type;

import ru.java.structural.bridge.paymentsystem.PaymentSystem;

public class CreditCard extends Card {
    public CreditCard(PaymentSystem paymentSystem) {
        super(paymentSystem);
    }

    @Override
    protected void cardType() {
        System.out.println("credit card");
    }
}
