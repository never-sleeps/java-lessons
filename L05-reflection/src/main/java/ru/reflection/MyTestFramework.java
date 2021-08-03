package ru.reflection;

import ru.reflection.annotations.After;
import ru.reflection.annotations.Before;
import ru.reflection.annotations.Test;
import ru.reflection.reflection.ReflectionHelper;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyTestFramework {

    private int allTests = 0;
    private int failedTests = 0;

    /**
     * Запуск тестов в классах [classes]
     * @param classes
     */
    public void runClasses(Class<?>... classes){
        List<Method> testMethods;
        Method beforeMethod;
        Method afterMethod;
        Object object;

        for(Class<?> clazz : classes) {
            beforeMethod = ReflectionHelper.findAnnotatedMethod(clazz, Before.class);
            afterMethod = ReflectionHelper.findAnnotatedMethod(clazz, After.class);
            testMethods = ReflectionHelper.findAnnotatedMethods(clazz, Test.class);

            if(!testMethods.isEmpty()){
                for(Method testMethod : testMethods){
                    object = ReflectionHelper.instantiate(clazz);
                    runTestMethod(object, beforeMethod, testMethod, afterMethod);
                }
            }
        }
        printResults();
    }

    /**
     * Запуск @Before, @Test, @After методов
     * @param object - объект класса
     * @param beforeMethod - метод @Before
     * @param method - метод @Test
     * @param afterMethod - метод @After
     */
    private void runTestMethod(
            Object object,
            Method beforeMethod,
            Method method,
            Method afterMethod
    ) {
        try {
            if (beforeMethod != null ) {
                ReflectionHelper.callMethod(object, beforeMethod.getName());
            }
            ReflectionHelper.callMethod(object, method.getName());
            if (afterMethod != null ) {
                ReflectionHelper.callMethod(object, afterMethod.getName());
            }
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                failedTests++;
                String exceptionMessage = ((InvocationTargetException) e.getCause()).getTargetException().getMessage();
                System.err.println(String.format("%s. %s > %s", failedTests, method.getName(), exceptionMessage));
            } else {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        allTests++;
    }

    /**
     * Запуск тестов для директории [packageForRun]
     * @param packageForRun
     */
    public void runPackage(String packageForRun){
        List<Class<?>> classes = getClassesForPackage(packageForRun);
        Class<?>[] classesArray = new Class<?>[classes.size()];
        classes.toArray(classesArray);
        runClasses(classesArray);
    }

    /**
     * Получение классов для пакета [packageName]
     * @param packageName - название пакета
     * @return
     */
    private static List<Class<?>> getClassesForPackage(String packageName) {
        String path = packageName.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(path);
        if (resource == null) {
            throw new RuntimeException("No resource for " + path);
        }
        return processDirectory(new File(resource.getPath()), packageName);
    }

    private static List<Class<?>> processDirectory(File directory, String packageName) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        String[] files = directory.list();
        if (files != null) {
            for (String fileName : files) {
                if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    classes.add(loadClass(className));
                }
            }
        }
        return classes;
    }

    /**
     * Загрузка класса
     * @param className - имя класса
     * @return объект Class
     */
    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException for class '" + className + "'");
        }
    }

    /**
     * Выведение на консоль статистики запуска
     */
    private void printResults() {
        boolean existErrors = failedTests > 0;
        PrintStream printStream = (existErrors) ? System.err : System.out;

        printStream.println(
                String.format(
                        "\nTest count: %s.\n%s test completed, %s failed.",
                        allTests, (allTests - failedTests), failedTests
                )
        );
        if (existErrors) {
            printStream.println("FAILURE: Build failed with an exception.\n");
        }
    }
}
