package ru.java.creational.abstractfactory.luminescent;

import ru.java.creational.abstractfactory.Bulb;


public class BulbLuminescent implements Bulb {
  @Override
  public void light() {
    System.out.println("Luminescent light");
  }
}
