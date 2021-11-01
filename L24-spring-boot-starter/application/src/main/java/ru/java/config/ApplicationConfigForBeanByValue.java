package ru.java.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigForBeanByValue {

    @Bean
    AppConfigBeanByValue appConfigByValue(@Value("${application.param-name}") String param) {
        return new AppConfigBeanByValue(param);
    }
}
