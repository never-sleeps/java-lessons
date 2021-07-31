package ru.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MyIntMapTest {

    @Test
    void putAndGet() {
        MyIntMap map = new MyIntMap(1);
        int value = 88;
        String key = "key";

        map.put(key, value);
        assertEquals(value, map.get(key));
    }

    @Test
    void putAndGetSequence() {
        int size = 10;
        String keyStr = "k";
        MyIntMap map = new MyIntMap(size);

        for (int idx = 0; idx < size; idx++) {
            map.put(keyStr + idx, idx);
        }

        for (int idx = 0; idx < size; idx++) {
            assertEquals(idx, map.get(keyStr + idx));
        }
    }

    @Test
    void getNotExists() {
        MyIntMap map = new MyIntMap(1);
        String key = "key";

        assertThrows(IndexOutOfBoundsException.class, () -> map.get(key));
    }
}
