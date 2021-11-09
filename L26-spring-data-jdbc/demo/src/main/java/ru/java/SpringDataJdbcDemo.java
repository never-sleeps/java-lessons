package ru.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SpringDataJdbcDemo {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringDataJdbcDemo.class, args);

        context.getBean("serviceForDemo", ServiceForDemo.class).doAction();
    }
}
