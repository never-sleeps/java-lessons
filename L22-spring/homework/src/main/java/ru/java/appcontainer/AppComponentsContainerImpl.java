package ru.java.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.java.appcontainer.api.AppComponent;
import ru.java.appcontainer.api.AppComponentsContainer;
import ru.java.appcontainer.api.AppComponentsContainerConfig;
import ru.java.appcontainer.exception.ApplicationContextInitializationException;
import ru.java.appcontainer.exception.NoSuchAppComponentsContainer;
import ru.java.appcontainer.exception.NoSuchBeanDefinitionException;

import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    /**
     * Инициализация Configuration-классов [initialConfigClasses]
     * @param initialConfigClasses - Configuration-классы
     */
    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    /**
     * Инициализация Configuration-класса [initialConfigClass]
     * @param initialConfigClass - Configuration-класс
     */
    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    /**
     * Инициализация Configuration-классов из пакета [packageToScan]
     * @param packageToScan - пакет для сканирования
     */
    public AppComponentsContainerImpl(String packageToScan) {
        var scanner = new Reflections(packageToScan, new SubTypesScanner(false));
        var initialConfigClasses = scanner.getSubTypesOf(Object.class).stream()
                .filter(it -> !it.isInterface() || !it.isAnnotation())
                .toArray(Class<?>[]::new);
        processConfig(initialConfigClasses);

    }

    private void processConfig(Class<?>... configClasses) {
        try {
            checkConfigClasses(configClasses);
            fillContextWithConfigClasses(configClasses);
        } catch (Exception e) {
            throw new ApplicationContextInitializationException(e);
        }
    }

    /**
     * Инициализация компонентов (бинов) из @AppComponentsContainerConfig-классов [configClasses]
     * согласно order для @AppComponentsContainerConfig и order для @AppComponent
     */
    private void fillContextWithConfigClasses(Class<?>[] configClasses) throws ReflectiveOperationException {
        // AppComponentsContainerConfig-классы, отсортированные согласно order
        var configClassesSortedByOrder = Arrays.stream(configClasses)
                .sorted(Comparator.comparingInt(it -> it.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());

        for (var configClass : configClassesSortedByOrder) {
            fillContextWithConfigClass(configClass);
        }
    }

    /**
     * Инициализация компонентов (бинов) из @AppComponentsContainerConfig-класса [configClass]
     */
    private void fillContextWithConfigClass(Class<?> configClass) throws ReflectiveOperationException {
        // AppComponent-методы конфиг-класса, отсортированные согласно order
        var sortedMethods = Arrays.stream(configClass.getMethods())
                .filter(it -> it.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(it -> it.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        // создание инстанса конфиг-класса
        var instance = configClass.getDeclaredConstructor().newInstance();

        // инициализация компонентов (бинов)
        for (var method : sortedMethods) {
            // получение списка необходимых для метода параметров
            var parameters = method.getParameterTypes();
            // получение компонентов для необходимых для метода параметров
            var args = Arrays.stream(parameters).map(this::findComponent).toArray();
            // получение имени компонента из @AppComponent
            var componentName = method.getAnnotation(AppComponent.class).name();
            // инициализация компонента
            var component = method.invoke(instance, args);

            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        }
    }

    /**
     * Проверка того, что классы [configClasses] являются @AppComponentsContainerConfig - классами
     */
    private void checkConfigClasses(Class<?>[] configClasses) {
        for (Class<?> configClass : configClasses) {
            checkConfigClass(configClass);
        }
    }

    /**
     * Проверка того, что класс [configClass] является Configuration - классом
     * @throws IllegalArgumentException в случае, если класс не является @AppComponentsContainerConfig - классом
     */
    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new NoSuchAppComponentsContainer(configClass.getName() + " is not @AppComponentsContainerConfig");
        }
    }

    /**
     * Поиска  @AppComponent (бина) определённого типа [componentClass]
     * @throws IllegalArgumentException в случае отсутствия компонента (бина) класса [componentClass]
     */
    private Object findComponent(Class<?> componentClass) {
        return appComponents.stream()
                .filter(componentClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException(componentClass.getName()));
    }

    /**
     * Поиск @AppComponent (бина) по его классу [componentClass]
     * @throws NoSuchBeanDefinitionException в случае отсутствия компонента (бина) класса [componentClass]
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) findComponent(componentClass);
    }

    /**
     * Поиск @AppComponent (бина) по его идентификатору [componentName]
     * @throws NoSuchBeanDefinitionException в случае отсутствия компонента (бина) с идентификатором [componentName]
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new NoSuchBeanDefinitionException(componentName);
        }
        return component;
    }
}