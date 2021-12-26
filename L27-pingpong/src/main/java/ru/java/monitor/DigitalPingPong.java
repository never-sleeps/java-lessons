package ru.java.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Два потока печатают числа от 1 до 10, потом от 10 до 1.
 * Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
 * Поток 1: 1  2  3  4  5  6  7  8  9  10    9  8  7   6  5  4  3   2  1  2   3  4....
 * Поток 2:   1  2  3  4  5  6  7  8  9    10  9  8  7  6   5   4  3   2  1  2   3....
 * Всегда должен начинать Поток 1.
 */
public class DigitalPingPong {
    private static final Logger logger = LoggerFactory.getLogger(DigitalPingPong.class);

    private static final String THREAD_NAME_1 = "t1";
    private static final String THREAD_NAME_2 = "t2";

    private final Iterator<Integer> t1NumberSequence;
    private final Iterator<Integer> t2NumberSequence;

    private String lastThreadName;

    public DigitalPingPong() {
        this.t1NumberSequence = generateNumberSequence();
        this.t2NumberSequence = generateNumberSequence();
    }

    public static void main(String[] args) {
        DigitalPingPong pingPong = new DigitalPingPong();

        pingPong.lastThreadName = THREAD_NAME_2; // таким образом фиксируем, чтобы первым начал работу t1
        Thread t1 = new Thread(pingPong::action);
        Thread t2 = new Thread(pingPong::action);

        t1.setName(THREAD_NAME_1);
        t2.setName(THREAD_NAME_2);
        t1.start();
        t2.start();
    }

    private synchronized void action() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThreadName.equals(Thread.currentThread().getName())) {
                    this.wait();
                }

                lastThreadName = Thread.currentThread().getName();

                if (lastThreadName.equals(THREAD_NAME_1)) {
                    print(t1NumberSequence);
                } else {
                    print(t2NumberSequence);
                }

                sleep((lastThreadName.equals(THREAD_NAME_1)) ? 500 : 400);
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void print(Iterator<Integer> numberSequence) {
        if (numberSequence.hasNext()) {
            var valueForPrint = numberSequence.next();
            logger.info(String.valueOf(valueForPrint));
        } else Thread.currentThread().interrupt();
    }

    private Iterator<Integer> generateNumberSequence() {
        List<Integer> list = new ArrayList<>(20);
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        for (int i = 9; i >= 1; i--) {
            list.add(i);
        }
        return list.iterator();
    }

    private static void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
