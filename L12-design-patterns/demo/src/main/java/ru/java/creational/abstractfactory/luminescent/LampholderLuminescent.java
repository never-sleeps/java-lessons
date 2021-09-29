package ru.java.creational.abstractfactory.luminescent;

import ru.java.creational.abstractfactory.Lampholder;


public class LampholderLuminescent implements Lampholder {
  @Override
  public void hold() {
    System.out.println("Luminescent hold");
  }
}
