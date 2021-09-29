package ru.java.structural.decorator;


public class Demo {
    public static void main(String[] args) {
        var dataSource = new DataSourceImpl();
        println(dataSource.getInteger());

        var ds1 = new DataSourceDecoratorAdder(dataSource);
        println(ds1.getInteger());

        var ds2 = new DataSourceDecoratorMultiplicator(dataSource);
        println(ds2.getInteger());

        var ds3 = new DataSourceDecoratorAdder(new DataSourceDecoratorMultiplicator(dataSource));
        println(ds3.getInteger());

    }

    private static void println(int value) {
        System.out.println(value);
    }
}
