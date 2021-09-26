package ru.java.ex01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoggingDemo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggingDemo.class);

    public static void main(String[] args) {
        new SimpleLoggingDemo().log();
    }

    private void log() {
        var value = "test";
        // Устаревший вариант
        if (logger.isDebugEnabled()) {
            logger.error("Hello logging:" + value);
        }

        //Современный вариант
        logger.error("Hello logging:{}", value); // 03:29:08.612 [main] ERROR ru.java.ex01.SimpleLoggingDemo - Hello logging:test

        try {
            throw new RuntimeException("exception for log");
        } catch (Exception e) {
            logger.error("exception log:", e);
        }
        /*
            03:30:00.355 [main] ERROR ru.java.ex01.SimpleLoggingDemo - exception log:
            java.lang.RuntimeException: exception for log
                at ru.java.ex01.SimpleLoggingDemo.log(SimpleLoggingDemo.java:24)
                at ru.java.ex01.SimpleLoggingDemo.main(SimpleLoggingDemo.java:10)
         */
    }
}
