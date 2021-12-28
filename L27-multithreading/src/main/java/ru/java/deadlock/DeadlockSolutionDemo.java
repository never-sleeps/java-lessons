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
public class DeadlockSolutionDemo {
    private static final Logger logger = LoggerFactory.getLogger(DeadlockSolutionDemo.class);

    private final Resource r1 = new Resource(1, "R1");
    private final Resource r2 = new Resource(2, "R2");

    public static void main(String[] args) {
        new DeadlockSolutionDemo().demo();
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

    // первым будет захвачен ресурс с наименьшим id
    private static void action(Resource has, Resource need) {
        Resource one = has.id > need.id ? has : need;
        Resource two = has.id > need.id ? need : has;
        logger.info("{} has: {}", Thread.currentThread().getName(), has);
        synchronized (one) {
            sleep();
            logger.info("{} taking: {}", Thread.currentThread().getName(), need);
            synchronized (two) {
                logger.info("taken by {}", Thread.currentThread().getName());
                someUsefullAction(has, need);
            }
        }
    }

    private static void someUsefullAction(Resource has, Resource need) {
        //
    }


    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Resource {
        private final int id;
        private final String name;

        Resource(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Resource{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
