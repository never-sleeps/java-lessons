package ru.java.structural.bridge;

import ru.java.structural.bridge.paymentsystem.MastercardPS;
import ru.java.structural.bridge.paymentsystem.MirPS;
import ru.java.structural.bridge.paymentsystem.VisaPS;
import ru.java.structural.bridge.type.CreditCard;
import ru.java.structural.bridge.type.DebitCard;

public class Demo {
    public static void main(String[] args) {
        var card1 = new CreditCard(new VisaPS());
        card1.info();

        var card2 = new DebitCard(new MastercardPS());
        card2.info();

        var card3 = new DebitCard(new MirPS());
        card3.info();
    }
}
