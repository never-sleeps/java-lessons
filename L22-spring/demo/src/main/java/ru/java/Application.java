package ru.java;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.java.services.GameService;

@ComponentScan
public class Application {
    public static void main(String[] args) throws Exception {
//        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        GameService gameService = context.getBean(GameService.class);
        gameService.startGame();
    }
}
