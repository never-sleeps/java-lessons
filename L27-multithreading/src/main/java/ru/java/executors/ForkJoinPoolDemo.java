package ru.java.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool хорошо подходит для автоматизации распараллеливания задач.
 * Используется внутри параллельных стримов и в CompletableFuture
 */
public class ForkJoinPoolDemo {
    private static final Logger logger = LoggerFactory.getLogger(ForkJoinPoolDemo.class);

    public static void main(String[] args) {
        new ForkJoinPoolDemo().go();
    }

    private void go() {
        var forkJoinPool = new ForkJoinPool(); // количество потоков в ForkJoinPool'е будет зависеть от количества ядер

        TaskSumInt taskSumInt = new TaskSumInt(new int[]{1, 2, 3, 4, 5, 6, 7}); // задача = просуммировать массив чисел
        var result = forkJoinPool.invoke(taskSumInt);
        logger.info("result:{}", result);
    }

    public static class TaskSumInt extends RecursiveTask<Integer> {
        private final int[] data;

        TaskSumInt(int[] data) {
            this.data = data;
            logger.info("data:{}", data);
        }

        // вычисление будет производиться по принципу merge sort, использует рекурсивный алгоритм
        @Override
        protected Integer compute() {
            if (data.length > 1) {
                var taskL = new TaskSumInt(Arrays.copyOfRange(data, 0, data.length / 2));
                taskL.fork(); // запуск механизма ещё раз

                var taskR = new TaskSumInt(Arrays.copyOfRange(data, data.length / 2, data.length));
                taskR.fork();

                var result = taskL.join();
                result += taskR.join();
                return result;
            } else {
                return data[0];
            }
        }
    }
}
/*
[main] INFO ru.java.executors.ForkJoinPoolDemo - data:[1, 2, 3, 4, 5, 6, 7]
[ForkJoinPool-1-worker-1] INFO ru.java.executors.ForkJoinPoolDemo - data:[1, 2, 3]
[ForkJoinPool-1-worker-1] INFO ru.java.executors.ForkJoinPoolDemo - data:[4, 5, 6, 7]
[ForkJoinPool-1-worker-2] INFO ru.java.executors.ForkJoinPoolDemo - data:[1]
[ForkJoinPool-1-worker-3] INFO ru.java.executors.ForkJoinPoolDemo - data:[4, 5]
[ForkJoinPool-1-worker-2] INFO ru.java.executors.ForkJoinPoolDemo - data:[2, 3]
[ForkJoinPool-1-worker-1] INFO ru.java.executors.ForkJoinPoolDemo - data:[4]
[ForkJoinPool-1-worker-3] INFO ru.java.executors.ForkJoinPoolDemo - data:[6, 7]
[ForkJoinPool-1-worker-5] INFO ru.java.executors.ForkJoinPoolDemo - data:[2]
[ForkJoinPool-1-worker-6] INFO ru.java.executors.ForkJoinPoolDemo - data:[6]
[ForkJoinPool-1-worker-1] INFO ru.java.executors.ForkJoinPoolDemo - data:[5]
[ForkJoinPool-1-worker-6] INFO ru.java.executors.ForkJoinPoolDemo - data:[7]
[ForkJoinPool-1-worker-5] INFO ru.java.executors.ForkJoinPoolDemo - data:[3]
 */
