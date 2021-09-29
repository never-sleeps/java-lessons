package ru.java.creational.abstractfactory.luminescent;

import ru.java.creational.abstractfactory.AbstractFactory;
import ru.java.creational.abstractfactory.Bulb;
import ru.java.creational.abstractfactory.Lampholder;


public class LuminescentFactory implements AbstractFactory {
  @Override
  public Bulb createBulb() {
    return new BulbLuminescent();
  }

  @Override
  public Lampholder createLampholder() {
    return new LampholderLuminescent();
  }
}
