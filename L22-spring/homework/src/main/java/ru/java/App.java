package ru.java;

import ru.java.appcontainer.AppComponentsContainerImpl;
import ru.java.appcontainer.api.AppComponentsContainer;
import ru.java.config.AppConfig;
import ru.java.config.AppConfig1;
import ru.java.config.AppConfig2;
import ru.java.services.GameProcessor;
import ru.java.services.GameProcessorImpl;

/*
Приложение представляет из себя тренажер таблицы умножения
*/
public class App {
    public static void main(String[] args) {
        // инициализация контейнера (приложение работает в каждом из указанных ниже вариантов)
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class); // по @Configuration-классу
//        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class); // по списку @Configuration-классов
//        AppComponentsContainer container = new AppComponentsContainerImpl("ru.java.config"); // по переданному пакету


        // Приложение работает в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class); // по интерфейсу компонента
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class); // по реализации компонента
        GameProcessor gameProcessor = container.getAppComponent("gameProcessor"); // по названию компонента

        gameProcessor.startGame();
    }
}
