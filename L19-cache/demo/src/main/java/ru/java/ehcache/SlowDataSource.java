package ru.java.ehcache;

import java.util.concurrent.TimeUnit;


class SlowDataSource {

    private SlowDataSource() {
    }

    static long getValue(int key) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return key;
    }
}
