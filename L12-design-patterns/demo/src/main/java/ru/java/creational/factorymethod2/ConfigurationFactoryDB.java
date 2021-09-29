package ru.java.creational.factorymethod2;


class ConfigurationFactoryDB extends ConfigurationFactory {
  @Override
  Configuration buildConfiguration() {
    return new ConfigurationDB();
  }
}
