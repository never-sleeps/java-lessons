package ru.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.java.config.ApplicationConfigForConfigProps;

// http://localhost:8080/actuator/health
// http://localhost:8080/swagger-ui/index.html

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfigForConfigProps.class)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
