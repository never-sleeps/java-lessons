package ru.java.structural.bridge.type;

import ru.java.structural.bridge.paymentsystem.PaymentSystem;

public class DebitCard extends Card {
    public DebitCard(PaymentSystem paymentSystem) {
        super(paymentSystem);
    }

    @Override
    protected void cardType() {
        System.out.println("debit card");
    }
}
