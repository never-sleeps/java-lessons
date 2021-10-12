package ru.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * VM options: -Xmx256m -Xms256m -Xlog:gc=debug
 */
public class WeakMapDemo {
    private static final Logger logger = LoggerFactory.getLogger(WeakMapDemo.class);

    public static void main(String[] args) throws InterruptedException {
        new WeakMapDemo().startWithStringKey();
//        new WeakMapDemo().startWithIntegerKey();
    }

    /**
     * Демо удаления объектов из WeakHashMap после вызова gc.
     * Типа ключей - String
     */
    private void startWithStringKey() throws InterruptedException {
        Map<String, Integer> cache = new WeakHashMap<>();
        var limit = 100;
        for (var idx = 0; idx < limit; idx++) {
            cache.put("key:" + idx, idx);
        }

        logger.info("before gc: {}", cache.size()); // before gc: 100
        for (Map.Entry<String, Integer> element : cache.entrySet()) {
            logger.info("key:{}, value:{}", element.getKey(), element.getValue());
        }

        System.gc();
        Thread.sleep(100); // after gc: 0
        logger.info("after gc: {}", cache.size());

        for (Map.Entry<String, Integer> element : cache.entrySet()) {
            logger.info("key:{}, value:{}", element.getKey(), element.getValue());
        }
    }

    /**
     * Демо НЕудаления объектов из WeakHashMap после вызова gc.
     * Типа ключей - Integer
     *
     * В данном случае ключи не будут удалены из-за механизма кэширования значений Integer от -128 до + 127
     */
    private void startWithIntegerKey() throws InterruptedException {
        Map<Integer, Integer> cache = new WeakHashMap<>();
        var limit = 100;
        for (var idx = 0; idx < limit; idx++) {
            cache.put(idx, idx);
        }

        logger.info("before gc: {}", cache.size()); // before gc: 100
        for (Map.Entry<Integer, Integer> element : cache.entrySet()) {
            logger.info("key:{}, value:{}", element.getKey(), element.getValue());
        }

        System.gc();
        Thread.sleep(100);
        logger.info("\n\nafter gc: {}", cache.size()); // after gc: 100

        for (Map.Entry<Integer, Integer> element : cache.entrySet()) {
            logger.info("key:{}, value:{}", element.getKey(), element.getValue());
        }
    }
}
