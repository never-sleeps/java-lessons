package ru.java.creational.abstractfactory.led;

import ru.java.creational.abstractfactory.Lampholder;


public class LampholderLed implements Lampholder {
  @Override
  public void hold() {
    System.out.println("Led hold");
  }
}
