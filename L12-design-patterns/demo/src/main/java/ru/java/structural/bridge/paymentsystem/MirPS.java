package ru.java.structural.bridge.paymentsystem;

public class MirPS implements PaymentSystem {
    @Override
    public void printName() {
        System.out.println("Mir");
    }
}
