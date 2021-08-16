package ru.java.aop.proxy;

public class ClassForDemo implements InterfaceForDemo{

    @Log
    public void calculation() { }

    @Log
    public int calculation(int param1) {
        return param1;
    }

    @Log
    public long calculation(int param1, long param2) {
        return param1 + param2;
    }

    @Log
    public double calculation(int param1, long param2, double param3) {
        return param1 + param2 + param3;
    }
}