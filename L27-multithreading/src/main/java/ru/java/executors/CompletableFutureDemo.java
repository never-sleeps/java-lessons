package ru.java.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture  - функционал асинхронных вызовов.
 * Идеально подходит для "пользовательской многозадачности".
 */
public class CompletableFutureDemo {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        simpleAsync();
//        asyncThen();
//        asyncError();
//        asyncAcceptBoth();
        asyncAcceptEither();
    }

    // принцип работы в данном примере похож на Executors.newSingleThreadExecutor
    private static void simpleAsync() throws ExecutionException, InterruptedException {
        logger.info("start");
        var future1 = CompletableFuture.supplyAsync(() -> task(1));
        logger.info("thread is not blocked");
        logger.info("result:{}", future1.get());
    }

    // пример линейной записи асинхронных действий
    private static void asyncThen() {
        logger.info("start");
        var future2 = CompletableFuture.supplyAsync(() -> task(2));
        // через thenAccept указываем, что вторая задача (тут - логирование) будет выполнена только поле того,
        // как выполнится и вернёт результат первая задача
        future2.thenAccept(val -> logger.info("result:{}", val)); // val - результат работы первой задачи
        future2.join();
    }

    // пример работы, когда при выполнении задачи возникает Exception
    private static void asyncError() {
        var future3 = CompletableFuture.supplyAsync(CompletableFutureDemo::throwRuntimeException);
        future3.exceptionally(Throwable::getMessage) // exceptionally - получит случившееся исключения
                .thenAccept(msg -> logger.info("msg:{}", msg)); // через thenAccept исключение будет обработано
    }

    // пример, когда нам нужны результаты выполнения обеих задач
    private static void asyncAcceptBoth() {
        logger.info("start");
        var futureT1 = CompletableFuture.supplyAsync(() -> task(100));
        var futureT2 = CompletableFuture.supplyAsync(() -> task(200));
        var joinedResult = futureT1.thenAcceptBoth(
                futureT2, (s1, s2) -> logger.info("joinedResult: {}, {}", s1, s2)
        ); // joinedResult: done100, done200
        joinedResult.join();
    }

    // пример, когда нам нужны результаты любого первого отработавшего
    // (может применяться, когда нам нужны результаты от любого первого ответившего сервиса, который вызываются каждый в своей задаче)
    private static void asyncAcceptEither() {
        logger.info("start");
        var futureT1 = CompletableFuture.supplyAsync(() -> {
                    sleep(1); // добавлено, чтобы в данном примере первый поток проиграл по скорости
                    return task(100);
                }
        );
        var futureT2 = CompletableFuture.supplyAsync(() -> task(200));
        var firstResult = futureT1.acceptEither(futureT2, s -> logger.info("firstResult: {}", s)); // firstResult: done200
        firstResult.join();
    }

    private static String throwRuntimeException() {
        throw new RuntimeException("error for Test");
    }

    private static String task(int id) {
        sleep(5);
        logger.info("task is done: {}", id);
        return "done" + id;
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
