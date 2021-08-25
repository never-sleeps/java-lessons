package ru.java.creational.abstractfactory.updated;

import ru.java.creational.abstractfactory.AbstractFactory;
import ru.java.creational.abstractfactory.Bulb;
import ru.java.creational.abstractfactory.Lampholder;


public class Demo {

    public Demo(AbstractFactory abstractFactory) {
        Bulb bulb = abstractFactory.createBulb();
        Lampholder lampholder = abstractFactory.createLampholder();

        bulb.light();
        lampholder.hold();
    }

    public static AbstractFactory configuration(Strategy strategy) {
        return strategy.configuration();
    }

    public static void main(String[] args) {
        new Demo(configuration(new LedStrategy()));
        new Demo(configuration(new LuminescentStrategy()));
    }

}
