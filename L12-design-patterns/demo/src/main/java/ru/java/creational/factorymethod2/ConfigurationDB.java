package ru.java.creational.factorymethod2;


public class ConfigurationDB implements Configuration {
  @Override
  public String params() {
    return "params from DB";
  }
}
