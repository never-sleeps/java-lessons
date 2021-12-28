package ru.java.concurrent.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Задача:  FIX with ConcurrentHashMap
 * Решение: Использовать ConcurrentHashMap вместо обычного HashMap
 *
 * Время работы: 540 ms
 */
class Fix4WithConcurrentHashMapUnitTest {

    private static final int ITERATIONS_COUNT = 1000 / 2;

    /**
     * Тест организован так: Есть некий map и два потока t1 и t2.
     * Один поток (t1) в этот map будет писать, другой поток (t2) будет этот map читать.
     * Ошибки, случившиеся во время выполнения, складываются в throwables. Этот список по окончании теста и оценивается.
     */
    @Test
    void testConcurrentHashMapWorksGreat() throws InterruptedException {

        final Map<String, String> map = new ConcurrentHashMap<>();
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    System.out.println("starting adding email " + i);
                    String s = randomAlphabetic(10) + "@gmail.com";
                    map.put(s, s);
                    System.out.println("finishing adding email " + i);
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    System.out.println("starting read iteration " + i);
                    map.forEach((k, v) -> System.out.println(k));
                    System.out.println("finishing read iteration " + i);
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });

        t1.start();
        t2.start();

        latch.countDown();

        t1.join();
        t2.join();

        assertThat(throwables).withFailMessage(throwables.toString()).isEmpty();

    }
}
