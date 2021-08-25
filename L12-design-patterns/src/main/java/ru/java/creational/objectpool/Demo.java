package ru.java.creational.objectpool;


public class Demo {
  public static void main(String[] args) {
    ConnectionFactory connectionFactory =  new ConnectionFactory();
    ConnectionPool pool = new ConnectionPool(5,connectionFactory);
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select(); // exception
  }
}
