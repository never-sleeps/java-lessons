package ru.java.creational.factorymethod1;


public class ConfigurationDB implements Configuration {
  @Override
  public String params() {
    return "params from DB";
  }
}
