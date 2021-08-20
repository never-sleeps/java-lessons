package ru.java.functioal;

public class LambdaDemo {
    public static void main(String[] args) {
        System.out.println(getOperation('*').getResult(10, 5)); // 50
        System.out.println(getOperation('/').getResult(10, 5)); // 2
        System.out.println(getOperation('+').getResult(10, 5)); // 15
        System.out.println(getOperation('-').getResult(10, 5)); // 5
    }

    private static Operation getOperation(char symbol) {
        return switch (symbol) {
            case '*' -> (value1, value2) -> value1 * value2;
            case '/' -> (value1, value2) -> value1 / value2;
            case '+' -> (value1, value2) -> value1 + value2;
            case '-' -> (value1, value2) -> value1 - value2;
            default -> (value1, value2) -> 0;
        };
    }

    private interface Operation {
        int getResult(int value1, int value2);
    }
}
