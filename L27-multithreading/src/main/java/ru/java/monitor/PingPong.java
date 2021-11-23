package ru.java.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);

    // переменная для хранения последнего значения/состояния.
    // Благодаря ей потоки смогут однозначно понимать чья сейчас очередь работы
    private String lastState; // volatile не нужен, поскольку happens-before в данном примере организовано с помощью synchronized

    /**
     * Благодаря synchronized метод организован как критическая секция (одновременно работать в нём может только один поток)
     * @param message - текст для печати
     */
    private synchronized void action(String message) {
        // while(true)) в таких случаях плохое решение,
        // поскольку не позволяет корректно остановить приложение (завершить транзакцию в бд, закрыть файловый дескриптор и т.д.)
        while(!Thread.currentThread().isInterrupted()) {
            try {
                // почему не if: редко, но всё-таки возможно, что поток может самопроизвольно проснуться,
                // эта особенность связана с реализацией железа (spurious wakeup - https://en.wikipedia.org/wiki/Spurious_wakeup)
                while (lastState.equals(message)) { // условие нужно для обеспечения очерёдности работы потоков
                    // на втором заходе в общий цикл поток видит, что последнему состоянию не равно его сообщению,
                    // попадает в этот цикл и засыпает
                    this.wait();
                }

                logger.info(message); // вывод сообщения
                lastState = message; // сохранение выведенного сообщения в переменную хранения последнего состояния
                sleep(1_000); // чисто для наглядности замедляем работу
                notifyAll(); // поток передаёт бразды правления следующему потоку
                logger.info("after notify");
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        pingPong.lastState = "pong"; // таким образом фиксируем, чтобы первым начал работу ping
        Thread pingThread = new Thread(() -> pingPong.action("ping"));
        Thread pongThread = new Thread(() -> pingPong.action("pong"));

        pingThread.setName("ping thread");
        pongThread.setName("pong thread");
        pingThread.start();
        pongThread.start();
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
