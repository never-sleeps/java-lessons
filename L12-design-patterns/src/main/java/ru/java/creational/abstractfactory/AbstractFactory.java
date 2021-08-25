package ru.java.creational.abstractfactory;


public interface AbstractFactory {
  Bulb createBulb();

  Lampholder createLampholder();
}
