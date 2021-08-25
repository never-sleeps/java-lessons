package ru.java.creational.factorymethod2;


class ConfigurationFactoryFile extends ConfigurationFactory  {
  @Override
  Configuration buildConfiguration() {
    return new ConfigurationFile();
  }
}
