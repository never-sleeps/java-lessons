package ru.java.concurrent.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Задача:  FIX with BlockingQueue
 * Решение: Использовать ConcurrentHashMap вместо обычного List
 *
 * Время работы: 46 ms
 */
class Fix5WithBlockingQueueTest {

    private static final int ITERATIONS_COUNT = 1000 / 2;

    /**
     * Тест организован так: Есть некий blockingQueue и два потока t1 и t2.
     * Один поток (t1) в этот blockingQueue будет писать, другой поток (t2) будет этот blockingQueue читать.
     * Ошибки, случившиеся во время выполнения, складываются в throwables. Этот список по окончании теста и оценивается.
     */
    @Test
    void testBlockingQueueWorksGreat() throws InterruptedException {

        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    blockingQueue.take(); // Блокирующий метод. Извлекает и удаляет элемент из головы очереди. Если очередь пустая блокирует выполнение до появления элемента
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    blockingQueue.put(i);
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
