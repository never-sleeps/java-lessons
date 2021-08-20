package ru.java.functioal;

import java.util.Objects;
import java.util.Scanner;
import java.util.function.*;

public class FunctionalInterfacesDemo {
    public static void main(String[] args) {
        // Predicate<T>: test проверяет выполнение условия и возвращает результат
        Predicate<Object> isNull = Objects::isNull;
        System.out.println(isNull.test(new Object())); // false
        System.out.println(isNull.test(null));      // true

        // Consumer<T>: accept выполняет какое-либо действие над объектом типа T, ничего не возвращает
        Consumer<String> printer = System.out::println;
        printer.accept("Hello");

        // Function<T, R>: apply выполняет переход от объекта типа T к объекту типа R
        Function<Integer, Double> converter = Double::valueOf;
        System.out.println(converter.apply(5));

        // UnaryOperator<T>: apply позволяет выполнять унарные операции над объектом типа T
        UnaryOperator<Double> sqrt = Math::sqrt;
        System.out.println(sqrt.apply(16.0)); // 4

        // BinaryOperator<T>: apply позволяет выполнять бинарные операции над объектом типа T
        BinaryOperator<Double> pow = Math::pow;
        System.out.println(pow.apply(2.0, 10.0)); // 1024

        // Supplier<T>: get не принимает аргументов, но возвращает объект типа T
        Supplier<String> text = () -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите строку:");
            return scanner.nextLine();
        };
        System.out.println(text.get());

    }
}