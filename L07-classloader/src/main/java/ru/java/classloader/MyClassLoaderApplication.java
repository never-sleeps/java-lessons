package ru.java.classloader;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class MyClassLoaderApplication {
    public static void main(String[] args) throws IOException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException
    {
        new MyClassLoaderApplication().start();
    }

    private void start() throws IOException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        var loader = new MyClassLoader();
        Class<?> clazz = loader.defineClass(
                "classes",
                "ru.java.classloader.ClassForLoading"
        );

        System.out.println(clazz.getName() + " methods:");
        Arrays.stream(clazz.getMethods())
                .forEach(method -> System.out.println(" > " + method.getName()));

        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();

        clazz.getMethod("myMethod").invoke(object);
    }
}
