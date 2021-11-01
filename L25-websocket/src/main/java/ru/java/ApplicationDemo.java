package ru.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    http://localhost:8080/serverTime.html - через ajax
    http://localhost:8080/serverWebSockerTime.html - через websocket
    http://localhost:8080 - чат через websocket'ы

 */
@SpringBootApplication
public class ApplicationDemo {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationDemo.class, args);
    }
}
