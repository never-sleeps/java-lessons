package ru.java.concurrent.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Задача:  FIX with monitor
 * Решение: Добавить синхронизацию по списку внутрь циклов чтения и  записи (synchronized (list)).
 *
 * Время работы: 671 ms
 */
class Fix1WithMonitorUnitTest {

    private static final int ITERATIONS_COUNT = 1000 / 2;

    /**
     * Тест организован так: Есть некий список list и два потока t1 и t2.
     * Один поток (t1) в этот список будет писать, другой поток (t2) будет этот список читать.
     * Ошибки, случившиеся во время выполнения, складываются в throwables. Этот список по окончании теста и оценивается.
     */
    @Test
    void testMonitorWorksGreat() throws InterruptedException {

        final List<String> list = new ArrayList<>();

        // 1 - число, которое нужно вызывать метод countDown, чтобы потоки, которые вызвали метод await(), разблокировались
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    synchronized (list) {
                        System.out.println("starting adding email " + i);
                        list.add(randomAlphabetic(10) + "@gmail.com");
                        System.out.println("finishing adding email " + i);
                    }
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    synchronized (list) {
                        System.out.println("starting read iteration " + i);
                        list.forEach(System.out::println);
                        System.out.println("finishing read iteration " + i);
                    }
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });

        t1.start();
        t2.start();

        // к этому моменту потоки будут созданы и запущены, но на самом деле работать не будут, поскольку они будут на блокировке в latch.await();
        // потоки начну работу после вызова latch.countDown(); Используется для запуска нескольких потоков одновременно

        latch.countDown();

        // ждём окончания работы потоков
        t1.join();
        t2.join();

        // к этому моменту потоки будут завершены
        assertThat(throwables).withFailMessage(throwables.toString()).isEmpty();
    }
}
