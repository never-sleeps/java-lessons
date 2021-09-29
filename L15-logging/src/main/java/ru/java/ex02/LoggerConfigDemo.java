package ru.java.ex02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerConfigDemo {
    private static final Logger logger = LoggerFactory.getLogger(LoggerConfigDemo.class);
    private long counter = 0;

    public static void main(String[] args) throws InterruptedException {
        new LoggerConfigDemo().loop();
    }

    private void loop() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            logger.info("info level:{}", counter);
            logger.error("error level:{}", counter);
            counter++;
            Thread.sleep(3_000);
        }
    }
}
