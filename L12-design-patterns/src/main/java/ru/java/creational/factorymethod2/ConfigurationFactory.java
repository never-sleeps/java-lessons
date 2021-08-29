package ru.java.creational.factorymethod2;


abstract class ConfigurationFactory {

  abstract Configuration buildConfiguration();

  static ConfigurationFactory getConfigurationFactory(String param) {
    if ("file".equals(param)) {
      return new ConfigurationFactoryFile();
    }
    if ("db".equals(param)) {
      return new ConfigurationFactoryDB();
    }
    throw new IllegalArgumentException("unknown param:" + param);
  }
}
