package ru.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Демо создания неубиваемой ссылки на объект с помощью задания лгики в finalize()
 *
 * VM options: -Xmx256m -Xms256m -Xlog:gc=debug
 */
public class ImmortalDemo {
    private static final Logger logger = LoggerFactory.getLogger(ImmortalDemo.class);

    public static void main(String[] args) throws InterruptedException {
        var immortal1 = new Immortal(null);
        logger.info("immortal1 before nulling: {}", immortal1); // immortal1 before nulling: ru.java.references.ImmortalDemo$Immortal@41a4555e

        // обнуляем ссылку на объект и запускаем gc
        immortal1 = null;
        System.gc(); // finalize it
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        //как видим объект благополучно удален
        logger.info("immortal1 after nulling: {}", immortal1); // immortal1 after nulling: null

        ////---------------------------------------

        // задаём Consumer, поведение которого выполнится при вызове finalize для объекта Immortal
        Immortal[] backStore = {new Immortal(null)};
        Consumer<Immortal> revival = obj -> backStore[0] = obj;

        var immortalReal = new Immortal(revival);
        logger.info("immortal2 before nulling: {}", immortalReal); // immortal2 before nulling: ru.java.references.ImmortalDemo$Immortal@3b22cdd0

        // обнуляем ссылку на объект и запускаем gc
        immortalReal = null;
        System.gc(); // finalize it
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        immortalReal = backStore[0];

        //поскольку для Consumer'а заданы функция присваивания ссылки в backStore[0], объект воскрес
        logger.info("immortal2 after nulling:{}", immortalReal); // immortal after nulling:ru.java.references.ImmortalDemo$Immortal@3b22cdd0
    }

    static class Immortal {
        Consumer<Immortal> revival;

        public Immortal(Consumer<Immortal> revival) {
            this.revival = revival;
        }

        @Override
        protected void finalize() {
            logger.info("finalize it");
            if (revival != null) {
                revival.accept(this);
            }
        }
    }
}
