package ru.java.creational.abstractfactory.updated;

import ru.java.creational.abstractfactory.AbstractFactory;
import ru.java.creational.abstractfactory.luminescent.LuminescentFactory;


public class LuminescentStrategy implements Strategy {
    @Override
    public AbstractFactory configuration() {
        return new LuminescentFactory();
    }
}
