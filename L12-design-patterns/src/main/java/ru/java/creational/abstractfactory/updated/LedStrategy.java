package ru.java.creational.abstractfactory.updated;

import ru.java.creational.abstractfactory.AbstractFactory;
import ru.java.creational.abstractfactory.led.LedFactory;


public class LedStrategy implements Strategy {
    @Override
    public AbstractFactory configuration() {
        return new LedFactory();
    }
}
