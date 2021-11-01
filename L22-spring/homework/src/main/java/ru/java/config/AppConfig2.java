package ru.java.config;

import ru.java.appcontainer.api.AppComponent;
import ru.java.appcontainer.api.AppComponentsContainerConfig;
import ru.java.services.*;

/**
 * Аналог @Configuration - класса.
 * Порядок инициализации @@AppComponentsContainerConfig-классов зависит от order.
 *
 * Порядок инициализации бинов (@AppComponent) зависит от order.
 * При наличии нескольких одинаковых значений order порядок будет случайным.
 */
@AppComponentsContainerConfig(order = 1)
public class AppConfig2 {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(
            IOService ioService,
            PlayerService playerService,
            EquationPreparer equationPreparer
    ) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
