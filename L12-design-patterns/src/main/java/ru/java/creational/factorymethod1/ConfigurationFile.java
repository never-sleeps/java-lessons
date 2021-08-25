package ru.java.creational.factorymethod1;


public class ConfigurationFile implements Configuration {
  @Override
  public String params() {
    return "params from file";
  }
}
