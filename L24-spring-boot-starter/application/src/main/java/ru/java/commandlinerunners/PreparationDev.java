package ru.java.commandlinerunners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.java.Messager;

import java.util.Arrays;


/*
Если бин имплементирует CommandLineRunner-интерфейс, то он запустится после того, как контекст поднимется и приложение стартанёт
но перед тем, как оно будет готово принимать какие-либо запросы снаружи.
 */
@Component
public class PreparationDev implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PreparationDev.class);

    // Messager - уже обычный бин, который создаётся в стартере.
    private final Messager messager;

    public PreparationDev(Messager messager) {
        this.messager = messager;
    }

    @Override
    public void run(String... args) {
        logger.info("args: {} ", Arrays.toString(args));
        logger.info("message from Messager: {}", messager.sayMessage());
    }
}
