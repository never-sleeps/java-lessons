package ru.java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.java.config.AppConfigBeanByValue;
import ru.java.config.ApplicationConfigForConfigProps;
import ru.java.service.GreetingService;


import java.util.Map;

@RestController
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    private final GreetingService greetingService;

    public GreetingController(
            GreetingService greetingService,
            ApplicationConfigForConfigProps props,
            @Qualifier("appConfigByValue") AppConfigBeanByValue appConfigBeanByValue
    ) {
        this.greetingService = greetingService;
        logger.info("PROPERTIES: props.getMessage(): {}", props.getMessage()); // null (поскольку application.message отсутствует в properties.yml)
        logger.info("PROPERTIES: props.getParamName(): {}", props.getParamName()); // Test Props msg@@@
        logger.info("PROPERTIES: appConfigBeanByValue.getParamName(): {}", appConfigBeanByValue.getParamName()); // Test Props msg@@@
    }

    @GetMapping("/hello") //http://localhost:8080/hello?name=ddd
    public Map<String, String> sayHello(@RequestParam(name="name") String name) {
        return this.greetingService.sayHello(name);
    }
}
