package ru.java.mainpackage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.java.mainpackage.config.AppConfigForBean;
import ru.java.mainpackage.config.AppConfigForConfigProps;
import ru.java.mainpackage.service.GreetingService;


import java.util.Map;

@RestController
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    private final GreetingService greetingService;

    public GreetingController(
            GreetingService greetingService,
            AppConfigForConfigProps props,
            @Qualifier("messageConfig") AppConfigForBean appConfigForBean
    ) {
        this.greetingService = greetingService;
        logger.info("ATTENTION! props.getMessage(): {}", props.getMessage());
        logger.info("ATTENTION! applicationConfig.getMessage(): {}", appConfigForBean.getParamName());
    }

    //http://localhost:8080/hello?name=ddd
    @GetMapping("/hello")
    public Map<String, String> sayHello(@RequestParam(name="name") String name) {
        return this.greetingService.sayHello(name);
    }
}
