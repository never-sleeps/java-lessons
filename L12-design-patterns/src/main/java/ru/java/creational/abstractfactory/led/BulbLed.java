package ru.java.creational.abstractfactory.led;

import ru.java.creational.abstractfactory.Bulb;


public class BulbLed implements Bulb {
  @Override
  public void light() {
    System.out.println("Led light");
  }
}
