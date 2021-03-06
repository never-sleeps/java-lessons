package ru.java.creational.builder;


public class Demo {
  public static void main(String[] args) {
    // Так плохо - не понятно, что за параметры
    BigObject bigObject1 = new BigObject(null, "2", null, "4", "5");
    System.out.println(bigObject1);

    // Так лучше
    BigObject bigObject2 = new BigObject.BigObjectBuilder("1")
        .withParam2("value of param2")   // задаем нужные параметры
        .withParam5("value of param5")   // в любом порядке
        //.withParam4() // для необязательных просто не вызываем метод
        .withParam3("value of param3")
        .build();          // получаем нужный нам объект

    System.out.println(bigObject2);
  }
}
