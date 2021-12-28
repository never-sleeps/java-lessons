package ru.java.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Результаты работы будут разниться и будут неверными:
 * поскольку happens-before происходит только при захвате и освобождении ОДНОГО И ТОГО ЖЕ же монитора,
 * а в примере у каждого из потока свой монитор.
 *
 * Предпочтительнее выбрать синхронизацию по this
 */
public class MonitorDemo {
    private static final Logger logger = LoggerFactory.getLogger(MonitorDemo.class);

    private int count = 0;
    private static final int LIMIT = 100_000_000;
    private final Object monitor = new Object();

    /** Ожидаемый результат = LIMIT * число потоков = 100_000_000 * 3 = 300_000_000 */
    public static void main(String[] args) throws InterruptedException {
        var counter = new MonitorDemo();
        counter.go();
    }

    // монитор объект final Object monitor = new Object();
    private void inc1() {
        synchronized (monitor) {
            for (int i = 0; i < LIMIT; i++) {
                count++;
            }
        }
    }

    // из-за использование static монитором будет MonitorDemo.class, будет применяться для всех объектов этого класса
    private static synchronized void inc2(MonitorDemo demo) {
        for (int i = 0; i < LIMIT; i++) {
            demo.count++;
        }
    }

    // монитором будет this
    private synchronized void inc3() {
        for (int i = 0; i < LIMIT; i++) {
            count++;
        }
    }

    private void go() throws InterruptedException {
        Thread thread1 = new Thread(this::inc1);
        Thread thread2 = new Thread(() -> inc2(this));
        Thread thread3 = new Thread(this::inc3);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
        logger.info("CounterBroken: {}", count);
    }
}
