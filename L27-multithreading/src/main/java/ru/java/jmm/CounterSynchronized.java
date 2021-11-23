package ru.java.jmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Результаты работы будут верными.
 * Но на самом деле тут пропала многопоточность, поскольку из-за synchronized потоки работают по очереди
 */
public class CounterSynchronized {
    private static final Logger logger = LoggerFactory.getLogger(CounterSynchronized.class);
    private int count = 0; // volatile уже не нужен, поскольку Happens-before будет работать уже на основе монитора,
    // использовать для перестраховки его тоже не нужно, поскольку его использование сильно бьёт по перфомансу приложения.
    // "Кашу маслом" в данном случае испортишь

    private static final int LIMIT = 100_000_000;
    private final Object monitor = new Object();


    /** Ожидаемый результат = LIMIT * число потоков = 100_000_000 * 3 = 300_000_000 */
    public static void main(String[] args) throws InterruptedException {
        var counter = new CounterSynchronized();
        counter.go();
    }

/*
    // synchronized на весь метод
    // технически монитором в данном случае выступает this
    private synchronized void inc() {
        for (var idx = 0; idx < LIMIT; idx++) {
            count++;
        }
    }

    // synchronized на фрагмент кода, в скобках указывается объект, по которому будем синхронизироваться, то есть монитор
    // монитор - некий объект (желательно должен быть final)
    private void inc() {
        synchronized (this) {
            for (var idx = 0; idx < LIMIT; idx++) {
                count++;
            }
        }
    }
*/

    // synchronized на объект-монитор
    private void inc() {
        synchronized (monitor) {
            for (var idx = 0; idx < LIMIT; idx++) {
                count++;
            }
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

