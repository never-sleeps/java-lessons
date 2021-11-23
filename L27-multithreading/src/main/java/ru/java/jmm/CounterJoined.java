package ru.java.jmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Результаты работы будут верными. Работает быстро.
 *
 * Запускает три потока, каждому из которых даём свою отдельную задачу посчитать в свою переменную.
 * Когда все они завершат работу, складываем полученный результат в единую переменную
 *
 * Happens-before тут проявляется при вызове join() у потоков
 */
public class CounterJoined {
    private static final Logger logger = LoggerFactory.getLogger(CounterJoined.class);
    private static final int LIMIT = 100_000_000;
    private int countPart1 = 0;
    private int countPart2 = 0;
    private int countPart3 = 0;

    /** Ожидаемый результат = LIMIT * число потоков = 100_000_000 * 3 = 300_000_000 */
    public static void main(String[] args) throws InterruptedException {
        var counter = new CounterJoined();
        counter.go();
    }

    private int inc() {
        var result = 0;
        for (var idx = 0; idx < LIMIT; idx++) {
            result++;
        }
        return result;
    }

    private void go() throws InterruptedException {
        var thread1 = new Thread(() -> countPart1 = inc());
        var thread2 = new Thread(() -> countPart2 = inc());
        var thread3 = new Thread(() -> countPart3 = inc());

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        int count = countPart1 + countPart2 + countPart3;

        logger.info("CounterBroken:{}", count);
    }
}

