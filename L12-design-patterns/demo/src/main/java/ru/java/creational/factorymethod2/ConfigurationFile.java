package ru.java.creational.factorymethod2;


public class ConfigurationFile implements Configuration {
  @Override
  public String params() {
    return "params from file";
  }
}
