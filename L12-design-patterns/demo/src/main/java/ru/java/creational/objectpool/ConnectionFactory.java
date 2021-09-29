package ru.java.creational.objectpool;


class ConnectionFactory {
  Connection create() {
    return new ConnectionOracle();
  }
}
