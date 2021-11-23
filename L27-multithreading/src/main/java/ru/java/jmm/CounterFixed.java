package ru.java.jmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Результаты работы будут верными. Работает медленно.
 *
 * Контракт классов Atomic гарантирует выполнение операции compare-and-set (cas) за «1 единицу процессорного времени».
 * При установке нового значения этой переменной вы также передаете ее старое значение (подход оптимистичной блокировки).
 * Если с момента вызова метода значение переменной отличается от ожидаемого — результатом выполнения будет false.
 *
 * Внутри Atomic на самом деле обёртка над базовым типом (внутри значение помечено volatile. например, private volatile int value;)
 * volatile обеспечивает видимость значения за пределами одного потока,
 * а cas-операцию обеспечивает метод в Unsafe, который обращается к конструкциям микропроцессора.
 *
 * Просадка по скорости происходит из-за большого числа "холостых" прогонов цикла.
 * Поток сохраняет старое значение count = 10, вычисляет его инкремент, проверяет равно ли ещё count старому значению 10,
 * если нет (например, оно теперь = 15), то повторяет этот оборот цикла заново: сохраняет старое значение count = 15, ...
 *
 */
public class CounterFixed {
    private static final Logger logger = LoggerFactory.getLogger(CounterFixed.class);

    private final AtomicInteger count = new AtomicInteger(0);
    private static final int LIMIT = 100_000_000;

    /** Ожидаемый результат = LIMIT * число потоков = 100_000_000 * 3 = 300_000_000 */
    public static void main(String[] args) throws InterruptedException {
        var counter = new CounterFixed();
        counter.go();
    }

    private void inc() {
        for (var idx = 0; idx < LIMIT; idx++) {
            count.incrementAndGet();
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

