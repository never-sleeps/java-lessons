package ru.java.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.java.service.GreetingService;

import java.util.Map;

@RestController
public class GreetingControllerRestStyle {

    private final GreetingService greetingService;

    public GreetingControllerRestStyle(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @ApiOperation("Вызов для пользователя приветствия из Messager")
    @GetMapping(value="/hello/{name}") //http://localhost:8080/hello/jone
    public Map<String, String> sayHello(
            @ApiParam("Имя пользователя")
            @PathVariable("name") String name
    ) {
        return this.greetingService.sayHello(name);
    }
}
