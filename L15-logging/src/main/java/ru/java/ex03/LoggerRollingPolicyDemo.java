package ru.java.ex03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerRollingPolicyDemo {
  private static final Logger logger = LoggerFactory.getLogger("LoggerRollingPolicyExampleLogName");
  private long counter = 0;

  public static void main(String[] args) throws InterruptedException {
    new LoggerRollingPolicyDemo().loop();
  }

  private void loop() throws InterruptedException {
    while (!Thread.currentThread().isInterrupted()) {
      logger.info("message for file:{}", counter);
      counter++;

      Thread.sleep(10);
    }
  }
}
