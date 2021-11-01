package ru.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.java.services.*;
import ru.java.services.EquationPreparerServiceImpl;
import ru.java.services.GameServiceImpl;
import ru.java.services.IOServiceImpl;
import ru.java.services.PlayerServiceImpl;

@Configuration
public class AppConfig {

    @Bean
    public EquationPreparerService equationPreparerService() {
        return new EquationPreparerServiceImpl();
    }

    @Bean
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @Bean
    public GameService gameService(
            IOService ioService,
            PlayerService playerService,
            EquationPreparerService equationPreparerService
    ) {
        return new GameServiceImpl(ioService, equationPreparerService, playerService);
    }

    @Bean
    public IOService ioService() {
        return new IOServiceImpl(System.out, System.in);
    }
}
