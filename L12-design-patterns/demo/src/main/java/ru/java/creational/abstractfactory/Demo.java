package ru.java.creational.abstractfactory;

import ru.java.creational.abstractfactory.led.LedFactory;
import ru.java.creational.abstractfactory.luminescent.LuminescentFactory;


public class Demo {

    public static void main(String[] args) {
        // Есть два типа лампочек: светодиодные (Led) и люминисцентные (Luminescent).
        // Мы хотим создавать лампочки и патроны для них.
        var demo = new Demo();

        AbstractFactory ledFactory = configuration("Led");
        demo.run(ledFactory);

        AbstractFactory luminescentFactory = configuration("Luminescent");
        demo.run(luminescentFactory);
    }

    public void run(AbstractFactory abstractFactory) {
        Bulb bulb = abstractFactory.createBulb();
        Lampholder lampholder = abstractFactory.createLampholder();

        bulb.light();
        lampholder.hold();
    }

    // "Фабрика фабрик"
    public static AbstractFactory configuration(String param) {
        if ("Led".equals(param)) {
            return new LedFactory();
        }
        if ("Luminescent".equals(param)) {
            return new LuminescentFactory();
        }
        throw new IllegalArgumentException("unknown param:" + param);
    }
}
