package ru.java.aop.proxy;

public class Application {
    public static void main(String[] args) {
        InterfaceForDemo demo = Ioc.createProxyClass();

        demo.calculation();
        demo.calculation(1);
        demo.calculation(1, 2L);
        demo.calculation(1, 2L, 3d);
    }
}