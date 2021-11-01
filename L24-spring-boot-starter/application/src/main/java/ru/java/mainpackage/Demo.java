package ru.java.mainpackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.java.mainpackage.config.AppConfigForConfigProps;

//http://localhost:8080/actuator/health

@SpringBootApplication
@EnableConfigurationProperties(AppConfigForConfigProps.class)
public class Demo {
    public static void main(String[] args) {
        SpringApplication.run(Demo.class, args);
    }
}
