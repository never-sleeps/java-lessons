package ru.reflection.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Вспомогательный класс для работы с классами через механизм Reflection
 */
public class ReflectionHelper {
    private ReflectionHelper() { }

    /**
     * Получить значение поля класса
     * @param object - объект
     * @param name - название поля
     * @return - значение поля класса
     */
    public static Object getFieldValue(Object object, String name) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Установить в поле класса заданное значение
     * @param object - объект
     * @param name - название поля класса
     * @param value - устанавливаемое значение
     */
    public static void setFieldValue(Object object, String name, Object value) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вызвать метод объекта
     * @param object - объект
     * @param name - название метода
     * @param args - аргументы метода
     * @return - результат выполнения метода
     * @throws RuntimeException
     */
    public static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создать объект класса
     * @param type - класс объекта
     * @param args - аргументы конструктора
     * @param <T> - generic-параметр
     * @return - созданный объект класса
     */
    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить список методов с указанной аннотацией
     * @param type - класс
     * @param annotation - аннотация
     * @return - список аннотированных методов
     */
    static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    public static Method findAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method;
            }
        }
        return null;
    }

    public static List<Method> findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getMethods();
        List<Method> annotatedMethods = new ArrayList<Method>(methods.length);
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
