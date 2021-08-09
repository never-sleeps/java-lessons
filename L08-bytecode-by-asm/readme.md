
Создание .class, идентичного .java:  
```java
public class HelloWorld {
    public static void printHelloWorld() {
        System.out.println("Hello, World!");
    }
}
```

Результат (classes/HelloWorld.class):
```java
public class HelloWorld {
    public HelloWorld() {
    }

    public static void printHelloWorld() {
        System.out.println("Hello, World!");
    }
}
```