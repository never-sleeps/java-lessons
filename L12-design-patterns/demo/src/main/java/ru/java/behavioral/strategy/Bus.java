package ru.java.behavioral.strategy;

public class Bus implements Strategy {
    @Override
    public void transportation() {
        System.out.println("доехать на автобусе");
    }
}
