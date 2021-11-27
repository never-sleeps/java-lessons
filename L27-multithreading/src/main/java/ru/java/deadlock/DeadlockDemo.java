package ru.java.deadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Dead lock случается, когда:
 * поток T1 удерживает ресурс R1, и хочет получить ресурс R2
 * поток T2 удерживает ресурс R2, и хочет получить ресурс R1.
 *
 * Потоки ждут друг друга, система не совершает прогресс.
 *
 *
 * Решением проблемы дедлоков является соблюдение порядка захвата ресурсов:
 * Ресурсы должны быть отсортированы по некоторому показателю, порядок захвата должен соответствовать порядку сортировки
 */
public class DeadlockDemo {
    private static final Logger logger = LoggerFactory.getLogger(DeadlockDemo.class);

    private final Resource r1 = new Resource("R1");
    private final Resource r2 = new Resource("R2");

    public static void main(String[] args) {
        new DeadlockDemo().demo();
    }

    private void demo() {
        var t1 = new Thread(() -> action(r1, r2));
        t1.setName("t1");

        var t2 = new Thread(() -> action(r2, r1));
        t2.setName("t2");

        t1.start();
        t2.start();


        sleep();
        sleep();
        logger.info("findDeadlockedThreads");
        long[] threads = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
        if (threads != null) {
            ThreadInfo[] threadInfo = ManagementFactory.getThreadMXBean().getThreadInfo(threads);
            logger.info("blocked threads:{}", Arrays.toString(threadInfo));
        }
    }


    private static void action(Resource has, Resource need) {
        logger.info("{} has: {}", Thread.currentThread().getName(), has);
        synchronized (has) {
            sleep();
            logger.info("{} taking: {}", Thread.currentThread().getName(), need);
            synchronized (need) {
                logger.info("taken by {}", Thread.currentThread().getName());
            }
        }
    }


    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Resource {
        private final String name;

        Resource(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Resource{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
