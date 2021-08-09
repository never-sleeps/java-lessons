package ru.java.bytecode;

/**
 * Простейший ClassLoader.
 * Просто вызывает родительский defineClass
 */
public class MyClassLoader extends ClassLoader {
    Class<?> defineClass(String className, byte[] bytecode) {
        return super.defineClass(className, bytecode, 0, bytecode.length);
    }
}
