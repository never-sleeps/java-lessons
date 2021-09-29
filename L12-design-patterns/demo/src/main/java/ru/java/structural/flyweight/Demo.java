package ru.java.structural.flyweight;


public class Demo {
    public static void main(String[] args) {
        var objectFactory = new ObjectFactory();

        var object1 = objectFactory.create(1);
        System.out.println(object1.toString());

        var object2 = objectFactory.create(2);
        System.out.println(object2.toString());

        var object3 = objectFactory.create(3);
        System.out.println(object3.toString());
    }
}
