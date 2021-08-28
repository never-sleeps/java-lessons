package ru.java.structural.bridge.paymentsystem;

public class VisaPS implements PaymentSystem {
    @Override
    public void printName() {
        System.out.println("VisaPS");
    }
}
