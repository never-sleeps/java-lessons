package ru.java.concurrent.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Задача:  FIX with CopyOnWriteArrayList
 * Решение: Использовать CopyOnWriteArrayList вместо обычного ArrayList
 *
 * Время работы: 448 ms
 */
class Fix3WithCopyOnWriteArrayListUnitTest {

    private static final int ITERATIONS_COUNT = 1000 / 2;

    /**
     * Тест организован так: Есть некий список list и два потока t1 и t2.
     * Один поток (t1) в этот список будет писать, другой поток (t2) будет этот список читать.
     * Ошибки, случившиеся во время выполнения, складываются в throwables. Этот список по окончании теста и оценивается.
     */
    @Test
    void testCopyOnWriteArrayListWorksGreat() throws InterruptedException {

        final List<String> list = new CopyOnWriteArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    System.out.println("starting adding email " + i);
                    list.add(randomAlphabetic(10) + "@gmail.com");
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
                    for (String email : list) {
                        System.out.println(email);
                    }
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
