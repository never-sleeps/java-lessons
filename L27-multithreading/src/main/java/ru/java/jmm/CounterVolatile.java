package ru.java.jmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Работа очень медленная из-за применения volatile.
 * Результаты работы будут разниться и будут неверными, поскольку операция count++; неатомарна:
 * поток 1 читает из оперативной памяти count = 0
 * поток 2 читает из оперативной памяти значение count = 0
 * поток 1 записывает в оперативную память count++ (результат count = 1)
 * поток 2 записывает в оперативную память count++ (результат тоже count = 1, поскольку поток 2 не знает о том, что значение count уже было изменено другим потоком)
 * ...
 *
 * JОдин из вариантов решения: заменить private volatile int count = 0; на private AtomicInteger count = new AtomicInteger(0);
 */
public class CounterVolatile {
    private static final Logger logger = LoggerFactory.getLogger(CounterVolatile.class);
    private volatile int count = 0; // значение volatile переменной будет видно из разных потоков.
    // Применяется в неблокирующих операциях, когда один поток изменяет, другой пишет. Когда изменяют оба потока, нужна синхронизация

    private static final int LIMIT = 100_000_000;

    /** Ожидаемый результат = LIMIT * число потоков = 100_000_000 * 3 = 300_000_000 */
    public static void main(String[] args) throws InterruptedException {
        var counter = new CounterVolatile();
        counter.go();
    }

    private void inc() {
        for (var idx = 0; idx < LIMIT; idx++) {
            count++;
        }
    }

    private void go() throws InterruptedException {
        var thread1 = new Thread(this::inc);
        var thread2 = new Thread(this::inc);
        var thread3 = new Thread(this::inc);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        logger.info("CounterBroken:{}", count);
    }
}

