package ru.java.config;

import ru.java.appcontainer.api.AppComponent;
import ru.java.appcontainer.api.AppComponentsContainerConfig;
import ru.java.services.IOService;
import ru.java.services.IOServiceImpl;

/**
 * Аналог @Configuration - класса.
 * Порядок инициализации @AppComponentsContainerConfig-классов зависит от order.
 *
 * Порядок инициализации бинов (@AppComponent) зависит от order.
 * При наличии нескольких одинаковых значений order порядок будет случайным.
 */
@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {
    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceImpl(System.out, System.in);
    }
}
