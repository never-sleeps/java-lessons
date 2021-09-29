package ru.java.creational.abstractfactory.led;

import ru.java.creational.abstractfactory.AbstractFactory;
import ru.java.creational.abstractfactory.Bulb;
import ru.java.creational.abstractfactory.Lampholder;


public class LedFactory implements AbstractFactory {
  @Override
  public Bulb createBulb() {
    return new BulbLed();
  }

  @Override
  public Lampholder createLampholder() {
    return new LampholderLed();
  }
}
